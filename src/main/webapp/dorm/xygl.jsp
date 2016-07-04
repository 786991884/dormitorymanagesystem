<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>学院管理</title>
</head>
<body>
	<script type="text/javascript">
		var editing; //判断用户是否处于编辑状态 
		var flag; //判断新增和修改方法
		$(function() {
			$('#dorm_xygl_datagrid').datagrid({
				idField : 'id',
				fit : true,
				border : false,
				fitColumns : true,
				checkOnSelect : false,
				selectOnCheck : true,
				url : 'collegeAction!datagrid.action',
				striped : true,
				loadMsg : '数据正在加载,请耐心的等待...',
				sortName : 'id',
				sortOrder : 'asc',
				//rownumbers : true,
				frozenColumns : [ [ {
					field : 'id',
					checkbox : true,
					title : '编号',
					width : 100
				} ] ],
				columns : [ [ {
					field : 'name',
					title : '学院名称',
					width : 100,
					sortable : true,
					styler : function(value, record) {
					},
					editor : {
						type : 'validatebox',
						options : {
							required : true,
							missingMessage : '学院名必填!'
						}
					}
				}, {
					field : 'manname',
					title : '院长姓名',
					width : 100,
					sortable : false,
					styler : function(value, record) {
					},
					editor : {
						type : 'validatebox',
						options : {}
					}
				}, {
					field : 'telephone',
					title : '联系电话',
					width : 100,
					sortable : true,
					styler : function(value, record) {
					},
					editor : {
						type : 'validatebox',
						options : {
							validType : 'mobile'
						}
					}
				}, {
					field : 'memo',
					title : '备注',
					width : 150,
					sortable : false,
					formatter : function(value, record, index) {
						if (value) {
							return '<span title='+value+'>' + value + '</span>';
						}
					},
					styler : function(value, record) {
					},
					editor : {
						type : 'validatebox',
						options : {}
					}
				} ] ],
				pagination : true,
				pageSize : 10,
				pageList : [ 5, 10, 15, 20, 50 ],
				toolbar : '#dorm_xygl_toolbar',
				onAfterEdit : function(index, record, changes) {
					$.post(flag == 'add' ? 'collegeAction!add.action' : 'collegeAction!edit.action', record, function(r) {
						if (r && r.success) {
							$('#dorm_xygl_datagrid').datagrid('acceptChanges');
							$.messager.show({
								title : '提示信息',
								msg : '操作成功!'
							});
						} else {
							$('#dorm_xygl_datagrid').datagrid('rejectChanges');
							$.messager.show({
								title : '提示信息',
								msg : r.msg
							});
						}

					}, "json");
					$('#dorm_xygl_datagrid').datagrid('uncheckRow', editing);
					$('#dorm_xygl_datagrid').datagrid('unselectAll');
					$('#dorm_xygl_datagrid').datagrid('selectRow', editing);
					editing == undefined;
				}

			});

		});
		function dorm_xygl_addFun() {
			if (editing == undefined) {
				flag = 'add';
				//1 先取消所有的选中状态
				$('#dorm_xygl_datagrid').datagrid('clearChecked');
				$('#dorm_xygl_datagrid').datagrid('clearSelections');
				//2追加一行
				//$('#dorm_xygl_datagrid').datagrid('appendRow', {});
				$('#dorm_xygl_datagrid').datagrid('insertRow', {
					index : 0,
					row : {}
				});

				//3获取当前页的行号
				//editing = $('#dorm_xygl_datagrid').datagrid('getRows').length - 1;
				editing = 0;
				//4开启编辑状态
				$('#dorm_xygl_datagrid').datagrid('beginEdit', editing);
			}
		}
		function dorm_xygl_removeFun() {
			var arr = $('#dorm_xygl_datagrid').datagrid('getChecked');
			if (arr.length <= 0) {
				$.messager.show({
					title : '提示信息',
					msg : '请选择进行删除操作!'
				});
			} else {
				$.messager.confirm('提示信息', '确认删除?', function(r) {
					if (r) {
						var ids = '';
						for (var i = 0; i < arr.length; i++) {
							ids += arr[i].id + ',';
						}
						ids = ids.substring(0, ids.length - 1);
						$.post('collegeAction!remove.action', {
							ids : ids
						}, function(result) {
							$('#dorm_xygl_datagrid').datagrid('load');
							$.messager.show({
								title : '提示信息',
								msg : '操作成功!'
							});
							$('#dorm_xygl_datagrid').datagrid('uncheckAll');
						});

					} else {
						return;
					}
				});
			}
		}
		function dorm_xygl_editFun() {
			var arr = $('#dorm_xygl_datagrid').datagrid('getChecked');
			if (arr.length != 1) {
				$.messager.show({
					title : '提示信息',
					msg : '只能选择一条记录进行修改!'
				});
			} else {
				if (editing == undefined) {
					flag = 'edit';
					//根据行记录对象获取该行的索引位置
					editing = $('#dorm_xygl_datagrid').datagrid('getRowIndex', arr[0]);
					//开启编辑状态
					$('#dorm_xygl_datagrid').datagrid('beginEdit', editing);
				}
			}
		}
		function dorm_xygl_searchFun() {
			if ($('#dorm_xygl_layout').layout('panel', 'north').panel('options').collapsed) {
				$('#dorm_xygl_layout').layout('expand', 'north');
			} else {
				$('#dorm_xygl_layout').layout('collapse', 'north');
			}
		}
		function dorm_xygl_saveFun() {
			//保存之前进行数据的校验 , 然后结束编辑并设置编辑状态字段 
			if ($('#dorm_xygl_datagrid').datagrid('validateRow', editing)) {
				$('#dorm_xygl_datagrid').datagrid('endEdit', editing);
				editing = undefined;
			} else {
				$.messager.show({
					title : '提示信息',
					msg : '数据验证不通过，请检查！'
				});
			}
		}
		function dorm_xygl_cancelFun() {
			$('#dorm_xygl_datagrid').datagrid('uncheckAll');
			//回滚数据 
			$('#dorm_xygl_datagrid').datagrid('rejectChanges');
			editing = undefined;
		}
		function dorm_xygl_exportExcelFun() {
			$("#dorm_xygl_searchForm").attr("action", "collegeAction!exportExcel.action").submit();
			$.messager.show({
				title : '提示信息',
				msg : '正在导出，请稍等!'
			});
		}
		$('#dorm_xygl_searchbth').click(function() {
			$('#dorm_xygl_datagrid').datagrid('load', serializeObject($('#dorm_xygl_searchForm')));
		});
		$('#dorm_xygl_clearbth').click(function() {
			$('#dorm_xygl_searchForm').form('clear');
			$('#dorm_xygl_datagrid').datagrid('load', {});
		});
	</script>
	<div id="dorm_xygl_layout" class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'north',title:'查询条件',border:false,collapsed:true" style="height: 100px;">
			<form id="dorm_xygl_searchForm" method="post">
				模糊查询:&nbsp;&nbsp;&nbsp;&nbsp;学院名称:<input type="text" name="name" class="easyui-validatebox" value="">&nbsp;&nbsp;&nbsp;&nbsp;<a id="dorm_xygl_searchbth" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a> <a id="dorm_xygl_clearbth" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">清空</a>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<table id="dorm_xygl_datagrid"></table>
		</div>
	</div>
	<div id="dorm_xygl_toolbar" class="" style="display: none;">
		<table cellspacing="0" cellpadding="0">
			<tr>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'collegeAction!add.action')}">
					<td><a onclick="dorm_xygl_addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-user_add'">添加</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'collegeAction!remove.action')}">
					<td><a onclick="dorm_xygl_removeFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-user_delete'">删除</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'collegeAction!edit.action')}">
					<td><a onclick="dorm_xygl_editFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-user_edit'">修改</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<td><a onclick="dorm_xygl_searchFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-user_go'">查询</a></td>
				<td>
					<div class="datagrid-btn-separator"></div>
				</td>
				<td><a onclick="dorm_xygl_saveFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'">保存操作</a></td>
				<td>
					<div class="datagrid-btn-separator"></div>
				</td>
				<td><a onclick="dorm_xygl_cancelFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a></td>
				<td>
					<div class="datagrid-btn-separator"></div>
				</td>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'collegeAction!exportExcel.action')}">
					<td><a onclick="dorm_xygl_exportExcelFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-exportexcel'">导出excel</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
			</tr>
		</table>
	</div>
</body>
</html>