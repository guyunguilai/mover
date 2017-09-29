package cn.ssic.moverlogic.net2request.getRosters;

import java.util.List;

import cn.ssic.moverlogic.net2request.NTRespBean;

/**
 * Created by Administrator on 2016/9/9.
 */
public class NTGetRostersRespBean extends NTRespBean{
    public Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        public List<Jobs> jobs;

        public List<Jobs> getJobs() {
            return jobs;
        }

        public void setJobs(List<Jobs> jobs) {
            this.jobs = jobs;
        }
    }
    public class Jobs{
        public String address;
        public int movesize;
        public String estime;
        public String moveDate;
        public String startTime;
        public String week;
        public int status;
        public int type;
        public String title;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        /*Alerts list中刚开始查询出来都有蓝色newjobs、黄色jobchanges以及红色cancellations的job list
        绿色available now的job则是蓝色newjobs的第一条job数据
        如果蓝色newjobs中状态发生改变，则会归类到黄色jobchanges中
        status等于0，则job颜色为蓝色newjobs
        status等于1,则job颜色为黄色jobchanges
        status等于3,则job颜色为红色cancellations*/

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getMovesize() {
            return movesize;
        }

        public void setMovesize(int movesize) {
            this.movesize = movesize;
        }

        public String getEstime() {
            return estime;
        }

        public void setEstime(String estime) {
            this.estime = estime;
        }

        public String getMoveDate() {
            return moveDate;
        }

        public void setMoveDate(String moveDate) {
            this.moveDate = moveDate;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }
    }
}
