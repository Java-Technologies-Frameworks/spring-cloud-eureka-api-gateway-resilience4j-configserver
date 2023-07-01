package com.jtf.paymentservice.api.controller;

import com.jtf.paymentservice.api.entity.Payment;
import com.jtf.paymentservice.api.service.PaymentService;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/doPayment")
    @Retry(name = "PAYMENT-SERVICE", fallbackMethod = "paymentProcess")
    public Payment doPayment(@RequestBody Payment payment) {
        return paymentService.doPayment(payment);
    }

    // for gateway impl
    @GetMapping("/{orderId}")
    public Payment findPaymentHistoryByOrderId(@PathVariable int orderId) {
        return paymentService.findPaymentHistoryByOrderId(orderId);
    }

    //fallback resilience4j method retry for doPayment() post method
    public Payment paymentProcess(Exception ee) {
        return new Payment(999, "success", "TXN12345", 123, 15000);
    }

    //fallback resilience4j method retry for findPaymentHistoryByOrderId() GET method
    @Retry(name = "PAYMENT-SERVICE", fallbackMethod = "findPaymentHistoryByOrderId")
    public Payment findPaymentHistoryByOrderId(Exception exp){
        return new Payment(999, "success", "TXN12345", 123, 15000);
    }
}
