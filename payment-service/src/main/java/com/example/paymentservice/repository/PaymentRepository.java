package com.example.paymentservice.repository;

import com.example.paymentservice.model.Payment;
import com.example.paymentservice.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
  // existing
  Optional<Payment> findByCourseIdAndUserIdAndStatus(
      Long courseId, Long userId, PaymentStatus status);

  // new: any payment (pending or paid)
  Optional<Payment> findByCourseIdAndUserId(Long courseId, Long userId);
}
