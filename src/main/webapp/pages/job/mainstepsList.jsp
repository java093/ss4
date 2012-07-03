<%@page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="../common/heard_inc.jsp"%>
<title>任务管理</title>
<script type="text/javascript">
//路径
baseUrl = "<%= baseUrl%>";
</script>
<script type="text/javascript">
//初始化列表
$(document).ready(function(){
	var urlstring  =baseUrl+'/workflow/listByName';
	// workflow列表
	$('#stepList').datagrid({
		title      :'步骤列表',
		width      :'100%',
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
            {title:'步骤名称',field:'NAME',width:$(this).width()*0.2,sortable:false},
            {title:'说明',field:'EXPLAIN',width:$(this).width()*0.4,sortable:false},
            {title:'ID',field:'id',sortable:false,width:$(this).width()*0.1,hidden:true} //hidden:true 隐藏列主键
		]],	
		onHeaderContextMenu: function(e, field){
			e.preventDefault();
			if (!$('#tmenu').length){
				createColumnMenu('#stepList');
			}
			$('#tmenu').menu('show', {
				left:e.pageX,
				top:e.pageY
			});
		},
		onLoadError:function(){$.messager.alert('提示信息', messageInfo.errorData,"error");},			
		pagination:true,
		rownumbers:true
	});
	
	var pager = $('#stepList').datagrid('getPager');
    $(pager).pagination({showPageList:false});
   	/*
	*字符转JSON
	*/
	 function strToJson(str){ var json = eval(str ); return json; } 
});


/*
*  查看是否选中行(1行或者多行)
*/
function getSelections(){
	var ids = new Array();
	var rows = $('#stepList').datagrid('getSelections');
	for(var i=0;i<rows.length;i++){
		ids.push(rows[i].id);
	}
	if(rows.length==0){
		$.messager.alert('提示信息',"至少选择一项");
		 return false;
	}
	return ids;
}


function searchWorkFlow(){

	var type = $("#STEPTYPE").val();
	var urlstring;
    if(type==0){
    	urlstring = baseUrl + '/workflow/listByName';	
    }else{
        urlstring = baseUrl + '/task/listByName';	
    }
    
	$query = {
		type: type,
		name:$.trim($("#NAME").val()),
		runtype:''
		
	};
	var selectRows = $('#stepList').datagrid('getSelections');	
	if (selectRows.length > 0) {
		$('#stepList').datagrid('clearSelections');
	}
	$("#stepList").datagrid('options').url =urlstring;
	$("#stepList").datagrid('options').queryParams = $query;
	$("#stepList").datagrid('load');	
}
//确定操作
//(workflowidParam,subtaskidParam,aliasParam,explainParam,runtype)
function addStepList(){
	if(getSelections()){
	var type = $("#STEPTYPE").val();
	var rows = $('#stepList').datagrid('getSelections');
	if(type==0){
		for(var i=0;i<rows.length;i++){
			var workflowid = rows[i].id;
			var name = rows[i].NAME;
			var explain = rows[i].EXPLAIN;
		    parent.addStepToGrid(workflowid,'',name,explain,type);	
		}	
	}else{
		for(var i=0;i<rows.length;i++){
			var subtaskid = rows[i].id;
			var name = rows[i].NAME;
			var explain = rows[i].EXPLAIN;
		    parent.addStepToGrid('',subtaskid,name,explain,type);	
		}	
	}
	
	if (rows.length > 0) {
		$('#stepList').datagrid('clearSelections');
	}
	 }
}

//关闭操作
function closeWin(){
	parent.$('#workflowadd').dialog('close');  	
}

function chooseStep(){
	var step = $("#STEPTYPE").val();
	if(step=='1'){//选择子任务时
		var urlstring = baseUrl + '/task/list';
		var selectRows = $('#stepList').datagrid('getSelections');	
		if (selectRows.length > 0) {
			$('#stepList').datagrid('clearSelections');
		}
		$("#stepList").datagrid('options').url =urlstring;
		$("#stepList").datagrid('load');	
	}
	if(step=='0'){
		var urlstring = baseUrl+'/workflow/listByName';
		var selectRows = $('#stepList').datagrid('getSelections');	
		if (selectRows.length > 0) {
			$('#stepList').datagrid('clearSelections');
		}
		$("#stepList").datagrid('options').url =urlstring;
		$("#stepList").datagrid('load');	
	}
}

</script>
</head>
<style>
.maintopRight{
	padding-right:10px;
}
a{TEXT-DECORATION:none}
</style>
<body>
<form method="get">
<div class="tbar">
	<a href="#" class="easyui-linkbutton" onclick="addStepList();" icon="icon-ok" plain="true">确定</a>
	<a href="#" class="easyui-linkbutton" onclick="closeWin();" icon="icon-undo" plain="true">取消</a>
</div>
<table border="0" cellpadding="0" cellspacing="0" width="100%"> 
  <tr>
    <td class="search">	
	 <table border="0" cellpadding="0" cellspacing="0" width="85%">	
		<tr height="30">
			<td nowrap="nowrap" width="10%" align="left">步骤类型：</td>
			<td nowrap="nowrap" width="15%" align="left">
			<select name="STEPTYPE" id="STEPTYPE" style="width:90%" onclick="chooseStep()">
			<option value="0" >workflow</option>
			<option value="1">子任务</option>	
			</select>
			</td>
			<td nowrap="nowrap" width="10%" align="left">&nbsp;&nbsp;步骤名称：</td>
			<td nowrap="nowrap" width="15%" align="left">
			     <input style="width:100px;" type="text" id="NAME" name="NAME"/>
			</td>
			<td width="15%" align="center">
				<a id = "" href="#" class="easyui-linkbutton" onclick="searchWorkFlow();" icon="icon-search" plain="true">查询</a>
		    </td>
		</tr>
	 </table>
</td>
</tr>
  <tr>
    <td>
      <div class="grid">
        <table id="stepList"></table>
      </div>   
    </td>
  </tr>
</table>
</form>
</body>
</html>

