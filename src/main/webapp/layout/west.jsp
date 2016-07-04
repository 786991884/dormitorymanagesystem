<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="easyui-panel" data-options="title:'功能导航',iconCls:'icon-administrator',border:false,fit:true">
	<div id="aa" class="easyui-accordion" data-options="fit:true,border:false">
		<!-- <div title="系统菜单" class="" data-options="iconCls:'icon-menu',border:false">
			<ul id="layout_west_tree" class="easyui-tree"
				data-options="url:'menuAction!getAllTreeNode.action',
						parentField:'pid',
						lines:true,
						onLoadSuccess:function(node,data){
					   		$(this).tree('collapseAll');
					    },
					    onClick:function(node){
					      if(node.attributes.url){
					          var url=node.attributes.url; 
					          addTab({
					             title:node.text,
					             href:url,
					             closable:true,
					             iconCls:node.iconCls
					           });
					        }
					    }">
			</ul>
		</div> -->

		<div title="系统菜单" data-options="iconCls:'icon-group_key',border:false">
			<ul id="layout_west_tree3" class="easyui-tree"
				data-options="url:'resourceAction!tree.action',
			            parentField:'pid',
						lines:true,
						onLoadSuccess:function(node,data){
					   		$(this).tree('collapseAll');
					    },
			       onClick:function(node){
					      if(node.attributes.url){
					          var url=node.attributes.url;
					          addTab({
					             title:node.text,
					             href:url,
					             closable:true,
					             iconCls:node.iconCls
					           });
					        }
					    }">
				<!-- <li><span>权限管理</span>
					<ul>
						<li><span>资源管理</span></li>
						<li><span>角色管理</span></li>
						<li><span>机构管理</span></li>
					</ul></li>
				<li><span>文件管理</span></li> -->
			</ul>
		</div>

		<div title="报表管理" data-options="iconCls:'icon-chart_bar',border:false">
			<ul id="layout_west_tree2" class="easyui-tree"
				data-options="url:'layout/report.json',onClick:function(node){
					      if(node.attributes.url){
					          var url=node.attributes.url;
					           $.messager.show({
							      title : '提示信息',
							      msg : '正在加载报表，请稍等!'
						       }); 
					          addTab({
					             title:node.text,
					             href:url,
					             closable:true,
					             iconCls:node.iconCls
					           });
					        }
					    }">
				<!-- <li><span>查看报表</span>
					<ul>
						<li><span>学生报表</span></li>
						<li><span>宿舍报表</span></li>
					</ul></li> -->
			</ul>
		</div>
	</div>
</div>