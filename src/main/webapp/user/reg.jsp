<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注册页面</title>
<script type="text/javascript">
	$(function() {
		$('#user_reg_regForm').form({
			url : 'userAction!reg.action',
			success : function(r) {
				var obj = $.parseJSON(r);
				if (obj.success) {
					$('#user_reg_regDialog').dialog('close');
				}
				$.messager.show({
					title : '提示信息',
					msg : obj.msg,
					showType : 'slide'
				});
			}
		});
		//增加回车提交表单功能
		$('#user_reg_regForm input').bind('keyup', function(event) {
			if (event.keyCode == '13') {
				$('#user_reg_regForm').submit;
			}
		});
	});
	function regAdmin() {
		//$('#index_regForm').submit();
		$('#user_reg_regForm').submit();
		/* if ($('#admin_reg_regForm').form('validate')) {
			$.ajax({
				url : 'adminAction!reg.action',
				data : $('#admin_reg_regForm').serialize(),
				dataType : 'json',
				success : function(r) {
					//var obj = $.parseJSON(r);
					if (r.success) {
						$('#admin_reg_regDialog').dialog('close');
					}
					$.messager.show({
						title : '提示信息',
						msg : r.msg
					//showType : 'slide'
					});
				}
			});
		} else {
			$.messager.show({
				title : '提示信息',
				msg : '表单验证不通过'
			});
		} */
	}
</script>
</head>
<body>
	<div id="user_reg_regDialog" class="easyui-dialog" style="width: 250px;" data-options="title:'注册',modal:true,closed:true,buttons:[{
				text:'注册',
				iconCls:'icon-edit',
				handler:function(){
					regAdmin();
				}
			}]">
		<form id="user_reg_regForm" method="post">
			<table>
				<tr>
					<th>登录名</th>
					<td><input name="name" class="easyui-validatebox" data-options="required:true,missingMessage:'用户名必填'"></td>
				</tr>
				<tr>
					<th>密码</th>
					<td><input name="pwd" type="password" class="easyui-validatebox" data-options="required:true"></td>
				</tr>
				<tr>
					<th>重复密码</th>
					<td><input name="repwd" type="password" class="easyui-validatebox" data-options="required:true,validType:'eqPassword[\'#user_reg_regForm input[name=pwd]\']'"></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>