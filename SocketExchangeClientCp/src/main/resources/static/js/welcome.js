/* (document).ready(function() {
	
	
	
	
} */
var baseAjaxURL = 'http://127.0.0.1:234'


function startServer(){
	$.ajax( {
				type: "POST",
				contentType: "application/json",
				url:baseAjaxURL + "/startServer",
				data: "",//JSON.stringify(getQueryCondition()),
				success:function(response) {
					var a=0;
				},
				dataSrc:function(data)
				{
					return data.data;
				}
			}
		);
}