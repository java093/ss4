

/*
 * 全局变量  页面分页用
 */
pageCount = new Array(5,10,15,20,30,40,50) ;

/*
 * 提示语
 */
messageInfo = {
		"addSuccess"   :"新增成功!",
		"editSuccess"  :"修改成功!",
		"noEdit"       :"该数据不可修改!",
		"noView"       :"该数据不可查看!",
		"upSuccess"    :"文件上传成功!",
		"upFail"       :"文件上传失败!",
		"addFail"      :"新增失败!",
		"editFail"     :"修改失败!",
		"comfirmDel"   :"确认删除吗?",
		"noView"       :"请选择要查看的一项!",
		"noSelect"     :"请选择要修改的一项!",
		"errorData"    :"数据加载错误。。。",
		"noSelect"     :"请选择一条进行操作!",
		"selectOne"    :"只能选择一条!",
		"onlyDept"     :"项目经理只能是管理专业!",
		"fullData"     :"数据输入不完整!",
		"accepted"     :"已接收!",
		"errorAccepted":"接收失败!"
		
	};

/*
 * 获取URL传参
 * 参数 name 
 */
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return "";
}


/*
*字符转JSON
*/
 function strToJson(str){ var json = eval(str ); return json; }  	
 
 /*
  * 空字符串判断,转换
  */
 function ISNull(cSTR) {
 	var retSTR = "";
 	if (cSTR != null) {
 		retSTR = cSTR;
 	}
 	return retSTR;
 }
 
 
 /*
  * 提示信息窗体
  * sTitle		窗体名称
  * MessgerInfo	消息内容
  * sIco  		图标(error,info,question,warning) 可为空
  */
function openMessager(sTitle,MessgerInfo,sIco){
	$.messager.alert(sTitle,MessgerInfo,sIco);
}
 
 
 
/*
 * 创建Grid表头右键菜单
 */
function createColumnMenu(gridName){
	var tmenu = $('<div id="tmenu" style="width:100px;"></div>').appendTo('body');
	var comTitles= $(gridName).datagrid('options');	
	for(var i=0; i<comTitles.columns[0].length; i++){
	 if(comTitles.columns[0][i].field!="ck"){
			if(!comTitles.columns[0][i].hidden){
				$('<div iconCls="icon-ok"/>').html(comTitles.columns[0][i].title).appendTo(tmenu);
			}else{
				$('<div iconCls="icon-empty"/>').html(comTitles.columns[0][i].title).appendTo(tmenu);
			}
		}
	}
	tmenu.menu({
		onClick: function(item){
			var fieldName="";
			var comInfo= $(gridName).datagrid('options');			
			for(var i=0; i<comInfo.columns[0].length; i++){
				if(comInfo.columns[0][i].title==item.text){
					fieldName = comInfo.columns[0][i].field;
				}
			}
			if (item.iconCls=='icon-ok'){
				$(gridName).datagrid('hideColumn', fieldName);
				tmenu.menu('setIcon', {
					target: item.target,
					iconCls: 'icon-empty'
				});
			} else {
				$(gridName).datagrid('showColumn', fieldName);
				tmenu.menu('setIcon', {
					target: item.target,
					iconCls: 'icon-ok'
				});
			}
		}
	});	
}


/*
 * 打开Dialog以及Iframe
 * divtitle 找开的Dialog的标题(title)
 * divName 打开的Dialog的id
 * framName Iframe的id
 * PageUrl Iframe的URL
 */

function openDialogIframe(divtitle,divName,framName,PageUrl) {
	  var iframe01 = document.getElementById(framName);
	  $('#'+divName).window('setTitle',divtitle);
	  openMsg();
	  var url = PageUrl;
 		iframe01.src =url;
	  if(iframe01.attachEvent){
		   iframe01.attachEvent("onload", function(){
			   closeMsg();			   
			   $('#'+divName).window('open');
 	   });
	  } else{
		   iframe01.onload = function(){
			   closeMsg();
			   $('#'+divName).window('open');				  
		   };
	  }
}


/*
 * 
 * 打开新窗体-无回调函数
 * divtitle  标题(title)
 * isFrm	url链接是否在iframe里面展示
 * winalign	窗口初始化位置normal right，tright，bright，left，tleft，bleft，top，bottom
 * isMax	口初始化是否最大化展示
 * PageUrl	口要加载的html片段地址.
 * winWidth 宽度
 * winHeigth 高度
 * callback（）回调函数
 */
function createWindow(id,divtitle,isFrm,winalign,winisMax,winWidth,winHeigth,PageUrl){
	$.createWin({  
		winId:id,
        title:divtitle,  
        isIframe:isFrm,
        align:winalign,
        isMax:winisMax,
        url:PageUrl,  
        height:winHeigth,   
        width:winWidth
	});   
}


