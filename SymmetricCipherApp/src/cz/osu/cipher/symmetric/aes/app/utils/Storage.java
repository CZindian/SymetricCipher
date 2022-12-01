package cz.osu.cipher.symmetric.aes.app.utils;

import cz.osu.cipher.symmetric.aes.app.exceptions.DirectoryDoesNotExistException;
import cz.osu.cipher.symmetric.aes.app.exceptions.FileDoesNotExistException;
import cz.osu.cipher.symmetric.aes.app.exceptions.FileOrDirectoryDoesNotExistException;
import cz.osu.cipher.symmetric.aes.app.model.Metadata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static cz.osu.cipher.symmetric.aes.app.utils.constants.Strings.DECRYPTED;
import static cz.osu.cipher.symmetric.aes.app.utils.constants.Strings.ENCRYPTED;

/**
 * Class to manage storage
 */
public class Storage {

    //region Attributes
    public static final String ENCRYPTED_PART = "_" + ENCRYPTED;
    public static final String DECRYPTED_PART = "_" + DECRYPTED;
    //endregion


    /**
     * @param filePath complete path of input file
     * @return loaded file string
     * @throws IOException                          is thrown on IO error
     * @throws DirectoryDoesNotExistException       is thrown when given directory from path does not exist
     * @throws FileOrDirectoryDoesNotExistException is thrown when file or directory does not exist on current path
     * @throws FileDoesNotExistException            is thrown when path does not contain any file
     */
    public static String load(String filePath) throws IOException, DirectoryDoesNotExistException,
            FileOrDirectoryDoesNotExistException, FileDoesNotExistException {

        Utils.checkValidityOf(filePath);

        Path path = Paths.get(filePath);
        String data = Files.readString(path);
        return data;

    }

    /**
     * Saves output file
     *
     * @param data          output data
     * @param filePath      complete path of output file
     * @param operationType defined type of operation (encrypted, decrypted)
     * @param metadata      is used as cache to store text, mode, block size chosen by user
     * @throws IOException is thrown on IO error
     */
    public static void save(String data, String filePath, String operationType, Metadata metadata) throws IOException {

        String newPath = getNewUri(filePath, operationType);
        metadata.setOutputPath(newPath);

        Path path = Paths.get(newPath);
        Files.writeString(path, data);

    }


    /**
     * @param filePath      complete path of input file
     * @param operationType defined type of operation (encrypted, decrypted)
     * @return path of new output file
     */
    private static String getNewUri(String filePath, String operationType) {

        filePath = getCleanFilePath(filePath);
        String[] fileNameParts = filePath.split("\\.");
        return fileNameParts[0] + "_" + operationType + "_" + getLocalDateTime() + "." + fileNameParts[1];

    }

    /**
     * @param filePath complete path of input file
     * @return returns path derived from input path
     */
    private static String getCleanFilePath(String filePath) {

        filePath = getFilePathWithout(ENCRYPTED_PART, filePath);
        filePath = getFilePathWithout(DECRYPTED_PART, filePath);
        return filePath;

    }

    /**
     * @param part     defines what to delete from input file
     * @param filePath complete path of input file
     * @return path without defined string part
     */
    private static String getFilePathWithout(String part, String filePath) {

        if (filePath.contains(part)) {
            StringBuilder sb = new StringBuilder();

            int start = filePath.indexOf(part);
            int end = filePath.lastIndexOf(".");

            for (int i = 0; i < filePath.length(); i++) {
                if (i < start || i >= end) {
                    sb.append(filePath.charAt(i));
                }
            }
            filePath = sb.toString();
        }

        return filePath;
    }

    private static String getLocalDateTime() {

        LocalDateTime localDateTime = LocalDateTime.now();
        String ret = localDateTime.getHour() + "_" + localDateTime.getMinute() + "_" +
                localDateTime.getSecond() + "_" + localDateTime.getDayOfMonth() + "_" +
                localDateTime.getMonthValue() + "_" + localDateTime.getYear();
        return ret;

    }

}
