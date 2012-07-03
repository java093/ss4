//初始化状态
$(document).ready(function(){
	var id =  getUrlParam("id");

		//URL传参
		//alert(getUrlParam("id"));
		var plan = null;
		$.ajax({
			url : baseUrl+'/plan/findbyid',
			dataType :"json",
			data : {
				id :id
			},
			type :"GET",
			error:function(){$.messager.alert('提示信息',"数据加载失败。。。","error");},
			success : function(data) {
				
//				plan = data;
//				alert(data.name);
//				alert(data.explain);
//				alert(data.type);
//				alert(data.time);
//				alert(data.startedtime);
//				alert(data.enddate);
//				alert(data.day_plan_month);
//				alert(data.start_flag);
				setDocument(data.name,data.explain,data.type,data.time,data.startedtime,data.enddate,data.enddate,data.day_plan_month,data.start_flag);
				
//				$('#addPrivilegeForm').form('load',plan);	
				
			}						
		});	
		
		// 验证规则初始化
		$('#addPrivilegeForm').validate({
			rules : {
				name : {
					required : true,
					maxlength : 100,
					remote : {
						url : baseUrl+'/plan/findbyname2',
						type : 'POST',
						dateType : 'json',
						data: {                     //要传递的数据
							id : getUrlParam("id")
						}
					}
				},
				explain :{
					maxlength : 250
				},
				plan_type : {
					required : true
				},
				plan_time_one : {
					required : true
				},
				plan_data_one : {
					required : true
				},
				plan_data_day : {
					required : true
				},
				plan_time1_day : {
					required : true
				},
				plan_time2_day : {
					required : true
				},
				plan_data_month : {
					required : true
				},
				plan_time1_month : {
					required : true
				},
				plan_time2_month : {
					required : true
				}
				
			},
			messages : {
				name : {
					remote : "计划名称已存在！"
				}
			}
		});
});

//单选框
var checkNUM = 0;
//每日复选框
var checkValue = null;

//选中redio
function getRadioBoxValue(radioName, radiovalue)  
{  
//	alert(radioName);
//	alert(radiovalue);
  var obj = document.getElementsByName(radioName);
  for(i = 0; i < obj.length; i++){  
    if(obj[i].value == radiovalue){  
      obj[i].checked = true;
    }  
  }
}




function setDocument(name,explain,type,time,startedtime,enddate,enddate,day_plan_month,start_flag){
//	alert(name);
//	alert(explain);
//	alert(day_plan_month);
	
	document.getElementById("name").value = name; 
	document.getElementById("explain").value = explain; 
	
	if(start_flag == 1){
		document.getElementById("plan_start").checked = true; 
	}
	getRadioBoxValue("plan_type",type);
	
	
	if(type == 0){
		aCheck1();
		document.getElementById("plan_time_one").value = time; 
		document.getElementById("plan_data_one").value = startedtime;
	}else if (type == 1){
		bCheck2();
		document.getElementById("plan_data_day").value = time; 
		document.getElementById("plan_time1_day").value = startedtime;
		document.getElementById("plan_time2_day").value = enddate;
	}else if (type == 2){
		bCheck3();
		arr = day_plan_month.split(",");
		for(var i=0; i<arr.length; i++){
			getRadioBoxValue("plan_month",arr[i]);
		}
		document.getElementById("plan_data_month").value = time; 
		document.getElementById("plan_time1_month").value = startedtime;
		document.getElementById("plan_time2_month").value = enddate;
	}
}






function aCheck1(){
	document.getElementById("span1").style.display="block";
	document.getElementById("span2").style.display="none";
	document.getElementById("span0").style.display="none";
	document.getElementById("span3").style.display="none";
	checkNUM = 0;
	
}
function bCheck2(){
	document.getElementById("span1").style.display="none";  
	document.getElementById("span2").style.display="block";
	document.getElementById("span0").style.display="none";
	document.getElementById("span3").style.display="none";
	checkNUM = 1;
}  
	
