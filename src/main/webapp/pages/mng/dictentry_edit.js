//初始化状态
$(document).ready(
		function() {
			// URL传参
			var old_dict_entry = getUrlParam("dict_entry");
//			alert("~~~~~"+old_dict_entry+"~~~~~~~~~~~~~");
			$.ajax({
				url : baseUrl + '/dictentry/findbyid',
				dataType : "json",
				data : {
					dict_entry : old_dict_entry
				},
				type : "GET",
				error : function() {
					$.messager.alert('提示信息', "数据加载失败。。。", "error");
				},
				success : function(data) {
					$('#updateDictentryForm').form('load', data);
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
							$("#dict_type").val(data.dict_type);
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
							$("#access_level").val(data.access_level);
						}
					});
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
			$('#updateDictentryForm').validate({
				rules : {
					dict_entry : {
						required : true,
						maxlength : 13,
						remote: {
							url: baseUrl+'/dictentry/findbyentry',
							type: 'POST',
							dateType: 'json',
							data : {
								old_dict_entry : old_dict_entry
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

/*
 * 新增,清空form 以及数据从第一页重新加载. 异步提交服务器，保存数据
 */
function editDictentry() {
	// URL传参
	var old_dict_entry = getUrlParam("dict_entry");
	var options = {
		url : baseUrl + '/dictentry/update',
		type : "post",
		dataType : 'json',
		data : {
			'old_dict_entry' : old_dict_entry,
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

	/*
	 * 异步提交
	 */
	$("#updateDictentryForm").ajaxSubmit(options);
}

// 调用验证方法
function showRequest() {
	return $("#updateDictentryForm").valid();
}

/*
 * 关闭弹出页面
 */
function closeWin() {
	parent.$('#jsjleditpage').dialog('close');
}
