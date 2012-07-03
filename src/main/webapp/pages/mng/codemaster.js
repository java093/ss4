//初始化
$(document).ready(function() {
	var urlstring = baseUrl + '/codemaster/listByName';
	/*
	 * limit :为每页的记录数(common.js参得)
	 */
	$('#codeList').datagrid({
		title : 'codemaster列表',
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
		idField : 'id',
		fitColumns : true,
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			title : '类型',
			field : 'codetype',
			width : $(this).width() * 0.1,
			sortable : false
		}, {
			title : '描述',
			field : 'description',
			width : $(this).width() * 0.1,
			sortable : false
		}, {
			title : '有效标记',
			field : 'mark',
			width : $(this).width() * 0.1,
			sortable : false
		} ] ],
		onHeaderContextMenu : function(e, field) {
			e.preventDefault();
			if (!$('#tmenu').length) {
				createColumnMenu('#codeList');
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

	var pager = $('#codeList').datagrid('getPager');
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
function codeadd() {
	// alert("add");
	createWindow("jsjladdpage", "codemaster新增", true, "normal", false, 500,
			350, "../pages/mng/codemaster_add.jsp");
}

/*
 * 打开修改页，返回刷新当前页(保留在当前页码以及查询条件不变)
 */
function codeedit() {
	// alert("update");
	var id = getSelections();
	if (id) {
		// alert("id="+id);
		var url = "../pages/mng/codemaster_edit.jsp?id=" + id;
		createWindow("jsjleditpage", "codemaster编辑", true, "normal", false,
				500, 350, url);
	}
}

/*
 * 删除选中项
 */
function deleteCodes() {
	var ids = getDeleteSelections();
	if (ids) {
		$.messager.confirm('提示信息', '您是否确认删除所选项?', function(r) {
			if (r) {
				$.ajax({
					url : baseUrl + "/codemaster/delete?ids=" + ids,
					dataType : "json",
					type : "POST",
					success : function(data) {
						if (data) {
							if (data.success) {
								$.messager.alert('提示信息', "数据删除成功!");
								$('#codeList').datagrid('load');
								clearSelect("codeList");
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
 * 获取要删除的多行
 */
function getDeleteSelections() {
	var ids = new Array();
	var rows = $('#codeList').datagrid('getSelections');
	for ( var i = 0; i < rows.length; i++) {
		ids.push(rows[i].id);
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
	var rows = $('#codeList').datagrid('getSelections');
	// alert(rows.length);
	for ( var i = 0; i < rows.length; i++) {
		ids.push(rows[i].id);
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
	// alert("ids="+ids);
	return ids; // alert(ids.join(':'));
}
