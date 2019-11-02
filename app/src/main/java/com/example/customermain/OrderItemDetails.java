package com.example.customermain;

import java.util.ArrayList;

public class OrderItemDetails {

    String orderCanteenId;
    String orderItems;
    String orderKey;
    String orderNum;
    String orderPrice;
    String orderTime;
    ArrayList<Integer> orderItemCount;
    ArrayList<String> orderItemList;

    public OrderItemDetails() {
    }

    public OrderItemDetails(String orderCanteenId, String orderItems, String orderKey, String orderNum, String orderPrice, String orderTime, ArrayList<Integer> orderItemCount, ArrayList<String> orderItemList) {
        this.orderCanteenId = orderCanteenId;
        this.orderItems = orderItems;
        this.orderKey = orderKey;
        this.orderNum = orderNum;
        this.orderPrice = orderPrice;
        this.orderTime = orderTime;
        this.orderItemCount = orderItemCount;
        this.orderItemList = orderItemList;
    }

    public String getOrderCanteenId() {
        return orderCanteenId;
    }

    public void setOrderCanteenId(String orderCanteenId) {
        this.orderCanteenId = orderCanteenId;
    }

    public String getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(String orderItems) {
        this.orderItems = orderItems;
    }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getorderPrice() {
        return orderPrice;
    }

    public void setorderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public ArrayList<Integer> getOrderItemCount() {
        return orderItemCount;
    }

    public void setOrderItemCount(ArrayList<Integer> orderItemCount) {
        this.orderItemCount = orderItemCount;
    }

    public ArrayList<String> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(ArrayList<String> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
