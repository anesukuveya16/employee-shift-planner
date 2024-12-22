package com.anesu.project.employeeservice.service.exception;

public class InvalidVacationRequestException extends RuntimeException {
  public InvalidVacationRequestException(String msg) {
    super(msg);
  }
}
