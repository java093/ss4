//验证油箱格式
function isEmail(strEmail) {
	if (strEmail.search(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) != -1){
		return true;
	}else{
		return false;
	}
}


// 长度限制
function isLength(str){
	if(str.length>3){
		return false;
	}else{
		return true;
	}
}


//只能是英文和数字
function  isEnglish(name)  
{  
	if(name.length  ==  0)
		return  false;
	for(i  =  0;  i  <  name.length;  i++)  {  
		if(name.charCodeAt(i)  >  128)
		return  false;
	}
	return  true;
}

//只能是中文
function  isChinese(name)  {  
if(name.length  ==  0)
	return  false;
for(i  =  0;  i  <  name.length;  i++)  {  
	if(name.charCodeAt(i)  >  128)
	return  true;
}
	return  false;
}

//只能是数字
function  isNumber(name)  
{  
	if(name.length  ==  0)
		return  false;
	for(i  =  0;  i  <  name.length;  i++)  {  
		if(name.charAt(i)  <  "0"  ||  name.charAt(i)  >  "9")
		return  false;
	}
		return  true;
}


//判断密码是否输入一致     
function issame(str1,str2)  
{  
	if (str1==str2)  
	{return(true);}  
	else  
	{return(false);}  
}  


//判断用户名是否为数字字母下滑线 
function notchinese(str){ 
	var reg=/[^A-Za-z0-9_]/g 
	    if (reg.test(str)){ 
	    	return (false); 
	    }else{ 
	    	return(true);    
	    } 
} 




//检查输入字符串是否为空或者全部都是空格
function isNull( str ){
	if ( str == "" ) return true;
	var regu = "^[ ]+$";
	var re = new RegExp(regu);
	return re.test(str);
}

//判断IP是否合法
function isIP(strIP) {
	if (isNull(strIP)) return false;
		var re=/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/g //匹配IP地址的正则表达式
		if(re.test(strIP)){
			if( RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256) 
				return true;
		}
			return false;
	}

//日期比较
function isDate(d1,d2) {
	d1Arr=d1.split('-');
	d2Arr=d2.split('-');
	v1=new Date(d1Arr[0],d1Arr[1],d1Arr[2]);
	v2=new Date(d2Arr[0],d2Arr[1],d2Arr[2]);
	if(v1 >= v2)
		return false;
	else
		return true;
}



 //比较时间 格式 yyyy-mm-dd hh:mi:ss  
function isTime(beginTime,endTime){  
	//beginTime = "2009-09-21 00:00:00";  
	
	beginTime =  beginTime.replace(/-/g,"/");
//	alert(beginTime);
	d1 = new Date(beginTime)
//	 var endTime = "2009-09-21 00:00:01";  
	 var beginTimes=beginTime.substring(0,10).split('-');  
//	 var endTimes=endTime.substring(0,10).split('-');  
	  
	 beginTime=beginTimes[1]+'-'+beginTimes[2]+'-'+beginTimes[0]+' '+beginTime.substring(10,19);  
//	 endTime=endTimes[1]+'-'+endTimes[2]+'-'+endTimes[0]+' '+endTime.substring(10,19);  
	  
//	 alert(beginTime+"aaa"+endTime);  
//	 alert("beginTime = " + Date.parse(d1));  
//	 alert("endTime = "  + Date.parse(endTime));  
//	 var a =(Date.parse(endTime)-Date.parse(beginTime))/3600/1000;  
	 if(Date.parse(d1) > endTime ){  
		 return false;
	 }else {  
		 return true; 
	 }
} 



