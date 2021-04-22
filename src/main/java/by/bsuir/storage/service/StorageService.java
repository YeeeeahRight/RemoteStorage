package by.bsuir.storage.service;

import java.io.IOException;
import java.util.List;

public interface StorageService {

    List<String> readDirectory(String location);

    String readFile(String location) throws IOException;

    boolean isRealFile(String location);

    boolean isExist(String location);

    void writeToFile(String location, String data) throws IOException;
}
