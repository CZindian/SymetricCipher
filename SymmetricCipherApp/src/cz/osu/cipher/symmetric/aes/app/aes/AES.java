package cz.osu.cipher.symmetric.aes.app.aes;

import cz.osu.cipher.symmetric.aes.app.exceptions.DirectoryDoesNotExistException;
import cz.osu.cipher.symmetric.aes.app.exceptions.FileDoesNotExistException;
import cz.osu.cipher.symmetric.aes.app.exceptions.FileOrDirectoryDoesNotExistException;
import cz.osu.cipher.symmetric.aes.app.model.Metadata;
import cz.osu.cipher.symmetric.aes.app.utils.Storage;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import static cz.osu.cipher.symmetric.aes.app.utils.constants.Strings.DECRYPTED;
import static cz.osu.cipher.symmetric.aes.app.utils.constants.Strings.ENCRYPTED;

/**
 * API for work with AES.
 * Supports 128, 192, 256b block sizes.
 * Supports CBC, CFB mode.
 */
public class AES {

    //region Attributes
    private static final String AES_CIPHER = "AES";
    private static final String AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
    private static final String AES_CFB_NO_PADDING = "AES/CFB/NoPadding";
    //endregion


    //region CBC encryption / decryption methods

    /**
     * @param metadata is used as cache to store text, mode, block size chosen by user
     * @return encrypted text
     */
    public static String encryptTextCBC(Metadata metadata) {
        return getEncryptedString(metadata, AES_CBC_PKCS5_PADDING);
    }

    /**
     * Encrypts file in CBC mode.
     *
     * @param metadata is used as cache to store text, mode, block size chosen by user
     * @throws IOException                          might be thrown on IO error
     * @throws DirectoryDoesNotExistException       might be thrown on non-valid file wrong path
     * @throws FileOrDirectoryDoesNotExistException might be thrown on non-valid file wrong path
     * @throws FileDoesNotExistException            might be thrown on non-valid file wrong path
     */
    public static void encryptFileCBC(Metadata metadata) throws IOException, DirectoryDoesNotExistException,
            FileOrDirectoryDoesNotExistException, FileDoesNotExistException {

        String data = Storage.load(metadata.getInputPath());

        metadata.setMessage(data);
        String encryptedData = getEncryptedString(metadata, AES_CBC_PKCS5_PADDING);

        if ((encryptedData != null)) {
            Storage.save(encryptedData, metadata.getInputPath(), ENCRYPTED, metadata);
        }

    }

    /**
     * @param metadata is used as cache to store text, mode, block size chosen by user
     * @return decrypted text
     */
    public static String decryptTextCBC(Metadata metadata) {
        return getDecryptedString(metadata, AES_CBC_PKCS5_PADDING);
    }

    /**
     * @param metadata is used as cache to store text, mode, block size chosen by user
     * @throws IOException                          might be thrown on IO error
     * @throws DirectoryDoesNotExistException       might be thrown on non-valid file wrong path
     * @throws FileOrDirectoryDoesNotExistException might be thrown on non-valid file wrong path
     * @throws FileDoesNotExistException            might be thrown on non-valid file wrong path
     */
    public static void decryptFileCBC(Metadata metadata) throws IOException, DirectoryDoesNotExistException,
            FileOrDirectoryDoesNotExistException, FileDoesNotExistException {

        String data = Storage.load(metadata.getInputPath());

        metadata.setMessage(data);
        String encryptedData = getDecryptedString(metadata, AES_CBC_PKCS5_PADDING);

        if ((encryptedData != null)) {
            Storage.save(encryptedData, metadata.getInputPath(), DECRYPTED, metadata);
        }

    }
    //endregion

    //region CFB encryption / decryption methods

    /**
     * @param metadata is used as cache to store text, mode, block size chosen by user
     * @return encrypted text
     */
    public static String encryptTextCFB(Metadata metadata) {
        return getEncryptedString(metadata, AES_CFB_NO_PADDING);
    }

    /**
     * @param metadata is used as cache to store text, mode, block size chosen by user
     * @throws IOException                          might be thrown on IO error
     * @throws DirectoryDoesNotExistException       might be thrown on non-valid file wrong path
     * @throws FileOrDirectoryDoesNotExistException might be thrown on non-valid file wrong path
     * @throws FileDoesNotExistException            might be thrown on non-valid file wrong path
     */
    public static void encryptFileCFB(Metadata metadata) throws IOException, DirectoryDoesNotExistException,
            FileOrDirectoryDoesNotExistException, FileDoesNotExistException {

        String data = Storage.load(metadata.getInputPath());

        metadata.setMessage(data);
        String encryptedData = getEncryptedString(metadata, AES_CFB_NO_PADDING);

        if ((encryptedData != null)) {
            Storage.save(encryptedData, metadata.getInputPath(), ENCRYPTED, metadata);
        }

    }

    public static String decryptTextCFB(Metadata metadata) {
        return getDecryptedString(metadata, AES_CFB_NO_PADDING);
    }

