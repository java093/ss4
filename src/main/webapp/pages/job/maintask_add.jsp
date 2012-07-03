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
<script type="text/javascript" src="<%=baseUrl%>/script/ajaxfileupload.js"></script>
<title>任务添加</title>

<script type="text/javascript">
var timeCall;
var task;              //任务数据
var taskStepList;      //  任务下列表数据
var taskPlanList;           // 计划列表数据
var idSeq;    // 修改操作用到的项目idSeq
var f;

$(document).ready(function(){

	// 如果url参数为0, 执行新增操作, 否则执行修改操作
	var paramTemp = location.search;
	if(null != paramTemp && "" != paramTemp){//修改操作
	   	 idSeq = location.search.split("=")[1];	
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
						type:0
					}
				  }
	          },
			RUNTYPE : {
				required : true
			},
			MAILNAME : {
				maxlength : 200
			},
			EXPLAIN : {
				maxlength : 400
			}
		},
		 messages: {
	    	 TASKNAME:{
	    		  remote:"该任务名称已存在！"
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
				createWindow("workflowadd","步骤列表",true,"normal",false,550,425,"../../pages/job/mainstepsList.jsp");
			}	
		},'-',{
			text:'上移',
			iconCls:'icon-up',
			id:'btnup',
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
				    {title:'步骤名称',field:'ALIAS',width:$(this).width()*0.1},
				    {title:'说明',field:'EXPLAIN',width:$(this).width()*0.1}
				]],				
				onLoadError:function(){$.messager.alert('提示信息', messageInfo.errorData,"error");},			
				rownumbers:true
	});
	
