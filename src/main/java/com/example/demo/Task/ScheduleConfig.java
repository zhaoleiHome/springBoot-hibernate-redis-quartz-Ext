package com.example.demo.Task;

import org.quartz.Scheduler;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;


@Configuration
public class ScheduleConfig implements ApplicationListener<ContextRefreshedEvent> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JobFactory jobFactory;
    @Autowired
    @Qualifier("dataSource")
    private DataSource primaryDataSource;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        logger.info(this.getClass() + "：任务已经启动....");
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        //获取配置属性
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        //在quartz.properties中的属性被读取并注入后再初始化对象
        propertiesFactoryBean.afterPropertiesSet();
        //创建SchedulerFactoryBean
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setQuartzProperties(propertiesFactoryBean.getObject());
        //使用数据源，自定义数据源
        factory.setDataSource(this.primaryDataSource);
        factory.setJobFactory(jobFactory);
        factory.setWaitForJobsToCompleteOnShutdown(true);//当spring要关闭的时候会等待所有的任务执行完毕之后才会真正的关闭
        factory.setOverwriteExistingJobs(false);
        factory.setStartupDelay(1);
        return factory;
    }

    /*
     * 通过SchedulerFactoryBean获取Scheduler的实例
     */
    @Bean(name = "scheduler")
    public Scheduler scheduler() throws IOException {
        return schedulerFactoryBean().getScheduler();
    }

    @Bean
    public QuartzInitializerListener executorListener() {
        return new QuartzInitializerListener();

    }
}
