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
baseUrl = "<%= baseUrl%>";

</script>
<script type="text/javascript">
</script>
<script language="JavaScript" type="text/javascript"
	src="<%=baseUrl%>/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=baseUrl%>/pages/plan/plan_edit.js"></script>
<script type="text/javascript" src="<%=baseUrl%>/pages/plan/proving.js"></script>
</head>

<body style="background:#EFF0F2">
	<div id="addPrivilege" icon="icon-save" >
		<form method="get" name="addPrivilegeForm" id="addPrivilegeForm" >
			<table cellpadding="0" cellspacing="0" border="0" id="uiform" class="form_table">
				<tr>
					<td colspan="4">
						<div class="tbar">
							<a href="#" class="easyui-linkbutton"
								onclick="caseSrhEditsave();" icon="icon-save" plain="true">保存</a>
							<a href="#" class="easyui-linkbutton" onclick="closeWin();"
								icon="icon-redo" plain="true">取消</a>
						</div>
					</td>
				</tr>

				<tr>
					<td class="form_title" width="25%">计划名称：</td>
					<td class="form_content"><input id="name" name="name" /></td>
					<td colspan="2" class="form_content">启动：<input type="checkbox"
						name="plan_start" id="plan_start" /></td>

				</tr>
				<tr>
					<td class="form_title">说明：</td>
					<td colspan="3" class="form_content"><textarea cols="45"
							name="explain" rows="4"></textarea></td>
				</tr>
				<tr>
					<td class="form_title">计划类型：</td>
					<td colspan="3" class="form_content"><input type="radio"
						name="plan_type" value="0" onclick="aCheck1()" /> 仅执行一次 &nbsp; <input
						type="radio" name="plan_type" value="1" onclick="bCheck2()" /> 每天
						&nbsp; <input type="radio" name="plan_type" value="2"
						onclick="bCheck3()" /> 每月 &nbsp;</td>
				</tr>
				<tr>
					<td class="form_title">详细设置：</td>
					<td colspan="3" class="form_content"><span id="span0" style="display: block"></span>
						<span id="span1" style="display: none">
							<table>
								<tr>
									<td width="90" >执行日期：</td>
									<td><input id="plan_data_one" class="Wdate" style="cursor:pointer"
										onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'1970-01-01',maxDate:'2099-12-31'})" readonly/></td>
								</tr>
								<tr>
									<td width="90" >执行时间：</td>
									<td><input id="plan_time_one" style="cursor:pointer"
										onfocus="WdatePicker({skin:'whyGreen',dateFmt:'H:mm:ss'})"
										class="Wdate" readonly/></td>
								</tr>
							</table>
					</span> <span id="span2" style="display: none">
							<table>
								<tr>
									<td width="90" >执行时间：</td>
									<td><input id="plan_data_day" style="cursor:pointer"
										onfocus="WdatePicker({skin:'whyGreen',dateFmt:'H:mm:ss'})"
										class="Wdate" readonly/></td>
								</tr>
								<tr>
									<td width="90" >开始日期：</td>
									<td><input id="plan_time1_day" class="Wdate" style="cursor:pointer"
										onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'1970-01-01',maxDate:'2099-12-31'})" readonly/></td>
								</tr>
								<tr>
									<td width="90" >结束日期：</td>
									<td><input id="plan_time2_day" class="Wdate" style="cursor:pointer"
										onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'1970-01-01',maxDate:'2099-12-31'})" readonly/></td>
								</tr>
							</table>
					</span> <span id="span3" style="display: none">
							<table>
								<tr>
									<td colspan="2">计划在每月以下时间执行：</td>
								</tr>
								<tr>
									<td colspan="2">
									<input type="checkbox" name="plan_month"
										value="1" />01 <input type="checkbox" name="plan_month"
										value="2" />02 <input type="checkbox" name="plan_month"
										value="3" />03 <input type="checkbox" name="plan_month"
										value="4" />04 <input type="checkbox" name="plan_month"
										value="5" />05  <input type="checkbox"
										name="plan_month" value="6" />06 <input type="checkbox"
										name="plan_month" value="7" />07 <input type="checkbox"
										name="plan_month" value="8" />08 <input type="checkbox"
										name="plan_month" value="9" />09 <input type="checkbox"
										name="plan_month" value="10" />10  <input
										type="checkbox" name="plan_month" value="11" />11 <input
										type="checkbox" name="plan_month" value="12" />12 <input
										type="checkbox" name="plan_month" value="13" />13 <input
										type="checkbox" name="plan_month" value="14" />14 <input
										type="checkbox" name="plan_month" value="15" />15  <input
										type="checkbox" name="plan_month" value="16" />16 <input
										type="checkbox" name="plan_month" value="17" />17 <input
										type="checkbox" name="plan_month" value="18" />18 <input
										type="checkbox" name="plan_month" value="19" />19 <input
										type="checkbox" name="plan_month" value="20" />20  <input
										type="checkbox" name="plan_month" value="21" />21 <input
										type="checkbox" name="plan_month" value="22" />22 <input
										type="checkbox" name="plan_month" value="23" />23 <input
										type="checkbox" name="plan_month" value="24" />24 <input
										type="checkbox" name="plan_month" value="25" />25  <input
										type="checkbox" name="plan_month" value="26" />26 <input
										type="checkbox" name="plan_month" value="27" />27 <input
										type="checkbox" name="plan_month" value="28" />28 <input
										type="checkbox" name="plan_month" value="29" />29 <input
										type="checkbox" name="plan_month" value="30" />30  <input
										type="checkbox" name="plan_month" value="31" />31</td>
								</tr>
								<tr>
									<td width="90" >执行时间：</td>
									<td><input id="plan_data_month"
										onfocus="WdatePicker({skin:'whyGreen',dateFmt:'H:mm:ss'})"
										class="Wdate" readonly style="cursor:pointer"/></td>
								</tr>
								<tr>
									<td width="90" >开始日期：</td>
									<td><input id="plan_time1_month" class="Wdate"
										onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'1970-01-01',maxDate:'2099-12-31'})" readonly style="cursor:pointer"/></td>
								</tr>
								<tr>
									<td width="90" >结束日期：</td>
									<td><input id="plan_time2_month" class="Wdate"
										onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'1970-01-01',maxDate:'2099-12-31'})" readonly style="cursor:pointer"/></td>
								</tr>

							</table>


					</span></td>
					<td></td>
				</tr>
				<tr><br/></tr>
				<td><br/></td>
				<td><br/></td>
			</table>
		</form>
	</div>
</body>
</html>