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
					$('#MAILNAME').val(task.MAIL_ID);					
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
					taskStepList = data.taskStepsList;
					taskPlanList = data.taskPlansList;
					$('#taskStepList').datagrid('loadData', taskStepList);				
					$('#taskPlanList').datagrid('loadData', taskPlanList);											
				}
			});	 
		} 
	}	
	initGrid();
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
		columns:[[
				    {title:'ID',field:'ID',width:$(this).width()*0.1,hidden:true}, //hidden:true 隐藏列主键
				    {title:'ORDERS',field:'ORDERS',width:$(this).width()*0.1,hidden:true}, //hidden:true 隐藏列主键
				    {title:'WORKFLOWID',field:'WORKFLOWID',width:$(this).width()*0.1,hidden:true},
				    {title:'SUBTASKID',field:'SUBTASKID',width:$(this).width()*0.1,hidden:true},
				    {title:'步骤名称',field:'ALIAS',width:$(this).width()*0.1},
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
		columns:[[
					{field:'ck',checkbox:true},
				    {title:'ID',field:'ID',width:$(this).width()*0.1,hidden:true}, //hidden:true 隐藏列主键
				    {title:'PLANID',field:'PLANID',width:$(this).width()*0.1,hidden:true}, //hidden:true 隐藏列主键
				    {title:'计划名称',field:'NAME',width:$(this).width()*0.2,sortable:false},
				    {title:'计划时间',field:'TIME',width:$(this).width()*0.2,sortable:false},
				    {title:'开始日期',field:'STARTTIME',width:$(this).width()*0.2,sortable:false},
		            {title:'计划类型',field:'TYPE',width:$(this).width()*0.2,sortable:false},
		            {title:'计划类型ID',field:'TYPEID',width:$(this).width()*0.2,sortable:false,hidden:true},
		            {title:'说明',field:'EXPLAIN',width:$(this).width()*0.4,sortable:false}
				]],				
				onLoadError:function(){$.messager.alert('提示信息', messageInfo.errorData,"error");},			
				rownumbers:true
	}); 
	
	
    }

/*
 * 关闭弹出页面
 */
function closeWin(){
	parent.$('#taskList').datagrid('clearSelections');
	parent.$('#viewTask').dialog('close');  
	
}
</script>
</head>

<body>
<form method="get" id="insertForm" name="insertForm">
<div class="tbar">
	<a href="javascript:closeWin();" class="easyui-linkbutton" icon="icon-undo" plain="true">关闭</a>
</div>
<div class="easyui-tabs" id="baseDiv">
	<div title="属性">
        <table cellpadding="0" cellspacing="0" border="0" id="uiform">	
		<tr>
			<td width="10%" class="form_title">任务名称：</td>
			<td width="15%" class="form_content">
				<input type="text" id="TASKNAME" name ="TASKNAME" readonly="readonly"/>
				<font color="red">*</font>
			</td>
	    </tr>			
		<tr>
			<td width="10%" class="form_title">运行方式：</td>
			<td width="15%" class="form_content">		
				<select id="RUNTYPE" name="RUNTYPE" style="width: 134px;" disabled="disabled"  onclick="updownControl()">	
				<option value="0" >串行</option>
				<option value="1">并行</option>	
				</select>
				<font color="red">*</font>
		</tr>
		<tr>
			<td width="10%" class="form_title">说明：</td>
            <td  class="form_content">
			<textarea  id="EXPLAIN"  name="EXPLAIN" style="width:496px;height:100px;border:1px solid #7F9DB9;background-color:#FFFFFF;voerflow-x:true; " readonly="readonly"></textarea> 
			</td>
		</tr>		
	 </table>
	</div>
	
	<div title="步骤">
		<table id="taskStepList"></table>
	</div>
	<div title="计划">
		<table id="taskPlanList"></table>
	</div>
   
   	<div title="通知">	 
    <input name='MAIL_ID' id='MAIL_ID' type='hidden'/>
	<table cellpadding="0" cellspacing="0" border="0" width="80%" >	
		    <tr>
		        <td colspan="3">
		            <table cellpadding="0" cellspacing="0" border="0" width="80%" >	
		                <tr>
		                <td width="10%" class="form_title">完成</td>
		                <td width="10%" class="form_content"><input type="radio" disabled="disabled" name="COMPLETEFLAG" value="0"/></td>
		                <td width="10%" class="form_title">成功</td>
		                <td width="10%" class="form_content"><input type="radio"  disabled="disabled" name="COMPLETEFLAG" value="1"/></td>
		                <td width="10%" class="form_title">失败</td>
		                <td width="10%" class="form_content"><input type="radio" disabled="disabled" name="COMPLETEFLAG" value="2"/></td>
		                </tr> 
		             </table>
		        </td>
		    </tr>
		    <tr>
		        <td width="6%" class="form_title">
		                        邮件名称：  
		        </td>
		        <td width="35%" >
				 &nbsp;<input type="text" style="width:90%" id="MAILNAME" readonly="readonly" name = "MAILNAME" />
				</td>	
		    </tr>
		    <tr>
				<td colspan="2" width="100%" >
					 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					 &nbsp;
					 (可以输入多个邮件名称，以分号(;)隔开)
				</td>   
			</tr>	
		</table>
   </div>
   
   
</div>
<div id="newWindow" class="easyui-window" closed="true" collapsible="false" minimizable="false" maximizable="false" resizable="false" draggable="false"  modal="true" zIndex=100 title="项目新增">		
	<iframe id='openDialog'  scrolling="no"  frameborder="0"  src="" style="width:550px;height:500px;"></iframe>
</div>
</form>
</body>
</html>
		