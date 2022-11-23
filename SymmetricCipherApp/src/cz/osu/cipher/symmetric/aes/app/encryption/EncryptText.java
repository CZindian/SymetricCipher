package cz.osu.cipher.symmetric.aes.app.encryption;

import cz.osu.cipher.symmetric.aes.app.aes.AES128;
import cz.osu.cipher.symmetric.aes.app.aes.AES192;
import cz.osu.cipher.symmetric.aes.app.aes.AES256;
import cz.osu.cipher.symmetric.aes.app.exceptions.EmptyMessageException;
import cz.osu.cipher.symmetric.aes.app.exceptions.UnsuportedBlockSizeException;
import cz.osu.cipher.symmetric.aes.app.exceptions.UnsuportedCipherModeException;
import cz.osu.cipher.symmetric.aes.app.model.Metadata;
import cz.osu.cipher.symmetric.aes.app.model.Mode;
import cz.osu.cipher.symmetric.aes.app.utils.Utils;

import static cz.osu.cipher.symmetric.aes.app.utils.constants.Phrases.CIPHER_KEY_LENGTH_COMMAND;
import static cz.osu.cipher.symmetric.aes.app.utils.constants.Phrases.MODE_INTRO_COMMAND;
import static cz.osu.cipher.symmetric.aes.app.utils.constants.Strings.CBC;
import static cz.osu.cipher.symmetric.aes.app.utils.constants.Strings.XTS;

public class EncryptText {

    //region Attributes
    private static Metadata metadata;
    private static String consoleInput;
    private static String encryptedMessage;
    //endregion

    public static void run() {

        metadata = new Metadata();

        System.out.println("Choose " + Mode.CBC.name() + " or " + Mode.XTS.name());

        runIntroMode();
        runIntroCipherKeyLength();
        runIntroMessage();
        runIntroPassword();

/*        System.out.println(
                metadata.getMode().name() + "\n" +
                        metadata.getCipherKeyLength() + "\n" +
                        metadata.getMessage() + "\n" +
                        metadata.getPassword()
        );*/
        encrypt();
        System.out.println("Encrypted message: " + encryptedMessage);

    }


    private static void encrypt() {

        switch (metadata.getMode().name()) {
            case CBC -> encryptByCBC();
            case XTS -> encryptByXTS();
            default -> throw new IllegalArgumentException(metadata.getMode().name() + " is not allowed!");
        }

    }

    private static void encryptByCBC() {

        switch (metadata.getCipherKeyLength()) {
            case 128 -> {
                encryptedMessage = AES128.encryptCBC(metadata);
            }
            case 192 -> {
                encryptedMessage = AES192.encryptCBC(metadata);
            }
            case 256 -> {
                encryptedMessage = AES256.encryptCBC(metadata);
            }
            default -> throw new UnsuportedBlockSizeException(metadata.getCipherKeyLength());
        }

    }

    //TODO
    private static void encryptByXTS() {
        throw new UnsupportedOperationException("encryptByXTS() is not implemented");
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
                case XTS -> metadata.setMode(Mode.XTS);
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
