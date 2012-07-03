$(document).ready(function() {
	// 验证规则初始化
	$('#insertForm').validate({
		rules : {
			hostName : {
				required : true,
				maxlength: 50
			},
			userName : {
				required : true,
				maxlength: 20
			},
			smtpPort : {
				required : true,
				range : [ 0, 65535 ],
				digits : true
			},
			PASSWORD : {
				required : true,
				maxlength: 10
			},
			repassword : {
				required : true,
				maxlength: 10,
				equalTo : "#PASSWORD"
			}
		}
	});
})

/*
 * 新增,清空form 以及数据从第一页重新加载. 异步提交服务器，保存数据
 */
function mailAdd() {
	var options = {
		url : baseUrl + '/mail/add',
		type : "post",
		dataType : 'json',
		data : {
			'hostName' : $("#hostName").val(),
			'userName' : $("#userName").val(),
			'smtpPort' : $("#smtpPort").val(),
			'password' : $("#PASSWORD").val()
		},
		beforeSubmit : showRequest,
		success : function(data) {
			if (data.success) {
				$.messager.alert('提示', messageInfo.addSuccess, 'info',
						function() {
							parent.$('#mailList').datagrid('load');
							parent.$('#jsjladdpage').dialog('close');
						});
			} else {
				$.messager.alert('提示', data.exceptionMsg);
			}
		},
		error : function() {
			$.messager.alert('提示信息', messageInfo.addFail, "error");
		},
		clearForm : true
	};

	/*
	 * 异步提交
	 */
	$("#insertForm").ajaxSubmit(options);

	// //表单提交
	// $('#insertForm').form({
	// url:baseUrl+'/mail/add?hostName='+$("#hostName").val()+'&userName='+$("#userName").val()+'&smtpPort='+$("#smtpPort").val()+'&password='+$("#PASSWORD").val(),
	// onSubmit:function(){
	// return $(this).form('validate');
	// },
	// success:function(data){
	// var data = eval( "(" + data + ")" );//json String变成对象
	// if(data.success){
	// $.messager.alert('提示',messageInfo.addSuccess,'info',
	// function(){
	// parent.$('#mailList').datagrid('load');
	// parent.$('#jsjladdpage').dialog('close');
	// });
	// }else{
	// $.messager.alert('提示',messageInfo.addFail);
	// }
	// }
	// });
	// // 提交 form
	// $('#insertForm').submit();
}

// 调用验证方法
function showRequest(formData, jqForm, options) {
	return $("#insertForm").valid();
}

/*
 * 关闭弹出页面
 */
function closeWin() {
	parent.$('#jsjladdpage').dialog('close');
}