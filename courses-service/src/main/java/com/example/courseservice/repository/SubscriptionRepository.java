package com.example.courseservice.repository;

import com.example.courseservice.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
  boolean existsByCourseIdAndStudentId(Long courseId, Long studentId);
  List<Subscription> findByStudentId(Long studentId);
  List<Subscription> findByCourseId(Long courseId);
}
