package com.dhlee.web.async;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

//@WebServlet(urlPatterns = "/AsyncServlet", asyncSupported = true)
public class AsyncServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		handle(request, response);
	}
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		handle(request, response);
	}
	
	private void handle(HttpServletRequest request,
			HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		System.out.println("AsyncServlet Start::Name="
				+ Thread.currentThread().getName() + "::ID="
				+ Thread.currentThread().getId());

		request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);

		AsyncContext asyncCtx = request.startAsync();
		AppAsyncListener asyncListener = new AppAsyncListener();
		asyncCtx.addListener(asyncListener);
		asyncCtx.setTimeout(30000); // Async Timeout 30 secs

//		ThreadPoolExecutor executor = (ThreadPoolExecutor) request
//				.getServletContext().getAttribute("executor");
//		executor.execute(new AsyncRequestProcessor(asyncCtx));
//		
		asyncCtx.start(new AsyncRequestProcessor(asyncCtx));
		
		long endTime = System.currentTimeMillis();
		System.out.println("AsyncServlet End::Name="
				+ Thread.currentThread().getName() + "::ID="
				+ Thread.currentThread().getId() + "::Time Taken="
				+ (endTime - startTime) + " ms.");

	}

}
