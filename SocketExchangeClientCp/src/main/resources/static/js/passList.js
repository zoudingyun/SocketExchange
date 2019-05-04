var AllData = [];
var layer;

function uploadPass(){
	var datanow = hot.getData();
	if(datanow.length>0){
		$.ajax( {
					type: "POST",
					contentType: "application/json",
					url:baseAjaxURL + "/uploadPass",
					data: JSON.stringify(formatPassList(datanow)),
					success:function(response) {
						if ( response.message== 'SUCCESS') {
							layer.msg('更新成功！');
						} else{
							layer.msg('更新失败：'+response.message);
							console.log(response.message);
						}
						 
					}
				}
			);
	}
	
}

function formatPassList(data){
	var re = [];
	for(var i=0;i<data.length;i++){
		var tmp = {agentAdd:null,agentPort:null,remoteAdd:null, remotePort:null, type:null};
		 tmp.agentAdd = data[i][0];
		 tmp.agentPort = data[i][1];
		 tmp.remoteAdd = data[i][2];
		 tmp.remotePort = data[i][3];
		 tmp.type = data[i][4];
		 re.push(tmp);
	}
	return re;
}


$(document).ready(function() {
	layui.use('layer', function(){
	  layer = layui.layer;
	}); 
	$.ajax( {
				type: "POST",
				contentType: "application/json",
				url:baseAjaxURL + "/queryPass",
				success:function(response) {
					var tmp = makeReportData(response.data);
					if(tmp.length>0){
						hot.loadData(tmp);
					}else{
						var nullData = [];
						hot.loadData(nullData);
						nullData = [
									{agentAdd:null,agentPort:null,remoteAdd:null, remotePort:null, type:null}
									];
						hot.loadData(nullData);
						return false;
					}
				}
			}
		);
		})
		
//查询参数封装
function makeReportData(dats) {
	if(dats.length<0){
		return null;
	}
	var ownNum = 0;
	var otherNum = 0;
	var params = [];
	for(var i=0;i<dats.length;i++)
	{
		var param={agentAdd:dats[i].agentAdd, agentPort:dats[i].agentPort, remoteAdd:dats[i].remoteAdd, remotePort:dats[i].remotePort, type:dats[i].type};
		params.push(param);	
		var tmpparam={agentAdd:dats[i].agentAdd, agentPort:dats[i].agentPort, remoteAdd:dats[i].remoteAdd, remotePort:dats[i].remotePort, type:dats[i].type};
		AllData.push(tmpparam);		
	}
	return params;
}

function goUrl(url){
	window.location.href=url;
}