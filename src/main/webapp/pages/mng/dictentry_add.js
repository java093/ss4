//初始化状态
$(document).ready(
		function() {
			var type = "DICT_TYPE";
			// 获取dict_type下拉框数据，加载页面时加载数据
			$.ajax({
				url : baseUrl + '/detail/findbycodetype',
				dataType : "json",
				data : {
					"codetype" : type
				},
				type : "POST",
				success : function(json) {
					selectinit(json, "dict_type");
				}
			});

			var level = "ACCESS_LEVEL";
			// 获取access_level下拉框数据，加载页面时加载数据
			$.ajax({
				url : baseUrl + '/detail/findbycodetype',
				dataType : "json",
				data : {
					"codetype" : level
				},
				type : "POST",
				success : function(json) {
					selectinit(json, "access_level");
				}
			});

			// 为下拉框加载数据
			function selectinit(data, selectid) {
				if (data != null) {
					var $select1 = $("#" + selectid);
					$select1.empty();
					for ( var i = 0; i < data.length; i++) {
						$("<option/>").attr("value", data[i].code).html(
								data[i].description).appendTo($select1);
					}
				}
			}
			// 验证规则初始化
			$('#addDictentryForm').validate({
				rules : {
					dict_entry : {
						required : true,
						maxlength : 13,
						remote: {
							url: baseUrl+'/dictentry/findbyentry',
							type: 'POST',
							dateType: 'json',
							data : {
								old_dict_entry : 0
							}
						}
					},
					add_enable : {
						required : true,
						number : true,
						maxlength : 1
					},
					entry_name : {
						required : true,
						maxlength : 64
					},
					defunct_ind : {
						required : true,
						maxlength : 1
					},
					remarks : {
						maxlength : 2000
					}
				},
				messages: {
					dict_entry:{
			    		  remote:"字典条目已存在！"
			    		} 
			     }
			});
		});

// 插入Dictentry数据
function addDictentry() {
	var options = {
		url : baseUrl + '/dictentry/add',
		type : "post",
		dataType : 'json',
		data : {
			'dict_entry' : $("#dict_entry").val(),
			'add_enable' : $("#add_enable").val(),
			'entry_name' : $("#entry_name").val(),
			'access_level' : $("#access_level").val(),
			'dict_type' : $("#dict_type").val(),
			'defunct_ind' : $("#defunct_ind").val(),
			'remarks' : $("#remarks").val()
		},
		beforeSubmit : showRequest,
		success : function(data) {
			if (data.success) {
				$.messager.alert('提示', '数据保存成功', 'info', function() {
					parent.$('#dictList').datagrid('load');
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
	$("#addDictentryForm").ajaxSubmit(options);
}

// 调用验证方法
function showRequest() {
	return $("#addDictentryForm").valid();
}

// 关闭ADD界面
function closeAdd() {
	parent.$('#jsjladdpage').dialog('close');
}
