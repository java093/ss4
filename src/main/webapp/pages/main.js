
var logid = new Array();
var select = 0
var i = 0;
$(document)
		.ready(
				function() {				
				var urlstring  =baseUrl+'/mng/mainLogList';	
				$('#logList').treegrid({
					width:'100%',
					nowrap: false,
					rownumbers: true,
					animate:true,
					fitColumns:true,
					showFooter:true,
					striped:true,
					singleSelect:true,
					deepCascade:true,
					loadMsg:'数据加载中请稍后……', 
					collapsible:true,
					idField:'ID',
					treeField:'TASKNAME',
							columns:[[							         
									          {title:'ID',field:'ID',width:$(this).width()*0.06,sortable:false,hidden:true},									       
									          {title:'任务ID',field:'TASK_ID',width:$(this).width()*0.06,sortable:false,hidden:true},	        	
									          {title:'任务名称',field:'TASKNAME',width:$(this).width()*0.25,align:'left',
									        	  formatter : function(value,rowData, rowIndex) {
									        		    var slevel = rowData.LEVEl;						        		    
									            		var s='';
									            		if(slevel=='1'){
									            			if(rowData.TASK_ID !='0'){										          		
									            			s = "<img src='../images/task.png'>"
																+ value
																+ "</img>";		
									            			}
									            		}else if(slevel=='2'){
									            			if((rowData.SUBTASKID !='0'&&rowData.WORKFLOWID=='0')){
									            				s = "<img src='../images/subtask.png'>"
																	+ value
																	+ "</img>";		
										            			}else if((rowData.WORKFLOWID !='0'&&rowData.SUBTASKID=='0')){
										            				s = "<img src='../images/workflow.png'>"
																		+ value
																		+ "</img>";	
										            				
										            			}
									            		}else if(slevel=='3'){										            		
									            			if(rowData.WORKFLOWID !='0'){									            		
									            				s = "<img src='../images/workflow.png'>"
																	+ value
																	+ "</img>";	
										            			}
									            		}				            		
									                     return s;
									        	  }
									          },
									          {title:'WorkFlowID',field:'WORKFLOWID',width:$(this).width()*0.1,sortable:false,hidden:true},
									          {title:'LOGID',field:'LOGID',width:$(this).width()*0.1,sortable:false,hidden:true},
									          {title:'SUBLOGID',field:'SUBLOGID',width:$(this).width()*0.1,sortable:false,hidden:true},
									          {title:'WORKFLOWLOGID',field:'WORKFLOWLOGID',width:$(this).width()*0.1,sortable:false,hidden:true},
									          {title:'LEVEl',field:'LEVEl',width:$(this).width()*0.1,sortable:false,hidden:true},
									          {title:'SubTaskID',field:'SUBTASKID',width:$(this).width()*0.1,sortable:false,hidden:true},
									          {title:'运行状态',field:'RUN_STATUS_CODE',width:$(this).width()*0.12,sortable:false,
									        		formatter : function(value,
															rowData, rowIndex) {
														var state = rowData.RUN_STATUS_CODE;
														if (state == "1") {											
															return "<b><font color=\"green\" >"+"Succeeded"+"</font></b>";
														} else if (state == "2") {
															return "<b>Disabled</b>";
														} else if (state == "3") {
															return "<b><font color=\"red\" >"+"Failed"+"</font></b>";
														} else if (state == "4") {
															return "<b>Stopped</b>";
														} else if (state == "5") {
															return "<b>Aborted</b>";
														} else if (state == "6") {
															return "<b>Running</b>";
														}else if (state == "7") {
															return "<b>Exception</b>";
														} else
															return value;
													}
									          },
									          {title:'开始时间',field:'START_TIME',width:$(this).width()*0.12,sortable:false},
									          {title:'结束时间',field:'END_TIME',width:$(this).width()*0.12,sortable:false},
									          {title:'运行时间',field:'RUN_COUNT',width:$(this).width()*0.11,align:'left',sortable:false},
									          {title:'日志',field:'DETAIL',width:$(this).width()*0.12,sortable:false,
										        	  formatter:function(value,rowData,rowIndex){
										        		    var slevel = rowData.LEVEl;						        		    
										            		var name = rowData.TASKNAME;
										            		var s='';
										            		if(slevel=='1'){
										            			if(rowData.LOGID !='0'){										          		
										            			id = rowData.LOGID;
										            			 s = "<a href=\"#\" onclick=\"ShowMessage('"+id+"','"+name+"')\">"+"日志"+"</a>";		
										            			}
										            		}else if(slevel=='2'){
										            			if((rowData.SUBLOGID !='0'&&rowData.WORKFLOWLOGID=='0')||(rowData.WORKFLOWLOGID !='0'&&rowData.SUBLOGID=='0')){
										            			
										            				id = rowData.LOGID;
											            			 s = "<a href=\"#\" onclick=\"ShowMessage('"+id+"','"+name+"')\">"+"日志"+"</a>";		
											            			}
										            		}else if(slevel=='3'){										            		
										            			if(rowData.WORKFLOWLOGID !='0'){									            		
										            				id = rowData.LOGID;
											            			 s = "<a href=\"#\" onclick=\"ShowMessage('"+id+"','"+name+"')\">"+"日志"+"</a>";		
											            			}
										            		}				            		
										                     return s;
										        	  }	
									            }
									]],	
					onBeforeLoad:function(row,param){
					  if(row){
							var slevel=$(this).treegrid('getLevel',row.ID); 
							var taskid = row.TASK_ID;
							var logid = row.LOGID;
							if(slevel=='1'){
							    urlsecond = baseUrl+'/mng/detailLogList?task_id='+taskid+'&logid='+logid+'&levelid='+slevel;
							}else if(slevel=='2'){
							  var subtaskid =row.SUBTASKID;
							  var sublogid = row.SUBLOGID;
							  urlsecond = baseUrl+'/mng/detailLogList?task_id='+taskid+'&subtaskid='+subtaskid+'&logid='+logid+'&sublogid='+sublogid+'&levelid='+slevel;
							}	
							$(this).treegrid('options').url = urlsecond;
						} else {
							$(this).treegrid('options').url = urlstring;
						}
					 },
//					 onClickRow:function(row){					
//							var slevel=$(this).treegrid('getLevel',row.ID); 						
//							if(slevel=='2'&&row.SUBTASKID=='0'&&row.WORKFLOWID!='0'&&row.RUN_STATUS_CODE=='6'){
//								 controlButton(false);			
//							}else if(slevel=='3'&&row.RUN_STATUS_CODE=='6'){
//								controlButton(false);
//							}else{
//								controlButton(true);
//							}							
//					 },
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
					   }			
				});				
});


