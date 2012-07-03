//初始化
$(document).ready(function() {
	var urlstring = baseUrl + '/con/listByName';
	/*
	 * limit :为每页的记录数(common.js参得)
	 */
	$('#conList').datagrid({
		title : 'con_tran_dict列表',
		width : '100%',
		nowrap : false,
		striped : true,
		pageNumber : 1,
		pageList : [ pageCount[1] ], // 分页大小
		queryParams : {
			limit : pageCount[1],
			'flowstatus' : 0
		}, // 分页参数，请不要删除（每页的记录数，请看common.js）
		url : urlstring,
		sortName : 'title',
		sortOrder : 'desc',
		idField : 'sou_sub_dict_desc',
		fitColumns : true,
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			title : '源系统编号',
			field : 'sou_code',
			width : $(this).width() * 0.07,
			sortable : false
		}, {
			title : '源系统说明',
			field : 'sou_desc',
			width : $(this).width() * 0.08,
			sortable : false
		}, {
			title : '源系统字典分类',
			field : 'sou_dict_code',
			width : $(this).width() * 0.08,
			sortable : false
		}, {
			title : '源系统字典分类说明',
			field : 'sou_dict_desc',
			width : $(this).width() * 0.1,
			sortable : false
		}, {
			title : '源系统字典编码',
			field : 'sou_sub_dict_code',
			width : $(this).width() * 0.08,
			sortable : false
		}, {
			title : '源系统字典编码说明',
			field : 'sou_sub_dict_desc',
			width : $(this).width() * 0.1,
			sortable : false
		}, {
			title : '统一字典分类',
			field : 'tran_dict_code',
			width : $(this).width() * 0.1,
			sortable : false
		}, {
			title : '纺一字典分类说明',
			field : 'tran_dict_desc',
			width : $(this).width() * 0.1,
			sortable : false
		}, {
			title : '统一编码',
			field : 'tran_code',
			width : $(this).width() * 0.05,
			sortable : false
		}, {
			title : '统一编码说明',
			field : 'tran_desc',
			width : $(this).width() * 0.1,
			sortable : false
		}, {
			title : '有效标志',
			field : 'mark',
			width : $(this).width() * 0.05,
			sortable : false
		}, {
			title : '备注',
			field : 'remarks',
			width : $(this).width() * 0.07,
			sortable : false
		}, {
			title : '创建人',
			field : 'createdBy',
			width : $(this).width() * 0.05,
			sortable : false
		}, {
			title : '创建时间',
			field : 'crteat_time',
			width : $(this).width() * 0.1,
			sortable : false
		} ] ],
		onHeaderContextMenu : function(e, field) {
			e.preventDefault();
			if (!$('#tmenu').length) {
				createColumnMenu('#conList');
			}
			$('#tmenu').menu('show', {
				left : e.pageX,
				top : e.pageY
			});
		},
		onLoadError : function() {
			$.messager.alert('提示信息', "数据加载错误。。。", "error");
		},
		pagination : true,
		rownumbers : true

	});

	var pager = $('#conList').datagrid('getPager');
	$(pager).pagination({
		showPageList : false
	});

	/*
	 * 字符转JSON
	 */
	function strToJson(str) {
		var json = eval(str);
		return json;
	}
});

/*
 * 打开新增页，返回重新加载Grid
 */
function conadd() {
	// alert("add");
	createWindow("jsjladdpage", "con_tran_dict新增", true, "normal", false, 500,
			400, "../pages/mng/con_tran_dict_add.jsp");
}

/*
 * 打开修改页，返回刷新当前页(保留在当前页码以及查询条件不变)
 */
function conedit() {
	// alert("update");
	var sou_code = getSelections();
	var sou_dict_code = getSou_dict_code();
	var sou_sub_dict_code = getSou_sub_dict_code();
	if (sou_code) {
		// alert("id="+id);
		var url = "../pages/mng/con_tran_dict_edit.jsp?sou_code=" + sou_code
				+ "&sou_dict_code=" + sou_dict_code + "&sou_sub_dict_code="
				+ sou_sub_dict_code;
		createWindow("jsjleditpage", "con_tran_dict编辑", true, "normal", false,
				500, 400, url);
	}
}

/*
 * 删除选中项
 */
