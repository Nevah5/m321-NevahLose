package dev.geeler.apiaces.playerservice.exception;

import dev.geeler.apiaces.playerservice.model.ServerErrorResponse;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.logging.log4j.LogManager;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LogManager.getLogger(GlobalExceptionHandler.class);

    // 500 - Internal Server Error
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ServerErrorResponse> handleServerError(Exception e) {
        final ServerErrorResponse serverErrorResponse = new ServerErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage()
        );
        LOGGER.error(serverErrorResponse.errorCode, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(serverErrorResponse);
    }
}
