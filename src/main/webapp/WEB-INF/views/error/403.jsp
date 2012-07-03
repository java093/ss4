<%@page import="org.apache.shiro.SecurityUtils"%>
<%@page import="org.apache.shiro.subject.Subject"%>
<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>
<%response.setStatus(200);%>

<%
	//记录日志
	Logger logger = LoggerFactory.getLogger(this.getClass());
	Subject user = SecurityUtils.getSubject();
	logger.warn("用户["+user.getPrincipal()+"]无此功能的操作权限");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>无此功能的操作权限</title>
</head>

<body>
<br />
<div><h3>无此功能的操作权限.</h3></div>
<br />
<div><a href="<c:url value="/"/>">返回首页</a></div>
</body>
</html>
