//初始化状态
$(document).ready(function() {
	//URL传参
	var id = getUrlParam("id");
	var informatic = null;
	var informatic_id = null;
	var InformaticaType;
	var tempCom = eval("[{'VALUE':'','NAME':'请选择'}]");
	$.ajax({
		url : baseUrl + '/db/findbyid',
		dataType : "json",
		data : {
			id : id
		},
		type : "GET",
		error : function() {
			$.messager.alert('提示信息', "数据加载失败。。。", "error");
		},
		success : function(data) {
			informatic = data;
			$('#editDataBase').form('load', informatic);
		}
	});
	
	// 验证规则初始化
	$('#editDataBase').validate({
		rules : {
			title : {
				required : true,
				maxlength : 100,
				remote : {
					url : baseUrl+'/db/findbyname',
					type : 'POST',
					dateType : 'json',
					data: {                     //要传递的数据
						id : getUrlParam("id")
					}
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

// 编辑DataBase数据
function editDB() {
	var id = getUrlParam("id");
	var options = {
		url : baseUrl + '/db/update',
		type : "post",
		dataType : 'json',
		data : {
			'id' : id,
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
				$.messager.alert('提示', '数据编辑成功', 'info', function() {
					parent.$('#DataBaseList').datagrid('load');
					parent.$('#jsjleditpage').dialog('close');
				});
			} else {
				$.messager.alert('提示', data.exceptionMsg);
			}
		},
		error : function() {
			$.messager.alert('提示信息', "数据编辑失败。。。", "error");
		},
		clearForm : true

	};
	/*
	 *异步提交
	 */
	$("#editDataBase").ajaxSubmit(options);
}

//测试DataBase连接
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
	 *异步提交
	 */
	$("#editDataBase").ajaxSubmit(options);
}

//关闭ADD界面
function closeAdd() {
	parent.$('#jsjleditpage').dialog('close');
}

/*
 * 提交前，数据验证
 */
function showRequest() {
	return $("#editDataBase").valid();
}

function savedisabled(){
	$('#save').linkbutton({
		disabled : true
	});
}