
<%
	session.removeAttribute("user");
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
	response.sendRedirect(basePath + "index.jsp");
%>