
	$.extend($.fn.treegrid.methods,{
		
		cascadeCheck : function(target,param){
		//	alert("s1111");
			var opts = $.data(target[0], "treegrid").options;
			if(opts.singleSelect)
				return;
			var idField = opts.idField;
			var status = false;
			var selectNodes = $(target).treegrid('getSelections');
			for(var i=0;i<selectNodes.length;i++){
				if(selectNodes[i][idField]==param.id)
					status = true;
			}
			//alert("ssss");
			selectParent(target[0],param.id,idField,status);
			selectChildren(target[0],param.id,idField,param.deepCascade,status);
			function selectParent(target,id,idField,status){
				var parent = $(target).treegrid('getParent',id);
				if(parent){
					var parentId = parent[idField];
					if(status)
						$(target).treegrid('select',parentId);
					//else
					//	$(target).treegrid('unselect',parentId);
					//selectParent(target,parentId,idField,status);
				}
			}
			function selectChildren(target,id,idField,deepCascade,status){
				if(!status&&deepCascade)
					$(target).treegrid('expand',id);
				var children = $(target).treegrid('getChildren',id);
				for(var i=0;i<children.length;i++){
					var childId = children[i][idField];
					if(status)
						$(target).treegrid('select',childId);
					else
						$(target).treegrid('unselect',childId);
					selectChildren(target,childId,idField,deepCascade,status);
				}
			}
		}
	});



	$.extend($.fn.treegrid.defaults,{
		onLoadSuccess : function() {
		//alert("ddd");
			var target = $(this);
			var opts = $.data(this, "treegrid").options;
			var panel = $(this).datagrid("getPanel");
			var gridBody = panel.find("div.datagrid-body");
			var idField = opts.idField;
			gridBody.find("div.datagrid-cell-check input[type=checkbox]").unbind(".treegrid").click(function(e){
				if(opts.singleSelect) return;
				if(opts.cascadeCheck||opts.deepCascadeCheck){
					var id=$(this).parent().parent().parent().attr("node-id");
					var status = false;
					if($(this).attr("checked")) status = true;
					selectParent(target,id,idField,status);
					selectChildren(target,id,idField,opts.deepCascadeCheck,status);
						function selectParent(target,id,idField,status){
						var parent = target.treegrid('getParent',id);
						if(parent){
							var parentId = parent[idField];
							if(status)
								target.treegrid('select',parentId);
							else
								target.treegrid('unselect',parentId);
							//selectParent(target,parentId,idField,status);
						}
					}
						function selectChildren(target,id,idField,deepCascade,status){
						if(status&&deepCascade)
							target.treegrid('expand',id);
						var children = target.treegrid('getChildren',id);
						for(var i=0;i<children.length;i++){
							var childId = children[i][idField];
							if(status)
								target.treegrid('select',childId);
							else
								target.treegrid('unselect',childId);
							selectChildren(target,childId,idField,deepCascade,status);
						}
					}
				}
				e.stopPropagation();
			});
		}
	});