package cn.ssic.moverlogic.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/31.
 */
public class StartJobReqBean extends ReqBean implements Serializable{
//    StartJobInnerClass inplay;
//
//    public StartJobInnerClass getInplay() {
//        return inplay;
//    }
//
//    public void setInplay(StartJobInnerClass inplay) {
//        this.inplay = inplay;
//    }
//
//    public class StartJobInnerClass {
        private int jobId;
        private int jobStarted;
        private String startTime;

        public int getJobId() {
            return jobId;
        }

        public void setJobId(int jobId) {
            this.jobId = jobId;
        }

        public int getJobStarted() {
            return jobStarted;
        }

        public void setJobStarted(int jobStarted) {
            this.jobStarted = jobStarted;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }
//    }



}
