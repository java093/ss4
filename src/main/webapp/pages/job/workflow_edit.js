//初始化状态
$(document).ready(function(){
	//URL传参
	var id =  getUrlParam("id");
	var workflow=null;
	$.ajax({
		url : baseUrl+'/workflow/findById',
		dataType :"json",
		data : {
			id :id
		},
		type :"GET",
		error:function(){$.messager.alert('提示信息',"数据加载失败。。。","error");},
		success : function(data) {
			workflow = data;
			$('#workflowForm').form('load',workflow);	
		}						
	});		
	
	// 验证规则初始化
	$('#workflowForm').validate({
		rules : {
			ALIAS : {
				required : true,
				maxlength : 100,
				remote: {
					url: baseUrl+'/workflow/findbyALIAS',
					type: 'POST',
					dateType: 'json',
					data: {                     //要传递的数据
						id : id
					}
				}
			},
			EXPLAIN : {
				maxlength : 400		
			}
		},
		messages: {
			ALIAS:{
	    		  remote:"别名已存在！"
	    		} 
	     } 	
	});
	
});



/*
 * 编辑workflow
 */

function workflowEditSave(){

	//URL传参
	var id =  getUrlParam("id");
    var options = {
    	        url:baseUrl+'/workflow/update',
    	        type: "post",
    	        dataType:'json',	
    	        data:{
    	        	'id':id,
    		        'ALIAS':$("#ALIAS").val(),
    		        'EXPLAIN':$("#EXPLAIN").val()
    		        },   
    		    beforeSubmit : showRequest,
    		    success:function(data){
    	    	    if(data.success){
    	    	    	$.messager.alert('提示','数据编辑成功','info', 
    	    	    	  function(){
    	    	    		parent.$('#workflowlist').datagrid('load');
    	    	    		parent.$('#jsjleditpage').dialog('close');  	    	    		
    	    	    	  });
    	    	    }else{
    	    	    	$.messager.alert('提示',data.exceptionMsg);
    	    	    }
    		    },
    		    error:function(){$.messager.alert('提示信息',"数据编辑失败。。。","error");},
    		    clearForm: true       
    	    };
    	      
    	        /*
    	        *异步提交
    	        */
    		$("#workflowForm").ajaxSubmit(options);
}
/*
 * 关闭弹出页面
 */
function closeWin(){
	parent.$('#workflowlist').datagrid('load');
	parent.$('#jsjleditpage').dialog('close');  	
}

//调用验证方法
function showRequest(formData, jqForm, options) {
	return $("#workflowForm").valid();
}