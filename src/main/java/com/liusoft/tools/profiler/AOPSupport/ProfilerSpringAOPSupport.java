package com.liusoft.tools.profiler.AOPSupport;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Profiler spring 接入类，实现spring中配置监控方法
 * Created with IntelliJ IDEA.
 * User: liukunyang
 * Date: 13-11-9
 * Time: 上午10:20
 * To change this template use File | Settings | File Templates.
 */
public class ProfilerSpringAOPSupport extends SpringBeanPostProcessor {

    private ApplicationContext applicationContext;

    private Map<String,String[]> monitorMethod;
    private MonitorBeanFactory monitorBeanFactory = new MonitorBeanFactory();


    @Override
    public Map<String, String[]> getMonitorMethodMapping() {
        return monitorMethod;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected Object replaceMonitorBean(Object bean, String beanName) {
        Object monitorBean =  monitorBeanFactory.getInstance( bean.getClass() , monitorMethod.get(beanName) );
        applicationContext.getAutowireCapableBeanFactory().autowireBeanProperties(monitorBean, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME,false);

        return monitorBean;
    }

    public void setMonitorMethod(Map monitorMethod) {
        Map<String,String[]> newMonitorMethod = new HashMap<String,String[]>();

        Iterator keyIt =  monitorMethod.keySet().iterator();
        String key = null;
        String value = null;
        while( keyIt.hasNext() ){

            key = (String) keyIt.next();
            value = (String) monitorMethod.get(key);
            newMonitorMethod.put( key , value.split(",")  );

        }

        this.monitorMethod = newMonitorMethod;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
