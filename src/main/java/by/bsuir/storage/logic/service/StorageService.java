package by.bsuir.storage.logic.service;

import java.io.IOException;
import java.util.List;

public interface StorageService {

    List<String> readDirectory(String location);

    String readFile(String location) throws IOException;

    boolean isRealFile(String location);

    boolean isExist(String location);

    void writeToFile(String data, String location) throws IOException;

    void addToEndFile(String data, String location) throws IOException;

    void deleteFile(String location) throws IOException;
}
