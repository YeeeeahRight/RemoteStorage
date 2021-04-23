package by.bsuir.storage.service.impl;

import by.bsuir.storage.service.FileService;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class FileServiceImpl implements FileService {
    private static final String ROOT_PATH = "data";
    private static final String NEW_LINE = "\r\n";

    @Override
    public String readFile(String location) throws IOException {
        return read(getRealLocation(location));
    }

    @Override
    public void writeToFile(String data, String location) throws IOException {
        write(data, getRealLocation(location));
    }

    @Override
    public void addToEndFile(String data, String location) throws IOException {
        String realLocation = getRealLocation(location);
        String fileData = read(realLocation);
        write(fileData + data, realLocation);
    }

    @Override
    public void deleteFile(String location) {
        File file = new File(getRealLocation(location));
        file.delete();
    }

    @Override
    public boolean isRealFile(String location) {
        String realLocation = getRealLocation(location);
        return new File(realLocation).isFile() && !realLocation.contains("../");
    }

    @Override
    public void copyFile(String location, String destination) throws IOException {
        String data = read(getRealLocation(location));
        write(data, getRealLocation(destination));
    }

    @Override
    public void moveFile(String location, String destination) throws IOException {
        String data = read(getRealLocation(location));
        write(data, getRealLocation(destination));
        deleteFile(location);
    }

    @Override
    public byte[] getFileBytes(String location) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(
                new FileInputStream(getRealLocation(location)));
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024 * 10];
        while ((nRead = bufferedInputStream.read(data, 0, data.length)) != -1) {
            byteArray.write(data, 0, nRead);
        }
        byteArray.flush();
        bufferedInputStream.close();
        return byteArray.toByteArray();
    }

    private String getRealLocation(String location) {
        return ROOT_PATH + "/" + location;
    }

    private String read(String location) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(location));
        while (bufferedReader.ready()) {
            content.append(bufferedReader.readLine()).append(NEW_LINE);
        }
        bufferedReader.close();
        String contentStr = content.toString();
        if (!contentStr.isEmpty()) {
            contentStr = contentStr.substring(0, contentStr.length() - 2);
        }
        return contentStr;
    }

    private void write(String data, String location) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(location));
        bufferedWriter.write(data);
        bufferedWriter.close();
    }
}
