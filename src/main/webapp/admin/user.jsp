<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title>用户管理</title>
</head>
<body>
	<c:if test="${fn:contains(sessionInfo.resourceList, 'userAction!editPage.action')}">
		<script type="text/javascript">
			$.canEdit = true;
		</script>
	</c:if>
	<c:if test="${fn:contains(sessionInfo.resourceList, 'userAction!remove.action')}">
		<script type="text/javascript">
			$.canDelete = true;
		</script>
	</c:if>
	<c:if test="${fn:contains(sessionInfo.resourceList, 'userAction!grantPage.action')}">
		<script type="text/javascript">
			$.canGrant = true;
		</script>
	</c:if>
	<c:if test="${fn:contains(sessionInfo.resourceList, 'userAction!editPwdPage.action')}">
		<script type="text/javascript">
			$.canEditPwd = true;
		</script>
	</c:if>
	<script type="text/javascript">
		var admin_user_dataGrid;
		$(function() {
			admin_user_dataGrid = $('#admin_user_dataGrid').datagrid({
				url : '${pageContext.request.contextPath}/userAction!dataGrid.action',
				fit : true,
				fitColumns : true,
				border : false,
				pagination : true,
				idField : 'id',
				pageSize : 10,
				pageList : [ 10, 20, 30, 40, 50 ],
				sortName : 'id',
				sortOrder : 'desc',
				checkOnSelect : false,
				selectOnCheck : false,
				nowrap : false,
				frozenColumns : [ [ {
					field : 'id',
					title : '编号',
					width : 150,
					checkbox : true
				}, {
					field : 'name',
					title : '登录名称',
					width : 80,
					sortable : true
				} ] ],
				columns : [ [ {
					field : 'pwd',
					title : '密码',
					width : 60,
					formatter : function(value, row, index) {
						return '******';
					}
				}, {
					field : 'createdatetime',
					title : '创建时间',
					width : 150,
					sortable : true
				}, {
					field : 'modifydatetime',
					title : '最后修改时间',
					width : 150,
					sortable : true
				}, {
					field : 'roleIds',
					title : '所属角色ID',
					width : 150,
					hidden : true
				}, {
					field : 'roleNames',
					title : '所属角色名称',
					width : 150
				}, {
					field : 'action',
					title : '操作',
					width : 100,
					formatter : function(value, row, index) {
						var str = '';
						if ($.canEdit) {
							str += $.formatString('<img onclick="admin_user_editFun(\'{0}\');" src="{1}" title="编辑"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/pencil.png');
						}
						str += '&nbsp;';
						if ($.canGrant) {
							str += $.formatString('<img onclick="admin_user_grantFun(\'{0}\');" src="{1}" title="授权"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/key.png');
						}
						str += '&nbsp;';
						if ($.canDelete) {
							str += $.formatString('<img onclick="admin_user_deleteFun(\'{0}\');" src="{1}" title="删除"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/cancel.png');
						}
						str += '&nbsp;';
						if ($.canEditPwd) {
							str += $.formatString('<img onclick="admin_user_editPwdFun(\'{0}\');" src="{1}" title="修改密码"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/lock/lock_edit.png');
						}
						return str;
					}
				} ] ],
				toolbar : '#admin_user_toolbar',
				onLoadSuccess : function() {
					$('#admin_user_searchForm table').show();
					$.messager.progress('close');

					$(this).datagrid('tooltip');
				},
				onRowContextMenu : function(e, rowIndex, rowData) {
					e.preventDefault();
					$(this).datagrid('unselectAll').datagrid('uncheckAll');
					$(this).datagrid('selectRow', rowIndex);
					$('#admin_user_menu').menu('show', {
						left : e.pageX,
						top : e.pageY
					});
				}
			});
		});

		function admin_user_editPwdFun(id) {
			admin_user_dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
			$.modalDialog({
				title : '编辑用户密码',
				width : 500,
				height : 300,
				href : '${pageContext.request.contextPath}/userAction!editPwdPage.action?id=' + id,
				buttons : [ {
					text : '编辑',
					handler : function() {
						$.modalDialog.openner_dataGrid = admin_user_dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
						var f = $.modalDialog.handler.find('#admin_user_editpwd_form');
						console.log(f);
						f.submit();
					}
				} ]
			});
		}

		function admin_user_deleteFun(id) {
			if (id == undefined) {//点击右键菜单才会触发这个
				var rows = admin_user_dataGrid.datagrid('getSelections');
				id = rows[0].id;
			} else {//点击操作里面的删除图标会触发这个
				admin_user_dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
			}
			$.messager.confirm('询问', '您是否要删除当前用户？', function(b) {
				if (b) {
					var currentUserId = '${sessionInfo.id}';/*当前登录用户的ID*/
					if (currentUserId != id) {
						$.messager.progress({
							title : '提示',
							text : '数据处理中，请稍后....'
						});
						$.post('${pageContext.request.contextPath}/userAction!remove.action', {
							id : id
						}, function(result) {
							if (result.success) {
								$.messager.alert('提示', result.msg, 'info');
								admin_user_dataGrid.datagrid('reload');
							}
							$.messager.progress('close');
						}, 'JSON');
					} else {
						$.messager.show({
							title : '提示',
							msg : '不可以删除自己！'
						});
					}
				}
			});
		}

		function admin_user_batchDeleteFun() {
			var rows = admin_user_dataGrid.datagrid('getChecked');
			var ids = [];
			if (rows.length > 0) {
				$.messager.confirm('确认', '您是否要删除当前选中的项目？', function(r) {
					if (r) {
						$.messager.progress({
							title : '提示',
							text : '数据处理中，请稍后....'
						});
						var currentUserId = '${sessionInfo.id}';/*当前登录用户的ID*/
						var flag = false;
						for (var i = 0; i < rows.length; i++) {
							if (currentUserId != rows[i].id) {
								ids.push(rows[i].id);
							} else {
								flag = true;
							}
						}
						$.getJSON('${pageContext.request.contextPath}/userAction!remove.action', {
							ids : ids.join(',')
						}, function(result) {
							if (result.success) {
								admin_user_dataGrid.datagrid('load');
								admin_user_dataGrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
							}
							if (flag) {
								$.messager.show({
									title : '提示',
									msg : '不可以删除自己！'
								});
							} else {
								$.messager.alert('提示', result.msg, 'info');
							}
							$.messager.progress('close');
						});
					}
				});
			} else {
				$.messager.show({
					title : '提示',
					msg : '请勾选要删除的记录！'
				});
			}
		}

		function admin_user_editFun(id) {
			if (id == undefined) {
				var rows = admin_user_dataGrid.datagrid('getSelections');
				id = rows[0].id;
			} else {
				admin_user_dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
			}
			$.modalDialog({
				title : '编辑用户',
				width : 500,
				height : 300,
				href : '${pageContext.request.contextPath}/userAction!editPage.action?id=' + id,
				buttons : [ {
					text : '编辑',
					handler : function() {
						$.modalDialog.openner_dataGrid = admin_user_dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
						var f = $.modalDialog.handler.find('#admin_user_editform');
						f.submit();
					}
				} ]
			});
		}

		function admin_user_addFun() {
			$.modalDialog({
				title : '添加用户',
				width : 500,
				height : 300,
				href : '${pageContext.request.contextPath}/userAction!addPage.action',
				buttons : [ {
					text : '添加',
					handler : function() {
						$.modalDialog.openner_dataGrid = admin_user_dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
						var f = $.modalDialog.handler.find('#admin_user_addform');
						f.submit();
					}
				} ]
			});
		}

		function admin_user_batchGrantFun() {
			var rows = admin_user_dataGrid.datagrid('getChecked');
			var ids = [];
			if (rows.length > 0) {
				for (var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
				$.modalDialog({
					title : '用户授权',
					width : 500,
					height : 300,
					href : '${pageContext.request.contextPath}/userAction!grantPage.action?ids=' + ids.join(','),
					buttons : [ {
						text : '授权',
						handler : function() {
							$.modalDialog.openner_dataGrid = admin_user_dataGrid;//因为授权成功之后，需要刷新这个dataGrid，所以先预定义好
							var f = $.modalDialog.handler.find('#admin_user_grant');
							f.submit();
						}
					} ]
				});
			} else {
				$.messager.show({
					title : '提示',
					msg : '请勾选要授权的记录！'
				});
			}
		}

		function admin_user_grantFun(id) {
			admin_user_dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
			$.modalDialog({
				title : '用户授权',
				width : 500,
				height : 300,
				href : '${pageContext.request.contextPath}/userAction!grantPage.action?ids=' + id,
				buttons : [ {
					text : '授权',
					handler : function() {
						$.modalDialog.openner_dataGrid = admin_user_dataGrid;//因为授权成功之后，需要刷新这个dataGrid，所以先预定义好
						var f = $.modalDialog.handler.find('#admin_user_grant');
						f.submit();
					}
				} ]
			});
		}

		function admin_user_searchFun() {
			admin_user_dataGrid.datagrid('load', $.serializeObject($('#admin_user_searchForm')));
		}
		function admin_user_cleanFun() {
			$('#admin_user_searchForm input').val('');
			admin_user_dataGrid.datagrid('load', {});
		}
	</script>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<div data-options="region:'north',title:'查询条件',border:false" style="height: 130px; overflow: hidden;">
			<form id="admin_user_searchForm">
				<table class="table table-hover table-condensed" style="display: none;">
					<tr>
						<th>登录名</th>
						<td><input name="name" placeholder="可以模糊查询登录名" class="span2" /></td>
					</tr>
					<tr>
						<th>创建时间</th>
						<td><input name="createdatetime" class="easyui-datetimebox" style="width: 160px;" editable=false value=""></td>
					</tr>
					<tr>
						<th>最后修改时间</th>
						<td><input name="modifydatetime" class="easyui-datetimebox" style="width: 160px;" editable=false value=""></td>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<table id="admin_user_dataGrid"></table>
		</div>
	</div>
	<div id="admin_user_toolbar" style="display: none;">
		<c:if test="${fn:contains(sessionInfo.resourceList, 'userAction!addPage.action')}">
			<a onclick="admin_user_addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'pencil_add'">添加</a>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.resourceList, 'userAction!grantPage.action')}">
			<a onclick="admin_user_batchGrantFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'tux'">批量授权</a>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.resourceList, 'userAction!remove.action')}">
			<a onclick="admin_user_batchDeleteFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'delete'">批量删除</a>
		</c:if>
		<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'brick_add',plain:true" onclick="admin_user_searchFun();">过滤条件</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'brick_delete',plain:true" onclick="admin_user_cleanFun();">清空条件</a>
	</div>

	<div id="admin_user_menu" class="easyui-menu" style="width: 120px; display: none;">
		<c:if test="${fn:contains(sessionInfo.resourceList, 'userAction!addPage.action')}">
			<div onclick="admin_user_addFun();" data-options="iconCls:'pencil_add'">增加</div>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.resourceList, 'userAction!remove.action')}">
			<div onclick="admin_user_deleteFun();" data-options="iconCls:'pencil_delete'">删除</div>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.resourceList, 'userAction!editPage.action')}">
			<div onclick="admin_user_editFun();" data-options="iconCls:'pencil'">编辑</div>
		</c:if>
	</div>
</body>
</html>