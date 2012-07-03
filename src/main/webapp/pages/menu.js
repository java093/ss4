$(function(){
	$.ajax({
			url:baseUrl+"/acctmenu/treeList?id=0", 
			success:function(result)
		{
			var json=eval(result);
			var menulist1;
			for(var i=0;i<json.length;i++){
				menulist1 = "<ul id='tt1"+i+"' class='easyui-tree' dnd='false'   animate='true' dnd='true' url='"+baseUrl+"/acctmenu/leftTreeList?moduleid="+json[i].id+"&tag=0'>" ;
				$("#west-id").accordion(
						'add', {
					　　title: json[i].text,
					    id:"accordion"+i,
					　　content: menulist1
				}
				);
			}
	},
		complete:function(){
			//首个选中
			var pp = $('#west-id').accordion('panels');
			    var t = pp[0].panel('options').title;
			    $('#west-id').accordion('select', t);
		}
	});
	
	$('#win').window({  
	    width:600,  
	    height:400,  
	    modal:true  
	});	
	//$('#win').window('open');  // close window 	
});

//修改密码
function changePwd(){
	$('#win').window('open');  // close window
}

//关闭修改密码弹出框
function closeWin() {
	$('#win').window('close');  // close window
}

function updateCurrentUserPwd() {
		// URL传参
		var options = {
			url : baseUrl + '/acctuser/updateCurrentUserPwd',
			type : "post",
			dataType : 'json',
			data : {
				'password' : $("#password").val()
			},
			success : function(data) {
				if (data.success) {
					$.messager.alert('提示', '数据保存成功', 'info', function() {
					});
				} else {
					$.messager.alert('提示', data.exceptionMsg);
				}
			},
			error : function() {
				$.messager.alert('提示信息', "数据保存失败。。。", "error");
			},
			clearForm : true
		};
	/*
	 * 异步提交
	 */
	$("#updateAcctUserForm").ajaxSubmit(options);
}

        