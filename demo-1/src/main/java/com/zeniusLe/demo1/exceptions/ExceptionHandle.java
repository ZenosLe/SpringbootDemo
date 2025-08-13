package com.zeniusLe.demo1.exceptions;

import com.zeniusLe.demo1.NormallizeApiResponse.ApiResponse;
import com.zeniusLe.demo1.NormallizeApiResponse.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ExceptionHandle {
    // cơ bản
    @ExceptionHandler(value = RuntimeException.class) // khi có 1 kiểu runtime exception xảy ra thì nó sẽ tập trung về đây
    ResponseEntity<String> handleException(RuntimeException exception){
        return ResponseEntity.badRequest().body(exception.getMessage()); // trả về nội dung muốn cần
    }

    // nâng cao
    @ExceptionHandler(value = AppExceptions.class)
    ResponseEntity<ApiResponse> HandleAppException(AppExceptions Appexception){
        ErrorCode errorCode =Appexception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity
                .status(errorCode.getStatusCode()) // trả về HttpStatusCode thay vì là bad_Request
                .body(apiResponse); // trả về nội dung muốn cần
    }

    @ExceptionHandler(value = AccessDeniedException.class) // lỗi này là 403
    // nếu như có AccessDeniedException xảy ra => được sử lý bởi handle này
    ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException exception){
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleValidation(MethodArgumentNotValidException validationException){
        String enumKey = validationException.getFieldError().getDefaultMessage(); // lấy ra tin nhắn trả về khi bị lỗi
        ErrorCode errorCode = ErrorCode.valueOf(enumKey); //
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }


}
