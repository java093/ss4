<%@page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="../common/heard_inc.jsp"%>
<title>Session一览</title>
<script type="text/javascript">
//路径
baseUrl = "<%= baseUrl%>";
</script>
<script type="text/javascript" src="<%=baseUrl%>/pages/mng/session.js"></script>
<script type="text/javascript">
</script>
</head>
<style>
.maintopRight{
	padding-right:10px;
}
a{TEXT-DECORATION:none}
</style>
<body>
<form method="get">
<table border="0" cellpadding="0" cellspacing="0" width="100%"> 
  <tr>
    <td>
		<div id = "divid" class="tbar">
		    <!-- <a href="#" class="easyui-linkbutton" onclick="sessionadd();" icon="icon-add" plain="true">新增</a>
			<a href="#" class="easyui-linkbutton" onclick="sessionUpdate();" icon="icon-edit" plain="true">编辑</a>
			<a id = "" href="#" class="easyui-linkbutton" onclick="deleteSession();" icon="icon-cancel" plain="true">删除</a> -->
			检查层次:
			<select name="check_level" id="check_level">
				<!-- <option value="0"></option>
				<option value="STG">STAGE层检查</option>
				<option value="ODS">ODS层检查</option> -->
			</select>
			<a id = "" href="#" class="easyui-linkbutton" onclick="queryByLevel();" icon="icon-search" plain="true">查询</a>
		</div>
    </td>
  </tr>
  <tr>
    <td>
      <div class="grid">
        <table id="sessionList"></table>
      </div>   
    </td>
  </tr>
</table>
</form>
</body>
</html>

