$(document).ready(function(){
	if ($("title").html().indexOf("NaTests") > -1) {
		$.ajax({
			  url: '/' + $("title").html() + '?generateAditionalFile',
			  success: function(data) {
				  // Generate a Fitna adapted html from the Fitnesse-original html
				  convert_tables();
				  
				  // Set events
				  $("img.plus_minus").click(function(evento){
					  var trParent = $(this).parents("tr");
					  show_hide_row(trParent.attr("id"));
				  });

				  $("img.del_img").click(function(evento){
					  var trParent = $(this).parents("tr");
					  delete_row(trParent.attr("id"));
				  });

				  $("img.copy_img").click(function(evento){
					  var trParent = $(this).parents("tr");
					  var tableParent = $(this).parents("table");
					  copy_row(tableParent.attr("id"), trParent.attr("id"));
				  });

				  $("input.save_cols").click(function(evento){
					  var tableParent = $(this).parents("table");
					  save_row_0(tableParent.attr("id"));
				  });

				  $("input.save_values").click(function(evento){
					  var trParent = $(this).parents("tr");
					  save_row(trParent.prev().attr("id")); //Se coge la fila anterior que no es la expandida
				  });
				  
				  // Convert ul and li items in a collapsible and expandible tree
				  convertTrees();
			  },
			  error: function(data) {
				  alert("There was a problem accessing the page.");
			  }			  
		});		
	}
});

function show_hide_row(idRow) {
	var rowExp = $("#" + idRow + "_exp");
	if (rowExp.css("display") == 'none') {
		rowExp.css("display", "table-row");
		$(this).attr("src", "/files/images/minus.gif");
	} else {
		rowExp.css("display", "none");
		$(this).attr("src", "/files/images/plus.gif");
	}			
}
	

function delete_row(idRow) {
	//Delete row from content.txt
	/*var contentLineTxt = "|";
	$("#" + idRow + " td").each(function(i){
		if ($(this).attr("id").indexOf(idRow + "c") > -1) { //If it is a parameter column
			contentLineTxt = contentLineTxt + $(this).html() + "|";
		}
	});
	contentLineTxt = contentLineTxt.replace(/&nbsp;/g, "");*/
	
	var idRowParts = idRow.split("_");
	$.ajax({
		//removeLineContentFile&tableNumber=t1&fileNumber=r3
		//url: '/' + $("title").html() + '?removeLineContentFile&line=' + encodeURI(contentLineTxt),
		url: '/' + $("title").html() + '?removeLineContentFile&tableNumber=' + idRowParts[0] + '&fileNumber=' + idRowParts[1],
		success: function(data) {
			// Delete rows from html
			var row = $("#" + idRow);
			row.remove();
			var rowExp = $("#" + idRow + "_exp");
			rowExp.remove();
		},
		error: function(data) {
			alert("Problem deleting row.");
		} 
	});
}


