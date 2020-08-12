package com.maces.ecommerce.skcashandcarry.Model;

public class Cart_Class {

    String Name, Image,price,Brand,product_id;
    long Product_Quantity;
    long Quantity;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public long getProduct_Quantity() {
        return Product_Quantity;
    }

    public void setProduct_Quantity(long product_Quantity) {
        Product_Quantity = product_Quantity;
    }

    public long getQuantity() {
        return Quantity;
    }

    public void setQuantity(long quantity) {
        Quantity = quantity;
    }

    public Cart_Class() {
    }

    public Cart_Class(String name, String image, String price, String brand, String product_id, long quantity) {
        Name = name;
        Image = image;
        this.price = price;
        Brand = brand;
        Quantity = quantity;
        this.product_id=product_id;
    }

    public Cart_Class(String name) {
        Name = name;
    }
}