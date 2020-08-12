package com.maces.ecommerce.skcashandcarry.Model;

import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("name")
    private String name;

    @SerializedName("brand")
    private String brand;

    @SerializedName("price")
    private String price;

    @SerializedName("description")
    private String description;

    @SerializedName("product_image")
    private String product_image;

    @SerializedName("size")
    private String size;

    @SerializedName("weight")
    private String weight;

    @SerializedName("InStock")
    private String InStock;

    @SerializedName("InTransit")
    private String InTransit;

    @SerializedName("brand_id")
    private String brand_id;

    @SerializedName("category_id")
    private String category_id;

    @SerializedName("p1")
    private String p1;

    @SerializedName("p2")
    private String p2;

    @SerializedName("p3")
    private String p3;
    @SerializedName("p4")
    private String p4;
    @SerializedName("p5")
    private String p5;

    @SerializedName("id")
    private int id;


    public Movie() {
    }

    public Movie(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getP1() {
        return p1;
    }

    public void setP1(String p1) {
        this.p1 = p1;
    }

    public String getP2() {
        return p2;
    }

    public void setP2(String p2) {
        this.p2 = p2;
    }

    public String getP3() {
        return p3;
    }

    public void setP3(String p3) {
        this.p3 = p3;
    }

    public String getP4() {
        return p4;
    }

    public void setP4(String p4) {
        this.p4 = p4;
    }

    public String getP5() {
        return p5;
    }

    public void setP5(String p5) {
        this.p5 = p5;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getInStock() {
        return InStock;
    }

    public void setInStock(String inStock) {
        InStock = inStock;
    }

    public String getInTransit() {
        return InTransit;
    }

    public void setInTransit(String inTransit) {
        InTransit = inTransit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
