<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>床位管理</title>
</head>
<body>
	<script type="text/javascript">
		var editing; //判断用户是否处于编辑状态 
		var flag; //判断新增和修改方法
		$(function() {
			$('#dorm_cwgl_datagrid').datagrid({
				idField : 'id',
				fit : true,
				border : false,
				fitColumns : true,
				checkOnSelect : false,
				selectOnCheck : true,
				url : 'bedAction!datagrid.action',
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
					title : '床位号',
					width : 100,
					sortable : true,
					align : 'center',
					editor : {
						type : 'validatebox',
						options : {
							required : true,
							missingMessage : '床位号必填!'
						}
					}
				}, {
					field : 'studentnumber',
					title : '学号',
					width : 120,
					sortable : false,
					styler : function(value, record) {
					},
					editor : {
						type : 'combobox',
						options : {
							mode : 'remote',
							url : 'bedAction!getStudents.action',
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
					field : 'dormitoryname',
					title : '房间号',
					width : 100,
					sortable : false,
					formatter : function(value, record, index) {
						return value;
					},
					editor : {
						type : 'combobox',
						options : {
							mode : 'remote',
							url : 'bedAction!getDormitorys.action',
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
					title : '备注信息',
					width : 150,
					sortable : false,
					align : 'center',
					formatter : function(value, record, index) {
						if (value) {
							return '<span title='+value+'>' + value + '</span>';
						}
					},
					editor : {
						type : 'validatebox',
						options : {

						}
					}
				} ] ],
				pagination : true,
				pageSize : 10,
				pageList : [ 5, 10, 15, 20, 50 ],
				toolbar : '#dorm_cwgl_toolbar',
				onAfterEdit : function(index, record, changes) {
					if (!record.studentnumber || $.trim(record.studentnumber) === "") {
						record.studentnumber = 0;
					}
					if (!record.dormitoryname || $.trim(record.dormitoryname) === "") {
						record.dormitoryname = 0;
					}
					if ($.isNumeric(record.studentnumber) && record.studentnumber.length > 8) {
						record.studentnumber = null;
					}
					if (!$.isNumeric(record.dormitoryname)) {
						record.dormitoryname = null;
					}
					$.post(flag == 'add' ? 'bedAction!add.action' : 'bedAction!edit.action', record, function(r) {
						if (r && r.success) {
							changes.studentnumber = r.obj.studentnumber;
							changes.dormitoryname = r.obj.dormitoryname;
							$('#dorm_cwgl_datagrid').datagrid('updateRow', {
								index : index,
								row : changes
							});
							//$('#dorm_cwgl_datagrid').datagrid('acceptChanges');
							$.messager.show({
								title : '提示信息',
								msg : '操作成功!'
							});
						} else {
							$('#dorm_cwgl_datagrid').datagrid('rejectChanges');
							$.messager.show({
								title : '提示信息',
								msg : r.msg
							});
						}

					}, "json");
					$('#dorm_cwgl_datagrid').datagrid('uncheckRow', editing);
					$('#dorm_cwgl_datagrid').datagrid('unselectAll');
					$('#dorm_cwgl_datagrid').datagrid('selectRow', editing);
					editing == undefined;

					/* var inserted = $('#dorm_cwgl_datagrid').datagrid('getChanges', 'inserted');
					var updated = $('#dorm_cwgl_datagrid').datagrid('getChanges', 'updated');
					if (inserted.length<1&&updated.length<1) {
						editing = undefined;
						$('#dorm_cwgl_datagrid').datagrid('unselectAll');
						return;
					}
					var url = '';
					if (inserted.length > 0) {
						url = 'UserServlet?method=save';
					}
					if (updated.length > 0) {
						url = 'UserServlet?method=update';
					}
					$.ajax({
						url : url,
						data : record,
						dataType : 'json',
						success : function(r) {
							if (r && r.success) {
								$('#dorm_cwgl_datagrid').datagrid('acceptChanges');
								$.messager.show({});
							} else {
								$('#dorm_cwgl_datagrid').datagrid('rejectChanges');
								$.messager.show({});
							}
						}
					});
					editing = undefined;
					$('#dorm_cwgl_datagrid').datagrid('unselectAll'); */
				}

			});

		});
		function dorm_cwgl_addFun() {
			if (editing == undefined) {
				flag = 'add';
				//1 先取消所有的选中状态
				$('#dorm_cwgl_datagrid').datagrid('clearChecked');
				$('#dorm_cwgl_datagrid').datagrid('clearSelections');
				//2追加一行
				//$('#dorm_cwgl_datagrid').datagrid('appendRow', {});
				$('#dorm_cwgl_datagrid').datagrid('insertRow', {
					index : 0,
					row : {}
				});
				//3获取当前页的行号
				//editing = $('#dorm_cwgl_datagrid').datagrid('getRows').length - 1;
				editing = 0;
				//4开启编辑状态
				$('#dorm_cwgl_datagrid').datagrid('beginEdit', editing);
			}
		}
		function dorm_cwgl_removeFun() {
			var arr = $('#dorm_cwgl_datagrid').datagrid('getChecked');
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
						$.post('bedAction!remove.action', {
							ids : ids
						}, function(result) {
							$('#dorm_cwgl_datagrid').datagrid('load');
							$.messager.show({
								title : '提示信息',
								msg : '操作成功!'
							});
							$('#dorm_cwgl_datagrid').datagrid('uncheckAll');
						});

					} else {
						return;
					}
				});
			}
		}
		function dorm_cwgl_editFun() {
			var arr = $('#dorm_cwgl_datagrid').datagrid('getChecked');
			if (arr.length != 1) {
				$.messager.show({
					title : '提示信息',
					msg : '只能选择一条记录进行修改!'
				});
			} else {
				if (editing == undefined) {
					flag = 'edit';
					//根据行记录对象获取该行的索引位置
					editing = $('#dorm_cwgl_datagrid').datagrid('getRowIndex', arr[0]);
					//开启编辑状态
					$('#dorm_cwgl_datagrid').datagrid('beginEdit', editing);
				}
			}
		}
		function dorm_cwgl_searchFun() {
			if ($('#dorm_cwgl_layout').layout('panel', 'north').panel('options').collapsed) {
				$('#dorm_cwgl_layout').layout('expand', 'north');
			} else {
				$('#dorm_cwgl_layout').layout('collapse', 'north');
			}
		}
		function dorm_cwgl_saveFun() {
			//保存之前进行数据的校验 , 然后结束编辑并设置编辑状态字段 
			if ($('#dorm_cwgl_datagrid').datagrid('validateRow', editing)) {
				$('#dorm_cwgl_datagrid').datagrid('endEdit', editing);
				editing = undefined;
			} else {
				$.messager.show({
					title : '提示信息',
					msg : '数据验证不通过，请检查！'
				});
			}
		}
		function dorm_cwgl_cancelFun() {
			$('#dorm_cwgl_datagrid').datagrid('uncheckAll');
			//回滚数据 
			$('#dorm_cwgl_datagrid').datagrid('rejectChanges');
			editing = undefined;
		}
		function dorm_cwgl_exportExcelFun() {
			$("#dorm_cwgl_serarchForm").attr("action", "bedAction!exportExcel.action").submit();
			$.messager.show({
				title : '提示信息',
				msg : '正在导出，请稍等!'
			});
		}
		$('#dorm_cwgl_searchbth').click(function() {
			$('#dorm_cwgl_datagrid').datagrid('load', serializeObject($('#dorm_cwgl_serarchForm')));
		});
		$('#dorm_cwgl_clearbth').click(function() {
			$('#dorm_cwgl_serarchForm').form('clear');
			$('#dorm_cwgl_datagrid').datagrid('load', {});
		});
	</script>
	<div id="dorm_cwgl_layout" class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'north',title:'查询条件',border:false,collapsed:true" style="height: 100px;">
			<form id="dorm_cwgl_serarchForm" method="post">
				模糊查询:&nbsp;&nbsp;&nbsp;&nbsp;床位号:<input name="name" value="" class="easyui-validatebox"> &nbsp;&nbsp;&nbsp;&nbsp; 学号:<input name="studentnumber" value="" class="easyui-validatebox">&nbsp;&nbsp;&nbsp;&nbsp; 宿舍号:<input name="dormitoryname" value="" class="easyui-validatebox">&nbsp;&nbsp;&nbsp;&nbsp; <a id="dorm_cwgl_searchbth" class="easyui-linkbutton"
					data-options="iconCls:'icon-search'">查询</a> <a id="dorm_cwgl_clearbth" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">清空</a>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<table id="dorm_cwgl_datagrid"></table>
		</div>
	</div>
	<div id="dorm_cwgl_toolbar" class="" style="display: none;">
		<table cellspacing="0" cellpadding="0">
			<tr>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'bedAction!add.action')}">
					<td><a onclick="dorm_cwgl_addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-table_add'">添加</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'bedAction!remove.action')}">
					<td><a onclick="dorm_cwgl_removeFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-table_delete'">删除</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'bedAction!edit.action')}">
					<td><a onclick="dorm_cwgl_editFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-table_edit'">修改</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<td><a onclick="dorm_cwgl_searchFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-table_go'">查询</a></td>
				<td>
					<div class="datagrid-btn-separator"></div>
				</td>
				<td><a onclick="dorm_cwgl_saveFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-table_save'">保存操作</a></td>
				<td>
					<div class="datagrid-btn-separator"></div>
				</td>
				<td><a onclick="dorm_cwgl_cancelFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a></td>
				<td>
					<div class="datagrid-btn-separator"></div>
				</td>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'bedAction!exportExcel.action')}">
					<td><a onclick="dorm_cwgl_exportExcelFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-exportexcel'">导出excel</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
			</tr>
		</table>
	</div>
</body>
</html>