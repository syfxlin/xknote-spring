package me.ixk.xknote.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * 非法操作路径异常
 *
 * @author Otstar Lin
 * @date 2020/12/11 上午 10:10
 */
public class IllegalOperationDirectoryException
    extends ResponseStatusException {

    public IllegalOperationDirectoryException(String path) {
        super(HttpStatus.FORBIDDEN, "Illegal operation directory: " + path);
    }
}
