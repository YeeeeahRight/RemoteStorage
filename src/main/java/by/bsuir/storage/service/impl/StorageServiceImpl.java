package by.bsuir.storage.service.impl;

import by.bsuir.storage.service.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class StorageServiceImpl implements StorageService {
    private static final String ROOT_PATH = "data";

    @Override
    public boolean isDirectory(String location) {
        return new File(getRealLocation(location)).isDirectory() && isCorrectPath(location);
    }

    @Override
    public boolean isCorrectPath(String path) {
        return !getRealLocation(path).contains("../");
    }

    @Override
    public Map<String, String> readDirectory(String location) {
        Map<String, String> directoryContent = new LinkedHashMap<>();
        getDirectoryContent(new File(getRealLocation(location)), directoryContent);
        return directoryContent;
    }

    @Override
    public void addFile(String name, String location, String type) throws IOException {
        File file = new File(getRealLocation(location) + "/" + name);
        if (type.equalsIgnoreCase("directory")) {
            file.mkdir();
        } else {
            file.createNewFile();
        }
    }

    @Override
    public void deleteDirectory(String location) {
        FileSystemUtils.deleteRecursively(new File(getRealLocation(location)));
    }

    @Override
    public void uploadFile(MultipartFile file, String location) throws IOException {
        String path = getRealLocation(location) + "/" + file.getOriginalFilename();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path));
        bufferedOutputStream.write(file.getBytes());
        bufferedOutputStream.close();
    }

    private String getRealLocation(String location) {
        return ROOT_PATH + "/" + location;
    }

    private void getDirectoryContent(File folder, Map<String, String> directoryContent) {
        if (folder == null) {
            return;
        }
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File fileEntry : files) {
            String name = fileEntry.getName();
            String type = fileEntry.isDirectory() ? "DIR: " : "FILE: ";
            directoryContent.put(name, type);
        }
    }
}
