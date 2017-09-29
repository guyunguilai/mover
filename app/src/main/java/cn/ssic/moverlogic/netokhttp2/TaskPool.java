package cn.ssic.moverlogic.netokhttp2;



import com.orhanobut.logger.Logger;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/8/29.
 */
public class TaskPool {
    private LinkedList<RunningTask> mQueue = new LinkedList<RunningTask>();
    private ExecutorService mExecutorService ;
    private RunningTask taskNow;
    public TaskPool(){
        mExecutorService = Executors.newCachedThreadPool();
    }

    //另类的单例模式
    public static TaskPool getInstance(){
        return NewTaskPool.instance;
    }
    public static class NewTaskPool{
        public static TaskPool instance = new TaskPool();
    }

    public void excute(RunningTask task){
        taskNow = task;
        task.setLiftCycleListener(new RunningTask.TaskLifeCycleListener() {
            @Override
            public void onStart(RunningTask task) {
                mQueue.add(task);
            }

            @Override
            public void onFinish(RunningTask task) {
                mQueue.remove(task);
            }
        });

        mExecutorService.execute(task);
        System.out.println("thread pool now : "+mExecutorService.toString());
    }

    RunningTask findTaskWithTag(Object tag){
        for (RunningTask task : mQueue){
            if (tag == task.getTag()){
                return task;
            }
        }
        return null;
    }

    void cancleTask(LoadingHandler handler,Object tag){//cancel的过程task可能还没被加入队列
        RunningTask task =  findTaskWithTag(tag);//返回的task也就同样可能为空
        if (null == task){
            if (null != taskNow){//cancel的过程同样taskNow也可能没被赋值
                taskNow.onTimeAccessHandler(handler);
                taskNow.cancle();
            }
        }else {
            task.cancle();
        }
    }
}
