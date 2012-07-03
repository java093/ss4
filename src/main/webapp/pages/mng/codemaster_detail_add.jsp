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
<title>codemastert_detail添加</title>
<script type="text/javascript">
//路径
baseUrl = "<%= baseUrl%>";

</script>
<script type="text/javascript" src="<%=baseUrl%>/pages/mng/codemaster_detail_add.js"></script>
<script type="text/javascript">
</script>
</head>

<body style="background:#EFF0F2">
		<form method="get" name="addCodeMasterDetailForm"
			id="addCodeMasterDetailForm">
			<table  cellpadding="0" cellspacing="0" border="0" class="form_table">
			<tr>
				<td>
					<div class="tbar">

							<a href="#" class="easyui-linkbutton" icon="icon-save" onclick="addCodeMasterDetail();" plain="true">保存</a> 
							<a href="#" class="easyui-linkbutton" icon="icon-redo" onclick="closeAdd();" plain="true">取消</a>
					</div>
				</td>
			</tr>
			<tr>
  				<td>
			<table cellpadding="0" cellspacing="0" border="0" id="uiform" class="form_table">
				<tr>
					<td width="25%" class="form_title">类型：</td>
					<td width="25%"  class="form_content">
						<select id="codetype" name="codetype" style="width: 144px;*width:146px;">
						</select><font color="red"> *</font>
					</td>
				</tr>
				<tr>
					<td width="25%" class="form_title">代码：</td>
					<td width="25%"  class="form_content"><input type="text" id="code" name="code" style="width: 140px;"></input><font color="red"> *</font></td>
				</tr>
				<tr>
					<td width="25%" class="form_title">代码值：</td>
					<td width="25%"  class="form_content"><input type="text" id="codevalue" name="codevalue" style="width: 140px;"></input></td>
				</tr>
				<tr>
					<td width="25%" class="form_title">描述：</td>
					<td width="25%"  class="form_content"><input type="text" id="description" name="description" style="width: 140px;"></input><font color="red"> *</font></td>
				</tr>
				<tr>
					<td width="25%" class="form_title">有效标记：</td>
					<td width="25%"  class="form_content">
						<select id="delflag" name="delflag"
						style="width: 144px;*width:146px;">
							<option value="0">有效</option>
							<option value="1">无效</option>
						</select><font color="red"> *</font>
					</td>
				</tr>
			</table>
				</td>
			</tr>
		</table>
		</form>
</body>
</html>