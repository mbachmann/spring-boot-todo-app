package com.example.todo.rest.advice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalRestControllerAdviceTest {

    private GlobalRestControllerAdvice advice;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private ConstraintViolationException constraintViolationException;

    @Mock
    private MissingServletRequestParameterException missingServletRequestParameterException;

    @Mock
    private HttpMediaTypeNotSupportedException httpMediaTypeNotSupportedException;

    @Mock
    private HttpMessageNotReadableException httpMessageNotReadableException;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        advice = new GlobalRestControllerAdvice();
    }

    @Test
    void testHandleMethodArgumentNotValid() {
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("objectName", "field", "error message")));
        when(bindingResult.getGlobalErrors()).thenReturn(List.of());

        ResponseEntity<ErrorMessage> response = advice.handleMethodArgumentNotValid(methodArgumentNotValidException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getErrors().contains("field, error message"));
    }

    @Test
    void testHandleConstraintViolationException() {
        ConstraintViolation<?> violation = new ConstraintViolation<>() {
            @Override
            public String getMessage() {
                return "must not be null";
            }

            @Override
            public String getMessageTemplate() {
                return null;
            }

            @Override
            public Object getRootBean() {
                return null;
            }

            @Override
            public Class<Object> getRootBeanClass() {
                return null;
            }

            @Override
            public Object getLeafBean() {
                return null;
            }

            @Override
            public Object[] getExecutableParameters() {
                return new Object[0];
            }

            @Override
            public Object getExecutableReturnValue() {
                return null;
            }

            @Override
            public Object getInvalidValue() {
                return null;
            }

            @Override
            public Path getPropertyPath() {
                return null;
            }

            @Override
            public jakarta.validation.metadata.ConstraintDescriptor<?> getConstraintDescriptor() {
                return null;
            }

            @Override
            public <U> U unwrap(Class<U> type) {
                return null;
            }
        };

        ConstraintViolationException exception = new ConstraintViolationException(Set.of(violation));

        ResponseEntity<ErrorMessage> response = advice.handleConstraintViolatedException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getErrors().contains("must not be null"));
    }


    @Test
    void testHandleMissingServletRequestParameterException() {
        when(missingServletRequestParameterException.getParameterName()).thenReturn("param");
        when(missingServletRequestParameterException.getMessage()).thenReturn("is required");

        ResponseEntity<ErrorMessage> response = advice.handleMissingServletRequestParameterException(missingServletRequestParameterException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getErrors().contains("param, is required"));
    }

    @Test
    void testHandleHttpMediaTypeNotSupported() {
        when(httpMediaTypeNotSupportedException.getContentType()).thenReturn(null);
        when(httpMediaTypeNotSupportedException.getSupportedMediaTypes()).thenReturn(List.of());

        ResponseEntity<ErrorMessage> response = advice.handleHttpMediaTypeNotSupported(httpMediaTypeNotSupportedException);

        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testHandleHttpMessageNotReadable() {
        Throwable cause = new RuntimeException("Invalid JSON");
        when(httpMessageNotReadableException.getMostSpecificCause()).thenReturn(cause);

        ResponseEntity<ErrorMessage> response = advice.handleHttpMessageNotReadable(httpMessageNotReadableException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getErrors().contains("Invalid JSON"));
    }
}
