package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    //@Autowired
    OrderRepository orderRepository = new OrderRepository();

    public void addOrder(Order order){
        orderRepository.addOrderTODb(order);
    }

    public void addPartner(String partnerId){
        orderRepository.addPartnerToDb(partnerId);
    }

    public void addOrderPartnerPair(String orderId, String partnerId){
        orderRepository.addOrderPartnerPairToDb(orderId,partnerId);
    }

    public Order getOrderById(String orderId){
        return orderRepository.getOrderByIdFromDb(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId){
        return orderRepository.getPartnerByIdFromDb(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId){
        return orderRepository.getOrderCountByPartnerIdFromDb(partnerId);
    }

    public List<String> getOrdersByPartnerId(String partnerId){
        return orderRepository.getOrdersByPartnerIdFromDb(partnerId);
    }

    public List<String> getAllOrders(){
        return orderRepository.getAllOrdersFromDb();
    }

    public Integer getCountOfUnassignedOrders(){
        return orderRepository.getCountOfUnassignedOrdersFromDb();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId){
        return orderRepository.getOrdersLeftAfterGivenTimeByPartnerIdFromDb(time,partnerId);
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId){
        return orderRepository.getLastDeliveryTimeByPartnerIdFromDb(partnerId);
    }

    public void deletePartnerById(String partnerId){
        orderRepository.deletePartnerByIdFromDb(partnerId);
    }

    public void deleteOrderById(String orderId){
        orderRepository.deleteOrderByIdFromDb(orderId);
    }
}
