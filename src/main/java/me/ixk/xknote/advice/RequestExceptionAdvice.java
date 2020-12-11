package me.ixk.xknote.advice;

import javax.servlet.http.HttpServletRequest;
import me.ixk.xknote.http.ResponseInfo;
import me.ixk.xknote.utils.GitUtil.GitException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 全局异常处理
 *
 * @author Otstar Lin
 * @date 2020/12/4 下午 1:07
 */
@ControllerAdvice
public class RequestExceptionAdvice extends ResponseEntityExceptionHandler {

    /**
     * 处理请求体异常
     *
     * @param ex      异常
     * @param headers 头字段
     * @param status  状态
     * @param request 请求
     *
     * @return 响应实体
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request
    ) {
        return ResponseInfo.stdError(
            String.format("Parameter not found. (%s)", ex.getMessage()),
            HttpStatus.BAD_REQUEST
        );
    }

    /**
     * 处理请求参数不存在异常
     *
     * @param ex      异常
     * @param headers 头字段
     * @param status  状态
     * @param request 请求
     *
     * @return 响应实体
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
        MissingServletRequestParameterException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request
    ) {
        return ResponseInfo.stdError(
            String.format("Parameter not found. (%s)", ex.getParameterName()),
            HttpStatus.BAD_REQUEST
        );
    }

    /**
     * 处理 Git 异常
     *
     * @param request 请求
     * @param ex      异常
     *
     * @return 响应实体
     */
    @ExceptionHandler(GitException.class)
    public ResponseEntity<Object> handleGitException(
        HttpServletRequest request,
        GitException ex
    ) {
        return ResponseInfo.stdError(
            String.format("Git process error. (%s)", ex.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
