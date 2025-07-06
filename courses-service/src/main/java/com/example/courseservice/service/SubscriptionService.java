package com.example.courseservice.service;

import com.example.courseservice.model.Course;
import com.example.courseservice.model.Subscription;
import com.example.courseservice.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {
  private final SubscriptionRepository subRepo;
  private final CourseService courseService;
  private final PaymentClientService payClient;

  public SubscriptionService(
      SubscriptionRepository subRepo,
      CourseService courseService,PaymentClientService payClient
  ) {
    this.subRepo      = subRepo;
    this.courseService = courseService;
        this.payClient = payClient;
  }

  public void subscribe(Long courseId, Long studentId) {
    Course c = courseService.get(courseId);
    if (!c.isApproved()) {
      throw new IllegalStateException("Course not approved yet");
    }
       if (!payClient.hasPaid(courseId, studentId)) {
      throw new IllegalStateException("Payment required before subscribing");
    }
    if (subRepo.existsByCourseIdAndStudentId(courseId, studentId)) {
      throw new IllegalStateException("Already subscribed");
    }
    Subscription sub = new Subscription();
    sub.setCourseId(courseId);
    sub.setStudentId(studentId);
    subRepo.save(sub);
  }

  public List<Course> getCoursesForStudent(Long studentId) {
    return subRepo.findByStudentId(studentId).stream()
      .map(s -> courseService.get(s.getCourseId()))
      .filter(Course::isApproved)
      .collect(Collectors.toList());
  }

  public List<Long> getStudentIdsForCourse(Long courseId) {
    return subRepo.findByCourseId(courseId).stream()
      .map(Subscription::getStudentId)
      .collect(Collectors.toList());
  }
}
