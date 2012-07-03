// 插入acctUser数据
$(document).ready(function() {
	//验证规则初始化
	$('#saveAcctUserForm').validate({
		rules : {
			login_name : {
				required : true,
				minlength : 4,
				remote: {
					url: baseUrl+'/acctuser/findByLoginName',
					type: 'POST',
					dateType: 'json'
				}
			},
			name : {
				required : true,
				minlength : 4
			},
			password : {
				required : true,
				minlength : 4
			}
		},
		messages: {
			login_name:{
	    		  remote:"登陆名已存在！"
	    		} 
	     }
	});
})
function saveAcctUser() {
	if (true) {
		var options = {
			url : baseUrl + '/acctuser/add',
			type : "post",
			dataType : 'json',
			data : {
				'login_name' : $("#login_name").val(),
				'password' : $("#password").val(),
				'name' : $("#name").val()
			},
			beforeSubmit : showRequest,
			success : function(data) {
				if (data.success) {
					$.messager.alert('提示', '数据保存成功', 'info', function() {
						parent.$('#sysdictList').datagrid('load');
						parent.$('#jsjladdpage').dialog('close');
					});
				} else {
					$.messager.alert('提示', data.exceptionMsg);
				}
			},
			error : function() {
				$.messager.alert('提示信息', "数据保存失败。。。", "error");
			},
			clearForm : true
		};
	}
	/*
	 * 异步提交
	 */
	$("#saveAcctUserForm").ajaxSubmit(options);
}

//调用验证方法
function showRequest(formData, jqForm, options) {
	return $("#saveAcctUserForm").valid();
}

// 关闭ADD界面
function closeAdd() {
	parent.$('#sysdictList').datagrid('load');
	parent.$('#jsjladdpage').dialog('close');
}
