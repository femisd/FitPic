package com.example.offlinemaps;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SelfieTest {

    private Selfie selfie = new Selfie();

    @Test
    public void testGetAndSetSelfieId() {
        selfie.setId("1");
        assertEquals("1", selfie.getId());
    }

    @Test
    public void testGetAndSetSelfieLatitude() {
        selfie.setLatitude(54.54);
        assertEquals(54.54, selfie.getLatitude(), 0.00);
    }

    @Test
    public void testGetAndSetSelfieLongitude() {
        selfie.setLongitude(54.54);
        assertEquals(54.54, selfie.getLongitude(), 0.00);
    }

    @Test
    public void testSelfieToString() {
        assertEquals("Selfie{id='null', latitude=0.0, longitude=0.0}",selfie.toString());
    }
}