/*
 * 打开新窗体-带回调函数
 * divtitle  标题(title)
 * isFrm	url链接是否在iframe里面展示
 * winalign	窗口初始化位置normal right，tright，bright，left，tleft，bleft，top，bottom
 * isMax	口初始化是否最大化展示
 * PageUrl	口要加载的html片段地址.
 * winWidth 宽度
 * winHeigth 高度
 * callback（）回调函数
 */
function createWindowCallback(id,divtitle,isFrm,winalign,winisMax,winWidth,winHeigth,PageUrl,callback){
	$.createWin({  
		winId:id,
        title:divtitle,  
        isIframe:isFrm,
        align:winalign,
        isMax:winisMax,
        url:PageUrl,  
        height:winHeigth,   
        width:winWidth ,      
        onClose:function(){   
		callback();
		}
	});   
}


function openMsg(){
	$("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body");     
    $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。").appendTo("body").css({display:"block",  
          left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2}); 
	
}

 function closeMsg(){
	 var wrap =$(document.body);
	    wrap.find("div.datagrid-mask-msg").remove();
	   wrap.find("div.datagrid-mask").remove();
	   //$("body").children("div.datagrid-mask-msg").remove();
 }
 
 /**
  * ArrayToJson字符串
  * @param o
  * @return
  */
 function arrayToJson(o) { 
	 var r = []; 
	 if (typeof o == "string") return "\"" + o.replace(/([\'\"\\])/g, "\\$1").replace(/(\n)/g, "\\n").replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t") + "\""; 
		 if (typeof o == "object") { 
			 if (!o.sort) { 
				 for (var i in o) 
				 r.push(i + ":" + arrayToJson(o[i])); 
					 if (!!document.all && !/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/.test(o.toString)) { 
					 r.push("toString:" + o.toString.toString()); 
					 } 
				 r = "{" + r.join() + "}"; 
			 } else { 
				 for (var i = 0; i < o.length; i++) { 
				 r.push(arrayToJson(o[i])); 
				 } 
				 r =  r.join(); 
			 } 
			 return r; 
	 } 
	 return o.toString(); 
}
  /**
  * objeceToJson字符串
  * @param o
  * @return
  */
 function obj2str(o){
	   var r = [];
	   if(typeof o == "string" || o == null) {
	     return o;
	   }
	   if(typeof o == "object"){
	     if(!o.sort){
	       r[0]="{"
	       for(var i in o){
	         r[r.length]=i;
	         r[r.length]=":";
	         r[r.length]=obj2str(o[i]);
	         r[r.length]=",";
	       }
	       r[r.length-1]="}"
	     }else{
	       r[0]="["
	       for(var i =0;i<o.length;i++){
	         r[r.length]=obj2str(o[i]);
	         r[r.length]=",";
	       }
	       r[r.length-1]="]"
	     }
	     return r.join("");
	   }
	   return o.toString();
	}
 /**
  *为页所有lable赋值
  *labelArr 所有lable数组对象
  * @param obj 数据对象（map或json格式）
  * @return
  */
 function setLableValue(labelArr,obj){
	 for(var i = 0; i < labelArr.length; i++){
			if(null != obj[""+$(labelArr[i]).attr('id')+""] && obj[""+$(labelArr[i]).attr('id')+""]!=""){
			$(labelArr[i]).html(obj[""+$(labelArr[i]).attr('id')+""]);
			}else{
				$(labelArr[i]).html("&nbsp;");
			}
		}
 }

 /**
  * 序列化form
  * @param o
  * @return 返回json对象
  */
 function formToJson(idParam){
		var str = $(idParam).serialize();
	    str = str.replace(/&/g,"','");
	    str = str.replace(/=/g,"':'");
	    str = "({'"+str +"'})";
	    obj = eval(str);
	    return obj;  
	}
 
 /**
  * 文件的上传下载页面显示
  *idv为文件的id号、path为文件的路径、name为文件的名称、tableid是table的id号、fileid是需要创建的隐藏域，便于保存时循环取值 
  */
 
 function showfilelist(baseUrl,idv,path,name,tableid,fileid){

		var node0 = document.getElementById(tableid);
		//取的行数
		row_index = parseInt(node0.rows.length);
		//创建一行
		addRow = node0.insertRow(row_index);
		addRow.id = "tr_"+idv;

		var col2 = addRow.insertCell(0);
		 var a1 = document.createElement('a');
		 a1.setAttribute('href','javascript:deletefile("tr_'+idv+'");');
		 a1.innerHTML = "<img src='"+baseUrl+"/images/icon/del.jpg' alt='删除' border='0'>";
		 col2.appendChild(a1);
		 col2.setAttribute('width',20);
		//创建一个单元格
		var col1 = addRow.insertCell(1);
		 //创建一个超链接
		 var a = document.createElement('a');
		 //setAttribute是设置属性的
	     a.setAttribute('href',getURL(path,name));
	     a.innerHTML = name;
	     //超链接加入单元格中
		 col1.appendChild(a);

		
		 //创建一个隐藏域
		 var input1= document.createElement('input');
		 input1.setAttribute('type','hidden');
		 input1.setAttribute('id',fileid);
		 input1.setAttribute('name',fileid);
		 input1.setAttribute('value',idv);
		 col1.appendChild(input1);
}
 
 /**
  * 文件的上传下载页面显示,当$.messager.confirm、$.messager.alert不能使用时使用该方法
  *idv为文件的id号、path为文件的路径、name为文件的名称、tableid是table的id号、fileid是需要创建的隐藏域，便于保存时循环取值 
  */
 function showfilelistN(baseUrl,idv,path,name,tableid,fileid){

		var node0 = document.getElementById(tableid);
		//取的行数
		row_index = parseInt(node0.rows.length);
		//创建一行
		addRow = node0.insertRow(row_index);
		addRow.id = "tr_"+idv;

		var col2 = addRow.insertCell(0);
		 var a1 = document.createElement('a');
		 a1.setAttribute('href','javascript:deletefileN("tr_'+idv+'");');
		 a1.innerHTML = "<img src='"+baseUrl+"/images/icon/del.jpg' alt='删除' border='0'>";
		 col2.appendChild(a1);
		 col2.setAttribute('width',20);
		//创建一个单元格
		var col1 = addRow.insertCell(1);
		 //创建一个超链接
		 var a = document.createElement('a');
		 //setAttribute是设置属性的
	     a.setAttribute('href',getURL(path,name));
	     a.innerHTML = name;
	     //超链接加入单元格中
		 col1.appendChild(a);

		
		 //创建一个隐藏域
		 var input1= document.createElement('input');
		 input1.setAttribute('type','hidden');
		 input1.setAttribute('id',fileid);
		 input1.setAttribute('name',fileid);
		 input1.setAttribute('value',idv);
		 col1.appendChild(input1);
}
 
 /**
  * 文件的上传下载页面显示,此为没有权限删除的人的显示
  *idv为文件的id号、path为文件的路径、name为文件的名称、tableid是table的id号、fileid是需要创建的隐藏域，便于保存时循环取值 
  */
 
 function showfilelist1(baseUrl,idv,path,name,tableid,fileid){

		var node0 = document.getElementById(tableid);
		//取的行数
		row_index = parseInt(node0.rows.length);
		//创建一行
		addRow = node0.insertRow(row_index);
		addRow.id = "tr_"+idv;

		//创建一个单元格
		var col1 = addRow.insertCell(0);
		 //创建一个超链接
		 var a = document.createElement('a');
		 //setAttribute是设置属性的
	     a.setAttribute('href',getURL(path,name));
	     a.innerHTML = name;
	     //超链接加入单元格中
		 col1.appendChild(a);

		 //创建一个隐藏域
		 var input1= document.createElement('input');
		 input1.setAttribute('type','hidden');
		 input1.setAttribute('id',fileid);
		 input1.setAttribute('name',fileid);
		 input1.setAttribute('value',idv);
		 col1.appendChild(input1);
}
 
 
	//删除文件
 function deletefile(liid ){
 	$.messager.confirm('提示信息','确认删除所选项?',function(falge){
 	if(falge){
 		$("#"+liid).remove();
 		}	
 	});
 }
 
 
	//删除文件
function deletefileN(liid ){
	if(confirm('确认删除所选项？')){
		$("#"+liid).remove();
	}
}
 
 
 /*
  * js Map   
  */
 function Map() {
	 var struct = function(key, value) {
	  this.key = key;
	  this.value = value;
	 }
	 
	 var put = function(key, value){
	  for (var i = 0; i < this.arr.length; i++) {
	   if ( this.arr[i].key === key ) {
	    this.arr[i].value = value;
	    return;
	   }
	  }
	   this.arr[this.arr.length] = new struct(key, value);
	 }
	 
	 var get = function(key) {
	  for (var i = 0; i < this.arr.length; i++) {
	   if ( this.arr[i].key === key ) {
	     return this.arr[i].value;
	   }
	  }
	  return null;
	 }
	 
	 var remove = function(key) {
	  var v;
	  for (var i = 0; i < this.arr.length; i++) {
	   v = this.arr.pop();
	   if ( v.key === key ) {
	    continue;
	   }
	   this.arr.unshift(v);
	  }
	 }
	 
	 var size = function() {
	  return this.arr.length;
	 }

	 var maptostr = function() { 
		  var str="";
		  if(this.arr.length>0){
			for (var i = 0; i < this.arr.length; i++) {  
			   if (str=="") {  
				 str=this.arr[i].key+":"+this.arr[i].value;
			   }else{
				 str=str+","+this.arr[i].key+":"+this.arr[i].value;
			   }
		  }  
	  }
		return str;
	 }    
	 
	 var isEmpty = function() {
	  return this.arr.length <= 0;
	 }
	 this.arr = new Array();
	 this.get = get;
	 this.put = put;
	 this.remove = remove;
	 this.size = size;
	 this.isEmpty = isEmpty;
	 this.maptostr = maptostr;
	}
