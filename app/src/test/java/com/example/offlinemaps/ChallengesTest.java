package com.example.offlinemaps;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChallengesTest {

    private Challenges challenges = new Challenges();

    @Test
    public void testGetAndSetProgress() {
        challenges.setProgress(400);
        assertEquals(400, challenges.getProgress());
    }

    @Test
    public void testGetAndSetChallengeName() {
        challenges.setChallengeName("challengeName");
        assertEquals("challengeName", challenges.getChallengeName());
    }

    @Test
    public void testGetAndChallengeLimit() {
        challenges.setChallengeLimit(50);
        assertEquals(50, challenges.getChallengeLimit());
    }

    @Test
    public void testGetAndSetChallengeFormat() {
        challenges.setChallengeFormat("challengeFormat");
        assertEquals("challengeFormat", challenges.getChallengeFormat());
    }

    @Test
    public void testGetAndSetPoints() {
        challenges.setPoints(500);
        assertEquals(500, challenges.getPoints());
    }
}