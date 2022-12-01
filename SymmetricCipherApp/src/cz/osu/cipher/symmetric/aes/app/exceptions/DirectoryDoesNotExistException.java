package cz.osu.cipher.symmetric.aes.app.exceptions;

/**
 * Should be thrown, when current directory does not exist
 */
public class DirectoryDoesNotExistException extends Exception {
    public DirectoryDoesNotExistException(String imgUri) {
        super("\t-directory '" + imgUri + "' does not exist");
    }
}
