// 插入acctMenu数据
$(document).ready(function() {
	//验证规则初始化
	$('#saveAcctMenuForm').validate({
		rules : {
			name : {
				required : true,
				minlength : 2
			},
			func_url : {
				required : true,
				minlength : 2
			},
			code : {
				required : true,
				minlength : 4
			}
		}
	});
})
function saveAcctMenu() {
		var options = {
			url : baseUrl + '/acctmenu/add',
			type : "post",
			dataType : 'json',
			data : {
				'parentId' : getUrlParam("parentId"),
				'email' : $("#email").val(),
				'login_name' : $("#login_name").val(),
				'password' : $("#password").val(),
				'name' : $("#name").val()
			},
			beforeSubmit : showRequest,
			success : function(data) {
				if (data.success) {
					$.messager.alert('提示信息', "保存数据成功!");
					$('#tree').tree('reload');
				} else {
					$.messager.alert('提示', data.exceptionMsg);
				}
			},
			error : function() {
				$.messager.alert('提示信息', "数据保存失败。。。", "error");
			},
			clearForm : true

		};
	/*
	 * 异步提交
	 */
	$("#saveAcctMenuForm").ajaxSubmit(options);
}

//调用验证方法
function showRequest(formData, jqForm, options) {
	return $("#saveAcctMenuForm").valid();
}

// 关闭ADD界面
function closeAdd() {
	parent.$('#sysdictList').datagrid('load');
	parent.$('#jsjladdpage').dialog('close');
}
