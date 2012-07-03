//初始化状态
$(document)
		.ready(
				function() {
					// URL传参
					var sou_code = getUrlParam("sou_code");
					var sou_dict_code = getUrlParam("sou_dict_code");
					var sou_sub_dict_code = getUrlParam("sou_sub_dict_code");
					var mail = null;
					var da = null;
					var sub = null;
					var isfirst = false;
					$.ajax({
						url : baseUrl + '/con/findbyid',
						dataType : "json",
						data : {
							sou_code : sou_code,
							sou_dict_code : sou_dict_code,
							sou_sub_dict_code : sou_sub_dict_code
						},
						type : "GET",
						error : function() {
							$.messager.alert('提示信息', "数据加载失败。。。", "error");
						},
						success : function(data) {
							mail = data;
							$('#updateConForm').form('load', mail);
							var trandictcode = mail.tran_dict_code;
							isfirst = true;
							select2datasouse(trandictcode);
							$.ajax({
								url : baseUrl + '/dictentry/dictentrySelect',
								dataType : "json",
								type : "POST",
								success : function(json) {
									da = json;
									// 为第一个下拉加载数据
									selectinit(da, "tran_dict_code");
									$("#tran_dict_code").val(
											mail.tran_dict_code);

								}
							});
						}
					});
					// 第一个下拉的onchange事件绑定
					$("#tran_dict_code")
							.change(
									function() {
										var select1value = $("#tran_dict_code")
												.val();
										var index = $("#tran_dict_code").get(0).selectedIndex;
										$(':input[name=tran_dict_desc]').val(
												da[index].entry_name);
										select2datasouse(select1value);
									});
					// 第二个下拉的onchange事件绑定
					$("#tran_code")
							.change(
									function() {
										var index = $("#tran_code").get(0).selectedIndex;
										$(':input[name=tran_desc]').val(
												sub[index].dict_prompt);
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
								if (isfirst) {
									$("#tran_code").val(mail.tran_code);
									isfirst = false;
								}
							},
							dataType : "json"
						});
					}
					// 为第一个下拉加载数据
					function selectinit(data, selectid) {
						if (data != null) {
							var $select1 = $("#" + selectid);
							// $select1.empty();
							for ( var i = 0; i < data.length; i++) {
								$("<option/>")
										.attr("value", data[i].dict_entry)
										.html(data[i].dict_entry).appendTo(
												$select1);
							}
						}
					}
					// 为第二个下拉加载数据
					function selectinit2(data, selectid) {
						if (data != null) {
							var $select1 = $("#" + selectid);
							$select1.empty();
							for ( var i = 0; i < data.length; i++) {
								$("<option/>").attr("value", data[i].subentry)
										.html(data[i].subentry).appendTo(
												$select1);
							}
							if (!isfirst) {
								var index = $("#tran_code").get(0).selectedIndex;
								$(':input[name=tran_desc]').val(
										sub[index].dict_prompt);
							}
						}
					}

					$('#updateConForm').validate({
						rules : {
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
							}
						}
					});
				});

/*
 * 新增,清空form 以及数据从第一页重新加载. 异步提交服务器，保存数据
 */
function editCon() {
	// URL传参
	var options = {
		url : baseUrl + '/con/update',
		type : "post",
		dataType : 'json',
		data : {
			'sou_code' : $("#sou_code").val(),
			'sou_desc' : $("#sou_desc").val(),
			'sou_dict_code' : $("#sou_dict_code").val(),
			'sou_dict_desc' : $("#sou_dict_desc").val(),
			'sou_sub_dict_code' : $("#sou_sub_dict_code").val(),
			'sou_sub_dict_desc' : $("#sou_sub_dict_desc").val(),
			'tran_dict_code' : $("#tran_dict_code").val(),
			'tran_dict_desc' : $("#tran_dict_desc").val(),
			'tran_code' : $("#tran_code").val(),
			'tran_desc' : $("#tran_desc").val(),
			'defunct_ind' : $("#defunct_ind").val(),
			'remarks' : $("#remarks").val()
		},
		beforeSubmit : showRequest,
		success : function(data) {
			if (data.success) {
				$.messager.alert('提示', '数据保存成功', 'info', function() {
					parent.$('#conList').datagrid('load');
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
	$("#updateConForm").ajaxSubmit(options);
}

// 调用验证方法
function showRequest() {
	return $("#updateConForm").valid();
}

/*
 * 关闭弹出页面
 */
function closeWin() {
	parent.$('#jsjleditpage').dialog('close');
}
