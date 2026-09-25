package com.pigopi.vault.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {

	private static final long serialVersionUID = 1L;

	@ExceptionHandler(value = ResourceAlreadyExistException.class)
	public ResponseEntity<ErrorResponse> handleResourceAlredyExistException(ResourceAlreadyExistException ex) {
		ErrorResponse resp = new ErrorResponse();
		resp.setStatusCode(HttpStatus.CONFLICT.value());
		resp.setErrorMessage(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(resp, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(value = UnAuthorizedException.class)
	public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnAuthorizedException ex) {
		ErrorResponse resp = new ErrorResponse();
		resp.setStatusCode(HttpStatus.UNAUTHORIZED.value());
		resp.setErrorMessage(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(resp, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(value = ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
		ErrorResponse resp = new ErrorResponse();
		resp.setErrorMessage(ex.getMessage());
		resp.setStatusCode(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<ErrorResponse>(resp, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMehtodArgumentNotValidException(
			MethodArgumentNotValidException ex) {
		Map<String, String> map = new HashMap<>();
		BindingResult obj = ex.getBindingResult();
		List<FieldError> errorList = obj.getFieldErrors();
		for (FieldError fError : errorList) {
			map.put(fError.getField(), fError.getDefaultMessage());
		}
		return new ResponseEntity<Map<String, String>>(map, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		ErrorResponse response = new ErrorResponse();
		response.setErrorMessage(ex.getMessage());
		response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
