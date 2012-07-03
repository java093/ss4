<%@ page language="java"  contentType="text/html; charset=UTF-8"
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
<link rel="stylesheet" type="text/css" href="<%= baseUrl%>/script/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%= baseUrl%>/script/easyui/themes/icon.css"> 
<script type="text/javascript" src="<%=baseUrl%>/script/ajaxfileupload.js"></script>
<script type="text/javascript" src="<%= baseUrl%>/script/easyui/jquery.easyui.min.js"></script>
<title>任务编辑</title>

<script type="text/javascript">
var tempCom = eval("[{'VALUE':'','NAME':'请选择'}]");
var selectRole;

$(document).ready(function(){
	//URL传参
	var id =  getUrlParam("id");
	var task=null;
	$.ajax({
		url : "<%=baseUrl%>/task/findbyid",
		dataType :"json",
		data : {
			id :id
		},
		type :"GET",
		error:function(){$.messager.alert('提示信息',"数据加载失败。。。","error");},
		success : function(data) {
			task = data;
			$('#insertForm').form('load',task);	
		}						
	});		
});

/*
 *提交前，数据验证
 */
function showRequest() {
	if($.trim($("#name").val())==""){
	 	$.messager.alert('提示','任务名称不能为空'); 
	     return false;
 	}
	 if($.trim($("#status").val())==""){
	 	$.messager.alert('提示','运行方式不能为空'); 
	    return false;
	 }   
	 return true;
} 

/*
 * 新增,清空form 以及数据从第一页重新加载.
 * 异步提交服务器，保存数据
 */
 var flage = false;
 
function taskEditsave(){
	if(showRequest()){
	//URL传参
	var id =  getUrlParam("id");
	var options = {
        url:"<%=baseUrl%>/task/edittask",
        type: "post",
        dataType:'json',	
        data:{
	        'name':$("#name").val(),
	        'status':$("#status").val(),
	        'memo':$("#memo").val(),
	        'id':id
	        },                   
	    success:function(data){
    	    if(data.success){
    	    	$.messager.alert('提示','数据保存成功','info', 
    	    	  function(){
    	    		parent.$('#taskList').datagrid('load');
    	    		parent.$('#jsjleditpage').dialog('close');
    	    		
    	    	  });
    	    }else{
    	    	$.messager.alert('提示',data.exceptionMsg);
    	    }
	    },
	    error:function(){$.messager.alert('提示信息',"数据保存失败。。。","error");},
	    clearForm: true       
    };
       
        /*
        *异步提交
        */
	$("#insertForm").ajaxSubmit(options);
	}	
}

/*
 * 关闭弹出页面
 */
function closeWin(){
	parent.$('#taskList').datagrid('load');
	parent.$('#jsjladdpage').dialog('close');  	
}




</script>


</head>

<body style="background:#EFF0F2">
<form method="get" id="insertForm">
<table cellpadding="0" cellspacing="0" border="0" id="uiform" class="form_table"> 
  <tr>
    <td >
		<div class="tbar">
		    <a href="#" class="easyui-linkbutton" onclick="taskEditsave();" icon="icon-save" plain="true">保存</a>
			<a href="#" class="easyui-linkbutton" onclick="caseSrhEditsubmit();" icon="icon-redo" plain="true">取消</a>
		</div>
    </td>
  </tr>
  <tr>
    <td >	

	  <table cellpadding="0" cellspacing="0" border="0" id="uiform" class="form_table">	
		<tr>
			<td width="25%" class="form_title">任务名称：</td>
			<td width="25%" class="form_content">
				<input type="text" id="name" name = "name" maxLength="200"  />
				<font color="red">*</font>
	 			<input	type="hidden" name="fileIds" id="fileIds" />
			</td>
			</tr>
			
		<tr>
			<td width="25%" class="form_title">运行方式：</td>
			<td width="25%" class="form_content">		
				<select name="status" id="status" name="runStyle">
				<option value="0">串行 </option>
				<option value="1">并行</option>			
			</select>
				<font color="red">*</font>
			</td>
		</tr>
			
		<tr>
			<td class="form_title">说明：</td>
            <td  class="form_content" colspan ="3">
			<textarea  id="memo"  name="memo" style="width:496px;height:100px;border:1px solid #7F9DB9;background-color:#FFFFFF;voerflow-x:true; " ></textarea> 
			</td>
		</tr>	
		
	 </table>
</td>
</tr>
</table>
</form>
</body>
</html>