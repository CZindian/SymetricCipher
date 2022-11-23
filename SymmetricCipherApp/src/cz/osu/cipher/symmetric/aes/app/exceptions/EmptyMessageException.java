package cz.osu.cipher.symmetric.aes.app.exceptions;

public class EmptyMessageException extends Exception {
    public EmptyMessageException() {
        super("\t-input must have more than 0 characters and must not contain whitespaces only");
    }
}
