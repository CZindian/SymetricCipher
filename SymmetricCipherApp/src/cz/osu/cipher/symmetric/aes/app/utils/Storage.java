package cz.osu.cipher.symmetric.aes.app.utils;

import java.io.File;
import java.nio.file.Paths;

public class Storage {

    //TODO check validity of path
    public static File load(String path) {

        File ret = Paths.get(path).toFile();
        return ret;

    }

}
