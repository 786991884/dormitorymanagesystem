<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>宿舍管理系统</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="js/jquery-easyui-1.3.5/jquery.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/jquery.cookie.js"></script>
<script type="text/javascript" src="js/jquery-easyui-1.3.5/jquery.easyui.min.js"></script>
<link rel="stylesheet" id="themeLink" type="text/css" href="js/jquery-easyui-1.3.5/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.3.5/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="style/extEasyUIIcon.css" />
<script type="text/javascript" src="js/jquery-easyui-1.3.5/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="js/xubhutil.js"></script>
<script type="text/javascript" src="js/jquery.ajaxfileupload.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css">
<script type="text/javascript">
	$(function() {

	});
</script>
</head>
<body class="easyui-layout">
	<div data-options="region:'north',border:false" style="height: 50px; overflow: hidden;">
		<jsp:include page="layout/north.jsp"></jsp:include>
	</div>
	<div data-options="region:'south'" style="height: 20px; overflow: hidden;">
		<jsp:include page="layout/south.jsp"></jsp:include>
	</div>
	<div data-options="region:'west',split:true" style="width: 200px; overflow: hidden">
		<jsp:include page="layout/west.jsp"></jsp:include>
	</div>
	<div data-options="region:'east',title:'日历',split:true,collapsed:true" style="width: 200px; overflow: hidden;">
		<jsp:include page="layout/east.jsp"></jsp:include>
	</div>
	<div data-options="region:'center'" style="overflow: hidden">
		<jsp:include page="layout/center.jsp"></jsp:include>

	</div>
	<jsp:include page="user/login.jsp"></jsp:include>
	<jsp:include page="user/reg.jsp"></jsp:include>
</body>
</html>