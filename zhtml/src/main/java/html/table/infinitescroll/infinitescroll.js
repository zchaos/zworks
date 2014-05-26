var datas = [];
var datacount = 100000;
var FIELDS = ["F1", "F2"];
var fieldlen = FIELDS.length;
for (var i = 0; i < datacount; i++) {
	var obj = {};
	for (var j = 0; j < fieldlen; j++) {
		var field = FIELDS[j];
		obj[field] = i + "-" + field;
	}
	datas.push(obj);
}
var doc = document;
var gridview = getElementsByClassName("grid-view")[0];
var gridho = getElementsByClassName("grid-height-overflow")[0];
var gridtable = getElementsByClassName("grid-view-table")[0];
var gridbody = gridtable.tBodies[0];

// 当前起始数据行
var dataindex = 0;
// 每页显示50行数据
var pagedatanum = 50;

var els = null;
function getElementsByClassName(className) {
	// if (doc.getElementsByClassName) {
	// return doc.getElementsByClassName(className);
	// }
	if (!els) {
		els = doc.getElementsByTagName("*");
	}
	var rs = [];
	var len = els.length;
	for (var i = 0; i < len; i++) {
		var el = els[i];
		var cName = el.className;
		if (!cName) {
			continue;
		}
		if ((" " + cName + " ").indexOf(" " + className + " ") >= 0) {
			rs.push(el);
		}
	}
	return rs;
}

function createRowsHtml(datas, dataindex, datanum) {
	var rs = [];
	for (var i = 0; i < datanum; i++) {
		var data = datas[dataindex + i];
		rs.push('<tr class="grid-body-row" data-index="', i, '">');
		for (var j = 0; j < fieldlen; j++) {
			var field = FIELDS[j];
			rs.push('<td class="grid-body-col">', '<div class="grid-body-content">', data[field], '</div>', '</td>');
		}
		rs.push('</tr>');
	}
	return rs.join("");
}

function createRows(datas, dataindex, datanum) {
	var tbody = doc.createElement("tbody");

	var html = createRowsHtml(datas, dataindex, datanum);
	tbody.innerHTML = html;
	var rows = tbody.childNodes;

	var frag = doc.createDocumentFragment();

	var rs = [];
	var len = rows.length;
	for (var i = 0; i < len; i++) {
		rs.push(rows[i]);
	}
	for (var i = 0; i < len; i++) {
		frag.appendChild(rs[i]);
	}
	return frag;
}

function createRows2(datas, dataindex, datanum) {//ie6上要用这种方法
	var div = doc.createElement("div");

	var html = createRowsHtml(datas, dataindex, datanum);
	div.innerHTML = "<table><tbody>" + html + "</tbody></table>";

	var tbody = div.firstChild.tBodies[0];

	var rows = tbody.childNodes;
	var frag = doc.createDocumentFragment();

	var rs = [];
	var len = rows.length;
	for (var i = 0; i < len; i++) {
		rs.push(rows[i]);
	}
	for (var i = 0; i < len; i++) {
		frag.appendChild(rs[i]);
	}
	return frag;
}

var rows = createRows(datas, dataindex, pagedatanum);
gridbody.appendChild(rows);

var gridrows = gridbody.rows;
var rowlength = gridrows.length;
var rowheight = gridbody.childNodes[0].offsetHeight;
var hotop = datacount * rowheight;
gridho.style.top = hotop + "px";
//当前滚动条滚动到的位置
var scrollPosition = 0;
//如果可以显示的行小于此值，则调整表格的行
var numFromEdge = 8;
var buffersize = 16;

function handlerScrollView(event) {
	//滚动的方向，如果为1，则是向下滚动，为-1则是向上滚动
	var scrollTop = gridview.scrollTop;
	var scrollDirection = scrollTop - scrollPosition > 0 ? 1 : -1;
	scrollPosition = scrollTop;

	var requestStart;
	var requestEnd;
	if (scrollDirection === 1) {//向下滚动
		var dataIndex = getLastDataIndex();
		if (dataIndex < datacount - 1) {
			var lastVisibleDataIndex = getLastVisibleIndex();
			if (dataIndex - lastVisibleDataIndex < numFromEdge) {
				requestStart = getFirstVisibleIndex() - buffersize;
			}
		}
	} else {

	}
	if (requestStart != null) {
		var firstDataIndex = getFirstDataIndex();
		if (requestStart != firstDataIndex) {
			requestEnd = Math.min(requestStart + pagedatanum - 1, datacount - 1);

			renderRange(requestStart, requestEnd);
		}
	}
}

function renderRange(requestStart, requestEnd) {
	var firstDataIndex = getFirstDataIndex();
	var lastDataIndex = getLastDataIndex();
	if (requestStart > firstDataIndex) {
		var num = requestEnd - lastDataIndex;

		//num < pagedatanum 滚动行数比较少
		//requestEnd == datacount - 1 已经滚动到了底部
		if (num < pagedatanum) {
			var rs = [];
			for (var i = 0; i < num; i++) {
				rs.push(gridrows[i]);
			}

			for (var i = 0; i < num; i++) {
				gridbody.appendChild(rs[i]);
			}

			for (var i = 0; i < num; i++) {
				var row = rs[i];
				setDataIndex(row, lastDataIndex + i + 1);
				renderRow(row);
			}
		} else {//一次滚动太多，全部刷新
			var start = requestEnd - rowlength;
			for (var i = 0; i < rowlength; i++) {
				var row = gridrows[i];
				setDataIndex(row, start + i);
				renderRow(row);
			}
		}
		gridtable.style.top = (requestStart * rowheight) + "px";
	} else {

	}
}

function renderRow(row) {
	var dataIndex = getDataIndex(row);
	var data = datas[dataIndex];
	var cells = row.cells;
	for (var i = 0; i < fieldlen; i++) {
		var cell = cells[i];
		var div = cell.firstChild;
		div.innerHTML = data[FIELDS[i]];
	}
}

function getFirstVisibleIndex() {
	var position = gridview.scrollTop;
	return getVisibleIndex(position);
}

function getLastVisibleIndex() {
	var position = gridview.scrollTop + gridview.clientHeight;
	return getVisibleIndex(position);
}

function getVisibleIndex(position) {
	return Math.floor(position / rowheight);
}

function getDataIndex(row) {
	return parseInt(row.getAttribute("data-index"));
}

function setDataIndex(row, dataIndex) {
	return row.setAttribute("data-index", dataIndex);
}

function getFirstDataIndex() {
	return getDataIndex(gridrows[0]);
}

function getLastDataIndex() {
	return getDataIndex(gridrows[rowlength - 1]);
}

