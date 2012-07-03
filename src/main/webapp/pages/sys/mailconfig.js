//初始化
var id;
$(document).ready(function() {
	$.ajax({
		url : baseUrl + '/mail/list',
		dataType : "json",
		type : "GET",
		success : function(json) {
			if(json != null){
				$("#LOGICALNAME").text(json.LOGICALNAME);
				$("#PASSWORD").text(json.PASSWORD);
				$("#HOST").text(json.HOST);
				$("#PORT").text(json.PORT);
				id = json.id;
			}else{
				isFirst = true;
			}
		}
	});
	/*
	 * 字符转JSON
	 */
	function strToJson(str) {
		var json = eval(str);
		return json;
	}
	$('#mailForm').validate({
		rules : {
			name : {
				required : true,
				email : true,
				maxlength: 50
			},
			HOST : {
				required : true,
				ipv4 : true,
				maxlength: 50
			},
			PORT : {
				required : true,
				range : [ 0, 65535 ],
				digits : true
			},
			PASSWORD : {
				required : true,
				maxlength: 20
			}
		}
	});
});