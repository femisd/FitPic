package com.example.offlinemaps;

/**
 * This class represents a single item in the gallery.
 */
public class GalleryItem {
    public String imageUri;
    public String imageName;
    public boolean isSelected = false;

    public GalleryItem() {
    }

    public GalleryItem(String imageUri, String imageName) {
        this.imageUri = imageUri;
        this.imageName = imageName;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "GalleryItem{" +
                "imageUri='" + imageUri + '\'' +
                ", imageName='" + imageName + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}