package by.bsuir.storage.logic.file;

import java.io.IOException;

public interface FileManager {

    String read(String location) throws IOException;

    void write(String data, String location) throws IOException;
}