function copy_row(idTable, idRow){ 
	// get for currentIdRow
	$.ajax({
		  url: '/' + $("title").html() + '?getValueAditionalFile&tableNumber='+idTable+'&id=currentIdRow',
		  success: function(data) {
				var oldIdRow = parseInt(data);
				var currentIdRow = oldIdRow + 1;
				// update new currentIdRow value
				$.ajax({
					  url: '/' + $("title").html() + '?setValueAditionalFile&tableNumber='+idTable+'&id=currentIdRow&value=' + currentIdRow, 
					  success: function(data2) {
						  	var currentRow = $("#" + idRow);
							var currentRowExp = $("#" + idRow + "_exp");

							var newRow = currentRow.clone();
							newRow.attr("id", idTable + "_r" + currentIdRow);
							newRow.find(".plus_minus").click(function(evento){
								var trParent = $(this).parents("tr");
								show_hide_row(trParent.attr("id"));
							});
							$(newRow.children().get(1)).attr("id", idTable + "_r" + currentIdRow + "_id");							
							var numCols = newRow.children("td").length;
							var newLineTxt = "|"; //Save values in content.txt
							for (i=1; i<numCols-2; i++) {
								var child = $(newRow.children().get(i+1));
								child.attr("id", idTable + "_r" + currentIdRow + "c" + i);
								newLineTxt = newLineTxt + child.html() + "|";
							}
							newLineTxt = newLineTxt.replace(/&nbsp;/g, "");
							newRow.find(".del_img").click(function(evento){
								var trParent = $(this).parents("tr");
								delete_row(trParent.attr("id"));
							});
							newRow.find(".copy_img").click(function(evento){
								var trParent = $(this).parents("tr");
								var tableParent = $(this).parents("table");
								copy_row(tableParent.attr("id"), trParent.attr("id"));								
							});
							
							//Save new row in content.txt
							$.ajax({
								//url: '/' + $("title").html() + '?addLineContentFile&line=' + encodeURI(newLineTxt),
								url: '/' + $("title").html() + '?addLineContentFile&tableNumber=' + idTable + '&new_line=' + encodeURI(newLineTxt),
								success: function(data3) {
									// Save id and description in add.txt
									$.ajax({
										url: '/' + $("title").html() + '?setValueAditionalFile&tableNumber=' + idTable + '&id=r' + currentIdRow + '_id' + '&value=' + $("#" + idRow + "_id").html(),
										success: function(data4) {
											$.ajax({
												url: '/' + $("title").html() + '?setValueAditionalFile&tableNumber=' + idTable + '&id=r' + currentIdRow + '_desc' + '&value=' + $("#" + idRow + "_id").attr("title"),
												// Save id and desc in html
												success: function(data5) {
													$("#" + idTable).append(newRow);

													var newRowExp = currentRowExp.clone();
													newRowExp.attr("id", idTable + "_r" + currentIdRow + "_exp");
													newRowExp.css("display", "none");

													var ulTag = $(newRowExp.find("ul.mktree"));
													ulTag.attr("id", idTable + "_tree" + currentIdRow);
													var ulInputChildren = ulTag.children("input");
													$(ulInputChildren.get(0)).attr("id", idTable + "_r" + currentIdRow + "_id_new");
													$(ulInputChildren.get(1)).attr("id", idTable + "_r" + currentIdRow + "_desc_new");

													//Ponemos los onclick correspondientes en los elementos "span" y las clases correspondientes en los "li"
													var currentColumn = 1; // para que funcione usar treenode en jquery
													ulTag.find("li").each(function(i){
														if ($(this).find("ul").length == 0) { //no tiene sublistas, es un elemento hoja
															this.className = nodeBulletClass; 
															this.firstElementChild.onclick = retFalse;
															this.children[1].id = idTable + "_r" + currentIdRow + "c" + currentColumn + "_new"; //cambia el id del elemento "input"
															currentColumn++;
														} else { //tiene sublistas
															this.className = nodeClosedClass;
															this.firstElementChild.onclick = treeNodeOnclick;
														}
													});

													newRowExp.find(".save_values").click(function(evento){
														var trParent = $(this).parents("tr");
														save_row(trParent.prev().attr("id")); //Se coge la fila anterior que no es la expandida
													});
													
													$("#" + idTable).append(newRowExp);
												},
											  	error: function(data) {
											  		alert("Problem saving new description.");
											  	}
											});			
										},
									  	error: function(data) {
									  		alert("Problem saving new id.");
									  	}
									});									
								}, 
								error: function(data) {
							  		alert("Problem creating new row.");
							  	}
							});
					  	},
					  	error: function(data) {
					  		alert("Problem creating new row.");
					  	}
					});	
		  	},
		  	error: function(data) {
		  		alert("Problem creating new row.");
		  	}
	});
}


function rowCollapsible(numRow) {
	if (numRow%2 == 0)
		return false;
	else 
		return true;
}		
			

function numColsShowed (colsShowed) {
	var res = 0;
	for (var i=0; i<colsShowed.length; i++) {
		if (colsShowed[i])
			res++;
	}
	return res;
}


/*function show_hide_column(idTable, col_no, do_show) {
	$.ajax({
		  url: '/' + $("title").html() + '?getValueAditionalFile&tableNumber='+idTable+'&id=colsShowed',
		  success: function(data) {
				var colsShowed = eval('(' + data + ')');
				if(do_show == undefined) do_show = false; 
				colsShowed[col_no-2] = do_show;
				var colsShowedStr = '[' + colsShowed.join(", ") + ']';
				$.ajax({
					  url: '/' + $("title").html() + '?setValueAditionalFile&tableNumber='+idTable+'&id=colsShowed&value='+encodeURI(colsShowedStr),
					  success: function(data2) {
						var numCols = numColsShowed(colsShowed);
						var stl;
						if (do_show) 
							stl = 'table-cell';
						else
							stl = 'none';
						var tbl  = $("#" + idTable);
						var cels;
						tbl.find("tr").each(function(i){
							cels = $(this).find("td");
							if (rowCollapsible(i)) {
								$(cels[col_no]).css("display", stl);
							} else {
								$(cels[1]).attr("colspan", "" + (numCols+1) + ""); // Cambiar a (numCols + 1)
							}
						});
					}
				});	
		}
	});
}*/


