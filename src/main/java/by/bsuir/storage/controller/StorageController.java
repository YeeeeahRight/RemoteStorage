package by.bsuir.storage.controller;

import by.bsuir.storage.contsant.URLConstant;
import by.bsuir.storage.exception.EntityNotFound;
import by.bsuir.storage.exception.OutOfStorageBoundsException;
import by.bsuir.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping(value = "/storage", produces = "text/plain;charset=UTF-8")
public class StorageController {
    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/**", produces = "text/plain;charset=UTF-8")
    public String getDirectoryContent(HttpServletRequest request, Model model)  {
        String location = extractFileLocation(request, URLConstant.DIRECTORY_LOCATION_BEGIN);
        if (!storageService.isDirectory(location)) {
            throw new EntityNotFound("Directory not found");
        }
        model.addAttribute("directoryContent", storageService.readDirectory(location));
        model.addAttribute("location", addSlashInEnd(location));
        if (!addSlashInEnd(location).equals("/")) {
            model.addAttribute("prevLocation", getPrevLocation(location));
        }
        return "directory-content";
    }

    @PostMapping("/**")
    public String addFile(HttpServletRequest request,
                          @RequestParam String name,
                          @RequestParam String type) throws IOException {
        String location = extractFileLocation(request, URLConstant.DIRECTORY_LOCATION_BEGIN);
        if (!storageService.isCorrectPath(location + name)) {
            throw new OutOfStorageBoundsException("Incorrect name");
        }
        storageService.addFile(name, location, type);
        return URLConstant.DIRECTORY_READ_REDIRECT + location;
    }

    @PostMapping("/upload/**")
    public String uploadFile(HttpServletRequest request,
                             @RequestParam MultipartFile file) throws IOException {
        String location = extractFileLocation(request, URLConstant.DIRECTORY_LOCATION_BEGIN + "/upload");
        storageService.uploadFile(file, location);
        return URLConstant.DIRECTORY_READ_REDIRECT + location;
    }

    @DeleteMapping("/**")
    public String deleteDirectory(HttpServletRequest request) {
        String location = extractFileLocation(request, URLConstant.DIRECTORY_LOCATION_BEGIN);
        if (!storageService.isDirectory(location) || addSlashInEnd(location).equals("/")) {
            throw new EntityNotFound("Directory not found");
        }
        storageService.deleteDirectory(location);
        return URLConstant.DIRECTORY_READ_REDIRECT + getPrevLocation(location);
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

    private String extractFileLocation(HttpServletRequest request, String from) {
        String requestURL = request.getRequestURI();
        int index = requestURL.indexOf(from);
        return requestURL.substring(index + from.length());
    }
}
