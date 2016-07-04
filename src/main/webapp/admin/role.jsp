<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title>角色管理</title>
</head>
<body>
	<c:if test="${fn:contains(sessionInfo.resourceList, 'roleAction!editPage.action')}">
		<script type="text/javascript">
			$.canEdit = true;
		</script>
	</c:if>
	<c:if test="${fn:contains(sessionInfo.resourceList, 'roleAction!delete.action')}">
		<script type="text/javascript">
			$.canDelete = true;
		</script>
	</c:if>
	<c:if test="${fn:contains(sessionInfo.resourceList, 'roleAction!grantPage.action')}">
		<script type="text/javascript">
			$.canGrant = true;
		</script>
	</c:if>
	<script type="text/javascript">
		var admin_role_treeGrid;
		$(function() {
			admin_role_treeGrid = $('#admin_jsgl_treeGrid').treegrid({
				url : '${pageContext.request.contextPath}/roleAction!treeGrid.action',
				idField : 'id',
				treeField : 'name',
				parentField : 'pid',
				fit : true,
				fitColumns : false,
				border : false,
				nowrap : true,
				frozenColumns : [ [ {
					title : '编号',
					field : 'id',
					width : 150,
					hidden : true
				}, {
					field : 'name',
					title : '角色名称',
					width : 150
				} ] ],
				columns : [ [ {
					field : 'seq',
					title : '排序',
					width : 40
				}, {
					field : 'pid',
					title : '上级角色ID',
					width : 150,
					hidden : true
				}, {
					field : 'pname',
					title : '上级角色',
					width : 80
				}, {
					field : 'resourceIds',
					title : '拥有资源',
					width : 250,
					formatter : function(value, row) {
						if (value) {
							return row.resourceNames;
						}
						return '';
					}
				}, {
					field : 'resourceNames',
					title : '拥有资源名称',
					width : 80,
					hidden : true
				}, {
					field : 'remark',
					title : '备注',
					width : 150
				}, {
					field : 'action',
					title : '操作',
					width : 70,
					formatter : function(value, row, index) {
						var str = '';
						if ($.canEdit) {
							str += $.formatString('<img onclick="admin_role_editFun(\'{0}\');" src="{1}" title="编辑"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/pencil.png');
						}
						str += '&nbsp;';
						if ($.canGrant) {
							str += $.formatString('<img onclick="admin_role_grantFun(\'{0}\');" src="{1}" title="授权"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/key.png');
						}
						str += '&nbsp;';
						if ($.canDelete) {
							str += $.formatString('<img onclick="admin_role_deleteFun(\'{0}\');" src="{1}" title="删除"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/cancel.png');
						}
						return str;
					}
				} ] ],
				toolbar : '#admin_jsgl_toolbar',
				onContextMenu : function(e, row) {
					e.preventDefault();
					$(this).treegrid('unselectAll');
					$(this).treegrid('select', row.id);
					$('#admin_jsgl_menu').menu('show', {
						left : e.pageX,
						top : e.pageY
					});
				},
				onLoadSuccess : function() {
					$.messager.progress('close');

					$(this).treegrid('tooltip');
				}
			});
		});

		function admin_role_deleteFun(id) {
			if (id != undefined) {
				admin_role_treeGrid.treegrid('select', id);
			}
			var node = admin_role_treeGrid.treegrid('getSelected');
			if (node) {
				$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
					if (b) {
						$.messager.progress({
							title : '提示',
							text : '数据处理中，请稍后....'
						});
						$.post('${pageContext.request.contextPath}/roleAction!delete.action', {
							id : node.id
						}, function(result) {
							if (result.success) {
								$.messager.alert('提示', result.msg, 'info');
								admin_role_treeGrid.treegrid('reload');
							}
							$.messager.progress('close');
						}, 'JSON');
					}
				});
			}
		}

		function admin_role_editFun(id) {
			if (id != undefined) {
				admin_role_treeGrid.treegrid('select', id);
			}
			var node = admin_role_treeGrid.treegrid('getSelected');
			if (node) {
				$.modalDialog({
					title : '编辑角色',
					width : 500,
					height : 300,
					href : '${pageContext.request.contextPath}/roleAction!editPage.action?id=' + node.id,
					buttons : [ {
						text : '编辑',
						handler : function() {
							$.modalDialog.openner_treeGrid = admin_role_treeGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
							var f = $.modalDialog.handler.find('#admin_jsgl_editform');
							f.submit();
						}
					} ]
				});
			}
		}

		function admin_role_addFun() {
			$.modalDialog({
				title : '添加角色',
				width : 500,
				height : 300,
				href : '${pageContext.request.contextPath}/roleAction!addPage.action',
				buttons : [ {
					text : '添加',
					handler : function() {
						$.modalDialog.openner_treeGrid = admin_role_treeGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
						var f = $.modalDialog.handler.find('#admin_jsgl_addform');
						f.submit();
					}
				} ]
			});
		}

		function admin_role_redo() {
			var node = admin_role_treeGrid.treegrid('getSelected');
			if (node) {
				admin_role_treeGrid.treegrid('expandAll', node.id);
			} else {
				admin_role_treeGrid.treegrid('expandAll');
			}
		}

		function admin_role_undo() {
			var node = admin_role_treeGrid.treegrid('getSelected');
			if (node) {
				admin_role_treeGrid.treegrid('collapseAll', node.id);
			} else {
				admin_role_treeGrid.treegrid('collapseAll');
			}
		}

		function admin_role_grantFun(id) {
			if (id != undefined) {
				admin_role_treeGrid.treegrid('select', id);
			}
			var node = admin_role_treeGrid.treegrid('getSelected');
			if (node) {
				$.modalDialog({
					title : '角色授权',
					width : 500,
					height : 500,
					href : '${pageContext.request.contextPath}/roleAction!grantPage.action?id=' + node.id,
					buttons : [ {
						text : '授权',
						handler : function() {
							$.modalDialog.openner_treeGrid = admin_role_treeGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
							var f = $.modalDialog.handler.find('#admin_roleGrant_form');
							f.submit();
						}
					} ]
				});
			}
		}
	</script>
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
			<table id="admin_jsgl_treeGrid"></table>
		</div>
	</div>
	<div id="admin_jsgl_toolbar" style="display: none;">
		<c:if test="${fn:contains(sessionInfo.resourceList, 'roleAction!addPage.action')}">
			<a onclick="admin_role_addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'pencil_add'">添加</a>
		</c:if>
		<a onclick="admin_role_redo();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'resultset_next'">展开</a> <a onclick="admin_role_undo();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'resultset_previous'">折叠</a> <a onclick="admin_role_treeGrid.treegrid('reload');" href="javascript:void(0);" class="easyui-linkbutton"
			data-options="plain:true,iconCls:'transmit'">刷新</a>
	</div>

	<div id="admin_jsgl_menu" class="easyui-menu" style="width: 120px; display: none;">
		<c:if test="${fn:contains(sessionInfo.resourceList, 'roleAction!addPage.action')}">
			<div onclick="admin_role_addFun();" data-options="iconCls:'pencil_add'">增加</div>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.resourceList, 'roleAction!delete.action')}">
			<div onclick="admin_role_deleteFun();" data-options="iconCls:'pencil_delete'">删除</div>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.resourceList, 'roleAction!editPage.action')}">
			<div onclick="admin_role_editFun();" data-options="iconCls:'pencil'">编辑</div>
		</c:if>
	</div>
</body>
</html>