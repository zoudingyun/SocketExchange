function appClose(){
	$.ajax( {
				type: "POST",
				contentType: "application/json",
				url:baseAjaxURL + "/exit"
			}
		);
}