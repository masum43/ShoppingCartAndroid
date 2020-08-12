package com.maces.ecommerce.skcashandcarry.Model;

public class SliderItem {
    String ImageUrl,Description;

    public SliderItem(String imageUrl, String description) {
        ImageUrl = imageUrl;
        Description = description;
    }

    public SliderItem() {
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
