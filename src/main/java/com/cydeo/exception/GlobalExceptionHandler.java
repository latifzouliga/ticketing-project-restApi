package com.cydeo.exception;

import com.cydeo.annotation.DefaultExceptionMessage;
import com.cydeo.dto.DefaultExceptionMessageDTO;

import com.cydeo.entity.ResponseWrapper;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.ObjectUtils;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
// interceptor: if any exception happens in this application, it needs to be intercepted and come to this class first
public class GlobalExceptionHandler {

    // when an exception comes to this class, one of the 3 methods will be executed

    // any exception related with TicketingProjectException.class
    @ExceptionHandler(TicketingProjectException.class)
    public ResponseEntity<ResponseWrapper> serviceException(TicketingProjectException se) {
        String message = se.getMessage();
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .success(false)
                        .code(HttpStatus.CONFLICT.value())
                        .message(message)
                        .build(),
                HttpStatus.CONFLICT);
    }

    // any exception related with AccessDeniedException.class
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseWrapper> accessDeniedException(AccessDeniedException se) {
        String message = se.getMessage();
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .success(false)
                        .code(HttpStatus.FORBIDDEN.value())
                        .message(message)
                        .build(),
                HttpStatus.FORBIDDEN);
    }

    // validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseWrapper> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
//        Map<String, List<String>> errors = new HashMap<>();
//
//        ex.getBindingResult()
//                .getAllErrors()
//                .forEach(error -> {
//                    String fieldName = ((FieldError) error).getField();
//                    String errorMessage = error.getDefaultMessage();
//
//                    // Collect errors by field name
////                    errors.computeIfAbsent(fieldName, key -> new ArrayList<>()).add(errorMessage);
//                    if (!errors.containsKey(fieldName)){
//                        errors.put(fieldName,new ArrayList<>());
//                    }
//                        errors.get(fieldName).add(errorMessage);
//
//                });

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(
                        ResponseWrapper.builder()
                                .success(false)
                                .message("Validation error")
                                .data(errors)
                                .build());

    }


    // any exception related with these 4 classes
    @ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class, BadCredentialsException.class})
    public ResponseEntity<ResponseWrapper> genericException(HandlerMethod handlerMethod) {

        // the first condition will execute if the exception happens in methods that are annotated with @DefaultExceptionMessage
        Optional<DefaultExceptionMessageDTO> defaultMessage = getMessageFromAnnotation(handlerMethod.getMethod());

        if (defaultMessage.isPresent() && !ObjectUtils.isEmpty(defaultMessage.get().getMessage())) {
            ResponseWrapper response = ResponseWrapper
                    .builder()
                    .success(false)
                    .message(defaultMessage.get().getMessage())
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .success(false)
                        .message("Action failed: An error occurred! You need to be careful bro!!!")
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Optional<DefaultExceptionMessageDTO> getMessageFromAnnotation(Method method) {
        DefaultExceptionMessage defaultExceptionMessage = method.getAnnotation(DefaultExceptionMessage.class);
        if (defaultExceptionMessage != null) {
            DefaultExceptionMessageDTO defaultExceptionMessageDto = DefaultExceptionMessageDTO
                    .builder()
                    .message(defaultExceptionMessage.defaultMessage())
                    .build();
            return Optional.of(defaultExceptionMessageDto);
            //DefaultExceptionMessageDTO message = new DefaultExceptionMessageDTO(defaultExceptionMessage.defaultMessage());
//            return Optional.of(message);
        }
        return Optional.empty();
    }
}