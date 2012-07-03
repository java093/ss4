//初始化状态
$(document).ready(
		function() {
			var da = null;
			var sub = null;
			// 获取一级下拉数据，加载页面时加载数据
			$.ajax({
				url : baseUrl + '/dictentry/dictentrySelect',
				dataType : "json",
				type : "POST",
				success : function(json) {
					da = json;
					selectinit(da, "tran_dict_code");
				}
			});
			// 获取二级下拉的数据
			function select2datasouse(shenid) {
				$.ajax({
					type : "post",
					data : {
						"dict_entry" : shenid
					},
					url : baseUrl + '/dictentry/findSubentry',
					success : function(json) {
						sub = json;
						selectinit2(sub, "tran_code");
					},
					dataType : "json"
				});
			}
			// 第一个下拉的change事件绑定
			$("#tran_dict_code").change(function() {
				var select1value = $("#tran_dict_code").val();
				var index = $("#tran_dict_code").get(0).selectedIndex;
				// if (index != 0) {
				$(':input[name=tran_dict_desc]').val(da[index].entry_name);
				// } else {
				// $(':input[name=tran_dict_desc]').val("");
				// }
				// $(':input[name=tran_desc]').val("");
				select2datasouse(select1value);
			});
			// 第二个下拉的change事件绑定
			$("#tran_code").change(function() {
				var index = $("#tran_code").get(0).selectedIndex;
				// if (index != 0) {
				$(':input[name=tran_desc]').val(sub[index].dict_prompt);
				// } else {
				// $(':input[name=tran_desc]').val("");
				// }
			});
			// 为第一个下拉加载数据
			function selectinit(data, selectid) {
				if (data != null) {
					var $select1 = $("#" + selectid);
					$select1.empty();
					// $("<option/>").attr("value", "0").html("---请选择---")
					// .appendTo($select1);
					for ( var i = 0; i < data.length; i++) {
						$("<option/>").attr("value", data[i].dict_entry).html(
								data[i].dict_entry).appendTo($select1);
					}
					var select1value = $("#tran_dict_code").val();
					var index = $("#tran_dict_code").get(0).selectedIndex;
					$(':input[name=tran_dict_desc]').val(da[index].entry_name);
					select2datasouse(select1value);
				}
			}
			// 为第二个下拉加载数据
			function selectinit2(data, selectid) {
				if (data != null) {
					var $select1 = $("#" + selectid);
					$select1.empty();
					// $("<option/>").attr("value", "0").html("---请选择---")
					// .appendTo($select1);
					for ( var i = 0; i < data.length; i++) {
						$("<option/>").attr("value", data[i].subentry).html(
								data[i].subentry).appendTo($select1);
					}
					var index = $("#tran_code").get(0).selectedIndex;
					$(':input[name=tran_desc]').val(sub[index].dict_prompt);
				}
			}

			$('#addCon').validate({
				rules : {
					sou_code : {
						required : true,
						number : true,
						maxlength: 2
					},
					sou_desc : {
						required : true,
						maxlength: 100
					},
					sou_dict_code : {
						required : true,
						number : true,
						maxlength: 10
					},
					sou_dict_desc : {
						required : true,
						maxlength: 100
					},
					sou_sub_dict_code : {
						required : true,
						number : true,
						maxlength: 8,
						remote: {
							url: baseUrl+'/con/findbycode',
							type: 'POST',
							dateType: 'json',
							data: {                     //要传递的数据
								sou_code :function() {
									return $("#sou_code").val();
								},
								sou_dict_code :function() {
									return $("#sou_dict_code").val();
								}
							}
						  }
					},
					sou_sub_dict_desc : {
						required : true,
						maxlength: 100
					},
					tran_dict_desc : {
						required : true,
						maxlength: 100
					},
					tran_desc : {
						required : true,
						maxlength: 100
					},
					defunct_ind : {
						required : true,
						maxlength : 1
					},
					remarks : {
						maxlength: 2000
					}
				},
				messages: {
					sou_sub_dict_code:{
			    		  remote:"该系列已存在！"
			    		} 
			     }
			});
		});

// 插入ConTranDict数据
function addConTranDict() {
//	var options = {
//		url : baseUrl + '/con/add',
//		type : "post",
//		dataType : 'json',
//		data : {
//			'sou_code' : $("#sou_code").val(),
//			'sou_desc' : $("#sou_desc").val(),
//			'sou_dict_code' : $("#sou_dict_code").val(),
//			'sou_dict_desc' : $("#sou_dict_desc").val(),
//			'sou_sub_dict_code' : $("#sou_sub_dict_code").val(),
//			'sou_sub_dict_desc' : $("#sou_sub_dict_desc").val(),
//			'tran_dict_code' : $('#tran_dict_code').val(),
//			'tran_dict_desc' : $("#tran_dict_desc").val(),
//			'tran_code' : $("#tran_code").val(),
//			'tran_desc' : $("#tran_desc").val(),
//			'defunct_ind' : $("#defunct_ind").val(),
//			'remarks' : $("#remarks").val()
//		},
//		beforeSubmit : showRequest,
//		success : function(data) {
//			if (data.success) {
//				$.messager.alert('提示', '数据保存成功', 'info', function() {
//					parent.$('#conList').datagrid('load');
//					parent.$('#jsjladdpage').dialog('close');
//				});
//			} else {
//				$.messager.alert('提示', data.exceptionMsg);
//			}
//		},
//		error : function() {
//			$.messager.alert('提示信息', "数据保存失败。。。", "error");
//		},
//		clearForm : true
//
//	};
//	/*
//	 * 异步提交
//	 */
//	$("#addCon").ajaxSubmit(options);
	if(showRequest()){
		$add = {
				'sou_code' : $("#sou_code").val(),
				'sou_desc' : $("#sou_desc").val(),
				'sou_dict_code' : $("#sou_dict_code").val(),
				'sou_dict_desc' : $("#sou_dict_desc").val(),
				'sou_sub_dict_code' : $("#sou_sub_dict_code").val(),
				'sou_sub_dict_desc' : $("#sou_sub_dict_desc").val(),
				'tran_dict_code' : $('#tran_dict_code').val(),
				'tran_dict_desc' : $("#tran_dict_desc").val(),
				'tran_code' : $("#tran_code").val(),
				'tran_desc' : $("#tran_desc").val(),
				'defunct_ind' : $("#defunct_ind").val(),
				'remarks' : $("#remarks").val()
		}
		var  urlParam = baseUrl+'/con/add';     // 新增操作
		$.post(urlParam,$add,function(data){
		    var data = eval( "(" + data + ")" );//json String变成对象
			if(data.success){		
					    $.messager.alert('提示信息','数据添加成功', 'info', function(){
						parent.$("#conList").datagrid('load');
						parent.$('#jsjladdpage').dialog('close');
					});
				}else{				
					$.messager.alert('提示信息',messageInfo.addFail,'warning');	
			}
		}); 
	}
}

// 调用验证方法
function showRequest() {
	return $("#addCon").valid();
}

// 关闭ADD界面
function closeAdd() {
	parent.$('#jsjladdpage').dialog('close');
}
