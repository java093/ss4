//初始化列表
$(document).ready(function(){
	var urlstring  =baseUrl+'/task/mainlist';
	/*
	 * limit :为每页的记录数(common.js参得)
	 */	
	$('#taskList').datagrid({
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
            {title:'ID',field:'id',width:$(this).width()*0.06,sortable:false,hidden:true},
            {title:'任务名称',field:'TASKNAME',width:$(this).width()*0.1,sortable:false},
            {title:'最后修改人',field:'UPDATESERID',width:$(this).width()*0.1,sortable:false},
            {title:'运行方式',field:'RUNTYPE',width:$(this).width()*0.1,sortable:false},
            {title:'生成时间',field:'CRTTIME',width:$(this).width()*0.1,sortable:false},
            {title:'修改时间',field:'UPTTIME',width:$(this).width()*0.1,sortable:false}
         
		]],	
		onDblClickRow: function(rowIndex, rowData){
			var url = "../pages/job/mainTask_view.jsp?id="+rowData.id;
			createWindow("viewTask","任务查看",true,"normal",false,630,480,url);	
		},
		onHeaderContextMenu: function(e, field){
			e.preventDefault();
			if (!$('#tmenu').length){
				createColumnMenu('#taskList');
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
	
	var pager = $('#taskList').datagrid('getPager');
    $(pager).pagination({showPageList:false});

   	/*
	*字符转JSON
	*/
	 function strToJson(str){ var json = eval(str ); return json; } 
});

/*
 * 新增编辑 ，返回重新加载Grid
*/ 
function taskadd() {
		createWindow("editTask","主任务新增",true,"normal",false,630,480,"../pages/job/maintask_add.jsp?id=0");	
}


/*
打开修改页，返回刷新当前页(保留在当前页码以及查询条件不变)
*/
function taskupdate(){

	var id=getSelections();
	if(id){
		var url = "../pages/job/maintask_add.jsp?id="+id;
		createWindow("editTask","主任务编辑",true,"normal",false,630,480,url);	
	}
}

function taskview(){
	var id=getSelections();
	if(id){
		var url = "../pages/job/mainTask_view.jsp?id="+id;
		createWindow("viewTask","任务查看",true,"normal",false,630,480,url);	
	}
}


/*
 * 删除任务
 */
function deleteTasks(){
       var ids = getDeleteSelections();
       if(ids){
       $.messager.confirm('提示信息','您是否确认删除所选项?',function(r){ 
    	   if(r){
	        $.ajax({
	  	          url : baseUrl+"/task/delete?ids="+ids+"&type=0",
	  	          dataType : "json",
	  	          type : "POST",	  	  
	  	          success : function(data) {
	      	  	      if(data){
	      	  	    	if(data.success){
	      	  	    	$.messager.alert('提示信息',"数据删除成功!");
	      		    	    $('#taskList').datagrid('reload'); 
	      	    	    	$('#taskList').datagrid('clearSelections');
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


function searchTASK(){
	var urlstring = baseUrl + '/task/listByName';
	$query = {
		 name: $.trim($("#TASKNAME").val()),
		 type: 0,
		 runtype: $.trim($("#runtype").val())
	};
	var selectRows = $('#taskList').datagrid('getSelections');	
	if (selectRows.length > 0) {
		$('#taskList').datagrid('clearSelections');
	}
	$("#taskList").datagrid('options').url =urlstring;
	$("#taskList").datagrid('options').queryParams = $query;
	$("#taskList").datagrid('load');	
}

/*
*查看是否选中行(编辑)
*/
function getSelections(){
	var ids = new Array();
	var rows = $('#taskList').datagrid('getSelections');
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
*删除选中多行
*/
function getDeleteSelections(){
	var ids = new Array();
	var rows = $('#taskList').datagrid('getSelections');
	for(var i=0;i<rows.length;i++){
		ids.push(rows[i].id);
	}
	if(rows.length==0){
		$.messager.alert('提示信息',"至少选择一项");
		
		 return false;
	}
	return ids;
}








