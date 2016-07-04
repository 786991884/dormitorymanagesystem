<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="ftitle" style="font-size: 14px; font-weight: bold; color: #666; padding: 5px 0; margin-bottom: 10px; border-bottom: 1px solid #ccc;">上传Excel</div>
<form id="stu_importExcel_form" style="margin: 0; padding: 10px 30px;" method="post" enctype="multipart/form-data">
	<div class="fitem" style="margin-bottom: 5px;">
		<label style="display: inline-block; width: 100px;">上传修改的模版数据:</label> <input readonly="readonly" type="file" id="uploadExcel" name="uploadExcel" class="easyui-validatebox" data-options="required:true">
	</div>
</form>
