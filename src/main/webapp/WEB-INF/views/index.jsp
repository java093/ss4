<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String baseUrl=request.getContextPath();
 %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据中心管理平台</title>
<link rel="stylesheet" type="text/css" href="<%= baseUrl%>/script/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%= baseUrl%>/script/easyui/themes/icon.css">
<script type="text/javascript" src="<%= baseUrl%>/script/easyui/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%= baseUrl%>/script/easyui/jquery.easyui.min.js"></script>
</head>
<style>
.maintopRight{
	padding-right:10px;
}
a{TEXT-DECORATION:none}
</style>
<script>

/**
 * 判断tab页是否存在
 * @tt tabsPanel
 */
function isTabExist(id){
	// 定义所有tab panel的id集合
	var arr = new Array();
	var pp = $('#tabs').tabs('tabs');
	for (var i=0; i<pp.length; i++){
		var p = pp[i].panel('options');
		if(p.id){
			arr.push(p.id);
		} else {
			// 首页的场合
			arr.push("0");
		}
	}
	// 判断新增的tab的id是否在arr集合中
	for(var j=0; j<arr.length; j++){
		if(arr[j] == id){
			return true;
		}
	}
	return false;
}

/**
 * 新增tab
 * @title : 标题
 * @url : 链接地址
 */
function addTab(title,url){
	var tt = $('#tabs');
	if (tt.tabs('exists',title)) {//如果tab已经存在,则选中并刷新该tab
		tt.tabs('select', title);
	} else {
		if (url) {
			var content = '<iframe scrolling="auto" frameborder="0" src="' + url + '" style="width:100%;height:100%;"></iframe>';
		} else {
			var content = '未实现';
		}
		tt.tabs('add', {
			title :title,
			closable :true,
			content :content
		});
	}
	var cfg = {
		tabTitle :title,
		url :url
	}
	refreshTab(cfg);
}

/**
 * 刷新tab
 * @cfg
 *example: {tabTitle:'tabTitle',url:'refreshUrl'}
 *如果tabTitle为空，则默认刷新当前选中的tab
 *如果url为空，则默认以原来的url进行reload
 */
function refreshTab(cfg) {
	var refresh_tab = cfg.tabTitle ? $('#tabs')
			.tabs('getTab', cfg.tabTitle) : $('#tabs').tabs('getSelected');
	if (refresh_tab && refresh_tab.find('iframe').length > 0) {
		var _refresh_ifram = refresh_tab.find('iframe')[0];
		var refresh_url = cfg.url ? cfg.url : _refresh_ifram.src;
		//_refresh_ifram.src = refresh_url;
		_refresh_ifram.contentWindow.location.href = refresh_url;
	}
}
</script>

<body class="easyui-layout">
	<div region="north" border="true" style="height:67px;background: url('<%= baseUrl%>/images/img/bg.gif');">
		<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td align=left ><img src="<%= baseUrl%>/images/img/title.gif">数据中心管理平台，这里需要配图，及相应的版面设计</td>
				<td align="center">&nbsp;</td>
				<td align="right" class="maintopRight"><img src="<%= baseUrl%>/images/img/logo.gif"></td>
			</tr>
		</table>
	</div>
	<div region="west" split="false" title="功能菜单" style="width:160px;padding1:1px;overflow:hidden;">
		<div class="easyui-accordion" fit="true" border="false">
			<div title="任务管理" selected="true" style="padding:0px;overflow:auto;">
				<ul id="tree" class="easyui-tree" animate="false" dnd="false">
					<li><a href="javascript:void(0)" onclick="addTab('Workflow','<%= baseUrl%>/job/index')">Workflow</a></li>
					<li state="opened">
						<span>任务</span>
						<ul>
							<li><a href="javascript:void(0)" onclick="addTab('子任务','<%= baseUrl%>/task/index')">子任务</a></li>
							<li><a href="javascript:void(0)" onclick="addTab('主任务','<%= baseUrl%>/pages/ts/xmgl/xmch/xmch_List.jsp?funcId=329632')">主任务</a></li>
						</ul>
					</li>
			</div>
			<div title="计划管理" style="padding:10px;overflow:auto">
				<ul id="tree" class="easyui-tree" animate="false" dnd="false">
					<li><a href="javascript:void(0)" onclick="addTab('计划','<%= baseUrl%>/plan/index')">计划管理</a></li>
				</ul>	
			</div>

			<div title="数据质量控制" style="padding:10px">
				<ul id="tree" class="easyui-tree" animate="false" dnd="false">
					<li><a href="javascript:void(0)" onclick="addTab('统一字典管理','<%= baseUrl%>/pages/xn/cxgl/zhcx/zhcx-list.jsp')">统一字典管理</a></li>
					<li><a href="javascript:void(0)" onclick="addTab('数据质量控制','<%= baseUrl%>/pages/xn/cxgl/zhcx/zhcx-list.jsp')">数据质量控制</a></li>
					<li><a href="javascript:void(0)" onclick="addTab('日志管理','<%= baseUrl%>/pages/xn/cxgl/zhcx/zhcx-list.jsp')">日志管理</a></li>
				</ul>	
			</div>
			<div title="系统管理" style="padding:10px">
				<ul id="tree" class="easyui-tree" animate="false" dnd="false">
					<li state="opened">
						<span>系统参数</span>
						<ul>
							<li state="opened">
								<span>ETL配置</span>
								<ul>
									<li><a href="javascript:void(0)" onclick="addTab('Informatica','<%= baseUrl%>/sys/index')">Informatica</a></li>
								</ul>
							</li>
						</ul>
					</li>
					<li><a href="javascript:void(0)" onclick="addTab('数据库配置','<%= baseUrl%>/pages/xn/cxgl/zhcx/zhcx-list.jsp')">数据库配置</a></li>
					<li><a href="javascript:void(0)" onclick="addTab('邮件配置','<%= baseUrl%>/mail/index')">邮件配置</a></li>
					<li state="opened">
						<span>系统权限管理</span>
						<ul>
							<li><a href="javascript:void(0)" onclick="addTab('系统权限管理','<%= baseUrl%>/pages/xn/cxgl/zhcx/zhcx-list.jsp')">系统权限管理</a></li>
						</ul>
					</li>
				</ul>
			</div>
		</div>
	</div>
	<div region="center" style="overflow:hidden;">
		<div class="easyui-tabs" fit="true" border="false" id="tabs">
			<div title="监控首页" style="padding:20px;overflow:hidden;">
				<div style="margin-top:20px;">
					<h1>最近24小时监控信息</h1>
				</div>
			</div>
		</div>
	</div>
</body>
</html>