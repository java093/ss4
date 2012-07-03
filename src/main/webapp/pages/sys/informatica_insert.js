//初始化状态
$(document).ready(
		function() {
			var dbType;
			// var tempCom = eval("[{'VALUE':'','NAME':'请选择'}]");
			$.ajax({
				url : baseUrl + '/sys/finddb',
				dataType : "json",
				type : "GET",
				error : function() {
					parent.$.messager.alert('提示信息', "没有查询到可关联的数据库信息，请先添加数据库信息",
							"error");
					parent.$('#jsjladdpage').dialog('close');
				},
				success : function(data) {
					if (data == null) {
						parent.$.messager.alert('提示信息',
								"没有查询到可关联的数据库信息，请先添加数据库信息", "error");
						parent.$('#jsjladdpage').dialog('close');
					} else {
						$.ajax({
							url : baseUrl + '/sys/db_id',
							dataType : "json",
							type : "POST",
							success : function(json) {
								selectinit(json, "knowledge_base");
							}
						});
					}
				}
			});

			// 为下拉框加载数据
			function selectinit(data, selectid) {
				if (data != null) {
					var $select1 = $("#" + selectid);
					$select1.empty();
					for ( var i = 0; i < data.length; i++) {
						$("<option/>").attr("value", data[i].ID).html(
								data[i].TITLE).appendTo($select1);
					}
				}
			}
//			// 项目状态
//			dbType = $('#knowledge_base').ligerComboBox({
//				data : tempCom,
//				width : "143",
//				valueField : 'VALUE',
//				textField : 'NAME',
//				valueFieldID : 'prjstatusvalue'
//			});
//			dbType.selectValue("");
//			$.getJSON(baseUrl + '/sys/db_id', function(data) {
//				var rows = data.rows;
//				dbType.setData(rows);
//
//			});

			// 验证规则初始化
			$('#addInformatica').validate({
				rules : {
					server_name : {
						required : true,
						maxlength : 200,
						remote : {
							url : baseUrl + '/sys/findbyname',
							type : 'POST',
							dateType : 'json'
						}
					},
					ip : {
						required : true,
						ipv4 : true
					},
					port : {
						required : true,
						number : true
					},
					domain : {
						required : true
					},
					integration_server : {
						required : true,
						maxlength : 50
					},
					user_name : {
						required : true
					},
					password : {
						required : true
					},
					knowledge_base : {
						required : true
					},
					wrlogpath : {
						required : true,
						maxlength : 100
					},
					sesslogpath : {
						required : true,
						maxlength : 100
					}
				},
				messages : {
					server_name : {
						remote : "服务器名称已存在！"
					}
				}
			});
		});

// 插入Informatic数据
function addInformatic() {
	if (showRequest()) {
		var options = {
			url : baseUrl + '/sys/add',
			type : "post",
			dataType : 'json',
			data : {
				'server_name' : $("#server_name").val(),
				'ip' : $("#ip").val(),
				'port' : $("#port").val(),
				'domain' : $("#domain").val(),
				'knowledge_base' : $("#knowledge_base").val(),
				'integration_server' : $("#integration_server").val(),
				'user_name' : $("#user_name").val(),
				'password' : $("#password").val(),
				'wrlogpath' : $("#wrlogpath").val(),
				'sesslogpath' : $("#sesslogpath").val()
			},
			success : function(data) {
				if (data.success) {
					$.messager.alert('提示', '数据保存成功', 'info', function() {
						parent.$('#eltList').datagrid('load');
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
	$("#addInformatica").ajaxSubmit(options);
}

// 关闭ADD界面
function closeAdd() {
	parent.$('#jsjladdpage').dialog('close');
}

/**
 * 测试连接
 */
function testConnection() {
	if (showRequest()) {
		var options = {
			url : baseUrl + '/sys/testConnection',
			type : "post",
			dataType : 'json',
			data : {
				'server_name' : $("#server_name").val(),
				'ip' : $("#ip").val(),
				'port' : $("#port").val(),
				'domain' : $("#domain").val(),
				'knowledge_base' : $("#knowledge_base").val(),
				'integration_server' : $("#integration_server").val(),
				'user_name' : $("#user_name").val(),
				'password' : $("#password").val()
			},
			success : function(data) {
				if (data.success == "connected") {
					$.messager.alert('提示', '连接成功', 'info', function() {
						$('#save').linkbutton({
							disabled : false
						});
					});
				} else if (data.success == "failed") {
					$.messager.alert('提示', "连接失败");
				} else if (data.success == "wait") {
					testConnection();
				}
			},
			error : function() {
				$.messager.alert('提示信息', "连接失败");
			}
		// clearForm: true

		};
	}
	/*
	 * 异步提交
	 */
	$("#addInformatica").ajaxSubmit(options);
}

/*
 * 提交前，数据验证
 */
function showRequest() {
	return $("#addInformatica").valid();
}

function savedisabled(){
	$('#save').linkbutton({
		disabled : true
	});
}
