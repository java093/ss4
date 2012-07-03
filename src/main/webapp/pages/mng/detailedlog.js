//初始化状态为“我的代办”
$(document).ready(function(){
	var id=getUrlParam("ID");
	var name=decodeURIComponent(getUrlParam("NAME"));
	document.getElementById("labelName").value=name;
	var urlstring  =baseUrl+'/mng/detailedloglist';
	$('#detailedLogList').datagrid({
		title:'日志运行列表',
		width:'98%',
		animate:false,
		rownumbers: true,
		animate:false,
		deepCascade:false,
		collapsible:false,	
		nowrap         : true,
		height         : $(window).height()-10,
		singleSelect   :true,
		loadMsg        :'数据加载中请稍后……', 
		pageNumber:1,
		pageList:[pageCount[1]],			//分页大小
		queryParams:{limit:pageCount[1],ID:id},	//分页参数，请不要删除（每页的记录数，请看common.js）
		url:urlstring,	
		idField:'ID',
		fitColumns: true,
		columns:[[		          
		          {title:'ID',field:'id',width:$(this).width()*0.06,sortable:true,hidden:true},
		        	
		            
		            {title:'开始时间',field:'START_TIME',width:$(this).width()*0.1},
		            {title:'结束时间',field:'END_TIME',width:$(this).width()*0.1},
		            {title:'运行时间',field:'run_Count',width:$(this).width()*0.05,
		        		formatter:function(value,rowData,rowIndex){
		        			var state = rowData.RUN_STATUS_CODE;
		        			if(state=="1")
		        			{return "Succeeded";}
		        			if(state=="2")
		        			{return "Disabled";}
		        			if(state=="3")
		        			{return "Failed";}
		        			if(state=="4")
		        			{return "Stopped";}
		        			if(state=="5")
		        			{return "Aborted";}
		        			if(state=="6")
		        			{return "Running";}
		        		}},	 
		            {title:'运行状态',field:'RUN_STATUS_CODE',width:$(this).width()*0.1},
		            {title:'详情',field:'DETAIL',width:$(this).width()*0.1,hidden:true}
		]],				
		onHeaderContextMenu: function(e, field){
			e.preventDefault();
			if (!$('#tmenu').length){
				createColumnMenu('#detailedLogList');
			}
			$('#tmenu').menu('show', {
				left:e.pageX,
				top:e.pageY
			});
		},
		onClickRow : function(row){
			var rows = $('#detailedLogList').datagrid('getSelections');
			if(rows.length <= 0){
				$.messager.alert('提示信息','请选择要启动的一项!');		
				return;
			}
			var detail = rows[0].DETAIL;
			
			document.getElementById("inputDetail").value=detail;
			
		},
		onLoadError:function(){$.messager.alert('提示信息',"数据加载错误。。。","error");},			
		pagination:true
		
	});		
	
	var pager = $('#detailedLogList').datagrid('getPager');
    $(pager).pagination({showPageList:false});

   	/*
	*字符转JSON
	*/
	 function strToJson(str){ var json = eval(str ); return json; } 
});








