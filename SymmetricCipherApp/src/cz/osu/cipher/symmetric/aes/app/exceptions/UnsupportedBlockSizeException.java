package cz.osu.cipher.symmetric.aes.app.exceptions;

/**
 * Should be called, when unsupported cipher block size is given
 */
public class UnsupportedBlockSizeException extends RuntimeException {
    public UnsupportedBlockSizeException(int length) {
        super("\t-AES block size of '" + length + "' is not supported");
    }
}
