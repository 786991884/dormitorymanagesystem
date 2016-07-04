<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>学生管理</title>
</head>
<body>
	<script type="text/javascript">
		var editing; //判断用户是否处于编辑状态 
		var flag; //判断新增和修改方法
		$(function() {
			$('#stu_xsgl_datagrid').datagrid({
				idField : 'id',
				fit : true,
				border : false,
				//title : '学生信息表',
				fitColumns : true,
				checkOnSelect : false,
				selectOnCheck : true,
				url : 'studentAction!datagrid.action',
				striped : true,
				loadMsg : '数据正在加载,请耐心的等待...',
				sortName : 'id',
				sortOrder : 'asc',
				//rownumbers : true,
				frozenColumns : [ [ {
					field : 'id',
					checkbox : true,
					title : '编号',
					width : 120
				} ] ],
				columns : [ [ {
					field : 'number',
					title : '学号',
					width : 120,
					sortable : true,
					align : 'left',
					editor : {
						type : 'validatebox',
						options : {
							validType : 'length[8, 12]',
							required : true,
							missingMessage : '学号必填!'
						}
					}
				}, {
					field : 'name',
					title : '姓名',
					width : 70,
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
					width : 40,
					sortable : true,
					formatter : function(value, record, index) {
						if (value == '男') {
							return '<span style=color:red; >男</span>';
						} else if (value == '女') {
							return '<span style=color:green; >女</span>';
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
					field : 'birthday',
					title : '出生日期',
					width : 100,
					sortable : true,
					editor : {
						type : 'datetimebox',
						options : {
							editable : true
						}
					}
				}, {
					field : 'telephone',
					title : '电话',
					width : 100,
					sortable : true,
					editor : {
						type : 'validatebox',
						options : {
							validType : 'mobile',
							editable : true
						}
					}
				}, {
					field : 'address',
					title : '家庭地址',
					width : 100,
					sortable : false,
					editor : {
						type : 'validatebox',
						option : {
							editable : false
						}
					}
				}, {
					field : 'graddate',
					title : '毕业时间',
					width : 100,
					sortable : true,
					editor : {
						type : 'datetimebox',
						option : {
							editable : false
						}
					}
				}, {
					field : 'livedate',
					title : '入住时间',
					width : 100,
					sortable : true,
					editor : {
						type : 'datetimebox',
						option : {
							editable : false
						}
					}
				}, {
					field : 'clazzname',
					title : '所属班级',
					width : 100,
					sortable : false,
					editor : {
						type : 'combobox',
						options : {
							mode : 'remote',
							url : 'studentAction!getClazzs.action',
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
					field : 'bedname',
					title : '所属床位',
					width : 100,
					sortable : false,
					editor : {
						type : 'combobox',
						options : {
							mode : 'remote',
							url : 'studentAction!getBeds.action',
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
					width : 100,
					sortable : false,
					formatter : function(value, record, index) {
						if (value) {
							return '<span title='+value+'>' + value + '</span>';
						}
					},
					editor : {
						type : 'validatebox',
						option : {
							editable : false
						}
					}
				} ] ],
				pagination : true,
				pageSize : 10,
				pageList : [ 5, 10, 15, 20, 50 ],
				toolbar : '#stu_xsgl_toolbar',
				onBeforeEdit : function(rowIndex, rowData) {
				},
				onAfterEdit : function(index, record, changes) {
					if (!record.bedname || $.trim(record.bedname) === "") {
						record.bedname = 0;
					}
					if (!record.clazzname || $.trim(record.clazzname) === "") {
						record.clazzname = 0;
					}
					if (!$.isNumeric(record.bedname)) {
						record.bedname = null;
					}
					if (!$.isNumeric(record.clazzname)) {
						record.clazzname = null;
					}
					$.post(flag == 'add' ? 'studentAction!add.action' : 'studentAction!edit.action', record, function(r) {
						if (r && r.success) {
							changes.bedname = r.obj.bedname;
							changes.clazzname = r.obj.clazzname;
							$('#stu_xsgl_datagrid').datagrid('updateRow', {
								index : index,
								row : changes
							});
							//$('#stu_xsgl_datagrid').datagrid('acceptChanges');
							$.messager.show({
								title : '提示信息',
								msg : '操作成功!'
							});
						} else {
							$('#stu_xsgl_datagrid').datagrid('rejectChanges');
							$.messager.show({
								title : '提示信息',
								msg : r.msg
							});
						}

					}, "json");
					$('#stu_xsgl_datagrid').datagrid('uncheckRow', editing);
					$('#stu_xsgl_datagrid').datagrid('unselectAll');
					$('#stu_xsgl_datagrid').datagrid('selectRow', editing);
					editing == undefined;
				}

			});

		});
		function stu_xsgl_addFun() {
			if (editing == undefined) {
				flag = 'add';
				//1 先取消所有的选中状态
				$('#stu_xsgl_datagrid').datagrid('clearChecked');
				$('#stu_xsgl_datagrid').datagrid('clearSelections');
				//2追加一行
				//$('#stu_xsgl_datagrid').datagrid('appendRow', {});
				$('#stu_xsgl_datagrid').datagrid('insertRow', {
					index : 0,
					row : {}
				});

				//3获取当前页的行号
				//editing = $('#stu_xsgl_datagrid').datagrid('getRows').length - 1;
				editing = 0;
				//4开启编辑状态
				$('#stu_xsgl_datagrid').datagrid('beginEdit', editing);
			}
		}
		function stu_xsgl_removeFun() {
			var arr = $('#stu_xsgl_datagrid').datagrid('getChecked');
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
						$.post('studentAction!remove.action', {
							ids : ids
						}, function(result) {
							$('#stu_xsgl_datagrid').datagrid('load');
							$.messager.show({
								title : '提示信息',
								msg : '操作成功!'
							});
							$('#stu_xsgl_datagrid').datagrid('uncheckAll');
						});

					} else {
						return;
					}
				});
			}
		}
		function stu_xsgl_editFun() {
			var arr = $('#stu_xsgl_datagrid').datagrid('getChecked');
			if (arr.length != 1) {
				$.messager.show({
					title : '提示信息',
					msg : '只能选择一条记录进行修改!'
				});
			} else {
				if (editing == undefined) {
					flag = 'edit';
					//根据行记录对象获取该行的索引位置
					editing = $('#stu_xsgl_datagrid').datagrid('getRowIndex', arr[0]);
					//开启编辑状态
					$('#stu_xsgl_datagrid').datagrid('beginEdit', editing);
				}
			}
		}
		function stu_xsgl_searchFun() {
			if ($('#stu_xsgl_layout').layout('panel', 'north').panel('options').collapsed) {
				$('#stu_xsgl_layout').layout('expand', 'north');
			} else {
				$('#stu_xsgl_layout').layout('collapse', 'north');
			}
		}
		function stu_xsgl_saveFun() {
			//保存之前进行数据的校验 , 然后结束编辑并设置编辑状态字段 
			if ($('#stu_xsgl_datagrid').datagrid('validateRow', editing)) {
				$('#stu_xsgl_datagrid').datagrid('endEdit', editing);
				editing = undefined;
			} else {
				$.messager.show({
					title : '提示信息',
					msg : '数据验证不通过，请检查！'
				});
			}
		}
		function stu_xsgl_cancelFun() {
			$('#stu_xsgl_datagrid').datagrid('uncheckAll');
			//回滚数据 
			$('#stu_xsgl_datagrid').datagrid('rejectChanges');
			editing = undefined;
		}
		function stu_xsgl_exportExcelFun() {
			$("#stu_xsgl_searchForm").attr("action", "studentAction!exportExcel.action").submit();
			$.messager.show({
				title : '提示信息',
				msg : '正在导出，请稍等!'
			});
		}
		function stu_xsgl_downtemplateFun() {
			var url = "downloadTemplateAction!studentTemplate.action";
			window.location.href = url;
		}
		function stu_xsgl_importExcel() {
			var d = $('<div/>').dialog({
				width : 400,
				height : 300,
				href : 'stu/importExcel.jsp',
				modal : true,
				title : '上传excel数据',
				buttons : [ {
					text : '确定上传',
					handler : function() {
						$.ajaxFileUpload({
							url : 'uploadAction!uploadTemplateData.action',
							secureuri : false,
							fileElementId : 'uploadExcel',
							dataType : 'json',
							success : function(data, status) {
								//服务器成功响应处理函数
								//alert(data.message);//从服务器返回的json中取出message中的数据,其中message为在struts2中action中定义的成员变量
								d.dialog('close');
								$.messager.show({
									title : '提示信息',
									msg : "上传成功！"
								});
							},
							error : function(data, status, e) {
								d.dialog('close');
								//服务器响应失败处理函数
								$.messager.show({
									title : '提示信息',
									msg : "上传成功！"
								});
							}
						});
					}
				} ],
				onClose : function() {
					$(this).dialog('destroy');
				},
				onLoad : function() {
				}
			});
		}
		$('#stu_xsgl_searchbtn').click(function() {
			$('#stu_xsgl_datagrid').datagrid('load', serializeObject($('#stu_xsgl_searchForm')));
		});
		$('#stu_xsgl_clearbtn').click(function() {
			$('#stu_xsgl_searchForm').form('clear');
			$('#stu_xsgl_datagrid').datagrid('load', {});
		});
	</script>
	<div id="stu_xsgl_layout" class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'north',title:'查询条件',border:false,collapsed:true" style="height: 100px;">
			<form id="stu_xsgl_searchForm" method="post">
				模糊查询:&nbsp;&nbsp;&nbsp;&nbsp; 学号:<input name="number" value="" class="easyui-validatebox">&nbsp;&nbsp;&nbsp;&nbsp; 姓名:<input name="name" class="easyui-validatebox" value="">&nbsp;&nbsp;&nbsp;&nbsp; 班级名称:<input name="clazzname" class="easyui-validatebox" value=""> &nbsp;&nbsp;&nbsp;&nbsp; 入住时间:<input name="livedate" class="easyui-datetimebox" style="width: 160px;"
					editable=false value="">&nbsp;&nbsp;&nbsp;&nbsp;<br> 床位号:<input name="bedname" value="" class="easyui-validatebox">&nbsp;&nbsp;&nbsp;&nbsp; <a id="stu_xsgl_searchbtn" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a> <a id="stu_xsgl_clearbtn" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">清空</a>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<table id="stu_xsgl_datagrid"></table>
		</div>
	</div>
	<div id="stu_xsgl_toolbar" class="" style="display: none;">
		<table cellspacing="0" cellpadding="0">
			<tr>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'studentAction!add.action')}">
					<td><a onclick="stu_xsgl_addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-user_add'">添加</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'studentAction!remove.action')}">
					<td><a onclick="stu_xsgl_removeFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-user_delete'">删除</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'studentAction!edit.action')}">
					<td><a onclick="stu_xsgl_editFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-user_edit'">修改</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<td><a onclick="stu_xsgl_searchFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-user_go'">查询</a></td>
				<td>
					<div class="datagrid-btn-separator"></div>
				</td>
				<td><a onclick="stu_xsgl_saveFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'">保存操作</a></td>
				<td>
					<div class="datagrid-btn-separator"></div>
				</td>
				<td><a onclick="stu_xsgl_cancelFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a></td>
				<td>
					<div class="datagrid-btn-separator"></div>
				</td>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'studentAction!exportExcel.action')}">
					<td><a onclick="stu_xsgl_exportExcelFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-exportexcel'">导出excel</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'downloadTemplateAction!studentTemplate.action')}">
					<td><a onclick="stu_xsgl_downtemplateFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-download'">下载数据模版</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
				<c:if test="${fn:contains(sessionInfo.resourceList, 'uploadAction!uploadTemplateData.action')}">
					<td><a onclick="stu_xsgl_importExcel();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-document_import'">导入数据</a></td>
					<td>
						<div class="datagrid-btn-separator"></div>
					</td>
				</c:if>
			</tr>
		</table>
	</div>
</body>
</html>