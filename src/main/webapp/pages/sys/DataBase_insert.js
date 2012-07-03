//初始化状态
$(document).ready(function() {
//	var InformaticaType;
//	var tempCom = eval("[{'VALUE':'','NAME':'Informatica'}]");
//	var data1 = {
//		"rows" : [ {
//			"NAME" : "Informatica",
//			"VALUE" : "0"
//		}, {
//			"NAME" : "知识库",
//			"VALUE" : "1"
//		} ]
//	}
//	InformaticaType = $('#dbuse').ligerComboBox({
//		data : tempCom,
//		resize : true,
//		width : $("#title").width,
//		valueField : 'VALUE',
//		textField : 'NAME',
//		valueFieldID : 'prjstatusvalue',
//	});
//	InformaticaType.selectValue("");
//	InformaticaType.setData(data1.rows);

	// 验证规则初始化
	$('#addDataBase').validate({
		rules : {
			title : {
				required : true,
				maxlength : 100,
				remote : {
					url : baseUrl+'/db/findbyname',
					type : 'POST',
					dateType : 'json'
				}
			},
			dbname : {
				required : true,
				maxlength : 20
			},
			server_name : {
				required : true,
				maxlength : 100
			},
			dbtype : {
				required : true
			},
			username : {
				required : true
			},
			password : {
				required : true
			},
			port : {
				required : true,
				number : true
			},
			ip : {
				required : true,
				ipv4 : true
			}
		},
		messages : {
			title : {
				remote : "服务名称已存在！"
			}
		}
	});

});

// 插入DataBase数据
function addDB() {
	var options = {
		url : baseUrl + '/db/add',
		type : "post",
		dataType : 'json',
		data : {
			'title' : $("#title").val(),
			'dbname' : $("#dbname").val(),
			'dbtype' : $("#dbtype").val(),
			'username' : $("#username").val(),
			'password' : $("#password").val(),
			'port' : $("#port").val(),
			'ip' : $("#ip").val(),
			'server_name' : $("#server_name").val(),
			'dbuse' : $('#dbuse').value
		},
		success : function(data) {
			if (data.success) {
				$.messager.alert('提示', '数据保存成功', 'info', function() {
					parent.$('#DataBaseList').datagrid('load');
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
	 * 异步提交
	 */
	$("#addDataBase").ajaxSubmit(options);
}

// 测试DataBase连接
function testConnection() {
	if (showRequest()) {
		var options = {
			url : baseUrl + '/db/testConnection',
			type : "post",
			dataType : 'json',
			data : {
				'title' : $("#title").val(),
				'dbname' : $("#dbname").val(),
				'dbtype' : $("#dbtype").val(),
				'username' : $("#username").val(),
				'password' : $("#password").val(),
				'port' : $("#port").val(),
				'server_name' : $("#server_name").val(),
				'ip' : $("#ip").val()
			},
			success : function(data) {
				if (data.success) {
					$.messager.alert('提示', '数据库连接成功', 'info', function() {
						$('#save').linkbutton({
							disabled : false
						});
					});
				} else {
					$.messager.alert('提示', "数据库连接失败");
				}
			},
			error : function() {
				$.messager.alert('提示信息', "数据库连接失败。。。", "error");
			}
		};
	}
	/*
	 * 异步提交
	 */
	$("#addDataBase").ajaxSubmit(options);
}

// 关闭ADD界面
function closeAdd() {
	parent.$('#jsjladdpage').dialog('close');
}

/*
 * 提交前，数据验证
 */
function showRequest() {
	return $("#addDataBase").valid();
}

function savedisabled(){
	$('#save').linkbutton({
		disabled : true
	});
}