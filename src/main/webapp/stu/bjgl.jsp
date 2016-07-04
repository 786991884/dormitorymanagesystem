<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>班级管理</title>
</head>
<body>
	<script type="text/javascript">
		var editing; //判断是否处于编辑状态 
		var flag; //判断新增和修改方法
		$(function() {
			$('#stu_bjgl_datagrid').datagrid({
				idField : 'id',
				fit : true,
				border : false,
				//title : '班级信息表',
				fitColumns : true,
				checkOnSelect : false,
				selectOnCheck : true,
				url : 'clazzAction!datagrid.action',
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
					title : '班级名称',
					width : 100,
					sortable : true,
					align : 'center',
					editor : {
						type : 'validatebox',
						options : {
							required : true,
							missingMessage : '名称必填!'
						}
					}
				}, {
					field : 'teachername',
					title : '班主任',
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
					title : '电话',
					width : 100,
					sortable : true,
					editor : {
						type : 'validatebox',
						options : {
							validType : 'mobile'
						}
					}
				}, {
					field : 'professionname',
					title : '所属专业',
					width : 100,
					sortable : false,
					editor : {
						type : 'combobox',
						options : {
							mode : 'remote',
							url : 'clazzAction!getProfessions.action',
							valueField : 'id',
							textField : 'name',
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
					title : '备注信息',
					width : 150,
					sortable : false,
					formatter : function(value, record, index) {
						if (value) {
							return '<span title='+value+'>' + value + '</span>';
						}
					},
					editor : {
						type : 'validatebox',
						options : {
							editable : true
						}
					}
				} ] ],
				pagination : true,
				pageSize : 10,
				pageList : [ 5, 10, 15, 20, 50 ],
				toolbar : '#stu_bjgl_toolbar',
				onAfterEdit : function(index, record, changes) {
					if (!record.professionname || $.trim(record.professionname) === "") {
						record.professionname = 0;
					}
					if (!$.isNumeric(record.professionname)) {
						record.professionname = null;
					}
					$.post(flag == 'add' ? 'clazzAction!add.action' : 'clazzAction!edit.action', record, function(r) {
						if (r && r.success) {
							changes.professionname = r.obj.professionname;
							$('#stu_bjgl_datagrid').datagrid('updateRow', {
								index : index,
								row : changes
							});
							//$('#stu_bjgl_datagrid').datagrid('acceptChanges');
							$.messager.show({
								title : '提示信息',
								msg : '操作成功!'
							});
						} else {
							$('#stu_bjgl_datagrid').datagrid('rejectChanges');
							$.messager.show({
								title : '提示信息',
								msg : r.msg
							});
						}

					}, "json");
					$('#stu_bjgl_datagrid').datagrid('uncheckRow', editing);
					$('#stu_bjgl_datagrid').datagrid('unselectAll');
					$('#stu_bjgl_datagrid').datagrid('selectRow', editing);
					editing == undefined;
				}

			});

		});
		function stu_bjgl_addFun() {
			if (editing == undefined) {
				flag = 'add';
				//1 先取消所有的选中状态
				$('#stu_bjgl_datagrid').datagrid('clearChecked');
				$('#stu_bjgl_datagrid').datagrid('clearSelections');
				//2追加一行
				//$('#stu_xsgl_datagrid').datagrid('appendRow', {});
				$('#stu_bjgl_datagrid').datagrid('insertRow', {
					index : 0,
					row : {}
				});

				//3获取当前页的行号
				//editing = $('#stu_xsgl_datagrid').datagrid('getRows').length - 1;
				editing = 0;
				//4开启编辑状态
				$('#stu_bjgl_datagrid').datagrid('beginEdit', editing);
			}
		}
		function stu_bjgl_removeFun() {
			var arr = $('#stu_bjgl_datagrid').datagrid('getChecked');
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
						$.post('clazzAction!remove.action', {
							ids : ids
						}, function(result) {
							$('#stu_bjgl_datagrid').datagrid('load');
							$.messager.show({
								title : '提示信息',
								msg : '操作成功!'
							});
							$('#stu_bjgl_datagrid').datagrid('uncheckAll');
						});

					} else {
						return;
					}
				});
			}
		}
		function stu_bjgl_editFun() {
			var arr = $('#stu_bjgl_datagrid').datagrid('getChecked');
			if (arr.length != 1) {
				$.messager.show({
					title : '提示信息',
					msg : '只能选择一条记录进行修改!'
				});
			} else {
				if (editing == undefined) {
					flag = 'edit';
					//根据行记录对象获取该行的索引位置
					editing = $('#stu_bjgl_datagrid').datagrid('getRowIndex', arr[0]);
					//开启编辑状态
					$('#stu_bjgl_datagrid').datagrid('beginEdit', editing);
				}
			}
		}
		function stu_bjgl_searchFun() {
			if ($('#stu_bjgl_layout').layout('panel', 'north').panel('options').collapsed) {
				$('#stu_bjgl_layout').layout('expand', 'north');
			} else {
				$('#stu_bjgl_layout').layout('collapse', 'north');
			}
		}
		function stu_bjgl_saveFun() {
			//保存之前进行数据的校验 , 然后结束编辑并设置编辑状态字段 
			if ($('#stu_bjgl_datagrid').datagrid('validateRow', editing)) {
				$('#stu_bjgl_datagrid').datagrid('endEdit', editing);
				editing = undefined;
			} else {
				$.messager.show({
					title : '提示信息',
					msg : '数据验证不通过，请检查！'
				});
			}
		}
		function stu_bjgl_cancelFun() {
			$('#stu_bjgl_datagrid').datagrid('uncheckAll');
			//回滚数据 
			$('#stu_bjgl_datagrid').datagrid('rejectChanges');
			editing = undefined;
		}
		function stu_bjgl_exportExcelFun() {
			$("#stu_bjgl_searchForm").attr("action", "clazzAction!exportExcel.action").submit();
			$.messager.show({
				title : '提示信息',
				msg : '正在导出，请稍等!'
			});
		}
		$('#stu_bjgl_searchbtn').click(function() {
			$('#stu_bjgl_datagrid').datagrid('load', serializeObject($('#stu_bjgl_searchForm')));
		});
		$('#stu_bjgl_clearbtn').click(function() {
			$('#stu_bjgl_searchForm').form('clear');
			$('#stu_bjgl_datagrid').datagrid('load', {});
		});
	</script>
	<div id="stu_bjgl_layout" class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'north',title:'查询条件',border:false,collapsed:true" style="height: 100px;">
			<form id="stu_bjgl_searchForm" method="post">
				模糊查询:&nbsp;&nbsp;&nbsp;&nbsp; 班级名称:<input name="name" value="" class="easyui-validatebox">&nbsp;&nbsp;&nbsp;&nbsp; 专业名称:<input name="professionname" value="" class="easyui-validatebox">&nbsp;&nbsp;&nbsp;&nbsp; <a id="stu_bjgl_searchbtn" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a> <a id="stu_bjgl_clearbtn" class="easyui-linkbutton"
					data-options="iconCls:'icon-cancel'">清空</a>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<table id="stu_bjgl_datagrid"></table>
		</div>
	</div>
	<div id="stu_bjgl_toolbar" class="" style="display: none;">
		<table cellspacing="0" cellpadding="0">
			<tr>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'clazzAction!add.action')}">
					<td><a onclick="stu_bjgl_addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-user_add'">添加</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'clazzAction!remove.action')}">
					<td><a onclick="stu_bjgl_removeFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-user_delete'">删除</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'clazzAction!edit.action')}">
					<td><a onclick="stu_bjgl_editFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-user_edit'">修改</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<td><a onclick="stu_bjgl_searchFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-user_go'">查询</a></td>
				<td>
					<div class="datagrid-btn-separator"></div>
				</td>
				<td><a onclick="stu_bjgl_saveFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'">保存操作</a></td>
				<td>
					<div class="datagrid-btn-separator"></div>
				</td>
				<td><a onclick="stu_bjgl_cancelFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a></td>
				<td>
					<div class="datagrid-btn-separator"></div>
				</td>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'clazzAction!exportExcel.action')}">
					<td><a onclick="stu_bjgl_exportExcelFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-exportexcel'">导出excel</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
			</tr>
		</table>
	</div>
</body>
</html>