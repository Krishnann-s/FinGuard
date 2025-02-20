package com.main.dto;

import java.time.LocalDate;

import lombok.Data;
@Data
public class GoalDto {
  private int userId;
  private double targetAmount;
  private double currentAmount;
  private String description;
  private LocalDate targetDate;
}
