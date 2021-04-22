package by.bsuir.storage.service.impl;

import by.bsuir.storage.service.StorageService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class StorageServiceImpl implements StorageService {
    private static final String ROOT_PATH = "data";

    @Override
    public String readFile(String location) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(getRealLocation(location)));
        while (bufferedReader.ready()) {
            content.append(bufferedReader.readLine());
        }
        return content.toString();
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
