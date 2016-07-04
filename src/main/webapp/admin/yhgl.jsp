<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
</head>
<body>
	<script type="text/javascript">
		$(function() {
			$('#admin_yhgl_datagrid').datagrid({
				url : 'userAction!dataGrid.action',
				fit : true,
				striped : true,
				fitColumns : true,
				border : false,
				pagination : true,
				idField : 'id',
				pagePosition : 'both',
				pageSize : 5,
				pageList : [ 5, 8, 15, 20, 50 ],
				sortName : 'id',
				sortOrder : 'asc',
				//rownumbers : true,
				checkOnSelect : false,
				selectOnCheck : true,
				frozenColumns : [ [ {
					field : 'id',
					title : '编号',
					width : 50,
					//hidden : true,
					checkbox : true
				}, {
					field : 'name',
					title : '登录名称',
					width : 100,
					align : 'center',
					sortable : true
				} ] ],
				columns : [ [ {
					field : 'pwd',
					title : '密码',
					width : 50,
					formatter : function(value, row, index) {
						//return '<span title='+value+', width=80px;>' + value + '</span>';
						return '******';
					}
				}, {
					field : 'createdatetime',
					title : '创建时间',
					width : 100,
					sortable : true
				}, {
					field : 'modifydatetime',
					title : '修改时间',
					width : 100,
					sortable : true
				} ] ],
				toolbar : '#dorm_yhgl_toolbar'
			});
		});

		function admin_yhgl_addFun() {
			$('#admin_yhgl_addForm input').val('');
			//$('#admin_yhgl_addDialog').dialog('open');
			$('#admin_yhgl_addDialog').dialog('open').dialog('setTitle', '增加用户');
		}
		function admin_yhgl_removeFun() {
			var rows = $('#admin_yhgl_datagrid').datagrid('getChecked');
			var ids = [];

			if (rows.length > 0) {
				$.messager.confirm('确认删除', '您是否要删除当前选中的行?', function(r) {
					if (r) {
						for (var i = 0; i < rows.length; i++) {
							ids.push(rows[i].id);
						}
						$.ajax({
							type : 'post',
							url : 'userAction!remove.action',
							data : {
								ids : ids.join(',')
							},
							dataType : 'json',
							success : function(r) {
								$('#admin_yhgl_datagrid').datagrid('load');
								$('#admin_yhgl_datagrid').datagrid('uncheckAll');
								$.messager.show({
									title : '提示信息',
									msg : r.msg
								});
							}
						});
					} else {
						return;
					}
				});
			} else {
				$.messager.show({
					title : '提示信息',
					msg : '请至少选中一行删除！！！'
				});

			}
		}
		function admin_yhgl_editFun() {
			var rows = $('#admin_yhgl_datagrid').datagrid('getChecked');
			if (rows.length == 1) {
				var d = $('<div/>').dialog({
					width : 400,
					height : 300,
					href : 'admin/yhglEdit.jsp',
					modal : true,
					title : '编辑用户',
					buttons : [ {
						text : '编辑',
						handler : function() {
							$('#admin_yhglEdit_form').form('submit', {
								url : 'userAction!edit.action',
								success : function(r) {
									var obj = $.parseJSON(r);
									if (obj.success) {
										d.dialog('close');
										$('#admin_yhgl_datagrid').datagrid('reload');
										$('#admin_yhgl_datagrid').datagrid('updateRow', {
											index : $('#admin_yhgl_datagrid').datagrid('getRowIndex', rows[0]),
											row : obj.obj
										});
										var index = $('#admin_yhgl_datagrid').datagrid('getRowIndex', rows[0]);
										$('#admin_yhgl_datagrid').datagrid('uncheckAll');
										$('#admin_yhgl_datagrid').datagrid('selectRow', index);
									}
									$.messager.show({
										title : '提示信息',
										msg : obj.msg
									});
								}
							});
						}
					} ],
					onClose : function() {
						$(this).dialog('destroy');
					},
					onLoad : function() {
						//console.info(rows[0]);
						$('#admin_yhglEdit_form').form('load', rows[0]);
					}
				});
			} else {
				$.messager.show({
					title : '提示信息',
					msg : '请选择一条编辑'
				});
			}
		}
		function admin_yhgl_searchFun() {
			//console.info($('#admin_yhgl_layout').layout('panel', 'north').panel('options').collapsed);
			if ($('#admin_yhgl_layout').layout('panel', 'north').panel('options').collapsed) {
				$('#admin_yhgl_layout').layout('expand', 'north');
			} else {
				$('#admin_yhgl_layout').layout('collapse', 'north');
			}
		}
		function admin_yhgl_saveFun() {
			//保存之前进行数据的校验 , 然后结束编辑并设置编辑状态字段 
			if ($('#dorm_yhgl_datagrid').datagrid('validateRow', editing)) {
				$('#dorm_yhgl_datagrid').datagrid('endEdit', editing);
				editing = undefined;
			} else {
				$.messager.show({
					title : '提示信息',
					msg : '数据验证不通过，请检查！'
				});
			}
		}
		function admin_yhgl_cancelFun() {
			$('#dorm_yhgl_datagrid').datagrid('uncheckAll');
			//回滚数据 
			$('#dorm_yhgl_datagrid').datagrid('rejectChanges');
			editing = undefined;
		}
		function admin_yhgl_exportExcelFun() {
			var params = $('#admin_yhgl_searchForm').serialize();
			params = decodeURIComponent(params, true);
			//再进行编码
			params = encodeURI(encodeURI(params));
			//var url = "userAction!exportExcel.action";
			var url = "userAction!exportExcel.action?" + params;
			window.location.href = url;
		}
		$('#admin_yhgl_searchbtn').click(function() {
			$('#admin_yhgl_datagrid').datagrid('load', serializeObject($('#admin_yhgl_searchForm')));
		});
		$('#admin_yhgl_clearbtn').click(function() {
			$('#admin_yhgl_searchForm').form('clear');
			$('#admin_yhgl_datagrid').datagrid('load', {});
		});
	</script>
	<div id="admin_yhgl_layout" class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'north',title:'查询条件',border:false,collapsed:true,collapsible:false" style="height: 100px;">
			<form id="admin_yhgl_searchForm" method="post">
				模糊查询: 用户名：<input name="name" value="" class="easyui-validatebox">&nbsp;&nbsp;&nbsp;&nbsp; 创建时间：<input name="createdatetime" class="easyui-datetimebox" style="width: 160px;" editable=false value="">&nbsp;&nbsp;&nbsp;&nbsp; 最后修改时间：<input name="modifydatetime" class="easyui-datetimebox" style="width: 160px;" editable=false value="">&nbsp;&nbsp;&nbsp;&nbsp; <a
					id="admin_yhgl_searchbtn" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a> <a id="admin_yhgl_clearbtn" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">清空</a>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<table id="admin_yhgl_datagrid"></table>
		</div>
	</div>
	<div id="admin_yhgl_addDialog" class="easyui-dialog"
		data-options="
