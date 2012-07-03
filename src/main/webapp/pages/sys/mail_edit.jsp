<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%
	String datetime = new SimpleDateFormat("yyyy-MM-dd")
			.format(Calendar.getInstance().getTime()); //获取系统时间
%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%@include file="../common/heard_inc.jsp"%>
<%-- <link rel="stylesheet" type="text/css" href="<%= baseUrl%>/script/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%= baseUrl%>/script/easyui/themes/icon.css"> 
<script type="text/javascript" src="<%=baseUrl%>/script/ajaxfileupload.js"></script>
<script type="text/javascript" src="<%= baseUrl%>/script/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%= baseUrl%>/script/easyui/js/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%= baseUrl%>/script/easyui/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%= baseUrl%>/script/easyui/validateTool.js"></script> --%>
<title>邮件配置编辑</title>

<script type="text/javascript" src="<%=baseUrl%>/pages/sys/mail_edit.js"></script>
<script type="text/javascript">
//路径
baseUrl = "<%=baseUrl%>";
</script>
</head>

<body style="background: #EFF0F2">
	<form method="get" id="updateForm" name="updateForm">
		<table cellpadding="0" cellspacing="0" border="0" class="form_table">
			<tr>
				<td>
					<div class="tbar">
						<a href="#" class="easyui-linkbutton" icon="icon-save"
							onclick="mailEditSave();" plain="true">保存</a> <a href="#"
							class="easyui-linkbutton" icon="icon-redo" onclick="closeWin();"
							plain="true">取消</a>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<table align="center" cellpadding="0" cellspacing="0" border="0"
						class="form_table">
						<tr>
							<td width="25%" class="form_title">主机名：</td>
							<td width="25%" class="form_content"><input id="HOST"
								type="text" name="HOST"></input><font color="red"> *</font></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">用户名：</td>
							<td width="25%" class="form_content"><input id="USERNAME"
								type="text" name="USERNAME"></input><font color="red"> *</font></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">SMTP端口：</td>
							<td width="25%" class="form_content"><input id="PORT"
								type="text" name="PORT"></input><font color="red"> *</font></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">密码：</td>
							<td width="25%" class="form_content"><input id="PASSWORD"
								type="password" name="PASSWORD"></input><font color="red"> *</font></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">确认密码：</td>
							<td width="25%" class="form_content"><input id="repassword"
								type="password" name="repassword"></input><font color="red"> *</font></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>