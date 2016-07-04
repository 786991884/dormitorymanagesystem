<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>职工管理</title>
</head>
<body>
	<script type="text/javascript">
		var editing; //判断是否处于编辑状态 
		var flag; //判断新增和修改方法
		$(function() {
			$('#dorm_zggl_datagrid').datagrid({
				idField : 'id',
				fit : true,
				border : false,
				fitColumns : true,
				checkOnSelect : false,
				selectOnCheck : true,
				url : 'staffAction!datagrid.action',
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
					title : '姓名',
					width : 100,
					sortable : true,
					styler : function(value, record) {
					},
					editor : {
						type : 'validatebox',
						options : {
							required : true,
							missingMessage : '姓名必填!'
						}
					}
				}, {
					field : 'sex',
					title : '性别',
					width : 50,
					sortable : true,
					styler : function(value, record, index) {
						if (value == '男') {
							return 'color:red;';
						} else if (value == '女') {
							return 'color:green;';
						}
					},
					editor : {
						type : 'combobox',
						options : {
							data : [ {
								id : '男',
								val : '男'
							}, {
								id : '女',
								val : '女'
							} ],
							valueField : 'id',
							textField : 'val',
							required : true,
							missingMessage : '性别必填!'
						}
					}
				}, {
					field : 'telephone',
					title : '电话',
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
					field : 'buildingname',
					title : '所管楼宇',
					width : 100,
					sortable : false,
					styler : function(value, record) {
					},
					editor : {
						type : 'combobox',
						options : {
							mode : 'remote',
							url : 'staffAction!getBuildings.action',
							valueField : 'id',
							textField : 'number',
							delay : 1000,
							onBeforeLoad : function(param) {
								if (param == null || param.q == null || param.q.replace(/ /g, '') == '') {
									var value = $(this).combobox('getValue');
									if (value) {// 修改的时候才会出现q为空而value不为空
										param.id = value;
										return true;
									}
									return false;
								}
							}
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
					editor : {
						type : 'validatebox',
						options : {}
					}
				} ] ],
				pagination : true,
				pageSize : 10,
				pageList : [ 5, 10, 15, 20, 50 ],
				toolbar : '#dorm_zggl_toolbar',
				onAfterEdit : function(index, record, changes) {
					if (!record.buildingname || $.trim(record.buildingname) === "") {
						record.buildingname = 0;
					}
					if (!$.isNumeric(record.buildingname)) {
						record.buildingname = null;
					}
					$.post(flag == 'add' ? 'staffAction!add.action' : 'staffAction!edit.action', record, function(r) {
						if (r && r.success) {
							changes.buildingname = r.obj.buildingname;
							$('#dorm_zggl_datagrid').datagrid('updateRow', {
								index : index,
								row : changes
							});
							//$('#dorm_zggl_datagrid').datagrid('acceptChanges');
							$.messager.show({
								title : '提示信息',
								msg : '操作成功!'
							});
						} else {
							$('#dorm_zggl_datagrid').datagrid('rejectChanges');
							$.messager.show({
								title : '提示信息',
								msg : r.msg
							});
						}

					}, "json");
					$('#dorm_zggl_datagrid').datagrid('uncheckRow', editing);
					$('#dorm_zggl_datagrid').datagrid('unselectAll');
					$('#dorm_zggl_datagrid').datagrid('selectRow', editing);
					editing == undefined;
				}

			});

		});
		function dorm_zggl_addFun() {
			if (editing == undefined) {
				flag = 'add';
				//1 先取消所有的选中状态
				$('#dorm_zggl_datagrid').datagrid('clearChecked');
				$('#dorm_zggl_datagrid').datagrid('clearSelections');
				//2追加一行
				//$('#dorm_xygl_datagrid').datagrid('appendRow', {});
				$('#dorm_zggl_datagrid').datagrid('insertRow', {
					index : 0,
					row : {}
				});

				//3获取当前页的行号
				//editing = $('#dorm_xygl_datagrid').datagrid('getRows').length - 1;
				editing = 0;
				//4开启编辑状态
				$('#dorm_zggl_datagrid').datagrid('beginEdit', editing);
			}
		}
		function dorm_zggl_removeFun() {
			var arr = $('#dorm_zggl_datagrid').datagrid('getChecked');
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
						$.post('staffAction!remove.action', {
							ids : ids
						}, function(result) {
							$('#dorm_zggl_datagrid').datagrid('load');
							$.messager.show({
								title : '提示信息',
								msg : '操作成功!'
							});
							$('#dorm_zggl_datagrid').datagrid('uncheckAll');
						});

					} else {
						return;
					}
				});
			}
		}
		function dorm_zggl_editFun() {
			var arr = $('#dorm_zggl_datagrid').datagrid('getChecked');
			if (arr.length != 1) {
				$.messager.show({
					title : '提示信息',
					msg : '只能选择一条记录进行修改!'
				});
			} else {
				if (editing == undefined) {
					flag = 'edit';
					//根据行记录对象获取该行的索引位置
					editing = $('#dorm_zggl_datagrid').datagrid('getRowIndex', arr[0]);
					//开启编辑状态
					$('#dorm_zggl_datagrid').datagrid('beginEdit', editing);
				}
			}
		}
		function dorm_zggl_searchFun() {
			if ($('#dorm_zggl_layout').layout('panel', 'north').panel('options').collapsed) {
				$('#dorm_zggl_layout').layout('expand', 'north');
			} else {
				$('#dorm_zggl_layout').layout('collapse', 'north');
			}
		}
		function dorm_zggl_saveFun() {
			//保存之前进行数据的校验 , 然后结束编辑并设置编辑状态字段 
			if ($('#dorm_zggl_datagrid').datagrid('validateRow', editing)) {
				$('#dorm_zggl_datagrid').datagrid('endEdit', editing);
				editing = undefined;
			} else {
				$.messager.show({
					title : '提示信息',
					msg : '数据验证不通过，请检查！'
				});
			}
		}
		function dorm_zggl_cancelFun() {
			$('#dorm_zggl_datagrid').datagrid('uncheckAll');
			//回滚数据 
			$('#dorm_zggl_datagrid').datagrid('rejectChanges');
			editing = undefined;
		}
		function dorm_zggl_exportExcelFun() {
			$("#dorm_zggl_searchForm").attr("action", "staffAction!exportExcel.action").submit();
			$.messager.show({
				title : '提示信息',
				msg : '正在导出，请稍等!'
			});
		}
		$('#dorm_zggl_searchbth').click(function() {
			$('#dorm_zggl_datagrid').datagrid('load', serializeObject($('#dorm_zggl_searchForm')));
		});
		$('#dorm_zggl_clearbth').click(function() {
			$('#dorm_zggl_searchForm').form('clear');
			$('#dorm_zggl_datagrid').datagrid('load', {});
		});
	</script>
	<div id="dorm_zggl_layout" class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'north',title:'查询条件',border:false,collapsed:true" style="height: 100px;">
			<form id="dorm_zggl_searchForm" method="post">
				模糊查询:&nbsp;&nbsp;&nbsp;&nbsp; 楼管名称:<input type="text" name="name" class="easyui-validatebox" value="">&nbsp;&nbsp;&nbsp;&nbsp; 楼宇名称:<input type="text" name="buildingname" class="easyui-validatebox" value="">&nbsp;&nbsp;&nbsp;&nbsp; <a id="dorm_zggl_searchbth" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a> <a id="dorm_zggl_clearbth"
					class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">清空</a>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<table id="dorm_zggl_datagrid"></table>
		</div>
	</div>
	<div id="dorm_zggl_toolbar" class="" style="display: none;">
		<table cellspacing="0" cellpadding="0">
			<tr>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'staffAction!add.action')}">
					<td><a onclick="dorm_zggl_addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-user_add'">添加</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'staffAction!remove.action')}">
					<td><a onclick="dorm_zggl_removeFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-user_delete'">删除</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'staffAction!edit.action')}">
					<td><a onclick="dorm_zggl_editFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-user_edit'">修改</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<td><a onclick="dorm_zggl_searchFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-user_go'">查询</a></td>
				<td>
					<div class="datagrid-btn-separator"></div>
				</td>
				<td><a onclick="dorm_zggl_saveFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'">保存操作</a></td>
				<td>
					<div class="datagrid-btn-separator"></div>
				</td>
				<td><a onclick="dorm_zggl_cancelFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a></td>
				<td>
					<div class="datagrid-btn-separator"></div>
				</td>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'staffAction!exportExcel.action')}">
					<td><a onclick="dorm_zggl_exportExcelFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-exportexcel'">导出excel</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
			</tr>
		</table>
	</div>
</body>
</html>