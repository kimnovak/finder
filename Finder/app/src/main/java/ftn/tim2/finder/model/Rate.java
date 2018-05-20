package ftn.tim2.finder.model;

public class Rate {

    private int count;
    private int score;

    public Rate() {
    }

    public Rate(int count, int score) {
        this.count = count;
        this.score = score;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
