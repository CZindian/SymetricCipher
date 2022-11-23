package cz.osu.cipher.symmetric.aes.app.exceptions;

public class UnsuportedBlockSizeException extends RuntimeException {
    public UnsuportedBlockSizeException(int length) {
        super("AES block size of '" + length + "' is not supported");
    }
}
