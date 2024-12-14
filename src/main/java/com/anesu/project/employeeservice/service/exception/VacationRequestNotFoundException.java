package com.anesu.project.employeeservice.service.exception;

public class VacationRequestNotFoundException extends RuntimeException {
  public VacationRequestNotFoundException(String message) {
    super(message);
  }
}
