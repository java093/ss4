//初始化状态
$(document).ready(
		function() {
			// 获取下拉框数据，加载页面时加载数据
			$.ajax({
				url : baseUrl + '/detail/findtype',
				dataType : "json",
				type : "POST",
				success : function(json) {
					selectinit(json, "codetype");
				}
			});

			// 为下拉框加载数据
			function selectinit(data, selectid) {
				if (data != null) {
					var $select1 = $("#" + selectid);
					$select1.empty();
					for ( var i = 0; i < data.length; i++) {
						$("<option/>").attr("value", data[i].codetype).html(
								data[i].description).appendTo($select1);
					}
				}
			}
			
			// 验证规则初始化
			$('#addCodeMasterDetailForm').validate({
				rules : {
					code : {
						required : true,
						maxlength: 10,
						remote: {
							url: baseUrl+'/detail/findbycode',
							type: 'POST',
							dateType: 'json',
							data: {                     //要传递的数据
								codetype :function() {
									return $("#codetype").val();
								},
								id : 0
							}
						  }
					},
					description : {
						required : true,
						maxlength: 20
					},
					codevalue : {
						maxlength: 10
					},
					delflag : {
						required : true,
						maxlength: 1
					}
				},
				messages: {
					code:{
			    		  remote:"该代码已存在！"
			    		} 
			     }
			});
		});

// 插入CodeMasterDetail数据
function addCodeMasterDetail() {
	var options = {
		url : baseUrl + '/detail/add',
		type : "post",
		dataType : 'json',
		data : {
			'codetype' : $("#codetype").val(),
			'code' : $("#code").val(),
			'codevalue' : $("#codevalue").val(),
			'description' : $("#description").val(),
			'flag' : $("#flag").val()
		},
		beforeSubmit : showRequest,
		success : function(data) {
			if (data.success) {
				$.messager.alert('提示', '数据保存成功', 'info', function() {
					parent.$('#codeDetailList').datagrid('load');
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
	$("#addCodeMasterDetailForm").ajaxSubmit(options);
}

// 调用验证方法
function showRequest(formData, jqForm, options) {
	return $("#addCodeMasterDetailForm").valid();
}

// 关闭ADD界面
function closeAdd() {
	parent.$('#jsjladdpage').dialog('close');
}
