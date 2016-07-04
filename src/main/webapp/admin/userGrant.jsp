<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(function() {
		console.log('${user.roleIds}');
		$('#roleIds').combotree({
			url : '${pageContext.request.contextPath}/roleAction!tree.action',
			parentField : 'pid',
			lines : true,
			panelHeight : 'auto',
			multiple : true,
			onLoadSuccess : function() {
				$.messager.progress('close');
				var strs = new Array();
				strs = '${user.roleIds}'.split(",");
				$("#roleIds").combotree('setValues', strs);
			},
			cascadeCheck : false
		//value : $.stringToList('${user.roleIds}')
		});

		$('#admin_user_grant').form({
			url : '${pageContext.request.contextPath}/userAction!grant.action',
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
					$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
					$.modalDialog.handler.dialog('close');
					$.modalDialog.openner_dataGrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
				} else {
					$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<form id="admin_user_grant" method="post">
			<table class="table table-hover table-condensed">
				<tr>
					<th>编号</th>
					<td><input name="ids" type="text" class="span2" value="${ids}" readonly="readonly"></td>
					<th>所属角色</th>
					<td><select id="roleIds" name="roleIds" style="width: 140px; height: 29px;"></select><img src="${pageContext.request.contextPath}/style/images/extjs_icons/cut_red.png" onclick="$('#roleIds').combotree('clear');" /></td>
				</tr>
			</table>
		</form>
	</div>
</div>