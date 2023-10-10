package coherentsolutions.airapp.model.dto;

import lombok.Data;

@Data
public class ErrorDTO {

    private final String devMessage;

    private final String userMessage;

    private final Integer statusCode;

}
