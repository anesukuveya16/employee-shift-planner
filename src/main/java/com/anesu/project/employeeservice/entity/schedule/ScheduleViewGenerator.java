package com.anesu.project.employeeservice.entity.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ScheduleViewGenerator {

  /**
   * Generates a list of calendar entries for a given schedule. Each entry specifies whether the day
   * is a working day or a day off.
   *
   * @param schedule the schedule object containing details like start and end date, shift type,
   *     etc.
   * @return a list of strings representing the calendar view for the schedule
   */
  public static List<String> generateCalendarView(Schedule schedule) {
    List<String> calendar = new ArrayList<>();
    LocalDate currentDate = schedule.getStartDate();

    while (!currentDate.isAfter(schedule.getEndDate())) {
      // Check if the current date is a working day or day off
      if (isDayOff(currentDate)) {
        calendar.add(currentDate.toString() + ": Day Off");
      } else {
        calendar.add(
            currentDate.toString() + ": Working Day (" + schedule.getShiftType().name() + ")");
      }
      currentDate = currentDate.plusDays(1); // Move to the next day
    }

    return calendar;
  }

  /**
   * Determines if a given date is a day off. By default, weekends (Saturday and Sunday) are
   * considered days off.
   *
   * @param date the date to check
   * @return true if the date is a day off, false otherwise
   */
  private static boolean isDayOff(LocalDate date) {
    // You can modify this method to check for holidays or other custom off days
    return date.getDayOfWeek() == java.time.DayOfWeek.SATURDAY
        || date.getDayOfWeek() == java.time.DayOfWeek.SUNDAY;
  }
}
