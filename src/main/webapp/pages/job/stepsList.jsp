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
	
	 $.ajax({
		  url: baseUrl+'/workflow/workflowSelect',
         dataType : "json",
         type : "POST",	  	  
         success : function(json) {
       	  for(var i=0;i<json.length;i++){           		  
       		  $("#workFlowServerId").append("<option value='"+json[i].id+"'>"+json[i].server_name+"</option>");  
           }        
        }
	    });

	
	var urlstring  =baseUrl+'/workflow/listByName';
	// workflow列表
	$('#workFlowList').datagrid({
		title      :'WorkFlow列表',
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
            {title:'WORKFLOW名称',field:'WORKFLOW_NAME',width:$(this).width()*0.2,sortable:true},
            {title:'WORKFLOW别名',field:'ALIAS',width:$(this).width()*0.2,sortable:true},   
            {title:'所属INFAMATIC',field:'INFAMATIC',width:$(this).width()*0.2,sortable:true},
            {title:'说明',field:'EXPLAIN',width:$(this).width()*0.3,sortable:true},
            {title:'ID',field:'id',sortable:false,width:$(this).width()*0.1,hidden:true} //hidden:true 隐藏列主键
		]],	
		onHeaderContextMenu: function(e, field){
			e.preventDefault();
			if (!$('#tmenu').length){
				createColumnMenu('#workFlowList');
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
	
	var pager = $('#workFlowList').datagrid('getPager');
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
	var rows = $('#workFlowList').datagrid('getSelections');
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
	var urlstring = baseUrl + '/workflow/listByName';
	$query = {
		name: $.trim($("#ALIAS").val()),
		infa_id:$("#workFlowServerId").find("option:selected").val()
	};
	var selectRows = $('#workFlowList').datagrid('getSelections');	
	if (selectRows.length > 0) {
		$('#workFlowList').datagrid('clearSelections');
	}
	$("#workFlowList").datagrid('options').url =urlstring;
	$("#workFlowList").datagrid('options').queryParams = $query;
	$("#workFlowList").datagrid('load');	
}
//确定操作
function addStepList(){
	 if(getSelections()){
	 var rows = $('#workFlowList').datagrid('getSelections');
	 for(var i=0;i<rows.length;i++){
		var id = rows[i].id;
		var ALIAS = rows[i].ALIAS;
		var EXPLAIN = rows[i].EXPLAIN;
	    parent.addStepToGrid(id,ALIAS,EXPLAIN);	
	 }	
	 if (rows.length > 0) {
		$('#workFlowList').datagrid('clearSelections');
	}
	 }
}

//关闭操作
function closeWin(){
	parent.$('#workflowadd').dialog('close');  	
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
	 <table border="0" cellpadding="0" cellspacing="0" width="100%">	
		<tr height="30">
		    <td nowrap="nowrap" width="8%" align="left" >服务器名称：</td>
			<td width="23%" align="left">
			<select name="workFlowServer" id="workFlowServerId"  style="width:90%">	
			<option value="" >请选择</option>
			</select>
		    </td>	
			<td nowrap="nowrap" width="13%" align="left">WorkFlow别名：</td>
			<td nowrap="nowrap" width="15%" align="left">
			<input style="width:100px;" type="text" id="ALIAS" name="ALIAS"/>
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
        <table id="workFlowList"></table>
      </div>   
    </td>
  </tr>
</table>
</form>
</body>
</html>

