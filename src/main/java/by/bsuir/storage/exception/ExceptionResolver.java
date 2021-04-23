package by.bsuir.storage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.FileNotFoundException;

@ControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoHandlerFound() {
        return "not-found";
    }

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleFileNotFound(Model model) {
        model.addAttribute("message", "Path is not found");
        return "error/error-with-message";
    }

    @ExceptionHandler(EntityNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEntityNotFound(EntityNotFound e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error/error-with-message";
    }

    @ExceptionHandler(OutOfStorageBoundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleOutOfStorageBounds(OutOfStorageBoundsException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error/error-with-message";
    }
}
