# re(pression's) Telegram Messenger for Android

A slightly customized version of the official Telegram app.

**⚠️ WARNING: This app violates Telegram's terms of service. It may stop working at any time.**

## Features

1. **Ghost (Offline) Mode**  
   Stay hidden—no one will see when you're online.

2. **No-Reading Mode**  
   Read messages without notifying the sender.

3. **No-Typing Mode**  
   Type your messages without showing the "typing..." indicator.

4. **Secret Screenshot**  
   Take screenshots in secret chats without detection.

5. **Bypass Screenshot Restrictions**  
   Capture screenshots and download content from channels where these actions are restricted.

6. **Additional Enhancements**  
   Enjoy a few other small features that make this app even better.

**Note:** Ghost mode is only functional when both No-Reading and No-Typing modes are enabled.

## Building from Source

1. [Obtain your own API ID and Hash](https://core.telegram.org/api/obtaining_api_id) from Telegram.

2. Configure your `local.properties` file as follows:
    ```plaintext
    RELEASE_KEY_PASSWORD=<your_password>
    RELEASE_KEY_ALIAS=<your_alias>
    RELEASE_STORE_PASSWORD=<your_password>
    APP_ID=<your_app_id>
    APP_HASH=<your_app_hash>
    ```

3. Build the app using Android Studio.