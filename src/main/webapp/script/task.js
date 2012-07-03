$(function(){
	//加载表格数据
	ajaxTable();
	
	//初始化弹出层
	setDialog_add();
	closeDialog_add();
	setDialog_edit();
	closeDialog_edit();
});

/**--------------table------------------**/
/**
 * 加载表格数据
 */
function ajaxTable(){
	//加载表格
	$('#loginInfoTable').datagrid({
		toolbar:[{//正上方工具栏
			text:'添加新用户',
			iconCls:'icon-add',
			handler:function(){
				//点击工具栏运行的js方法
				openDialog_add();
			}
		},'-',{
				text:'批量删除',
				iconCls:'icon-cancel',
				handler:function(){
					batch('delete');
				}
		},'-',{
			text:'批量启用',
			iconCls:'icon-ok',
			handler:function(){
				batch('available');
			}
		},'-',{
			text:'批量禁用',
			iconCls:'icon-remove',
			handler:function(){
				batch('invalid');
			}
		}],
		pageNumber : 1,
		loadMsg:'数据加载中,请稍后...',
		pageList:[10,30,50,100], //设置每页显示多少条
		onLoadError:function(){
			alert('数据加载失败!');
		},
		queryParams:{//查询条件
		},
		onClickRow:function(rowIndex, rowData){
			//取消选择某行后高亮
			$('#loginInfoTable').datagrid('unselectRow', rowIndex);
		},
		onLoadSuccess:function(){
			var value = $('#loginInfoTable').datagrid('getData')['errorMsg'];
			if(value!=null){
				alert("错误消息:"+value);
			}
		}
	}).datagrid('acceptChanges');
}
/**
 * 设置操作列的信息
 * 参数说明
          value 这个可以不管，但是要使用后面 row 和index 这个参数是必须的
          row   当前行的数据
          index 当前行的索引  从0 开始
 */
function optFormater(value,row,index){
	var loginId = row.loginId;
	var loginCode = row.loginCode;
	var id_code = loginId+",'"+loginCode+"'";
	var opt = '';
	if(row.statuId==0){
		opt = '<a href="#" onclick="doUsable('+row.loginId+')">启用</a> | ';
	}else if(row.statuId==1){
		opt = '<a href="#" onclick="doForbidden('+row.loginId+')">禁用</a> |  ';
	}
	var detail = '<a href="#" onclick="goDetail('+row.loginId+')">详细</a> |  ';
	var edit = '<a href="javascript:openDialog_edit('+id_code+')">编辑</a> | ';
	var del = '<a href="#" onclick="doDel('+row.loginId+')">删除</a>';
	return opt+edit+del;
};
//刷新表格
function reloadTable(){
	$('#loginInfoTable').datagrid('reload');
}


/**--------------添加操作弹出框------------------**/
//设置弹出框的属性
function setDialog_add(){
	$('#loginInfoAdd').dialog({
		title : '用户添加',
		modal: true,         	//模式窗口：窗口背景不可操作
		collapsible : true,  	//可折叠，点击窗口右上角折叠图标将内容折叠起来
		resizable : true,    	//可拖动边框大小
		onClose : function(){   //继承自panel的关闭事件
			loginInfoAddReset();
		}
	});
}
//打开对话框
function openDialog_add(){
	$('#loginInfoAdd').dialog('open');
}
//关闭对话框
function closeDialog_add(){
	$('#loginInfoAdd').dialog('close');
}
//执行用户添加操作
function loginInfoAdd(){
	var validateResult = true;
	//easyui 表单验证
	$('#table_loginInfoAdd input').each(function () {
		if ($(this).attr('required') || $(this).attr('validType')) {
			if (!$(this).validatebox('isValid')) {
				//如果验证不通过，则返回false
				validateResult = false;
				return;
		    }
		}
    });
	if(validateResult==false){
		return;
	}
	
	$.ajax({
		async : false,
		cache:false,
		type: 'POST',
		dataType : "json",
		data : {
			"loginInfo.loginCode" : $("#loginInfoAdd_loginCode").val(),
			"loginInfo.password" : $("#loginInfoAdd_password").val()
		},
		url: root+'/ospm/loginInfo/doLoginInfoAdd.jhtml',//请求的action路径
		error: function () {//请求失败处理函数
			alert('请求失败');
		},success:function(data){
			var messgage = "添加成功!";
			if(data==null){//未返回任何消息表示添加成功
				loginInfoAddReset();
				//刷新列表
				reloadTable();
			}else if(data.errorMsg!=null){//返回异常信息
				messgage = data.errorMsg;
			}
			$("#loginInfoAdd_message").html(messgage);
		}
	});
}
//用户添加页面数据重置操作
function loginInfoAddReset(){
	$("#loginInfoAdd_message").html("");
	$("#loginInfoAdd_loginCode").val("");
	$("#loginInfoAdd_password").val("");
}

