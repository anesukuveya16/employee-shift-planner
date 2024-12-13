package com.anesu.project.employeeservice.service.exception;

public class ShiftRequestNotFoundException extends RuntimeException {
  public ShiftRequestNotFoundException(String message) {
    super(message);
  }
}
