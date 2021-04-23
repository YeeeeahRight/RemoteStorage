package by.bsuir.storage.controller;

import by.bsuir.storage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/storage/file")
public class FileController {
    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public String readFile(@RequestParam String location,
                           Model model) throws IOException {
        if (fileService.isRealFile(location)) {
            String content = fileService.readFile(location);
            model.addAttribute("data", content);
            model.addAttribute("location", location);
            model.addAttribute("dirLocation", getPrevLocation(location));
            return "file-content";
        }
        return "file-not-found";
    }

    @PutMapping
    public String writeToFile(@RequestParam String location,
                              @RequestParam String data) throws IOException {
        if (fileService.isRealFile(location)) {
            fileService.writeToFile(data, location);
            return "redirect:/storage/file?location=" + location;
        }
        return "file-not-found";
    }

    @PostMapping
    public String addToEndFile(@RequestParam String location,
                               @RequestParam String data) throws IOException {
        if (fileService.isRealFile(location)) {
            fileService.addToEndFile(data, location);
            return "redirect:/storage/file?location=" + location;
        }
        return "file-not-found";
    }

    @DeleteMapping
    public String deleteFile(@RequestParam String location) {
        if (fileService.isRealFile(location)) {
            fileService.deleteFile(location);
            return "redirect:/storage?location=" + getPrevLocation(location);
        }
        return "file-not-found";
    }

    private String getPrevLocation(String location) {
        int slashPosition = location.lastIndexOf("/");
        if (slashPosition > 0) {
            return location.substring(0, slashPosition);
        }
        return "/";
    }
}
