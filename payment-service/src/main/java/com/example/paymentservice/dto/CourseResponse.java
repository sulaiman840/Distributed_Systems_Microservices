package com.example.paymentservice.dto;

import java.math.BigDecimal;

public record CourseResponse(
  Long id,
  Long teacherId,
  String title,
  String description,
  boolean approved,
  BigDecimal price
) { }
