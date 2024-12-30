package com.anesu.project.employeeservice.service;

import com.anesu.project.employeeservice.entity.schedule.Schedule;
import com.anesu.project.employeeservice.entity.shift.ShiftEntry;
import com.anesu.project.employeeservice.entity.shift.ShiftRequest;
import com.anesu.project.employeeservice.entity.shift.ShiftRequestStatus;
import com.anesu.project.employeeservice.model.ScheduleService;
import com.anesu.project.employeeservice.model.repository.ScheduleRepository;
import com.anesu.project.employeeservice.service.exception.InvalidScheduleException;
import com.anesu.project.employeeservice.service.exception.ScheduleNotFoundException;
import com.anesu.project.employeeservice.service.util.ScheduleValidator;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServiceImpl implements ScheduleService {

  private static final String SCHEDULE_NOT_FOUND_EXCEPTION_MESSAGE = "Schedule not found with id: ";

  private final ScheduleRepository scheduleRepository;
  private final ScheduleValidator scheduleValidator;

  public ScheduleServiceImpl(
      ScheduleRepository scheduleRepository, ScheduleValidator scheduleValidator) {
    this.scheduleRepository = scheduleRepository;
    this.scheduleValidator = scheduleValidator;
  }

  @Override
  public Schedule createSchedule(Schedule schedule) {
    scheduleValidator.validate(schedule);
    return scheduleRepository.save(schedule);
  }

  @Override
  public Schedule updateSchedule(Long scheduleId, Schedule updatedSchedule) {

    Schedule existingScheduleToUpdate =
        getScheduleById(scheduleId)
            .orElseThrow(
                () ->
                    new ScheduleNotFoundException(
                        SCHEDULE_NOT_FOUND_EXCEPTION_MESSAGE + scheduleId));

    Schedule updatedExsistingSchedule =
        updateExsistingSchedule(updatedSchedule, existingScheduleToUpdate);

    scheduleValidator.validate(updatedExsistingSchedule);

    return scheduleRepository.save(updatedExsistingSchedule);
  }

  @Override
  public Schedule addShiftToSchedule(Long employeeId, ShiftRequest approvedShiftRequest) {

    validateApprovedShiftRequest(approvedShiftRequest);

    Optional<Schedule> scheduleInApprovedShiftCalendarWeek =
        getScheduleInApprovedShiftCalendarWeek(employeeId, approvedShiftRequest);

    Schedule schedule =
        scheduleInApprovedShiftCalendarWeek.isPresent()
            ? addNewShiftEntryToSchedule(approvedShiftRequest, scheduleInApprovedShiftCalendarWeek)
            : createNewScheduleForApprovedShiftCalendarWeek(approvedShiftRequest, employeeId);

    return scheduleRepository.save(schedule);
  }

  @Override
  public Optional<Schedule> getScheduleById(Long scheduleId) {
    return scheduleRepository.findById(scheduleId);
  }

  @Override
  public Optional<List<Schedule>> getSchedulesByEmployeeAndDateRange(
      Long employeeId, LocalDate startDate, LocalDate endDate) {
    return scheduleRepository.findByEmployeeIdAndDateRange(employeeId, startDate, endDate);
  }

  @Override
  public void deleteSchedule(Long scheduleId) {
    if (!scheduleRepository.existsById(scheduleId)) {
      throw new ScheduleNotFoundException(SCHEDULE_NOT_FOUND_EXCEPTION_MESSAGE + scheduleId);
    }
    scheduleRepository.deleteById(scheduleId);
  }

  private Schedule updateExsistingSchedule(Schedule updatedSchedule, Schedule existingSchedule) {
    existingSchedule.setStartDate(updatedSchedule.getStartDate());
    existingSchedule.setEndDate(updatedSchedule.getEndDate());
    existingSchedule.setShifts(updatedSchedule.getShifts());
    existingSchedule.setVacations(updatedSchedule.getVacations());
    existingSchedule.setTotalWorkingHours(updatedSchedule.getTotalWorkingHours());
    return existingSchedule;
  }

  private void validateApprovedShiftRequest(ShiftRequest approvedShiftRequest) {
    if (!ShiftRequestStatus.APPROVED.equals(approvedShiftRequest.getStatus())) {
      throw new InvalidScheduleException(
          "Invalid schedule operation. ShiftRequest with ID: "
              + approvedShiftRequest.getId()
              + " is PENDING. Only approved shifts can be added to the schedule.");
    }
  }

  private Optional<Schedule> getScheduleInApprovedShiftCalendarWeek(
      Long employeeId, ShiftRequest approvedShiftRequest) {
    LocalDateTime startOfShiftCalenderWeek =
        approvedShiftRequest.getShiftDate().with(DayOfWeek.MONDAY);
    LocalDateTime endOfShiftCalendarWeek =
        approvedShiftRequest.getShiftDate().with(DayOfWeek.SUNDAY);

    return scheduleRepository.findByEmployeeIdAndCalendarWeek(
        employeeId, startOfShiftCalenderWeek, endOfShiftCalendarWeek);
  }

  private Schedule createNewScheduleForApprovedShiftCalendarWeek(
      ShiftRequest approvedShiftRequest, Long employeeId) {

    List<ShiftEntry> shiftEntries = new ArrayList<>();
    shiftEntries.add(ShiftEntry.from(approvedShiftRequest));

    return new Schedule()
        .builder()
        .employeeId(employeeId)
        .startDate(approvedShiftRequest.getShiftDate())
        .endDate(determineShiftEndDate(approvedShiftRequest))
        .totalWorkingHours(approvedShiftRequest.getShiftLengthInHours())
        .shifts(shiftEntries)
        .build();
  }

  private LocalDateTime determineShiftEndDate(ShiftRequest approvedShiftRequest) {
    return approvedShiftRequest
        .getShiftDate()
        .plusHours(approvedShiftRequest.getShiftLengthInHours());
  }

  private Schedule addNewShiftEntryToSchedule(
      ShiftRequest approvedShiftRequest, Optional<Schedule> schedulesInApprovedShiftCalendarWeek) {
    Schedule schedule = schedulesInApprovedShiftCalendarWeek.get();
    schedule.getShifts().add(ShiftEntry.from(approvedShiftRequest));
    return schedule;
  }
}
