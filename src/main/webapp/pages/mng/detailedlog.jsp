<%@page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="../common/heard_inc.jsp"%>
<title>日志管理</title>
<script type="text/javascript">
//路径
baseUrl = "<%= baseUrl%>";
</script>
<script type="text/javascript" src="<%=baseUrl%>/pages/mng/detailedlog.js"></script>
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
<div class="grid">
      <table  border="0" cellpadding="0" cellspacing="0" width="100%" height="96%">
      	<tr>
      		<td  width="70%" >
      			<table id="detailedLogList" ></table>	
      		</td>
      		<td width="30%" >
      		<label>任务名称:</label>
      		<input type="text" border="0" id="labelName" readonly="readonly">
				<br/>
				<label>详&nbsp;&nbsp;&nbsp;&nbsp;情:</label>
				
				<textarea name="inputDetail" style="max-width:200px;max-height:320px;min-width:200px;min-height:320px" readonly="readonly" id="inputDetail" cols="45" rows="5"></textarea>
				<br/>
      		</td>
      	</tr>
      </table>
 </div>
      
</form>
</body>
</html>

