/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.criteria;

import java.util.LinkedList;

/**
 * 包含多个排序条件
 * @author 西安捷然
 */
public class OrderCriteria {
    private LinkedList<Order> orders = new LinkedList<>();

    public LinkedList<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }
}