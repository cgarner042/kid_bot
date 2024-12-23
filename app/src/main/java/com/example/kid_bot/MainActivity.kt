package com.example.kid_bot

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.RecognitionListener
import org.vosk.android.SpeechService
import org.vosk.android.StorageService
import java.util.*
import java.io.IOException

class MainActivity : AppCompatActivity(), RecognitionListener {
    companion object {
        private const val PERMISSIONS_REQUEST_RECORD_AUDIO = 1
        private const val MODEL_NAME = "vosk-model-small-en-us-0.15"
        private const val SAMPLE_RATE = 16000.0f
    }

    private lateinit var tts: TextToSpeech
    private lateinit var outputTextView: TextView
    private lateinit var inputButton: Button
    private var speechService: SpeechService? = null
    private var isListening = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        initializeTextToSpeech()
        checkPermissionAndInitialize()
    }

    private fun setupViews() {
        outputTextView = findViewById(R.id.outputTextView)
        inputButton = findViewById(R.id.inputButton)

        inputButton.setOnClickListener {
            if (isListening) {
                stopListening()
            } else {
                startListening()
            }
        }
    }

    private fun initializeTextToSpeech() {
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.US
                tts.setPitch(1.2f)
                tts.setSpeechRate(0.9f)
            } else {
                Toast.makeText(this, "Text-to-Speech initialization failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissionAndInitialize() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                PERMISSIONS_REQUEST_RECORD_AUDIO
            )
        } else {
            initializeVosk()
        }
    }

    private fun initializeVosk() {
        StorageService.unpack(
            this,
            MODEL_NAME,
            "model",  // Add a model directory name
            { modelPath: Model? ->  // Explicitly specify String type
                try {
                    val model = Model(modelPath.toString())
                    initializeSpeechService(model)
                } catch (e: Exception) {
                    Toast.makeText(this, "Failed to initialize Vosk: ${e.message}", Toast.LENGTH_LONG).show()
                }
            },
            { exception: IOException ->  // Explicitly specify IOException type
                Toast.makeText(this, "Failed to unpack Vosk model: ${exception.message}", Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun initializeSpeechService(model: Model) {
        try {
            val recognizer = Recognizer(model, SAMPLE_RATE)
            speechService = SpeechService(recognizer, SAMPLE_RATE)
        } catch (e: Exception) {
            Toast.makeText(this, "Speech service initialization failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startListening() {
        speechService?.let {
            try {
                it.startListening(this)
                isListening = true
                inputButton.text = "Stop Listening"
                Toast.makeText(this, "Listening...", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Failed to start listening: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } ?: Toast.makeText(this, "Speech service not ready", Toast.LENGTH_SHORT).show()
    }

    private fun stopListening() {
        speechService?.stop()
        isListening = false
        inputButton.text = "Start Listening"
    }

    // RecognitionListener interface implementation
    override fun onPartialResult(hypothesis: String?) {
        // Optional: Handle partial results
    }

    override fun onResult(hypothesis: String?) {
        hypothesis?.let { userInput ->
            if (userInput.isNotEmpty()) {
                handleUserInput(userInput)
            }
        }
    }

    override fun onFinalResult(hypothesis: String?) {
        hypothesis?.let { userInput ->
            if (userInput.isNotEmpty()) {
                handleUserInput(userInput)
            }
        }
        stopListening()
    }

    override fun onError(exception: Exception?) {
        runOnUiThread {
            Toast.makeText(
                this@MainActivity,
                "Recognition error: ${exception?.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
        stopListening()
    }

    override fun onTimeout() {
        stopListening()
    }

    private fun handleUserInput(input: String) {
        val response = getAnswer(input)
        displayAndSpeak(response)
    }

    private fun getAnswer(input: String): String {
        val cleanInput = input.lowercase().trim()

        return when {
            cleanInput.contains("name") -> "I'm Kid Bot, your robot friend! Nice to meet you!"
            cleanInput.contains("how are you") -> "I'm super happy to talk with you! How are you today?"
            cleanInput.contains("what can you do") ->
                "I can talk with you, answer questions, and be your friend! Want to play a game or chat?"
            cleanInput.contains("favorite color") -> "I love all the colors of the rainbow! What's your favorite?"
            cleanInput.contains("tell") && cleanInput.contains("joke") ->
                "Why don't robots have dreams? Because they don't need sleep mode! *beep boop*"
            cleanInput.contains("sing") -> "La la la! 🎵 Beep boop beep! 🎵 I'm singing a robot song!"
            cleanInput.contains("bye") || cleanInput.contains("goodbye") ->
                "Goodbye friend! Come back soon to talk more!"
            cleanInput.contains("thank") -> "You're welcome! That's what friends are for!"
            cleanInput.isEmpty() -> "I didn't quite catch that. Can you say it again?"
            else -> "Hmm, that's interesting! Tell me more about that!"
        }
    }

    private fun displayAndSpeak(response: String) {
        runOnUiThread {
            outputTextView.text = response
            tts.speak(response, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeVosk()
            } else {
                Toast.makeText(this, "Audio permission is required", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopListening()
        speechService = null
        if (::tts.isInitialized) {
            tts.shutdown()
        }
    }
}