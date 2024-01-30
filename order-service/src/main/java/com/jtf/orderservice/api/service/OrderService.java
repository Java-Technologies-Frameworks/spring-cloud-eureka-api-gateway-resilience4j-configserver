package com.jtf.orderservice.api.service;

import com.jtf.orderservice.api.common.Payment;
import com.jtf.orderservice.api.common.TransactionRequest;
import com.jtf.orderservice.api.common.TransactionResponse;
import com.jtf.orderservice.api.entity.Order;
import com.jtf.orderservice.api.repository.OrderRepository;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RefreshScope
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    @Lazy   // added for config server
    private RestTemplate restTemplate;
    // key value copied from cloud config remote repository
    @Value("${microservice.payment-service.endpoints.endpoint.uri}")
    private String PAYMENT_ENDPOINT_URL;

//    public Order saveOrder(Order order) {
//        return orderRepository.save(order);
//    }

    @Retry(name = "ORDER-SERVICE", fallbackMethod = "paymentDoneAndOrderPlaced")
    public TransactionResponse saveOrder(TransactionRequest request) {
        String resMessage = "";
        Order order = request.getOrder();
        Payment payment = request.getPayment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getPrice());
        //rest call
        //Payment paymentResponse = restTemplate.postForObject("http://localhost:9191/payment/doPayment", payment, Payment.class);

        /*instaed of mapping the host and port map the service name */
        //Payment paymentResponse = restTemplate.postForObject("http://PAYMENT-SERVICE/payment/doPayment", payment, Payment.class);
        // added for config server properties read from git repo
        Payment paymentResponse = restTemplate.postForObject(PAYMENT_ENDPOINT_URL, payment, Payment.class);
        resMessage = paymentResponse.getPaymentStatus().equals("success") ? "payment processing is sucessful and order placed" : "there is a failure in payment API order added to cart";

        orderRepository.save(order);
        return new TransactionResponse(order, paymentResponse.getTransactionId(), paymentResponse.getAmount(), resMessage);
    }

    public TransactionResponse paymentDoneAndOrderPlaced(Exception e) {
        return new TransactionResponse(new Order(999, "Order placed", 1, 15000), "TNX12345", 15000, "payment processing is sucessful and order placed");
    }
}
