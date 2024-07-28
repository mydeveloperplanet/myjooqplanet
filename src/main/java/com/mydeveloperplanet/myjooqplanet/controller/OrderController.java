package com.mydeveloperplanet.myjooqplanet.controller;

import java.util.ArrayList;
import java.util.List;

import com.mydeveloperplanet.myjooqplanet.model.OrderArticle;
import com.mydeveloperplanet.myjooqplanet.repository.OrderRepository;
import com.mydeveloperplanet.myjooqplanet.api.OrdersApi;
import com.mydeveloperplanet.myjooqplanet.model.OrderFullData;
import com.mydeveloperplanet.myjooqplanet.repository.dto.OrderArticleIn;
import com.mydeveloperplanet.myjooqplanet.repository.dto.OrderArticleOut;
import com.mydeveloperplanet.myjooqplanet.repository.dto.OrderIn;
import com.mydeveloperplanet.myjooqplanet.repository.dto.OrderOut;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController implements OrdersApi {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public ResponseEntity<Void> createOrder(com.mydeveloperplanet.myjooqplanet.model.Order apiOrder) {
        OrderIn orderIn = new OrderIn(apiOrder.getCustomerId());
        Integer orderId = orderRepository.addOrder(orderIn);

        List<OrderArticleIn> articles = new ArrayList<>();
        for (com.mydeveloperplanet.myjooqplanet.model.OrderArticle apiArticle : apiOrder.getArticles()) {
            articles.add(new OrderArticleIn(apiArticle.getArticleId(), apiArticle.getNumber()));
        }

        orderRepository.addArticles(orderId, articles);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<OrderFullData> getOrder(Long orderId) {
        OrderOut orderOut = orderRepository.getOrder(orderId.intValue());
        return ResponseEntity.ok(repoToApi(orderId, orderOut));
    }

    @Override
    public ResponseEntity<List<Long>> getOrdersOfCustomer(Long customerId) {
        List<Integer> orderIds = orderRepository.getOrdersOfCustomer(customerId.intValue());
        List<Long> apiOrderIds = orderIds.stream().map(Integer::longValue).toList();
        return ResponseEntity.ok(apiOrderIds);
    }

    private OrderFullData repoToApi(Long orderId, OrderOut orderOut) {
        OrderFullData ofd = new OrderFullData();
        ofd.setOrderId(orderId);
        ofd.setCustomerId(orderOut.customerId());
        List<OrderArticle> apiArticles = new ArrayList<>();
        for (OrderArticleOut orderArticleOut : orderOut.articles()) {
            OrderArticle apiArticle = new OrderArticle();
            apiArticle.setArticleId(orderArticleOut.articleId());
            apiArticle.setNumber(orderArticleOut.number());
            apiArticles.add(apiArticle);
        }
        ofd.setArticles(apiArticles);
        return ofd;
    }
}
