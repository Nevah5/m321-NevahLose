package dev.geeler.apiaces.gameservice.exception;

import dev.geeler.apiaces.gameservice.model.ErrorResponse;
import dev.geeler.apiaces.gameservice.model.ServerErrorResponse;
import io.jsonwebtoken.security.SignatureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LogManager.getLogger(GlobalExceptionHandler.class);

    // 400 - Bad Request
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleClientError(IllegalArgumentException e) {
        final ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

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
