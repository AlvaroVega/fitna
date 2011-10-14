/*$(document).ready(function(){
	if ($("title").html().indexOf("NaTests") > -1) {
		$.ajax({
			  url: '/' + $("title").html() + '?generateAditionalFile',
			  success: function(data) {
				  convert_tables();
			  },
			  error: function(data) {
				  alert("There was a problem accessing the page.");
			  }			  
		});		
	}
});*/

function convert_tables() {
	$("div.main > table").each(function(i){
		$(this).attr("id", "t" + (i+1));
		$(this).attr("class", "fitna_table");
		$(this).attr("border", "0");
		convert_rows("t" + (i+1));
		insert_exp_rows("t" + (i+1));
		paint_cols_showed("t" + (i+1));
	});
}	

function convert_rows(idTable) {
	$("#" + idTable + " tr").each(function(i){
		if (i == 0) { // 1st row of the table, that contains the name of the test class
			var numCols = num_cols_table(idTable);
			if (numCols > -1) //Mejor poner los span al final....
				$($(this).children().get(0)).attr("colspan", (numCols+1).toString());
			$($(this).children().get(0)).attr("class", "top_cell");	
			$(this).prepend('<td class="left_col"></td>');
		}	else if (i == 1) {
			$(this).attr("id", idTable + "_r" + (i-1));
			$(this).prepend('<td>Id. prueba</td>');
			$(this).prepend('<td class="left_col"><img class="plus_minus" src="/files/images/plus.gif"/></td>');
			$(this).append('<td class="right_col"></td>');
		} else {
			$(this).attr("id", idTable + "_r" + (i-1));
			convert_cells(idTable + "_r" + (i-1));
			$(this).prepend('<td id="' + idTable + '_r' + (i-1) + '_id"></td>');
			$(this).prepend('<td class="left_col"><img class="plus_minus" src="/files/images/plus.gif"/></td>');
			var rightCell = $('<td class="right_col"></td>');
			rightCell.append('<img class="del_img" src="/files/images/icon_delete.jpg" height="20" width="20" title="Delete row"/>&nbsp;');
			rightCell.append('<img class="copy_img" src="/files/images/icon_copy.png" height="20" width="20" title="Add new row from these values"/>&nbsp;');
			$(this).append(rightCell);
			$.ajax({
				url: '/' + $("title").html() + '?getValueAditionalFile&tableNumber='+idTable+'&id=r' + (i-1) + '_id',
				success: function(idRow) {
					$.ajax({
						url: '/' + $("title").html() + '?getValueAditionalFile&tableNumber='+idTable+'&id=r' + (i-1) + '_desc',
						success: function(descRow) {
							$("#" + idTable + "_r" + (i-1) + "_id").attr("title", descRow);
							$("#" + idTable + "_r" + (i-1) + "_id").html(idRow);
						},
						error: function(data) {
							alert("Test id and description cannot be obtained.");
						}
					});
				},
			  	error: function(data) {
			  		alert("Test id and description cannot be obtained.");
			  	}
			});
		}
	});
}	

function convert_cells(idRow) {
	$("#" + idRow + " td").each(function(i){
		$(this).attr("id", idRow + "c" + (i+1));		
	});		
}

function insert_exp_rows(idTable) {
	$("#" + idTable + " tr").each(function(i){
		if (i>0) { // Row with table name hasn't got expanded row
			var idRow = $(this).attr("id");
			var rowExp = $('<tr id="' + idRow + '_exp" class="row_exp"></tr>');
			rowExp.append('<td class="left_col_tree"></td>');
			
			var treeCell = $('<td class="tree_cell" colspan="' + (num_cols_table(idTable)-2) + '">');
			var ulItem = $('<ul class="mktree" id="' + idTable + '_tree' + idRow.substring(idRow.length-1, idRow.length) + '">');
			if (i>1){ // Next elements only added for rows different to header
				ulItem.append('&nbsp;&nbsp;&nbsp;Identificador prueba:&nbsp;<input id="' + idRow + '_id_new" size="30" type="text" value=""/><br/>');
				ulItem.append('Descripci&oacute;n prueba:&nbsp;&nbsp;<input id="' + idRow + '_desc_new" size="30" type="text" value=""/>');
				$.ajax({
					url: '/' + $("title").html() + '?getValueAditionalFile&tableNumber='+idTable+'&id=r' + (i-1) + '_id',
					success: function(idRowOld) {
						$.ajax({
							url: '/' + $("title").html() + '?getValueAditionalFile&tableNumber='+idTable+'&id=r' + (i-1) + '_desc',
							success: function(descRowOld) {
								$("#" + idRow + "_id_new").attr("value", idRowOld);
								$("#" + idRow + "_desc_new").attr("value", descRowOld);
							},
							error: function(data) {
								alert("Test id and description cannot be obtained.");
							}
						});
					},
				  	error: function(data) {
				  		alert("Test id and description cannot be obtained.");
				  	}
				});

			}
			generate_tree (ulItem, idTable, idRow);
			treeCell.append(ulItem);
			if (i>1) {
				treeCell.append('<input class="save_values" type="submit" value="Save"/>');				
			} else {
				treeCell.append('<input class="save_cols" type="submit" value="Save"/>');
			}
			rowExp.append(treeCell);
			$(this).after(rowExp);
		}	
	});	
}

