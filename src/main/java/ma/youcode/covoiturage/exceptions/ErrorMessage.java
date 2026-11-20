package ma.youcode.covoiturage.exceptions;

import java.time.LocalDateTime;
import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class ErrorMessage extends ProblemDetail {

    public ErrorMessage(String message, HttpStatus status, String path) {
        super(ProblemDetail.forStatus(status));
        setType(URI.create("urn:problem:" + status.name().toLowerCase()));
        setTitle(status.getReasonPhrase());
        setDetail(message);
        setInstance(URI.create(path));
        setProperty("timestamp", LocalDateTime.now());
        setProperty("message", message);
        setProperty("path", path);
    }
}
