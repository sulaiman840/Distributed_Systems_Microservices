package com.example.paymentservice.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "payments")
public class Payment {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false) private Long userId;
  @Column(nullable = false) private Long courseId;
  @Column(nullable = false) private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentStatus status = PaymentStatus.PENDING;

  public Payment() { }
  public Payment(Long userId, Long courseId, BigDecimal amount) {
    this.userId = userId;
    this.courseId = courseId;
    this.amount = amount;
    this.status = PaymentStatus.PENDING;
  }
  public Long getId() { return id; }
  public Long getUserId() { return userId; }
  public void setUserId(Long userId) { this.userId = userId; }
  public Long getCourseId() { return courseId; }
  public void setCourseId(Long courseId) { this.courseId = courseId; }
  public BigDecimal getAmount() { return amount; }
  public void setAmount(BigDecimal amount) { this.amount = amount; }
  public PaymentStatus getStatus() { return status; }
  public void setStatus(PaymentStatus status) { this.status = status; }

  @Override
  public String toString() {
    return "Payment{" +
           "id=" + id +
           ", userId=" + userId +
           ", courseId=" + courseId +
           ", amount=" + amount +
           ", status=" + status +
           '}';
  }
}