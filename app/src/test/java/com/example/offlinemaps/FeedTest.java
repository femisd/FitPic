package com.example.offlinemaps;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FeedTest {

    private Feed feed = new Feed();

    @Test
    public void testGetAndSetUsername() {
        feed.setUsername("Mike");
        assertEquals("Mike", feed.getUsername());
    }

    @Test
    public void testGetAndSetDate() {
        feed.setDate("dd/mm/yyyy");
        assertEquals("dd/mm/yyyy", feed.getDate());
    }

    @Test
    public void testGetAndSetLocation() {
        feed.setLocation("Bournemouth");
        assertEquals("Bournemouth", feed.getLocation());
    }

    @Test
    public void testGetAndSetImage() {
        feed.setImage(434472742);
        assertEquals(434472742, feed.getImage());
    }

    @Test
    public void testSelfieToString() {
        assertEquals("Feed{username='null', date='null', location='null', image=0}",feed.toString());
    }
}