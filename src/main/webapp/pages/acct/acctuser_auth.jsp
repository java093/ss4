<%@page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="../common/heard_inc.jsp"%>
<title>人员授权</title>
<script type="text/javascript" src="<%=baseUrl%>/pages/acct/acctauth.js"></script>
</head>

<body style="background: #EFF0F2">
	<table cellpadding="0" cellspacing="0" border="0" class="form_table">
			<tr>
				<td>
					<div class="tbar">
						<a href="#" class="easyui-linkbutton" icon="icon-save"
							onclick="saveAcctAuth();" plain="true">保存</a> 
						<a href="#" class="easyui-linkbutton" icon="icon-redo" 
							onclick="closeAdd();" plain="true">取消</a>
					</div>
				</td>
			</tr>
		</table>
	<ul id="tree">
	</ul>
</body>
</html>