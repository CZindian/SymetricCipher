package cz.osu.cipher.symmetric.aes.app.exceptions;

public class FileDoesNotExistException extends Exception {
    public FileDoesNotExistException(String uri) {
        super("\t-missing file or file does not exist in '" + uri + "'");
    }
}
