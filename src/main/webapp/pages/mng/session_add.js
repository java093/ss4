//初始化状态
$(document).ready(function() {

});

// 插入Session数据
function addSession() {
	var options = {
		url : baseUrl + '/session/add',
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
	/*
	 *异步提交
	 */
	$("#addSessionForm").ajaxSubmit(options);
}

//关闭ADD界面
function closeAdd() {
	parent.$('#jsjladdpage').dialog('close');
}
