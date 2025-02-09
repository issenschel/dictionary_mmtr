package com.example.dictionary_mmtr.controller;

import com.example.dictionary_mmtr.dto.ResponseDto;
import com.example.dictionary_mmtr.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Locale;

@RestControllerAdvice
public class AdviceController {

    private final MessageSource messageSource;

    @Autowired
    public AdviceController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(DictionaryException.class)
    public ResponseEntity<ResponseDto> dictionaryException(DictionaryException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(getLanguageTypeFromRequest(e.getMessage()) + e.getError()));
    }

    @ExceptionHandler(KeyNotFoundException.class)
    public ResponseEntity<ResponseDto> keyNotFoundException(KeyNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(getLanguageTypeFromRequest(e.getMessage())));
    }

    @ExceptionHandler(KeyFoundException.class)
    public ResponseEntity<ResponseDto> keyFoundException(KeyFoundException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDto(getLanguageTypeFromRequest(e.getMessage())));
    }

    @ExceptionHandler(RemoveEntryException.class)
    public ResponseEntity<ResponseDto> removeEntryException(RemoveEntryException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDto(getLanguageTypeFromRequest(e.getMessage()) + e.getError()));
    }

    @ExceptionHandler(AddEntryException.class)
    public ResponseEntity<ResponseDto> addEntryException(AddEntryException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDto(getLanguageTypeFromRequest(e.getMessage()) + e.getError()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResponseDto> validationException(ValidationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDto(getLanguageTypeFromRequest(e.getMessage())));
    }

    private String getLanguageTypeFromRequest(String e) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(e, null, locale);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseDto> handleMethodNotSupported(HttpRequestMethodNotSupportedException e, Locale locale) {
        String errorMessage = messageSource.getMessage("error.method.not.supported",
                new Object[]{e.getMethod(), String.join(", ", e.getSupportedMethods())}, locale);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ResponseDto(errorMessage));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDto> httpMessageNotReadable(Locale locale) {
        String errorMessage = messageSource.getMessage("error.message.not.readable", null, locale);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(errorMessage));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseDto> httpMessageNotReadable(MissingServletRequestParameterException e, Locale locale) {
        String errorMessage = messageSource.getMessage("error.missing.parameter", new Object[]{e.getParameterName()}, locale);
        return ResponseEntity.badRequest().body(new ResponseDto(errorMessage));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseDto> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, Locale locale) {
        if (ex.getCause() instanceof ConversionFailedException) {
            return handleConversionFailedException((ConversionFailedException) ex.getCause(), locale);
        }
        String errorMessage = messageSource.getMessage("error.method.argument.type.mismatch",
                new Object[]{ex.getName(), ex.getRequiredType().getSimpleName()}, locale);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(errorMessage));
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<ResponseDto> handleConversionFailedException(ConversionFailedException ex, Locale locale) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidEnumValueException) {
            return handleInvalidEnumValueException((InvalidEnumValueException) cause, locale);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(messageSource.getMessage("error.conversion.failed", null, locale)));
    }

    @ExceptionHandler(InvalidEnumValueException.class)
    public ResponseEntity<ResponseDto> handleInvalidEnumValueException(InvalidEnumValueException ex, Locale locale) {
        String errorMessage = messageSource.getMessage("error.invalid.enum.value",
                new Object[]{ex.getInvalidValue(), InvalidEnumValueException.getValidValues(ex.getEnumType())}, locale);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(errorMessage));
    }

}
