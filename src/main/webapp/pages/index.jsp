<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="../pages/common/heard_inc.jsp"%>
<title>数据中心管理平台</title>
<script type="text/javascript">
//路径
baseUrl = "<%=baseUrl%>";
</script>
<script type="text/javascript"
	src="<%=baseUrl%>/script/easyui/excanvas.min.js"></script>	
 <script type="text/javascript"
	src="<%=baseUrl%>/script/easyui/jquery.flot.js"></script>
<script type="text/javascript"
	src="<%=baseUrl%>/script/easyui/jquery.flot.threshold.js"></script>
<script type="text/javascript" src="<%=baseUrl%>/pages/index.js"></script>
<script type="text/javascript" src="<%=baseUrl%>/pages/menu.js"></script>
<script type="text/javascript" src="<%=baseUrl%>/pages/main.js"></script>

</head>
<style>
.datagrid-body .tree-folder{
	display:inline-block;
	background:url() no-repeat;
	width:0px;
	height:0px;
	vertical-align:middle;
}
.datagrid-body .tree-folder-open{
	background:url() no-repeat;
}
.datagrid-body .tree-file{
	display:inline-block;
	background:url() no-repeat;
	width:0px;
	height:0px;
	vertical-align:middle;
}
.icon-start{
	background:url('../images/start.png') no-repeat;
}
.icon-pause{
	background:url('../images/Pause.png') no-repeat;
}
.icon-stop{
	background:url('../images/stop.png') no-repeat;
}
.maintopRight {
	padding-right: 10px;
}

