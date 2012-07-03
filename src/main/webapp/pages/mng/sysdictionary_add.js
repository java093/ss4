//初始化状态
$(document).ready(
		function() {
			// 获取dict_entry下拉框数据，加载页面时加载数据
			$.ajax({
				url : baseUrl + '/dictentry/dictentrySelect',
				dataType : "json",
				type : "POST",
				success : function(json) {
					dict_entryinit(json, "dict_entry");
				}
			});

			// 为dict_entry下拉框加载数据
			function dict_entryinit(data, selectid) {
				if (data != null) {
					var $select1 = $("#" + selectid);
					$select1.empty();
					for ( var i = 0; i < data.length; i++) {
						$("<option/>").attr("value", data[i].dict_entry).html(
								data[i].dict_entry).appendTo($select1);
					}
				}
			}

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
			$('#addSysDictionaryForm').validate({
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
								}
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

// 插入SysDictionary数据
function addSysDictionary() {
		var options = {
			url : baseUrl + '/sysdict/add',
			type : "post",
			dataType : 'json',
			data : {
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
	$("#addSysDictionaryForm").ajaxSubmit(options);
}

//调用验证方法
function showRequest(formData, jqForm, options) {
	return $("#addSysDictionaryForm").valid();
}

// 关闭ADD界面
function closeAdd() {
	parent.$('#jsjladdpage').dialog('close');
}
