<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String baseUrl = request.getContextPath();
%>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="0">

<link rel="stylesheet" type="text/css" href="<%= baseUrl%>/script/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%= baseUrl%>/script/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="<%= baseUrl%>/css/common.css">
<link rel="stylesheet" type="text/css" href="<%= baseUrl%>/static/jquery-validation/1.9.0/milk.css">
<link rel="stylesheet" type="text/css" href="<%= baseUrl%>/script/easyui/combox/skins/Aqua/css/ligerui-form.css">
<script type="text/javascript" src="<%= baseUrl%>/script/easyui/DatePicker/WdatePicker.js"></script> 
<script type="text/javascript" src="<%= baseUrl%>/script/easyui/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%= baseUrl%>/static/jquery-validation/1.9.0/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%= baseUrl%>/static/jquery-validation/1.9.0/additional-methods.min.js"></script>
<script type="text/javascript" src="<%= baseUrl%>/static/jquery-validation/1.9.0/messages_cn.js"></script>
<script type="text/javascript" src="<%= baseUrl%>/script/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%= baseUrl%>/script/easyui/plugins/jquery.validatebox.js"></script>
<script type="text/javascript" src="<%= baseUrl%>/script/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=baseUrl%>/script/jquery.form.js"></script>
<script src="<%= baseUrl%>/script/easyui/combox/base.js" type="text/javascript"></script>
<script src="<%= baseUrl%>/script/easyui/combox/ligerComboBox.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=baseUrl%>/script/easyui/windowControl.js"></script>
<script type="text/javascript" src="<%=baseUrl%>/script/easyui/mask.js"></script>
<script type="text/javascript" src="<%=baseUrl%>/pages/common/common.js"></script>
<script type="text/javascript">
 var root = "<%=baseUrl%>"; //js中存放当前页面的root路径方便调用
 var baseUrl = "<%=baseUrl%>"; //js中存放当前页面的baseUrl路径方便调用
</script>