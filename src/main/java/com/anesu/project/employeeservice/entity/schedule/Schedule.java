package com.anesu.project.employeeservice.entity.schedule;

import static com.anesu.project.employeeservice.entity.schedule.ScheduleViewGenerator.generateCalendarView;

import com.anesu.project.employeeservice.entity.shift.ShiftType;
import java.time.LocalDate;
import java.util.List;

public class Schedule {

  private Long employeeId;
  private LocalDate startDate;
  private LocalDate endDate;
  private int daysOff;
  private int workingDays;
  private int workingHours;
  private ShiftType shiftType;
  private List<String> calendarView;

  public Schedule(
      Long employeeId,
      LocalDate startDate,
      LocalDate endDate,
      int daysOff,
      int workingDays,
      int workingHours,
      ShiftType shiftType) {
    this.employeeId = employeeId;
    this.startDate = startDate;
    this.endDate = endDate;
    this.daysOff = daysOff;
    this.workingDays = workingDays;
    this.workingHours = workingHours;
    this.shiftType = shiftType;
    this.calendarView = generateCalendarView(this);
  }

  // Getters and setters
  public Long getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(Long employeeId) {
    this.employeeId = employeeId;
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

  public int getDaysOff() {
    return daysOff;
  }

  public void setDaysOff(int daysOff) {
    this.daysOff = daysOff;
  }

  public int getWorkingDays() {
    return workingDays;
  }

  public void setWorkingDays(int workingDays) {
    this.workingDays = workingDays;
  }

  public int getWorkingHours() {
    return workingHours;
  }

  public void setWorkingHours(int workingHours) {
    this.workingHours = workingHours;
  }

  public ShiftType getShiftType() {
    return shiftType;
  }

  public void setShiftType(ShiftType shiftType) {
    this.shiftType = shiftType;
  }

  public List<String> getCalendarView() {
    return calendarView;
  }

  public void setCalendarView(List<String> calendarView) {
    this.calendarView = calendarView;
  }

  // To String Method for better display of Schedule information
  @Override
  public String toString() {
    return "Schedule [employeeId="
        + employeeId
        + ", startDate="
        + startDate
        + ", endDate="
        + endDate
        + ", daysOff="
        + daysOff
        + ", workingDays="
        + workingDays
        + ", workingHours="
        + workingHours
        + ", shiftType="
        + shiftType
        + ", calendarView="
        + calendarView
        + "]";
  }
}
