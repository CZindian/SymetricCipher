package cz.osu.cipher.symmetric.aes.app.encryption;

import cz.osu.cipher.symmetric.aes.app.aes.AES;
import cz.osu.cipher.symmetric.aes.app.exceptions.*;
import cz.osu.cipher.symmetric.aes.app.model.Metadata;
import cz.osu.cipher.symmetric.aes.app.model.Mode;
import cz.osu.cipher.symmetric.aes.app.utils.Utils;

import java.io.IOException;

import static cz.osu.cipher.symmetric.aes.app.utils.constants.Phrases.*;
import static cz.osu.cipher.symmetric.aes.app.utils.constants.Strings.CBC;
import static cz.osu.cipher.symmetric.aes.app.utils.constants.Strings.CFB;

public class EncryptFile {

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

        encrypt();

        if (metadata.getOutputPath() != null) {
            System.out.println("'" + metadata.getInputPath() + "'" + " successfully encrypted.");
            System.out.println("New file can be found in '" + metadata.getOutputPath() + "'");
            System.out.println("Store initialization vector for decryption: " + metadata.getIvForDecryption());
        }

    }

    private static void encrypt() {

        try {
            switch (metadata.getMode().name()) {
                case CBC -> AES.encryptFileCBC(metadata);
                case CFB -> AES.encryptFileCFB(metadata);
                default -> throw new IllegalArgumentException(metadata.getMode().name() + " is not allowed!");
            }

        } catch (IOException | FileOrDirectoryDoesNotExistException |
                DirectoryDoesNotExistException | FileDoesNotExistException e) {
            System.out.println(e.getMessage());
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
            Utils.checkValidityOf(consoleInput);
            metadata.setInputPath(consoleInput);

        } catch (EmptyMessageException | DirectoryDoesNotExistException |
                FileOrDirectoryDoesNotExistException | FileDoesNotExistException e) {
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
    //endregion

}
