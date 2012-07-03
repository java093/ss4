<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*"%> 
<%@ page import="java.text.*"%>
<% 
String datetime=new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()); //获取系统时间 
%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%@include file="../common/heard_inc.jsp"%>
<link rel="stylesheet" type="text/css" href="<%= baseUrl%>/script/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%= baseUrl%>/script/easyui/themes/icon.css"> 

<title>workflow编辑</title>

<script type="text/javascript" src="<%=baseUrl%>/pages/job/workflow_edit.js"></script>
<script type="text/javascript">
//路径
baseUrl = "<%= baseUrl%>";

</script>
</head>

<body style="background:#EFF0F2">
    <form method="get" id="workflowForm" name="workflowForm">
	<table cellpadding="0" cellspacing="0" border="0" class="form_table">
		<tr>
  		<td colspan="2">
			<div class="tbar">
				<a href="#" class="easyui-linkbutton" icon="icon-save" onclick="workflowEditSave();" plain="true">保存</a>
				<a href="#" class="easyui-linkbutton" icon="icon-redo" onclick="closeWin();" plain="true">取消</a>
			</div>
		</td>
  		</tr>
  		<tr>
  			<td>
		  		<table align="center"  cellpadding="0" cellspacing="0" border="0" class="form_table">
		  			<tr>
						<td width="25%" class="form_title">workflow名称：</td>
						<td width="25%" class="form_content"><input  id="WORKFLOW_NAME" name="WORKFLOW_NAME" readonly="readonly" class="easyui-validatebox"></input>
						
						</td>
					</tr>
			  		<tr>
						<td width="25%" class="form_title">workflow别名：</td>
						<td width="25%" class="form_content"><input  type="text" id="ALIAS" name="ALIAS" class="easyui-validatebox"></input></td>
					</tr>
					<tr>
						<td width="25%" class="form_title">说明：</td>
						<td width="25%"  class="form_content">
						<textarea  id="EXPLAIN"  name="EXPLAIN" style="width:340px;height:90px;border:1px solid #7F9DB9;background-color:#FFFFFF;voerflow-x:true; " ></textarea> 
						</td>
					</tr>
				
		  		</table>
		  	</td>
		</tr>
		

	</table>
 	</form>
</body>
</html>