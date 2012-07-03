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
	src="<%=baseUrl%>/pages/sys/DataBase_insert.js"></script>
<script type="text/javascript">
	
</script>
</head>

<body>
	<div id="addPrivilege" icon="icon-save"
		style="padding: 5px; width: 100%; height: 300px;">
		<form method="get" name="addDataBase" id="addDataBase">
			<tr>
				<td colspan="3" align="center">
					<div class="tbar">
						<a href="#" class="easyui-linkbutton" icon="icon-ok"
							onclick="testConnection();" plain="true">测试连接</a> <a href="#"
							class="easyui-linkbutton" icon="icon-save" onclick="addDB();"
							plain="true" disabled="true" id="save" name="save">保存</a> <a
							href="#" class="easyui-linkbutton" icon="icon-redo"
							onclick="closeAdd();" plain="true">取消</a>
					</div>
				</td>
			</tr>

			<table cellpadding="0" cellspacing="0" border="0" id="uiform"
				class="form_table">
				<tr>
					<td width="25%" class="form_title">别名：</td>
					<td width="50%" class="form_content"><input name="title"
						type="text" style="width: 140px;"></input><font color="red">
							*</font></td>
				</tr>
				<tr>
					<td width="25%" class="form_title">知识库名称：</td>
					<td width="50%" class="form_content"><input name="dbname"
						type="text" style="width: 140px;"></input><font color="red">
							*</font></td>
				</tr>
				<tr>
					<td width="25%" class="form_title">数据库名称：</td>
					<td width="50%" class="form_content"><input name="server_name"
						type="text" style="width: 140px;" onchange="savedisabled()"></input><font color="red">
							*</font></td>
				</tr>
				<tr>
					<td width="25%" class="form_title">数据库类型：</td>
					<td width="50%" class="form_content"><input name="dbtype"
						readonly="readonly" value="jdbc:oracle:thin" type="text"
						style="width: 140px;"><font color="red"> *</font></td>
				</tr>
				<tr>
					<td width="25%" class="form_title">数据库用户名：</td>
					<td width="50%" class="form_content"><input name="username"
						type="text" style="width: 140px;" onchange="savedisabled()"></input><font color="red">
							*</font></td>
				</tr>
				<tr>
					<td width="25%" class="form_title">数据库密码：</td>
					<td width="50%" class="form_content"><input name="password"
						type="password" style="width: 140px;" onchange="savedisabled()"></input><font color="red">
							*</font></td>
				</tr>
				<tr>
					<td width="25%" class="form_title">端口号：</td>
					<td width="50%" class="form_content"><input name="port"
						type="text" style="width: 140px;" onchange="savedisabled()"></input><font color="red">
							*</font></td>
				</tr>
				<tr>
					<td width="25%" class="form_title">数据库IP：</td>
					<td width="50%" class="form_content"><input name="ip"
						type="text" style="width: 140px;" onchange="savedisabled()"></input><font color="red">
							*</font></td>
				</tr>
				<tr>
					<td width="25%" class="form_title">数据库使用：</td>
					<td width="50%" class="form_content">
						<!-- <input class="inputbox" type="text" id="infomatic_id" name="infomatic_id" /> -->
						<select id="dbuse" name="dbuse" style="width: 145px;">
							<option value="0">Informatica</option>
							<option value="1">统一字典</option>
					</select><font color="red"> *</font>
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>