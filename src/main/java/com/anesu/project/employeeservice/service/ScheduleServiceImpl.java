package com.anesu.project.employeeservice.service;

import com.anesu.project.employeeservice.entity.schedule.Schedule;
import com.anesu.project.employeeservice.model.ScheduleService;
import com.anesu.project.employeeservice.model.repository.ScheduleRepository;
import com.anesu.project.employeeservice.service.exception.ScheduleNotFoundException;
import com.anesu.project.employeeservice.service.util.ScheduleValidator;
import java.time.LocalDate;
import java.util.List;
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

    Schedule existingScheduleToUpdate = getScheduleById(scheduleId);

    Schedule updateExsistingSchedule =
        updateExsistingSchedule(updatedSchedule, existingScheduleToUpdate);

    scheduleValidator.validate(updateExsistingSchedule);

    return scheduleRepository.save(updateExsistingSchedule);
  }

  @Override
  public Schedule getScheduleById(Long scheduleId) {
    return scheduleRepository
        .findById(scheduleId)
        .orElseThrow(
            () -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND_EXCEPTION_MESSAGE + scheduleId));
  }

  @Override
  public List<Schedule> getSchedulesByEmployeeAndDateRange(
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
}
