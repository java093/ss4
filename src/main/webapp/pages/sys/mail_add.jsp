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
<title>邮件添加</title>

<script type="text/javascript" src="<%=baseUrl%>/pages/sys/mail_add.js"></script>
<script type="text/javascript">
//路径
baseUrl = "<%= baseUrl%>";
</script>
</head>

<body style="background:#EFF0F2">
 	<form method="post" id="insertForm">
	<table  cellpadding="0" cellspacing="0" border="0" class="form_table">
		<tr>
	  		<td>
	 			<div class="tbar">
					<a href="#" class="easyui-linkbutton" icon="icon-save" onclick="mailAdd();" plain="true">保存</a>
					<a href="#" class="easyui-linkbutton" icon="icon-redo" onclick="closeWin();" plain="true">取消</a>
				</div>
			</td>
  		</tr>
  		<tr>
  			<td>
		  		<table  cellpadding="0" cellspacing="0" border="0" class="form_table">
					<tr>
						<td width="25%" class="form_title">主机名：</td>
						<td width="25%"  class="form_content"><input type="text" id="hostName" name="hostName"></input><font color="red"> *</font></td>
					</tr>
					<tr>
						<td width="25%" class="form_title">用户名：</td>
						<td width="25%"  class="form_content"><input type="text" id="userName" name="userName"></input><font color="red"> *</font></td>
					</tr>
					<tr>
						<td width="25%" class="form_title">SMTP端口：</td>
						<td width="25%"  class="form_content"><input id="smtpPort"
								 type="text" name="smtpPort"></input><font color="red"> *</font></td>
					</tr>
					<tr>
						<td width="25%" class="form_title">密码：</td>
						<td width="25%"  class="form_content"><input id="PASSWORD" name="PASSWORD" type="password"></input><font color="red"> *</font></td>
					</tr>
					<tr>
						<td width="25%" class="form_title">确认密码：</td>
						<td width="25%"  class="form_content"><input id="repassword" name="repassword" type="password"></input><font color="red"> *</font></td>
					</tr>
		  		</table>
  			</td>
  		</tr>
	</table>
 	</form>
</body>
</html>