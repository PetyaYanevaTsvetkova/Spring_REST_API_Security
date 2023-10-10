package coherentsolutions.airapp.controller.advice;

import coherentsolutions.airapp.model.dto.ErrorDTO;
import coherentsolutions.airapp.model.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ObjectNotFoundHandler {

    @ExceptionHandler({ObjectNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorDTO handleNotFoundError(ObjectNotFoundException ex) {
        return new ErrorDTO(
                ex.getMessage(),
                String.format("User message: There is no object with id: %d", ex.getObjectId()),
                404);
    }

}
