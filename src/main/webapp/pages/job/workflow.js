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
	/*
	 * limit :为每页的记录数(common.js参得)
	 */	
	$('#workflowlist').datagrid({
		title:'workflow 一览',
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
            {title:'workflow名称',field:'WORKFLOW_NAME',width:$(this).width()*0.08,sortable:false},
            {title:'别名',field:'ALIAS',width:$(this).width()*0.07,sortable:false},
            {title:'文件夹',field:'SUBJECT_AREA',width:$(this).width()*0.07,sortable:false},
            {title:'用户名称',field:'UPDATEUSERID',width:$(this).width()*0.1,sortable:false},
            {title:'服务器名称',field:'INFAMATIC',width:$(this).width()*0.08,sortable:false},
            {title:'生成时间',field:'CRTTIME',width:$(this).width()*0.12,sortable:false},
            {title:'最后修改时间',field:'UPTTIME',width:$(this).width()*0.12,sortable:false},
            {title:'说明',field:'EXPLAIN',width:$(this).width()*0.08,sortable:false}
		]],				
		onHeaderContextMenu: function(e, field){
			e.preventDefault();
			if (!$('#tmenu').length){
				createColumnMenu('#workflowlist');
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
	
	var pager = $('#workflowlist').datagrid('getPager');
    $(pager).pagination({showPageList:false});

   	/*
	*字符转JSON
	*/
	 function strToJson(str){ var json = eval(str ); return json; }
	 
});



/*
打开修改页，返回刷新当前页(保留在当前页码以及查询条件不变)
*/
function updateWorkFlow(){
	
	var id=getSelections();
	if(id){
		var url = "../pages/job/workflow_edit.jsp?id="+id;
		createWindow("jsjleditpage","workflow—编辑",true,"normal",false,500,310,url);
	}
}


/*
*查看是否选中行
*/
function getSelections(){
	var ids = new Array();
	var rows = $('#workflowlist').datagrid('getSelections');
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
 * 删除workflow
 */
function deleteWorkFlow(){
    var ids = getDeleteSelections();
    if(ids){
    $.messager.confirm('提示信息','您是否确认删除所选项?',function(r){ 
 	   if(r){
	        $.ajax({
	  	          url : baseUrl+"/workflow/deleteWorkFlow?ids="+ids,
	  	          dataType : "json",
	  	          type : "POST",	  	  
	  	          success : function(data) {
	      	  	      if(data){
	      	  	    	if(data.success){
	      	  	    	$.messager.alert('提示信息',"数据删除成功!");
	      	    	    	$('#workflowlist').datagrid('reload'); 
	      	    	    	$('#workflowlist').datagrid('clearSelections');
	      	    	    }else{
	      	    	    	$.messager.alert('提示信息',"数据删除失败!");
	      	    	    }	
	      	  	    	      	       
	      	  	      }
	  	         }
	     	 });
	   }
	   });
    }
}

/*
*删除选中多行
*/
function getDeleteSelections(){
	var ids = new Array();
	var rows = $('#workflowlist').datagrid('getSelections');
	for(var i=0;i<rows.length;i++){
		ids.push(rows[i].id);
	}
	if(rows.length==0){
		$.messager.alert('提示信息',"至少选择一项");
		
		 return false;
	}
	return ids;
}

/*
 * 根据workflow别名模糊查询。
 */
function findListByName(){
	
	var urlstring = baseUrl + '/workflow/listByName';
	$query = {
		name: $.trim($("#ALIAS").val()),
		infa_id:$("#workFlowServerId").find("option:selected").val()
	};
	var selectRows = $('#workflowlist').datagrid('getSelections');	
	if (selectRows.length > 0) {
		$('#workflowlist').datagrid('clearSelections');
	}
	$("#workflowlist").datagrid('options').url =urlstring;
	$("#workflowlist").datagrid('options').queryParams = $query;
	$("#workflowlist").datagrid('load');
}
/*
	导入
 */
function importButton(){
	
		var url = "../pages/job/workflowImport.jsp";
		createWindow("jsjleditpage","workflow导入",true,"normal",false,730,410,url);
	
}
