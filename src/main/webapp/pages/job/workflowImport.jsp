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
<script type="text/javascript" src="<%=baseUrl%>/pages/job/workflowImport.js"></script>
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
    <div class="tbar">
	<a href="#" class="easyui-linkbutton" onclick="importToData();" icon="icon-ok" plain="true">导入</a>
	<a href="#" class="easyui-linkbutton" onclick="closeWin();" icon="icon-undo" plain="true">取消</a>
    </div>	
    </td>
  </tr>
  <tr>
    <td class="search">	
	   <table border="0" cellpadding="0" cellspacing="0" width="30%">	
		<tr>
			<td nowrap="nowrap" width="" align="right" >服务器名称：</td>
			<td width="100" align="left">
			<select name="workFlowServer" id="workFlowServerId" style="width:100px;" >	
			</select>
		    </td>
		    
		    <td nowrap="nowrap" ><a id = "" href="#" class="easyui-linkbutton" onclick="confirmWorkFlow_Two();" icon="icon-search" plain="true">查询</a></td>
		   <!--  <td nowrap="nowrap"  width="100"><a id = "" href="#" class="easyui-linkbutton" onclick="importToData();" icon="icon-save" plain="true">导入</a></td>  -->
		    
		</tr>
	 </table>
</td>
</tr>
  <tr>
    <td>
      <div class="grid">
        <table id="workFlowImportList"></table>
      </div>   
    </td>
  </tr>
</table>
</form>
</body>
</html>
