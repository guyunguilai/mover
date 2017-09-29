package cn.ssic.moverlogic.net2request;

/**
 * Created by Administrator on 2016/10/20.
 */

public class JobType {
    String type;
    String mintime;
    double moveSize;
    String timeIncrements;
    String minimumTime;
    String estime;

    public String getMinimumtime() {
        return minimumTime;
    }

    public void setMinimumtime(String minimumtime) {
        this.minimumTime = minimumtime;
    }

    public String getEstime() {
        return estime;
    }

    public void setEstime(String estime) {
        this.estime = estime;
    }

    public double getMovesize() {
        return moveSize;
    }

    public void setMovesize(double movesize) {
        this.moveSize = movesize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMintime() {
        return mintime;
    }

    public void setMintime(String mintime) {
        this.mintime = mintime;
    }

    public String getTimeincrements() {
        return timeIncrements;
    }

    public void setTimeincrements(String timeincrements) {
        this.timeIncrements = timeincrements;
    }
}
