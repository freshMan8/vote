package com.tencent.wxcloudrun.config;

import com.tencent.wxcloudrun.contants.CommonConstant;
import com.tencent.wxcloudrun.dto.ApiResponse;
import com.tencent.wxcloudrun.exception.VoteException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 异常处理器
 *
 * @author DL
 * @date 2023/3/8
 */
@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebExceptionHandler {

    /**
     * 创建响应Entity
     *
     * @param errCode   错误码
     * @param errMsg    错误描述
     * @return 响应
     */
    private ResponseEntity<Object> createResponseEntity(int errCode, String errMsg) {
        return new ResponseEntity<>(ApiResponse.error(errCode, errMsg),
                HttpStatus.OK
        );
    }

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    protected ResponseEntity<Object> handleBindException(Exception ex, HttpServletRequest request) {

        // 分隔符
        String delimiter = ",";

        String errMsg = "";
        if (ex instanceof BindException) {
            // 错误描述
            errMsg = ((BindException) ex).getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(delimiter));
        }
        else if (ex instanceof MethodArgumentNotValidException) {
            errMsg = ((MethodArgumentNotValidException) ex).getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(delimiter));
        }
        log.error("handle err:{}", errMsg);
        return createResponseEntity(CommonConstant.ErrorCode.SVR, errMsg);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception ex, HttpServletRequest request) {

        log.error("handle err", ex);
        if (ex instanceof NullPointerException) {
            // 空指针
            return createResponseEntity(CommonConstant.ErrorCode.SVR, "服务器内部异常");
        }
        String errMsg = StringUtils.trim(ex.getMessage());
        if (ex instanceof SQLIntegrityConstraintViolationException) {
            // 数据库重复
            return createResponseEntity(CommonConstant.ErrorCode.DATA, "数据重复");
        }
        if (ex instanceof VoteException) {
            return createResponseEntity(((VoteException) ex).getErrCode(), ((VoteException) ex).getErrMsg());
        }
        // 未知异常
        return createResponseEntity(CommonConstant.ErrorCode.SVR, "服务器内部异常");
    }
}
