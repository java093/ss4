//初始化状态为“我的代办”
$(document).ready(function(){
	var urlstring  =baseUrl+'/mng/list';
	/*	
	 * limit :为每页的记录数(common.js参得)
	 */	
	$('#taskList').treegrid({
		title:'任务列表',
		width:'100%',
		animate:false,
		rownumbers: false,
		animate:false,
		fitColumns:true,
		striped:true,
		deepCascade:true,
		collapsible:true,	
		nowrap         : false,
		height         : $(window).height()-90,
		singleSelect   :true,
		loadMsg        :'数据加载中请稍后……', 		
		pageNumber:1,
		pageList:[pageCount[1]],			//分页大小
		queryParams:{limit:pageCount[1],'flowstatus':0},	//分页参数，请不要删除（每页的记录数，请看common.js）
		url:urlstring,	
		sortName: 'title',
		sortOrder: 'desc',
		idField:'ID',		
		treeField:'NAME',
		fitColumns: true,
		columns:[[
		          {field:'ck',checkbox:true,hidden:true},
		          {title:'ID',field:'ID',width:$(this).width()*0.06,sortable:true},
		          {title:'TASK_ID',field:'TASK_ID',width:$(this).width()*0.06,sortable:true,hidden:true},	        	
		        	{field:'NAME',title:'任务名称',width:$(this).width()*0.565,align:'left',
		        	  formatter:function(value,rowData,rowIndex){
		            		var workflowid = rowData.WORKFLOWID;
		            		var subtaskid = rowData.SUBTASKID;	
		            		var s="";
		            		if(workflowid==null&&subtaskid==null)
		            		{
		            			s = "<img src='../script/easyui/themes/default/images/accordion_collapse.png'>"+value+"</img>";
		            		}
		            		if(workflowid==null&&subtaskid!=null)
		            		{
		            			s = "<img src='../script/easyui/themes/default/images/accordion_expand.png'>"+value+"</img>";
		            		}
		            		if(workflowid!=null&&subtaskid==null)
		            		{
		            			s = "<img src='../script/easyui/themes/default/images/datebox_arrow.png'>"+value+"</img>";
		            		}
		            		 return s;
		        	  }	
		        	},
		        	{title:'WorkFlowID',field:'WORKFLOWID',width:$(this).width()*0.1,sortable:true},
		        	{title:'SubTaskID',field:'SUBTASKID',width:$(this).width()*0.1,sortable:true},
		            {title:'运行状态',field:'RUN_STATUS_CODE',width:$(this).width()*0.1,sortable:true},
		            {title:'开始时间',field:'START_TIME',width:$(this).width()*0.1,sortable:true},
		            {title:'结束时间',field:'END_TIME',width:$(this).width()*0.1,sortable:true},
		            {title:'运行时间',field:'RUN_COUNT',width:$(this).width()*0.1,sortable:true},
		            {title:'日志',field:'DETAIL',width:$(this).width()*0.1,sortable:true,
			        	  formatter:function(value,rowData,rowIndex){
			            		var name = rowData.NAME;
			            		var id = rowData.ID;	
			            		
			            		var s = "<a href=\"#\" onclick=\"ShowMessage('"+id+"','"+name+"')\">"+"日志"+"</a>";
			            		
			            		 return s;
			        	  }	
		            }
		]],				
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
	
	var pager = $('#taskList').treegrid('getPager');
    $(pager).pagination({showPageList:false});

   	/*
	*字符转JSON
	*/
	 function strToJson(str){ var json = eval(str ); return json; } 
});


function ShowMessage(id,name)
{
	
	createWindow("jsjladdpage","详细日志",true,"normal",false,730,410,"../pages/mng/detailedlog.jsp?ID="+id+"&NAME="+encodeURIComponent(encodeURIComponent(name))+"");
	
}




/*
 * 开始事件
*/
function taskEvent(type) {		
	var rows = $('#taskList').treegrid('getSelections');
	if(rows.length <= 0){
		$.messager.alert('提示信息','请选择要启动的一项!');		
		return;
	}
	var id=rows[0].ID;
	var workflowid=rows[0].WORKFLOWID;
	var subtaskid=rows[0].SUBTASKID;	
	if(workflowid==null&&subtaskid==null)//主任务相关事件
	{
		if(type=="1")//主任务启动
		{
			TaskStart(id);
		}
		if(type=="2")//主任务暂停
		{
			TaskPause(id);
		}
		if(type=="3")//主任务停止
		{
			TaskStop(id);
		}
		return;
	}
	if(workflowid==null&&subtaskid!=null)//子任务相关事件
	{
		if(type=="1")//子任务启动
		{
			SubTaskStart(subtaskid);
		}
		if(type=="2")//子任务暂停
		{
			SubTaskPause(subtaskid);
		}
		if(type=="3")//子任务停止
		{
			SubTaskStop(subtaskid);
		}
		
		return;
	}
	if(workflowid!=null&&subtaskid==null)//WorkFlow相关事件
	{
		if(type=="1")//WorkFlow启动
		{
			WorkFlowStart(workflowid);
		}
		if(type=="2")//WorkFlow暂停
		{
			WorkFlowPause(workflowid);
		}
		if(type=="3")//WorkFlow停止
		{
			WorkFlowStop(workflowid);
		}		
		return;
	}
}
//主任务启动
function TaskStart(id)
{
	alert("主任务启动");
}
//主任务暂停
function TaskPause(id)
{
	alert("主任务暂停");
}
//主任务停止
function TaskStop(id)
{
	alert("主任务停止");
}

//子任务启动
function SubTaskStart(subtaskid)
{
	alert("子任务启动");
}

//子任务暂停
function SubTaskPause(subtaskid)
{
	alert("子任务暂停");
}
//子任务停止
function SubTaskStop(subtaskid)
{
	alert("子任务停止");
}

//workflow启动
function WorkFlowStart(workflowid)
{
	alert("WORKFLOWID="+workflowid);
	$.post(baseUrl + "/mng/workflowstart", {
		WORKFLOWID : workflowid
	});
}
//workflow暂停
function WorkFlowPause(workflowid)
{
	alert("WORKFLOWID="+workflowid);
	$.post(baseUrl + "/mng/workflowpause", {
		WORKFLOWID : workflowid
	});
}
//workflow停止
function WorkFlowStop(workflowid)
{
	alert("WORKFLOWID="+workflowid);
	$.post(baseUrl + "/mng/workflowstop", {
		WORKFLOWID : workflowid
	});
}

/*
*查看是否选中行
*/
function getSelections(){
	var ids = new Array();
	var rows = $('#employeeList').datagrid('getSelections');
	for(var i=0;i<rows.length;i++){
		ids.push(rows[i].accumseq);
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
	return ids ; //alert(ids.join(':'));
}


/*
*查询事件
*/
function slectTaskByCondition()
{
	var txtTaskName=$("#txtTaskName").val();   
    var selectTaskState=$("#selectTaskState").val();
    var selectTaskType=$("#selectTaskType").val();
    var txtStartTime=$("#txtStartTime").val();
    var txtStopTime=$("#txtStopTime").val();
    $.post(baseUrl + "/mng/slectTaskByCondition", {
    	TXTTASKNAME : txtTaskName,
    	SELECTTASKSTATE : selectTaskState,
    	SELECTTASKTYPE : selectTaskType,
    	TXTSTARTTIME : txtStartTime,
    	TXTSTOPTIME : txtStopTime
	});
}





