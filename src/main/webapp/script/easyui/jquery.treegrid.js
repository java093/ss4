/**
 * jQuery EasyUI 1.2.5
 * 
 * Licensed under the GPL terms
 * To use it on other terms please contact us
 *
 * Copyright(c) 2009-2011 stworthy [ stworthy@gmail.com ] 
 * 
 */
(function($) {
	function getObjectIndex(rows, row) {
		for ( var i = 0, count = rows.length; i < count; i++) {
			if (rows[i] == row) {
				return i;
			}
		}
		return -1;
	};
	function transferData(rows, row) {
		var index = getObjectIndex(rows, row);
		if (index != -1) {
			rows.splice(index, 1);
		}
	};
	function initGrid(target) {
		
		var opts = $.data(target, "treegrid").options;
		$(target).datagrid($.extend( {}, opts, {
			url : null,
			onLoadSuccess : function() {
			},
			onResizeColumn : function(field, width) {
				setRowHeight(target);
				opts.onResizeColumn.call(target, field, width);
			},
			onSortColumn : function(sortName, sortOrder) {
				opts.sortName = sortName;
				opts.sortOrder = sortOrder;
				if (opts.remoteSort) {
					request(target);
				} else {
					var data = $(target).treegrid("getData");
					loadData(target, 0, data);
				}
				opts.onSortColumn.call(target, sortName, sortOrder);
			},
			onBeforeEdit : function(rowIndex, rowData) {
				if (opts.onBeforeEdit.call(target, rowData) == false) {
					return false;
				}
			},
			onAfterEdit : function(rowIndex, row, newValues) {
				bindEvents(target);
				opts.onAfterEdit.call(target, row, newValues);
			},
			onCancelEdit : function(rowIndex, row) {
				bindEvents(target);
				opts.onCancelEdit.call(target, row);
			}
		}));
		if (opts.pagination) {
			var pager = $(target).datagrid("getPager");
			pager.pagination( {
				pageNumber : opts.pageNumber,
				pageSize : opts.pageSize,
				pageList : opts.pageList,
				onSelectPage : function(pageNumber, pageSize) {
					opts.pageNumber = pageNumber;
					opts.pageSize = pageSize;
					request(target);
				}
			});
			opts.pageSize = pager.pagination("options").pageSize;
		}
	};
	function setRowHeight(target, nodeId) {
		var opts = $.data(target, "datagrid").options;
		var panel = $.data(target, "datagrid").panel;
		var gridView = panel.children("div.datagrid-view");
		var gridView1 = gridView.children("div.datagrid-view1");
		var gridView2 = gridView.children("div.datagrid-view2");
		if (opts.rownumbers
				|| (opts.frozenColumns && opts.frozenColumns.length > 0)) {
			if (nodeId) {
				setHeight(nodeId);
				gridView2.find("tr[node-id=" + nodeId + "]").next("tr.treegrid-tr-tree")
						.find("tr[node-id]").each(function() {
							setHeight($(this).attr("node-id"));
						});
			} else {
				gridView2.find("tr[node-id]").each(function() {
					setHeight($(this).attr("node-id"));
				});
				if (opts.showFooter) {
					var footer = $.data(target, "datagrid").footer || [];
					for ( var i = 0; i < footer.length; i++) {
						setHeight(footer[i][opts.idField]);
					}
					$(target).datagrid("resize");
				}
			}
		}
		if (opts.height == "auto") {
			var gridBody1 = gridView1.children("div.datagrid-body");
			var gridBody2 = gridView2.children("div.datagrid-body");
			var height = 0;
			var lastHeight = 0;
			gridBody2.children().each(function() {
				var c = $(this);
				if (c.is(":visible")) {
					height += c.outerHeight();
					if (lastHeight < c.outerWidth()) {
						lastHeight = c.outerWidth();
					}
				}
			});
			if (lastHeight > gridBody2.width()) {
				height += 18;
			}
			gridBody1.height(height);
			gridBody2.height(height);
			gridView.height(gridView2.height());
		}
		gridView2.children("div.datagrid-body").triggerHandler("scroll");
		function setHeight(nodeId) {
			var tr1 = gridView1.find("tr[node-id=" + nodeId + "]");
			var tr2 = gridView2.find("tr[node-id=" + nodeId + "]");
			tr1.css("height", "");
			tr2.css("height", "");
			var height = Math.max(tr1.height(), tr2.height());
			tr1.css("height", height);
			tr2.css("height", height);
		};
	};
	function fixRowNumbers(target) {
		var opts = $.data(target, "treegrid").options;
		if (!opts.rownumbers) {
			return;
		}
		$(target).datagrid("getPanel").find("div.datagrid-view1 div.datagrid-body div.datagrid-cell-rownumber")
				.each(function(i) {
					var num = i + 1;
					$(this).html(num);
				});
	};
	function bindEvents(target) {
		var opts = $.data(target, "treegrid").options;
		var panel = $(target).datagrid("getPanel");
		var gridBody = panel.find("div.datagrid-body");
		gridBody.find("span.tree-hit").unbind(".treegrid").bind("click.treegrid",
				function() {
					var tr = $(this).parent().parent().parent();
					var id = tr.attr("node-id");
					toggle(target, id);
					return false;
				}).bind("mouseenter.treegrid", function() {
			if ($(this).hasClass("tree-expanded")) {
				$(this).addClass("tree-expanded-hover");
			} else {
				$(this).addClass("tree-collapsed-hover");
			}
		}).bind("mouseleave.treegrid", function() {
			if ($(this).hasClass("tree-expanded")) {
				$(this).removeClass("tree-expanded-hover");
			} else {
				$(this).removeClass("tree-collapsed-hover");
			}
		});
		gridBody.find("tr[node-id]").unbind(".treegrid").bind(
				"mouseenter.treegrid",
				function() {
					var id = $(this).attr("node-id");
					gridBody.find("tr[node-id=" + id + "]").addClass(
							"datagrid-row-over");
				}).bind(
				"mouseleave.treegrid",
				function() {
					var id = $(this).attr("node-id");
					gridBody.find("tr[node-id=" + id + "]").removeClass(
							"datagrid-row-over");
				}).bind("click.treegrid", function() {
			var id = $(this).attr("node-id");
			if (opts.singleSelect) {
				unselectAll(target);
				select(target, id);
			} else {
				if ($(this).hasClass("datagrid-row-selected")) {
					unselectAll(target, id);
				} else {
					select(target, id);
				}
			}
			opts.onClickRow.call(target, find(target, id));
		}).bind("dblclick.treegrid", function() {
			var id = $(this).attr("node-id");
			opts.onDblClickRow.call(target, find(target, id));
		}).bind("contextmenu.treegrid", function(e) {
			var id = $(this).attr("node-id");
			opts.onContextMenu.call(target, e, find(target, id));
		});
		gridBody.find("div.datagrid-cell-check input[type=checkbox]").unbind(
				".treegrid").bind("click.treegrid", function(e) {
			var id = $(this).parent().parent().parent().attr("node-id");
			if (opts.singleSelect) {
				unselectAll(target);
				select(target, id);
			} else {
				if ($(this).attr("checked")) {
					select(target, id);
				} else {
					unselectAll(target, id);
				}
			}
			e.stopPropagation();
		});
		var gridHeader = panel.find("div.datagrid-header");
		gridHeader.find("input[type=checkbox]").unbind().bind("click.treegrid",
				function() {
					if (opts.singleSelect) {
						return false;
					}
					if ($(this).attr("checked")) {
						selectAll(target);
					} else {
						unselectAll(target);
					}
				});
	};
	function initSubTree(target, nodeId) {
		var opts = $.data(target, "treegrid").options;
		var gridView = $(target).datagrid("getPanel").children("div.datagrid-view");
		var gridView1 = gridView.children("div.datagrid-view1");
		var gridView2 = gridView.children("div.datagrid-view2");
		var tr1 = gridView1.children("div.datagrid-body").find(
				"tr[node-id=" + nodeId + "]");
		var tr2 = gridView2.children("div.datagrid-body").find(
				"tr[node-id=" + nodeId + "]");
		var colspan1 = $(target).datagrid("getColumnFields", true).length
				+ (opts.rownumbers ? 1 : 0);
		var colspan2 = $(target).datagrid("getColumnFields", false).length;
		createSubTree(tr1, colspan1);
		createSubTree(tr2, colspan2);
		function createSubTree(tr, colspan) {
			$("<tr class=\"treegrid-tr-tree\">"
					+ "<td style=\"border:0px\" colspan=\"" + colspan
					+ "\">" + "<div></div>" + "</td>" + "</tr>")
					.insertAfter(tr);
		};
	};
	function loadData(target, nodeId, param, isAppend) {
		var opts = $.data(target, "treegrid").options;
		param = opts.loadFilter.call(target, param, nodeId);
		var panel = $.data(target, "datagrid").panel;
		var gridView = panel.children("div.datagrid-view");
		var gridView1 = gridView.children("div.datagrid-view1");
		var gridView2 = gridView.children("div.datagrid-view2");
		var row = find(target, nodeId);
			if (row) {
			var tr1 = gridView1.children("div.datagrid-body").find("tr[node-id=" + nodeId + "]");
			var tr2 = gridView2.children("div.datagrid-body").find("tr[node-id=" + nodeId + "]");
			var cc1 = tr1.next("tr.treegrid-tr-tree").children("td").children("div");
			var cc2 = tr2.next("tr.treegrid-tr-tree").children("td").children("div");
		} else {
			var cc1 = gridView1.children("div.datagrid-body").children("div.datagrid-body-inner");
			var cc2 = gridView2.children("div.datagrid-body");
		}
		if (!isAppend) {
			$.data(target, "treegrid").data = [];
			cc1.empty();
			cc2.empty();
		}
		if (opts.view.onBeforeRender) {
			opts.view.onBeforeRender.call(opts.view, target, nodeId, param);
		}
		opts.view.render.call(opts.view, target, cc1, true);
		opts.view.render.call(opts.view, target, cc2, false);
		if (opts.showFooter) {
			opts.view.renderFooter.call(opts.view, target, gridView1
					.find("div.datagrid-footer-inner"), true);
			opts.view.renderFooter.call(opts.view, target, gridView2
					.find("div.datagrid-footer-inner"), false);
		}
		if (opts.view.onAfterRender) {
			opts.view.onAfterRender.call(opts.view, target);
		}
		opts.onLoadSuccess.call(target, row, param);
		if (!nodeId && opts.pagination) {
			var total = $.data(target, "treegrid").total;
			var pager = $(target).datagrid("getPager");
			if (pager.pagination("options").total != total) {
				pager.pagination( {
					total : total
				});
			}
		}
		setRowHeight(target);
		fixRowNumbers(target);
		fixCheckboxSize();
		bindEvents(target);
		function fixCheckboxSize() {
			var gridHeader = gridView.find("div.datagrid-header");
			var gridBody = gridView.find("div.datagrid-body");
			var headerck = gridHeader.find("div.datagrid-header-check");
			if (headerck.length) {
				var ck = gridBody.find("div.datagrid-cell-check");
				if ($.boxModel) {
					ck.width(headerck.width());
					ck.height(headerck.height());
				} else {
					ck.width(headerck.outerWidth());
					ck.height(headerck.outerHeight());
				}
			}
		};
	};
	function request(target, parentId, param, isAppend, callBack) {
		var opts = $.data(target, "treegrid").options;
		var body = $(target).datagrid("getPanel").find("div.datagrid-body");
		if (param) {
			opts.queryParams = param;
		}
		var queryParams = $.extend( {}, opts.queryParams);
		if (opts.pagination) {
			$.extend(queryParams, {
				page : opts.pageNumber,
				rows : opts.pageSize
			});
		}
		if (opts.sortName) {
			$.extend(queryParams, {
				sort : opts.sortName,
				order : opts.sortOrder
			});
		}
		var row = find(target, parentId);
		if (opts.onBeforeLoad.call(target, row, queryParams) == false) {
			return;
		}
		if (!opts.url) {
			return;
		}
		var folder = body.find("tr[node-id=" + parentId + "] span.tree-folder");
		folder.addClass("tree-loading");
		$(target).treegrid("loading");
		$.ajax( {
			type : opts.method,
			url : opts.url,
			data : queryParams,
			dataType : "json",
			success : function(data) {
				folder.removeClass("tree-loading");
				$(target).treegrid("loaded");
				alert(parentId);
				loadData(target, parentId, data.rows, isAppend);
				if (callBack) {
					callBack();
				}
			},
			error : function() {
				folder.removeClass("tree-loading");
				$(target).treegrid("loaded");
				opts.onLoadError.apply(target, arguments);
				if (callBack) {
					callBack();
				}
			}
		});
	};
	function getRoot(target) {
		var roots = getRoots(target);
		if (roots.length) {
			return roots[0];
		} else {
			return null;
		}
	};
	function getRoots(target) {
		return $.data(target, "treegrid").data;
	};
	function getParent(target, nodeId) {
		var row = find(target, nodeId);
		//alert("row.HIGHERFOLDERID"+row.HIGHERFOLDERID);
		if (row.HIGHERFOLDERID) {
			return find(target, row.HIGHERFOLDERID);
		} else {
			return null;
		}
	};
	function getChildren(target, nodeId) {
		var opts = $.data(target, "treegrid").options;
		var body = $(target).datagrid("getPanel").find(
				"div.datagrid-view2 div.datagrid-body");
		var children = [];
		if (nodeId) {
			findChildren(nodeId);
		} else {
			var roots = getRoots(target);
			for ( var i = 0; i < roots.length; i++) {
				children.push(roots[i]);
				findChildren(roots[i][opts.idField]);
			}
		}
		function findChildren(nodeId) {
			var node = find(target, nodeId);
			if (node && node.children) {
				for ( var i = 0, len = node.children.length; i < len; i++) {
					var child = node.children[i];
					children.push(child);
					findChildren(child[opts.idField]);
				}
			}
		};
		return children;
	};
	function getSelected(target) {
		var target = getSelections(target);
		if (target.length) {
			return target[0];
		} else {
			return null;
		}
	};
	function getSelections(target) {
		var selectedRows = [];
		var panel = $(target).datagrid("getPanel");
		panel.find("div.datagrid-view2 div.datagrid-body tr.datagrid-row-selected")
				.each(function() {
					var id = $(this).attr("node-id");
					selectedRows.push(find(target, id));
				});
		return selectedRows;
	};
	function getLevel(target, nodeId) {
		if (!nodeId) {
			return 0;
		}
		var opts = $.data(target, "treegrid").options;
		var gridView = $(target).datagrid("getPanel").children("div.datagrid-view");
		var treeNode = gridView.find("div.datagrid-body tr[node-id=" + nodeId + "]")
				.children("td[field=" + opts.treeField + "]");
		return treeNode.find("span.tree-indent,span.tree-hit").length;
	};
	function find(target, id) {
		var opts = $.data(target, "treegrid").options;
		var data = $.data(target, "treegrid").data;
		var cc = [ data ];
		while (cc.length) {
			var c = cc.shift();
			for ( var i = 0; i < c.length; i++) {
				var rowData = c[i];
				if (rowData[opts.idField] == id) {
					return rowData;
				} else {
					if (rowData["children"]) {
						cc.push(rowData["children"]);
					}
				}
			}
		}
		return null;
	};
	function select(target, nodeId) {
		var gridBody = $(target).datagrid("getPanel").find("div.datagrid-body");
		var tr = gridBody.find("tr[node-id=" + nodeId + "]");
		tr.addClass("datagrid-row-selected");
		tr.find("div.datagrid-cell-check input[type=checkbox]").attr("checked",true);
	};
	function unselectAll(target, nodeId) {
		var gridBody = $(target).datagrid("getPanel").find("div.datagrid-body");
		var tr = gridBody.find("tr[node-id=" + nodeId + "]");
		tr.removeClass("datagrid-row-selected");
		tr.find("div.datagrid-cell-check input[type=checkbox]").attr("checked",false);
	};
	function selectAll(target) {
		var tr = $(target).datagrid("getPanel").find("div.datagrid-body tr[node-id]");
		tr.addClass("datagrid-row-selected");
		tr.find("div.datagrid-cell-check input[type=checkbox]").attr("checked",true);
	};
	function unselectAll(target) {
		var tr = $(target).datagrid("getPanel").find("div.datagrid-body tr[node-id]");
		tr.removeClass("datagrid-row-selected");
		tr.find("div.datagrid-cell-check input[type=checkbox]").attr("checked",false);
	};
	function collapse(target, nodeId) {
		var opts = $.data(target, "treegrid").options;
		var gridBody = $(target).datagrid("getPanel").find("div.datagrid-body");
		var row = find(target, nodeId);
		var tr = gridBody.find("tr[node-id=" + nodeId + "]");
		var hit = tr.find("span.tree-hit");
		if (hit.length == 0) {
			return;
		}
		if (hit.hasClass("tree-collapsed")) {
			return;
		}
		if (opts.onBeforeCollapse.call(target, row) == false) {
			return;
		}
		hit.removeClass("tree-expanded tree-expanded-hover").addClass("tree-collapsed");
		hit.next().removeClass("tree-folder-open");
		row.STATE = "CLOSED";
		tr = tr.next("tr.treegrid-tr-tree");
		var cc = tr.children("td").children("div");
		if (opts.animate) {
			cc.slideUp("normal", function() {
				setRowHeight(target, nodeId);
				opts.onCollapse.call(target, row);
			});
		} else {
			cc.hide();
			setRowHeight(target, nodeId);
			opts.onCollapse.call(target, row);
		}
	};
	function expand(target, nodeId) {
		var opts = $.data(target, "treegrid").options;
		var gridBody = $(target).datagrid("getPanel").find("div.datagrid-body");
		var tr = gridBody.find("tr[node-id=" + nodeId + "]");
		var hit = tr.find("span.tree-hit");
		var row = find(target, nodeId);
		if (hit.length == 0) {
			return;
		}
		if (hit.hasClass("tree-expanded")) {
			return;
		}
		if (opts.onBeforeExpand.call(target, row) == false) {
			return;
		}
		hit.removeClass("tree-collapsed tree-collapsed-hover").addClass("tree-expanded");
		hit.next().addClass("tree-folder-open");
		var subtree = tr.next("tr.treegrid-tr-tree");
		if (subtree.length) {
			var cc = subtree.children("td").children("div");
			expandSubtree(cc);
		} else {
			initSubTree(target, row[opts.idField]);
			var subtree = tr.next("tr.treegrid-tr-tree");
			var cc = subtree.children("td").children("div");
			cc.hide();
			request(target, row[opts.idField], {
				id : row[opts.idField]
			}, true, function() {
				expandSubtree(cc);
			});
		}
		function expandSubtree(cc) {
			row.STATE = "OPEN";
			if (opts.animate) {
				cc.slideDown("normal", function() {
					setRowHeight(target, nodeId);
					opts.onExpand.call(target, row);
				});
			} else {
				cc.show();
				setRowHeight(target, nodeId);
				opts.onExpand.call(target, row);
			}
		};
	};
	function toggle(target, nodeId) {
		var gridBody = $(target).datagrid("getPanel").find("div.datagrid-body");
		var tr = gridBody.find("tr[node-id=" + nodeId + "]");
		var hit = tr.find("span.tree-hit");
		if (hit.hasClass("tree-expanded")) {
			collapse(target, nodeId);
		} else {
			expand(target, nodeId);
		}
	};
	function collapseAll(target, nodeId) {
		var opts = $.data(target, "treegrid").options;
		var children = getChildren(target, nodeId);
		if (nodeId) {
			children.unshift(find(target, nodeId));
		}
		for ( var i = 0; i < children.length; i++) {
			collapse(target, children[i][opts.idField]);
		}
	};
	function expandAll(target, nodeId) {
		var opts = $.data(target, "treegrid").options;
		var children = getChildren(target, nodeId);
		if (nodeId) {
			children.unshift(find(target, nodeId));
		}
		for ( var i = 0; i < children.length; i++) {
			expand(target, children[i][opts.idField]);
		}
	};
	function expandTo(target, nodeId) {
		var opts = $.data(target, "treegrid").options;
		var ids = [];
		var p = getParent(target, nodeId);
		while (p) {
			var id = p[opts.idField];
			ids.unshift(id);
			p = getParent(target, id);
		}
		for ( var i = 0; i < ids.length; i++) {
			expand(target, ids[i]);
		}
	};
	function append(target, param) {
		var opts = $.data(target, "treegrid").options;
		if (param.parent) {
			var gridBody = $(target).datagrid("getPanel").find("div.datagrid-body");
			var tr = gridBody.find("tr[node-id=" + param.parent + "]");
			if (tr.next("tr.treegrid-tr-tree").length == 0) {
				initSubTree(target, param.parent);
			}
			var cell = tr.children("td[field=" + opts.treeField + "]").children(
					"div.datagrid-cell");
			var icon = cell.children("span.tree-icon");
			if (icon.hasClass("tree-file")) {
				icon.removeClass("tree-file").addClass("tree-folder");
				var hit = $("<span class=\"tree-hit tree-expanded\"></span>")
						.insertBefore(icon);
				if (hit.prev().length) {
					hit.prev().remove();
				}
			}
		}
		loadData(target, param.parent, param.data, true);
	};
	function remove(target, nodeId) {
		var opts = $.data(target, "treegrid").options;
		var gridBody = $(target).datagrid("getPanel").find("div.datagrid-body");
		var tr = gridBody.find("tr[node-id=" + nodeId + "]");
		tr.next("tr.treegrid-tr-tree").remove();
		tr.remove();
		var parent = del(nodeId);
		if (parent) {
			if (parent.children.length == 0) {
				tr = gridBody.find("tr[node-id=" + parent[opts.treeField] + "]");
				var cell = tr.children("td[field=" + opts.treeField + "]")
						.children("div.datagrid-cell");
				cell.find(".tree-icon").removeClass("tree-folder").addClass("tree-file");
				cell.find(".tree-hit").remove();
				$("<span class=\"tree-indent\"></span>").prependTo(cell);
			}
		}
		fixRowNumbers(target);
		function del(id) {
			var cc;
			var parent = getParent(target, nodeId);
			if (parent) {
				cc = parent.children;
			} else {
				cc = $(target).treegrid("getData");
			}
			for ( var i = 0; i < cc.length; i++) {
				if (cc[i][opts.treeField] == id) {
					cc.splice(i, 1);
					break;
				}
			}
			return parent;
		};
	};
	$.fn.treegrid = function(options, param) {
		if (typeof options == "string") {
			var method = $.fn.treegrid.methods[options];
			if (method) {
				return method(this, param);
			} else {
				return this.datagrid(options, param);
			}
		}
		options = options || {};
		return this.each(function() {
			var state = $.data(this, "treegrid");
			if (state) {
				$.extend(state.options, options);
			} else {
				$.data(this, "treegrid", {
					options : $.extend( {}, $.fn.treegrid.defaults,
							$.fn.treegrid.parseOptions(this), options),
					data : []
				});
			}
			initGrid(this);
			request(this);
		});
	};
	$.fn.treegrid.methods = {
		options : function(jq) {
			return $.data(jq[0], "treegrid").options;
		},
		resize : function(jq, options) {
			return jq.each(function() {
				$(this).datagrid("resize", options);
			});
		},
		fixRowHeight : function(jq, id) {
			return jq.each(function() {
				setRowHeight(this, id);
			});
		},
		loadData : function(jq, data) {
			return jq.each(function() {
				loadData(this, null, data);
			});
		},
		reload : function(jq, id) {
			return jq.each(function() {
				if (id) {
					var record = $(this).treegrid("find", id);
					if (record.children) {
						record.children.splice(0, record.children.length);
					}
					var gridBody = $(this).datagrid("getPanel").find(
							"div.datagrid-body");
					var tr = gridBody.find("tr[node-id=" + id + "]");
					tr.next("tr.treegrid-tr-tree").remove();
					var hit = tr.find("span.tree-hit");
					hit.removeClass("tree-expanded tree-expanded-hover")
							.addClass("tree-collapsed");
					expand(this, id);
				} else {
					request(this, null, {});
				}
			});
		},
		reloadFooter : function(jq, footer) {
			return jq.each(function() {
				var opts = $.data(this, "treegrid").options;
				var gridView = $(this).datagrid("getPanel").children(
						"div.datagrid-view");
				var gridView1 = gridView.children("div.datagrid-view1");
				var gridView2 = gridView.children("div.datagrid-view2");
				if (footer) {
					$.data(this, "treegrid").footer = footer;
				}
				if (opts.showFooter) {
					opts.view.renderFooter.call(opts.view, this, gridView1
							.find("div.datagrid-footer-inner"), true);
					opts.view.renderFooter.call(opts.view, this, gridView2
							.find("div.datagrid-footer-inner"), false);
					if (opts.view.onAfterRender) {
						opts.view.onAfterRender.call(opts.view, this);
					}
					$(this).treegrid("fixRowHeight");
				}
			});
		},
		loading : function(jq) {
			return jq.each(function() {
				$(this).datagrid("loading");
			});
		},
		loaded : function(jq) {
			return jq.each(function() {
				$(this).datagrid("loaded");
			});
		},
		getData : function(jq) {
			return $.data(jq[0], "treegrid").data;
		},
		getFooterRows : function(jq) {
			return $.data(jq[0], "treegrid").footer;
		},
		getRoot : function(jq) {
			return getRoot(jq[0]);
		},
		getRoots : function(jq) {
			return getRoots(jq[0]);
		},
		getParent : function(jq, id) {
			return getParent(jq[0], id);
		},
		getChildren : function(jq, id) {
			return getChildren(jq[0], id);
		},
		getSelected : function(jq) {
			return getSelected(jq[0]);
		},
		getSelections : function(jq) {
			return getSelections(jq[0]);
		},
		getLevel : function(jq, id) {
			return getLevel(jq[0], id);
		},
		find : function(jq, id) {
			return find(jq[0], id);
		},
		isLeaf : function(jq, id) {
			var opts = $.data(jq[0], "treegrid").options;
			var tr = opts.finder.getTr(jq[0], id);
			var hit = tr.find("span.tree-hit");
			return hit.length == 0;
		},
		select : function(jq, id) {
			return jq.each(function() {
				select(this, id);
			});
		},
		unselect : function(jq, id) {
			return jq.each(function() {
				unselectAll(this, id);
			});
		},
		selectAll : function(jq) {
			return jq.each(function() {
				selectAll(this);
			});
		},
		unselectAll : function(jq) {
			return jq.each(function() {
				unselectAll(this);
			});
		},
		collapse : function(jq, id) {
			return jq.each(function() {
				collapse(this, id);
			});
		},
		expand : function(jq, id) {
			return jq.each(function() {
				expand(this, id);
			});
		},
		toggle : function(jq, id) {
			return jq.each(function() {
				toggle(this, id);
			});
		},
		collapseAll : function(jq, id) {
			return jq.each(function() {
				collapseAll(this, id);
			});
		},
		expandAll : function(jq, id) {
			return jq.each(function() {
				expandAll(this, id);
			});
		},
		expandTo : function(jq, id) {
			return jq.each(function() {
				expandTo(this, id);
			});
		},
		append : function(jq, param) {
			return jq.each(function() {
				append(this, param);
			});
		},
		remove : function(jq, id) {
			return jq.each(function() {
				remove(this, id);
			});
		},
		refresh : function(jq, id) {
			return jq.each(function() {
				var opts = $.data(this, "treegrid").options;
				opts.view.refreshRow.call(opts.view, this, id);
			});
		},
		beginEdit : function(jq, id) {
			return jq.each(function() {
				$(this).datagrid("beginEdit", id);
				$(this).treegrid("fixRowHeight", id);
			});
		},
		endEdit : function(jq, id) {
			return jq.each(function() {
				$(this).datagrid("endEdit", id);
			});
		},
		cancelEdit : function(jq, id) {
			return jq.each(function() {
				$(this).datagrid("cancelEdit", id);
			});
		}
	};
	$.fn.treegrid.parseOptions = function(target) {
		var t = $(target);
		return $.extend( {}, $.fn.datagrid.parseOptions(target), {
			treeField : t.attr("treeField"),
			animate : (t.attr("animate") ? t.attr("animate") == "true" : undefined)
		});
	};
	var view = $.extend({},$.fn.datagrid.defaults.view,{
		render : function(target, container, frozen) {
			var opts = $.data(target, "treegrid").options;
			var fields = $(target).datagrid("getColumnFields", frozen);
			var grid = this;
			var nodes = buildTreeNodes(frozen, this.treeLevel, this.treeNodes);
			$(container).append(nodes.join(""));
			function buildTreeNodes(frozen, treeLevel, rows) {
				var html = [ "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>" ];
				for ( var i = 0; i < rows.length; i++) {
					var row = rows[i];
					if (row.STATE != "OPEN"
							&& row.STATE != "CLOSED") {
						row.STATE = "OPEN";
					}
					var style = opts.rowStyler ? opts.rowStyler.call(target, row) : "";
					var attr = style ? "style=\"" + style + "\"" : "";
					html.push("<tr node-id=" + row[opts.idField] + " " + attr + ">");
					html = html.concat(grid.renderRow.call(grid,target, fields, frozen, treeLevel, row));
					html.push("</tr>");
					if (row.children && row.children.length) {
						var tt = buildTreeNodes(frozen, treeLevel + 1, row.children);
						var v = row.STATE == "CLOSED" ? "none" : "block";
						html.push("<tr class=\"treegrid-tr-tree\"><td style=\"border:0px\" colspan="
								+ (fields.length + (opts.rownumbers ? 1 : 0))
								+ "><div style=\"display:"
								+ v + "\">");
						html = html.concat(tt);
						html.push("</div></td></tr>");
					}
				}
				html.push("</tbody></table>");
				return html;
			};
		},
		renderFooter : function(target, grid, frozen) {
			var opts = $.data(target, "treegrid").options;
			var footer = $.data(target, "treegrid").footer || [];
			var fields = $(target).datagrid("getColumnFields", frozen);
			var html = [ "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>" ];
			for ( var i = 0; i < footer.length; i++) {
				var row = footer[i];
				row[opts.idField] = row[opts.idField] || ("foot-row-id" + i);
				html.push("<tr node-id=" + row[opts.idField] + ">");
				html.push(this.renderRow.call(this, target, fields, frozen, 0, row));
				html.push("</tr>");
			}
			html.push("</tbody></table>");
			$(grid).html(html.join(""));
		},
		renderRow : function(target, fields, frozen, deepth, row) {
			var opts = $.data(target, "treegrid").options;
			var cc = [];
			if (frozen && opts.rownumbers) {
				cc.push("<td class=\"datagrid-td-rownumber\"><div class=\"datagrid-cell-rownumber\">0</div></td>");
			}
			for ( var i = 0; i < fields.length; i++) {
				var field = fields[i];
				var col = $(target).datagrid("getColumnOption", field);
				if (col) {
					var style = col.styler ? (col.styler(row[field], row) || "") : "";
					var style = col.hidden ? "style=\"display:none;"
							+ style + "\""
							: (style ? "style=\"" + style + "\"" : "");
					cc.push("<td field=\"" + field + "\" " + style + ">");
					var style = "width:" + (col.boxWidth) + "px;";
					style += "text-align:" + (col.align || "left") + ";";
					style += opts.nowrap == false ? "white-space:normal;" : "";
					cc.push("<div style=\"" + style + "\" ");
					if (col.checkbox) {
						cc.push("class=\"datagrid-cell-check ");
					} else {
						cc.push("class=\"datagrid-cell ");
					}
					cc.push("\">");
					if (col.checkbox) {
						if (row.checked) {
							cc.push("<input type=\"checkbox\" checked=\"checked\"/>");
						} else {
							cc.push("<input type=\"checkbox\"/>");
						}
					} else {
						var val = null;
						if (col.formatter) {
							val = col.formatter(row[field], row);
						} else {
							val = row[field] || "&nbsp;";
						}
						if (field == opts.treeField) {
							for ( var j = 0; j < deepth; j++) {
								cc.push("<span class=\"tree-indent\"></span>");
							}
							if (row.STATE == "CLOSED") {
								cc.push("<span class=\"tree-hit tree-collapsed\"></span>");
								cc.push("<span class=\"tree-icon tree-folder "
										+ (row.iconCls ? row.iconCls : "")
										+ "\"></span>");
							} else {
								if (row.children
										&& row.children.length) {
									cc.push("<span class=\"tree-hit tree-expanded\"></span>");
									cc.push("<span class=\"tree-icon tree-folder tree-folder-open "
											+ (row.iconCls ? row.iconCls : "")
											+ "\"></span>");
								} else {
									cc.push("<span class=\"tree-indent\"></span>");
									cc.push("<span class=\"tree-icon tree-file "
											+ (row.iconCls ? row.iconCls : "")
											+ "\"></span>");
								}
							}
							cc.push("<span class=\"tree-title\">" + val + "</span>");
						} else {
							cc.push(val);
						}
					}
					cc.push("</div>");
					cc.push("</td>");
				}
			}
			return cc.join("");
		},
		refreshRow : function(target, id) {
			var row = $(target).treegrid("find", id);
			var opts = $.data(target, "treegrid").options;
			var gridBody = $(target).datagrid("getPanel").find("div.datagrid-body");
			var style = opts.rowStyler ? opts.rowStyler.call(target,row) : "";
			var style = style ? style : "";
			var tr = gridBody.find("tr[node-id=" + id + "]");
			tr.attr("style", style);
			tr.children("td").each(function() {
				var cell = $(this).find("div.datagrid-cell");
				var field = $(this).attr("field");
				var col = $(target).datagrid("getColumnOption", field);
				if (col) {
					var style = col.styler ? (col.styler(row[field], row) || "") : "";
					var style = col.hidden ? "display:none;" + style : (style ? style : "");
					$(this).attr("style", style);
					var val = null;
					if (col.formatter) {
						val = col.formatter(row[field], row);
					} else {
						val = row[field] || "&nbsp;";
					}
					if (field == opts.treeField) {
						cell.children("span.tree-title").html(val);
						var cls = "tree-icon";
						var icon = cell.children("span.tree-icon");
						if (icon.hasClass("tree-folder")) {
							cls += " tree-folder";
						}
						if (icon.hasClass("tree-folder-open")) {
							cls += " tree-folder-open";
						}
						if (icon.hasClass("tree-file")) {
							cls += " tree-file";
						}
						if (row.iconCls) {
							cls += " " + row.iconCls;
						}
						icon.attr("class", cls);
					} else {
						cell.html(val);
					}
				}
			});
			$(target).treegrid("fixRowHeight", id);
		},
		onBeforeRender : function(target, nodeId, param) {
			if (!param) {
				return false;
			}
			var opts = $.data(target, "treegrid").options;
	//		alert("param.length=="+param.length);
			if (param.length == undefined) {
				if (param.footer) {
					$.data(target, "treegrid").footer = param.footer;
				}
				if (param.total) {
					$.data(target, "treegrid").total = param.total;
				}
				param = this.transfer(target, nodeId, param.rows);
			} else {
				function setParent(param, nodeId) {
					for ( var i = 0; i < param.length; i++) {						
						var row = param[i];
						row.HIGHERFOLDERID = nodeId;
						if (row.children && row.children.length) {
							setParent(row.children, row[opts.idField]);
						}
					}
				};
				setParent(param, nodeId);
			}
			var node = find(target, nodeId);
			if (node) {
				if (node.children) {
					node.children = node.children.concat(param);
				} else {
					node.children = param;
				}
			} else {
				$.data(target, "treegrid").data = $.data(target,
						"treegrid").data.concat(param);
			}
			if (!opts.remoteSort) {
				this.sort(target, param);
			}
			this.treeNodes = param;
			this.treeLevel = $(target).treegrid("getLevel", nodeId);
		},
		sort : function(target, param) {
			var opts = $.data(target, "treegrid").options;
			var opt = $(target).treegrid("getColumnOption", opts.sortName);
			if (opt) {
				var sorter = opt.sorter || function(a, b) {
					return (a > b ? 1 : -1);
				};
				sort(param);
			}
			function sort(param) {
				param.sort(function(r1, r2) {
					return sorter(r1[opts.sortName],
							r2[opts.sortName])
							* (opts.sortOrder == "asc" ? 1 : -1);
				});
				for ( var i = 0; i < param.length; i++) {
					var children = param[i].children;
					if (children && children.length) {
						sort(children);
					}
				}
			};
		},
		transfer : function(target, nodeId, data) {
			var opts = $.data(target, "treegrid").options;
			var rows = [];
			for ( var i = 0; i < data.length; i++) {
				rows.push(data[i]);
			}
			var children = [];
			for ( var i = 0; i < rows.length; i++) {
				var row = rows[i];
				if (!nodeId) {
					if (!row.HIGHERFOLDERID) {
						children.push(row);
						transferData(rows, row);
						i--;
					}
				} else {
					if (row.HIGHERFOLDERID == nodeId) {
						children.push(row);
						transferData(rows, row);
						i--;
					}
				}
			}
			var toDo = [];
			for ( var i = 0; i < children.length; i++) {
				toDo.push(children[i]);
			}
			while (toDo.length) {
				var node = toDo.shift();
				for ( var i = 0; i < rows.length; i++) {
					var row = rows[i];
					if (row.HIGHERFOLDERID == node[opts.idField]) {
						if (node.children) {
							node.children.push(row);
						} else {
							node.children = [ row ];
						}
						toDo.push(row);
						transferData(rows, row);
						i--;
					}
				}
			}
			return children;
		}
	});
	$.fn.treegrid.defaults = $.extend({},$.fn.datagrid.defaults,{
		treeField : null,
		animate : false,
		singleSelect : true,
		view : view,
		loadFilter : function(data, nodeId) {
			return data;
		},
		finder : {
			getTr : function(target, id, type, step) {
				type = type || "body";
				step = step || 0;
				var dc = $.data(target, "datagrid").dc;
				if (step == 0) {
					var opts = $.data(target, "treegrid").options;
					var tr1 = opts.finder.getTr(target, id, type, 1);
					var tr2 = opts.finder.getTr(target, id, type, 2);
					return tr1.add(tr2);
				} else {
					if (type == "body") {
						return (step == 1 ? dc.body1 : dc.body2) .find("tr[node-id=" + id + "]");
					} else {
						if (type == "footer") {
							return (step == 1 ? dc.footer1 : dc.footer2).find("tr[node-id=" + id + "]");
						} else {
							if (type == "selected") {
								return (step == 1 ? dc.body1 : dc.body2).find("tr.datagrid-row-selected");
							} else {
								if (type == "last") {
									return (step == 1 ? dc.body1 : dc.body2).find("tr:last[node-id]");
								} else {
									if (type == "allbody") {
										return (step == 1 ? dc.body1 : dc.body2).find("tr[node-id]");
									} else {
										if (type == "allfooter") {
											return (step == 1 ? dc.footer1 : dc.footer2).find("tr[node-id]");
										}
									}
								}
							}
						}
					}
				}
			},
			getRow : function(target, id) {
				return $(target).treegrid("find", id);
			}
		},
		onBeforeLoad : function(row, param) {
		},
		onLoadSuccess : function(row, data) {
		},
		onLoadError : function() {
		},
		onBeforeCollapse : function(row) {
		},
		onCollapse : function(row) {
		},
		onBeforeExpand : function(row) {
		},
		onExpand : function(row) {
		},
		onClickRow : function(row) {
		},
		onDblClickRow : function(row) {
		},
		onContextMenu : function(e, row) {
		},
		onBeforeEdit : function(row) {
		},
		onAfterEdit : function(row, newValues) {
		},
		onCancelEdit : function(row) {
		}
	});
})(jQuery);
