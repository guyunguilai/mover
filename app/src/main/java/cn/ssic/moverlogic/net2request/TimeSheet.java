package cn.ssic.moverlogic.net2request;

import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 */

public class TimeSheet {
    List<Pause> pause;
    FinishTime finishTime;
    StartTimes startTimes;
    TotalChargeTime totalChargeTime;

    public List<Pause> getPause() {
        return pause;
    }

    public void setPause(List<Pause> pause) {
        this.pause = pause;
    }

    public FinishTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(FinishTime finishTime) {
        this.finishTime = finishTime;
    }

    public StartTimes getStartTimes() {
        return startTimes;
    }

    public void setStartTimes(StartTimes startTimes) {
        this.startTimes = startTimes;
    }

    public TotalChargeTime getTotalChargeTime() {
        return totalChargeTime;
    }

    public void setTotalChargeTime(TotalChargeTime totalChargeTime) {
        this.totalChargeTime = totalChargeTime;
    }
}
