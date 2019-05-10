package com.example.offlinemaps;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserTest {

    private User user = new User();
    @Test
    public void testGetAndSetProfilePicture() {
        user.setmProfilePicture("");
        assertEquals("", user.getmProfilePicture());
    }

    @Test
    public void testGetAndSetUsername() {
        user.setmUsername("Mike");
        assertEquals("Mike", user.getmUsername());
    }

    @Test
    public void testGetAndSetLocation() {
        user.setmLocation("Bournemouth");
        assertEquals("Bournemouth", user.getmLocation());
    }

    @Test
    public void testGetAndSetSteps() {
        user.setmSteps(100);
        assertEquals(100, user.getmSteps());
    }

    @Test
    public void testGetAndSetCalories() {
        user.setmCaloriesBurned(100.00);
        assertEquals(100.00, user.getmCaloriesBurned(), 0.00);
    }

    @Test
    public void testGetAndsSetPhotos() {
        user.setmPhotos(100);
        assertEquals(100, user.getmPhotos());
    }

    @Test
    public void testGetAndSetFollowers() {
        user.setmFollowers(100);
        assertEquals(100, user.getmFollowers());
    }

    @Test
    public void testGetAndSetFollowing() {
        user.setmFollowing(1000);
        assertEquals(1000, user.getmFollowing());
    }

    @Test
    public void testGetAndSetPoints() {
        user.setmPoints(100);
        assertEquals(100, user.getmPoints());
    }

    @Test
    public void testGetAndSetVIPstatusTrue() {
        user.setmVIP(true);
        assertTrue(user.getmVIP());
    }

    @Test
    public void testGetAndSetVIPstatusFalse() {
        user.setmVIP(false);
        assertFalse(user.getmVIP());
    }
}