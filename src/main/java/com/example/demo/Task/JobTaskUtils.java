package com.example.demo.Task;

import com.example.demo.DataBase.TaskTable;
import com.example.demo.DataBase.im.TaskNote;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
@Configuration
public class JobTaskUtils {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    /**
     *  比较简单的不需要cron表达式即可实现的定时任务
     * @param jobName  任务名字
     * @param jobGroupName  任务组名字
     * @param triggerName  触发器名字
     * @param triggerGroupName   触发器组名字
     * @param jobClass  任务类
     * @param time    intevalTime时间间隔
     * @param count 执行几次
     */
    public void simpleTaskDemo(String jobName,String jobGroupName,
                                      String triggerName,String triggerGroupName,
                                      Class jobClass,int time,int count,TaskNote taskNote){
        TaskTable taskTable = taskNote.findTaskNote(jobName);
        if(taskTable!=null){
            return;
        }
        TaskTable taskTable1 = new TaskTable();
        taskTable1.setTask_name(jobName);
        taskTable1.setCreate_at(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        taskNote.save(taskTable1);
        //获取一个调度器工厂类
        Scheduler scheduler = null;
        try{
            //通过工厂类获取一个调度器
            scheduler = schedulerFactory.getScheduler();
            //获取jobDetail，添加实现类和设置任务的名字
            JobDetail job = JobBuilder.newJob(jobClass).withIdentity(jobName,jobGroupName).build();
            //定义调度触发规则
            //触发器
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName,triggerGroupName)
                    .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(time).withRepeatCount(count)).startNow().build();
            scheduler.scheduleJob(job,trigger);
            scheduler.start();
        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param jobName  任务的名字
     * @param jobGroupName  任务组的名字
     * @param triggerName  //触发器的名字
     * @param triggerGroupName //触发器组的名字
     * @param cron //表达式
     * @param jobClass  //任务类
     */
    public void taskDemo(String jobName,String jobGroupName,String triggerName,String triggerGroupName,String cron,Class jobClass,TaskNote taskNote){
        logger.error(jobName);
        TaskTable taskTable = taskNote.findTaskNote(jobName);
        if(taskTable!=null){
            return;
        }
        TaskTable taskTable1 = new TaskTable();
        taskTable1.setTask_name(jobName);
        taskTable1.setCreate_at(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        taskNote.save(taskTable1);
        //获取一个调度器工厂类
        Scheduler scheduler = null;
        try{
            //通过工厂类获取一个调度器
            scheduler = schedulerFactory.getScheduler();
            //获取jobDetail，添加实现类和设置任务的名字
            JobDetail job = JobBuilder.newJob(jobClass).withIdentity(jobName,jobGroupName).build();
            //定义调度触发规则
            //触发器
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName,triggerGroupName)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron)).startNow().build();
            scheduler.scheduleJob(job,trigger);
            scheduler.start();
        }catch (Exception e ){
            e.printStackTrace();
        }
    }
    public void shutdownJobs(){
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            if (!sched.isShutdown()) {
                sched.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
