package coherentsolutions.airapp.controller.advice;

import coherentsolutions.airapp.model.dto.ErrorDTO;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ObjectNotValidHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorDTO handleNotValidError(MethodArgumentNotValidException ex) {
        return new ErrorDTO(
                ex.getMessage(),
                "User message: Please, enter a valid input",
                400);
    }

}
