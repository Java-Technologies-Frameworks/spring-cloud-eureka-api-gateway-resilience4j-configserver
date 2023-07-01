package com.jtf.paymentservice.api.repository;

import com.jtf.paymentservice.api.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Integer> {
    // added for gtwy one more get method
    Payment findByOrderId(int orderId);
}
