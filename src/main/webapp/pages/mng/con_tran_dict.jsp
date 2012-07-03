<%@page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="../common/heard_inc.jsp"%>
<title>邮件配置一览</title>
<script type="text/javascript">
//路径
baseUrl = "<%= baseUrl%>";
</script>
<script type="text/javascript" src="<%=baseUrl%>/pages/mng/con_tran_dict.js"></script>
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
		    <a href="#" class="easyui-linkbutton" onclick="conadd();" icon="icon-add" plain="true">新增</a>
			<a href="#" class="easyui-linkbutton" onclick="conedit();" icon="icon-edit" plain="true">编辑</a>
			<a id = "" href="#" class="easyui-linkbutton" onclick="deleteCons();" icon="icon-cancel" plain="true">删除</a>
		</div>
    </td>
  </tr>
  <tr>
    <td>
      <div class="grid">
        <table id="conList"></table>
      </div>   
    </td>
  </tr>
</table>
</form>
</body>
</html>

