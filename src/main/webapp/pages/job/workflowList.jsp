<%@page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="../common/heard_inc.jsp"%>
<title>workflow管理</title>
<script type="text/javascript">
//路径
baseUrl = "<%= baseUrl%>";
</script>
<script type="text/javascript" src="<%=baseUrl%>/pages/job/workflow.js"></script>
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
			<a id = "" href="#" class="easyui-linkbutton" onclick="importButton();" icon="icon-save" plain="true">导入</a>
			<a href="#" class="easyui-linkbutton" onclick="updateWorkFlow();" icon="icon-edit" plain="true">编辑</a>
			<a id = "" href="#" class="easyui-linkbutton" onclick="deleteWorkFlow();" icon="icon-cancel" plain="true">删除</a>
		</div>
    </td>
  </tr>
  <tr>
    <td class="search">	
	   <table border="0" cellpadding="0" cellspacing="0" width="60%">	
		<tr>
		
			<td nowrap="nowrap" width="8%" align="left" >服务器名称：</td>
			<td nowrap="nowrap" width="12%" align="left">
			<select name="workFlowServer" id="workFlowServerId" style="width:90%" >	
			</select>
		    </td>
		
			<td nowrap="nowrap" width="8%" align="center" >workflow别名：</td>
			<td width="12%" align="left">
			<input id="ALIAS" class="easyui-validatebox" ></input>
		    </td>
		    <td nowrap="nowrap" width="10%" align="left"><a id = "" href="#" class="easyui-linkbutton" onclick="findListByName();" icon="icon-search" plain="true">查询</a></td>
		    
		</tr>
	 </table>
</td>
</tr>
  <tr>
    <td>
      <div class="grid">
        <table id="workflowlist"></table>
      </div>   
    </td>
  </tr>
</table>
</form>
</body>
</html>

