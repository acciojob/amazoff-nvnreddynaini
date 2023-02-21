package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

    HashMap<String,Order> orderdb = new HashMap<>();
    HashMap<String,DeliveryPartner> deliveryPartnerdb = new HashMap<>();
    HashMap<String, List<String>> partnerOrderDb = new HashMap<>();
    HashMap<String,String> orderToPartnerDb = new HashMap<>();


    public void addOrderTODb(Order order){
        orderdb.put(order.getId(), order);
    }

    public void addPartnerToDb(String partnerId){
        deliveryPartnerdb.put(partnerId,new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPairToDb(String orderId, String partnerId){
        if(orderdb.containsKey(orderId) && deliveryPartnerdb.containsKey(partnerId)){
            List<String> orderList;
            if(partnerOrderDb.containsKey(partnerId)){
                orderList = partnerOrderDb.get(partnerId);
            }else{
                orderList = new ArrayList<>();
            }
            orderList.add(orderId);
            partnerOrderDb.put(partnerId,orderList);
            orderToPartnerDb.put(orderId,partnerId); //To get the count of unassigned orders below.

            //update the no of orders in partner object
            DeliveryPartner deliveryPartner = deliveryPartnerdb.get(partnerId);
            deliveryPartner.setNumberOfOrders(orderList.size());

        }
    }

    public Order getOrderByIdFromDb(String orderId){
        if(orderdb.containsKey(orderId)){
            return orderdb.get(orderId);
        }else{
            return null;
        }
    }

    public DeliveryPartner getPartnerByIdFromDb(String partnerId){
        if(deliveryPartnerdb.containsKey(partnerId)){
            return deliveryPartnerdb.get(partnerId);
        }else{
            return null;
        }
    }

    public Integer getOrderCountByPartnerIdFromDb(String partnerId){
        if(partnerOrderDb.containsKey(partnerId)){
            return partnerOrderDb.get(partnerId).size();
            //countOfOrders=deliveryPartnerdb.get(partnerId).getNumberOfOrders();
        }else{
            return 0;
        }
    }

    public List<String> getOrdersByPartnerIdFromDb(String partnerId){
        if(partnerOrderDb.containsKey(partnerId)){
            return partnerOrderDb.get(partnerId);
        }else{
            return null;
        }
    }

    public List<String> getAllOrdersFromDb(){
        return new ArrayList<>(orderdb.keySet());
    }

    public Integer getCountOfUnassignedOrdersFromDb(){
        return orderdb.size()-orderToPartnerDb.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerIdFromDb(String time, String partnerId){
        int currTime = Integer.parseInt(time.substring(0,2)) * 60 + Integer.parseInt(time.substring(3));
        int count = 0;
        for(String orderId : partnerOrderDb.get(partnerId)){
            if(orderdb.get(orderId).getDeliveryTime() > currTime){
                count++;
            }
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerIdFromDb(String partnerId){
        int lastDelivery = Integer.MIN_VALUE;
        if(partnerOrderDb.containsKey(partnerId)){
            for(String orderId : partnerOrderDb.get(partnerId)){
                if(orderdb.get(orderId).getDeliveryTime() > lastDelivery){
                    lastDelivery = orderdb.get(orderId).getDeliveryTime();
                }
            }
        }
        int hour = lastDelivery/60;
        int min = lastDelivery%60;

        String strHours = Integer.toString(hour);
        if(strHours.length()==1){
            strHours = "0"+strHours;
        }

        String strMin = Integer.toString(min);
        if(strMin.length()==1){
            strMin = "0"+strMin;
        }
        return strHours+":"+strMin;
    }

    public void deletePartnerByIdFromDb(String partnerId){
        List<String> ordersList = partnerOrderDb.get(partnerId);
        for(String orderId : ordersList){
            orderToPartnerDb.remove(orderId);
        }
        partnerOrderDb.remove(partnerId);
        deliveryPartnerdb.remove(partnerId);
    }

    public void deleteOrderByIdFromDb(String orderId){
        orderdb.remove(orderId);
        if(orderToPartnerDb.containsKey(orderId)) {
            String partnerId = orderToPartnerDb.get(orderId);
            orderToPartnerDb.remove(orderId);
            partnerOrderDb.get(partnerId).remove(orderId);
            deliveryPartnerdb.get(partnerId).setNumberOfOrders(partnerOrderDb.get(partnerId).size());
        }
    }
}
