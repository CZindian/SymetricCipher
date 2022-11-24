package cz.osu.cipher.symmetric.aes.app.utils.constants;

import cz.osu.cipher.symmetric.aes.app.model.Mode;

public class Phrases {

    public static final String INTRO_COMMAND = "Choose 'd' for decryption or 'e' for encryption:";
    public static final String SPECIES_INTRO_COMMAND = "Choose 'f' for a file or 't' for text:'";
    public static final String MODE_INTRO_COMMAND = "Type '" + Mode.CBC.name() + "' or '" + Mode.CFB.name() + "' upper or lower case:";
    public static final String CIPHER_KEY_LENGTH_COMMAND = "Choose length of cipher key (128, 192, 256):";

}
