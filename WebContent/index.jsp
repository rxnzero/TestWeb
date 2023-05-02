<%@ page import="com.dhlee.message.StandardMessage" %>
<%@ page import="com.dhlee.message.manager.StandardMessageManager" %>
<%@ page import="com.dhlee.service.InterfaceMapper" %>
<%@ page import="com.dhlee.service.InterfaceMessage" %>
<%
	out.println("---- test jsp called<br>");
	StandardMessageManager manager = StandardMessageManager.getInstance();
	StandardMessage standardMessage = manager.getStandardMessage();
	InterfaceMapper mapper = manager.getMapper();
	
	
	InterfaceMessage interfaceMessage = new InterfaceMessage();
	interfaceMessage.setStandardMessage(standardMessage);
	interfaceMessage.setMapper(mapper);
	
	out.println(">> Init.");
	out.println("getTranCode = "+interfaceMessage.getMapper().getTranCode(interfaceMessage.getStandardMessage()) +"<br>");
	out.println("getEaiSvcCode = "+ interfaceMessage.getMapper().getEaiSvcCode(standardMessage) +"<br>");
	out.println("standardMessage = "+interfaceMessage.getStandardMessage().toPrettyJson() +"<br>");
	
	String changed = "ELK";
	out.println("\n>> Change : setTranCode = "+ changed +"<br>");
	interfaceMessage.getMapper().setTranCode(interfaceMessage.getStandardMessage(), changed);
	out.println("getTranCode = "+mapper.getTranCode(interfaceMessage.getStandardMessage()) +"<br>");
	out.println("getEaiSvcCode = "+ interfaceMessage.getMapper().getEaiSvcCode(standardMessage) +"<br>");
	out.println("standardMessage = "+ interfaceMessage.getStandardMessage().toPrettyJson() +"<br>");
%>