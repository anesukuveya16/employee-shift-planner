package com.anesu.project.employeeservice.service;

import com.anesu.project.employeeservice.entity.schedule.Schedule;
import com.anesu.project.employeeservice.model.SchedulingService;
import com.anesu.project.employeeservice.model.repository.ScheduleRepository;
import com.anesu.project.employeeservice.service.exception.ScheduleNotFoundException;
import com.anesu.project.employeeservice.service.util.ScheduleValidator;
import java.time.LocalDate;
import java.util.List;

public class SchedulingServiceImpl implements SchedulingService {

  private final ScheduleRepository scheduleRepository;
  private final ScheduleValidator scheduleValidator;

  public SchedulingServiceImpl(
      ScheduleRepository scheduleRepository, ScheduleValidator scheduleValidator) {
    this.scheduleRepository = scheduleRepository;
    this.scheduleValidator = scheduleValidator;
  }

  @Override
  public Schedule createSchedule(Schedule schedule) {
    scheduleValidator.validateSchedule(schedule);
    return scheduleRepository.save(schedule);
  }

  @Override
  public Schedule updateSchedule(Long scheduleId, Schedule updatedSchedule)
      throws ScheduleNotFoundException {

    Schedule existingSchedule =
        scheduleRepository
            .findById(scheduleId)
            .orElseThrow(
                () -> new ScheduleNotFoundException("Schedule not found with ID: " + scheduleId));

    updateExistingScheduleWithNewData(updatedSchedule, existingSchedule);

    return scheduleRepository.save(existingSchedule);
  }

  @Override
  public List<Schedule> getSchedulesByEmployeeAndDateRange(
      Long employeeId, LocalDate startDate, LocalDate endDate) {
    return scheduleRepository.findSchedulesByEmployeeAndDateRange(employeeId, startDate, endDate);
  }

  @Override
  public List<Schedule> getSchedulesByLocationAndDate(Long officeLocationId, LocalDate date) {
    return scheduleRepository.findSchedulesByLocationAndDate(officeLocationId, date);
  }

  @Override
  public List<Schedule> getSchedulesByEmployeeId(Long employeeId) {
    return scheduleRepository.findAllById(employeeId);
  }

  @Override
  public List<Schedule> integrateVacationRequest(Long vacationRequestId) {
    return scheduleRepository.integrateVacationRequest(vacationRequestId);
  }

  @Override
  public Schedule integrateShiftRequest(Long shiftRequestId) {
    return scheduleRepository.integrateShiftRequest(shiftRequestId);
  }

  @Override
  public boolean deleteSchedule(Long employeeId, LocalDate date) {
    return scheduleRepository.deleteByEmployeeAndDate(employeeId, date) > 0;
  }

  private void updateExistingScheduleWithNewData(
          Schedule updatedSchedule, Schedule existingSchedule) {
    existingSchedule.setStartDate(updatedSchedule.getStartDate());
    existingSchedule.setEndDate(updatedSchedule.getEndDate());
    existingSchedule.setShiftType(updatedSchedule.getShiftType());
    existingSchedule.setDaysOff(updatedSchedule.getDaysOff());
    existingSchedule.setWorkingDays(updatedSchedule.getWorkingDays());
    existingSchedule.setWorkingHours(updatedSchedule.getWorkingHours());
  }
}
