package com.maces.ecommerce.skcashandcarry.Model;

public class Order_Model {
    String Status, Price, Date, image_url, Quantity, Method, id,ProductName;

    public Order_Model() {
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public Order_Model(String status, String price, String date, String image_url, String quantity, String method, String id, String productName) {
        Status = status;
        Price = price;
        Date = date;
        this.image_url = image_url;
        Quantity = quantity;
        Method = method;
        this.id = id;
        ProductName = productName;
    }
}