function bCheck3(){
	document.getElementById("span1").style.display="none";  
	document.getElementById("span2").style.display="none";
	document.getElementById("span0").style.display="none";
	document.getElementById("span3").style.display="block";
	checkNUM = 2;
}
	
/*
 * 关闭弹出页面
 */
function closeWin(){
	parent.$('#planList').datagrid('load');
	parent.$('#jsjleditplan').dialog('close');  	
}	

/**取出checkboxValue的值***/
function checkboxValue(){
	checkValue = null;
   	var blackName = document.getElementsByName("plan_month");
   
    for(var i = 0; i < blackName.length; i++)
    {
       if(blackName[i].checked == true)
       {
    	   checkValue += "," + blackName[i].value;
       }
    }
} 

//判断check是否选中
function check(){   
	if(document.getElementById("plan_start").checked){
	    return 1;
	}
	return 0;
}  

/***提交前判断**/
function showRequest2(){
	if($.trim($("#name").val())==""){
	 	$.messager.alert('提示','计划名称不能为空'); 
	 	return false;
	} 
	if(null == checkNUM ){
		$.messager.alert('提示','计划类型不能为空'); 
		return false;
	}else{
		if(checkNUM == 0){
			if($.trim($("#plan_data_one").val())==""){
			 	$.messager.alert('提示','日期不能为空'); 
			 	return false;
			} 
			
			if($.trim($("#plan_time_one").val())==""){
			 	$.messager.alert('提示','时间不能为空'); 
			return false;
			} 
			
			var da1 = $.trim($("#plan_data_one").val())+ " " + $.trim($("#plan_time_one").val());			
			if(isTime(da1,new Date())){
				// alert(new date());
				$.messager.alert('提示','起始时间需大于当前时间');
				return false;
			}
		}
		if(checkNUM == 1){
			if($.trim($("#plan_data_day").val())==""){
			 	$.messager.alert('提示','执行时间不能为空'); 
			 	return false;
			} 
			if($.trim($("#plan_time1_day").val())==""){
			 	$.messager.alert('提示','开始日期不能为空'); 
			 	return false;
			} 
			if($.trim($("#plan_time2_day").val())==""){
			 	$.messager.alert('提示','结束日期不能为空');
			 	return false;
			} 

			var da1 = $.trim($("#plan_time1_day").val())+ " " + $.trim($("#plan_data_day").val());			
			if(isTime(da1,new Date())){
				// alert(new date());
				$.messager.alert('提示','起始时间需大于当前时间');
				return false;
			}
			
			if(!isDate($.trim($("#plan_time1_day").val()),$.trim($("#plan_time2_day").val()))){
				$.messager.alert('提示','结束时间需大于开始时间');
				return false;
			}
		}
		if(checkNUM == 2){

			if($.trim($("#plan_data_month").val())==""){
			 	$.messager.alert('提示','执行时间不能为空'); 
			 	return false;
			} 
			if($.trim($("#plan_time1_month").val())==""){
			 	$.messager.alert('提示','开始日期不能为空'); 
			 	return false;
			} 
			if($.trim($("#plan_time2_month").val())==""){
			 	$.messager.alert('提示','结束日期不能为空'); 
			 	return false;
			} 
			checkboxValue();
			if(null == checkValue){
			 	$.messager.alert('提示','请选择执行日'); 
			 	return false;
			} 
			var da1 = $.trim($("#plan_time1_month").val())+ " " + $.trim($("#plan_data_month").val());			
			if(isTime(da1,new Date())){
				// alert(new date());
				$.messager.alert('提示','起始时间需大于当前时间');
				return false;
			}
			if(!isDate($.trim($("#plan_time1_month").val()),$.trim($("#plan_time2_month").val()))){
				$.messager.alert('提示','结束时间需大于开始时间');
				return false;
			}
		}
	}
	return true;
//	isDate($.trim($("#plan_time1_day").val()),$.trim($("#plan_time2_day").val()));
//	return false;
}	

//调用验证方法
function showRequest(formData, jqForm, options) {
	return $("#addPrivilegeForm").valid();
}

/*
 * 新增,清空form 以及数据从第一页重新加载.
 * 异步提交服务器，保存数据
 */
