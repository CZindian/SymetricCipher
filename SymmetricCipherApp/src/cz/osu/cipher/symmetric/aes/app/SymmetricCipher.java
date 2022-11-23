package cz.osu.cipher.symmetric.aes.app;

import cz.osu.cipher.symmetric.aes.app.decryption.DecryptionManager;
import cz.osu.cipher.symmetric.aes.app.encryption.EncryptionManager;
import cz.osu.cipher.symmetric.aes.app.exceptions.IllegalConsoleInputException;
import cz.osu.cipher.symmetric.aes.app.utils.Utils;

import static cz.osu.cipher.symmetric.aes.app.utils.constants.Phrases.INTRO_COMMAND;

public class SymmetricCipher {

    private static String consoleInput;

    public static void run() {

        System.out.println("Welcome!");
        System.out.println("This application will encrypt / decrypt your data using the powerful AES II cipher.");

        runIntro();

    }

    private static void runIntro() {

        System.out.println(INTRO_COMMAND);
        listenConsoleInput();

        System.out.println("Do you want to exit the program? (y/n)");
        listenConsoleInputExit();

    }

    private static void listenConsoleInput() {

        consoleInput = Utils.getConsoleInput();
        processConsoleInput();

    }

    private static void listenConsoleInputExit() {

        consoleInput = Utils.getConsoleInput();

        if (consoleInput.equals("y")) {
            System.exit(1);
        } else {
            runIntro();
        }

    }

    private static void processConsoleInput() {


        try {
            switch (consoleInput) {
                case "d" -> DecryptionManager.run();
                case "e" -> EncryptionManager.run();
                default -> throw new IllegalConsoleInputException(consoleInput);
            }

        } catch (IllegalConsoleInputException e) {
            System.out.println(e.getMessage());
            runIntro();

        }

    }

}
