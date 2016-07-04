<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录页面</title>
<script type="text/javascript">
	$(function() {

		$('#user_login_loginForm').form({
			url : 'userAction!login.action',
			success : function(r) {
				var obj = $.parseJSON(r);
				if (obj.success) {
					$('#user_login_loginDialog').dialog('close');
					$('a[id="userinfo"]').html('[<strong>' + obj.obj.name + '</strong>],欢迎你');
					$("#layout_west_tree3").tree('reload');
				}
				$.messager.show({
					title : '提示信息',
					msg : obj.msg,
					showType : 'slide'
				});
			}
		});
		var sessionInfo_userId = '${sessionInfo.id}';
		if (sessionInfo_userId) {
			/**如果是，如果已经登陆过了，那么刷新页面后也不需要弹出登录窗体*/
			$('#user_login_loginDialog').dialog('close');
		}
		//增加回车提交表单功能
		$('#user_login_loginForm input').bind('keyup', function(event) {
			if (event.keyCode == '13') {
				$('#user_login_loginForm').submit();
			}
		});
		$('#user_login_loginForm input[name=name]').focus();
	});
</script>
</head>
<body>
	<div id="user_login_loginDialog" class="easyui-dialog"
		data-options="title:'登录',modal:true,closable:false,buttons:[{
				text:'注册',
				iconCls:'icon-edit',
				handler:function(){
				  $('#user_reg_regForm').form('load',{
				    name:'',
				    pwd:'',
				    repwd:'',
				  });
				  $('#user_reg_regDialog').dialog('open');
				}
			},{
				text:'登录',
				iconCls:'icon-help',
				handler:function(){
				   $('#user_login_loginForm').submit();
				}
	    	}]">
		<form method="post" id="user_login_loginForm">
			<table>
				<tr>
					<th>登录名</th>
					<td><input name="name" class="easyui-validatebox" data-options="required:true,missingMessage:'登录名必填'"></td>
				</tr>
				<tr>
					<th>密码</th>
					<td><input name="pwd" type="password" class="easyui-validatebox" data-options="required:true,missingMessage:'密码必填'"></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>