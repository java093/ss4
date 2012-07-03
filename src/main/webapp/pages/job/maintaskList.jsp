<%@page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="../common/heard_inc.jsp"%>
<title>任务管理</title>
<script type="text/javascript">
//路径
baseUrl = "<%= baseUrl%>";
</script>
<script type="text/javascript" src="<%=baseUrl%>/pages/job/maintask.js"></script>
<script type="text/javascript">
</script>
</head>
<style>
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
		    <a href="#" class="easyui-linkbutton" onclick="taskadd();" icon="icon-add" plain="true">新增</a>
			<a href="#" class="easyui-linkbutton" onclick="taskupdate();" icon="icon-edit" plain="true">编辑</a>
			<a href="#" class="easyui-linkbutton" onclick="taskview();" icon="icon-updates" plain="true">查看</a>
			<a id = "" href="#" class="easyui-linkbutton" onclick="deleteTasks();" icon="icon-cancel" plain="true">删除</a>
		
		</div>
    </td>
  </tr>
  <tr>
    <td class="search">	
		<table border="0" cellpadding="0" cellspacing="0" width="50%">	
		<tr height="30">
			<td nowrap="nowrap" width="5%" align="left">运行方式：</td>
			<td nowrap="nowrap" width="10%" align="left">		
			<select name="runtype"  id="runtype" style="width:90px;">
			<option value="" >请选择</option>
			<option value="0" >串行</option>
			<option value="1">并行</option>	
			</select>
			</td>
			<td nowrap="nowrap" width="5%" align="left">任务名称：
			</td>
			<td nowrap="nowrap" width="10%" align="left">
			<input style="width:100px;" type="text" id="TASKNAME" name="TASKNAME"/>
			</td>
			<td width="10%" align="left">
				<a id = "" href="#" class="easyui-linkbutton" onclick="searchTASK();" icon="icon-search" plain="true">查询</a>
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

