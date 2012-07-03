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
<title>con_tran_dict管理</title>
<script type="text/javascript">
//路径
baseUrl = "<%= baseUrl%>";

</script>
<script type="text/javascript" src="<%=baseUrl%>/pages/mng/con_tran_dict_edit.js"></script>
<script type="text/javascript">
</script>
</head>

<body style="background: #EFF0F2">
	<form method="get" name="updateConForm" id="updateConForm">
		<table cellpadding="0" cellspacing="0" border="0" class="form_table">
			<tr>
				<td>
					<div class="tbar">

						<a href="#" class="easyui-linkbutton" icon="icon-save"
							onclick="editCon();" plain="true">保存</a> 
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
							<td width="25%" class="form_title">源系统编号：</td>
							<td width="25%" class="form_content"><input type="text" id="sou_code" name="sou_code"
								readonly="readonly" style="width: 140px;"></input><font color="red"> *</font></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">源系统说明：</td>
							<td width="25%" class="form_content"><input type="text" id="sou_desc" name="sou_desc"
								readonly="readonly" style="width: 140px;"></input><font color="red"> *</font></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">源系统字典分类：</td>
							<td width="25%" class="form_content"><input type="text"
								id="sou_dict_code" name="sou_dict_code" readonly="readonly" style="width: 140px;"></input><font color="red"> *</font></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">源系统字典分类说明：</td>
							<td width="25%" class="form_content"><input type="text"
								id="sou_dict_desc" name="sou_dict_desc" readonly="readonly" style="width: 140px;"></input><font color="red"> *</font></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">源系统字典编码：</td>
							<td width="25%" class="form_content"><input type="text"
								id="sou_sub_dict_code" name="sou_sub_dict_code" readonly="readonly" style="width: 140px;"></input><font color="red"> *</font></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">源系统字典编码说明：</td>
							<td width="25%" class="form_content"><input type="text"
								id="sou_sub_dict_desc" name="sou_sub_dict_desc" readonly="readonly" style="width: 140px;"></input><font color="red"> *</font></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">统一字典分类：</td>
							<td width="25%" class="form_content"><select
								id="tran_dict_code" name="tran_dict_code"
								style="width: 144px;*width:146px;">
							</select><font color="red"> *</font></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">纺一字典分类说明：</td>
							<td width="25%" class="form_content"><input type="text"
								id="tran_dict_desc" name="tran_dict_desc" style="width: 140px;"></input><font color="red"> *</font></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">统一编码：</td>
							<td width="25%" class="form_content"><select id="tran_code"
								name="tran_code" style="width: 144px;*width:146px;">
							</select><font color="red"> *</font></td>
						</tr>
						<tr>
							<td width="25%" class="form_title">统一编码说明：</td>
							<td width="25%" class="form_content"><input type="text" id="tran_desc" name="tran_desc" style="width: 140px;"></input><font color="red"> *</font></td>
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