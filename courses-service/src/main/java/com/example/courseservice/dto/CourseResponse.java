package com.example.courseservice.dto;

import java.math.BigDecimal;

public record CourseResponse(
  Long   id,
  Long   teacherId,
  String title,
  String description , 
  BigDecimal price , 
  boolean approved
) { }
