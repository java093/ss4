	//初始化
$(document).ready(function(){
	var urlstring  =baseUrl+'/plan/listBySearch1';
	
//	var tempCom = eval("[{'VALUE':'','NAME':'请选择'}]");
//	var data1={"rows":[{"NAME":"请选择","VALUE":""},{"NAME":"仅执行一次","VALUE":"0"},{"NAME":"每天","VALUE":"1"},{"NAME":"每月","VALUE":"2"}]}
//	plantype = $('#PLANTYPE').ligerComboBox({
//		data : tempCom,
//		width : "25%",
//		valueField : 'VALUE',
//		textField : 'NAME',
//		valueFieldID : 'prjstatusvalue'
//	});
//	plantype.selectValue("");
//	plantype.setData(data1.rows);
	
	/*
	 * limit :为每页的记录数(common.js参得)
	 */	
	$('#planList').datagrid({
		title:'计划列表',
		width:'100%',			
		nowrap: false,
		striped: true,			
		pageNumber:1,
		pageList:[pageCount[1]],			//分页大小
		queryParams:{limit:pageCount[1],'flowstatus':0},	//分页参数，请不要删除（每页的记录数，请看common.js）
		url:urlstring,	
		sortName: 'title',
		sortOrder: 'desc',
		idField:'id',
		fitColumns: true,
		columns:[[
            {field:'ck',checkbox:true},
            {title:'计划名称',field:'name',width:$(this).width()*0.1,sortable:false},
            {title:'计划类型',field:'type',width:$(this).width()*0.1,sortable:false},
            {title:'说明',field:'explain',width:$(this).width()*0.1,sortable:false},
            {title:'计划时间',field:'time',width:$(this).width()*0.1,sortable:false},
            {title:'开始计划日期',field:'startedtime',width:$(this).width()*0.1,sortable:false},
            {title:'结束计划日期',field:'enddate',width:$(this).width()*0.1,sortable:false},
            {title:'月计划天',field:'day_plan_month',width:$(this).width()*0.1,sortable:false},
            {title:'启动标志',field:'start_flag',width:$(this).width()*0.1,sortable:false}
		]],				
		onHeaderContextMenu: function(e, field){
			e.preventDefault();
			if (!$('#tmenu').length){
				createColumnMenu('#planList');
			}
			$('#tmenu').menu('show', {
				left:e.pageX,
				top:e.pageY
			});
		},
		onLoadError:function(){$.messager.alert('提示信息',"数据加载错误。。。","error");},			
		pagination:true,			
		rownumbers:true
		
	});		
	
	var pager = $('#planList').datagrid('getPager');
    $(pager).pagination({showPageList:false});

   	/*
	*字符转JSON
	*/
	 function strToJson(str){ var json = eval(str ); return json; } 
});

/**
 * 查询方法
 */
//根据检索范围查询
function selectbyflowstatus(){
	var urlstring  = baseUrl+'/techaccum/searchCaseList.do?time='+(new Date().getTime());
	var flowstatusy = document.getElementsByName("flowstatus");
	var flowstatusx;
	for(x = 0; x < flowstatusy.length; x++){
		if(flowstatusy[x].checked){
			flowstatusx = flowstatusy[x].value;
		}
	}
	$query = {
			limit:pageCount[1],					//分页参数，请不要删除（每页的记录数，请看common.js）
			title:$.trim($("#txtCondtile").val()),
			deptname:$.trim($("#deptname").val()),
			deptid:$.trim($("#deptid").val()),
			engName:$.trim($("#txtCondProjectname").val()),
			capacity:$.trim($("#capacity").val()),
			capacityname:$.trim($("#capacityname").val()),
			flowstatus:flowstatusx			
			};
		//清除所有被选中的chickbox
		var selectRows = $('#employeeList').datagrid('getSelections');
		if(selectRows.length>0){
			$('#employeeList').datagrid('clearSelections');
		}
		$("#employeeList").datagrid('options').queryParams=$query;
		$("#employeeList").datagrid('load');
}

