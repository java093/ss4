<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Informatic系统信息</title>
<%@ include file="../common/heard_inc.jsp"%>
</head>
<body ChangeSize="testSize()">
<form method="get">
<table border="0" cellpadding="0" cellspacing="0" width="100%"> 
<!-- 工具条（按钮区） -->
  <tr>
    <td>
		<div class="tbar">
		<a href="#" class="easyui-linkbutton"   onclick="searchAttSrh()" icon="icon-search" plain="true">查询</a>    
		<a href="#" class="easyui-linkbutton" onclick="OutExcle()" icon="icon-export" plain="true">导出</a>
		<a href="#" class="easyui-linkbutton" onclick="openStatistics()" icon="icon-view" plain="true">统计</a>
		</div>
    </td>
  </tr>
  <!-- Search Condition -->
  <tr>
    <td class="search">	
	  <table border="0" cellpadding="0" cellspacing="0" width="100%">	
		<tr>
			<td nowrap="nowrap" width="80" align="right">集团：</td>
			<td width="200" align="left"><input type="text" id="groupid" /></td>
			<td nowrap="nowrap" width="80" align="right">电厂：</td>
			<td width="200" align="left"><input type="text" id="dcid" /></td>
			<td nowrap="nowrap" width="80" align="right">项目名称：</td>
			<td width="200" align="left">
				<input id="prjseq" name="prjseq" >
		    </td>
			<td></td>
		</tr>
		<tr>
		    <td nowrap="nowrap" width="80" align="right">专业：</td>
			<td width="200" align="left"><input type="text" id="deptid" /></td>
			<td nowrap="nowrap" width="80" align="right">姓名：</td>
			<td width="200" align="left"><input type="text" id="empid" /></td>
			<td align="right" nowrap="nowrap"><label>考勤日期：</label></td>
			<td align="left" colspan="2">
			<input type="text" class="Wdate" id="prjdatefrom" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'prjdateto\',{d:-1});}'})" size="12"/>
            <label>-</label>
            <input type="text" class="Wdate" id="prjdateto" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'prjdatefrom\',{d:1});}'})" size="12"/>
            </td>
		</tr>
	 </table>
   </td>
  </tr>
  <!-- Result List -->
  <tr>
    <td>
      <div class="grid">
        <table id="infaList"></table>
      </div>   
    </td>
  </tr>
</table>

<script type="text/javascript">
</script>

</form>
</body>
</html>