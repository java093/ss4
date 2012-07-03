<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*"%> 
<%@ page import="java.text.*"%>
<% 
String datetime=new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()); //获取系统时间 
%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%@include file="../common/heard_inc.jsp"%>
<title>任务添加</title>

<script type="text/javascript">
var timeCall;
var task;              //任务数据
var taskStepList;      //  任务下列表数据
var taskPlanList;           // 计划列表数据
var idSeq;             // 修改操作用到的项目idSeq
$(document).ready(function(){
	// 如果url参数为0, 执行新增操作, 否则执行修改操作
	var paramTemp = location.search;
	if(null != paramTemp && "" != paramTemp){//修改操作
	   	 idSeq = location.search.split("=")[1];
	    // alert(idSeq);
		 if(null != idSeq && "" != idSeq && !isNaN(idSeq)&&0!=idSeq){
			  $.getJSON("<%=baseUrl %>/task/findbyid",{'id':idSeq}, function(data){	
				if(data.success){	
					task = data.task;	
					$('#TASKNAME').val(task.TASKNAME);
					$('#RUNTYPE').val(task.RUNTYPE);
					$('#EXPLAIN').val(task.EXPLAIN);
					$('#MAIL_ID').val(task.MAIL_ID);
					$('#MAILNAME').val(task.MAILNAME);
					
					var noticflg = task.COMPLETEFLAG;
					if(noticflg!=''){
     					if(noticflg==0){		
							$('input:radio').eq(0).attr('checked',true);
						 }else if(noticflg==1){						
							 $('input:radio').eq(1).attr('checked',true);
						 }else if(noticflg==2){
							 $('input:radio').eq(2).attr('checked',true);
						   }
					}
					if(task.RUNTYPE=='1'){
						$('#btnup').linkbutton('disable');
						$('#btndown').linkbutton('disable');	
					}
					taskStepList = data.taskStepsList;
					taskPlanList = data.taskPlansList;
					$('#taskStepList').datagrid('loadData', taskStepList);				
					$('#taskPlanList').datagrid('loadData', taskPlanList);
					
											
				}
			});	 
		} 
	}	
	initGrid();
	//验证规则初始化
	$('#insertForm').validate({
		rules : {
			TASKNAME : {
				required : true,
				maxlength : 50,
				remote: {
					url: '<%=baseUrl%>/task/findbyname',
					type: 'POST',
					dateType: 'json',
					data: {                     //要传递的数据
						id :idSeq,
						type:1
					}
				  }
	          },
			RUNTYPE: {
				required : true
			},
			EXPLAIN: {
				maxlength : 400
			}
		},
	  messages: {
	    	 TASKNAME:{
	    		  remote:"任务名已存在！"
	    		} 
	     } 	
	});
	
	
});

