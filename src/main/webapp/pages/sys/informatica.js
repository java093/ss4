//初始化状态为“我的代办”
$(document).ready(function() {
	var urlstring = baseUrl + '/sys/list';
	/*
	 * limit :为每页的记录数(common.js参得)
	 */
	$('#eltList').datagrid({
		title : 'Informatica 系统信息',
		width : '100%',
		nowrap : false,
		striped : true,
		pageNumber : 1,
		pageList : [ pageCount[1] ], // 分页大小
		queryParams : {
			limit : pageCount[1]
		}, // 分页参数，请不要删除（每页的记录数，请看common.js）
		url : urlstring,
		sortName : 'title',
		sortOrder : 'desc',
		idField : 'id',
		fitColumns : true,
		columns : [ [ {
			field : 'ck',
			checkbox : true
		},
		// {title:'ID',field:'id',width:$(this).width()*0.06,sortable:true},
		{
			title : '名称',
			field : 'server_name',
			width : $(this).width() * 0.1,
			sortable : false
		}, {
			title : 'IP地址',
			field : 'ip',
			width : $(this).width() * 0.1,
			sortable : false
		}, {
			title : '端口',
			field : 'port',
			width : $(this).width() * 0.06,
			sortable : false
		}, {
			title : 'Domain',
			field : 'domain',
			width : $(this).width() * 0.06,
			sortable : false
		}, {
			title : '知识库',
			field : 'knowledge_base',
			width : $(this).width() * 0.1,
			sortable : false
		},  {
			title : '服务器用户名',
			field : 'user_name',
			width : $(this).width() * 0.1,
			sortable : false
		},  {
			title : '集成服务器',
			field : 'integration_server',
			width : $(this).width() * 0.1,
			sortable : false
		}, {
			title : 'workflow日志保存路径',
			field : 'wrlogpath',
			width : $(this).width() * 0.1,
			sortable : false
		}, {
			title : 'session日志保存路径',
			field : 'sesslogpath',
			width : $(this).width() * 0.1,
			sortable : false
		} ] ],
		pagination : true,
		rownumbers : true,
		onHeaderContextMenu : function(e, field) {
			e.preventDefault();
			if (!$('#tmenu').length) {
				createColumnMenu('#eltList');
			}
			$('#tmenu').menu('show', {
				left : e.pageX,
				top : e.pageY
			});
		},
		onLoadError : function() {
			$.messager.alert('提示信息', "数据加载错误。。。", "error");
		}
	});

	var pager = $('#eltList').datagrid('getPager');
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
function informaticaadd() {
	// openDialogIframe("案例分析新增","newWindow","openDialog","jsjl_add.jsp");
	createWindow("jsjladdpage", "新增 Informatica 系统参数", true, "normal", false,
			500, 450, "../pages/sys/informatica_insert.jsp");
}

/*
 * 打开编辑页，返回重新加载Grid
 */
function informaticaedit() {

	var id = getSelections();
	if (id) {
		var url = "../pages/sys/informatica_edit.jsp?id=" + id;
		createWindow("jsjleditpage", "编辑 Informatica 系统参数", true, "normal",
				false, 500, 450, url);
	}
	// openDialogIframe("案例分析新增","newWindow","openDialog","jsjl_add.jsp");

}

/*
 * 删除任务
 */
function deleteInformatica() {
	var ids = getDeleteSelections();
	if (ids) {
		$.messager.confirm('提示信息', '您是否确认删除所选项?', function(r) {
			if (r) {
				$.ajax({
					url : baseUrl + "/sys/delete?ids=" + ids,
					dataType : "json",
					type : "POST",
					success : function(data) {
						if (data) {
							if (data.success) {
								$.messager.alert('提示信息', "数据删除成功!");
								$('#eltList').datagrid('load');
								clearSelect("eltList");
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
 * 删除选中多行
 */
function getDeleteSelections() {
	var ids = new Array();
	var rows = $('#eltList').datagrid('getSelections');
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
	var rows = $('#eltList').datagrid('getSelections');
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
