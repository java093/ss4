//初始化状态
$(document).ready(function() {
	// URL传参
	var id = getUrlParam("id");
	$.ajax({
		url : baseUrl + '/codemaster/findbyid',
		dataType : "json",
		data : {
			id : id
		},
		type : "GET",
		error : function() {
			$.messager.alert('提示信息', "数据加载失败。。。", "error");
		},
		success : function(data) {
			$('#updateForm').form('load', data);
		}
	});
	// 验证规则初始化
	$('#updateForm').validate({
		rules : {
			codetype : {
				required : true,
				maxlength: 20,
				remote: {
					url: baseUrl+'/codemaster/findbytype',
					type: 'POST',
					dateType: 'json',
					data : {
						id : id
					}
				}
			},
			description : {
				required : true,
				maxlength: 20
			},
			flag : {
				required : true,
				number: true,
				maxlength: 1
			}
		},
		messages: {
			codetype:{
	    		  remote:"类型已存在！"
	    		} 
	     } 
	});
});

/*
 * 新增,清空form 以及数据从第一页重新加载. 异步提交服务器，保存数据
 */
function codeUpdate() {
//	// URL传参
//	var id = getUrlParam("id");
//	var options = {
//		url : baseUrl + '/codemaster/update',
//		type : "post",
//		dataType : 'json',
//		data : {
//			'id' : id,
//			'codetype' : $("#codetype").val(),
//			'description' : $("#description").val(),
//			'flag' : $("#flag").val()
//		},
//		beforeSubmit : showRequest,
//		success : function(data) {
//			if (data.success) {
//				$.messager.alert('提示', '数据保存成功', 'info', function() {
//					parent.$('#codeList').datagrid('load');
//					parent.$('#jsjleditpage').dialog('close');
//				});
//			} else {
//				$.messager.alert('提示', data.exceptionMsg);
//			}
//		},
//		error : function() {
//			$.messager.alert('提示信息', "数据保存失败。。。", "error");
//		},
//		clearForm : true
//	};
//
//	/*
//	 * 异步提交
//	 */
//	$("#updateForm").ajaxSubmit(options);
	if(showRequest()){
		var id = getUrlParam("id");
		$add = {
				'id' : id,
				'codetype' : $("#codetype").val(),
				'description' : $("#description").val(),
				'flag' : $("#flag").val()
		}
		var  urlEdit = baseUrl+'/codemaster/update';//编辑操作
		$.post(urlEdit,$add,function(data){
		    var data = eval( "(" + data + ")" );//json String变成对象
			if(data.success){		
					    $.messager.alert('提示信息',messageInfo.editSuccess, 'info', function(){
						parent.$("#codeList").datagrid('load');
						parent.$('#jsjleditpage').dialog('close');
					});
				}else{				
					
					$.messager.alert('提示信息',messageInfo.editFail,'warning');		
			}
		}); 
	}
}

// 调用验证方法
function showRequest() {
	return $("#updateForm").valid();
}

/*
 * 关闭弹出页面
 */
function closeWin() {
	parent.$('#jsjleditpage').dialog('close');
}
