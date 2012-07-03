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
<meta http-equiv="X-UA-Compatible" content="IE=7" /></meta>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="../common/heard_inc.jsp"%>
<title>任务管理</title>
<script type="text/javascript">
//路径
baseUrl = "<%= baseUrl%>";

</script>
<script type="text/javascript" src="<%=baseUrl%>/pages/mng/dictentry_edit.js"></script>
<script type="text/javascript">
</script>
</head>

<body style="background: #EFF0F2">
	<form method="get" name="updateDictentryForm" id="updateDictentryForm">
		<table cellpadding="0" cellspacing="0" border="0" class="form_table">
			<tr>
				<td>
					<div class="tbar">

						<a href="#" class="easyui-linkbutton" icon="icon-save"
							onclick="editDictentry();" plain="true">保存</a> 
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
							<td width="25%" class="form_title">字典条目：</td>
							<td width="25%" class="form_content"><input type="text"
								id="dict_entry" name="dict_entry" style="width: 140px;"></input><font color="red"> *</font></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">可否增加子项：</td>
							<td width="25%" class="form_content"><select
								id="add_enable" name="add_enable" style="width: 144px;*width:146px;">
									<option value="1">是</option>
									<option value="0">否</option>
								</select><font color="red"> *</font></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">条目名称：</td>
							<td width="25%" class="form_content"><input type="text"
								id="entry_name" name="entry_name" style="width: 140px;"></input><font color="red"> *</font></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">存取级别：</td>
							<td width="25%" class="form_content"><select
								id="access_level" name="access_level" style="width: 144px;*width:146px;">
							</select><font color="red"> *</font></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">字典类型：</td>
							<td width="25%" class="form_content"><select id="dict_type"
								name="dict_type" style="width: 144px;*width:146px;">
							</select><font color="red"> *</font></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">有效标志：</td>
							<td width="25%" class="form_content">
								<select id="defunct_ind" name="defunct_ind"
								style="width: 144px;*width:146px;">
									<option value="Y">有效</option>
									<option value="N">无效</option>
								</select><font color="red"> *</font>
							</td>
						</tr>
						<tr>
							<td width="25%" class="form_title">备注：</td>
							<td width="25%" class="form_content"><input type="text" id="remarks" name="remarks" style="width: 140px;"></input></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>