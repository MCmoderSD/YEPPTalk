package de.MCmoderSD;

import de.MCmoderSD.JavaAudioLibrary.AudioFile;
import de.MCmoderSD.JavaAudioLibrary.AudioRecorder;
import de.MCmoderSD.OpenAI.OpenAI;
import de.MCmoderSD.OpenAI.modules.Chat;
import de.MCmoderSD.OpenAI.modules.Speech;
import de.MCmoderSD.OpenAI.modules.Transcription;

import de.MCmoderSD.json.JsonUtility;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Assistant {

    // Constants
    public final static String BOLD = "\033[0;1m";
    public final static String UNBOLD = "\u001B[0m";

    // Associations
    private final OpenAI openAI;
    private final Chat chat;
    private final Speech speech;
    private final Transcription transcription;

    // Constructor
    public Assistant(String configPath, boolean absolutePath) throws IOException, URISyntaxException {

        // Initialize OpenAI and modules
        openAI = new OpenAI(JsonUtility.loadJson(configPath, absolutePath));
        chat = openAI.getChat();
        speech = openAI.getSpeech();
        transcription = openAI.getTranscription();

        // Loop
        new Thread(this::loop).start();
    }

    private void loop() {

        // Variables
        var id = 1;
        Scanner scanner = new Scanner(System.in);
        AudioRecorder recorder = new AudioRecorder();
        BigDecimal totalCost = new BigDecimal(0);

        // Loop
        while (openAI.isActive()) {

            // Wait for user input
            System.out.println(BOLD + "Press enter to start recording");
            scanner.nextLine();

            // Start recording
            recorder.startRecording();
            System.out.println("Recording... Press enter to stop" + UNBOLD);

            // Wait for user input
            scanner.nextLine();

            // Stop and get audio file
            recorder.stopRecording();
            AudioFile userAudio = recorder.getAudioFile();

            // Transcribe audio
            String userInput = transcription.transcribe(userAudio);

            // Print user input
            System.out.println("User Input: ");
            System.out.println(userInput);

            // Calculate cost
            BigDecimal transcriptionCost = transcription.calculatePrice(userAudio);
            System.out.println("\nTranscription Cost: $" + transcriptionCost.setScale(4, RoundingMode.HALF_UP));
            totalCost = totalCost.add(transcriptionCost);

            // Chat
            String response = chat.converse(id, userInput);

            // Calculate cost
            BigDecimal chatCost = chat.calculateConversationCost(id);
            System.out.println("Chat Cost: $" + chatCost.setScale(9, RoundingMode.HALF_UP));
            totalCost = totalCost.add(chatCost);

            // Text-to-Speech
            AudioFile audioResponse;
            audioResponse = speech.speak(response);

            // Calculate cost
            BigDecimal speechCost = speech.calculatePrice(response);
            System.out.println("Speech Cost: $" + speechCost.setScale(6, RoundingMode.HALF_UP));
            totalCost = totalCost.add(speechCost);

            // Play audio
            System.out.println("\nBot Response: ");
            System.out.println(response);
            System.out.println("\nTotal Cost: $" + totalCost.setScale(9, RoundingMode.HALF_UP));
            System.out.println("\n");
            audioResponse.play();
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException {

        // Configuration Path
        String configPath = "/config.json";

        // Check for arguments
        if (args.length > 0) configPath = args[0];

        // Start Assistant
        new Assistant(configPath, args.length > 0);
    }
}