function deleteCons() {
	var sou_codes = getDeleteSelections();
	var sou_dict_codes = getDeleteSou_dict_code();
	var sou_sub_dict_codes = getDeleteSou_sub_dict_code();
	if (sou_codes) {
		$.messager.confirm('提示信息', '您是否确认删除所选项?', function(r) {
			if (r) {
				$.ajax({
					url : baseUrl + "/con/delete?sou_codes=" + sou_codes
							+ "&sou_dict_codes=" + sou_dict_codes
							+ "&sou_sub_dict_codes=" + sou_sub_dict_codes,
					dataType : "json",
					type : "POST",
					success : function(data) {
						if (data) {
							if (data.success) {
								$.messager.alert('提示信息', "数据删除成功!");
								$('#conList').datagrid('load');
								clearSelect("conList");
							} else {
								$.messager.alert('提示信息', "数据删除失败!");
							}

						}
					}
				});
			}
		});
	}
}

/**
*取消DataGrid中的行选择(适用于Jquery Easy Ui中的dataGrid)
*注意：解决了无法取消"全选checkbox"的选择,不过，前提是必须将列表展示
*数据的DataGrid所依赖的Table放入html文档的最前面，至少该table前没有
*其他checkbox组件。
*
*@paramdataTableId将要取消所选数据记录的目标table列表id
*/
function clearSelect(dataTableId) {
    $('#' + dataTableId).datagrid('clearSelections');
    //取消选择DataGrid中的全选
    $("input[type='checkbox']").eq(0).attr("checked", false);
}

/*
 * 获取选中的多行
 */
function getDeleteSelections() {
	var sou_codes = new Array();
	var rows = $('#conList').datagrid('getSelections');
	for ( var i = 0; i < rows.length; i++) {
		sou_codes.push(rows[i].sou_code);
	}
	if (rows.length == 0) {
		$.messager.alert('提示信息', "至少选择一项");
		return false;
	}
	// alert(sou_codes);
	return sou_codes;
}

/*
 * 获取选中多行的Sou_dict_code
 */
function getDeleteSou_dict_code() {
	var sou_dict_codes = new Array();
	var rows = $('#conList').datagrid('getSelections');
	for ( var i = 0; i < rows.length; i++) {
		sou_dict_codes.push(rows[i].sou_dict_code);
	}
	if (rows.length == 0) {
		// $.messager.alert('提示信息',"至少选择一项");
		return false;
	}
	// alert(sou_dict_codes);
	return sou_dict_codes;
}

/*
 * 获取选中多行的Sou_sub_dict_code
 */
function getDeleteSou_sub_dict_code() {
	var sou_sub_dict_codes = new Array();
	var rows = $('#conList').datagrid('getSelections');
	for ( var i = 0; i < rows.length; i++) {
		sou_sub_dict_codes.push(rows[i].sou_sub_dict_code);
	}
	if (rows.length == 0) {
		// $.messager.alert('提示信息',"至少选择一项");
		return false;
	}
	// alert(sou_sub_dict_codes);
	return sou_sub_dict_codes;
}

/*
 * 查看是否选中行
 */
function getSelections() {
	var sou_code = new Array();
	var rows = $('#conList').datagrid('getSelections');
	// alert(rows.length);
	for ( var i = 0; i < rows.length; i++) {
		sou_code.push(rows[i].sou_code);
	}
	if (rows.length > 1) {
		$.messager.alert('提示信息', "您只能选择一项");
		return false;
	} else {
		if (rows.length > 0) {
		} else {
			$.messager.alert('提示信息', "请选择项");
			return false;
		}
	}
	// alert("sou_code="+sou_code);
	return sou_code; // alert(ids.join(':'));
}

/*
 * 查看是否选中行的Sou_dict_code
 */
function getSou_dict_code() {
	var sou_dict_code = new Array();
	var rows = $('#conList').datagrid('getSelections');
	// alert(rows.length);
	for ( var i = 0; i < rows.length; i++) {
		sou_dict_code.push(rows[i].sou_dict_code);
	}
	if (rows.length > 1) {
		// $.messager.alert('提示信息', "您只能选择一项");
		return false;
	} else {
		if (rows.length > 0) {
		} else {
			// $.messager.alert('提示信息', "请选择项");
			return false;
		}
	}
	// alert("sou_dict_code=="+sou_dict_code);
	return sou_dict_code; // alert(ids.join(':'));
}

/*
 * 查看是否选中行的Sou_sub_dict_code
 */
function getSou_sub_dict_code() {
	var sou_sub_dict_code = new Array();
	var rows = $('#conList').datagrid('getSelections');
	// alert(rows.length);
	for ( var i = 0; i < rows.length; i++) {
		sou_sub_dict_code.push(rows[i].sou_sub_dict_code);
	}
	if (rows.length > 1) {
		// $.messager.alert('提示信息', "您只能选择一项");
		return false;
	} else {
		if (rows.length > 0) {
		} else {
			// $.messager.alert('提示信息', "请选择项");
			return false;
		}
	}
	// alert("sou_sub_dict_code=="+sou_sub_dict_code);
	return sou_sub_dict_code; // alert(ids.join(':'));
}