//workflow列表
function initGrid(){
	// 项目组成员
	$('#taskStepList').datagrid({
		url:'',
		width      :'100%',
		nowrap     : false,
		striped    : true,
		idField    :'ORDERS',
		fitColumns : true,
		toolbar:[{
			text:'增加',
			iconCls:'icon-add',
			handler:function(){	       
				createWindow("workflowadd","WorkFlow列表",true,"normal",false,550,425,"../../pages/job/stepsList.jsp");
			}	
		},'-',{
			text:'上移',
			id:'btnup',
			iconCls:'icon-up',
			handler:function(){
				var select=getSelections('taskStepList');
				if(select){
					var row = $('#taskStepList').datagrid('getSelected');
					var index = $('#taskStepList').datagrid('getRowIndex', row);
					if(index==0){
						 $.messager.alert('提示信息',"已经在第一行,无法上移！");
						 $('#taskStepList').datagrid('clearSelections');
					}else{
						  $('#taskStepList').datagrid('deleteRow', index);
						  $('#taskStepList').datagrid('insertRow',{
								index: index-1,	// index start with 0
								row:row
							});						 
						  $('#taskStepList').datagrid('selectRow', index-1);
					}
				}
			}
			
		},'-',{
			text:'下移',
			id:'btndown',
			iconCls:'icon-down',
			handler:function(){
				var select=getSelections('taskStepList');
				if(select){
					var row = $('#taskStepList').datagrid('getSelected');
					var index = $('#taskStepList').datagrid('getRowIndex', row);
					if(index==$('#taskStepList').datagrid('getRows').length-1){
						 $.messager.alert('提示信息',"已经在最后一行,无法下移！");
						 $('#taskStepList').datagrid('clearSelections');
					}else{
						  $('#taskStepList').datagrid('deleteRow', index);
						  $('#taskStepList').datagrid('insertRow',{
								index: index+1,	// index start with 0
								row:row
							});
						  $('#taskStepList').datagrid('selectRow', index+1);
						    
					}
				}
			}	
		},'-',{
			text:'删除',
			iconCls:'icon-remove',
			handler:function(){
				var num = $('#taskStepList').datagrid('getSelections').length;
				if(num==0){
					$.messager.alert('提示信息',"至少选择一项");	
					 return false;
				}
				for(var i = 0; i < num; i++){
					var row = $('#taskStepList').datagrid('getSelected');
					var index = $('#taskStepList').datagrid('getRowIndex', row);
					$('#taskStepList').datagrid('deleteRow', index);
				}
			}
		}],
		columns:[[
				    {field:'ck',checkbox:true},
				    {title:'ID',field:'ID',width:$(this).width()*0.1,hidden:true}, //hidden:true 隐藏列主键
				    {title:'ORDERS',field:'ORDERS',width:$(this).width()*0.1,hidden:true}, //hidden:true 隐藏列主键
				    {title:'WORKFLOWID',field:'WORKFLOWID',width:$(this).width()*0.1,hidden:true},
				    {title:'SUBTASKID',field:'SUBTASKID',width:$(this).width()*0.1,hidden:true},
				    {title:'WORKFLOW别名',field:'ALIAS',width:$(this).width()*0.1},
				    {title:'说明',field:'EXPLAIN',width:$(this).width()*0.1}
				]],				
				onLoadError:function(){$.messager.alert('提示信息', messageInfo.errorData,"error");},			
				rownumbers:true
	});

	// 项目组成员
	$('#taskPlanList').datagrid({
		url:'',
		width      :'100%',
		nowrap     : false,
		striped    : true,
		idField    :'PLANID',
		fitColumns : true,
		toolbar:[{
			text:'增加',
			iconCls:'icon-add',
			handler:function(){	       
				createWindow("planadd","计划列表",true,"normal",false,550,425,"../../pages/job/plansList.jsp");
			}	
		},{
			text:'删除',
			iconCls:'icon-remove',
			handler:function(){
				var num = $('#taskPlanList').datagrid('getSelections').length;
				if(num==0){
					$.messager.alert('提示信息',"至少选择一项");	
					 return false;
				}
				for(var i = 0; i < num; i++){
					var row = $('#taskPlanList').datagrid('getSelected');
					var index = $('#taskPlanList').datagrid('getRowIndex', row);
					$('#taskPlanList').datagrid('deleteRow', index);
				}
			}
		}],
		columns:[[
					{field:'ck',checkbox:true},
					{title:'ID',field:'ID',width:$(this).width()*0.1,hidden:true}, //hidden:true 隐藏列主键
					{title:'PLANID',field:'PLANID',width:$(this).width()*0.1,hidden:true}, //hidden:true 隐藏列主键
					
					{title:'计划名称',field:'NAME',width:$(this).width()*0.2,sortable:false},
					{title:'计划类型',field:'TYPE',width:$(this).width()*0.2,sortable:false},
					{title:'说明',field:'EXPLAIN',width:$(this).width()*0.4,sortable:false}
				]],				
				onLoadError:function(){$.messager.alert('提示信息', messageInfo.errorData,"error");},			
				rownumbers:true
	});

	
	
    }



//datagrid数据转成字符串
function jsonToStrStep(arr) {
	var jsonStr = "{\"rows\":[";	
	for(i=0;i<arr.rows.length;i++){
      jsonStr += "{";
      jsonStr += "\"id\""+ ":" + "\"" + arr.rows[i].ID+ "\",";
      jsonStr += "\"orders\""+ ":" + "\"" + arr.rows[i].ORDERS+ "\",";
      jsonStr += "\"workflowid\""+ ":" + "\"" + arr.rows[i].WORKFLOWID + "\",";	
      jsonStr += "\"subtaskid\""+ ":" + "\"" + arr.rows[i].SUBTASKID + "\",";	
      jsonStr += "\"alias\""+ ":" + "\"" + arr.rows[i].ALIAS + "\",";
      jsonStr += "\"index\""+ ":" + "\"" + i+ "\",";
      jsonStr += "\"explain\""+ ":" + "\"" + arr.rows[i].EXPLAIN + "\"}";
      if(i != arr.rows.length-1){
    	  jsonStr += ",";  
      }
    }
	jsonStr += "]}";
	return jsonStr;
}