a {
	TEXT-DECORATION: none
}
body {
     overflow-x : hidden;   去掉横条
     overflow-y : hidden;   去掉竖条
} 
</style>
<script>
	
	/**
	 * 判断tab页是否存在
	 * @tt tabsPanel
	 */
	function isTabExist(id) {
		// 定义所有tab panel的id集合
		var arr = new Array();
		var pp = $('#tabs').tabs('tabs');
		for ( var i = 0; i < pp.length; i++) {
			var p = pp[i].panel('options');
			if (p.id) {
				arr.push(p.id);
			} else {
				// 首页的场合
				arr.push("0");
			}
		}
		// 判断新增的tab的id是否在arr集合中
		for ( var j = 0; j < arr.length; j++) {
			if (arr[j] == id) {
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
	function addTab(title, url) {
		var tt = $('#tabs');
		if (tt.tabs('exists', title)) {//如果tab已经存在,则选中并刷新该tab
			tt.tabs('select', title);
		} else {
			if (url) {
				var content = '<iframe scrolling="auto" frameborder="0" src="'
						+ url + '" style="width:100%;height:100%;"></iframe>';
			} else {
				var content = '未实现';
			}
			tt.tabs('add', {
				title : title,
				closable : true,
				content : content
			});
		}
		var cfg = {
			tabTitle : title,
			url : url
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
	<div region="north" border="true" style="height:67px;background: url('<%=baseUrl%>/images/bg.jpg');">
		<table width="100%" cellspacing="0" cellpadding="0" border="0"
			scroll="no">
			<tr>
				<td width="60%" align="left"><img src="../images/title.jpg"></td>
				<td valign="bottom" align="right" style="padding-bottom: 3px;">
				
			<shiro:user>
				<shiro:principal property="name" />：欢迎您！
			</shiro:user>
				<a
					href="javascript:void(0)" class="easyui-linkbutton"
					onclick="changePwd()">修改密码</a>&nbsp;<a href="<%=baseUrl%>/logout"
					class="easyui-linkbutton">退出登录</a></label></td>
			</tr>
		</table>
	</div>
	
	<div region="west" split="false" title="功能菜单"
		style="width: 160px; padding1: 1px; overflow: hidden;">
		<div class="easyui-accordion" fit="true" border="false" id="west-id">
			
		</div>
	</div>
	
	<div region="center" style="overflow: hidden;">
		<!-- 修改密码用 -->
		<div id="win" iconCls="icon-save" title="修改密码" closed="true">
			<form method="get" name="updateAcctUserForm" id="updateAcctUserForm">
				<table cellpadding="0" cellspacing="0" border="0" class="form_table">
					<tr>
						<td>
							<div class="tbar">
								<a href="#" class="easyui-linkbutton" icon="icon-save"
									onclick="updateCurrentUserPwd();" plain="true">保存</a> <a
									href="#" class="easyui-linkbutton" icon="icon-redo"
									onclick="closeWin();" plain="true">取消</a>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<table cellpadding="0" cellspacing="0" border="0" id="uiform"
								class="form_table">
								<tr>
									<td width="25%" class="form_title">密码：</td>
									<td width="25%" class="form_content"><input id="password"
										name="password"></input></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</form>
		</div>		
		<div class="easyui-tabs" fit="true" border="false" id="tabs">
			<div title="监控首页" style="overflow:hidden">
				<!--  
				<div id="divid2" class="tbar" style="width: 100%;height:25px">
					<div style="width:20%;height:100%;float: left;">
						<p>最近24小时任务状态</p>
					</div>
					<div style="width:80%;height:100%;float: left;">
						<p>最近一个月总任务进行时间</p>
					</div>
				</div>
				<div id="up" style="width:100%;height:160px">
					<div id="left" style="width:20%;height:140px;float: left;">
						<table id="task_state"  border="0" cellpadding="5px" cellspacing="0" width="100%" height="100%">
							<tr>
								<th align="left">状态</th>
								<th align="left">数量</th>
							</tr>
							<tr>
								<td>总计</td>
								<td id="total" ></td>
							</tr>
							<tr>
								<td>正在运行</td>
								<td id="running"></td>
							</tr>
							<tr>
								<td>成功</td>
								<td id="success"></td>
							</tr>
							<tr>
								<td>失败</td>
								<td id="fail"></td>
							</tr>
							<tr>
								<td>其他</td>
								<td id="other"></td>
							</tr>
						</table>
					</div>
					<div id="right" style="width:80%;height:140px; overflow:hidden" >
						<div id="placeholder"
							style="width: 90%; height: 120px; margin-top:15px;margin-left: 10px "></div>
					</div>
				</div>
				-->
				
				<div id="up">
					<table border="0" width="100%" height="100%">
						<tr>
							<td width="20%" height="100%">
								<div id="left" class="easyui-panel" title="最近24小时任务状态" style=" height:177px ">
									<table id="task_state"  border="0" cellpadding="5px" cellspacing="0" width="100%" height="100%">
									<tr>
										<th align="left">状态</th>
										<th align="left">数量</th>
									</tr>
									<tr>
										<td>总计</td>
										<td id="total" ></td>
									</tr>
									<tr>
										<td>正在运行</td>
										<td id="running"></td>
									</tr>
									<tr>
										<td>成功</td>
										<td id="success"></td>
									</tr>
									<tr>
										<td>失败</td>
										<td id="fail"></td>
									</tr>
									<tr>
										<td>其他</td>
										<td id="other"></td>
									</tr>
									</table> 
								</div>
							</td>
							<td width="80%" height="100%">
								<div id="right" class="easyui-panel" title="最近一个月总任务进行时间" style="height:177px;overflow:hidden" >
									<div id="placeholder"
										style="width: 90%; height: 120px; margin-top:15px;margin-left: 10px "></div>
								</div>
							</td>
							
						</tr>
					</table>
				</div>
				
				<div id="down" style="width:100%;height:70%;">
			    	<!-- Tool bar -->
				    <div style="width:100%;height:32px;overflow: hidden">
					    <table border="0" cellpadding="0" cellspacing="0" width="100%"> 
						  <tr>
						    <td width="70%" valign="middle">
								<div id="divid" class="tbar">
							        <a id="start" href="#" class="easyui-linkbutton"
																	onclick="TaskStart();" icon="icon-start" plain="true">启动</a>
<!-- 									<a id="pause" href="#" class="easyui-linkbutton" -->
<!-- 																	onclick="TaskPause();" icon="icon-pause" plain="true" disabled="true" name="pause">暂停</a> -->
<!-- 								    <a id="stop" href="#" class="easyui-linkbutton" -->
<!-- 																	onclick="TaskStop();" icon="icon-stop" plain="true" disabled="true" name="stop">停止</a> -->
							        <a id="stop" href="#" class="easyui-linkbutton"
																	onclick="Refresh();" icon="icon-reload" plain="true">刷新</a>
							</div>
						    </td>
						     <td align="right" valign="middle" width="30%">
						     <div id="divid" class="tbar">
							              自动刷新：<input type="checkbox"
								 name="autorefresh" id="autorefresh"  onclick="autoRefresh();" />
							     <select name="refresh"  id="refresh"  disabled="disabled" style="width:60px;" onclick="setAutoTime();"><!--jinxl 增添修改自动刷新时间选中时间 onclick="setAutoTime();-->
										<option value="1" >30秒</option>
										<option value="2" >60秒</option>
										<option value="3">90秒</option>
										<option value="4">120秒</option>	
										<option value="5">150秒</option>
										<option value="6">180秒</option>	
								</select>
							</div>
						    </td>
						    
						  </tr>
						  </table>
					</div>
					<!-- data grid -->
			      	<div class="grid" style="width: 100%;height: 100%;overflow: auto">
			        	<table id="logList"></table>
			      	</div>   
			</div>
			</div>
		</div>
	</div>
</body>
	
</html>