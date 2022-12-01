package cz.osu.cipher.symmetric.aes.app.aes;

import cz.osu.cipher.symmetric.aes.app.exceptions.DirectoryDoesNotExistException;
import cz.osu.cipher.symmetric.aes.app.exceptions.FileOrDirectoryDoesNotExistException;
import cz.osu.cipher.symmetric.aes.app.model.Metadata;
import cz.osu.cipher.symmetric.aes.app.utils.Storage;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import static cz.osu.cipher.symmetric.aes.app.utils.constants.Strings.DECRYPTED;
import static cz.osu.cipher.symmetric.aes.app.utils.constants.Strings.ENCRYPTED;

public class AES {

    //region Attributes
    private static final String AES_CIPHER = "AES";
    private static final String AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
    private static final String AES_CFB_NO_PADDING = "AES/CFB/NoPadding";
    //endregion


    //region CBC
    public static String encryptTextCBC(Metadata metadata) {
        return getEncryptedString(metadata, AES_CBC_PKCS5_PADDING);
    }

    public static void encryptFileCBC(Metadata metadata) throws IOException, DirectoryDoesNotExistException,
            FileOrDirectoryDoesNotExistException {

        String data = Storage.load(metadata.getInputPath());

        metadata.setMessage(data);
        String encryptedData = getEncryptedString(metadata, AES_CBC_PKCS5_PADDING);

        Storage.save(encryptedData, metadata.getInputPath(), ENCRYPTED);

    }

    public static String decryptTextCBC(Metadata metadata) {
        return getDecryptedString(metadata, AES_CBC_PKCS5_PADDING);
    }

    public static void decryptFileCBC(Metadata metadata) throws IOException, DirectoryDoesNotExistException,
            FileOrDirectoryDoesNotExistException {

        String data = Storage.load(metadata.getInputPath());

        metadata.setMessage(data);
        String encryptedData = getDecryptedString(metadata, AES_CBC_PKCS5_PADDING);

        Storage.save(encryptedData, metadata.getInputPath(), DECRYPTED);

    }
    //endregion

    //region CFB
    public static String encryptTextCFB(Metadata metadata) {
        return getEncryptedString(metadata, AES_CFB_NO_PADDING);
    }

    public static void encryptFileCFB(Metadata metadata) {
        throw new UnsupportedOperationException();
    }

    public static String decryptTextCFB(Metadata metadata) {
        return getDecryptedString(metadata, AES_CFB_NO_PADDING);
    }

    public static void decryptFileCFB(Metadata metadata) {
        throw new UnsupportedOperationException();
    }
    //endregion


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


    private static Key getPasswordBasedKey(Metadata metadata)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return AES.getPasswordBasedKey(AES_CIPHER, metadata.getCipherKeyLength(), metadata.getPassword().toCharArray());
    }


    private static IvParameterSpec generateIv() {

        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);

    }

    private static IvParameterSpec getIvFor(String ivString) {

        byte[] iv = Base64.getDecoder().decode(ivString);
        return new IvParameterSpec(iv);

    }

    private static Key getPasswordBasedKey(String cipher, int keySize, char[] password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        String salt = new String(password);

        PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt.getBytes(), 65535, keySize);
        SecretKey pbeKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(pbeKeySpec);
        return new SecretKeySpec(pbeKey.getEncoded(), cipher);

    }

    private static String encrypt(String algorithm, String input, Key key, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());

        return Base64.getEncoder().encodeToString(cipherText);

    }

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
