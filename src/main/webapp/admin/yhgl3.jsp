<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
</head>
<body>
	<script type="text/javascript">
		$(function() {
			$('#admin_yhgl_datagrid').edatagrid({
				url : 'userAction!datagrid.action',
				saveUrl : 'userAction!add.action',
				updateUrl : 'userAction!edit.action',
				destroyUrl : 'user!remove.action',
				fit : true,
				fitColumns : true,
				rownumbers : true,
				border : false,
				pagination : true,
				idField : 'id',
				//pagePosition : 'both',
				pageSize : 5,
				pageList : [ 5, 8, 15, 20, 50 ],
				sortName : 'name',
				sortOrder : 'asc',
				rownumbers : true,
				checkOnSelect : false,
				selectOnCheck : true,
				//singleSelect : true,
				frozenColumns : [ [ {
					field : 'id',
					title : '编号',
					width : 100,
					//hidden : true,
					checkbox : true
				}, {
					field : 'name',
					title : '登录名称',
					width : 100,
					sortable : true,
					editor : {
						type : 'validatebox',
						options : {
							required : true
						}
					}
				} ] ],
				columns : [ [ {
					field : 'pwd',
					title : '密码',
					width : 100,
					formatter : function(value, row, index) {
						//return '<span title='+value+'>' + value + '</span>';
						return '******';
					},
					editor : {
						type : 'validatebox',
						options : {
							required : true
						}
					}
				}, {
					field : 'createdatetime',
					title : '创建时间',
					width : 100,
					sortable : true,
					editor : {
						type : 'datetimebox',
						options : {
							required : true
						}
					}
				}, {
					field : 'modifydatetime',
					title : '修改时间',
					width : 100,
					editor : {
						type : 'datetimebox',
						options : {
							required : true
						}
					}
				} ] ],
				toolbar : [ {
					text : '增加',
					iconCls : 'icon-add',
					plain : true, //显示简洁效果
					handler : function() {
						//append();
						$('#admin_yhgl_datagrid').edatagrid('addRow');
					}
				}, '-', {
					text : '删除',
					iconCls : 'icon-remove',
					handler : function() {
						//remove();
						$('#admin_yhgl_datagrid').edatagrid('destroyRow');
					}
				}, '-', {
					text : '保存',
					iconCls : 'icon-save',
					handler : function() {
						//editFun();
						$('#admin_yhgl_datagrid').edatagrid('saveRow');
					}
				}, '-', {
					text : '取消操作',
					iconCls : 'icon-undo',
					handler : function() {
						$('#admin_yhgl_datagrid').edatagrid('cancelRow');
					}
				}, '-', {
					text : '查询用户',
					iconCls : 'icon-search',
					handler : function() {
						$('#admin_yhgl_layout').layout('expand', 'north');
					}
				} ]
			});
		});

		function remove() {
			var rows = $('#admin_yhgl_datagrid').datagrid('getChecked');
			var ids = [];
			$.messager.confirm('确认删除', '您是否要删除当前选中的行?', function(r) {
				if (r) {
					if (rows.length > 0) {
						for (var i = 0; i < rows.length; i++) {
							ids.push(rows[i].id);
						}
						$.ajax({
							type : 'post',
							url : 'user!remove.action',
							data : {
								ids : ids.join(',')
							},
							dataType : 'json',
							success : function(r) {
								$('#admin_yhgl_datagrid').datagrid('load');
								$('#admin_yhgl_datagrid').datagrid('unselectAll');
								$.messager.show({
									title : '提示信息',
									msg : r.msg
								});
							}
						});
					}
				} else {
					$.messager.show({
						title : '提示信息',
						msg : '请至少选中一行删除！！！'
					});
				}
			});
		}
		function editFun() {
			var rows = $('#admin_yhgl_datagrid').datagrid('getChecked');
			if (rows.length == 1) {
				var d = $('<div/>').dialog({
					width : 500,
					height : 200,
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
		$('#searchbtn').click(function() {
			$('#admin_yhgl_datagrid').datagrid('load', serializeObject($('#admin_yhgl_searchForm')));
		});
		$('#clearbtn').click(function() {
			$('#admin_yhgl_searchForm').form('clear');
			$('#admin_yhgl_datagrid').datagrid('load', {});
		});
	</script>
	<div id="admin_yhgl_layout" class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'north',title:'查询条件',border:false,collapsed:true" style="height: 100px;">
			<form id="admin_yhgl_searchForm" method="post">
				模糊查询: 用户名：&nbsp;&nbsp;&nbsp;&nbsp;<input name="name" value="" class="easyui-validatebox">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;创建时间：<input name="createdatetime" class="easyui-datetimebox" style="width: 160px;" editable=false value="">&nbsp;&nbsp;&nbsp;&nbsp;结束时间：<input name="modifydatetime" class="easyui-datetimebox" style="width: 160px;" editable=false value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<a id="searchbtn" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">查询</a> <a id="clearbtn" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true">清空</a>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<table id="admin_yhgl_datagrid"></table>
		</div>
	</div>
	<div id="admin_yhgl_addDialog" class="easyui-dialog" style="width: 500px; height: 200px; padding: 10px 20px" align="center"
		data-options="closed:true,
	                modal:true,title:'添加用户',
	                buttons:[{
	                text:'添加',
	                iconCls:'icon-add',
	                handler:function(){
	                    $('#admin_yhgl_addForm').form('submit',{
	                        url:'userAction!add.action',
	                        success:function(r){
	                            var obj=$.parseJSON(r);
	                            if(obj.success){
	                               /*$('#admin_yhgl_datagrid').datagrid('load');*/
	                              /* $('#admin_yhgl_addDialog').dialog('appendRow',obj.obj);*/
	                               $('#admin_yhgl_addDialog').dialog('insertRow',{
	                                  index:0,
	                                  row:obj.obj
	                               });
	                               $('#admin_yhgl_addDialog').dialog('close');
	                            }
	                            $.messager.show({
	                               title:'提示',
	                               msg:obj.msg
	                            });
	                        }
	                    });
	                },{
	                    text:'取消',
	                    iconCls:'icon-cancel',
	                    handler:function(){
	                        $('#admin_yhgl_addDialog').dialog('close');
	                    }
	                }
	              }, '-']">
		<div class="ftitle" style="font-size: 14px; font-weight: bold; color: #666; padding: 5px 0; margin-bottom: 10px; border-bottom: 1px solid #ccc;">用户信息</div>
		<form id="admin_yhgl_addForm" style="margin: 0; padding: 10px 30px;" method="post">
			<div class="fitem" style="margin-bottom: 5px;">
				<label style="display: inline-block; width: 80px;">编号:</label> <input name="id" class="easyui-validatebox" data-options="required:true">
			</div>
			<div class="fitem" style="margin-bottom: 5px;">
				<label style="display: inline-block; width: 80px;">登录名称:</label> <input name="name" class="easyui-validatebox" required="true">
			</div>
			<div class="fitem" style="margin-bottom: 5px;">
				<label style="display: inline-block; width: 80px;">密码:</label> <input name="pwd" class="easyui-validatebox" data-options="required:true">
			</div>
			<div class="fitem" style="margin-bottom: 5px;">
				<label style="display: inline-block; width: 80px;">创建时间:</label> <input name="createdatetime" class="easyui-validatebox" data-options="required:true" validType="email">
			</div>
			<div class="fitem" style="margin-bottom: 5px;">
				<label style="display: inline-block; width: 80px;">最后修改时间:</label> <input name="modifydatetime" class="easyui-validatebox" data-options="required:true" validType="email">
			</div>
		</form>
	</div>

</body>
</html>