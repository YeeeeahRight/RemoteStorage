package by.bsuir.storage.controller;

import by.bsuir.storage.logic.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/storage")
public class StorageController {
    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping
    public String readFile(@RequestParam("location") String location, Model model) throws IOException {
        if (!storageService.isExist(location)) {
            return "not-found";
        }
        if (storageService.isRealFile(location)) {
            String content = storageService.readFile(location);
            model.addAttribute("data", content);
            return "file-content";
        }
        model.addAttribute("listFiles", storageService.readDirectory(location));
        model.addAttribute("location", addSlashInEnd(location));
        return "directory-content";
    }

    @PutMapping
    public String writeToFile(@RequestParam("location") String location,
                              @RequestParam String data) throws IOException {
        if (!storageService.isExist(location) || !storageService.isRealFile(location)) {
            return "not-found";
        }
        storageService.writeToFile(data, location);
        return "redirect:/storage?location=" + location;
    }

    @PostMapping
    public String addToEndFile(@RequestParam("location") String location,
                              @RequestParam String data) throws IOException {
        if (!storageService.isExist(location) || !storageService.isRealFile(location)) {
            return "not-found";
        }
        storageService.addToEndFile(data, location);
        return "redirect:/storage?location=" + location;
    }

    private String addSlashInEnd(String location) {
        if (location.isEmpty() || location.charAt(location.length() - 1) != '/') {
            location += '/';
        }
        return location;
    }
}