closed : true,
			modal : true,
			title : '添加用户',
			buttons : [ {
				text : '添加',
				iconCls : 'icon-add',
				handler : function() {
					$('#admin_yhgl_addForm').form('submit', {
						url : 'userAction!add.action',
						success : function(r) {
							var obj = $.parseJSON(r);
							if (obj.success) {
								/*$('#admin_yhgl_datagrid').datagrid('load');*/
								/* $('#admin_yhgl_datagrid').datagrid('appendRow',obj.obj);*/
								$('#admin_yhgl_datagrid').datagrid('insertRow', {
									index : 0,
									row : obj.obj
								});
								$('#admin_yhgl_addDialog').dialog('close');
								$('#admin_yhgl_datagrid').datagrid('clearSelections');
								$('#admin_yhgl_datagrid').datagrid('clearChecked');
								$('#admin_yhgl_datagrid').datagrid('selectRow',0);
							}
							$.messager.show({
								title : '提示',
								msg : obj.msg
							});
						}
					});
				}
			},{
				text : '取消',
				iconCls : 'icon-cancel',
				handler : function() {
					$('#admin_yhgl_addDialog').dialog('close');
				}
			}]
	   "
		style="width: 400px; height: 300px;" align="center">
		<div class="ftitle" style="font-size: 14px; font-weight: bold; color: #666; padding: 5px 0; margin-bottom: 10px; border-bottom: 1px solid #ccc;">用户信息</div>
		<form id="admin_yhgl_addForm" style="margin: 0; padding: 10px 30px;" method="post">
			<div class="fitem" style="margin-bottom: 5px;">
				<label style="display: inline-block; width: 100px;">登录名称:</label> <input name="name" type="text" class="easyui-validatebox" data-options="required:true">
			</div>
			<div class="fitem" style="margin-bottom: 5px;">
				<label style="display: inline-block; width: 100px;">密码:</label> <input name="pwd" type="password" class="easyui-validatebox" data-options="required:true,validType:'midLength[5,16]'">
			</div>
			<div class="fitem" style="margin-bottom: 5px;">
				<label style="display: inline-block; width: 100px;">创建时间:</label> <input name="createdatetime" type="text" class="easyui-datetimebox" data-options="required:true,editable:false">
			</div>
			<div class="fitem" style="margin-bottom: 5px;">
				<label style="display: inline-block; width: 100px;">最后修改时间:</label> <input name="modifydatetime" type="text" class="easyui-datetimebox" data-options="required:true,editable:false">
			</div>
		</form>
	</div>
	<div id="dorm_yhgl_toolbar" class="" style="display: none;">
		<table cellspacing="0" cellpadding="0">
			<tr>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'userAction!add.action')}">
					<td><a onclick="admin_yhgl_addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-table_add'">添加</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'userAction!remove.action')}">
					<td><a onclick="admin_yhgl_removeFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-table_delete'">删除</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'userAction!edit.action')}">
					<td><a onclick="admin_yhgl_editFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-table_edit'">修改</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<td><a onclick="admin_yhgl_searchFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-table_go'">查询</a></td>
				<td>
					<div class="datagrid-btn-separator"></div>
				</td>
				<td><a onclick="admin_yhgl_saveFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-table_save'">保存操作</a></td>
				<td>
					<div class="datagrid-btn-separator"></div>
				</td>
				<td><a onclick="admin_yhgl_cancelFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a></td>
				<td>
					<div class="datagrid-btn-separator"></div>
				</td>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'userAction!exportExcel.action')}">
					<td><a onclick="admin_yhgl_exportExcelFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-exportexcel'">导出excel</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
			</tr>
		</table>
	</div>
</body>
</html>