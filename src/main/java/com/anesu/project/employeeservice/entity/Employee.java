package com.anesu.project.employeeservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@MappedSuperclass
public class Employee {
  @Id private Long id;

  @Column(updatable = false)
  private UUID personUuid;

  @Column(updatable = false)
  private LocalDate birthDate;

  private String firstName;
  private String lastName;
  private String phone;
  private String address;

  @Setter private String email;
  @Setter private EmployeeSchedule employeeSchedule;
}
