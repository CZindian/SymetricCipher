package cz.osu.cipher.symmetric.aes.app.model;

import cz.osu.cipher.symmetric.aes.app.utils.Utils;

import java.util.Base64;

public class Metadata {

    //region Attributes
    private int cipherKeyLength;
    private String password;
    private String message;
    private Mode mode;
    private String ivForDecryption;
    //endregion

    //region Getters
    public int getCipherKeyLength() {

        Utils.checkBlockAESValidity(cipherKeyLength);
        return cipherKeyLength;

    }

    public String getPassword() {

        Utils.checkValidity(password);
        return password;

    }

    public String getMessage() {

        Utils.checkValidity(password);
        return message;

    }

    public Mode getMode() {
        Utils.checkValidity(mode);
        return mode;
    }

    public String getIvForDecryption() {
        Utils.checkValidity(ivForDecryption);
        return ivForDecryption;
    }

    //endregion

    //region Setters
    public void setCipherKeyLength(int cipherKeyLength) {
        this.cipherKeyLength = cipherKeyLength;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setIvForDecryption(String ivForDecryption) {
        this.ivForDecryption = ivForDecryption;
    }

    public void setIvForDecryption(byte[] ivForDecryption) {
        String ivString = Base64.getEncoder().encodeToString(ivForDecryption);
        this.ivForDecryption = ivString;
    }

    //endregion

}
