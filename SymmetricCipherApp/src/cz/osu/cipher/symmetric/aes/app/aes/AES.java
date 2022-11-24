package cz.osu.cipher.symmetric.aes.app.aes;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class AES {

    private static String ivTmp;

    public static String getIvTmp() {

        String ret = ivTmp;
        ivTmp = null;
        return ret;

    }

    public static IvParameterSpec generateIv() {

        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        ivTmp = Base64.getEncoder().encodeToString(iv);
        return new IvParameterSpec(iv);

    }

    public static IvParameterSpec getIvFor(String ivString) {

        byte[] iv = Base64.getDecoder().decode(ivString);
        return new IvParameterSpec(iv);

    }

    public static Key getPasswordBasedKey(String cipher, int keySize, char[] password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        String salt = new String(password);

        PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt.getBytes(), 65535, keySize);
        SecretKey pbeKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(pbeKeySpec);
        return new SecretKeySpec(pbeKey.getEncoded(), cipher);

    }

    public static String encrypt(String algorithm, String input, Key key, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());

        return Base64.getEncoder().encodeToString(cipherText);

    }

    public static String decrypt(String algorithm, String cipherText, Key key, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));

        return new String(plainText);
    }

}
