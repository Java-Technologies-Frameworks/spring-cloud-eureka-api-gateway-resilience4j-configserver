package com.jtf.paymentservice.api.service;

import com.jtf.paymentservice.api.entity.Payment;
import com.jtf.paymentservice.api.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Random;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

//    public Payment doPayment(Payment payment) {
//        payment.setTransactionId(UUID.randomUUID().toString());
//        return paymentRepository.save(payment);
//    }

    // 2nd scenarios
    public Payment doPayment(Payment payment) {
        payment.setPaymentStatus(paymentProcessing());
        payment.setTransactionId(UUID.randomUUID().toString());
        return paymentRepository.save(payment);
    }

    public String paymentProcessing() {
        //api should be 3rd party payment gateway
        return new Random().nextBoolean() ? "success" : "false";
    }

    public Exception paymentProcessingForResilienceTest() {
        //api should be 3rd party payment gateway
        return new Exception("payment processing is fail due to low network connectivity");
    }

    //after gateway impl
    public Payment findPaymentHistoryByOrderId(int orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
}
