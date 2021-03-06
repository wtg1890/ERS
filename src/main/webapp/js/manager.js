window.onload = function() {
	populateAllCurrentRequests();
}


function populateAllCurrentRequests() {
	var xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 && xhr.status == 200) {
			var ajaxObject = JSON.parse(xhr.responseText);
			console.log(ajaxObject);
			addRows(ajaxObject);
		}
	};
	xhr.open("POST", "http://ec2-52-90-166-210.compute-1.amazonaws.com:8080/ERS/populateManagerPendingRequestTable.ajax");
	xhr.send();
};


function addRows(ajaxObject) {
	for (var i = 0; i < ajaxObject.length; i++) {
		var tr = document.createElement("tr");
		var tdRequestId = document.createElement("td");
		var tdRequestAmount = document.createElement("td");
		var tdRequestReason = document.createElement("td");
		var tdTimeCreated = document.createElement("td");
		
		tdRequestId.textContent = ajaxObject[i].requestId;
		tdRequestAmount.textContent = ajaxObject[i].requestAmount;
		tdRequestReason.textContent = ajaxObject[i].requestReason;
		tdTimeCreated.textContent = ajaxObject[i].timeRequestWasCreated;
		
		tr.appendChild(tdRequestId);
		tr.appendChild(tdRequestAmount);
		tr.appendChild(tdRequestReason);
		tr.appendChild(tdTimeCreated);
		
		document.getElementById("tblCurrentPendingRequests").appendChild(tr);
	}
};



