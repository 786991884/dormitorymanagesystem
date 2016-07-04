<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
	var index_tabs;
	var index_tabsMenu;
	$(function() {
		$('.carousel').carousel({
			interval : 2000
		});
		index_tabs = $('#layout_center_tabs').tabs({
			fit : true,
			border : false,
			onContextMenu : function(e, title) {
				e.preventDefault();
				index_tabsMenu.menu('show', {
					left : e.pageX,
					top : e.pageY
				}).data('tabTitle', title);
			},
			tools : []
		});
		index_tabsMenu = $('#index_tabsMenu').menu({
			onClick : function(item) {
				var curTabTitle = $(this).data('tabTitle');
				var type = $(item.target).attr('title');

				if (type === 'refresh') {
					index_tabs.tabs('getTab', curTabTitle).panel('refresh');
					return;
				}

				if (type === 'close') {
					var t = index_tabs.tabs('getTab', curTabTitle);
					if (t.panel('options').closable) {
						index_tabs.tabs('close', curTabTitle);
					}
					return;
				}

				var allTabs = index_tabs.tabs('tabs');
				var closeTabsTitle = [];

				$.each(allTabs, function() {
					var opt = $(this).panel('options');
					if (opt.closable && opt.title != curTabTitle && type === 'closeOther') {
						closeTabsTitle.push(opt.title);
					} else if (opt.closable && type === 'closeAll') {
						closeTabsTitle.push(opt.title);
					}
				});
				for (var i = 0; i < closeTabsTitle.length; i++) {
					index_tabs.tabs('close', closeTabsTitle[i]);
				}
			}
		});

	});
	function addTab(opts) {
		var t = $('#layout_center_tabs');
		if (t.tabs('exists', opts.title)) {
			t.tabs('select', opts.title);
		} else {
			t.tabs('add', opts);
		}
	}
</script>
<style type="text/css">
.myimg {
	width: 850px;
	height: 450px;
}
</style>
<div id="layout_center_tabs">
	<div title="首页" data-options="fit:true,iconCls:'icon-application_home'">
		<div class="container">
			<div class="row clearfix">
				<div class="col-md-12 column">
					<div class="carousel slide" id="mycarousel">
						<ol class="carousel-indicators">
							<li data-slide-to="0" data-target="#mycarousel"></li>
							<li data-slide-to="1" data-target="#mycarousel" class="active"></li>
							<li data-slide-to="2" data-target="#mycarousel"></li>
							<li data-slide-to="3" data-target="#mycarousel"></li>
							<li data-slide-to="4" data-target="#mycarousel"></li>
							<li data-slide-to="5" data-target="#mycarousel"></li>
							<li data-slide-to="6" data-target="#mycarousel"></li>
							<li data-slide-to="7" data-target="#mycarousel"></li>
							<li data-slide-to="8" data-target="#mycarousel"></li>
							<li data-slide-to="9" data-target="#mycarousel"></li>
						</ol>
						<div class="carousel-inner">
							<div class="item">
								<img alt="" src="images/001.jpg" class="myimg img-rounded" />
								<div class="carousel-caption">
									<h4>第一张图片</h4>
								</div>
							</div>
							<div class="item active">
								<img alt="" src="images/002.jpg" class="myimg img-rounded" />
								<div class="carousel-caption">
									<h4>第二张图片</h4>
								</div>
							</div>
							<div class="item">
								<img alt="" src="images/003.jpg" class="myimg img-rounded" />
								<div class="carousel-caption">
									<h4>第三张图片</h4>
								</div>
							</div>
							<div class="item">
								<img alt="" src="images/004.jpg" class="myimg img-rounded" />
								<div class="carousel-caption">
									<h4>第四张图片</h4>
								</div>
							</div>
							<div class="item">
								<img alt="" src="images/005.jpg" class="myimg img-rounded" />
								<div class="carousel-caption">
									<h4>第五张图片</h4>
								</div>
							</div>
							<div class="item">
								<img alt="" src="images/006.jpg" class="myimg img-rounded" />
								<div class="carousel-caption">
									<h4>第六张图片</h4>
								</div>
							</div>
							<div class="item">
								<img alt="" src="images/007.jpg" class="myimg img-rounded" />
								<div class="carousel-caption">
									<h4>第七张图片</h4>
								</div>
							</div>
							<div class="item">
								<img alt="" src="images/008.jpg" class="myimg img-rounded" />
								<div class="carousel-caption">
									<h4>第八张图片</h4>
								</div>
							</div>
							<div class="item">
								<img alt="" src="images/009.jpg" class="myimg img-rounded" />
								<div class="carousel-caption">
									<h4>第九张图片</h4>
								</div>
							</div>
							<div class="item">
								<img alt="" src="images/010.jpg" class="myimg img-rounded" />
								<div class="carousel-caption">
									<h4>第十张图片</h4>
								</div>
							</div>
						</div>
						<a class="left carousel-control" href="#mycarousel" data-slide="prev"><span><h1>&lsaquo;</h1></span></a> <a class="right carousel-control" href="#mycarousel" data-slide="next"><span><h1>&rsaquo;</h1></span></a>
					</div>
				</div>
			</div>
		</div>
	</div>

</div>