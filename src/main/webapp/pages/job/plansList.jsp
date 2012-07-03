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
  var urlstring  =baseUrl+'/plan/listBySearch';
//初始化列表
$(document).ready(function(){
	$('#plansList').datagrid({
		title      :'计划列表',
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
            {title:'计划名称',field:'name',width:$(this).width()*0.2,sortable:false},
            {title:'计划类型',field:'type',width:$(this).width()*0.2,sortable:false},
            {title:'计划类型ID',field:'typeid',width:$(this).width()*0.2,sortable:false,hidden:true},
            {title:'计划时间',field:'time',width:$(this).width()*0.2,sortable:false},
            {title:'开始日期',field:'startedtime',width:$(this).width()*0.2,sortable:false},
            {title:'启动标志',field:'start_flag',width:$(this).width()*0.2,sortable:false},
            {title:'说明',field:'explain',width:$(this).width()*0.4,sortable:false},
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
	
	var pager = $('#plansList').datagrid('getPager');
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
	var rows = $('#plansList').datagrid('getSelections');
	for(var i=0;i<rows.length;i++){
		ids.push(rows[i].id);
	}
	if(rows.length==0){
		$.messager.alert('提示信息',"至少选择一项");
		 return false;
	}
	return ids;
}


function searchPlan(){
	var urlstring = baseUrl + '/plan/listBySearch';
	$query = {
		type: $.trim($("#type").val()),
		name: $.trim($("#name").val())
	};
	var selectRows = $('#plansList').datagrid('getSelections');	
	if (selectRows.length > 0) {
		$('#plansList').datagrid('clearSelections');
	}
	$("#plansList").datagrid('options').url =urlstring;
	$("#plansList").datagrid('options').queryParams = $query;
	$("#plansList").datagrid('load');
	
}
//确定操作
function addPlanList(){
   if(getSelections()){
	var rows = $('#plansList').datagrid('getSelections');
	for(var i=0;i<rows.length;i++){
		var id = rows[i].id;
		var name = rows[i].name;
		var time = rows[i].time;
		var startedtime = rows[i].startedtime;
		var explain = rows[i].explain;
		var type = rows[i].type;
		var typeid = rows[i].typeid;	
	    parent.addPlanToGrid(id,name,time,startedtime,type,typeid,explain);	
	}	
	if (rows.length > 0) {
		$('#plansList').datagrid('clearSelections');
	}
	}
}

//关闭操作
function closeWin(){
	parent.$('#planadd').dialog('close');  	
}

//比较时间 格式 yyyy-mm-dd hh:mi:ss  
function isTime(beginTime,endTime){  
	beginTime =  beginTime.replace(/-/g,"/");
	d1 = new Date(beginTime)
	 var beginTimes=beginTime.substring(0,10).split('-');  
	 beginTime=beginTimes[1]+'-'+beginTimes[2]+'-'+beginTimes[0]+' '+beginTime.substring(10,19);  
	 if(Date.parse(d1) > endTime ){  
		 return false;
	 }else {  
		 return true; 
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
	<a href="#" class="easyui-linkbutton" onclick="addPlanList();" icon="icon-ok" plain="true">确定</a>
	<a href="#" class="easyui-linkbutton" onclick="closeWin();" icon="icon-undo" plain="true">取消</a>
</div>
<table border="0" cellpadding="0" cellspacing="0" width="100%"> 
  <tr>
    <td class="search">	
	 <table border="0" cellpadding="0" cellspacing="0" width="90%">	
		<tr height="30">
			<td nowrap="nowrap" width="10%" align="left">计划类型：</td>
			<td nowrap="nowrap" width="15%" align="left">		
			<select name="type"  id="type" style="width:100px;">
			<option value="" >请选择</option>
			<option value="0" >仅执行一次</option>
			<option value="1">每天</option>	
		   <option value="2" >每月</option>
			</select>
			</td>
		     <td nowrap="nowrap" width="10%" align="left">计划名称：</td>
			 <td nowrap="nowrap" width="15%" align="left">    
			 <input style="width:100px;" type="text" id="name" name="name"/>
			 </td>
			<td width="25%" align="center">
				<a id = "" href="#" class="easyui-linkbutton" onclick="searchPlan();" icon="icon-search" plain="true">查询</a>
		    </td>
		</tr>
	 </table>
</td>
</tr>
  <tr>
    <td>
      <div class="grid">
        <table id="plansList"></table>
      </div>   
    </td>
  </tr>
</table>
</form>
</body>
</html>

