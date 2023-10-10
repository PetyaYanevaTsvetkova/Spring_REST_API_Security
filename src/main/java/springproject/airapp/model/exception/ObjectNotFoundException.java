package coherentsolutions.airapp.model.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends RuntimeException {

    private final Long objectId;

    public ObjectNotFoundException(Long objectId) {
        this.objectId = objectId;
    }

}
