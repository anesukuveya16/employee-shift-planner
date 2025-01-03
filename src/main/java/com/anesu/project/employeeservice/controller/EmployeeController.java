package com.anesu.project.employeeservice.controller;

import com.anesu.project.employeeservice.entity.schedule.Schedule;
import com.anesu.project.employeeservice.entity.shift.ShiftRequest;
import com.anesu.project.employeeservice.entity.vacation.VacationRequest;
import com.anesu.project.employeeservice.model.ScheduleService;
import com.anesu.project.employeeservice.model.ShiftRequestService;
import com.anesu.project.employeeservice.model.VacationRequestService;
import com.anesu.project.employeeservice.service.exception.InvalidScheduleException;
import com.anesu.project.employeeservice.service.exception.ScheduleNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

  private final ScheduleService scheduleService;
  private final ShiftRequestService shiftRequestService;
  private final VacationRequestService vacationRequestService;

  @Autowired
  public EmployeeController(
      ScheduleService scheduleService,
      ShiftRequestService shiftRequestService,
      VacationRequestService vacationRequestService) {
    this.scheduleService = scheduleService;
    this.shiftRequestService = shiftRequestService;
    this.vacationRequestService = vacationRequestService;
  }

  // Schedule operations
  @PostMapping("/{employeeId}/schedule")
  public Schedule createSchedule(@PathVariable Long employeeId, @RequestBody Schedule schedule) {
    if (!employeeId.equals(schedule.getEmployeeId())) {
      throw new InvalidScheduleException(
          "Given employee id is not the same as the employee id in the schedule to be created.");
    }
    return scheduleService.createSchedule(schedule);
  }

  @PutMapping("/{employeeId}/schedule/{scheduleId}")
  public Schedule updateSchedule(
      @PathVariable Long employeeId,
      @PathVariable Long scheduleId,
      @RequestBody Schedule updatedSchedule) {
    return scheduleService.updateSchedule(scheduleId, updatedSchedule);
  }

  @DeleteMapping("/schedule/{scheduleId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteSchedule(@PathVariable Long scheduleId) {
    try {
      scheduleService.deleteSchedule(scheduleId);
    } catch (ScheduleNotFoundException ex) {
      throw new ScheduleNotFoundException(ex.getMessage());
    }
  }

  @GetMapping("/{employeeId}/schedule")
  public List<Schedule> getEmployeeSchedules(
      @PathVariable Long employeeId,
      @RequestParam("startDate") LocalDateTime startDate,
      @RequestParam("endDate") LocalDateTime endDate) {
    return scheduleService
        .getSchedulesByEmployeeAndDateRange(employeeId, startDate, endDate)
        .orElse(Collections.emptyList());
  }

  // Shift request operations
  @PostMapping("/{employeeId}/shift-request")
  public ShiftRequest createShiftRequest(
      @PathVariable Long employeeId, @RequestBody ShiftRequest shiftRequest) {
    return shiftRequestService.createShiftRequest(shiftRequest);
  }

  @PutMapping("/{employeeId}/shift-request/{shiftRequestId}/approve")
  public ShiftRequest approveShiftRequest(
      @PathVariable Long employeeId, @PathVariable Long shiftRequestId) {
    return shiftRequestService.approveShiftRequest(employeeId, shiftRequestId);
  }

  @PutMapping("/{employeeId}/shift-request/{shiftRequestId}/reject")
  public ShiftRequest rejectShiftRequest(
      @PathVariable Long employeeId,
      @PathVariable Long shiftRequestId,
      @RequestParam String rejectionReason) {
    return shiftRequestService.rejectShiftRequest(shiftRequestId, rejectionReason);
  }

  @GetMapping("/{employeeId}/shift-requests")
  public List<ShiftRequest> getEmployeeShiftRequests(@PathVariable Long employeeId) {
    return shiftRequestService.getShiftRequestsByEmployee(employeeId);
  }

  @GetMapping("/{employeeId}/shift-requests/date-range")
  public List<ShiftRequest> getShiftRequestsByDateRange(
      @PathVariable Long employeeId,
      @RequestParam("startDate") LocalDate startDate,
      @RequestParam("endDate") LocalDate endDate) {
    return shiftRequestService.getShiftRequestsByDateRange(startDate, endDate);
  }

  // Vacation request operations
  @PostMapping("/{employeeId}/vacation-request")
  public VacationRequest createVacationRequest(
      @PathVariable Long employeeId, @RequestBody VacationRequest vacationRequest) {
    return vacationRequestService.createVacationRequest(vacationRequest);
  }

  @PutMapping("/{employeeId}/vacation-request/{vacationRequestId}/withdraw")
  public VacationRequest withdrawVacationRequest(
      @PathVariable Long employeeId, @PathVariable Long vacationRequestId) {
    return vacationRequestService.withdrawVacationRequest(vacationRequestId, employeeId);
  }

  @GetMapping("/{employeeId}/vacation-requests")
  public List<VacationRequest> getEmployeeVacationRequests(@PathVariable Long employeeId) {
    return vacationRequestService.getVacationRequestsByEmployeeId(employeeId);
  }

  @GetMapping("/{employeeId}/vacation-requests/date-range")
  public List<VacationRequest> getVacationRequestsByDateRange(
      @PathVariable Long employeeId,
      @RequestParam("startDate") LocalDate startDate,
      @RequestParam("endDate") LocalDate endDate) {
    return vacationRequestService.getVacationRequestsByDateRange(employeeId, startDate, endDate);
  }
}
