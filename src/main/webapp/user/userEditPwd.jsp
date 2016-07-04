<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(function() {
		$.messager.progress('close');
		$('#user_editpwd_editCurrentUserPwdForm').form({
			url : '${pageContext.request.contextPath}/userAction!editCurrentUserPwd.action',
			onSubmit : function() {
				$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				var isValid = $(this).form('validate');
				if (!isValid) {
					$.messager.progress('close');
				}
				return isValid;
			},
			success : function(result) {
				$.messager.progress('close');
				result = $.parseJSON(result);
				if (result.success) {
					$.messager.alert('提示', result.msg, 'info');
					$.modalDialog.handler.dialog('close');
				} else {
					$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	});
</script>
<c:if test="${sessionInfo.name != null}">
	<form id="user_editpwd_editCurrentUserPwdForm" method="post">
		<table class="table table-hover table-condensed">
			<tr>
				<th>登录名</th>
				<td>${sessionInfo.name}</td>
			</tr>
			<tr>
				<th>原密码</th>
				<td><input name="oldPwd" type="password" placeholder="请输入原密码" class="easyui-validatebox" data-options="required:true"></td>
			</tr>
			<tr>
				<th>新密码</th>
				<td><input name="pwd" type="password" placeholder="请输入新密码" class="easyui-validatebox" data-options="required:true"></td>
			</tr>
			<tr>
				<th>重复</th>
				<td><input name="rePwd" type="password" placeholder="请再次输入新密码" class="easyui-validatebox" data-options="required:true,validType:'eqPassword[\'#editCurrentUserPwdForm input[name=pwd]\']'"></td>
			</tr>
		</table>
	</form>
</c:if>