function generate_tree (ulItem, idTable, idRow) {
	var inputLi = $('<li>ENTRADA</li>').append('<ul></ul>');
	var outputLi = $('<li>SALIDA</li>').append('<ul></ul>');
	var isRow0 = idRow.substring(idRow.length-1, idRow.length) == "0";
	var paramsRowCells = $($("#" + idTable + " tr").get(1)).children();
	for (i=2; i<paramsRowCells.length-1; i++) {
		var paramName = $(paramsRowCells[i]).html();
		if (paramName.substring(paramName.length-1, paramName.length) == "?") { //Output param
			if (isRow0) {
				build_tree_item_row0 (outputLi, paramName, idTable, i-1);
			} else {
				build_tree_item (outputLi, paramName.substring(0, paramName.length-1), $("#" + idRow + "c" + (i-1)).html(), idRow, i-1);
			}
		} else { //Input param
			if (isRow0) {
				build_tree_item_row0 (inputLi, paramName, idTable, i-1);
			} else {
				build_tree_item (inputLi, paramName, $("#" + idRow + "c" + (i-1)).html(), idRow, i-1);			
			}
		}
	}
	ulItem.append(inputLi);
	ulItem.append(outputLi);			
}

function build_tree_item_row0 (itemLi, parameter, idTable, numCol) {
	var subItemLi = $('<li>' + parameter + '&nbsp;&nbsp;&nbsp;Show?&nbsp;<input id="' + idTable + '_show_c' + numCol + '" type="checkbox" value="show_c' + numCol + '</li>');//" checked="checked"/></li>');
	$(itemLi.children().get(0)).append(subItemLi);
}

function build_tree_item (itemLi, parameterName, parameterValue, idRow, numCol) {
	var subItemLi;
	if (parameterValue == "&nbsp;"){
		subItemLi = $('<li>' + parameterName + ' <input id="' + idRow + 'c' + numCol + '_new" size="30" type="text" value=""/></li>');
	} else {
		subItemLi = $('<li>' + parameterName + ' <input id="' + idRow + 'c' + numCol + '_new" size="30" type="text" value="' + parameterValue + '"/></li>');		
	}
	$(itemLi.children().get(0)).append(subItemLi);
}

function num_cols_table(idTable) {
	var rows = $("#" + idTable + " tr");
	if (rows.length >= 2) {
		return $(rows.get(1)).children("td").length;
	} else {
		return -1;
	}
}

function paint_cols_showed(idTable){
	$.ajax({
		  url: '/' + $("title").html() + '?getValueAditionalFile&tableNumber='+idTable+'&id=colsShowed',
		  success: function(data) {
			  var colsShowed = eval('(' + data + ')');
			  var numCols = $("#" + idTable + "_r0").children("td").length;
			  var nColsShowed = numColsShowed(colsShowed);
			  for (i=0; i<numCols-3; i++) {
				  var stl;
				  if (colsShowed[i]) { 
					  stl = 'table-cell';
					  $("#" + idTable + "_show_c" + (i+1)).attr("checked", "checked");
				  } else {
					  stl = 'none';
				  } 
				  $("#" + idTable + " tr").each(function(j){
					  if (rowCollapsible(j)) {
						  $(this.cells[i+2]).css("display", stl);
					  } else {
						  $(this.cells[1]).attr("colspan", "" + (nColsShowed+1) + ""); 
					  }
				  });
			  }
		  },
		  error: function(data) {
			  alert("Problem showing/hiding columns.");
		  } 
	});
}