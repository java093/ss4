//初始化状态
$(document).ready(
		function() {
			// URL传参
			
			var id = getUrlParam("id");
			var subentry = getUrlParam("subentry");
			$.ajax({
				url : baseUrl + '/acctuser/findbyid',
				dataType : "json",
				data : {
					id : id,
					subentry : subentry
				},
				type : "GET",
				error : function() {
					$.messager.alert('提示信息', "数据加载失败。。。", "error");
				},
				success : function(data) {
					$('#updateAcctUserForm').form('load', data);
					//20120629 清空默认加密密码
					$("#password").val("");
				}
			});
		});

/*
 * 提交前，数据验证
 */
function showRequest() {
	return true;
}


/*
 * 新增,清空form 以及数据从第一页重新加载. 异步提交服务器，保存数据
 */
function updateAcctUserPwd() {
	var id =  getUrlParam("id");
	if (showRequest()) {
		// URL传参
		var options = {
			url : baseUrl + '/acctuser/updatePwd',
			type : "post",
			dataType : 'json',
			data : {
				'id':id,
				'password' : $("#password").val()
			},
			success : function(data) {
				if (data.success) {
					$.messager.alert('提示', '数据保存成功', 'info', function() {
						parent.$('#sysdictList').datagrid('load');
						parent.$('#jsjleditpage').dialog('close');
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
	$("#updateAcctUserForm").ajaxSubmit(options);
}
/*
 * 关闭弹出页面
 */
function closeWin() {
	parent.$('#sysdictList').datagrid('load');
	parent.$('#jsjleditpage').dialog('close');
}

