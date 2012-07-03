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
<title>session管理</title>
<script type="text/javascript">
//路径
baseUrl = "<%= baseUrl%>";

</script>
<script type="text/javascript" src="<%=baseUrl%>/pages/mng/session_edit.js"></script>
<script type="text/javascript">
</script>
</head>

<body style="background: #EFF0F2">
	<form method="get" name="updateSessionForm" id="updateSessionForm">
		<table cellpadding="0" cellspacing="0" border="0" class="form_table">
			<tr>
				<td>
					<div class="tbar">

						<a href="#" class="easyui-linkbutton" icon="icon-save"
							onclick="editSession();" plain="true">保存</a> 
						<a href="#" class="easyui-linkbutton" icon="icon-redo" 
							onclick="closeWin();" plain="true">取消</a>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<table cellpadding="0" cellspacing="0" border="0" id="uiform"
						class="form_table">
						<tr>
							<td width="25%" class="form_title">文件夹名称：</td>
							<td width="25%" class="form_content"><input
								readonly="readonly" name="subj_name"></input></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">文件夹ID：</td>
							<td width="25%" class="form_content"><input
								readonly="readonly" name="subj_id"></input></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">会话名称：</td>
							<td width="25%" class="form_content"><input
								readonly="readonly" name="session_name"></input></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">会话ID：</td>
							<td width="25%" class="form_content"><input
								readonly="readonly" name="session_id"></input></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">工作流名称：</td>
							<td width="25%" class="form_content"><input
								readonly="readonly" name="workflow_name"></input></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">工作流ID：</td>
							<td width="25%" class="form_content"><input
								readonly="readonly" name="workflow_id"></input></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">映射名称：</td>
							<td width="25%" class="form_content"><input
								readonly="readonly" name="mapping_name"></input></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">映射ID：</td>
							<td width="25%" class="form_content"><input
								readonly="readonly" name="mapping_id"></input></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">检查层次：</td>
							<td width="25%" class="form_content"><input
								name="check_level"></input></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">检查层次说明：</td>
							<td width="25%" class="form_content"><input
								name="check_level_desc"></input></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">检查类型：</td>
							<td width="25%" class="form_content"><input
								name="check_type"></input></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">检查类型说明：</td>
							<td width="25%" class="form_content"><input
								name="check_type_desc"></input></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">标识符：</td>
							<td width="25%" class="form_content"><input
								name="default_ind"></input></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>