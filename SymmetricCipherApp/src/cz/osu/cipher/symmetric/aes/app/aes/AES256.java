package cz.osu.cipher.symmetric.aes.app.aes;

import cz.osu.cipher.symmetric.aes.app.model.Metadata;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class AES256 {
    
    public static String encryptCBC(Metadata metadata) {

        try {
            IvParameterSpec iv = AES.generateIv();
            Key key = AES.getPasswordBasedKey("AES", 256, metadata.getPassword().toCharArray());
            metadata.setIvForDecryption(iv.getIV());

            return AES.encrypt("AES/CBC/PKCS5Padding", metadata.getMessage(), key, iv);

        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | InvalidKeyException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);

        }

    }

    public static String decryptCBC(Metadata metadata) {

        try {
            IvParameterSpec iv = AES.getIvFor(metadata.getIvForDecryption());
            Key key = AES.getPasswordBasedKey("AES", 256, metadata.getPassword().toCharArray());
            return AES.decrypt("AES/CBC/PKCS5Padding", metadata.getMessage(), key, iv);

        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | InvalidKeyException |
                 RuntimeException e) {
            System.out.println("\t-wrong credentials");

        }

        return null;

    }

}
