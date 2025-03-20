package de.MCmoderSD;

import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.JavaAudioLibrary.AudioFile;
import de.MCmoderSD.JavaAudioLibrary.AudioRecorder;

import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Assistant {

    // Constants
    public final static String BOLD = "\033[0;1m";
    public final static String UNBOLD = "\u001B[0m";

    // Associations
    private final OpenAI openAI;

    // Constructor
    public Assistant(String configPath, boolean absolutePath) throws IOException, URISyntaxException {

        // Load Config
        JsonNode config = JsonUtility.loadJson(configPath, absolutePath);

        // Initialize OpenAI
        openAI = new OpenAI(config);

        // Set config
        Builder.Chat.setConfig(config);
        Builder.Speech.setConfig(config);
        Builder.Transcription.setConfig(config);

        // Loop
        new Thread(this::loop).start();
    }

    private void loop() {

        // Variables
        var id = 1;
        Scanner scanner = new Scanner(System.in);
        AudioRecorder recorder = new AudioRecorder();

        // Loop
        while (true) {

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
            String userInput = openAI.transcribe(userAudio.getAudioData());

            // Print user input
            System.out.println("User Input: ");
            System.out.println(userInput);

            // Chat
            String response = openAI.prompt(id, userInput);

            // Text-to-Speech
            AudioFile audioResponse;
            try {
                audioResponse = new AudioFile(openAI.speech(response));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Play audio
            System.out.println("\nBot Response: ");
            System.out.println(response);
            audioResponse.play();
            System.out.println("\n\n");
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