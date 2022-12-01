package cz.osu.cipher.symmetric.aes.app.exceptions;

/**
 * Should be called, when current file does not exist or path does not contain file
 */
public class FileDoesNotExistException extends Exception {
    public FileDoesNotExistException(String uri) {
        super("\t-missing file or file does not exist in '" + uri + "'");
    }
}
