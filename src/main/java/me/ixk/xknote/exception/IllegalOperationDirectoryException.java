package me.ixk.xknote.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IllegalOperationDirectoryException
    extends ResponseStatusException {

    public IllegalOperationDirectoryException(String path) {
        super(HttpStatus.FORBIDDEN, "Illegal operation directory: " + path);
    }
}
