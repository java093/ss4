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
var urlstring = baseUrl + '/mail/listBySearch';
//初始化列表
$(document).ready(function(){
	// email列表 emailList
	$('#emailList').datagrid({
		title:'任务列表',
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
		    {title:'邮件名称',field:'LOGICALNAME',width:$(this).width()*0.2,sortable:false},
            {title:'主机名',field:'HOST',width:$(this).width()*0.2,sortable:false},
            {title:'端口号',field:'PORT',width:$(this).width()*0.2,sortable:false},
            {title:'ID',field:'id',sortable:false,width:$(this).width()*0.1,hidden:false} //hidden:true 隐藏列主键
		]],			
		onHeaderContextMenu: function(e, field){
			e.preventDefault();
			if (!$('#tmenu').length){
				createColumnMenu('#emailList');
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
	
	var pager = $('#emailList').datagrid('getPager');
    $(pager).pagination({showPageList:false});

   	/*
	*字符转JSON
	*/
	 function strToJson(str){ var json = eval(str ); return json; } 
});


/*
*查看是否选中行
*/
function getSelections(){
	var ids = new Array();
	var rows = $('#emailList').datagrid('getSelections');
	for(var i=0;i<rows.length;i++){
		ids.push(rows[i].id);
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
  return ids ;
}


function searchEmail(){
	var urlstring = baseUrl + '/mail/listBySearch';
	$query = {
		name: $.trim($("#name").val()),
	};
	var selectRows = $('#emailList').datagrid('getSelections');	
	if (selectRows.length > 0) {
		$('#emailList').datagrid('clearSelections');
	}
	$("#emailList").datagrid('options').url =urlstring;
	$("#emailList").datagrid('options').queryParams = $query;
	$("#emailList").datagrid('load');
	
}
//确定操作
function addEmailList(){
    if(getSelections()){
	var rows = $('#emailList').datagrid('getSelections');
	for(var i=0;i<rows.length;i++){
		var id = rows[i].id;
		var name = rows[i].LOGICALNAME;
		parent.addEmailName(name,id);
	}	
	if (rows.length > 0) {
		$('#emailList').datagrid('clearSelections');
		parent.$('#emailAdd').dialog('close');  
	}
    }
}

//关闭操作
function closeWin(){
	parent.$('#emailAdd').dialog('close');  	
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
	<a href="#" class="easyui-linkbutton" onclick="addEmailList();" icon="icon-ok" plain="true">确定</a>
	<a href="#" class="easyui-linkbutton" onclick="closeWin();" icon="icon-undo" plain="true">取消</a>
</div>
<table border="0" cellpadding="0" cellspacing="0" width="100%"> 
  <tr>
    <td class="search">	
	 <table border="0" cellpadding="0" cellspacing="0" width="55%">	
		<tr height="30">
		     <td nowrap="nowrap" width="20%" align="left">邮件名称：</td>
			    <td nowrap="nowrap" width="20%" align="left">   <input style="width:140px;" type="text" id="name" name="name"/>
			</td>
			
			<td width="20%" align="center">
				<a id = "" href="#" class="easyui-linkbutton" onclick="searchEmail();" icon="icon-search" plain="true">查询</a>
		    </td>
		</tr>
	 </table>
</td>
</tr>
  <tr>
    <td>
      <div class="grid">
        <table id="emailList"></table>
      </div>   
    </td>
  </tr>
</table>
</form>
</body>
</html>

