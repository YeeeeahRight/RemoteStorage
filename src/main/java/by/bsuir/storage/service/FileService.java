package by.bsuir.storage.service;

import java.io.IOException;

public interface FileService {

    String readFile(String location) throws IOException;

    void writeToFile(String data, String location) throws IOException;

    void addToEndFile(String data, String location) throws IOException;

    void deleteFile(String location);

    boolean isRealFile(String location);
}
