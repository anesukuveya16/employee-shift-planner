package com.anesu.project.employeeservice.entity.vacation;

import com.anesu.project.employeeservice.entity.Employee;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class VacationRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id", nullable = false)
  private Employee employee;

  private Long officeLocationId;
  private LocalDate startDate;
  private LocalDate endDate;

  @Enumerated(EnumType.STRING)
  private VacationRequestStatus status;

  private String rejectionReason;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public Long getOfficeLocationId() {
    return officeLocationId;
  }

  public void setOfficeLocationId(Long officeLocationId) {
    this.officeLocationId = officeLocationId;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public VacationRequestStatus getStatus() {
    return status;
  }

  public void setStatus(VacationRequestStatus status) {
    this.status = status;
  }

  public String getRejectionReason() {
    return rejectionReason;
  }

  public void setRejectionReason(String rejectionReason) {
    this.rejectionReason = rejectionReason;
  }
}
