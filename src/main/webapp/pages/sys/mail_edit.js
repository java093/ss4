//初始化状态
$(document).ready(function() {
	// URL传参
	var id = getUrlParam("id");
	$.ajax({
		url : baseUrl + '/mail/findbyid',
		dataType : "json",
		data : {
			id : id
		},
		type : "GET",
		error : function() {
			$.messager.alert('提示信息', "数据加载失败。。。", "error");
		},
		success : function(data) {
			$('#updateForm').form('load', data);
		}
	});
	// 验证规则初始化
	$('#updateForm').validate({
		rules : {
			HOST : {
				required : true,
				maxlength: 50
			},
			USERNAME : {
				required : true,
				maxlength: 20
			},
			PORT : {
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
});

/*
 * 新增,清空form 以及数据从第一页重新加载. 异步提交服务器，保存数据
 */
var flage = false;
function mailEditSave() {
	// URL传参
	var id = getUrlParam("id");
	var options = {
		url : baseUrl + '/mail/update',
		type : "post",
		dataType : 'json',
		data : {
			'id' : id,
			'HOST' : $("#HOST").val(),
			'USERNAME' : $("#USERNAME").val(),
			'PORT' : $("#PORT").val(),
			'PASSWORD' : $("#PASSWORD").val()
		},
		beforeSubmit : showRequest,
		success : function(data) {
			if (data.success) {
				$.messager.alert('提示', messageInfo.editSuccess, 'info', function() {
					parent.$('#mailList').datagrid('load');
					parent.$('#jsjleditpage').dialog('close');
				});
			} else {
				$.messager.alert('提示', data.exceptionMsg);
			}
		},
		error : function() {
			$.messager.alert('提示信息', messageInfo.editFail, "error");
		},
		clearForm : true
	};

	/*
	 * 异步提交
	 */
	$("#updateForm").ajaxSubmit(options);
}

// 调用验证方法
function showRequest(formData, jqForm, options) {
	return $("#updateForm").valid();
}

/*
 * 关闭弹出页面
 */
function closeWin() {
	parent.$('#jsjleditpage').dialog('close');
}