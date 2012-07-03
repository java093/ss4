<%@page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="../common/heard_inc.jsp"%>
<title>菜单管理</title>
<script type="text/javascript" src="<%=baseUrl%>/pages/acct/acctmenu_add.js"></script>
</head>

<body style="background: #EFF0F2">
	<form method="get" name="saveAcctMenuForm"
		id="saveAcctMenuForm">
		<table cellpadding="0" cellspacing="0" border="0" class="form_table">
			<tr>
				<td>
					<div class="tbar">

						<a href="#" class="easyui-linkbutton" icon="icon-save"
							onclick="saveAcctMenu();" plain="true">保存</a> 
						<a href="#" class="easyui-linkbutton" icon="icon-redo" 
							onclick="closeAdd();" plain="true">取消</a>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<table cellpadding="0" cellspacing="0" border="0" id="uiform"
						class="form_table">
						<tr>
							<td width="25%" class="form_title"> 菜单名称：</td>
							<td width="25%" class="form_content"><input id="name" name="name" ></input></td>
						</tr>
							<td width="25%" class="form_title">菜单url：</td>
							<td width="25%" class="form_content"><input
								id="func_url" name="func_url"></input></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">编码：</td>
							<td width="25%" class="form_content"><input
								id="code" name="code"></input></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>	
</body>
</html>