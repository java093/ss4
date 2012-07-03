<%@page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
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
<title>人员管理</title>
<script type="text/javascript" src="<%=baseUrl%>/pages/acct/acctuser_add.js"></script>
</head>

<body style="background: #EFF0F2">
	<form method="get" name="saveAcctUserForm"
		id="saveAcctUserForm">
		<table cellpadding="0" cellspacing="0" border="0" class="form_table">
			<tr>
				<td>
					<div class="tbar">

						<a href="#" class="easyui-linkbutton" icon="icon-save"
							onclick="saveAcctUser();" plain="true">保存</a> 
						<a href="#" class="easyui-linkbutton" icon="icon-redo" 
							onclick="closeAdd();" plain="true">取消</a>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<table cellpadding="0" cellspacing="0" border="0" id="uiform"
						class="form_table">
							<td width="25%" class="form_title">登录名：</td>
							<td width="25%" class="form_content"><input type="text"
								id="login_name" name="login_name"></input></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">姓名：</td>
							<td width="25%" class="form_content"><input type="text"
								id="name" name="name"></input></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">密码：</td>
							<td width="25%" class="form_content"><input type="text" id="password" name="password"></input></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>	
</body>
</html>