//初始化状态
$(document).ready(function() {
	// URL传参
	var session_id = getUrlParam("session_id");
	var mail = null;
	$.ajax({
		url : baseUrl + '/session/findbyid',
		dataType : "json",
		data : {
			session_id : session_id,
		},
		type : "GET",
		error : function() {
			$.messager.alert('提示信息', "数据加载失败。。。", "error");
		},
		success : function(data) {
			mail = data;
			$('#updateSessionForm').form('load', mail);
		}
	});
});

/*
 * 新增,清空form 以及数据从第一页重新加载. 异步提交服务器，保存数据
 */
function editSession() {
	// if(showRequest()){
	// URL传参
	var options = {
		url : baseUrl + '/session/update',
		type : "post",
		dataType : 'json',
		data : {
			'subj_name' : $("#subj_name").val(),
			'subj_id' : $("#subj_id").val(),
			'session_name' : $("#session_name").val(),
			'session_id' : $("#session_id").val(),
			'workflow_name' : $("#workflow_name").val(),
			'workflow_id' : $("#workflow_id").val(),
			'mapping_name' : $("#mapping_name").val(),
			'mapping_id' : $("#mapping_id").val(),
			'check_level' : $("#check_level").val(),
			'check_level_desc' : $("#check_level_desc").val(),
			'check_type' : $("#check_type").val(),
			'check_type_desc' : $("#check_type_desc").val(),
			'default_ind' : $("#default_ind").val()
		},
		success : function(data) {
			if (data.success) {
				$.messager.alert('提示', '数据保存成功', 'info', function() {
					parent.$('#sessionList').datagrid('load');
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

	// }
	/*
	 * 异步提交
	 */
	$("#updateSessionForm").ajaxSubmit(options);
}
/*
 * 关闭弹出页面
 */
function closeWin() {
	parent.$('#jsjleditpage').dialog('close');
}