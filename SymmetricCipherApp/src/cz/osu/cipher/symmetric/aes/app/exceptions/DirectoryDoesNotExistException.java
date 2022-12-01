package cz.osu.cipher.symmetric.aes.app.exceptions;

public class DirectoryDoesNotExistException extends Exception {
    public DirectoryDoesNotExistException(String imgUri) {
        super("\t-directory '" + imgUri + "' does not exist");
    }
}
