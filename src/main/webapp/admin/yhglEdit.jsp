<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="ftitle" style="font-size: 14px; font-weight: bold; color: #666; padding: 5px 0; margin-bottom: 10px; border-bottom: 1px solid #ccc;">用户信息</div>
<form id="admin_yhglEdit_form" style="margin: 0; padding: 10px 30px;" method="post">
	<div class="fitem" style="margin-bottom: 5px;">
		<label style="display: inline-block; width: 100px;">编号:</label> <input readonly="readonly" name="id" class="easyui-validatebox" data-options="required:true">
	</div>
	<div class="fitem" style="margin-bottom: 5px;">
		<label style="display: inline-block; width: 100px;">登录名称:</label> <input name="name" class="easyui-validatebox" data-options="required:true">
	</div>
	<div class="fitem" style="margin-bottom: 5px;">
		<label style="display: inline-block; width: 100px;">密码:</label> <input name="pwd" type="password" class="easyui-validatebox" data-options="required:true,validType:'midLength[5,16]'">
	</div>
	<div class="fitem" style="margin-bottom: 5px;">
		<label style="display: inline-block; width: 100px;">创建时间:</label> <input name="createdatetime" class="easyui-datetimebox" data-options="required:true,editable:false">
	</div>
	<div class="fitem" style="margin-bottom: 5px;">
		<label style="display: inline-block; width: 100px;">最后修改时间:</label> <input name="modifydatetime" class="easyui-datetimebox" data-options="required:true,editable:false">
	</div>
</form>