//	$('#taskStepList').datagrid('rejectChanges');
	// 计划任务列表
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
		},'-',{
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


//datagrid数据转成字符串
function jsonToStrStep(arr) {
	var jsonStr = "{\"rows\":[";	
	for(i=0;i<arr.rows.length;i++){
      jsonStr += "{";
      jsonStr += "\"id\""+ ":" + "\"" + arr.rows[i].ID+ "\",";
      jsonStr += "\"orders\""+ ":" + "\"" + arr.rows[i].ORDERS+ "\",";
      jsonStr += "\"workflowid\""+ ":" + "\"" + arr.rows[i].WORKFLOWID + "\",";	
      jsonStr += "\"subtaskid\""+ ":" + "\"" + arr.rows[i].SUBTASKID + "\",";		
      jsonStr += "\"name\""+ ":" + "\"" + arr.rows[i].ALIAS + "\",";
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
      jsonStr += "\"time\""+ ":" + "\"" + arr.rows[i].TIME + "\",";	
      jsonStr += "\"starttime\""+ ":" + "\"" + arr.rows[i].STARTTIME + "\",";	
      jsonStr += "\"name\""+ ":" + "\"" + arr.rows[i].NAME + "\",";
      jsonStr += "\"type\""+ ":" + "\"" + arr.rows[i].TYPE + "\",";
      jsonStr += "\"typeid\""+ ":" + "\"" + arr.rows[i].TYPEID + "\",";
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

	 //验证仅执行一次计划的时间小于当前时间
	    var rows = jsonToStrPlan($('#taskPlanList').datagrid('getData'));
		var json = eval( "(" + rows + ")" );//json String变成对象
	    for(i=0;i<json.rows.length;i++){
			   var typeid = json.rows[i].typeid;
				 if(typeid=='0'){
				    // alert("json.rows[i]:"+rows);
				     var da1 = $.trim($("#plan_data_one").val())+ " " + $.trim($("#plan_time_one").val());	
				     var starttime = json.rows[i].starttime;
				     var time = json.rows[i].time;
				    // alert("time:"+time);
				     start = starttime + " " + time;
				   //  alert(start);
					 if(isTime(start,new Date())){
					     $.messager.alert('提示',"仅执行一次计划："+json.rows[i].name+'的执行时间需大于当前时间');
					     $('#baseDiv').tabs('select','计划');
					     return false;
				     }
		     }
		   }	
	
     //验证邮箱
	 var noticeflag=$('input:radio[name="COMPLETEFLAG"]:checked').val();
	 var mailName = $("#MAILNAME").val();  	
	 if(mailName!=""){
		 var mail= new Array(); //定义一数组
		 var yxzz=/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
		 mail = mailName.split(";");
		 for(i=0;i<mail.length ;i++ ) {
			if(mail[i]!=""){
			 if(!yxzz.test(mail[i])){
				 $.messager.alert('提示',mail[i]+'的邮箱名格式不正确！');
				 $('#baseDiv').tabs('select','通知');
				 return false;
			  }	 
		   }
		 }
	 }
	 //验证邮件和完成状态的选择情况
	 if(noticeflag==null){
		 if(mailName!=""){
			$.messager.alert('提示','请选择邮件发送状态！');
			$('#baseDiv').tabs('select','通知');
			return false; 
		 }
	 }
	if(mailName==""){
			 if(noticeflag !=null){
				$.messager.alert('提示','请选择要发送的邮件'); 
				$('#baseDiv').tabs('select','通知');
				return false; 
			 }
	 }	 
	return $("#insertForm").valid();

} 

/*
 * 新增,清空form 以及数据从第一页重新加载.
 * 异步提交服务器，保存数据
 */
var flage = false;
function btnSava(){
}

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
function addStepToGrid(workflowidParam,subtaskidParam,nameParam,explainParam,runtype){
	
	var rows = jsonToStrStep($('#taskStepList').datagrid('getData'));
	var json = eval( "(" + rows + ")" );//json String变成对象
	var order = json.rows.length;
	var orders = 0;
	if(order>0){
       if(runtype==0){
		   for(i=0;i<json.rows.length;i++){
			   if(workflowidParam==json.rows[i].workflowid){ //判断workflow是否已经添加已添加的话，覆盖		   				 
				   $('#taskStepList').datagrid('deleteRow',i);				
			     }
			   }
	   }else{
		   for(i=0;i<json.rows.length;i++){        
			   if(subtaskidParam==json.rows[i].subtaskid){//判断任务是否已经添加已添加的话，覆盖		   
				   $('#taskStepList').datagrid('deleteRow',i);	         		
			     }			
			   }
	   }
		orders = parseInt(json.rows[order-1].orders)+1;
	}
	
	
	var row = {"ORDERS":orders,"SUBTASKID":subtaskidParam,"WORKFLOWID":workflowidParam,"ALIAS":nameParam,"EXPLAIN":explainParam};
	$("#taskStepList").datagrid("appendRow", row);
}


//添加计划到一览数据里
function addPlanToGrid(idParam,nameParam,timeParam,starttimeParam,typeParam,typeidParam,explainParam){
	var rows = jsonToStrPlan($('#taskPlanList').datagrid('getData'));
	var json = eval( "(" + rows + ")" );//json String变成对象
	if(json.rows.length>0){
	   for(i=0;i<json.rows.length;i++){
		   	   if(idParam==json.rows[i].planid){
		   		   //判断workflow是否已经添加已添加的话，覆盖	   		 
			  $('#taskPlanList').datagrid('deleteRow',i);	         
		     }
	     }
	   }	
	var row = {"PLANID":idParam,"NAME":nameParam,"TIME":timeParam,"STARTTIME":starttimeParam,"TYPE":typeParam,"TYPEID":typeidParam,"EXPLAIN":explainParam};
	
	$("#taskPlanList").datagrid("appendRow", row);
}

//保存按钮调用方法
function btnSave(){
	$('#baseDiv').tabs('select','属性');
	if(showRequest()){ //验证
		var stepRow = jsonToStrStep($('#taskStepList').datagrid('getData'));
      	var planRow = jsonToStrPlan($('#taskPlanList').datagrid('getData'));
      	var noticeflag = $("input[@type=radio][name=COMPLETEFLAG][checked]").val();
	    //alert("taskname:"+$("#TASKNAME").val()+"----->"+"runtype:"+$('#prjstatusvalue')[0].value+"----->"+"explain:"+$("#EXPLAIN").val())
		$add = {
				   'taskname':$("#TASKNAME").val(),
   		           'runtype':$("#RUNTYPE").val(),
   		           'explain':$("#EXPLAIN").val(),	
   		           'stepRows':stepRow, // 步骤	     
   		           'planRows':planRow,   //计划 
   		           'emailID':$("#MAILNAME").val(),	           
   		           'noticeflag':noticeflag,
   		           'type':0
		}
		var  urlParam = "<%=baseUrl%>"+"/task/add";     // 新增操作
		var  urlEdit = "<%=baseUrl%>"+"/task/edit?taskid="+idSeq;//编辑操作
		// 请求后台, 保存数据
		 if(null != idSeq&&0!=idSeq){
			// alert("修改");
				$.post(urlEdit,$add,function(data){
				    var data = eval( "(" + data + ")" );//json String变成对象
					if(data.success){		
							    $.messager.alert('提示信息',messageInfo.editSuccess, 'info', function(){
								parent.$("#taskList").datagrid('load');
								parent.$('#editTask').dialog('close');
							});
						}else{				
								
							$.messager.alert('提示信息',messageInfo.editFail,'warning');		
					}
				}); 	
		}else{
		//alert("插入");
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

/*
*查看是否选中行(编辑)
*/
function getSelectionsMove(){

	var ids = new Array();
	var rows = $('#taskStepList').datagrid('getSelected');	
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
*删除选中多行
*/
function getDeleteSelections(){
	var ids = new Array();
	var rows = $('#taskPlanList').datagrid('getSelections');
	for(var i=0;i<rows.length;i++){
		ids.push(rows[i].id);
	}
	if(rows.length==0){
		$.messager.alert('提示信息',"至少选择一项");		
		 return false;
	}
	return ids;
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

<body>
<form method="get" id="insertForm">
<div class="tbar">
	<a href="javascript:btnSave();" class="easyui-linkbutton" icon="icon-save" plain="true">保存</a>
	<a href="javascript:closeWin();" class="easyui-linkbutton" icon="icon-undo" plain="true">取消</a>
</div>
<div class="easyui-tabs" id="baseDiv">
	<div title="属性">
         <table cellpadding="0" cellspacing="0" border="0" id="uiform" class="form_table">	
		 <tr>
			<td width="35%" class="form_title">任务名称：</td>
			<td width="15%" class="form_content">
				<input type="text" id="TASKNAME" name ="TASKNAME" class="easyui-validatebox"  maxLength="200"  />
			    <font color="red">*</font>
			</td>			
			</tr>			
		<tr>
			<td width="35%" class="form_title">运行方式：</td>
			<td width="15%" class="form_content">		
				<select id="RUNTYPE" name="RUNTYPE" style="width: 26.5%"  onclick="updownControl()">	
				<option value="0" >串行</option>
				<option value="1">并行</option>	
				</select>
				<font color="red">*</font>		
			</td>
		</tr>			
		<tr>
			<td class="form_title">说明：</td>
            <td  class="form_content" colspan ="3">
			<textarea  id="EXPLAIN"  name="EXPLAIN" style="width:496px;height:100px;border:1px solid #7F9DB9;background-color:#FFFFFF;voerflow-x:true; " ></textarea> 
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
		                <td width="10%" class="form_content"><input type="radio" name="COMPLETEFLAG" value="0"/></td>
		                <td width="10%" class="form_title">成功</td>
		                <td width="10%" class="form_content"><input type="radio" name="COMPLETEFLAG" value="1"/></td>
		                <td width="10%" class="form_title">失败</td>
		                <td width="10%" class="form_content"><input type="radio" name="COMPLETEFLAG" value="2"/></td>
		                </tr> 
		             </table>
		        </td>
		    </tr>
		    <tr>
		        <td width="6%" class="form_title">
		                        邮件名称：  
		        </td>
		        <td width="35%" >
				 &nbsp;<input type="text" style="width:90%" id="MAILNAME" name = "MAILNAME" />
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
		