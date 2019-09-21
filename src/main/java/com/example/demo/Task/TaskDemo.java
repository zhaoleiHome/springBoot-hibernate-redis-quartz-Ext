package com.example.demo.Task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskDemo implements Job{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.error("定时任务执行了");
    }
}
