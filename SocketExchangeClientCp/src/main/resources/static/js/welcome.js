/* (document).ready(function() {
	
	
	
	
} */


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
					var a=0;
					state =  document.getElementById("serverState");
					state.innerHTML="ONLINE&nbsp&nbsp";
					state.style="color:#5FB878;";
					state =  document.getElementById("serverButtonIng");
					state.style="display:none;margin:0px;";
					state =  document.getElementById("serverButtonOff");
					state.style="display:block;margin:0px;";
				},
				error:function(){
					state =  document.getElementById("serverButtonIng");
					state.style="display:none;margin:0px;";
					state =  document.getElementById("serverButtonOn");
					state.style="display:block;margin:0px;";
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
					var a=0;
					var state =  document.getElementById("serverState");
					state.innerHTML="OFFLINE";
					state.style="color:#FF5722";
					state =  document.getElementById("serverButtonIng");
					state.style="display:none;margin:0px;";
					state =  document.getElementById("serverButtonOn");
					state.style="display:block;margin:0px;";
				},
				error:function(){
					state =  document.getElementById("serverButtonIng");
					state.style="display:none;margin:0px;";
					state =  document.getElementById("serverButtonOff");
					state.style="display:block;margin:0px;";
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

