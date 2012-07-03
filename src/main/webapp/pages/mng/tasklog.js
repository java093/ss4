//初始化状态为“我的代办”
$(document)
		.ready(
				function() {
					var urlstring = baseUrl + '/mng/taskloglisttotal';
					/*
					 * limit :为每页的记录数(common.js参得)
					 */
					$('#taskList')
							.datagrid(
									{
										title : '任务日志列表',
										width : '100%',
										animate : false,
										rownumbers : false,
										animate : false,
										fitColumns : true,
										striped : true,
										deepCascade : true,
										collapsible : true,
										nowrap : false,
										height : $(window).height() - 90,
										singleSelect : true,
										loadMsg : '数据加载中请稍后……',
										pageNumber : 1,
										pageList : [ pageCount[1] ], // 分页大小
										queryParams : {
											limit : pageCount[1],
											'flowstatus' : 0
										}, // 分页参数，请不要删除（每页的记录数，请看common.js）
										url : urlstring,
										sortName : 'title',
										sortOrder : 'desc',
										idField : 'ID',
										treeField : 'NAME',
										fitColumns : true,
										columns : [ [
												{
													field : 'ck',
													checkbox : true,
													hidden : true
												},
												{
													title : 'ID',
													field : 'id',
													width : $(this).width() * 0.06,
													hidden : true
												},
												{
													title : 'TASK_ID',
													field : 'task_id',
													width : $(this).width() * 0.06,
													hidden : true
												},
												{
													field : 'name',
													title : '任务名称',
													width : $(this).width() * 0.1,
													align : 'left',
													formatter : function(value,
															rowData, rowIndex) {
														var workflowid = rowData.WORKFLOWID;
														var subtaskid = rowData.SUBTASKID;
														var s = "<img src='../images/task.png'>"
																+ value
																+ "</img>";
														return s;
													}
												},
												{
													title : '运行状态',
													field : 'run_status_code',
													width : $(this).width() * 0.1,
													formatter : function(value,
															rowData, rowIndex) {
														var state = rowData.RUN_STATUS_CODE;
														if (value == "1") {
															return "<b><font color=\"green\" >"
																	+ "Succeeded"
																	+ "</font></b>";
														} else if (value == "2") {
															return "<b>Disabled</b>";
														} else if (value == "3") {
															return "<b><font color=\"red\" >"
																	+ "Failed"
																	+ "</font></b>";
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
													field : 'start',
													width : $(this).width() * 0.1
												},
												{
													title : '结束时间',
													field : 'end',
													width : $(this).width() * 0.1
												},
												{
													title : '运行时间',
													field : 'run_count',
													width : $(this).width() * 0.1
												},
												{
													title : '日志',
													field : 'detail',
													width : $(this).width() * 0.1,
													formatter : function(value,
															rowData, rowIndex) {
														var name = rowData.NAME;
														var id = rowData.id;
														var taskid = rowData.task_id;
														var s = "<a href=\"#\" onclick=\"ShowMessage('"
																+ id
																+ "','"
																+ name
																+ "','"
																+ taskid
																+ "')\">"
																+ "详情" + "</a>";

														return s;
													}
												} ] ],
										onHeaderContextMenu : function(e, field) {
											e.preventDefault();
											if (!$('#tmenu').length) {
												createColumnMenu('#taskList');
											}
											$('#tmenu').menu('show', {
												left : e.pageX,
												top : e.pageY
											});
										},
										onLoadError : function() {
											$.messager.alert('提示信息',
													"数据加载错误。。。", "error");
										},
										pagination : true,
										rownumbers : true

									});

					var pager = $('#taskList').treegrid('getPager');
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

function ShowMessage(id, name, taskid) {
	createWindow("jsjladdpage", "详细日志", true, "normal", false, 800, 600,
			"../pages/mng/taskdetaillogtree.jsp?ID=" + id + "&TASKID=" + taskid);

}

function Refresh() {
	$('#taskList').datagrid('reload');
}

// 主任务启动
function TaskStart() {
	TaskEvent(0);

}
// 主任务暂停
function TaskPause() {
	TaskEvent(1);
}
// 主任务停止
function TaskStop() {
	TaskEvent(2);

}

function TaskEvent(type) {
	var rows = $('#taskList').datagrid('getSelections');
	if (rows.length <= 0) {
		$.messager.alert('提示信息', '请选择要操作的一项!');
		return;
	}
	var id = rows[0].task_id;
	if (type == "0")//主任务启动1
	{
		var url = baseUrl + '/mng/start';
		$.messager.alert('提示信息', "启动" + rows[0].name);
		$.post(url, {
			'id' : id,
			'level' : '1'
		}, function(data) {
			var data = eval("(" + data + ")");//json String变成对象
			if (data.success) {
				Refresh();
			}
		});
	}
	//	if(type=="1")//主任务暂停
	//	{alert("主任务暂停,task_id="+id);}
	//	if(type=="2")//主任务停止
	//	{alert("主任务停止,task_id="+id);}
}

/*
 *查询事件
 */
function slectTaskByCondition() {
	var txtTaskName = $("#txtTaskName").val();
	var selectTaskState = $("#selectTaskState").val();
	var txtStartTime = $("#txtStartTime").val();
	var txtStopTime = $("#txtStopTime").val();

	if (!isDate(txtStartTime, txtStopTime)) {
		$.messager.alert('提示', '结束时间必须大于开始时间');
		return false;
	}

	var urlstring = baseUrl + '/mng/slectTaskLogByCondition';
	$query = {
		TXTTASKNAME : txtTaskName,
		SELECTTASKSTATE : selectTaskState,
		TXTSTARTTIME : txtStartTime,
		TXTSTOPTIME : txtStopTime
	};

	$("#taskList").datagrid('options').url = urlstring;
	$("#taskList").datagrid('options').queryParams = $query;
	$("#taskList").datagrid('load');
}

//日期比较
function isDate(d1, d2) {
	d1Arr = d1.split('-');
	d2Arr = d2.split('-');
	v1 = new Date(d1Arr[0], d1Arr[1], d1Arr[2]);
	v2 = new Date(d2Arr[0], d2Arr[1], d2Arr[2]);
	if (v1 >= v2)
		return false;
	else
		return true;
}
