package ma.youcode.covoiturage.exceptions;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationException(
            MethodArgumentNotValidException exception, HttpServletRequest request) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return buildErrorResponse(message, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleResourceNotFound(
            ResourceNotFoundException exception, HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND, request);
    }


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorMessage> handleBusinessException(
            BusinessException exception, HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, request);
    }
    

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorMessage> handleConflictException(
            ConflictException exception, HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorMessage> handleForbiddenException(
            ForbiddenException exception, HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleInternalServerError(
            Exception exception, HttpServletRequest request) {
        return buildErrorResponse(
                "Une erreur interne est survenue. Veuillez réessayer plus tard.",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }

    private ResponseEntity<ErrorMessage> buildErrorResponse(
            String message, HttpStatus status, HttpServletRequest request) {
        ErrorMessage body = new ErrorMessage(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI());

        return ResponseEntity.status(status).body(body);
    }
}
