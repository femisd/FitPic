package com.example.offlinemaps;

/**
 * This class represents a single item in the gallery.
 */
public class GalleryItem {
    public String imageUri;
    public String imageName;
    public boolean isSelected = false;

    public GalleryItem(String imageUri, String imageName) {
        this.imageUri = imageUri;
        this.imageName = imageName;
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