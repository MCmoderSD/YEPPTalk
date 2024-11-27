# YEPPTalk

## Description

YEPPTalk is an AI Assistant that uses the OpenAI API to talk to you like an Alexa or Google Assistant.
It's meant as a demonstration of my [OpenAI-Utility](https://github.com/MCmoderSD/OpenAI-Utility).

## Usage

You can get the API key from [OpenAI](https://platform.openai.com/signup). <br>
You have to provide a ``config.json`` in file in the ``resources`` folder with the following content:
```json
{
  "user": "YEPPTalk",
  "apiKey": "YOUR_API_KEY",

  "chat": {
    "model": "gpt-4o-2024-11-20",
    "maxConversationCalls": 10,
    "maxTokenSpendingLimit": 8192,
    "temperature": 1,
    "maxOutputTokens": 120,
    "topP": 1,
    "frequencyPenalty": 0,
    "presencePenalty": 0,
    "instruction": "You are an TwitchBot called the YEPPBot. You express yourself like a funny/edgy twitch user. You always like use the YEPP emote in your sentences and especially at the end. You don't use emojis just common twitch emote and especially the YEPP."
  },

  "transcription": {
    "model": "whisper-1",
    "prompt": "Transcribe the following audio file to German.",
    "language": "de",
    "temperature": 1
  },

  "speech": {
    "model": "tts-1",
    "voice": "onyx",
    "speed": 1,
    "format": "wav"
  }
}
```

Or download the [latest version](https://github.com/MCmoderSD/YEPPTalk/releases/latest) and run the bot with the following command line arguments:
```shell
java -jar YEPPTalk.jar "PATH_TO_CONFIG.JSON"
```