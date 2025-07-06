package com.example.courseservice.dto;

import java.math.BigDecimal;

public record CourseRequest(
  String  title,
  String  description,
  BigDecimal price 
) { }