function controlButton(boolean){
	$('#pause').linkbutton({
		disabled : boolean
	});
	$('#stop').linkbutton({
		disabled : boolean
	});	
}

function autoRefresh(){
	if($('#autorefresh').attr('checked')=="checked"){
		$('select').removeAttr('disabled');
		planRefresh();
	}else{
		$('select').attr('disabled','disabled');
		clearInterval(auto);
	}
}

//jinxl 添加选中时间，修改定时刷新时间
function setAutoTime(){
	clearInterval(auto);
	planRefresh();
}


function planRefresh(){
	var urlstring  =baseUrl+'/mng/mainLogList';	
	if($('#autorefresh').attr('checked')=="checked"){
		var time = $('#refresh').val()*1000*30;
		auto = setInterval(function(){	
			$.post(urlstring,null,function(data) {
				var data = eval( "(" + data + ")" );
				$('#logList').treegrid('loadData',data.rows);
				keepExpand();
			}); 
		},time);
	}
}


function ShowMessage(id,name)
{
	createWindow("jsjladdpage","详细日志",true,"normal",false,800,600,"mng/taskdetaillogtree.jsp?ID="+id);	
}

function TaskStart(){
	var rows =  $('#logList').treegrid('getSelections');
	if(rows.length <= 0){
		$.messager.alert('提示信息','请选择要启动的一项!');		
		return;
	}
	if(rows.length >= 2){
		$.messager.alert('提示信息','只能选择一项!');		
		return;
	}
	var slevel=$('#logList').treegrid('getLevel',rows[0].ID);
	var url=baseUrl+'/mng/start';
	if(slevel =='1'){
		// $.messager.alert('提示信息',"启动"+rows[0].TASKNAME);
		$.post(url,{'id':rows[0].TASK_ID,'level':slevel}, function(data){
			var data = eval( "(" + data + ")" );//json String变成对象
	    	if(data.success){   		
	    		 Refresh();
	    	}
	    });					
	}else if(slevel=='2'){
		
		var parentRow=$('#logList').treegrid('getParent',rows[0].ID);
        if(rows[0].SUBTASKID!='0'&&rows[0].WORKFLOWID=='0'){
        	// $.messager.alert('提示信息',"启动"+rows[0].TASKNAME+"TASKID:"+rows[0].TASKID);
		    $.post(url,{'id':rows[0].SUBTASKID,'parentTaskID':rows[0].TASK_ID,'level':slevel,'flag':1}, function(data){	
		    	var data = eval( "(" + data + ")" );//json String变成对象
		    	if(data.success){
		    		 Refresh();
		    	}
		    });
        }else{        	
        	$.post(url,{'id':rows[0].WORKFLOWID,'mainTaskID':rows[0].TASK_ID,'level':slevel,'flag':2}, function(data){	
        		var data = eval( "(" + data + ")" );//json String变成对象
        		if(data.success){
		    		 Refresh();
		    	}
   		     });
        }
	}else if(slevel=='3'){
		// $.messager.alert('提示信息',"启动"+rows[0].TASKNAME);
		var subtaskRow=$('#logList').treegrid('getParent',rows[0].ID);
		var maintaskRow =$('#logList').treegrid('getParent',subtaskRow.ID);
		$.post(url,{'id':rows[0].WORKFLOWID,'subtaskID':subtaskRow.SUBTASKID,'mainTaskID':maintaskRow.TASK_ID,'level':slevel}, function(data){			  
			var data = eval( "(" + data + ")" );//json String变成对象
			if(data.success){
	    		 Refresh();
	    	}
		});
	}
    
}

function Refresh(){
	var row = $('#logList').treegrid('getSelected');
	if(row!=null){
		select = row.ID;	
	}
	var urlstring  =baseUrl+'/mng/mainLogList';	
	$.post(urlstring,null, function(data){
		var data = eval( "(" + data + ")" );//json String变成对象
		$('#logList').treegrid('loadData',data.rows);
		keepExpand();
		$('#logList').treegrid('select',select);
	});
	
	
}

function  keepExpand(){
	for ( var i = 0; i < logid.length; i++) {
		$('#logList').treegrid('expand',logid[i]);
	}
	logid = new Array();
}
 
