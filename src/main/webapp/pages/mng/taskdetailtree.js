//初始化状态为“我的代办”
var logid = new Array();
var select = 0;
var maintaskid;
$(document)
		.ready(
				function() {
					var id = getUrlParam("ID");
					maintaskid = getUrlParam("TASKID");
					var urlstring = baseUrl + '/mng/taskdetailtree';
					/*
					 * limit :为每页的记录数(common.js参得)
					 */
					$('#taskdetailtree')
							.treegrid(
									{
										title : '详细列表',
										width : '100%',
										animate : false,
										rownumbers : false,
										animate : false,
										fitColumns : true,
										striped : true,
										deepCascade : false,
										collapsible : false,
										nowrap : false,
										height : $(window).height() - 90,
										singleSelect : true,
										loadMsg : '数据加载中请稍后……',										
										queryParams : {
											ID : id
										}, // 分页参数，请不要删除（每页的记录数，请看common.js）
										url : urlstring,
										sortName : 'title',
										sortOrder : 'desc',
										idField : 'ID',
										treeField : 'NAME',
										columns : [ [
												{
													title : 'ID',
													field : 'ID',
													width : $(this).width() * 0.06,
													hidden : true
												},
												{
													title : 'TASK_ID',
													field : 'TASK_ID',
													width : $(this).width() * 0.1,
													hidden : true
												},
												{
													field : 'NAME',
													title : '任务名称',
													width : $(this).width() * 0.2,
													formatter : function(value,
															rowData, rowIndex) {
														var workflowid = rowData.WORKFLOWID;
														var subtaskid = rowData.SUBTASKID;
														var s = "";
														if (workflowid == null
																&& subtaskid == null) {
															s = "<img src='../../images/task.png'>"
																+ value
																+ "</img>";
														} else if (workflowid == null
																&& subtaskid != null) {
															s = "<img src='../../images/subtask.png'>"
																+ value
																+ "</img>";	
														} else if (workflowid != null
																&& subtaskid == null) {
															s = "<img src='../../images/workflow.png'>"
																+ value
																+ "</img>";
														}
														return s;
													}
												},
												{
													title : 'WorkFlowID',
													field : 'WORKFLOWID',
													width : $(this).width() * 0.1,
													hidden : true
												},
												{
													title : 'SubTaskID',
													field : 'SUBTASKID',
													width : $(this).width() * 0.1,
													hidden : true
												},
												{
													title : '运行状态',
													field : 'RUN_STATUS_CODE',
													width : $(this).width() * 0.1,
													formatter : function(value,
															rowData, rowIndex) {
														var state = rowData.RUN_STATUS_CODE;
														if (value == "1") {
															return "<b><font color=\"green\" >"+"Succeeded"+"</font></b>";
														} else if (value == "2") {
															return "<b>Disabled</b>";
														} else if (value == "3") {
															return "<b><font color=\"red\" >"+"Failed"+"</font></b>";
														} else if (value == "4") {
															return "<b>Stopped</b>";
														} else if (value == "5") {
															return "<b>Aborted</b>";
														} else if (value == "6") {
															return "<b>Running</b>";
														} else
															return value;
													}
												},
												{
													title : '开始时间',
													field : 'START_TIME',
													width : $(this).width() * 0.2
												},
												{
													title : '结束时间',
													field : 'END_TIME',
													width : $(this).width() * 0.2
												},
												{
													title : '运行时间',
													field : 'RUN_COUNT',
													width : $(this).width() * 0.1
												}

										] ],
										onHeaderContextMenu : function(e, field) {
											e.preventDefault();
											if (!$('#tmenu').length) {
												createColumnMenu('#taskdetailtree');
											}
											$('#tmenu').menu('show', {
												left : e.pageX,
												top : e.pageY
											});
										},
										/**20120629添加，记录展开项的ID*/
										onExpand:function(row){
											 logid.push(row.ID);
										 },
										 onCollapse:function(row){
											var temp = new Array();
											temp = logid;
											for ( var i = 0; i < temp.length; i++) {
												if(row.ID==temp[i]){
													logid.splice(i,1);
												}
											 }
										   },
										onClickRow : function(row) {
											var rows = $('#taskdetailtree')
													.treegrid('getSelections');
											var detail = rows[0].DETAIL;
											document
													.getElementById("inputDetail").value = detail;
										},
										onLoadError : function() {
											$.messager.alert('提示信息',
													"数据加载错误。。。", "error");
										},
										pagination : false

									});

					var pager = $('#taskdetailtree').treegrid('getPager');
					$(pager).pagination({
						showPageList : false
					});

					/*
					 * 字符转JSON
					 */
					function strToJson(str) {
						var json = eval(str);
						return json;
					}
				});

/*
 * 启动事件
 */
function TaskStart() {
	event(1);
}

/*
 * 暂停事件
 */
function PauseEvent() {
	event(2);
}

/*
 * 停止事件
 */
function StopEvent() {
	event(3);
}

