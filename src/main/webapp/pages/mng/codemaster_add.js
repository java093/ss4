//初始化状态
$(document).ready(function() {
	// 验证规则初始化
	$('#addCodeMasterForm').validate({
		rules : {
			codetype : {
				required : true,
				maxlength: 20,
				remote: {
					url: baseUrl+'/codemaster/findbytype',
					type: 'POST',
					dateType: 'json',
					data: {                     //要传递的数据
						id : 0,
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

// 插入CodeMaster数据
function addCodeMaster() {
	if (showRequest()) {
//		var options = {
//			url : baseUrl + '/codemaster/add',
//			type : "post",
//			dataType : 'json',
//			data : {
//				'codetype' : $("#codetype").val(),
//				'description' : $("#description").val(),
//				'flag' : $("#flag").val()
//			},
//			success : function(data) {
//				if (data.success) {
//					$.messager.alert('提示', '数据保存成功', 'info', function() {
//						parent.$('#codeList').datagrid('load');
//						parent.$('#jsjladdpage').dialog('close');
//					});
//				} else {
//					$.messager.alert('提示', data.exceptionMsg);
//				}
//			},
//			error : function() {
//				$.messager.alert('提示信息', "数据保存失败。。。", "error");
//			},
//			clearForm : true
//	
//		};
//		/*
//		 * 异步提交
//		 */
//		$("#addCodeMasterForm").ajaxSubmit(options);
		
		$add = {
				'codetype' : $("#codetype").val(),
				'description' : $("#description").val(),
				'flag' : $("#flag").val()
		}
		var  urlParam = baseUrl+'/codemaster/add';     // 新增操作
		$.post(urlParam,$add,function(data){
		    var data = eval( "(" + data + ")" );//json String变成对象
			if(data.success){		
					    $.messager.alert('提示信息',messageInfo.addSuccess, 'info', function(){
						parent.$("#codeList").datagrid('load');
						parent.$('#jsjladdpage').dialog('close');
					});
				}else{				
					$.messager.alert('提示信息',messageInfo.addFail,'warning');	
			}
		});  
	}
}

//调用验证方法
function showRequest() {
	return $("#addCodeMasterForm").valid();
}

// 关闭ADD界面
function closeAdd() {
	parent.$('#jsjladdpage').dialog('close');
}
