<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>楼宇管理</title>
</head>
<body>
	<script type="text/javascript">
		var editing; //判断是否处于编辑状态 
		var flag; //判断新增和修改方法
		$(function() {
			$('#dorm_lygl_datagrid').datagrid({
				idField : 'id',
				fit : true,
				border : false,
				fitColumns : true,
				checkOnSelect : false,
				selectOnCheck : true,
				url : 'buildingAction!datagrid.action',
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
					field : 'number',
					title : '楼宇号',
					width : 80,
					sortable : true,
					align : 'center',
					editor : {
						type : 'validatebox',
						options : {
							required : true,
							missingMessage : '楼宇号必填!'
						}
					}
				}, {
					field : 'height',
					title : '楼层数',
					width : 50,
					sortable : true,
					editor : {
						type : 'combobox',
						options : {
							data : [ {
								id : 1,
								val : 1
							}, {
								id : 2,
								val : 2
							}, {
								id : 3,
								val : 3
							}, {
								id : 4,
								val : 4
							}, {
								id : 5,
								val : 5
							}, {
								id : 6,
								val : 6
							} ],
							valueField : 'id',
							textField : 'val',
							required : true,
							missingMessage : '楼层数必填!'
						}
					}
				}, {
					field : 'type',
					title : '楼类型',
					width : 50,
					sortable : false,
					editor : {
						type : 'combobox',
						options : {
							data : [ {
								id : "男",
								val : "男"
							}, {
								id : "女",
								val : "女"
							}, {
								id : "男女混合",
								val : "男女混合"
							} ],
							editable : true
						}
					}
				}, {
					field : 'staffname',
					title : '管理职工',
					width : 100,
					sortable : false,
					styler : function(value, record) {

					},
					editor : {
						type : 'combobox',
						options : {
							mode : 'remote',
							url : 'buildingAction!getStaffs.action',
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
						options : {}
					}
				} ] ],
				pagination : true,
				pageSize : 10,
				pageList : [ 5, 10, 15, 20, 50 ],
				toolbar : '#dorm_lygl_toolbar',
				onAfterEdit : function(index, record, changes) {
					if (!record.staffname || $.trim(record.staffname) === "") {
						record.staffname = 0;
					}
					if (!$.isNumeric(record.staffname)) {
						record.staffname = null;
					}
					$.post(flag == 'add' ? 'buildingAction!add.action' : 'buildingAction!edit.action', record, function(r) {
						if (r && r.success) {
							changes.staffname = r.obj.staffname;
							$('#dorm_lygl_datagrid').datagrid('updateRow', {
								index : index,
								row : changes
							});
							//$('#dorm_lygl_datagrid').datagrid('acceptChanges');
							$.messager.show({
								title : '提示信息',
								msg : '操作成功!'
							});
						} else {
							$('#dorm_lygl_datagrid').datagrid('rejectChanges');
							$.messager.show({
								title : '提示信息',
								msg : r.msg
							});
						}

					}, "json");
					$('#dorm_lygl_datagrid').datagrid('uncheckRow', editing);
					$('#dorm_lygl_datagrid').datagrid('unselectAll');
					$('#dorm_lygl_datagrid').datagrid('selectRow', editing);
					//console.info(editing);
					editing == undefined;
				}

			});

		});
		function dorm_lygl_addFun() {
			if (editing == undefined) {
				flag = 'add';
				//1 先取消所有的选中状态
				$('#dorm_lygl_datagrid').datagrid('clearChecked');
				$('#dorm_lygl_datagrid').datagrid('clearSelections');
				//2追加一行
				//$('#dorm_lygl_datagrid').datagrid('appendRow', {});
				$('#dorm_lygl_datagrid').datagrid('insertRow', {
					index : 0,
					row : {}
				});

				//3获取当前页的行号
				//editing = $('#dorm_lygl_datagrid').datagrid('getRows').length - 1;
				editing = 0;
				//4开启编辑状态
				$('#dorm_lygl_datagrid').datagrid('beginEdit', editing);
			}
		}
		function dorm_lygl_removeFun() {
			var arr = $('#dorm_lygl_datagrid').datagrid('getChecked');
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
						$.post('buildingAction!remove.action', {
							ids : ids
						}, function(result) {
							$('#dorm_lygl_datagrid').datagrid('load');
							$.messager.show({
								title : '提示信息',
								msg : '操作成功!'
							});
							$('#dorm_lygl_datagrid').datagrid('uncheckAll');
						});

					} else {
						return;
					}
				});
			}
		}
		function dorm_lygl_editFun() {
			var arr = $('#dorm_lygl_datagrid').datagrid('getChecked');
			if (arr.length != 1) {
				$.messager.show({
					title : '提示信息',
					msg : '只能选择一条记录进行修改!'
				});
			} else {
				if (editing == undefined) {
					flag = 'edit';
					//根据行记录对象获取该行的索引位置
					editing = $('#dorm_lygl_datagrid').datagrid('getRowIndex', arr[0]);
					//开启编辑状态
					$('#dorm_lygl_datagrid').datagrid('beginEdit', editing);
				}
			}
		}
		function dorm_lygl_searchFun() {
			if ($('#dorm_lygl_layout').layout('panel', 'north').panel('options').collapsed) {
				$('#dorm_lygl_layout').layout('expand', 'north');
			} else {
				$('#dorm_lygl_layout').layout('collapse', 'north');
			}
		}
		function dorm_lygl_saveFun() {
			//保存之前进行数据的校验 , 然后结束编辑并设置编辑状态字段 
			if ($('#dorm_lygl_datagrid').datagrid('validateRow', editing)) {
				$('#dorm_lygl_datagrid').datagrid('endEdit', editing);
				editing = undefined;
			} else {
				$.messager.show({
					title : '提示信息',
					msg : '数据验证不通过，请检查！'
				});
			}
		}
		function dorm_lygl_cancelFun() {
			$('#dorm_lygl_datagrid').datagrid('uncheckAll');
			//回滚数据 
			$('#dorm_lygl_datagrid').datagrid('rejectChanges');
			editing = undefined;
		}
		function dorm_lygl_exportExcelFun() {
			$("#dorm_lygl_searchForm").attr("action", "buildingAction!exportExcel.action").submit();
			$.messager.show({
				title : '提示信息',
				msg : '正在导出，请稍等!'
			});
		}
		$('#dorm_lygl_searchbth').click(function() {
			$('#dorm_lygl_datagrid').datagrid('load', serializeObject($('#dorm_lygl_searchForm')));
		});
		$('#dorm_lygl_clearbth').click(function() {
			$('#dorm_lygl_searchForm').form('clear');
			$('#dorm_lygl_datagrid').datagrid('load', {});
		});
	</script>
	<div id="dorm_lygl_layout" class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'north',title:'查询条件',border:false,collapsed:true" style="height: 100px;">
			<form id="dorm_lygl_searchForm" method="post">
				模糊查询:&nbsp;&nbsp;&nbsp;&nbsp; 楼宇号:<input name="number" value="" class="easyui-validatebox">&nbsp;&nbsp;&nbsp;&nbsp; 职工名:<input name="staffname" value="" class="easyui-validatebox">&nbsp;&nbsp;&nbsp;&nbsp; <a id="dorm_lygl_searchbth" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a> <a id="dorm_lygl_clearbth" class="easyui-linkbutton"
					data-options="iconCls:'icon-cancel'">清空</a>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<table id="dorm_lygl_datagrid"></table>
		</div>
	</div>
	<div id="dorm_lygl_toolbar" class="" style="display: none;">
		<table cellspacing="0" cellpadding="0">
			<tr>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'buildingAction!add.action')}">
					<td><a onclick="dorm_lygl_addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-building_add'">添加</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'buildingAction!remove.action')}">
					<td><a onclick="dorm_lygl_removeFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-building_delete'">删除</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'buildingAction!edit.action')}">
					<td><a onclick="dorm_lygl_editFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-building_edit'">修改</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<td><a onclick="dorm_lygl_searchFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-building_go'">查询</a></td>
				<td>
					<div class="datagrid-btn-separator"></div>
				</td>
				<td><a onclick="dorm_lygl_saveFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'">保存操作</a></td>
				<td>
					<div class="datagrid-btn-separator"></div>
				</td>
				<td><a onclick="dorm_lygl_cancelFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a></td>
				<td>
					<div class="datagrid-btn-separator"></div>
				</td>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'buildingAction!exportExcel.action')}">
					<td><a onclick="dorm_lygl_exportExcelFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-exportexcel'">导出excel</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
			</tr>
		</table>
	</div>
</body>
</html>