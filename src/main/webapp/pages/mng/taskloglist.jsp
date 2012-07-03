<%@page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="../common/heard_inc.jsp"%>
<title>日志管理</title>
<script type="text/javascript">
//路径
baseUrl = "<%= baseUrl%>";
</script>
<script type="text/javascript" src="<%=baseUrl%>/pages/mng/tasklog.js"></script>
<script type="text/javascript">

</script>
</head>
<style>
.tree-file{
	display:inline-block;
	background:url() no-repeat;
	width:16px;
	height:18px;
	vertical-align:middle;
}
.tree-folder{
	display:inline-block;
	background:url() no-repeat;
	width:16px;
	height:18px;
	vertical-align:middle;
}
.tree-folder-open{
	background:url() no-repeat;
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
.maintopRight{
	padding-right:10px;
}
a{TEXT-DECORATION:none}
</style>
<body>
<form method="get">
<table border="0" cellpadding="0" cellspacing="0" width="100%"> 
  <!-- <tr>
    <td>
		<div id = "divid" class="tbar">
		    <a id="start" href="#" class="easyui-linkbutton" onclick="TaskStart();" icon="icon-start" plain="true">启动</a>
			<a id ="pause" href="#" class="easyui-linkbutton" onclick="TaskPause();" icon="icon-pause" plain="true">暂停</a>
			<a id ="stop" href="#" class="easyui-linkbutton" onclick="TaskStop();" icon="icon-stop" plain="true">停止</a>	
			<a id="stop" href="#" class="easyui-linkbutton" onclick="Refresh();" icon="icon-reload" plain="true">刷新</a>	
		</div>
    </td>
  </tr> -->
  <tr>
    <td class="search">	
	   <table border="0" cellpadding="0" cellspacing="0" width="30%">	
		<tr>
			<td nowrap="nowrap" width="99%" align="left" >
				<label>任务名称:</label>
				<input type="text" id="txtTaskName" width="80"  />			
				<label>任务状态:</label>
				<select id="selectTaskState" name="selectTaskState" width="80" >
				<option value=""></option>
				<option value="1">Succeeded</option>
				<option value="2">Disabled</option>
				<option value="3">Failed</option>		
				<option value="4">Stopped</option>	
				<option value="5">Aborted</option>	
				<option value="6">Running</option>		
				</select>				
		 	
		 		<label>开始时间:</label>					
				<input readonly="readonly" class="Wdate"  id="txtStartTime" name="txtStartTime" onFocus="WdatePicker()"  style="cursor:pointer"/>
				<label>结束时间:</label>
				<input readonly="readonly" class="Wdate"  id="txtStopTime" name="txtStopTime" onFocus="WdatePicker()"  style="cursor:pointer"/>
				<a href="#" class="easyui-linkbutton" onclick="slectTaskByCondition();" icon="icon-search" plain="true">查询</a>
		 	</td>
		</tr>
	 </table>
</td>
</tr>
  <tr>
    <td>
      <div class="grid">
        <table id="taskList"></table>
      </div>   
    </td>
  </tr>
</table>
</form>
</body>
</html>

