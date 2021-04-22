package by.bsuir.storage.logic.service.impl;

import by.bsuir.storage.logic.file.FileManager;
import by.bsuir.storage.logic.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class StorageServiceImpl implements StorageService {
    private static final String ROOT_PATH = "data";

    private final FileManager fileManager;

    @Autowired
    public StorageServiceImpl(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public String readFile(String location) throws IOException {
        return fileManager.read(getRealLocation(location));
    }

    @Override
    public boolean isRealFile(String location) {
        return new File(getRealLocation(location)).isFile();
    }

    @Override
    public boolean isExist(String location) {
        return new File(getRealLocation(location)).exists();
    }

    @Override
    public List<String> readDirectory(String location) {
        List<String> names = new ArrayList<>();
        getDirectoryContent(new File(getRealLocation(location)), names);
        return names;
    }

    @Override
    public void writeToFile(String data, String location) throws IOException {
        fileManager.write(data, getRealLocation(location));
    }

    @Override
    public void addToEndFile(String data, String location) throws IOException {
        String realLocation = getRealLocation(location);
        String fileData = fileManager.read(realLocation);
        fileManager.write(fileData + data, realLocation);
    }

    @Override
    public void deleteFile(String location) throws IOException {
        File file = new File(getRealLocation(location));
        file.delete();
    }

    private String getRealLocation(String location) {
        return ROOT_PATH + "/" + location;
    }

    private void getDirectoryContent(File folder, List<String> names) {
        if (folder == null) {
            return;
        }
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File fileEntry : files) {
            names.add(fileEntry.getName());
        }
    }
}
