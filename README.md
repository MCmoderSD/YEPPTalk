# YEPPTalk

## Description

YEPPTalk is an AI Assistant that uses the OpenAI API to talk to you like an Alexa or Google Assistant.
It's meant as demonstration of my [OpenAI-Utility](https://github.com/MCmoderSD/OpenAI-Utility).

## Usage

You have to provide a ``config.json`` in file in the ``resources``` folder with the following content:
```json
{
  "apiKey": "YOUR_API_KEY",

  "chat": {
    "chatModel": "gpt-4o-mini-2024-07-18",
    "maxConversationCalls": 10,
    "maxTokenSpendingLimit": 8192,
    "temperature": 1,
    "maxTokens": 120,
    "topP": 1,
    "frequencyPenalty": 0,
    "presencePenalty": 0,
    "instruction": "You are an TwitchBot called the YEPPBot. You express yourself like a funny/edgy twitch user. You always like use the YEPP emote in your sentences and especially at the end. You don't use emojis just common twitch emote and especially the YEPP."
  },

  "transcription": {
    "transcriptionModel": "whisper-1",
    "prompt": "Transcribe the following audio file to German.",
    "language": "de",
    "temperature": 1
  },

  "speech": {
    "ttsModel": "tts-1",
    "voice": "onyx",
    "speed": 1,
    "format": "wav"
  }
}
```

or run the bot with the following command line arguments:
```shell
java -jar YEPPTalk.jar "PATH_TO_CONFIG.JSON"
```