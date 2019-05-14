 var ws = new ReconnectingWebSocket(baseAjaxURLWs+'client/test2');
 var consoles = new Array();
 var maxConsoles = 100;


 document.getElementById("console").value="";
 
 ws.onopen = function() {
     console.log('open');
 };

 ws.onmessage = function(e) {
     //console.log('message', e.data);
	 var data = JSON.parse(e.data)
	 if(data.type=='clientInfo'){
		 var timeStr =  document.getElementById("timeStr");
		 timeStr.innerHTML=data.time;
		 var state =  document.getElementById("serverState");
		 if (data.data.online) {
		 	state.innerHTML="ONLINE";
			state.style.color="#5FB878";
			state =  document.getElementById("serverButtonOn");
			state.style.display="none";
			state.style.margin="0px";
			state =  document.getElementById("serverButtonIng");
			state.style.display="none";
			state.style.margin="0px";
			state =  document.getElementById("serverButtonOff");
			state.style.display="block";
			state.style.margin="0px";
		 } else{
		 	state.innerHTML="OFFLINE";
			state.style.color="#FF5722";
			state =  document.getElementById("serverButtonOff");
			state.style.display="none";
			state.style.margin="0px";
			state =  document.getElementById("serverButtonIng");
			state.style.display="none";
			state.style.margin="0px";
			state =  document.getElementById("serverButtonOn");
			state.style.display="block";
			state.style.margin="0px";
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
		 
	 }else if(data.type=='CONSOLE'){
		 var mes = formatConsole(data.message)
		 
		 //自动清理过长的日志
		 if(consoles.length>=maxConsoles){
			 for(var i=0;i<consoles.length-1;i++){
				 consoles[i]=consoles[i+1];
			 }
			 consoles[consoles.length-1]=mes;
		 }else{
			consoles.push(mes);
		 }
		 
		 var consoleStr = '';
		 for(var i=0;i<consoles.length;i++){
			 consoleStr+=consoles[i];
		 }
		 
		 document.getElementById("console").innerHTML = consoleStr;
		 //自动滚到最底部
		 document.getElementById("console").scrollTop = document.getElementById("console").scrollHeight;
	 }
 };

 ws.onclose = function() {
     console.log('close');
 };
 
 //控制台日志颜色处理
 function formatConsole(message){
	 var tmp = message.split(' ');
	 var mes = "";
	 if(tmp.length>=7){
		 if(tmp[2]=="ERROR"){
		 	mes += "<span style='color:#9A9A9A;'>"+tmp[0] +" </span> ";
		 	mes += "<span style='color:#9A9A9A;'>"+tmp[1] +" </span> ";
		 	mes += "<span style='color:#FF6B68;'>"+tmp[2] +" </span>";
		 	mes += "<span style='color:#7F688A;'>"+tmp[3] +" </span>";
		 	mes += "<span>"+tmp[4] +" </span> ";
		 	mes += "<span>"+tmp[5] +" </span> ";
		 	mes += "<span style='color:#288B8B;'>"+tmp[6] +" </span>";
			mes += "<span>";
		 	for(var i = 7;i<tmp.length;i++){
		 		mes += tmp[i]+" ";
		 	}
			mes += " </span>";
		 	mes+="<br>";
		 }else if(tmp[3]=="INFO"&&tmp.length>=8){
	 		mes += "<span style='color:#9A9A9A;'>"+tmp[0] +" </span> ";
			mes += "<span style='color:#9A9A9A;'>"+tmp[1] +" </span> ";
			mes += "<span style='color:#A8C023;'>"+tmp[3] +" </span>";
			mes += "<span style='color:#7F688A;'>"+tmp[4] +" </span>";
			mes += "<span>"+tmp[5] +" </span> ";
			mes += "<span>"+tmp[6] +" </span> ";
			mes += "<span style='color:#288B8B;'>"+tmp[7] +" </span>";
			mes += "<span>";
			for(var i = 8;i<tmp.length;i++){
				mes += tmp[i]+" ";
			}
			mes += " </span>";
			mes+="<br>";
	 	}else if(tmp[3]=="WARN"&&tmp.length>=8){
	 		mes += "<span style='color:#9A9A9A;'>"+tmp[0] +" </span> ";
	 		mes += "<span style='color:#9A9A9A;'>"+tmp[1] +" </span> ";
	 		mes += "<span style='color:#FF6B68;'>"+tmp[3] +" </span>";
	 		mes += "<span style='color:#7F688A;'>"+tmp[4] +" </span>";
	 		mes += "<span>"+tmp[5] +" </span> ";
	 		mes += "<span>"+tmp[6] +" </span> ";
	 		mes += "<span style='color:#288B8B;'>"+tmp[7] +" </span>";
			mes += "<span>";
	 		for(var i = 8;i<tmp.length;i++){
	 			mes += tmp[i]+" ";
	 		}
			mes += " </span>";
	 		mes+="<br>";
	 	}else{
	 		mes+=message+"<br>";
	 	}
		return mes;
	 }
	 else{
		 mes+=message+"<br>";
		 return mes;
	 }
 }


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

