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
<script type="text/javascript" src="<%=baseUrl%>/pages/mng/log.js"></script>
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
		    <a href="#" class="easyui-linkbutton" onclick="taskEvent('1');" icon="icon-add" plain="true">启动</a>
			<a id = "" href="#" class="easyui-linkbutton" onclick="taskEvent('2');" icon="icon-edit" plain="true">暂停</a>
			<a id = "" href="#" class="easyui-linkbutton" onclick="taskEvent('3');" icon="icon-cancel" plain="true">停止</a>		
		</div>
    </td>
  </tr>
  <tr>
    <td class="search">	
	   <table border="0" cellpadding="0" cellspacing="0" width="30%">	
		<tr>
			<td nowrap="nowrap" width="99%" align="left" >
				<label>任务名称:</label>
				<input type="text" id="txtTaskName" width="80"  />			
				<label>任务状态:</label>
				<select id="selectTaskState" name="selectTaskState" width="80" >
				<option value="1">Succeeded</option>
				<option value="2">Disabled</option>
				<option value="3">Failed</option>		
				<option value="4">Stopped</option>	
				<option value="5">Aborted</option>	
				<option value="6">Running</option>		
				</select>
				
				<label>类&nbsp;&nbsp;别:</label>
				<select id="selectTaskType" name="selectTaskType" width="80"  >
				<option value="0">主任务</option>
				<option value="1">子任务</option>					
				</select>	
		 	</td>
		 	
		</tr>
		<tr>
		<td  nowrap="nowrap" width="99%" align="left" >
		 		<label>开始时间:</label>					
				<input readonly="readonly" class="Wdate"  id="txtStartTime" name="txtStartTime" onFocus="WdatePicker()" />
				<label>结束时间:</label>
				<input readonly="readonly" class="Wdate"  id="txtStopTime" name="txtStopTime" onFocus="WdatePicker()" />
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