function jsonToStrPlan(arr){
	var jsonStr = "{\"rows\":[";	
	for(i=0;i<arr.rows.length;i++){
      jsonStr += "{";
      jsonStr += "\"id\""+ ":" + "\"" + arr.rows[i].ID+ "\",";		
      jsonStr += "\"planid\""+ ":" + "\"" + arr.rows[i].PLANID + "\",";		
      jsonStr += "\"name\""+ ":" + "\"" + arr.rows[i].NAME + "\",";
      jsonStr += "\"type\""+ ":" + "\"" + arr.rows[i].TYPE + "\",";
      jsonStr += "\"explain\""+ ":" + "\"" + arr.rows[i].EXPLAIN + "\"}";
      if(i != arr.rows.length-1){
    	  jsonStr += ",";  
      }
    }
	jsonStr += "]}";
	return jsonStr;
}



/*
 *提交前，数据验证
 */
function showRequest() {
	return $("#insertForm").valid();

} 

/*
 * 新增,清空form 以及数据从第一页重新加载.
 * 异步提交服务器，保存数据
 */
var flage = false;

function getSelections(arr){
	var ids = new Array();
	var list ='#'+arr;
	var rows = $(list).datagrid('getSelections');
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

/*
 * 关闭弹出页面
 */
function closeWin(){
	parent.$('#editTask').dialog('close');  	
}



//添加步骤到一览数据里
function addStepToGrid(idParam,aliasParam, explainParam){
 
	var rows = jsonToStrStep($('#taskStepList').datagrid('getData'));
	var json = eval( "(" + rows + ")" );//json String变成对象
	var order = json.rows.length;
	var orders = 0;
	if(order>0){
	   for(i=0;i<json.rows.length;i++){
		   if(idParam==json.rows[i].workflowid){//判断workflow是否已经添加已添加的话，覆盖		   
			   $('#taskStepList').datagrid('deleteRow',i);	         
		     }
		   }
	   orders = parseInt(json.rows[order-1].orders)+1;
	   }

	var row = {"ORDERS":orders,"SUBTASKID":'',"WORKFLOWID":idParam,"ALIAS":aliasParam,"EXPLAIN":explainParam};
	$("#taskStepList").datagrid("appendRow", row);
}


//添加计划到一览数据里
function addPlanToGrid(idParam, nameParam,typeParam, explainParam){
	var rows = jsonToStrPlan($('#taskPlanList').datagrid('getData'));
	var json = eval( "(" + rows + ")" );//json String变成对象
	if(json.rows.length>0){
	   for(i=0;i<json.rows.length;i++){
		   	   if(idParam==json.rows[i].planid){
		   		   //判断workflow是否已经添加已添加的话，覆盖		   		 
			  $('#taskPlanList').datagrid('deleteRow', i);	         
		     }
	     }
	   }	
	var row = {"PLANID":idParam,"NAME":nameParam,"TYPE":typeParam,"EXPLAIN":explainParam};
	$("#taskPlanList").datagrid("appendRow", row);
}

//保存按钮调用方法
function btnSave(){
	$('#baseDiv').tabs('select','属性');
    if(showRequest()){ //验证
		var stepRow = jsonToStrStep($('#taskStepList').datagrid('getData'));
		$add = {
				   'taskname':$.trim($("#TASKNAME").val()),
   		           'runtype':$("#RUNTYPE").val(),
   		           'explain':$.trim($("#EXPLAIN").val()),	
   		           'stepRows':stepRow, // 步骤	
   		           'noticeflag':0,
   		           'emailID':0,
   		           'type':1	      
		}
		var  urlParam = "<%=baseUrl%>"+"/task/add";     // 新增操作
		var  urlEdit = "<%=baseUrl%>"+"/task/edit?taskid="+idSeq;//编辑操作
		// 请求后台, 保存数据
		 if(null != idSeq&&0!=idSeq){
				$.post(urlEdit,$add,function(data){
				    var data = eval( "(" + data + ")" );//json String变成对象
					if(data.success){		
							    $.messager.alert('提示信息',messageInfo.editSuccess, 'info', function(){
								parent.$("#taskList").datagrid('load',{'runtype':'','taskname':''});
								parent.$('#editTask').dialog('close');
							});
						}else{				
							
							$.messager.alert('提示信息',messageInfo.editFail,'warning');		
					}
				}); 	
		}else{

		$.post(urlParam,$add,function(data){
		    var data = eval( "(" + data + ")" );//json String变成对象
			if(data.success){		
					    $.messager.alert('提示信息',messageInfo.addSuccess, 'info', function(){
						parent.$("#taskList").datagrid('load');
						parent.$('#editTask').dialog('close');
					});
				}else{				
					$.messager.alert('提示信息',messageInfo.addFail,'warning');	
			}
		});  
		}
		}
	 
	}

function emailNotice(){
	createWindow("emailAdd","邮件列表",true,"normal",false,550,425,"../../pages/job/emailList.jsp");
}

function addEmailName(name,id){
	$('#MAILNAME').val(name);
	$('#MAIL_ID').val(id);
	
}

function updownControl(){
	var runtype = $("#RUNTYPE").val();
	if(runtype=='1'){
		$('#btnup').linkbutton('disable');
		$('#btndown').linkbutton('disable');
	}
	if(runtype=='0'){
		$('#btnup').linkbutton('enable');
		$('#btndown').linkbutton('enable');
	}
	
}
</script>
</head>

<body>
<form method="get" id="insertForm" name="insertForm">
<div class="tbar">
	<a href="javascript:btnSave();" class="easyui-linkbutton" icon="icon-save" plain="true">保存</a>
	<a href="javascript:closeWin();" class="easyui-linkbutton" icon="icon-undo" plain="true">取消</a>
</div>
<div class="easyui-tabs" id="baseDiv">
	<div title="属性">
        <table cellpadding="0" cellspacing="0" border="0" id="uiform">	
		<tr>
			<td width="10%" class="form_title">任务名称：</td>
			<td width="15%" class="form_content">
				<input type="text" id="TASKNAME" name ="TASKNAME"/>
				<font color="red">*</font>
			</td>
	    </tr>			
		<tr>
			<td width="10%" class="form_title">运行方式：</td>
			<td width="15%" class="form_content">		
				<select id="RUNTYPE" name="RUNTYPE" style="width:26.5%"  onclick="updownControl()">	
				<option value="0" >串行</option>
				<option value="1">并行</option>	
				</select>
				<font color="red">*</font>
		</tr>
		<tr>
			<td width="10%" class="form_title">说明：</td>
            <td  class="form_content">
			<textarea  id="EXPLAIN"  name="EXPLAIN" style="width:496px;height:100px;border:1px solid #7F9DB9;background-color:#FFFFFF;voerflow-x:true; " ></textarea> 
			</td>
		</tr>		
	 </table>
	</div>
	
	<div title="步骤">
		<table id="taskStepList"></table>
	</div>
<!-- 	<div title="计划">
		<table id="taskPlanList"></table>
	</div>
	
	<div title="通知">
	<div class="tbar"  style="background-color:#EFF0F2;">
	  <a href="#" class="easyui-linkbutton" onclick="emailNotice();" icon="icon-add" plain="true">添加</a>
	</div>		 
    <input name='MAIL_ID' id='MAIL_ID' type='hidden'/>
	<table cellpadding="0" cellspacing="0" border="0" width="80%" >	
		    <tr>
		        <td colspan="2">
		            <table cellpadding="0" cellspacing="0" border="0" width="80%" >	
		                <tr>
		                <td width="8%" class="form_title">完成</td>
		                <td width="10%" class="form_content"><input type="radio" name="COMPLETEFLAG" value="0" /></td>
		                <td width="8%" class="form_title">成功</td>
		                <td width="10%" class="form_content"><input type="radio" name="COMPLETEFLAG" value="1"/></td>
		                <td width="8%" class="form_title">失败</td>
		                <td width="10%" class="form_content"><input type="radio" name="COMPLETEFLAG" value="2"/></td>
		                </tr> 
		             </table>
		        </td>
		    </tr>
		    <tr>
		        <td width="6%" class="form_title">
		                        邮件名称：  
		        </td>
		        <td width="18%" >
				 &nbsp;<input type="text" style="width:140px;" id="MAILNAME" readonly="readonly" name = "MAILNAME" maxLength="200"/>
				</td>	       
		    </tr>
		</table>
   </div> -->
</div>
<div id="newWindow" class="easyui-window" closed="true" collapsible="false" minimizable="false" maximizable="false" resizable="false" draggable="false"  modal="true" zIndex=100 title="项目新增">		
	<iframe id='openDialog'  scrolling="no"  frameborder="0"  src="" style="width:550px;height:500px;"></iframe>
</div>
</form>
</body>
</html>
		