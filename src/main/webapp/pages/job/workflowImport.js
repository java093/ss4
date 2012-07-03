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
});

//二级联动
function setworkFlowLibrary(inforid){
	  $.ajax({
		  url: baseUrl+"/workflow/workflowSelectDataBase?inforid="+inforid,
          dataType : "json",
          type : "POST",	  	  
          success : function(json) {
        	  
  	  	    	  for(var i=0;i<json.length;i++){  
  	         	   $("#workFlowLibraryId").append("<option value='"+json[i].id+"'>"+json[i].title+"</option>");  
  	              }  
  	    	     	  
  	  	    	 
          }
  	  	    
	    });
}


/*
*查看是否选中行
*/
function getSelections(){
	var ids = new Array();
	var rows = $('#workFlowImportList').datagrid('getSelections');
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
 * 点击确认按钮时，根据服务器和知识库查询workflow.
 */
function confirmWorkFlow(){
	var workId = $("#workFlowLibraryId ").val();
	var title = $("#workFlowServerId").find("option:selected").text();
	var dbname = $("#workFlowLibraryId").find("option:selected").text();
//	$.post(baseUrl+"/workflow/confirmWorkFlow", { title: title, dbname: dbname });
	pageList(workId,title,dbname);
	
	

}

/*
 * 点击确认按钮时，根据服务器和知识库查询workflow.
 */
function confirmWorkFlow_Two(){
	var workId = $("#workFlowServerId ").val();
	var title = $("#workFlowServerId").find("option:selected").text();
	//pageList_Two(workId,title);
	
	//informatica配置信息为空时，加一个非空提示
	if(workId != null){
		pageList_Two(workId,title);
	}else{
		$.messager.alert('提示信息',"INFOMATIC配置信息为空，请先配置好INFOMATIC信息。");
	}

}


/*
 * 展示数据
 */
function pageList_Two(workId,title){
	var urlstring  =baseUrl+"/workflow/confirmWorkFlow?workId="+workId+"&title="+title;
	$('#workFlowImportList').datagrid({
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
            {title:'文件夹',field:'SUBJECT_AREA',width:$(this).width()*0.1,sortable:false},
            {title:'workflow名称',field:'WORKFLOW_NAME',width:$(this).width()*0.1,sortable:false},
            {title:'生成时间',field:'START_TIME',width:$(this).width()*0.1,sortable:false},
            {title:'最后修改时间',field:'END_TIME',width:$(this).width()*0.1,sortable:false}
		]],				
		onHeaderContextMenu: function(e, field){
			e.preventDefault();
			if (!$('#tmenu').length){
				createColumnMenu('#workFlowImportList');
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
	
	var pager = $('#workFlowImportList').datagrid('getPager');
    $(pager).pagination({showPageList:false});
	/*
	*字符转JSON
	*/
	 function strToJson(str){ var json = eval(str ); return json; }
}


/*
 * 展示数据
 */
function pageList(workId,title,dbname){
	var urlstring  =baseUrl+"/workflow/confirmWorkFlow?workId="+workId+"&title="+title+"&dbname="+dbname;
	$('#workFlowImportList').datagrid({
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
            {title:'文件夹',field:'SUBJECT_AREA',width:$(this).width()*0.1,sortable:true},
            {title:'文件流名称',field:'WORKFLOW_NAME',width:$(this).width()*0.1,sortable:true},
            {title:'生成时间',field:'START_TIME',width:$(this).width()*0.1,sortable:true},
            {title:'最后修改时间',field:'END_TIME',width:$(this).width()*0.1,sortable:true}
		]],				
		onHeaderContextMenu: function(e, field){
			e.preventDefault();
			if (!$('#tmenu').length){
				createColumnMenu('#workFlowImportList');
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
	
	var pager = $('#workFlowImportList').datagrid('getPager');
    $(pager).pagination({showPageList:false});
	/*
	*字符转JSON
	*/
	 function strToJson(str){ var json = eval(str ); return json; }
}

/*
 * 把选择的workflow导入到目标数据库。
 */
function importToData(){
    var ids = getDeleteSelections();
    if(ids){
    $.messager.confirm('提示信息','您是否确认导入所选的workflow?',function(r){ 
 	   if(r){
	        $.ajax({
	  	          url : baseUrl+"/workflow/importToData?ids="+ids,
	  	          dataType : "json",
	  	          type : "POST",	  	  
	  	          success : function(data) {
	      	  	      if(data){
	      	  	    	if(data.success){
	      	  	    	$.messager.alert('提示信息',"workflow导入成功,如果workflow已存在，将不会重复导入,而是更新已存在的workflow。",'info', 
    	    	    	  function(){
    	    	    		parent.$('#workflowlist').datagrid('load');
    	    	    		parent.$('#jsjleditpage').dialog('close');  	    	    		
    	    	    	  });  
	      	    	   }else{
	      	    	    	$.messager.alert('提示信息',"workflow导入失败!");
	      	    	    }	
	      	  	    	      	       
	      	  	      }
	  	         }
	     	 });
	   }
	   });
    }
}

/*
*选中多行workflow
*/
function getDeleteSelections(){
	var ids = new Array();
	var rows = $('#workFlowImportList').datagrid('getSelections');
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
 * 取消导入
 */
function closeWin(){
	parent.$('#jsjleditpage').dialog('close'); 	
}
