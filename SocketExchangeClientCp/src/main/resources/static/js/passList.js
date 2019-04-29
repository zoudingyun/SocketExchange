function uploadPass(){
	var datanow = hot.getData();
	$.ajax( {
				type: "POST",
				contentType: "application/json",
				url:baseAjaxURL + "/uploadPass",
				data: JSON.stringify(datanow),
				success:function(response) {
					var a=0;
				}
			}
		);
}

function formatPassList(data){
	var re = [];
	for(var i=0;i<data.length;i++){
		re[i] = data[i][0];
	}
}