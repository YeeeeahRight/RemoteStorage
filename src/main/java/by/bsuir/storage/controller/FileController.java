package by.bsuir.storage.controller;

import by.bsuir.storage.exception.EntityNotFound;
import by.bsuir.storage.exception.OutOfStorageBoundsException;
import by.bsuir.storage.service.FileService;
import by.bsuir.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/storage/file")
public class FileController {
    private final FileService fileService;
    private final StorageService storageService;

    @Autowired
    public FileController(FileService fileService, StorageService storageService) {
        this.fileService = fileService;
        this.storageService = storageService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public String readFile(@RequestParam String location,
                           Model model) throws IOException {
        if (!fileService.isRealFile(location)) {
            throw new EntityNotFound("File not found");
        }
        String content = fileService.readFile(location);
        model.addAttribute("data", content);
        model.addAttribute("location", location);
        model.addAttribute("dirLocation", getPrevLocation(location));
        return "file-content";
    }

    @PutMapping
    public String writeToFile(@RequestParam String location,
                              @RequestParam String data) throws IOException {
        if (!fileService.isRealFile(location)) {
            throw new EntityNotFound("File not found");
        }
        fileService.writeToFile(data, location);
        return "redirect:/storage/file?location=" + location;
    }

    @PostMapping
    public String addToEndFile(@RequestParam String location,
                               @RequestParam String data) throws IOException {
        if (!fileService.isRealFile(location)) {
            throw new EntityNotFound("File not found");
        }
        fileService.addToEndFile(data, location);
        return "redirect:/storage/file?location=" + location;
    }

    @PostMapping("/copy")
    public String copyFile(@RequestParam String location,
                           @RequestParam String path) throws IOException {
        if (!storageService.isCorrectPath(path)) {
            throw new OutOfStorageBoundsException("Path to copy is invalid");
        }
        if (!fileService.isRealFile(location)) {
            throw new EntityNotFound("File not found");
        }
        fileService.copyFile(location, path);
        return "redirect:/storage/file?location=" + path;
    }

    @PutMapping("/move")
    public String moveFile(@RequestParam String location,
                           @RequestParam String path) throws IOException {
        if (!storageService.isCorrectPath(path)) {
            throw new OutOfStorageBoundsException("Path to move is invalid");
        }
        if (!fileService.isRealFile(location)) {
            throw new EntityNotFound("File not found");
        }
        fileService.moveFile(location, path);
        return "redirect:/storage/file?location=" + path;
    }

    @DeleteMapping
    public String deleteFile(@RequestParam String location) {
        if (!fileService.isRealFile(location)) {
            throw new EntityNotFound("File not found");
        }
        fileService.deleteFile(location);
        return "redirect:/storage?location=" + getPrevLocation(location);
    }

    private String getPrevLocation(String location) {
        int slashPosition = location.lastIndexOf("/");
        if (slashPosition > 0) {
            return location.substring(0, slashPosition);
        }
        return "/";
    }
}
