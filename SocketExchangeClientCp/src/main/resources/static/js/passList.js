function uploadPass(){
	var datanow = hot.getData();
	if(datanow.length>0){
		$.ajax( {
					type: "POST",
					contentType: "application/json",
					url:baseAjaxURL + "/uploadPass",
					data: JSON.stringify(formatPassList(datanow)),
					success:function(response) {
						var a=0;
					}
				}
			);
	}
	
}

function formatPassList(data){
	var re = [];
	for(var i=0;i<data.length;i++){
		var tmp = {agent:null,remoteAdd:null, remotePort:null, type:null};
		 tmp.agent = data[i][0]+':'+data[i][1];
		 tmp.remoteAdd = data[i][2];
		 tmp.remotePort = data[i][3];
		 tmp.type = data[i][4];
		 re.push(tmp);
	}
	return re;
}