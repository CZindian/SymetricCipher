package cz.osu.cipher.symmetric.aes.app.decryption;

import cz.osu.cipher.symmetric.aes.app.exceptions.IllegalConsoleInputException;
import cz.osu.cipher.symmetric.aes.app.utils.Utils;

import static cz.osu.cipher.symmetric.aes.app.utils.constants.Phrases.SPECIES_INTRO_COMMAND;

public class DecryptionManager {

    private static String consoleInput;

    public static void run() {

        System.out.println("Do you want to decrypt a file or simple text?");

        runIntro();

    }

    private static void runIntro() {

        System.out.println(SPECIES_INTRO_COMMAND);
        listenConsoleInput();

    }

    private static void listenConsoleInput() {

        consoleInput = Utils.getConsoleInput();
        processConsoleInput();

    }

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