function save_row_0(idTable){
	$.ajax({
		  url: '/' + $("title").html() + '?getValueAditionalFile&tableNumber='+idTable+'&id=colsShowed',
		  success: function(data) {
			  var colsShowed = eval('(' + data + ')');
			  var numCols = $("#" + idTable + "_r0").children("td").length;
			  for (i=0; i<numCols-3; i++) {
				  var do_show = $("#" + idTable + "_show_c" + (i+1)).attr("checked");
				  if(do_show == undefined) do_show = false; 
				  colsShowed[i] = do_show;
			  }
			  //Save in adf.txt
			  var colsShowedStr = '[' + colsShowed.join(", ") + ']';
			  $.ajax({
				  url: '/' + $("title").html() + '?setValueAditionalFile&tableNumber='+idTable+'&id=colsShowed&value='+encodeURI(colsShowedStr),
				  success: function(data2) {
					  var nColsShowed = numColsShowed(colsShowed);
					  for (i=0; i<numCols-3; i++) {
						  var stl;
						  if (colsShowed[i]) 
							  stl = 'table-cell';
						  else
							  stl = 'none';
						  $("#" + idTable + " tr").each(function(j){
							  //cels = $(this).find("td");
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
		  },
		  error: function(data) {
			  alert("Problem showing/hiding columns.");
		  } 
	});
}

			
function save_row(idRow){
	var numCols = $("#" + idRow).children("td").length;
	var numParamCols = numCols - 3;
	var cell, newCell;
	//Save row in content.txt
	//var contentLineTxtOld = "|";
	var contentLineTxtNew = "|";
	for (var i=1; i<=numParamCols; i++) {
		//cell = $("#" + idRow + "c" + i);
		newCell = $("#" + idRow + "c" + i + "_new");
		//contentLineTxtOld = contentLineTxtOld + cell.html() + "|";
		contentLineTxtNew = contentLineTxtNew + newCell.attr("value") + "|";
	}	
	//contentLineTxtOld = contentLineTxtOld.replace(/&nbsp;/g, "");
	//contentLineTxtNew = contentLineTxtNew.replace(/&nbsp;/g, "");
	var idRowParts = idRow.split("_");
	$.ajax({
		//setLineContentFile&tableNumber=t1&fileNumber=r3&new_line=minuevalinea
		//url: '/' + $("title").html() + '?setLineContentFile&old_line=' + encodeURI(contentLineTxtOld) + '&new_line=' + encodeURI(contentLineTxtNew),
		url: '/' + $("title").html() + '?setLineContentFile&tableNumber=' + idRowParts[0] + '&fileNumber=' + idRowParts[1] + '&new_line=' + encodeURI(contentLineTxtNew),
		success: function(data) {
			// Save row in html
			for (var i=1; i<=numParamCols; i++) {
				cell = $("#" + idRow + "c" + i);
				newCell = $("#" + idRow + "c" + i + "_new");
				cell.html(newCell.attr("value"));
			}	
		},
	  	error: function(data) {
	  		alert("Problem saving cell values.");
	  	}
	});

	// Save id and description in add.txt
	var newId = $("#" + idRow + "_id_new").attr("value");
	var newDesc = $("#" + idRow + "_desc_new").attr("value");
	//var idRowParts = idRow.split("_");
	$.ajax({
		url: '/' + $("title").html() + '?setValueAditionalFile&tableNumber=' + idRowParts[0] + '&id=' + idRowParts[1] + '_id' + '&value=' + newId,
		success: function(data) {
			$.ajax({
				url: '/' + $("title").html() + '?setValueAditionalFile&tableNumber=' + idRowParts[0] + '&id=' + idRowParts[1] + '_desc' + '&value=' + newDesc,
				// Save id and desc in html
				success: function(data2) {
					var idRowCell = $("#" + idRow + "_id");
					idRowCell.html(newId);
					idRowCell.attr("title", newDesc);
				},
			  	error: function(data) {
			  		alert("Problem saving new description.");
			  	}
			});			
		},
	  	error: function(data) {
	  		alert("Problem saving new id.");
	  	}
	});
}			
