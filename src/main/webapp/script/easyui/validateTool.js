/**最短位数验证*/
$.extend($.fn.validatebox.defaults.rules, {  
	minLength: {  
		validator: function(value, param){  
			return value.length >= param[0];  
		},  
		message: 'Please enter at least {0} characters.'  
	}  
});
/**最长位数验证*/
$.extend($.fn.validatebox.defaults.rules, {  
	maxLength: {  
		validator: function(value, param){  
			return value.length <= param[0];  
		},  
		message: 'Please enter at most {0} characters.'  
	}  
});
/**数字验证*/
$.extend($.fn.validatebox.defaults.rules, {  
	onlyNumber: {  
		validator: function(value, param){  
			return !isNaN(value);  
		},  
		message: '只能输入数字.'  
	}  
});
/**IP验证*/
$.extend($.fn.validatebox.defaults.rules, {  
	isIP: {  
		validator: function(value, param){
			var patrn =/^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/;//正则表达式，\d为数字,{1,3}一位或为三位.
			if(patrn.exec(value)){
				var laststr = value.split(".");    //用.把字符串str分开 
				var last_patrn=/^\d{1,3}$/;
				
			}
			return patrn.exec(value) && (parseInt(laststr[0])<=255 && parseInt(laststr[1])<=255&&parseInt(laststr[2])<=255&&parseInt(laststr[3])<=255);  
		},  
		message: 'IP格式不对.'  
	}  
});
/**日期验证*/
$.extend($.fn.validatebox.defaults.rules, {  
	isBig: {  
		validator: function(value, param){
			var d1Arr=$("#HOST").val().split('-');
			var d2Arr=$("#USERNAME").val().split('-');
			var v1=new Date(d1Arr[0],d1Arr[1],d1Arr[2]);
			var v2=new Date(d2Arr[0],d2Arr[1],d2Arr[2]);
			return v1<v2;  
		},  
		message: '结束日期必须大于开始日期.'  
	}  
});
/**密码验证*/
$.extend($.fn.validatebox.defaults.rules, {  
	checkPWD: {  
		validator: function(value, param){
			return value == $("#PASSWORD").val();  
		},  
		message: '两次密码输入不一致.'  
	}  
});
/**端口号验证*/
$.extend($.fn.validatebox.defaults.rules, {  
	checkPORT: {  
		validator: function(value, param){
			return !isNaN(value) && value <65536 && value >=0;  
		},  
		message: '端口号不符合规范.'  
	}  
});
