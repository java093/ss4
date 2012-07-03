<%@page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=7" /></meta>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="../common/heard_inc.jsp"%>
<title>日志管理</title>
<script type="text/javascript">
//路径
baseUrl = "<%= baseUrl%>";
</script>
<script type="text/javascript" src="<%=baseUrl%>/pages/mng/taskdetailtree.js"></script>
<script type="text/javascript">

</script>
</head>
<style>
.tree-file{
	display:inline-block;
	background:url() no-repeat;
	width:0px;
	height:0px;
	vertical-align:middle;
}
.tree-folder{
	display:inline-block;
	background:url() no-repeat;
	width:0px;
	height:0px;
	vertical-align:middle;
}
.tree-folder-open{
	background:url() no-repeat;
}
.icon-start{
	background:url('../../images/start.png') no-repeat;
}
.icon-pause{
	background:url('../../images/Pause.png') no-repeat;
}
.icon-stop{
	background:url('../../images/stop.png') no-repeat;
}
.maintopRight{
	padding-right:10px;
}
a{TEXT-DECORATION:none}
</style>
<body>
<form method="get">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td>
					<div id = "divid" class="tbar">
					    <a id="start" href="#" class="easyui-linkbutton" onclick="TaskStart();" icon="icon-start" plain="true">启动</a>
<!-- 						<a id ="pause" href="#" class="easyui-linkbutton" onclick="TaskPause();" icon="icon-pause" plain="true">暂停</a> -->
<!-- 						<a id ="stop" href="#" class="easyui-linkbutton" onclick="TaskStop();" icon="icon-stop" plain="true">停止</a>	 -->
						<!-- <a id="stop" href="#" class="easyui-linkbutton" onclick="Refresh();" icon="icon-reload" plain="true">刷新</a>	 -->
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="grid">
						<table border="0" cellpadding="0" cellspacing="0" width="100%"
							height="100%">
							<tr style="height: 100%; width: 100%">
								<td width="80%" height="100%">
									<div id = "taskdetailtree" class="easyui-panel" style=""></div>
								</td>
								<td width="20%" height="100%">
									<div id = "" title = "详    情" class="easyui-panel" style="">
									<textarea name="inputDetail"
										style="max-height: 460px;min-height: 441px; *min-height: 422px"
										readonly="readonly" id="inputDetail" ></textarea>
									</div>
								</td>
							</tr>
						</table>
					</div>  
				</td>
			</tr>
		</table>
	</form>
</body>
</html>

