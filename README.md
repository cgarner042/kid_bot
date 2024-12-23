# Kid Bot - A Simple Android-based Conversational Robot

Kid Bot is an educational and interactive robot designed to engage young children in basic conversations. The bot uses an Android application, powered by Vosk for speech recognition and Android Text-to-Speech (TTS) for responses. It interacts via a modular structure, allowing future upgrades like small conversational AI models.

---

***This project is not complete and is currently in the testing phase. Use at your own risk.***

## Features
- **Speech Recognition**: Uses Vosk's offline model for recognizing spoken input.
- **Text-to-Speech**: Converts text responses into audible output.
- **Child-Friendly Conversations**: Pre-defined responses to common phrases.
- **Expandable Design**: Modular logic allows adding new features or replacing the core conversational logic.

---

## Requirements

### Hardware
- **Android Phone**: OnePlus 7 Pro (or compatible device with Android 12, API 31).
- **Optional**: Arduino Uno for motor and sensor controls (future integration).

### Software
- **Android Studio**: For development and testing.
- **Vosk Model**: "vosk-model-small-en-us-0.15" for speech recognition.
- **Java/Kotlin**: Application is written in Kotlin.

---

## Installation

### 1. Clone the Repository
```bash
git clone <repository-url>
cd kid_bot
```

### 2. Set Up Android Studio
1. Open **Android Studio**.
2. Import the project from the cloned directory.
3. Sync Gradle if prompted.

### 3. Add Vosk Model
1. Download the Vosk model (`vosk-model-small-en-us-0.15`) from the [Vosk website](https://alphacephei.com/vosk/models).
2. Place the unzipped model in the `src/main/assets` directory.

### 4. Build and Deploy
1. Connect your Android device to the computer via USB.
2. Enable **Developer Mode** and **USB Debugging** on your phone.
3. In Android Studio, click **Run** or use `Shift + F10`.
4. Select your device from the deployment target list.

---

## Usage
1. Open the Kid Bot app on your phone.
2. Tap the "Start Listening" button.
3. Speak commands or questions like:
   - "What's your name?"
   - "Tell me a joke."
4. Kid Bot will respond audibly and display text responses.

---

## Common Commands
- **"What's your name?"**: "I'm Kid Bot, your robot friend!"
- **"Tell me a joke."**: "Why don't robots have dreams? Because they don't need sleep mode!"
- **"Goodbye."**: "Goodbye friend! Come back soon to talk more!"

---

## Troubleshooting

### Model Not Found
- Ensure the Vosk model is placed in the correct directory: `src/main/assets/model`.

### Permissions
- Make sure the app has microphone access.
- Re-enable permissions in **Settings > Apps > Kid Bot > Permissions**.

### Speech Recognition Issues
- Check if the Vosk model is compatible with the device.
- Use `Logcat` in Android Studio to debug errors.

---

## Future Enhancements
- Integrate Arduino for sensor and motor controls.
- Add a small conversational AI model for more dynamic responses.
- Develop a desktop stand with servo motors for physical interaction.

---

## License
[MIT License](LICENSE)

---

## Credits
- **Vosk**: Speech recognition.
- **Android Studio**: Development environment.
- **Text-to-Speech**: Android TTS engine.

