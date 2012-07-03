<%@page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<script type="text/javascript" src="<%=baseUrl%>/pages/plan/plan.js"></script>
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
					<div id="divid" class="tbar">

						<a href="#" class="easyui-linkbutton" onclick="planadd();"
							icon="icon-add" plain="true">新增</a> <a id="" href="#"
							class="easyui-linkbutton" onclick="informaticaedit();"
							icon="icon-edit" plain="true">编辑</a> <a id="" href="#"
							class="easyui-linkbutton" onclick="deletePlans();"
							icon="icon-cancel" plain="true">删除</a>
					</div>
				</td>
			</tr>
			<tr>
				<td class="search">
					<table border="0" cellpadding="0" cellspacing="0" width="50%">
						<tr height="30">
							<td nowrap="nowrap" width="15%" align="left">计划类型：</td>
							<td nowrap="nowrap" width="20%" align="left"><select
								name="type" id="type" style="width: 100px;">
									<option value="">请选择</option>
									<option value="0">仅执行一次</option>
									<option value="1">每天</option>
									<option value="2">每月</option>
							</select></td>
							<td nowrap="nowrap" width="20%" align="right">计划名称：</td>
							<td nowrap="nowrap" width="20%" align="right"><input
								style="width: 100px;" type="text" id="name" name="name" /></td>
							<td width="20%" align="right"><a id="" href="#"
								class="easyui-linkbutton" onclick="searchPlan();"
								icon="icon-search" plain="true">查询</a></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<div class="grid">
						<table id="planList"></table>
					</div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>

