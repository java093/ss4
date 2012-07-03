//初始化状态
$(document).ready(
		function() {
			// URL传参
			var id = getUrlParam("id");
			var code = null;
			$.ajax({
				url : baseUrl + '/detail/findbyid',
				dataType : "json",
				data : {
					id : id
				},
				type : "GET",
				error : function() {
					$.messager.alert('提示信息', "数据加载失败。。。", "error");
				},
				success : function(data) {
					code = data;
					$('#updateForm').form('load', code);
					$.ajax({
						url : baseUrl + '/detail/findtype',
						dataType : "json",
						type : "POST",
						success : function(json) {
							data = json;
							selectinit(data, "codetype");
							$("#codetype").val(code.codetype);
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
						$("<option/>").attr("value", data[i].codetype).html(
								data[i].description).appendTo($select1);
					}
				}
			}

			// 验证规则初始化
			$('#updateForm').validate({
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
								id : id
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

/*
 * 新增,清空form 以及数据从第一页重新加载. 异步提交服务器，保存数据
 */
function codeDetailUpdate() {
	// URL传参
	var id = getUrlParam("id");
	var options = {
		url : baseUrl + '/detail/update',
		type : "post",
		dataType : 'json',
		data : {
			'id' : id,
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
	$("#updateForm").ajaxSubmit(options);
}

//调用验证方法
function showRequest() {
	return $("#updateForm").valid();
}

/*
 * 关闭弹出页面
 */
function closeWin() {
	parent.$('#jsjleditpage').dialog('close');
}
