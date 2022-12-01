package cz.osu.cipher.symmetric.aes.app.decryption;

import cz.osu.cipher.symmetric.aes.app.exceptions.IllegalConsoleInputException;
import cz.osu.cipher.symmetric.aes.app.utils.Utils;

import static cz.osu.cipher.symmetric.aes.app.utils.constants.Phrases.SPECIES_INTRO_COMMAND;

/**
 * Manager for file and text decryption
 */
public class DecryptionManager {

    private static String consoleInput;

    /**
     * Main run method
     */
    public static void run() {

        System.out.println("Do you want to decrypt a file or simple text?");

        runIntro();

    }

    /**
     * Main intro. It introduces user for next steps
     */
    private static void runIntro() {

        System.out.println(SPECIES_INTRO_COMMAND);
        listenConsoleInput();

    }

    private static void listenConsoleInput() {

        consoleInput = Utils.getConsoleInput();
        processConsoleInput();

    }

    /**
     * Switches to file decryption or text decryption on user command
     */
    private static void processConsoleInput() {

        try {
            switch (consoleInput) {
                case "f" -> DecryptFile.run();
                case "t" -> DecryptText.run();
                default -> throw new IllegalConsoleInputException(consoleInput);
            }

        } catch (IllegalConsoleInputException e) {
            System.out.println(e.getMessage());
            runIntro();

        }

    }

}
