package cz.osu.cipher.symmetric.aes.app.encryption;

import cz.osu.cipher.symmetric.aes.app.aes.AES;
import cz.osu.cipher.symmetric.aes.app.exceptions.EmptyMessageException;
import cz.osu.cipher.symmetric.aes.app.exceptions.UnsuportedBlockSizeException;
import cz.osu.cipher.symmetric.aes.app.exceptions.UnsuportedCipherModeException;
import cz.osu.cipher.symmetric.aes.app.model.Metadata;
import cz.osu.cipher.symmetric.aes.app.model.Mode;
import cz.osu.cipher.symmetric.aes.app.utils.Utils;

import static cz.osu.cipher.symmetric.aes.app.utils.constants.Phrases.CIPHER_KEY_LENGTH_COMMAND;
import static cz.osu.cipher.symmetric.aes.app.utils.constants.Phrases.MODE_INTRO_COMMAND;
import static cz.osu.cipher.symmetric.aes.app.utils.constants.Strings.*;

public class EncryptText {

    //region Attributes
    private static Metadata metadata;
    private static String consoleInput;
    private static String encryptedMessage;
    //endregion

    public static void run() {

        metadata = new Metadata();

        System.out.println("Choose " + Mode.CBC.name() + " or " + Mode.CFB.name());

        runIntroMode();
        runIntroCipherKeyLength();
        runIntroMessage();
        runIntroPassword();

        encrypt();
        System.out.println("Encrypted message: " + encryptedMessage);
        System.out.println("Store initialization vector for decryption: " + metadata.getIvForDecryption());

    }

    private static void encrypt() {

        switch (metadata.getMode().name()) {
            case CBC -> encryptedMessage = AES.encryptTextCBC(metadata);
            case CFB -> encryptedMessage = AES.encryptTextCFB(metadata);
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

    private static void runIntroMessage() {

        System.out.println("Type your message:");
        listenConsoleInputMessage();

    }

    private static void runIntroPassword() {

        System.out.println("Type your password:");
        listenConsoleInputPassword();

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

    private static void listenConsoleInputMessage() {

        try {
            consoleInput = Utils.getConsoleInput();
            Utils.checkConsoleInputValidity(consoleInput);
            metadata.setMessage(consoleInput);

        } catch (EmptyMessageException e) {
            System.out.println(e.getMessage());
            runIntroMessage();

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
    //endregion

}
