<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<% String baseUrl = request.getContextPath(); %>
<link rel="stylesheet" type="text/css" href="<%= baseUrl%>/css/css.css">
<title>Login</title>
<script type="text/javascript">
var ie  =navigator.appName=="Microsoft Internet Explorer"?true:false;

function keyDown(e)
{
	var realkey = null;
	 if(!ie){
	  var nkey=e.which;
	  var iekey='现在是ns浏览器';
	  realkey=e.which;
	 }
	 if(ie){
	  var iekey=event.keyCode;
	  var nkey='现在是ie浏览器';
	  realkey=event.keyCode;
 	 }
	  if(13 == realkey){
		  login();
	  }

 //alert('ns浏览器中键值:'+nkey+'\n'+'ie浏览器中键值:'+iekey+'\n'+'实际键为'+realkey);
}
document.onkeydown = keyDown;

	function MM_swapImgRestore() { //v3.0
		var i, x, a = document.MM_sr;
		for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++)
			x.src = x.oSrc;
	}
	
//    function submitForm(frm,event){ 
//        var event=window.event?
//        window.event:event; 
//        if(event.keyCode==13){ 
//        	document.loginForm.submit(); 
//        } 
//      } 


	
	function login(){
//		$(#"loginForm").submit();
//alert(document.loginForm.username);
//alert(document.loginForm.password);
		document.loginForm.submit();
//		Document.all.loginForm.submit();
	}
	function reset(){
		username.value = null;
		password.balue = null;
	}
	function MM_preloadImages() { //v3.0
		var d = document;
		if (d.images) {
			if (!d.MM_p)
				d.MM_p = new Array();
			var i, j = d.MM_p.length, a = MM_preloadImages.arguments;
			for (i = 0; i < a.length; i++)
				if (a[i].indexOf("#") != 0) {3
					d.MM_p[j] = new Image;
					d.MM_p[j++].src = a[i];
				}
		}
	}

	function MM_findObj(n, d) { //v4.01
		var p, i, x;
		if (!d)
			d = document;
		if ((p = n.indexOf("?")) > 0 && parent.frames.length) {
			d = parent.frames[n.substring(p + 1)].document;
			n = n.substring(0, p);
		}
		if (!(x = d[n]) && d.all)
			x = d.all[n];
		for (i = 0; !x && i < d.forms.length; i++)
			x = d.forms[i][n];
		for (i = 0; !x && d.layers && i < d.layers.length; i++)
			x = MM_findObj(n, d.layers[i].document);
		if (!x && d.getElementById)
			x = d.getElementById(n);
		return x;
	}

	function MM_swapImage() { //v3.0
		var i, j = 0, x, a = MM_swapImage.arguments;
		document.MM_sr = new Array;
		for (i = 0; i < (a.length - 2); i += 3)
			if ((x = MM_findObj(a[i])) != null) {
				document.MM_sr[j++] = x;
				if (!x.oSrc)
					x.oSrc = x.src;
				x.src = a[i + 2];
			}
	}
</script>

</head>

<body>
	<%
		String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		if (error != null) {
	%>
	<div class="error prepend-top" style="font: '黑体'; font-size: 18px; color:red; font-weight: bold;">登录失败，请重试.</div>
	<%
		}
	%>
	<%
		if (request.getParameter("unauthorized") != null) {
	%>
	<div class="error prepend-top">
		用户无权限，请登录其他用户或
		<a href="javascript:history.back()">返回上一页</a>
	</div>
	<%
		}
	%>
	<form:form id="loginForm" name="loginForm" action="${ctx}/login" method="post">
		<fieldset >
			<div id="mainbox" class="mainbox">
				<div id="topbox" align="center" class="topbox">
					<img src="images/top_pic.jpg" align="middle" width="787px" height="245px" style="vertical-align: middle" />
				</div>
				<!--topbox-->
				<div id="midllebox" align="center" class="midllebox">
					<div style="height: 30px"></div>
					<div id="middlebox_login" class="middlebox_login">
						<div id="" class="loginbox">
							<ul>
								<li>
									<span
										style="font: '黑体'; font-size: 16px; color: #666666; font-weight: bold;">
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请输入用户名和密码
									</span>
								</li>
								<li style="margin-top: 20px;">
									<span style="font: '宋体'; font-size: 14px; color: #63676a;">用户名：</span>
									<input style="width: 170px" type="text" id="username" name="username" size="40" value="${username}" class="required"/>
								</li>
								<li style="margin-top: 15px;">
									<span style="font: '宋体'; font-size: 14px; color: #63676a;">密&nbsp;&nbsp;&nbsp;码：</span>
									<input type="password" id="password" name="password" size="40"  class="required" style="width: 170px"  />
								</li>
								<li style="margin-top: 18px; margin-left: 30px;">
									<a  href="javascript:void(0)" onMouseOut="MM_swapImgRestore()" 
										onMouseOver="MM_swapImage('Image6','','images/button5.jpg',1)" onclick="login()"  >
										<img src="images/button1.jpg" name="Image6" width="82" height="29" border="0" >
									</a> 
									<a href="javascript:void(0)" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image7','','images/button6.jpg',1)" onclick="reset()" >
										<img src="images/button2.jpg" name="Image7" width="82" height="29" border="0" style="margin-left: 8px">
									</a>
								</li>
								<span style="padding-left:10px;">
								<!--  <input id="submit" class="button" type="submit" value="登录" /> -->  </span>
								 <!-- input name="" type="button"  style=" width:10px; height:10px;  border:0; background:url(图片路径) no-repeat left top" -->
							</ul>
						</div>
						<!--loginbox-->
					</div>
					<!--middlebox_login-->
				</div>
				<!--midllebox-->

				<div id="bottombox" align="center" class="bottombox"></div>
				<!--bottombox-->
			</div>
			<!--mainbox-->
	</form:form>
</body>
</html>
