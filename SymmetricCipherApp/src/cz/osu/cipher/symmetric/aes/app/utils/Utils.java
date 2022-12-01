package cz.osu.cipher.symmetric.aes.app.utils;

import cz.osu.cipher.symmetric.aes.app.exceptions.DirectoryDoesNotExistException;
import cz.osu.cipher.symmetric.aes.app.exceptions.EmptyMessageException;
import cz.osu.cipher.symmetric.aes.app.exceptions.FileOrDirectoryDoesNotExistException;
import cz.osu.cipher.symmetric.aes.app.exceptions.UnsuportedBlockSizeException;
import cz.osu.cipher.symmetric.aes.app.model.Mode;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Utils {

    public static String getConsoleInput() {

        String input;
        input = new Scanner(System.in).nextLine();
        return input;

    }

    public static void checkConsoleInputValidity(String string) throws EmptyMessageException {

        if (string != null) {
            if (string.isEmpty() || string.isBlank()) {
                throw new EmptyMessageException();
            }
        }

    }

    public static void checkValidity(String string) {

        if (string == null) {
            throw new NullPointerException("argument of isValid(string) is null!");
        }

        if (string.isEmpty() || string.isBlank()) {
            throw new IllegalArgumentException("argument of isValid(string) has 0 length or contains whitespaces only!");
        }

    }

    public static void checkValidity(Mode mode) {

        if (mode == null) {
            throw new NullPointerException("argument of isValid(mode) is null!");
        }

    }

    public static void checkBlockAESValidity(int length) {

        boolean statement = length == 128 || length == 192 || length == 256;
        if (!statement) {
            throw new UnsuportedBlockSizeException(length);
        }

    }

    public static void checkValidityOf(String uri)
            throws FileOrDirectoryDoesNotExistException, DirectoryDoesNotExistException {

        Path path = Paths.get(uri);
        if (uri.length() == 0 || !Files.exists(path))
            throw new FileOrDirectoryDoesNotExistException(uri);
        isDirectoryValid(uri);

    }

    private static void isDirectoryValid(String imgUri) throws DirectoryDoesNotExistException {

        Path parent = Paths.get(imgUri).getParent();
        if (!Files.isDirectory(parent)) {
            throw new DirectoryDoesNotExistException(imgUri);
        }

    }

}
