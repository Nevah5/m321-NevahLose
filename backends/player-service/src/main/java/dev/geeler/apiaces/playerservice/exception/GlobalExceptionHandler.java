package dev.geeler.apiaces.playerservice.exception;

import dev.geeler.apiaces.playerservice.model.ErrorResponse;
import dev.geeler.apiaces.playerservice.model.ServerErrorResponse;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LogManager.getLogger(GlobalExceptionHandler.class);

    // 400 - Bad Request
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleClientError(MethodArgumentTypeMismatchException e) {
        final ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid Data type passed for " + e.getName()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // 400 - Bad Request
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleClientError(HttpMessageNotReadableException e) {
        final ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid JSON format"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

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

    // 404 - Not Found
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoResourceFoundException e) {
        final ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // 404 - Not Found
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e) {
        final ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
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