function event(type) {
	var rows = $('#taskdetailtree').treegrid('getSelections');
	if (rows.length <= 0) {
		$.messager.alert('提示信息', '请选择要操作的一项!');
		return;
	}
	var id = rows[0].ID;
	var taskid = rows[0].TASK_ID;
	var tasklogid = rows[0].TASK_LOGID;
	var workflow = rows[0].WORKFLOW;
	var workflowid = rows[0].WORKFLOWID;
	var subtaskid = rows[0].SUBTASKID;
	var url=baseUrl+'/mng/start';
	if (workflowid == null && subtaskid == null)// 主任务相关事件
	{
		if (type == "1")// 主任务启动
		{
//			TaskStart(id);
			/**20120629添加 负责主任务的启动*/
			$.post(url,{'id':taskid,'level':'1'}, function(data){
				var data = eval( "(" + data + ")" );//json String变成对象
		    	if(data.success){   		
		    		$.messager.alert('提示信息',"主任务："+rows[0].NAME+"已启动");
		    		 Refresh();
		    	}
		    });	
		}
		if (type == "2")// 主任务暂停
		{
			TaskPause(id);
		}
		if (type == "3")// 主任务停止
		{
			TaskStop(id);
		}
		return;
	}
	if (workflowid == null && subtaskid != null)// 子任务相关事件
	{
		if (type == "1")// 子任务启动
		{
//			SubTaskStart(subtaskid);
			/**20120629添加 负责子任务的启动*/
			$.post(url,{'id':taskid,'parentTaskID':maintaskid,'level':'2','flag':1}, function(data){	
		    	var data = eval( "(" + data + ")" );//json String变成对象
		    	if(data.success){
		    		$.messager.alert('提示信息',"子任务："+rows[0].NAME+"已启动");
		    		 Refresh();
		    	}
		    });
		}
		if (type == "2")// 子任务暂停
		{
			SubTaskPause(subtaskid);
		}
		if (type == "3")// 子任务停止
		{
			SubTaskStop(subtaskid);
		}
		return;
	}
	if (workflowid != null && subtaskid == null)// WorkFlow相关事件
	{
		if (type == "1")// WorkFlow启动
		{
//			WorkFlowStart(workflowid);
			/**20120629添加 负责workflow的启动*/
			if(tasklogid != null){//主任务WorkFlow
				$.post(url,{'id':workflow,'mainTaskID':maintaskid,'level':'2','flag':2}, function(data){	
	        		var data = eval( "(" + data + ")" );//json String变成对象
	        		if(data.success){
	        			$.messager.alert('提示信息',"workflow："+rows[0].NAME+"已启动");
			    		 Refresh();
			    	}
	   		     });
			}else{//子任务WorkFlow
				var subtaskRow=$('#taskdetailtree').treegrid('getParent',rows[0].ID);
				$.post(url,{'id':workflow,'subtaskID':subtaskRow.SUBTASKID,'mainTaskID':maintaskid,'level':'3'}, function(data){			  
					var data = eval( "(" + data + ")" );//json String变成对象
					if(data.success){
						$.messager.alert('提示信息',"workflow："+rows[0].NAME+"已启动");
			    		 Refresh();
			    	}
				});
			}
		}
		if (type == "2")// WorkFlow暂停
		{
			WorkFlowPause(workflowid);
		}
		if (type == "3")// WorkFlow停止
		{
			WorkFlowStop(workflowid);
		}
		return;
	}
}

//// 主任务启动
//function TaskStart(id) {
////	alert("主任务启动");
//}
//// 主任务暂停
//function TaskPause(id) {
////	alert("主任务暂停");
//}
//// 主任务停止
//function TaskStop(id) {
////	alert("主任务停止");
//}

// 子任务启动
function SubTaskStart(subtaskid) {
//	alert("子任务启动");
}

// 子任务暂停
function SubTaskPause(subtaskid) {
//	alert("子任务暂停");
}
// 子任务停止
function SubTaskStop(subtaskid) {
//	alert("子任务停止");
}

// workflow启动
function WorkFlowStart(workflowid) {
//	alert("WORKFLOWID=" + workflowid);
	$.post(baseUrl + "/mng/workflowstart", {
		WORKFLOWID : workflowid
	});
}
// workflow暂停
function WorkFlowPause(workflowid) {
//	alert("WORKFLOWID=" + workflowid);
	$.post(baseUrl + "/mng/workflowpause", {
		WORKFLOWID : workflowid
	});
}
// workflow停止
function WorkFlowStop(workflowid) {
//	alert("WORKFLOWID=" + workflowid);
	$.post(baseUrl + "/mng/workflowstop", {
		WORKFLOWID : workflowid
	});
}
/**20120629修改，为刷新按钮提供方法*/
function Refresh(){
	var row = $('#taskdetailtree').treegrid('getSelected');
	if(row!=null){
		select = row.ID;	
	}
	var id = getUrlParam("ID");
	var urlstring  =baseUrl+'/mng/taskdetailtree';	
	$.post(urlstring,{'ID':id}, function(data){
		var data = eval( "(" + data + ")" );//json String变成对象
		$('#taskdetailtree').treegrid('loadData',data.rows);
		keepExpand();
		$('#taskdetailtree').treegrid('select',select);
	});
//	$('#taskdetailtree').treegrid('reload',{'ID':id});
}
/**20120629修改，刷新后展开历史状态*/
function  keepExpand(){
	for ( var i = 0; i < logid.length; i++) {
		$('#taskdetailtree').treegrid('expand',logid[i]);
	}
}