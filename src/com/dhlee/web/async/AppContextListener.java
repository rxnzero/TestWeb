package com.dhlee.web.async;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//@WebListener
public class AppContextListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		// Default Timeout Setting 60sec
		ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 200, 60000L,
				TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(100));
		servletContextEvent.getServletContext().setAttribute("executor",
				executor);
//		System.out.println("AppContextListener contextInitialized::set ThreadPoolExecutor");
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		ThreadPoolExecutor executor = (ThreadPoolExecutor) servletContextEvent
				.getServletContext().getAttribute("executor");
		executor.shutdown();
//		System.out.println("AppContextListener contextDestroyed::shutdown ThreadPoolExecutor");
	}

}
