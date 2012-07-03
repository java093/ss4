<%@page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="../common/heard_inc.jsp"%>
<title>任务管理</title>
<script type="text/javascript">
//路径
baseUrl = "<%=baseUrl%>";
</script>
<script type="text/javascript"
	src="<%=baseUrl%>/pages/sys/informatica_edit.js"></script>
<script type="text/javascript">
	
</script>
</head>

<body>
	<div id="addPrivilege" icon="icon-save"
		style="padding: 5px; width: 100%; height: 300px;">
		<form method="get" name="editInformatica" id="editInformatica">

			<tr>
				<td colspan="3" align="center">
					<div class="tbar">
						<a href="#" class="easyui-linkbutton" icon="icon-ok"
							onclick="testConnection();" plain="true">测试连接</a> <a href="#"
							class="easyui-linkbutton" icon="icon-save"
							onclick="editInformatic();" plain="true" disabled="true"
							id="save" name="save">保存</a> <a href="#"
							class="easyui-linkbutton" icon="icon-redo" onclick="closeAdd();"
							plain="true">取消</a>
					</div>
				</td>
			</tr>
			<table cellpadding="0" cellspacing="0" border="0" id="uiform"
				class="form_table">
				<tr>
					<td width="25%" class="form_title">服务器名称：</td>
					<td width="50%" class="form_content"><input name="server_name"
						type="text" style="width: 140px;"></input><font color="red">
							*</font></td>
				</tr>
				<tr>
					<td width="25%" class="form_title">IP地址：</td>
					<td width="50%" class="form_content"><input name="ip"
						type="text" style="width: 140px;" onchange="savedisabled()"></input><font color="red"> *</font></td>
				</tr>
				<tr>
					<td width="25%" class="form_title">端口：</td>
					<td width="50%" class="form_content"><input name="port"
						type="text" style="width: 140px;" onchange="savedisabled()"></input><font color="red">
							*</font></td>
				</tr>
				<tr>
					<td width="25%" class="form_title">Domain：</td>
					<td width="50%" class="form_content"><input name="domain"
						type="text" style="width: 140px;" onchange="savedisabled()"></input><font color="red">
							*</font></td>
				</tr>
				<tr>
					<td width="25%" class="form_title">知识库：</td>
					<td width="50%" class="form_content"><select
						id="knowledge_base" name="knowledge_base" style="width: 145px;">

					</select></td>
				</tr>
				<tr>
					<td width="25%" class="form_title">集成服务器：</td>
					<td width="50%" class="form_content"><input
						name="integration_server" type="text" style="width: 140px;"></input><font color="red">
							*</font></td>
				</tr>
				<tr>
					<td width="25%" class="form_title">服务器用户名：</td>
					<td width="50%" class="form_content"><input name="user_name"
						type="text" style="width: 140px;"  onchange="savedisabled()"></input><font color="red">
							*</font></td>
				</tr>
				<tr>
					<td width="25%" class="form_title">服务器密码：</td>
					<td width="50%" class="form_content"><input name="password"
						type="password" style="width: 140px;"  onchange="savedisabled()"></input><font color="red">
							*</font></td>
				</tr>
				<tr>
					<td width="25%" class="form_title">workflow日志保存路径：</td>
					<td width="50%" class="form_content"><input name="wrlogpath"
						type="text" style="width: 140px;"></input><font color="red">
							*</font></td>
				</tr>
				<tr>
					<td width="25%" class="form_title">session日志保存路径：</td>
					<td width="50%" class="form_content"><input name="sesslogpath"
						type="text" style="width: 140px;"></input><font color="red">
							*</font></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>