<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<script type="text/javascript" charset="utf-8">
	$(function() {

		$('#layout_east_calendar').calendar({
			fit : true,
			current : new Date(),
			border : false,
			onSelect : function(date) {
				$(this).calendar('moveTo', new Date());
			}
		});
		$('#layout_east_onlinePanel').panel({
			tools : [ {
				iconCls : 'database_refresh',
				handler : function() {
				}
			} ]
		});
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height: 200px; overflow: hidden;">
		<div id="layout_east_calendar"></div>
	</div>
	<div data-options="region:'center',border:false" style="overflow: hidden;">
		<div id="layout_east_onlinePanel" data-options="fit:true,border:false" align="center" title="侧边栏">
			<div>
				<span class="label label-success">徐冰浩</span><br />(8000元)
			</div>
			<div>
				<span class="label label-success">张 双</span>(扩展项目)<br />(5000元)
			</div>
			<div>
				<span class="label label-success">***</span><br />()(1000元)
			</div>
			<div class="well well-small" style="margin: 3px;">如发现系统有BUG，请发Email:786991884@qq.com</div>
		</div>
	</div>
</div>