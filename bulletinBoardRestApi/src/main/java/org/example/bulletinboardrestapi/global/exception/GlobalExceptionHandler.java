package org.example.bulletinboardrestapi.global.exception;

import org.example.bulletinboardrestapi.global.app.AppConfig;
import org.example.bulletinboardrestapi.global.dto.RsData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice // @ControllerAdvice + @ResponseBody : 모든 컨트롤러에 적용되는 예외처리
public class GlobalExceptionHandler {

    @ExceptionHandler({NoSuchElementException.class, NoResourceFoundException.class})
    public ResponseEntity<RsData<Void>> handle(NoSuchElementException e) {

        // 개발 모드에서만 작동되도록.
        if(AppConfig.isNotProd()) e.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        new RsData<>(
                                "404-1",
                                "해당 데이터가 존재하지 않습니다"
                        )
                );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RsData<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        // 개발 모드에서만 작동되도록.
        if(AppConfig.isNotProd()) e.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new RsData<>(
                                "400-1",
                                "Json(요청) 형식이 잘못되었습니다."
                        )
                );
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    @Transactional(rollbackFor = HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<RsData<Void>> handleInternalServerErrorException(HttpServerErrorException.InternalServerError e) {

        // 개발 모드에서만 작동되도록.
        if(AppConfig.isNotProd()) e.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new RsData<>(
                                "500-1",
                                "서버 에러"
                        )
                );
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RsData<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        String message = e.getBindingResult().getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + " : " + fe.getCode() + " : "  + fe.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining("\n"));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new RsData<>(
                                "400-1",
                                message
                        )
                );
    }


    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<RsData<Void>> ServiceExceptionHandle(ServiceException ex) {

        // 개발 모드에서만 작동되도록.
        if(AppConfig.isNotProd()) ex.printStackTrace();

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(
                        new RsData<>(
                                ex.getCode(),
                                ex.getMsg()
                        )
                );
    }

}
