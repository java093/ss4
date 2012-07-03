
$(document)
		.ready(
				function() {
				// 初始化最近24小时任务状态
					$.ajax({
						url : baseUrl + '/index/findtaskstate',
						dataType : "json",
						type : "GET",
						error : function() {
							$.messager.alert('提示信息', "数据加载失败。。。", "error");
						},
						success : function(data) {
							$("#task_state tr:eq(1) td:nth-child(2)").html(
									data.total);
							$("#task_state tr:eq(2) td:nth-child(2)").html(
									data.running);
							$("#task_state tr:eq(3) td:nth-child(2)").html(
									data.success);
							$("#task_state tr:eq(4) td:nth-child(2)").html(
									data.fail);
							$("#task_state tr:eq(5) td:nth-child(2)").html(
									data.other);
						}
					});

					var plot_data = new Array();
					$
							.ajax({
								url : baseUrl + '/index/monthlyrun',
								dataType : "json",
								type : "GET",
								error : function() {
									$.messager.alert('提示信息', "数据加载失败。。。",
											"error");
								},
								success : function(data) {
									var dataArray = new Array();
									for ( var i = 0; i < data.rows.length; i++) {
										i = parseInt(i);
										plot_data.push([ data.rows[i].day,
												data.rows[i].time ]);
									}

									$(function() {
										function plotWithOptions(t) {
											$
													.plot(
															$("#placeholder"),
															[ {
																data : plot_data,
																//label: "每日总任务时间",
																//color : "green",
																threshold : {
																	below : t,
																	color : "green"
																},
																lines : {
																	steps : false
																}
															} ],
															{
																series : {
																	lines : {
																		show : true
																	},
																	points : {
																		show : true
																	}
																},
																xaxis : {
																	tickFormatter : tickFormatter,
																	ticks : data.rows.length
																},
																yaxis : {
																	ticks : 2,
																	min : 0
																},
																grid : {
																	backgroundColor : {
																		colors : [
																				"#fff",
																				"#eee" ]
																	},
																	hoverable : true,
																	clickable : true
																}
															});
										}
										;

										plotWithOptions(80);
										function tickFormatter(val) {
											var show = val + "日";
											return show;
										}

										function formatDate(val) {
											return val;
										}

										function showTooltip(x, y, contents) {
											$(
													'<div id="tooltip">'
															+ contents
															+ '</div>').css({
												position : 'absolute',
												display : 'none',
												top : y + 5,
												left : x + 5,
												border : '1px solid #fdd',
												padding : '2px',
												'background-color' : '#fee',
												opacity : 0.80
											}).appendTo("body").fadeIn(200);
										}

										var previousPoint = null;
										$("#placeholder")
												.bind(
														"plothover",
														function(event, pos,
																item) {
															$("#x")
																	.text(
																			pos.x
																					.toFixed(2));
															$("#y")
																	.text(
																			pos.y
																					.toFixed(2));

															if (item) {
																if (previousPoint != item.dataIndex) {
																	previousPoint = item.dataIndex;

																	$(
																			"#tooltip")
																			.remove();
																	var x = item.datapoint[0], y = item.datapoint[1];
																	var ctn = "统计日期："
																			+ formatDate(x)
																			+ "日, 运行时间："
																			+ y
																			+ "小时";
																	showTooltip(
																			item.pageX,
																			item.pageY,
																			ctn);
																}
															} else {
																$("#tooltip")
																		.remove();
																previousPoint = null;
															}
														});

									});
								}
							});
				});




