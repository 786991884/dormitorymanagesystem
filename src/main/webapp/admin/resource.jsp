<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title>资源管理</title>
</head>
<body>
	<c:if test="${fn:contains(sessionInfo.resourceList, 'resourceAction!editPage.action')}">
		<script type="text/javascript">
			$.canEdit = true;
		</script>
	</c:if>
	<c:if test="${fn:contains(sessionInfo.resourceList, 'resourceAction!delete.action')}">
		<script type="text/javascript">
			$.canDelete = true;
		</script>
	</c:if>
	<script type="text/javascript">
		var admin_resource_treeGrid;
		$(function() {
			admin_resource_treeGrid = $('#admin_zygl_treeGrid').treegrid({
				url : 'resourceAction!treeGrid.action',
				idField : 'id',
				treeField : 'name',
				parentField : 'pid',
				fit : true,
				fitColumns : false,
				border : false,
				frozenColumns : [ [ {
					title : '编号',
					field : 'id',
					width : 150,
					hidden : true
				} ] ],
				columns : [ [ {
					field : 'name',
					title : '资源名称',
					width : 200
				}, {
					field : 'url',
					title : '资源路径',
					width : 230
				}, {
					field : 'typeId',
					title : '资源类型ID',
					width : 150,
					hidden : true
				}, {
					field : 'typeName',
					title : '资源类型',
					width : 80
				}, {
					field : 'seq',
					title : '排序',
					width : 40
				}, {
					field : 'pid',
					title : '上级资源ID',
					width : 150,
					hidden : true
				}, {
					field : 'pname',
					title : '上级资源',
					width : 80
				}, {
					field : 'action',
					title : '操作',
					width : 50,
					formatter : function(value, row, index) {
						var str = '';
						if ($.canEdit) {
							str += $.formatString('<img onclick="admin_resource_editFun(\'{0}\');" src="{1}" title="编辑"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/pencil.png');
						}
						str += '&nbsp;';
						if ($.canDelete) {
							str += $.formatString('<img onclick="admin_resource_deleteFun(\'{0}\');" src="{1}" title="删除"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/cancel.png');
						}
						return str;
					}
				}, {
					field : 'remark',
					title : '备注',
					width : 150
				} ] ],
				toolbar : '#admin_zygl_toolbar',
				onContextMenu : function(e, row) {
					e.preventDefault();
					$(this).treegrid('unselectAll');
					$(this).treegrid('select', row.id);
					$('#admin_zygl_menu').menu('show', {
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

		function admin_resource_deleteFun(id) {
			if (id != undefined) {
				admin_resource_treeGrid.treegrid('select', id);
			}
			var node = admin_resource_treeGrid.treegrid('getSelected');
			if (node) {
				$.messager.confirm('询问', '您是否要删除当前资源？', function(b) {
					if (b) {
						$.messager.progress({
							title : '提示',
							text : '数据处理中，请稍后....'
						});
						$.post('${pageContext.request.contextPath}/resourceAction!delete.action', {
							id : node.id
						}, function(result) {
							if (result.success) {
								$.messager.alert('提示', result.msg, 'info');
								admin_resource_treeGrid.treegrid('reload');
								$("#layout_west_tree3").tree('reload');
							}
							$.messager.progress('close');
						}, 'JSON');
					}
				});
			}
		}

		function admin_resource_editFun(id) {
			if (id != undefined) {
				admin_resource_treeGrid.treegrid('select', id);
			}
			var node = admin_resource_treeGrid.treegrid('getSelected');
			if (node) {
				$.modalDialog({
					title : '编辑资源',
					width : 500,
					height : 300,
					href : '${pageContext.request.contextPath}/resourceAction!editPage.action?id=' + node.id,
					buttons : [ {
						text : '编辑',
						handler : function() {
							$.modalDialog.openner_treeGrid = admin_resource_treeGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
							var f = $.modalDialog.handler.find('#admin_zygl_editform');
							f.submit();
						}
					} ]
				});
			}
		}

		function admin_resource_addFun() {
			$.modalDialog({
				title : '添加资源',
				width : 500,
				height : 300,
				href : '${pageContext.request.contextPath}/resourceAction!addPage.action',
				buttons : [ {
					text : '添加',
					handler : function() {
						$.modalDialog.openner_treeGrid = admin_resource_treeGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
						var f = $.modalDialog.handler.find('#admin_zygl_addform');
						f.submit();
					}
				} ]
			});
		}

		function admin_resource_redo() {
			var node = admin_resource_treeGrid.treegrid('getSelected');
			if (node) {
				admin_resource_treeGrid.treegrid('expandAll', node.id);
			} else {
				admin_resource_treeGrid.treegrid('expandAll');
			}
		}

		function admin_resource_undo() {
			var node = admin_resource_treeGrid.treegrid('getSelected');
			if (node) {
				admin_resource_treeGrid.treegrid('collapseAll', node.id);
			} else {
				admin_resource_treeGrid.treegrid('collapseAll');
			}
		}
	</script>
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
			<table id="admin_zygl_treeGrid"></table>
		</div>
	</div>
	<div id="admin_zygl_toolbar" style="display: none;">
		<c:if test="${fn:contains(sessionInfo.resourceList, 'resourceAction!addPage')}">
			<a onclick="admin_resource_addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'pencil_add'">添加</a>
		</c:if>
		<a onclick="admin_resource_redo();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'resultset_next'">展开</a> <a onclick="admin_resource_undo();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'resultset_previous'">折叠</a> <a onclick="admin_resource_treeGrid.treegrid('reload');" href="javascript:void(0);" class="easyui-linkbutton"
			data-options="plain:true,iconCls:'transmit'">刷新</a>
	</div>

	<div id="admin_zygl_menu" class="easyui-menu" style="width: 120px; display: none;">
		<c:if test="${fn:contains(sessionInfo.resourceList, 'resourceAction!addPage')}">
			<div onclick="admin_resource_addFun();" data-options="iconCls:'pencil_add'">增加</div>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.resourceList, 'resourceAction!delete')}">
			<div onclick="admin_resource_deleteFun();" data-options="iconCls:'pencil_delete'">删除</div>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.resourceList, 'resourceAction!editPage')}">
			<div onclick="admin_resource_editFun();" data-options="iconCls:'pencil'">编辑</div>
		</c:if>
	</div>
</body>
</html>