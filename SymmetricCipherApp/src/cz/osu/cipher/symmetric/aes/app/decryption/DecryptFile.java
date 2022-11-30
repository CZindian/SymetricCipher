package cz.osu.cipher.symmetric.aes.app.decryption;

import cz.osu.cipher.symmetric.aes.app.aes.AES;
import cz.osu.cipher.symmetric.aes.app.exceptions.EmptyMessageException;
import cz.osu.cipher.symmetric.aes.app.exceptions.UnsuportedBlockSizeException;
import cz.osu.cipher.symmetric.aes.app.exceptions.UnsuportedCipherModeException;
import cz.osu.cipher.symmetric.aes.app.model.Metadata;
import cz.osu.cipher.symmetric.aes.app.model.Mode;
import cz.osu.cipher.symmetric.aes.app.utils.Utils;

import static cz.osu.cipher.symmetric.aes.app.utils.constants.Phrases.*;
import static cz.osu.cipher.symmetric.aes.app.utils.constants.Strings.CBC;
import static cz.osu.cipher.symmetric.aes.app.utils.constants.Strings.CFB;

public class DecryptFile {

    //region Attributes
    private static Metadata metadata;
    private static String consoleInput;
    //endregion

    public static void run() {

        metadata = new Metadata();

        System.out.println("Choose " + Mode.CBC.name() + " or " + Mode.CFB.name());

        runIntroMode();
        runIntroCipherKeyLength();
        runIntroInputPath();
        runIntroPassword();
        runIntroIvForDecryption();

        decrypt();
        System.out.println("'" + metadata.getInputPath() + "'" + " successfully decrypted.");

    }

    private static void decrypt() {

        switch (metadata.getMode().name()) {
            case CBC -> AES.decryptFileCBC(metadata);
            case CFB -> AES.decryptFileCFB(metadata);
            default -> throw new IllegalArgumentException(metadata.getMode().name() + " is not allowed!");
        }

    }


    //region Manage mode, cipherKeyLength, message, password
    private static void runIntroMode() {

        System.out.println(MODE_INTRO_COMMAND);
        listenConsoleInputMode();

    }

    private static void runIntroCipherKeyLength() {

        System.out.println(CIPHER_KEY_LENGTH_COMMAND);
        listenConsoleInputCipherKeyLength();

    }

    public static void runIntroInputPath() {

        System.out.println(COMPLETE_FILE_PATH_COMMAND);
        System.out.println(EXAMPLE_FILE_PATH_COMMAND);
        listenConsoleInputPath();

    }


    private static void runIntroPassword() {

        System.out.println("Type your password:");
        listenConsoleInputPassword();

    }

    private static void runIntroIvForDecryption() {

        System.out.println("Insert initialization that you have got on encryption:");
        listenConsoleInputIvForDecryption();

    }
    //endregion


    //region Listen console inputs
    private static void listenConsoleInputMode() {

        try {
            consoleInput = Utils.getConsoleInput().toUpperCase();

            switch (consoleInput) {
                case CBC -> metadata.setMode(Mode.CBC);
                case CFB -> metadata.setMode(Mode.CFB);
                default -> throw new UnsuportedCipherModeException(consoleInput);
            }

        } catch (UnsuportedCipherModeException e) {
            System.out.println(e.getMessage());
            runIntroMode();
        }

    }

    private static void listenConsoleInputCipherKeyLength() {

        try {
            consoleInput = Utils.getConsoleInput();
            int cipherKeyLength = Integer.parseInt(consoleInput);
            Utils.checkBlockAESValidity(cipherKeyLength);

            metadata.setCipherKeyLength(cipherKeyLength);

        } catch (NumberFormatException | UnsuportedBlockSizeException e) {
            System.out.println(e.getMessage());
            runIntroCipherKeyLength();

        }

    }

    private static void listenConsoleInputPath() {

        try {
            consoleInput = Utils.getConsoleInput();
            Utils.checkConsoleInputValidity(consoleInput);
            metadata.setInputPath(consoleInput);

        } catch (EmptyMessageException e) {
            System.out.println(e.getMessage());
            runIntroInputPath();

        }

    }

    private static void listenConsoleInputPassword() {

        try {
            consoleInput = Utils.getConsoleInput();
            Utils.checkConsoleInputValidity(consoleInput);
            metadata.setPassword(consoleInput);

        } catch (EmptyMessageException e) {
            System.out.println(e.getMessage());
            runIntroPassword();

        }

    }

    private static void listenConsoleInputIvForDecryption() {

        try {
            consoleInput = Utils.getConsoleInput();
            Utils.checkConsoleInputValidity(consoleInput);
            metadata.setIvForDecryption(consoleInput);

        } catch (EmptyMessageException e) {
            System.out.println(e.getMessage());
            runIntroIvForDecryption();

        }

    }
    //endregion

}