var flage = false;
function caseSrhEditsave(){
	//alert("11111");
	var ckn = check();
	if(showRequest() && showRequest2()){
		if(checkNUM == 0){
		    var options = {
		    		url:baseUrl+'/plan/update',
	    	        type: "post",
	    	        dataType:'json',	
	    	        data:{
	    	        	'id':getUrlParam("id"),
	    		        'plan_name':$("#name").val(),
	    		        //'plan_type':checkNUM,
	    		        'plan_state':$("#explain").val(),
	    		        'plan_start':ckn,
	    		        'time':$("#plan_time_one").val(),
	    		        'stardate':$("#plan_data_one").val()
	    		       
	    		        },     
	    		    beforeSubmit : showRequest,    
	    		    success:function(data){
	    	    	    if(data.success){
	    	    	    	$.messager.alert('提示','数据保存成功','info', 
	    	    	    	  function(){
	    	    	    		parent.$('#planList').datagrid('load');
	    	    	    		parent.$('#jsjleditplan').dialog('close');  	    	    		
	    	    	    	  });
	    	    	    }else{
	    	    	    	$.messager.alert('提示',data.exceptionMsg);
	    	    	    }
	    		    },
	    		    error:function(){$.messager.alert('提示信息',"数据保存失败。。。","error");},
	    		    clearForm: true       
	    	    };
		}else if (checkNUM == 1){
		    var options = {
		    		
		    		url:baseUrl+'/plan/update',
	    	        type: "post",
	    	        dataType:'json',	
	    	        data:{
	    	        	'id':getUrlParam("id"),
	    		        'plan_name':$("#name").val(),
	    		        //'plan_type':checkNUM,
	    		        'plan_state':$("#explain").val(),
	    		        'plan_start':ckn,
	    		        'time':$("#plan_data_day").val(),
	    		        'stardate':$("#plan_time1_day").val(),
	    		        'enddate':$("#plan_time2_day").val()
	    		       
	    		        },    
		    		    beforeSubmit : showRequest,    
		    		    success:function(data){
		    	    	    if(data.success){
		    	    	    	$.messager.alert('提示','数据保存成功','info', 
		    	    	    	  function(){
		    	    	    		parent.$('#planList').datagrid('load');
		    	    	    		parent.$('#jsjleditplan').dialog('close');  	    	    		
		    	    	    	  });
		    	    	    }else{
		    	    	    	$.messager.alert('提示',data.exceptionMsg);
		    	    	    }
		    		    },
		    		    error:function(){$.messager.alert('提示信息',"数据保存失败。。。","error");},
		    		    clearForm: true        
	    	    };
		}else if (checkNUM == 2){
		    var options = {
		    		url:baseUrl+'/plan/update',
	    	        type: "post",
	    	        dataType:'json',	
	    	        data:{
	    	        	'id':getUrlParam("id"),
	    		        'plan_name':$("#name").val(),
	    		        //'plan_type':checkNUM,
	    		        'plan_state':$("#explain").val(),
	    		        'plan_start':ckn,
	    		        'time':$("#plan_data_month").val(),
	    		        'stardate':$("#plan_time1_month").val(),
	    		        'enddate':$("#plan_time2_month").val(),
	    		        'day_plan_month':checkValue
	    		       
	    		        },   
		    		    beforeSubmit : showRequest,    
		    		    success:function(data){
		    	    	    if(data.success){
		    	    	    	$.messager.alert('提示','数据保存成功','info', 
		    	    	    	  function(){
		    	    	    		parent.$('#planList').datagrid('load');
		    	    	    		parent.$('#jsjleditplan').dialog('close');  	    	    		
		    	    	    	  });
		    	    	    }else{
		    	    	    	$.messager.alert('提示',data.exceptionMsg);
		    	    	    }
		    		    },
		    		    error:function(){$.messager.alert('提示信息',"数据保存失败。。。","error");},
		    		    clearForm: true   
	    	    };
		}

    	      
	}
    	        /*
    	        *异步提交
    	        */
    		$("#addPrivilegeForm").ajaxSubmit(options);
}