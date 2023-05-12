package com.dhlee.web.async;

import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.Properties;

import javax.servlet.AsyncContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;

public class AsyncRequestProcessor implements Runnable {
	private AsyncContext asyncContext;

    private String adapterGroupName;
    private String adapterName;
    private String encode = "euc-kr";
    
	public AsyncRequestProcessor() {
	}

	public AsyncRequestProcessor(AsyncContext asyncCtx) {
		this.asyncContext = asyncCtx;
		
		// TODO : define adapter name rule
		this.adapterGroupName = "_EAI_IN_HTT_SyS";
		this.adapterName = "_EAI_IN_HTT_SyS{ALL}";
	}

	@Override
	public void run() {
		System.out.println("Async Supported? "
				+ asyncContext.getRequest().isAsyncSupported());
		
		ServletRequest request = asyncContext.getRequest();
		
		ServletInputStream sis;
		byte[] requestBytes = null;
		byte[] result = null;
		try {
			sis = request.getInputStream();
		
	    	ByteBuffer bb = ByteBuffer.allocate(1024);
		    int i=0;
		    byte[] cbuf = new byte[1024];
		    while( ( i = sis.read(cbuf, 0, 1024) ) != -1 ) {
		    	if (i==1024){
		    		bb.put(cbuf);
		    	}else{
		    		byte[] tail = new byte[i];
		    		System.arraycopy(cbuf, 0, tail, 0, i);
		    		bb.put(tail);
		    	}
		    }
		    requestBytes = new byte[bb.position()];
		    bb.position(0);
		    bb.get(requestBytes);
		    
            Properties prop = new Properties();
            // call Inbound Proxy
            result = service(requestBytes, prop);
			PrintWriter out = asyncContext.getResponse().getWriter();
			String encodedResponse = new String(result, encode);
			out.write(encodedResponse);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			if(asyncContext != null) {
				asyncContext.complete();
			}
		}
	}

	protected byte[] service(byte[] message, Properties prop) throws Exception {
		System.out.println("AsyncRequestProcessor Name="
				+ Thread.currentThread().getName() + ", ID="
				+ Thread.currentThread().getId());
        return "async servlet say - Hello~".getBytes();
    }
}

