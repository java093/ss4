<%@page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="../common/heard_inc.jsp"%>
<title>邮件配置一览</title>
<script type="text/javascript">
</script>
<script type="text/javascript" src="<%=baseUrl%>/pages/sys/mailconfig.js"></script>
<script>
	var isEdit = false;
	var isSave = false;
	var LOGICALNAME;
	var PASSWORD;
	var HOST;
	var PORT;
	var isFirst = false;
	function mailEdit() {
		if (!isEdit) {
			isEdit = true;
			LOGICALNAME = tdclick($('#LOGICALNAME'), "name");
			PASSWORD = tdclick($('#PASSWORD'), "PASSWORD");
			HOST = tdclick($('#HOST'), "HOST");
			PORT = tdclick($('#PORT'), "PORT");
			isSave = true;
		}
	}
	/*编辑单项事件*/
	function tdclick(labelname, name) {
		var td = labelname; //得到当前td的内容
		var text = td.text(); //将td的内容保存
		td.html(""); //清空td中的内容
		var input = $("<input>"); //新建一个输入框
		input.attr("value", text); //定义输入框的内容，将保存的td值作为输入框的值
		input.attr("id", name); //定义输入框的内容，将保存的td值作为输入框的值
		input.attr("name", name); //定义输入框的内容，将保存的td值作为输入框的值
		td.append(input); //将输入框加入到td中，也可以用input.appendTo(td);
		var inputDom = input.get(0); //将jQuery对象转换成Dom对象
		inputDom.select(); //让输入框中的内容被高亮选中
		td.unbind("click"); //清除点击事件
		return input;
	}

	// 调用验证方法
	function showRequest() {
		return $("#mailForm").valid();
	}

	function mailSave() {
		if (isSave) {
			var name = $(LOGICALNAME).attr("value");
			var pwd = $(PASSWORD).attr("value");
			var server = $(HOST).attr("value");
			var port1 = $(PORT).attr("value");
			$update = {
				'id' : id,
				'HOST' : server,
				'LOGICALNAME' : name,
				'PORT' : port1,
				'PASSWORD' : pwd
			}
			if (showRequest()) {
				isEdit = false;
				isSave = false;
				var urlParam;
				if(isFirst){
					urlParam = baseUrl + '/mail/add'; // 新增操作
				}else{
					urlParam = baseUrl + '/mail/update'; // 编辑操作
				}
				$.post(urlParam, $update, function(data) {
					var data = eval("(" + data + ")");
					if (data.success) {
						if(isFirst){
							isFirst = false;
							$.ajax({
								url : baseUrl + '/mail/list',
								dataType : "json",
								type : "GET",
								success : function(json) {
									if(json != null){
										id = json.id;
									}
								}
							});
						}
						save(LOGICALNAME);
						save(PASSWORD);
						save(HOST);
						save(PORT);
						$.messager.alert('提示信息', '保存成功', 'info', function() {
						});
					} else {
						$.messager.alert('提示信息', '保存失败', 'warning');
					}
				});
			}
		}
	}

	function save(labelname) {
		var inputNode = $(labelname); //获取当前输入框
		var inputText = labelname.val();//保存当前输入框的内容
		var tdNode = labelname.parent();//清空td里的内容
		tdNode.html(inputText); //将保存的输入框中的内容加到td中
		return inputText;
	}
</script>
</head>
<style>
input{
	width:200px;
}

</style>
<body>
	<form method="get" id="mailForm" name="mailForm">
		<table cellpadding="0" cellspacing="0" border="0" class="form_table">
			<tr>
				<td>
					<div class="tbar">
						<a href="#" class="easyui-linkbutton" icon="icon-save"
							onclick="mailSave();" plain="true">保存</a> <a href="#"
							class="easyui-linkbutton" icon="icon-edit" onclick="mailEdit();"
							plain="true">编辑</a>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<table id="edit_table" cellpadding="0" cellspacing="0" border="0"
						class="form_table">
						<tr>
							<th align="left" class="form_content">参数名称</th>
							<th align="left" class="form_content">参数值</th>
							<th align="left" class="form_content">参数描述</th>
						</tr>
						<tr>
							<td width="10%" class="form_title">邮箱名称：</td>
							<td id="LOGICALNAME" width="20%" class="form_content"></td>
							<td width="50%" class="form_content"><label id="email_desc">邮件发送人地址</label></td>
						</tr>
						<tr>
							<td width="10%" class="form_title">邮箱密码：</td>
							<td id="PASSWORD" width="10%" class="form_content"></td>
							<td width="10%" class="form_content"><label
								id="password_desc">邮件发送人密码</label></td>
						</tr>
						<tr>
							<td width="10%" class="form_title">邮箱服务器：</td>
							<td id="HOST" width="10%" class="form_content"></td>
							<td width="10%" class="form_content"><label id="server_desc">邮件服务器地址</label></td>
						</tr>
						<tr>
							<td width="10%" class="form_title">服务器端口：</td>
							<td id="PORT" width="10%" class="form_content"></td>
							<td width="10%" class="form_content"><label id="port_desc">邮件服务器端口</label></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>

