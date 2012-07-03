// 插入acctMenu数据
var menuTree;
$(document).ready(function() {
		var userid = getUrlParam("id");
		menuTree=$('#tree').tree({
		checkbox: true,
		onlyLeafCheck:true,
		//url: 'tree_data.json',
		url: '../../acctmenu/treeList?id=0&userid='+userid,
		onBeforeExpand:function(node,param){  
            $('#tree').tree('options').url = "../../acctmenu/treeList?id=" + node.id+"&tag="+node.attributes.tag+'&userid='+userid;
        },
		onClick:function(node){
			$(this).tree('toggle', node.target);
			//alert('you click '+node.text);
		},
		onContextMenu: function(e, node){
			e.preventDefault();
			$('#tt2').tree('select', node.target);
			$('#mm').menu('show', {
				left: e.pageX,
				top: e.pageY
			});
		}
	});
})


/**
 * 保存选中的菜单
 */
function saveAcctAuth(){
	  var node = $('#tree').tree('getChecked');
	  var ids=new Array();;
	  for(var i=0;i<node.length;i++){
		  ids[i]=node[i].id;
	  }
	  $.ajax({
		  type: 'POST',
		  url: '../../acctuser/saveUserMenu',
		  data: {ids:ids.toString(),
			  	userid:getUrlParam("id")			  
			  	},
		  dataType: 'json',
		  success: function(data) {
				if (data.success) {
					$.messager.alert('提示', '数据保存成功', 'info', function() {
					});
				} else {
					$.messager.alert('提示', data.exceptionMsg);
				}
			}
	  });
} 


// 关闭ADD界面
function closeAdd() {
	parent.$('#sysdictList').datagrid('load');
	parent.$('#jsjleditpage').dialog('close');
}
