//初始化状态
$(document).ready(
		function() {
			// URL传参
			var dict_entry = getUrlParam("dict_entry");
			var subentry = getUrlParam("subentry");
			$.ajax({
				url : baseUrl + '/sysdict/findbyid',
				dataType : "json",
				data : {
					dict_entry : dict_entry,
					subentry : subentry
				},
				type : "GET",
				error : function() {
					$.messager.alert('提示信息', "数据加载失败。。。", "error");
				},
				success : function(data) {
					$('#updateSysdictionaryForm').form('load', data);
					// 获取dict_entry下拉框数据，加载页面时加载数据
					$.ajax({
						url : baseUrl + '/dictentry/dictentrySelect',
						dataType : "json",
						type : "POST",
						success : function(json) {
							dictentryinit(json, "dict_entry");
							$("#dict_entry").val(dict_entry);
						}
					});

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
			// 为dict_entry下拉框加载数据
			function dictentryinit(data, selectid) {
				if (data != null) {
					var $select1 = $("#" + selectid);
					// $select1.empty();
					for ( var i = 0; i < data.length; i++) {
						$("<option/>").attr("value", data[i].dict_entry).html(
								data[i].dict_entry).appendTo($select1);
					}
				}
			}

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
			$('#updateSysdictionaryForm').validate({
				rules : {
					subentry : {
						required : true,
						number: true,
						maxlength: 8,
						remote: {
							url: baseUrl+'/sysdict/findbysub',
							type: 'POST',
							dateType: 'json',
							data: {                     //要传递的数据
								dict_entry :function() {
									return $("#dict_entry").val();
								},
								old_dict_entry : dict_entry,
								old_subentry : subentry
							}
						  }
					},
					dict_prompt : {
						required : true,
						maxlength: 64
					},
					defunct_ind : {
						required : true,
						maxlength: 1
					},
					remarks : {
						maxlength: 2000
					}
				},
				messages: {
					subentry:{
			    		  remote:"字典子项已存在！"
			    		} 
			     }
			});
		});

/*
 * 新增,清空form 以及数据从第一页重新加载. 异步提交服务器，保存数据
 */
function editSysdictionary() {
	var dict_entry = getUrlParam("dict_entry");
	var subentry = getUrlParam("subentry");
		// URL传参
		var options = {
			url : baseUrl + '/sysdict/update',
			type : "post",
			dataType : 'json',
			data : {
				'old_dict_entry' : dict_entry,
				'old_subentry' : subentry,
				'branch_no' : $("#branch_no").val(),
				'dict_entry' : $("#dict_entry").val(),
				'dict_type' : $("#dict_type").val(),
				'subentry' : $("#subentry").val(),
				'access_level' : $("#access_level").val(),
				'dict_prompt' : $("#dict_prompt").val(),
				'defunct_ind' : $("#defunct_ind").val(),
				'remarks' : $("#remarks").val()
			},
			beforeSubmit : showRequest,
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

	/*
	 * 异步提交
	 */
	$("#updateSysdictionaryForm").ajaxSubmit(options);
}

//调用验证方法
function showRequest(formData, jqForm, options) {
	return $("#updateSysdictionaryForm").valid();
}

/*
 * 关闭弹出页面
 */
function closeWin() {
	parent.$('#jsjleditpage').dialog('close');
}
