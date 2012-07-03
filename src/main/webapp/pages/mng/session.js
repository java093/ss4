//初始化
$(document).ready(function() {
	var urlstring = baseUrl + '/session/listByName';
	/*
	 * limit :为每页的记录数(common.js参得)
	 */
	$('#sessionList').datagrid({
		title : '数据质量控制列表',
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
		idField : 'session_id',
		fitColumns : true,
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			title : '文件夹名称',
			field : 'subj_name',
			width : $(this).width() * 0.1,
			sortable : false
		}, {
			title : '会话名称',
			field : 'session_name',
			width : $(this).width() * 0.1,
			sortable : false
		}, {
			title : '工作流名称',
			field : 'workflow_name',
			width : $(this).width() * 0.1,
			sortable : false
		}, {
			title : '映射名称',
			field : 'mapping_name',
			width : $(this).width() * 0.1,
			sortable : false
		}, {
			title : '检查类型',
			field : 'check_type',
			width : $(this).width() * 0.1,
			sortable : false
		} ] ],
		onHeaderContextMenu : function(e, field) {
			e.preventDefault();
			if (!$('#tmenu').length) {
				createColumnMenu('#sessionList');
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

	var pager = $('#sessionList').datagrid('getPager');
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
	
	var level = "CHECK_LEVEL";
	// 获取check_level下拉框数据，加载页面时加载数据
	$.ajax({
		url : baseUrl + '/detail/findbycodetype',
		dataType : "json",
		data : {
			"codetype" : level
		},
		type : "POST",
		success : function(json) {
			selectinit(json, "check_level");
		}
	});
	
	// 为下拉框加载数据
	function selectinit(data, selectid) {
		if (data != null) {
			var $select1 = $("#" + selectid);
			$select1.empty();
			$("<option/>").attr("value", "")
					.html("请选择").appendTo($select1);
			for ( var i = 0; i < data.length; i++) {
				$("<option/>").attr("value", data[i].code).html(
						data[i].description).appendTo($select1);
			}
		}
	}
});

/*
 * 打开新增页，返回重新加载Grid
 */
function sessionadd() {
	// alert("add");
	createWindow("jsjladdpage", "Session新增", true, "normal", false, 500, 400,
			"../pages/mng/session_add.jsp");
}

/*
 * 打开修改页，返回刷新当前页(保留在当前页码以及查询条件不变)
 */
function sessionUpdate() {
	// alert("update");
	var session_id = getSelections();
	if (session_id) {
		// alert("session_id="+session_id);
		var url = "../pages/mng/session_edit.jsp?session_id=" + session_id;
		createWindow("jsjleditpage", "Session编辑", true, "normal", false, 500,
				400, url);
	}
}

/*
 * 删除选中项
 */
function deleteSession() {
	var ids = getDeleteSelections();
	if (ids) {
		$.messager.confirm('提示信息', '您是否确认删除所选项?', function(r) {
			if (r) {
				$.ajax({
					url : baseUrl + "/session/delete?ids=" + ids,
					dataType : "json",
					type : "POST",
					success : function(data) {
						if (data) {
							if (data.success) {
								$.messager.alert('提示信息', "数据删除成功!");
								$('#sessionList').datagrid('load');
								clearSelect("sessionList");
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
 * 通过check_level查询数据
 */
function queryByLevel() {
	var selectvalue = $("#check_level").val();
	var urlstring = null;
//	if (selectvalue == 0) {
//		urlstring = baseUrl + '/session/list';
//	} else {
		urlstring = baseUrl + '/session/listByName';
//	}
	$query = {
		check_level : $.trim($("#check_level").val())
	};
	var selectRows = $('#sessionList').datagrid('getSelections');
	if (selectRows.length > 0) {
		$('#sessionList').datagrid('clearSelections');
	}
	$("#sessionList").datagrid('options').url = urlstring;
	$("#sessionList").datagrid('options').queryParams = $query;
	$("#sessionList").datagrid('load');

}

/*
 * 删除选中多行
 */
function getDeleteSelections() {
	var ids = new Array();
	var rows = $('#sessionList').datagrid('getSelections');
	for ( var i = 0; i < rows.length; i++) {
		ids.push(rows[i].session_id);
	}
	if (rows.length == 0) {
		$.messager.alert('提示信息', "至少选择一项");

		return false;
	}
	// alert(ids);
	return ids;
}

/*
 * 查看是否选中行
 */
function getSelections() {
	var ids = new Array();
	var rows = $('#sessionList').datagrid('getSelections');
	// alert(rows.length);
	for ( var i = 0; i < rows.length; i++) {
		ids.push(rows[i].session_id);
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
	// alert(ids);
	return ids; // alert(ids.join(':'));
}
