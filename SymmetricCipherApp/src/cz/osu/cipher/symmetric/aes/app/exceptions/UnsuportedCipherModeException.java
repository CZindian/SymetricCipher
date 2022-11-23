package cz.osu.cipher.symmetric.aes.app.exceptions;

public class UnsuportedCipherModeException extends Exception {
    public UnsuportedCipherModeException(String string) {
        super("\t-mode '" + string + "' is not allowed");
    }
}
