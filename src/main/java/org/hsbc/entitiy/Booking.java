package org.hsbc.entitiy;

public class Booking {
    private final String customerName;
    private final int tableSize;
    private final String date;
    private final String time;
    private int orderNumber;

    public Booking(String customerName, int tableSize, String date, String time) {
        this.customerName = customerName;
        this.tableSize = tableSize;
        this.date = date;
        this.time = time;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName(){
        return customerName;
    }
    public int getTableSize(){
        return tableSize;
    }
    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getOrderNumber(){
        return orderNumber;
    }
}
