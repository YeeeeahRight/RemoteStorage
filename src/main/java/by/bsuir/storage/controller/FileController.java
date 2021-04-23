package by.bsuir.storage.controller;

import by.bsuir.storage.contsant.URLConstant;
import by.bsuir.storage.exception.EntityNotFound;
import by.bsuir.storage.exception.OutOfStorageBoundsException;
import by.bsuir.storage.service.FileService;
import by.bsuir.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/storage/files")
public class FileController {
    private final FileService fileService;
    private final StorageService storageService;

    @Autowired
    public FileController(FileService fileService, StorageService storageService) {
        this.fileService = fileService;
        this.storageService = storageService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/**")
    public String readFile(HttpServletRequest request,
                           Model model) throws IOException {
        String location = extractFileLocation(request, URLConstant.FILE_LOCATION_BEGIN);
        if (!fileService.isRealFile(location)) {
            throw new EntityNotFound("File not found");
        }
        String content = fileService.readFile(location);
        model.addAttribute("data", content);
        model.addAttribute("location", location);
        model.addAttribute("dirLocation", getPrevLocation(location));
        return "file-content";
    }

    @GetMapping("/download/**")
    public void downloadFile(HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        String location = extractFileLocation(request, URLConstant.FILE_LOCATION_BEGIN + "/download");
        if (!fileService.isRealFile(location)) {
            throw new EntityNotFound("File not found");
        }
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + getFileName(location));
        response.getOutputStream().write(fileService.getFileBytes(location));
        response.flushBuffer();
    }

    @PutMapping("/**")
    public String writeToFile(HttpServletRequest request,
                              @RequestParam String data) throws IOException {
        String location = extractFileLocation(request, URLConstant.FILE_LOCATION_BEGIN);
        if (!fileService.isRealFile(location)) {
            throw new EntityNotFound("File not found");
        }
        fileService.writeToFile(data, location);
        return URLConstant.FILE_READ_REDIRECT + location;
    }

    @PostMapping("/**")
    public String addToEndFile(HttpServletRequest request,
                               @RequestParam String data) throws IOException {
        String location = extractFileLocation(request, URLConstant.FILE_LOCATION_BEGIN);
        if (!fileService.isRealFile(location)) {
            throw new EntityNotFound("File not found");
        }
        fileService.addToEndFile(data, location);
        return URLConstant.FILE_READ_REDIRECT + location;
    }

    @PostMapping("/copy/**")
    public String copyFile(HttpServletRequest request,
                           @RequestParam String path) throws IOException {
        String location = extractFileLocation(request, URLConstant.FILE_LOCATION_BEGIN + "/copy");
        if (!storageService.isCorrectPath(path)) {
            throw new OutOfStorageBoundsException("Path to copy is invalid");
        }
        if (!fileService.isRealFile(location)) {
            throw new EntityNotFound("File not found");
        }
        fileService.copyFile(location, path);
        return URLConstant.FILE_READ_REDIRECT + path;
    }

    @PutMapping("/move/**")
    public String moveFile(HttpServletRequest request,
                           @RequestParam String path) throws IOException {
        String location = extractFileLocation(request, URLConstant.FILE_LOCATION_BEGIN + "/move");
        if (!storageService.isCorrectPath(path)) {
            throw new OutOfStorageBoundsException("Path to move is invalid");
        }
        if (!fileService.isRealFile(location)) {
            throw new EntityNotFound("File not found");
        }
        fileService.moveFile(location, path);
        return URLConstant.FILE_READ_REDIRECT + path;
    }

    @DeleteMapping("/**")
    public String deleteFile(HttpServletRequest request) {
        String location = extractFileLocation(request, URLConstant.FILE_LOCATION_BEGIN);
        System.out.println(location);
        if (!fileService.isRealFile(location)) {
            throw new EntityNotFound("File not found");
        }
        fileService.deleteFile(location);
        return URLConstant.DIRECTORY_READ_REDIRECT + getPrevLocation(location);
    }

    private String getPrevLocation(String location) {
        int slashPosition = location.lastIndexOf("/");
        if (slashPosition > 0) {
            return location.substring(0, slashPosition);
        }
        return "/";
    }

    private String getFileName(String location) {
        return location.substring(location.lastIndexOf("/"));
    }

    private String extractFileLocation(HttpServletRequest request, String from) {
        int index = request.getRequestURI().indexOf(from);
        return request.getRequestURI().substring(index + from.length());
    }
}
