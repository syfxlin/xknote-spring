package me.ixk.xknote.advice;

import me.ixk.xknote.http.ResponseInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MissingRequestParameterAdvice
    extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request
    ) {
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("PathParam")) {
                return ResponseInfo.stdError(
                    "Parameter not found. (path)",
                    HttpStatus.BAD_REQUEST
                );
            }
            if (ex.getMessage().contains("OldNewPathParam")) {
                return ResponseInfo.stdError(
                    "Parameter not found. (old_path, new_path)",
                    HttpStatus.BAD_REQUEST
                );
            }
        }
        return super.handleHttpMessageNotReadable(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
        MissingServletRequestParameterException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request
    ) {
        if ("path".equals(ex.getParameterName())) {
            return ResponseInfo.stdError(
                "Parameter not found. (path)",
                HttpStatus.BAD_REQUEST
            );
        }
        if ("old_path".equals(ex.getParameterName())) {
            return ResponseInfo.stdError(
                "Parameter not found. (old_path)",
                HttpStatus.BAD_REQUEST
            );
        }
        if ("new_path".equals(ex.getParameterName())) {
            return ResponseInfo.stdError(
                "Parameter not found. (new_path)",
                HttpStatus.BAD_REQUEST
            );
        }
        return super.handleMissingServletRequestParameter(
            ex,
            headers,
            status,
            request
        );
    }
}
