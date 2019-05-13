package com.example.offlinemaps;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GalleryItemTest {

    private GalleryItem galleryItem = new GalleryItem();

    @Test
    public void testGetAndSetName() {
        galleryItem.setImageName("name");
        assertEquals("name", galleryItem.getImageName());
    }

    @Test
    public void testGetAndSet() {
        String image = "imageURI";
        galleryItem.setImageUri(image);
        assertEquals("imageURI", galleryItem.getImageUri());
    }

    @Test
    public void testGetAndSetIsSelected() {
        galleryItem.setSelected(true);
        assertTrue(galleryItem.isSelected());
    }

    @Test
    public void testSelfieToString() {
        assertEquals("GalleryItem{imageUri='null', imageName='null', isSelected=false}", galleryItem.toString());
    }
}