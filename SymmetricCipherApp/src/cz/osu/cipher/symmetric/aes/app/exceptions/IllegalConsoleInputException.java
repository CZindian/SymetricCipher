package cz.osu.cipher.symmetric.aes.app.exceptions;

/**
 * Should be called, when program catches unexpected console value.
 */
public class IllegalConsoleInputException extends Exception {
    public IllegalConsoleInputException(String input) {
        super("\t-you typed unexpected character: '" + input + "'");
    }
}
