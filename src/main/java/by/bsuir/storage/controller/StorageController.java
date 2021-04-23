package by.bsuir.storage.controller;

import by.bsuir.storage.exception.EntityNotFound;
import by.bsuir.storage.exception.OutOfStorageBoundsException;
import by.bsuir.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/storage")
public class StorageController {
    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public String getDirectoryContent(@RequestParam String location, Model model)  {
        if (!storageService.isDirectory(location)) {
            throw new EntityNotFound("Directory not found");
        }
        model.addAttribute("directoryContent", storageService.readDirectory(location));
        model.addAttribute("location", addSlashInEnd(location));
        if (!location.equals("/")) {
            model.addAttribute("prevLocation", getPrevLocation(location));
        }
        return "directory-content";
    }

    @PostMapping
    public String addFile(@RequestParam String location,
                          @RequestParam String name,
                          @RequestParam String type) throws IOException {
        if (!storageService.isCorrectPath(location + name)) {
            throw new OutOfStorageBoundsException("Incorrect name");
        }
        storageService.addFile(name, location, type);
        return "redirect:/storage?location=" + location;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam String location,
                             @RequestParam MultipartFile file) throws IOException {
        storageService.uploadFile(file, location);
        return "redirect:/storage?location=" + location;
    }

    @DeleteMapping
    public String deleteDirectory(@RequestParam String location) {
        if (!storageService.isDirectory(location)) {
            throw new EntityNotFound("Directory not found");
        }
        storageService.deleteDirectory(location);
        return "redirect:/storage?location=" + getPrevLocation(location);
    }

    private String addSlashInEnd(String location) {
        if (location.isEmpty() || location.charAt(location.length() - 1) != '/') {
            location += '/';
        }
        return location;
    }

    private String getPrevLocation(String location) {
        int slashPosition = location.lastIndexOf("/");
        if (slashPosition > 0) {
            int preLastSlashPos = location.substring(0, slashPosition).lastIndexOf("/");
            return location.substring(0, preLastSlashPos + 1);
        }
        return "/";
    }
}
