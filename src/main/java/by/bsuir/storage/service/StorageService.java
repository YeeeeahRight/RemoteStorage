package by.bsuir.storage.service;

import java.io.IOException;
import java.util.Map;

public interface StorageService {

    Map<String, String> readDirectory(String location);

    boolean isDirectory(String location);

    boolean isCorrectPath(String path);

    void addFile(String name, String location, String type) throws IOException;

    void deleteDirectory(String location);
}
