//初始化状态为“我的代办”
$(document).ready(function() {
	var urlstring = baseUrl + '/db/list';
	/*
	 * limit :为每页的记录数(common.js参得)
	 */
	$('#DataBaseList').datagrid({
		title : 'DataBase 系统信息',
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
			title : '别名',
			field : 'title',
			width : $(this).width() * 0.1,
			sortable : false
		},
		// {title:'ID',field:'id',width:$(this).width()*0.06,sortable:true},
		{
			title : '知识库名称',
			field : 'dbname',
			width : $(this).width() * 0.1,
			sortable : false
		}, {
			title : '数据库名称',
			field : 'server_name',
			width : $(this).width() * 0.1,
			sortable : false
		}, {
			title : '类型',
			field : 'dbtype',
			width : $(this).width() * 0.1,
			sortable : false
		}, {
			title : '用户名',
			field : 'username',
			width : $(this).width() * 0.1,
			sortable : false
		}, {
			title : '端口',
			field : 'port',
			width : $(this).width() * 0.1,
			sortable : false
		}, {
			title : '数据库IP',
			field : 'ip',
			width : $(this).width() * 0.1,
			sortable : false
		}, {
			title : '数据库使用',
			field : 'dbuse',
			width : $(this).width() * 0.1,
			sortable : false
		} ] ],
		onHeaderContextMenu : function(e, field) {
			e.preventDefault();
			if (!$('#tmenu').length) {
				createColumnMenu('#DataBaseList');
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

	var pager = $('#DataBaseList').datagrid('getPager');
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
function dbadd() {
	// openDialogIframe("案例分析新增","newWindow","openDialog","jsjl_add.jsp");
	createWindow("jsjladdpage", "新增 DataBase 系统参数", true, "normal", false, 500,
			450, "../pages/sys/DataBase_insert.jsp");
}

/*
 * 打开编辑页，返回重新加载Grid
 */
function dbedit() {

	var id = getSelections();
	if (id) {
		var url = "../pages/sys/DataBase_edit.jsp?id=" + id;
		createWindow("jsjleditpage", "编辑 DataBase 系统参数", true, "normal", false,
				500, 450, url);
	}
	// openDialogIframe("案例分析新增","newWindow","openDialog","jsjl_add.jsp");

}

/*
 * 删除任务
 */
function deletedb() {
	var ids = getDeleteSelections();
	if (ids) {
		$.messager.confirm('提示信息', '您是否确认删除所选项?', function(r) {
			if (r) {
				$.ajax({
					url : baseUrl + "/db/delete?ids=" + ids,
					dataType : "json",
					type : "POST",
					success : function(data) {
						if (data) {
							if (data.success) {
								$.messager.alert('提示信息', "数据删除成功!");
								$('#DataBaseList').datagrid('load');
								clearSelect("DataBaseList");
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
 * 取消DataGrid中的行选择(适用于Jquery Easy Ui中的dataGrid)
 * 注意：解决了无法取消"全选checkbox"的选择,不过，前提是必须将列表展示
 * 数据的DataGrid所依赖的Table放入html文档的最前面，至少该table前没有 其他checkbox组件。
 * 
 * @paramdataTableId将要取消所选数据记录的目标table列表id
 */
function clearSelect(dataTableId) {
	$('#' + dataTableId).datagrid('clearSelections');
	// 取消选择DataGrid中的全选
	$("input[type='checkbox']").eq(0).attr("checked", false);
}

/*
 * 删除选中多行
 */
function getDeleteSelections() {
	var ids = new Array();
	var rows = $('#DataBaseList').datagrid('getSelections');
	for ( var i = 0; i < rows.length; i++) {
		ids.push(rows[i].id);
	}
	if (rows.length == 0) {
		$.messager.alert('提示信息', "至少选择一项");

		return false;
	}
	return ids;
}

/*
 * 查看是否选中行
 */
function getSelections() {
	var ids = new Array();
	var rows = $('#DataBaseList').datagrid('getSelections');
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
	return ids;
}
