 var ws = new ReconnectingWebSocket('ws://127.0.0.1:234/client/test1');
 
 ws.onopen = function() {
     console.log('open');
 };

 ws.onmessage = function(e) {
     console.log('message', e.data);
	 var data = JSON.parse(e.data)
	 if(data.type=='clientInfo'){
		 var timeStr =  document.getElementById("timeStr");
		 timeStr.innerHTML=data.time;
		 var state =  document.getElementById("serverState");
		 if (data.data.online) {
		 	state.innerHTML="ONLINE";
			state.style="color:#5FB878;";
			state =  document.getElementById("serverButtonOn");
			state.style="display:none;margin:0px;";
			state =  document.getElementById("serverButtonIng");
			state.style="display:none;margin:0px;";
			state =  document.getElementById("serverButtonOff");
			state.style="display:block;margin:0px;";
		 } else{
		 	state.innerHTML="OFFLINE";
			state.style="color:#FF5722";
			state =  document.getElementById("serverButtonOff");
			state.style="display:none;margin:0px;";
			state =  document.getElementById("serverButtonIng");
			state.style="display:none;margin:0px;";
			state =  document.getElementById("serverButtonOn");
			state.style="display:block;margin:0px;";
		 }
		 state =  document.getElementById("onlinePassCount");
		 state.innerHTML=data.data.passListCount;
		 state =  document.getElementById("workerMaximumPoolSize");
		 state.innerHTML=data.data.workerMaximumPoolSize;
		 state =  document.getElementById("workerActiveThreadCount");
		 state.innerHTML=data.data.workerActiveThreadCount;
		 state =  document.getElementById("serverMaximumPoolSize");
		 state.innerHTML=data.data.serverMaximumPoolSize;
		 state =  document.getElementById("serverActiveThreadCount");
		 state.innerHTML=data.data.serverActiveThreadCount;
		 
	 }
 };

 ws.onclose = function() {
     console.log('close');
 };


function startServer(){
	var state =  document.getElementById("serverButtonIng");
	state.style="display:block;margin:0px;";
	state =  document.getElementById("serverButtonOn");
	state.style="display:none;margin:0px;";
	
	$.ajax( {
				type: "POST",
				contentType: "application/json",
				url:baseAjaxURL + "/startServer",
				data: "",//JSON.stringify(getQueryCondition()),
				success:function(response) {
				},
				dataSrc:function(data)
				{
					return data.data;
				}
			}
		);
	
}

function offServer(){
	var state =  document.getElementById("serverButtonIng");
	state.style="display:block;margin:0px;";
	state =  document.getElementById("serverButtonOff");
	state.style="display:none;margin:0px;";
	
	$.ajax( {
				type: "POST",
				contentType: "application/json",
				url:baseAjaxURL + "/offServer",
				data: "",//JSON.stringify(getQueryCondition()),
				success:function(response) {
				},
				dataSrc:function(data)
				{
					return data.data;
				}
			}
		);
}

function goUrl(url){
	window.location.href=url;
}

