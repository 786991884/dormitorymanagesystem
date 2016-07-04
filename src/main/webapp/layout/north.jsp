<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<script type="text/javascript">
	$(function() {
		//加载后就开始执行
		var mystyle = $.cookie("mywebstyle");
		if (mystyle) {
			$("#themeLink").attr("href", "js/jquery-easyui-1.3.5/themes/" + mystyle + "/easyui.css");
			//切换select为cookie中保存的值
			$("#themeSel").val(mystyle);
		} else {//用默认样式
			$("#themeLink").attr("href", "js/jquery-easyui-1.3.5/themes/default/easyui.css");
		}
		//修改Link href值？？？什么时间修改
		$("#themeSel").bind("change", function() {
			$("#themeLink").attr("href", "js/jquery-easyui-1.3.5/themes/" + $("#themeSel").val() + "/easyui.css");
			//把选择保存起来，注意用了框架集，要想让本网站的页面都访问到，就必须设置
			$.cookie('mywebstyle', $("#themeSel").val(), {
				expires : 7
			});
		});
	});
	function logoutFun(b) {
		$.getJSON('${pageContext.request.contextPath}/userAction!logout.action', {
			t : new Date()
		}, function(result) {
			if (b) {
				location.replace('${pageContext.request.contextPath}/');
			}
		});
	}

	function editCurrentUserPwd() {
		$.modalDialog({
			title : '修改密码',
			width : 300,
			height : 250,
			href : '${pageContext.request.contextPath}/user/userEditPwd.jsp',
			buttons : [ {
				text : '修改',
				handler : function() {
					var f = $.modalDialog.handler.find('#editCurrentUserPwdForm');
					f.submit();
				}
			} ]
		});
	}
	function currentUserRole() {
		$.modalDialog({
			title : '我的角色',
			width : 300,
			height : 250,
			href : '${pageContext.request.contextPath}/userAction!currentUserRolePage.action'
		});
	}
	function currentUserResource() {
		$.modalDialog({
			title : '我可以访问的资源',
			width : 300,
			height : 250,
			href : '${pageContext.request.contextPath}/userAction!currentUserResourcePage.action'
		});
	}
</script>
<body>
	<div class="container">
		<div class="row clearfix">
			<div class="col-md-12 column">
				<nav style="border: 0px;" class="navbar navbar-default navbar-fixed-top" role="navigation">
					<div class="navbar-header">
						<a class="navbar-brand" href="javascript:void(0)"><span class="glyphicon glyphicon-home">欢迎使用宿舍管理系统</span></a>
					</div>
					<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
						<form class="navbar-form navbar-left" role="search">
							<div class="form-group">
								<label for="themeSel" class="control-label"><p class="text-success" style="font-size: 14px;">主&nbsp;&nbsp;题:&nbsp;&nbsp;</p></label><select class="form-control" id="themeSel">
									<option value="black">black</option>
									<option value="bootstrap">bootstrap</option>
									<option value="cupertino">cupertino</option>
									<option value="dark-hive">dark-hive</option>
									<option value="default" selected="selected">default</option>
									<option value="gray">gray</option>
									<option value="metro">metro</option>
									<option value="pepper-grinder">pepper-grinder</option>
									<option value="sunny">sunny</option>
									<option value="metro-blue">metro-blue</option>
									<option value="metro-green">metro-green</option>
									<option value="metro-gray">metro-gray</option>
									<option value="metro-orange">metro-orange</option>
									<option value="metro-red">metro-red</option>
								</select>
							</div>
							<div class="form-group">
								<input type="text" class="form-control" />
							</div>
							<button type="button" class="btn btn-default">Submit</button>

						</form>

						<ul class="nav navbar-nav navbar-right">
							<li class="dropdown"><a href="javascript:void(0)" class="dropdown-toggle" data-toggle="dropdown">个人详细信息<strong class="caret"></strong></a>
								<ul class="dropdown-menu">
									<li><a href="javascript:void(0)" onclick="editCurrentUserPwd();">修改密码</a></li>
									<li><a href="javascript:void(0)" onclick="currentUserRole();">我的角色</a></li>
									<li><a href="javascript:void(0)" onclick="currentUserResource();">我的权限</a></li>
								</ul></li>
							<div id="index_tabsMenu" style="width: 120px; display: none;">
								<div title="close" data-options="iconCls:'delete'">关闭</div>
								<div title="closeOther" data-options="iconCls:'delete'">关闭其它</div>
								<div title="closeAll" data-options="iconCls:'delete'">关闭所有</div>
							</div>
							<li><a id="userinfo"><c:if test="${sessionInfo.id != null}">[<strong>${sessionInfo.name}</strong>],欢迎你！</c:if></a></li>
							<li class=""><a href="javascript:void(0)" onclick="logoutFun(true);"><span class="glyphicon glyphicon-off">注销</span></a></li>
							<li><a href="javascript:void(0)"><span id="timerLi"><script type="text/javascript">
								function printTime() {
									var objD = new Date();
									var str, colorhead, colorfoot;
									var yy = objD.getYear();
									if (yy < 1900)
										yy = yy + 1900;
									var MM = objD.getMonth() + 1;
									if (MM < 10)
										MM = '0' + MM;
									var dd = objD.getDate();
									if (dd < 10)
										dd = '0' + dd;
									var hh = objD.getHours();
									if (hh < 10)
										hh = '0' + hh;
									var mm = objD.getMinutes();
									if (mm < 10)
										mm = '0' + mm;
									var ss = objD.getSeconds();
									if (ss < 10)
										ss = '0' + ss;
									var ww = objD.getDay();
									if (ww == 0)
										colorhead = "<font color=\"#FF0000\">";
									if (ww > 0 && ww < 6)
										colorhead = "<font color=\"#373737\">";
									if (ww == 6)
										colorhead = "<font color=\"#008000\">";
									if (ww == 0)
										ww = "星期日";
									if (ww == 1)
										ww = "星期一";
									if (ww == 2)
										ww = "星期二";
									if (ww == 3)
										ww = "星期三";
									if (ww == 4)
										ww = "星期四";
									if (ww == 5)
										ww = "星期五";
									if (ww == 6)
										ww = "星期六";
									colorfoot = "</font>";
									str = colorhead + yy + "年" + MM + "月" + dd + "日 " + hh + ":" + mm + ":" + ss + "  " + ww + colorfoot;
									$("#timerLi").empty().append(str);

								}
								printTime();
								setInterval(printTime, 1000);
							</script></span>&nbsp;&nbsp;&nbsp;</a></li>
						</ul>
					</div>
				</nav>
			</div>
		</div>
	</div>
</body>