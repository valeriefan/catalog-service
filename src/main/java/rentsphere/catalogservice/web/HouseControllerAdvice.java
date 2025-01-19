package rentsphere.catalogservice.web;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rentsphere.catalogservice.domain.HouseAlreadyExistsException;
import rentsphere.catalogservice.domain.HouseNotFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class HouseControllerAdvice {

    @ExceptionHandler(HouseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String houseNotFoundHandler(HouseNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(HouseAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    String houseAlreadyExistsHandler(HouseAlreadyExistsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ) {
        var errors = new HashMap<String, String>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
