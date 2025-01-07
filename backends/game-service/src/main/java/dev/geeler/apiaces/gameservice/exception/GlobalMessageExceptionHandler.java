package dev.geeler.apiaces.gameservice.exception;

import dev.geeler.apiaces.gameservice.model.http.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@Slf4j
public class GlobalMessageExceptionHandler {
    @MessageExceptionHandler(NotFoundException.class)
    @SendToUser("/queue/errors")
    public String handleException(NotFoundException exception) {
        log.info("NotFoundException handled: {}", exception.getMessage());
        return new ErrorResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        ).toJsonString();
    }

    @MessageExceptionHandler(IllegalStateException.class)
    @SendToUser("/queue/errors")
    public String handleException(IllegalStateException exception) {
        log.info("NotFoundException handled: {}", exception.getMessage());
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        ).toJsonString();
    }

    @MessageExceptionHandler(MaxGameSizeException.class)
    @SendToUser("/queue/errors")
    public String handleException(MaxGameSizeException exception) {
        log.info("NotFoundException handled: {}", exception.getMessage());
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        ).toJsonString();
    }
}
