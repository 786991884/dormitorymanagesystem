<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(function() {
		var data = eval("(" + '${userResources}' + ")");
		$('#user_resources_userResources').tree({
			data : data,
			parentField : 'pid',
			onLoadSuccess : function() {
				$.messager.progress('close');
				if (data.length < 1) {
					$('#userResources').append('<img src="${pageContext.request.contextPath}/style/images/blue_face/bluefaces_35.png" alt="您没有任何权限" /><div>您没有任何权限</div>');
				}
			}
		});
	});
</script>
<c:if test="${sessionInfo.name == null}">
	<img src="${pageContext.request.contextPath}/style/images/blue_face/bluefaces_35.png" alt="" />
	<div>登录已超时，请重新登录，然后再刷新本功能！</div>
	<script type="text/javascript" charset="utf-8">
		try {
			$.messager.progress('close');
		} catch (e) {
		}
	</script>
</c:if>
<c:if test="${sessionInfo.name != null}">
	<ul id="user_resources_userResources"></ul>
</c:if>