/**--------------编辑操作弹出框------------------**/
//设置弹出框的属性
function setDialog_edit(){
	$('#loginInfoEdit').dialog({
		title : '用户编辑',
		modal: true,         	//模式窗口：窗口背景不可操作
		collapsible : true,  	//可折叠，点击窗口右上角折叠图标将内容折叠起来
		resizable : true    	//可拖动边框大小
	});
}
//打开对话框
function openDialog_edit(loginId,loginCode){
	loginInfoEditReset(loginId,loginCode);
	$('#loginInfoEdit').dialog('open');
}
//关闭对话框
function closeDialog_edit(){
	$('#loginInfoEdit').dialog('close');
}
//根据用户id查询用户的信息
function loginInfoEditReset(loginId,loginCode){
	$("#loginInfoEdit_message").html("");
	$("#loginInfoEdit_loginId").val(loginId);
	$("#loginInfoEdit_loginCode").html(loginCode);
	$("#loginInfoEdit_password").val("");
	$("#loginInfoEdit_repassword").val("");
}
//执行用户编辑操作
function loginInfoEdit(){
	var validateResult=true;
	//easyui 表单验证
	$('#table_loginInfoEdit input').each(function () {
		if ($(this).attr('required') || $(this).attr('validType')) {
			if (!$(this).validatebox('isValid')) {
				//如果验证不通过，则返回false
				validateResult = false;
				return;
		    }
		}
    });
	if(validateResult==false){
		return;
	}
	
	$.ajax({
		async : false,
		cache:false,
		type: 'POST',
		dataType : "json",
		data : {
			"loginInfoEditDto.loginInfo.id" : $("#loginInfoEdit_loginId").val(),
			"loginInfoEditDto.loginInfo.password" : $("#loginInfoEdit_password").val(),
			"loginInfoEditDto.rePassword" : $("#loginInfoEdit_repassword").val()
		},
		url: root+'/ospm/loginInfo/doLoginInfoEdit.jhtml',//请求的action路径
		error: function () {//请求失败处理函数
			alert('请求失败');
		},success:function(data){
			var messgage = "修改成功!";
			if(data==null){//未返回任何消息表示添加成功
				//刷新列表
				reloadTable();
			}else if(data.errorMsg!=null){//返回异常信息
				messgage = data.errorMsg;
			}
			$("#loginInfoEdit_message").html(messgage);
		}
	});
}


/**--------------执行删除操作------------------**/
function doDel(loginId){
	$.messager.confirm('删除提示', '你确定永久删除该数据吗?', function(r){
		if (r){
			var url = root+'/ospm/loginInfo/doLoginInfoDelete.jhtml?loginId='+loginId;
			changeStatus(url);
		}
	});
}
/**
 * 启用
 * @param loginId
 * @return
 */
function doUsable(loginId){
	$.messager.confirm('启用提示', '你确定启用该数据吗?', function(r){
		if (r){
			var url = root+'/ospm/loginInfo/doLoginInfoAvailable.jhtml?loginId='+loginId;
			changeStatus(url);
		}
	});
}
/**
 * 禁用
 * @param loginId
 * @return
 */
function doForbidden(loginId){
	$.messager.confirm('删除提示', '你确定禁用该数据吗?', function(r){
		if (r){
			var url = root+'/ospm/loginInfo/doLoginInfoInvalid.jhtml?loginId='+loginId;
			changeStatus(url);
		}
	});
}

/**
 * 修改状态的Ajax
 * @param url
 * @return
 */
function changeStatus(url){
	$.ajax({
		async : false,
		cache:false,
		type: 'POST',
		dataType : "json",
		url:url,//请求的action路径
		error: function () {//请求失败处理函数
			alert('请求失败');
		},success:function(data){
			if(data!=null){//返回异常信息
				$.messager.alert('错误提示',data.errorMsg,'error');
			}
			reloadTable();
		}
	});
}

/**
 * 批量操作
 * @return
 */
function batch(flag){
	if($('#loginInfoTable').datagrid('getSelected')){
		//首先如果用户选择了数据，则获取选择的数据集合
		var ids = [];
		var cods = [];
		var selectedRow = $('#loginInfoTable').datagrid('getSelections');
		for(var i=0;i<selectedRow.length;i++){
			ids.push(selectedRow[i].loginId);
			cods.push(selectedRow[i].loginCode);
		}
		var loginId = ids.join(',');
		
		if(flag == "available"){
			//启用操作
			$.messager.confirm('启用提示', '你确定启用下列用户吗?<br/>'+cods.join(','), function(r){
				if (r){
					var url = root+'/ospm/loginInfo/doLoginInfoAvailable.jhtml?loginId='+loginId;
					changeStatus(url);
				}
			});
		}else if(flag == "invalid"){
			//禁用操作
			$.messager.confirm('禁用提示', '你确定禁用下列用户吗?<br/>'+cods.join(','), function(r){
				if (r){
					var url = root+'/ospm/loginInfo/doLoginInfoInvalid.jhtml?loginId='+loginId;
					changeStatus(url);
				}
			});
		}else{
			//删除操作
			$.messager.confirm('删除提示', '你确定永久删除下列用户吗?<br/>'+cods.join(','), function(r){
				if (r){
					var url = root+'/ospm/loginInfo/doLoginInfoDelete.jhtml?loginId='+loginId;
					changeStatus(url);
				}
			});
		}
	}//end of if
}