    /**
     * @param metadata is used as cache to store text, mode, block size chosen by user
     * @throws IOException                          might be thrown on IO error
     * @throws DirectoryDoesNotExistException       might be thrown on non-valid file wrong path
     * @throws FileOrDirectoryDoesNotExistException might be thrown on non-valid file wrong path
     * @throws FileDoesNotExistException            might be thrown on non-valid file wrong path
     */
    public static void decryptFileCFB(Metadata metadata) throws IOException, DirectoryDoesNotExistException,
            FileOrDirectoryDoesNotExistException, FileDoesNotExistException {

        String data = Storage.load(metadata.getInputPath());

        metadata.setMessage(data);
        String encryptedData = getDecryptedString(metadata, AES_CFB_NO_PADDING);

        if ((encryptedData != null)) {
            Storage.save(encryptedData, metadata.getInputPath(), DECRYPTED, metadata);
        }

    }
    //endregion


    /**
     * @param metadata  is used as cache to store text, mode, block size chosen by user
     * @param algorithm defined algorithm chosen by user (AES/CBC/PKCS5Padding or AES/CFB/NoPadding)
     * @return encrypted string by defined algorithm
     */
    private static String getEncryptedString(Metadata metadata, String algorithm) {

        try {
            IvParameterSpec iv = generateIv();
            Key key = getPasswordBasedKey(metadata);
            metadata.setIvForDecryption(iv.getIV());

            return encrypt(algorithm, metadata.getMessage(), key, iv);

        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException |
                NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | InvalidKeyException e) {
            System.out.println("\t-" + e.getMessage());
            throw new RuntimeException(e);

        }

    }

    /**
     * @param metadata  is used as cache to store text, mode, block size chosen by user
     * @param algorithm defined algorithm chosen by user (AES/CBC/PKCS5Padding or AES/CFB/NoPadding)
     * @return decrypted string by defined algorithm
     */
    private static String getDecryptedString(Metadata metadata, String algorithm) {

        try {
            IvParameterSpec iv = getIvFor(metadata.getIvForDecryption());
            Key key = getPasswordBasedKey(metadata);
            return decrypt(algorithm, metadata.getMessage(), key, iv);

        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException |
                NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | InvalidKeyException |
                RuntimeException e) {
            System.out.println("\t-wrong credentials");

        }

        return null;

    }


    /**
     * @param metadata is used as cache to store text, mode, block size chosen by user
     * @return generated key
     * @throws NoSuchAlgorithmException is thrown when unsupported security algorithm is called
     * @throws InvalidKeySpecException  is thrown when invalid key is generated
     */
    private static Key getPasswordBasedKey(Metadata metadata)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return AES.getPasswordBasedKey(AES_CIPHER, metadata.getCipherKeyLength(), metadata.getPassword().toCharArray());
    }

    /**
     * @param cipher   current cipher (AES)
     * @param keySize  defined block side by user
     * @param password string password given from user
     * @return generated key, dependent on password and salt
     * @throws NoSuchAlgorithmException is thrown when unsupported security algorithm is called
     * @throws InvalidKeySpecException  is thrown when invalid key is generated
     */
    private static Key getPasswordBasedKey(String cipher, int keySize, char[] password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        String salt = new String(password);

        PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt.getBytes(), 65535, keySize);
        SecretKey pbeKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(pbeKeySpec);
        return new SecretKeySpec(pbeKey.getEncoded(), cipher);

    }

    /**
     * @return randomly generated initialization vector
     */
    private static IvParameterSpec generateIv() {

        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);

    }

    /**
     * @param ivString initialization vector in string representation
     * @return initialization vector generated on encryption,
     * for later purposes of decryption
     */
    private static IvParameterSpec getIvFor(String ivString) {

        byte[] iv = Base64.getDecoder().decode(ivString);
        return new IvParameterSpec(iv);

    }


    /**
     * @param algorithm type of cipher
     * @param input     input for encryption
     * @param key       cipher key
     * @param iv        initialization vector
     * @return encrypted string
     * @throws NoSuchPaddingException             is thrown on incorrect padding
     * @throws NoSuchAlgorithmException           is thrown on incorrect defined cipher
     * @throws IllegalBlockSizeException          is thrown on incorrect block size
     * @throws BadPaddingException                is thrown on incorrect padding
     * @throws InvalidAlgorithmParameterException is thrown on illegal input method argument
     * @throws InvalidKeyException                is thrown on incorrect key
     */
    private static String encrypt(String algorithm, String input, Key key, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());

        return Base64.getEncoder().encodeToString(cipherText);

    }

    /**
     * @param algorithm  type of cipher
     * @param cipherText input for decryption
     * @param key        cipher key
     * @param iv         initialization vector
     * @return decrypted string
     * @throws NoSuchPaddingException             is thrown on incorrect padding
     * @throws NoSuchAlgorithmException           is thrown on incorrect defined cipher
     * @throws IllegalBlockSizeException          is thrown on incorrect block size
     * @throws BadPaddingException                is thrown on incorrect padding
     * @throws InvalidAlgorithmParameterException is thrown on illegal input method argument
     * @throws InvalidKeyException                is thrown on incorrect key
     */
    private static String decrypt(String algorithm, String cipherText, Key key, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));

        return new String(plainText);
    }

}
