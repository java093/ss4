//初始化
$(document).ready(function() {
	menuTree=$('#tree').tree({
	checkbox: false,
	//url: 'tree_data.json',
	url: 'treeList?id=0',
	onBeforeExpand:function(node,param){  
        $('#tree').tree('options').url = "treeList?id=" + node.id+"&tag="+node.attributes.tag;
    },
	onClick:function(node){
		$(this).tree('toggle', node.target);
		//alert('you click '+node.text);
	},
	onContextMenu: function(e, node){
		e.preventDefault();
		$('#tt2').tree('select', node.target);
		$('#mm').menu('show', {
			left: e.pageX,
			top: e.pageY
		});
	}
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
	 var node = $('#tree').tree('getSelected');
	 if(node==null){
		 $.messager.alert('提示信息', "请选择树级节点添加!"); 
	 }
	createWindow("jsjladdpage", "菜单新增", true, "normal", false, 500,
			350, "../pages/acct/acctmenu_add.jsp?parentId="+node.id);
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
		var url = "../pages/acct/acctmenu_edit.jsp?id="
				+ id + "&subentry=" + subentry;
		createWindow("jsjleditpage", "菜单编辑", true, "normal", false,
				500, 350, url);
	}
}

/*
 * 删除选中项
 */
function deleteAcctUser() {
	var node = $('#tree').tree('getSelected');
    if(node.id){
		$.messager.confirm('提示信息', '您是否确认删除所选项?', function(r) {
			if (r) {
				$.ajax({
					url : baseUrl + "/acctmenu/delete?id="
							+ node.id ,
					dataType : "json",
					type : "POST",
					success : function(data) {
						if (data) {
							if (data.success) {
								$.messager.alert('提示信息', "数据删除成功!");
								$('#tree').tree('reload'); 
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
	var rows = $('#list').datagrid('getSelections');
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
	var rows = $('#list').datagrid('getSelections');
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
	// alert(ids);
	return ids; // alert(ids.join(':'));
}

/*
 * 查看是否选中行的Subentry
 */
function getSubentry() {
	var ids = new Array();
	var rows = $('#list').datagrid('getSelections');
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
