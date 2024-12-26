package com.anesu.project.employeeservice.entity;

import com.anesu.project.employeeservice.entity.schedule.Schedule;
import com.anesu.project.employeeservice.entity.vacation.VacationRequest;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Entity
public class Employee {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(updatable = false, unique = true, nullable = false)
  private UUID personUuid;

  @Column(updatable = false, nullable = false)
  private LocalDate birthDate;

  private String firstName;
  private String lastName;
  private String phone;
  private String address;
  private String email;

  @OneToOne(
      mappedBy = "employee",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private Schedule schedule;

  @OneToMany(
      mappedBy = "employee",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<VacationRequest> vacationRequests;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UUID getPersonUuid() {
    return personUuid;
  }

  public void setPersonUuid(UUID personUuid) {
    this.personUuid = personUuid;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Schedule getSchedule() {
    return schedule;
  }

  public void setSchedule(Schedule employeeSchedule) {
    this.schedule = employeeSchedule;
  }

  public List<VacationRequest> getVacationRequests() {
    return vacationRequests;
  }

  public void setVacationRequests(List<VacationRequest> vacationRequests) {
    this.vacationRequests = vacationRequests;
  }
}
