//初始化
$(document).ready(function() {
	var urlstring = baseUrl + '/acctuser/list';
	/*
	 * limit :为每页的记录数(common.js参得)
	 */
	$('#sysdictList').datagrid({
		title : '人员列表',
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
			title : '登陆名',
			field : 'login_name',
			width : $(this).width() * 0.1
		}, {
			title : '用户名',
			field : 'name',
			width : $(this).width() * 0.1
		}] ],
		onHeaderContextMenu : function(e, field) {
			e.preventDefault();
			if (!$('#tmenu').length) {
				createColumnMenu('#sysdictList');
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

	var pager = $('#sysdictList').datagrid('getPager');
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
function add() {
	createWindow("jsjladdpage", "人员新增", true, "normal", false, 500,
			350, "../pages/acct/acctuser_add.jsp");
}

/*
 * 打开修改页，返回刷新当前页(保留在当前页码以及查询条件不变)
 */
function edit() {
	// alert("update");
	var id = getSelections();
	var subentry = getSubentry();
	if (id) {
		// alert("id="+id);
		var url = "../pages/acct/acctuser_edit.jsp?id="
				+ id + "&subentry=" + subentry;
		createWindow("jsjleditpage", "人员编辑", true, "normal", false,
				500, 350, url);
	}
}

function updatePwd() {
	// alert("update");
	var id = getSelections();
	var subentry = getSubentry();
	if (id) {
		// alert("id="+id);
		var url = "../pages/acct/acctuser_uppwd.jsp?id="
				+ id + "&subentry=" + subentry;
		createWindow("jsjleditpage", "修改密码", true, "normal", false,
				500, 350, url);
	}
}

/*
 * 删除选中项
 */
function deleteAcctUser() {
	var ids = getDeleteSelections();
    if(ids){
		$.messager.confirm('提示信息', '您是否确认删除所选项?', function(r) {
			if (r) {
				$.ajax({
					url : baseUrl + "/acctuser/delete?ids="
							+ ids ,
					dataType : "json",
					type : "POST",
					success : function(data) {
						if (data) {
							if (data.success) {
								$.messager.alert('提示信息', "数据删除成功!");
								$('#sysdictList').datagrid('load');
								clearSelect("sysdictList");
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
 * 获取选中多行
 */
function getDeleteSelections() {
	var ids = new Array();
	var rows = $('#sysdictList').datagrid('getSelections');
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
	var rows = $('#sysdictList').datagrid('getSelections');
//	 alert(rows.length);
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
	// alert(ids);
	return ids; // alert(ids.join(':'));
}

/*
 * 查看是否选中行的Subentry
 */
function getSubentry() {
	var ids = new Array();
	var rows = $('#sysdictList').datagrid('getSelections');
	for ( var i = 0; i < rows.length; i++) {
		ids.push(rows[i].subentry);
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
	// alert(ids);
	return ids;
}


/**
 * 用户授权
 * @returns
 */
function auth() {	
	var id = getSelections();
	if (id) {
		// alert("id="+id);
		var url = "../pages/acct/acctuser_auth.jsp?id="
				+ id ;
		createWindow("jsjleditpage", "人员授权", true, "normal", false,
				500, 350, url);
	}
}

