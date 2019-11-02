package com.example.customermain;

import java.util.ArrayList;

public class OrderItems {

    String orderKey;
    String orderNum;
    String orderTime;
    ArrayList<String> orderItemList;
    ArrayList<Integer> orderItemCount;
    String orderCanteenId;
    String orderPrice;
    String orderItems;
    String orderUserId;

    public OrderItems(String orderKey, String orderNum,String orderUserId, String orderTime, ArrayList<String> orderItemList, ArrayList<Integer> orderItemCount, String orderCanteenId, String orderPrice, String orderItems) {
        this.orderKey = orderKey;
        this.orderNum = orderNum;
        this.orderTime = orderTime;
        this.orderItemList = orderItemList;
        this.orderItemCount = orderItemCount;
        this.orderCanteenId = orderCanteenId;
        this.orderPrice = orderPrice;
        this.orderItems = orderItems;
        this.orderUserId = orderUserId;
    }

    public String getOrderUserId() {
        return orderUserId;
    }

    public void setOrderUserId(String orderUserId) {
        this.orderUserId = orderUserId;
    }

    public OrderItems() {
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

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public ArrayList<String> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(ArrayList<String> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public ArrayList<Integer> getOrderItemCount() {
        return orderItemCount;
    }

    public void setOrderItemCount(ArrayList<Integer> orderItemCount) {
        this.orderItemCount = orderItemCount;
    }

    public String getOrderCanteenId() {
        return orderCanteenId;
    }

    public void setOrderCanteenId(String orderCanteenId) {
        this.orderCanteenId = orderCanteenId;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(String orderItems) {
        this.orderItems = orderItems;
    }
}