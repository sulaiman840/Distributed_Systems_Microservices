package com.example.courseservice.dto;

public record CourseResponse(
  Long   id,
  Long   teacherId,
  String title,
  String description , 
  boolean approved
) { }
