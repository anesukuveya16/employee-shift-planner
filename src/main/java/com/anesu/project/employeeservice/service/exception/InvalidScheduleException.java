package com.anesu.project.employeeservice.service.exception;

public class InvalidScheduleException extends RuntimeException {
  public InvalidScheduleException(String message) {
    super(message);
  }
}
