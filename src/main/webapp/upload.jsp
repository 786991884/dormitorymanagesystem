<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上传测试</title>
</head>
<body>
	<s:form action="doUpload" theme="simple" method="post" enctype="multipart/form-data">
		<s:file name="file" label="File" />
		<s:submit />
	</s:form>
</body>
</html>