package by.bsuir.storage.logic.file;

import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class FileManagerImpl implements FileManager {
    private static final String NEW_LINE = "\r\n";

    @Override
    public String read(String location) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(location));
        while (bufferedReader.ready()) {
            content.append(bufferedReader.readLine()).append(NEW_LINE);
        }
        bufferedReader.close();
        return content.toString();
    }

    @Override
    public void write(String data, String location) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(location));
        bufferedWriter.write(data);
        bufferedWriter.close();
    }
}
