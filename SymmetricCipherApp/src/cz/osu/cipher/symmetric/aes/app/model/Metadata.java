package cz.osu.cipher.symmetric.aes.app.model;

import cz.osu.cipher.symmetric.aes.app.utils.Utils;

public class Metadata {

    //region Attributes
    private int cipherKeyLength;
    private String password;
    private String message;
    private Mode mode;
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

    //endregion

}