/*
打开新增页，返回重新加载Grid
*/ 
function planadd() {
	//openDialogIframe("案例分析新增","newWindow","openDialog","jsjl_add.jsp");
	//alert("add");
	createWindow("jsjladdpage","计划新增",true,"normal",false,450,420,"../pages/plan/plan_add.jsp");
}


/*
打开修改页，返回刷新当前页(保留在当前页码以及查询条件不变)
*/
function caseSrhupdate(){
	var accumseq=getSelections();
	if(accumseq){
		var url = "jsjl_Edit.jsp?accumseq="+accumseq+"&time="+(new Date().getTime());
		//openDialogIframe("技术积累—修改","newWindow","openDialog",url);
		createWindow("jsjleditpage","技术积累—修改",true,"normal",false,730,410,url);
	}
}


/*
打开修改页，返回刷新当前页(保留在当前页码以及查询条件不变)
*/
function caseSrhAuditing(){
	var accumseq=getSelections();
	if(accumseq){
		var url = "jsjl_Auditing.jsp?accumseq="+accumseq+"&time="+(new Date().getTime());
		//openDialogIframe("技术积累—审批","newWindow","openDialog",url);
		createWindow("jsjlauditingpage","技术积累—审批",true,"normal",false,730,410,url);
	}
}

/*
 * 打开修改页面，
 参数testseq 记录主键
 */
function caseSrhview(){
	var accumseq=getSelections();
	if(accumseq){
		var url = "jsjl_View.jsp?accumseq="+accumseq+"&time="+(new Date().getTime());
		//openDialogIframe("技术积累—查看","newWindow","openDialog",url);
		createWindow("jsjlviewpage","技术积累—查看",true,"normal",false,730,410,url);
	}
	
}


/*
*查看是否选中行
*/
function getSelections(){
	var ids = new Array();
	var rows = $('#employeeList').datagrid('getSelections');
	for(var i=0;i<rows.length;i++){
		ids.push(rows[i].accumseq);
	}
		if(rows.length>1){
			$.messager.alert('提示信息',"您只能选择一项");
			return false;
		}else{
			if(rows.length>0){
			}else{
				$.messager.alert('提示信息',"请选择项");
				return false;
			}
		}
	return ids ; //alert(ids.join(':'));
}

/*
*删除选中多行
*/
function getDeleteSelections(){
	var ids = new Array();
	var rows = $('#planList').datagrid('getSelections');
	for(var i=0;i<rows.length;i++){
		ids.push(rows[i].id);
	}
	if(rows.length==0){
		$.messager.alert('提示信息',"至少选择一项");
		
		 return false;
	}
	return ids;
}

/*
 * 删除任务
 */
function deletePlans() {
	var ids = getDeleteSelections();
	if (ids) {
		$.messager.confirm('提示信息', '您是否确认删除所选项?', function(r) {
			if (r) {
				$.ajax({
					url : baseUrl + "/plan/delete?ids=" + ids,
					dataType : "json",
					type : "POST",
					success : function(data) {
						if (data) {
							if (data.success) {
								$.messager.alert('提示信息', "数据删除成功!");
								$('#planList').datagrid('reload');
								$('#planList').datagrid('clearSelections');
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

/*
 * 查看是否选中行
 */
function getSelections() {
	var ids = new Array();
	var rows = $('#planList').datagrid('getSelections');
	//alert(rows.length);
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


/*
 * 打开编辑页，返回重新加载Grid
 */
function informaticaedit() {

	var id = getSelections();
	if (id) {
		var url = "../pages/plan/plan_edit.jsp?id=" + id;
		//alert(url);
		createWindow("jsjleditplan", "编辑", true, "normal",
				false, 460, 420, url);
	}

}

//查询
function searchPlan(){
	var urlstring = baseUrl + '/plan/listBySearch1';
	$query = {
//		type: $('#prjstatusvalue')[0].value,
		type: $.trim($("#type").val()),
		name: $.trim($("#name").val())
	};
	var selectRows = $('#planList').datagrid('getSelections');	
	if (selectRows.length > 0) {
		$('#planList').datagrid('clearSelections');
	}
	$("#planList").datagrid('options').url =urlstring;
	$("#planList").datagrid('options').queryParams = $query;
	$("#planList").datagrid('load');
	
}



