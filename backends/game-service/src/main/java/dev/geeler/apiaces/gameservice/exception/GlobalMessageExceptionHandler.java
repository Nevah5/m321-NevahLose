package dev.geeler.apiaces.gameservice.exception;

import dev.geeler.apiaces.gameservice.model.http.ErrorResponse;
import dev.geeler.apiaces.gameservice.model.http.ServerErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.MessagingException;
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
        log.info("IllegalStateException handled: {}", exception.getMessage());
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        ).toJsonString();
    }

    @MessageExceptionHandler(MaxGameSizeException.class)
    @SendToUser("/queue/errors")
    public String handleException(MaxGameSizeException exception) {
        log.info("MaxGameSizeException handled: {}", exception.getMessage());
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        ).toJsonString();
    }

    @MessageExceptionHandler(IllegalArgumentException.class)
    @SendToUser("/queue/errors")
    public String handleException(IllegalArgumentException exception) {
        log.info("IllegalArgumentException handled: {}", exception.getMessage());
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        ).toJsonString();
    }

    @MessageExceptionHandler(MessagingException.class)
    @SendToUser("/queue/errors")
    public String handleException(MessagingException exception) {
        log.info("MessagingException handled: {}", exception.getMessage());
        return new ServerErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage()
        ).toJsonString();
    }
}
