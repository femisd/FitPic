package com.example.offlinemaps;

public class Challenges {

    private int progress;
    private String challengeName;
    private int challengeLimit;
    private String challengeFormat;
    private int points;

    public Challenges() {
    }

    public Challenges(int progress, String challengeName, int challengeLimit, String challengeFormat, int points) {
        this.progress = progress;
        this.challengeName = challengeName;
        this.challengeLimit = challengeLimit;
        this.challengeFormat = challengeFormat;
        this.points = points;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public int getChallengeLimit() {
        return challengeLimit;
    }

    public void setChallengeLimit(int challengeLimit) {
        this.challengeLimit = challengeLimit;
    }

    public String getChallengeFormat() {
        return challengeFormat;
    }

    public void setChallengeFormat(String challengeFormat) {
        this.challengeFormat = challengeFormat;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
