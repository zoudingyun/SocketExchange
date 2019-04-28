var baseAjaxURL = ajaxUtils.baseAjaxURL;
var nowCenterID = "";
var AllData = [];
var ajaxData = [];
var companys = [];
var centersList = [];
var userinfo;
var report;
var gfrl = 1;
var fdrl = 1;


//查询参数封装
function getQueryCondition() {
	var params={'username':''};
	userId = getUrlParam("userId");
	params.username = userId;

	return params;
}

//查询参数封装(报表查询)
function getQueryConditionReport(id,date) {
	var params={'centerid':'','dataTime':''};
	params.centerid = id;
	params.dataTime = date;
	return params;
}

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
		var gsval = "";
		var num = 0;
		if(dats[i].own == true)
		{
			gsval = "本单位场站";
			ownNum++;
			num = ownNum;
		}
		else
		{
			gsval = "外单位场站";
			otherNum++;
			num = otherNum;
		}
		var param={gs:gsval, xh:num, company:dats[i].companyname, name:dats[i].stationName, bzname:dats[i].zhtjname, zjrl:dats[i].zjrl, type:dats[i].stationtype, isJr:dats[i].isjr, jrType:dats[i].jrfs, desc:dats[i].wtqkdesc, byWork:dats[i].thismonwork, xyWork:dats[i].nextmonwork};
		params.push(param);	
		var tmpparam={gs:gsval, xh:num, company:dats[i].companyname, name:dats[i].stationName, bzname:dats[i].zhtjname, zjrl:dats[i].zjrl, type:dats[i].stationtype, isJr:dats[i].isjr, jrType:dats[i].jrfs, desc:dats[i].wtqkdesc, byWork:dats[i].thismonwork, xyWork:dats[i].nextmonwork};
		AllData.push(tmpparam);		
	}
	return params;
}

//合并通单位头
function sumCell(table){
	var ownStart = -1;
	var ownEnd = -1;
	var otherStart = -1;
	var otherEnd = -1;
	for(var i=0;i<ajaxData.length;i++){
		if(ajaxData[i].own){
			if(ownStart<0){
				ownStart = i;
			}
		}else
		{
			if(ownStart>=0){
				ownEnd = i-1;
				break;
			}
		}
	}
	
	for(var i=0;i<ajaxData.length;i++){
		if(!ajaxData[i].own){
			if(otherStart<0){
				otherStart = i;
			}
		}else
		{
			if(otherStart>=0){
				otherEnd = i-1;
			}
		}
	}
	if(ownStart>=0&&ownEnd<0){
		ownEnd = ajaxData.length-1;
	}
	if(otherStart>=0&&otherEnd<0){
		otherEnd = ajaxData.length-1;
	}
	
	table.updateSettings({
		mergeCells: [
						{row:0+ownStart, col: 0, rowspan:1+ownEnd-ownStart, colspan: 1	},
						{row:0+otherStart, col: 0, rowspan:1+otherEnd -otherStart, colspan: 1	}
					]
	});
}

function query(){
	queryReport($("#selectCompany").find("option:selected").val(),getDate());
}

$(document).ready(function() {
	//queryType = $("#type").val();
	userId = getUrlParam("userId");
	var currentBtn = document.getElementById("jtveiw");
	
	//时间控件
	laydate.render({
	elem: '#test1' //指定元素
	,type: 'month'
	,value: new Date()
	,max:  getNowDate()
	});
	
	
	$.ajax( {
				type: "POST",
				contentType: "application/json",
				url:baseAjaxURL + "/nnrg/centerOnlineState/centersList?dataTime="+getDate(),
				data: JSON.stringify(getQueryCondition()),
				success:function(response) {
					if(response.data.length>0){
						if(response.data[0].isJTUser == "true")
						currentBtn.style.display = "block";
						else
						currentBtn.style.display = "none";
					}
					
					for(var i=0;i<response.data.length;i++){
						$("#selectCompany").prepend("<option value='"+response.data[i].centerid+"'>"+response.data[i].centername+"</option>");
					}
					$("#selectCompany").selectpicker('refresh');
					//var checkText=;
					queryReport($("#selectCompany").find("option:selected").val(),getDate());
					queryCompanyList();
					centersList = response.data;
					//alert($("#selectCompany").options[index].value);
				},
				dataSrc:function(data)
				{
					return data.data;
				}
			}
		);
});

 function getDate(){
				if($("#test1").val() == ""){
					var myDate = new Date();
					return myDate.getFullYear()+'-'+(myDate.getMonth()+1);
				}else{
					return $("#test1").val(); //得到日期生成的值，如：2017-08-18
				}
	  }

function getNowDate(){
	var myDate = new Date();
	return myDate.getFullYear()+'-'+myDate.getMonth(); 
}

function queryReport(centerid,date){
	$.ajax( {
				type: "POST",
				contentType: "application/json",
				url:baseAjaxURL + "/nnrg/centerOnlineState/onlinereport?dataTime="+getDate(),
				data: JSON.stringify(getQueryConditionReport(centerid,date)),
				success:function(response) {
					nowCenterID = centerid;
					ajaxData = response.data;
					AllData = [];
					var tmp = makeReportData(response.data);
					if(tmp.length>0){
						hot.loadData(tmp);
						var tmpdatada = hot.getData();
						flashValue(ajaxData);
					}else{
						//hot.loadData(tmp);
						var nullData = [];
						hot.loadData(nullData);
						nullData = [
									{gs:null, xh:null, company:null, name:null, bzname:null, zjrl:null, type:null, isJr:null, jrType:null, desc:null, byWork:null, xyWork:null}
									];
						hot.loadData(nullData);
						flashValue(ajaxData);
						return false;
					}
					
					sumCell(hot);
				}
			}
		);
}

function queryCompanyList(){
	$.ajax( {
				type: "POST",
				contentType: "application/json",
				url:baseAjaxURL + "/nnrg/centerOnlineState/companylist",
				success:function(response) {
					companys = response.data;
					var companyNames = [];
					for(var i=0;i<companys.length;i++){
						var companyName = companys[i].companyname;
						companyNames.push(companyName);
					}
					hot.updateSettings({
					columns: [	{
									data: 'gs',
									readOnly: true,
								wordWrap: true,
								colWidths: '60px',
								className:'htMiddle htCenter'
								},
								{
									data: 'xh',
									readOnly: true,
								wordWrap: true,
								colWidths: '55px',
								className:'htMiddle htCenter'
								},
								{
									data: 'company',
									editor: 'select',
									selectOptions: companyNames,
									className:'htMiddle htCenter'
								},
								{
									data: 'name',
								wordWrap: true,
								colWidths: '150px',
								className:'htMiddle htCenter'
								}
								,
								{
								data: 'bzname',
								wordWrap: true,
								colWidths: '150px',
								className:'htMiddle htCenter'
								}
								,
								{
								data: 'zjrl',
								type: 'numeric',
								className:'htMiddle htCenter'
								}
								,
								{
								data: 'type',
								editor: 'select',
								selectOptions: ['陆上风电', '海上风电', '集中式光伏', '分布式光伏'],
								className:'htMiddle htCenter'
								}
								,
								{
								data: 'isJr',
								editor: 'select',
								selectOptions: ['已接入', '未接入'],
								wordWrap: false,
								colWidths: '70px',
								className:'htMiddle htCenter'
								}
								,
								{
								data: 'jrType',
								wordWrap: true,
								colWidths: '85px',
								className:'htMiddle htCenter'
								}
								,
								{
								data: 'desc',
								wordWrap: true,
								colWidths: '200px'
								}
								,
								{
								data: 'byWork',
								wordWrap: true,
								colWidths: '200px'
								}
								,
								{
								data: 'xyWork',
								wordWrap: true,
								colWidths: '200px'
								}
								]
					});
				}
			}
		);
}

function save(){
	saveReport();
	if(report == 0 || report==-1){
		saveUserInfo();
		if(userinfo==report&&report == -1){
			modalJs({
				icon: -1,
				title: "保存失败：",
				content: "<p>没有修改任何记录！</p>"
			});
		}else if(userinfo==0&&report == -1){
			modalJs({
				icon: -1,
				title: "保存成功：",
				content: "<p>填报用户信息更新成功！</p><br/>"
			});
			queryReport(nowCenterID);
		}else if(userinfo==report&&report == 0){
			modalJs({
				icon: -1,
				title: "保存成功：",
				content: "<p>接入状态填报更新成功！</p><br/>"
						+"<p>填报用户信息更新成功！</p><br/>"
			});
			queryReport(nowCenterID);
		}else if(userinfo==-1&&report == 0){
			modalJs({
				icon: -1,
				title: "保存成功：",
				content: "<p>接入状态填报更新成功！</p><br/>"
			});
			queryReport(nowCenterID);
		}
	}else{
		modalJs({
			icon: -1,
			title: "保存失败：",
			content: report
		});
	}
	
	
}

function saveUserInfo(){
	var tmpInfo;
	for(var i=0;i<centersList.length;i++){
		if(nowCenterID == centersList[i].centerid){
			tmpInfo = centersList[i];
			break;
		}
	}
	if(tmpInfo.inputuser==$("#inputuser").val()
	&&tmpInfo.inputuserPhone==$("#inputuserPhone").val()
	&&tmpInfo.checkuser==$("#checkuser").val()
	&&tmpInfo.checkuserPhone==$("#checkuserPhone").val()
	&&tmpInfo.companyuser==$("#companyuser").val()
	&&tmpInfo.companyuserPhone==$("#companyuserPhone").val()
	&&tmpInfo.remark==$("#remark").val()
	){
		userinfo = -1;
		return -1;
	}else{
		var tmpdata = {
		centerid:nowCenterID
		,inputuser:$("#inputuser").val()
		,inputuserPhone:$("#inputuserPhone").val()
		,checkuser:$("#checkuser").val()
		,checkuserPhone:$("#checkuserPhone").val()
		,companyuser:$("#companyuser").val()
		,companyuserPhone:$("#companyuserPhone").val()
		,remark:$("#remark").val()
		}
		$.ajax( {
					type: "POST",
					contentType: "application/json",
					async: false,
					url:baseAjaxURL + "/nnrg/centerOnlineState/uploadUserInfo?dataTime="+getDate(),
					data: JSON.stringify(tmpdata),
					success:function(response) {
						 userinfo = 0;
						 return 0;
					}
				}
			);
	}
}

function saveReport(){
	saveUserInfo();
	var datanow = hot.getData();
	var changed = 0;
	for(var a=0;a<datanow.length;a++){
		if(datanow[a][0] ==null)
		{
			if(datanow[a][0] ==null &&datanow[a][2] ==null&&datanow[a][3] ==null&&datanow[a][4] ==null&&datanow[a][5] ==null&&datanow[a][6] ==null&&datanow[a][7] ==null&&datanow[a][8] ==null&&datanow[a][9] ==null&&datanow[a][10] ==null&&datanow[a][11] ==null){
				continue;
			}else{
				if(datanow[a][2] !=null&&datanow[a][3] !=null&&datanow[a][4] !=null&&datanow[a][5] !=null&&datanow[a][6] !=null&&datanow[a][7]&&
				datanow[a][2] !=""&&datanow[a][3] !=""&&datanow[a][4] !=""&&datanow[a][5] !=""&&datanow[a][6] !=""&&datanow[a][7] !=""
				){
					if(parseFloat(datanow[a][5])!==parseFloat(datanow[a][5])){
						report = "<p>新增场站信息失败：第"+(a+1)+"行装机容量只能为纯数字！</p><br/>";
						return "<p>新增场站信息失败：第"+(a+1)+"行装机容量只能为纯数字！</p><br/>";
					}
					changed =1;
					continue;
				}else{
					report = "<p>新增场站信息失败：第"+(a+1)+"行记录缺少关键信息！</p><br/>";
					return "<p>新增场站信息失败：第"+(a+1)+"行记录缺少关键信息！</p><br/>";
				}
				
			}
		}
		for(var i=0;i<AllData.length;i++){
			if(datanow[a][3] == AllData[i].name){
				if(datanow[a][2] !=null&&datanow[a][3] !=null&&datanow[a][4] !=null&&datanow[a][5] !=null&&datanow[a][6] !=null&&datanow[a][7]!=null&&
				datanow[a][2] !=""&&datanow[a][3] !=""&&datanow[a][4] !=""&&datanow[a][5] !=""&&datanow[a][6] !=""&&datanow[a][7] !=""
				){
					if(parseFloat(datanow[a][5])!==parseFloat(datanow[a][5])){
						return "<p>更新场站信息失败：第"+(a+1)+"行装机容量只能为纯数字！</p><br/>";
					}
					if(datanow[a][2] != AllData[i].company){
						changed =1;
						break;
					}else if(datanow[a][4] != AllData[i].bzname){
						changed =1;
						break;
					}else if(datanow[a][5] != AllData[i].zjrl){
						changed =1;
						break;
					}else if(datanow[a][6] != AllData[i].type){
						changed =1;
						break;
					}else if(datanow[a][7] != AllData[i].isJr){
						changed =1;
						break;
					}else if(datanow[a][8] != AllData[i].jrType){
						changed =1;
						break;
					}else if(datanow[a][9] != AllData[i].desc){
						changed =1;
						break;
					}else if(datanow[a][10] != AllData[i].byWork){
						changed =1;
						break;
					}else if(datanow[a][11] != AllData[i].xyWork){
						changed =1;
						break;
					}
					break;
				}else{
					report = "<p>更新场站信息失败：第"+(a+1)+"行记录缺少关键信息！</p><br/>";
					return "<p>更新场站信息失败：第"+(a+1)+"行记录缺少关键信息！</p><br/>";
				}
				
			}
			else{
				if(i==AllData.length-1){
					changed =1;
				}
			}
		}
	}
	if(datanow.length<AllData.length){
		changed =1;
	}
	if(changed>0){
		var sendObj = readyUpload(datanow);
		$.ajax( {
					type: "POST",
					contentType: "application/json",
					async: false,
					url:baseAjaxURL + "/nnrg/centerOnlineState/uploadReport?dataTime="+getDate(),
					data: JSON.stringify(sendObj),
					success:function(response) {
						report = 0;
						return 0;
					}
				}
			);
		
		
		var a = 0;
	}else{
		report = -1;
		return -1;
	}	
}

function saveReport1(){
	saveUserInfo();
	var datanow = hot.getData();
	var changed = 0;
	for(var a=0;a<datanow.length;a++){
		if(datanow[a][0] ==null)
		{
			if(datanow[a][0] ==null &&datanow[a][2] ==null&&datanow[a][3] ==null&&datanow[a][4] ==null&&datanow[a][5] ==null&&datanow[a][6] ==null&&datanow[a][7] ==null&&datanow[a][8] ==null&&datanow[a][9] ==null&&datanow[a][10] ==null&&datanow[a][11] ==null){
				continue;
			}else{
				if(datanow[a][2] !=null&&datanow[a][3] !=null&&datanow[a][4] !=null&&datanow[a][5] !=null&&datanow[a][6] !=null&&datanow[a][7]&&
				datanow[a][2] !=""&&datanow[a][3] !=""&&datanow[a][4] !=""&&datanow[a][5] !=""&&datanow[a][6] !=""&&datanow[a][7] !=""
				){
					if(parseFloat(datanow[a][5])!==parseFloat(datanow[a][5])){
						modalJs({
							icon: -1,
							title: "保存失败：",
							content: "<p>新增场站信息失败：第"+(a+1)+"行装机容量只能为纯数字！</p><br/>"
						});
						return false;
					}
					changed =1;
					continue;
				}else{
					modalJs({
						icon: -1,
						title: "保存失败：",
						content: "<p>新增场站信息失败：第"+(a+1)+"行记录缺少关键信息！</p><br/>"
					});
					return false;
				}
				
			}
		}
		for(var i=0;i<AllData.length;i++){
			if(datanow[a][3] == AllData[i].name){
				if(datanow[a][2] !=null&&datanow[a][3] !=null&&datanow[a][4] !=null&&datanow[a][5] !=null&&datanow[a][6] !=null&&datanow[a][7]!=null&&
				datanow[a][2] !=""&&datanow[a][3] !=""&&datanow[a][4] !=""&&datanow[a][5] !=""&&datanow[a][6] !=""&&datanow[a][7] !=""
				){
					if(parseFloat(datanow[a][5])!==parseFloat(datanow[a][5])){
						modalJs({
							icon: -1,
							title: "保存失败：",
							content: "<p>更新场站信息失败：第"+(a+1)+"行装机容量只能为纯数字！</p><br/>"
						});
						return false;
					}
					if(datanow[a][2] != AllData[i].company){
						changed =1;
						break;
					}else if(datanow[a][4] != AllData[i].bzname){
						changed =1;
						break;
					}else if(datanow[a][5] != AllData[i].zjrl){
						changed =1;
						break;
					}else if(datanow[a][6] != AllData[i].type){
						changed =1;
						break;
					}else if(datanow[a][7] != AllData[i].isJr){
						changed =1;
						break;
					}else if(datanow[a][8] != AllData[i].jrType){
						changed =1;
						break;
					}else if(datanow[a][9] != AllData[i].desc){
						changed =1;
						break;
					}else if(datanow[a][10] != AllData[i].byWork){
						changed =1;
						break;
					}else if(datanow[a][11] != AllData[i].xyWork){
						changed =1;
						break;
					}
					break;
				}else{
					modalJs({
						icon: -1,
						title: "保存失败：",
						content: "<p>更新场站信息失败：第"+(a+1)+"行记录缺少关键信息！</p><br/>"
					});
					return false;
				}
				
			}
			else{
				if(i==AllData.length-1){
					changed =1;
				}
			}
		}
	}
	if(datanow.length<AllData.length){
		changed =1;
	}
	if(changed>0){
		var sendObj = readyUpload(datanow);
		$.ajax( {
					type: "POST",
					contentType: "application/json",
					url:baseAjaxURL + "/nnrg/centerOnlineState/uploadReport",
					data: JSON.stringify(sendObj),
					success:function(response) {
						modalJs({
							icon: -1,
							title: "保存成功：",
							content: "<p>已保存至数据库！</p><br/>"
						});
						queryReport(nowCenterID);
					}
				}
			);
		
		
		var a = 0;
	}else{
		modalJs({
			icon: -1,
			title: "保存失败：",
			content: "<p>没有修改任何记录！</p><br/>"
		});
	}	
}

function readyUpload(dats){
	var datsObj = [];
	var addDatsObj = [];
	for(var a=0;a<dats.length;a++){
		var comid = "";
		for(var j=0;j<companys.length;j++){
			if(companys[j].companyname==dats[a][2]){
				comid = companys[j].companyid;
			}
		}
		for(var i=0;i<AllData.length;i++){
			if(dats[a][3] == AllData[i].name){
				datsObj.push({orderid:i+1,parentid:comid,stationName:dats[a][3],zhtjname:dats[a][4],zjrl:dats[a][5],stationtype:dats[a][6],isjr:dats[a][7],
				jrfs:dats[a][8],wtqkdesc:dats[a][9],thismonwork:dats[a][10],nextmonwork:dats[a][11],centerid:nowCenterID
				});
				break;
			}else if(i==AllData.length-1){
				addDatsObj.push({orderid:dats.length+addDatsObj.length,parentid:comid,stationName:dats[a][3],zhtjname:dats[a][4],zjrl:dats[a][5],stationtype:dats[a][6],isjr:dats[a][7],
				jrfs:dats[a][8],wtqkdesc:dats[a][9],thismonwork:dats[a][10],nextmonwork:dats[a][11],centerid:nowCenterID
				});
			}
		}
		if(AllData.length ==0){
			addDatsObj.push({orderid:1+addDatsObj.length,parentid:comid,stationName:dats[a][3],zhtjname:dats[a][4],zjrl:dats[a][5],stationtype:dats[a][6],isjr:dats[a][7],
			jrfs:dats[a][8],wtqkdesc:dats[a][9],thismonwork:dats[a][10],nextmonwork:dats[a][11],centerid:nowCenterID
			});
		}
	}
	
	var obj =  datsObj.concat(addDatsObj);
	return obj;
	
}

function calcDat(dats){
	$("#jrrlInput").val("dasdsa");
	alert("dsds");
}

//计算接入统计
function flashValue(data){
	var rl = 0;
	var fdrl = 0;
	var fdgs = 0;
	var gfrl = 0;
	var gfgs = 0;
	for(var i=0;i<data.length;i++){
		if(!(parseFloat(data[i].zjrl)!==parseFloat(data[i].zjrl))){
			if(data[i].isjr == "已接入"){
				if(data[i].stationtype.indexOf("风电")>=0){
					fdrl = fdrl+parseFloat(data[i].zjrl);
					fdgs++;
				}else if(data[i].stationtype.indexOf("光伏")>=0){
					gfrl = gfrl+parseFloat(data[i].zjrl);
					gfgs++;
				}
			}
			rl = rl+parseFloat(data[i].zjrl);
		}
	}
	
	$.ajax( {
				type: "POST",
				contentType: "application/json",
				async: false,
				url:baseAjaxURL + "/nnrg/centerOnlineState/centersList?dataTime="+getDate(),
				data: JSON.stringify(getQueryCondition()),
				success:function(response) {
					centersList = response.data;
				}
			}
		);
	
	for(var i=0;i<centersList.length;i++){
		if(nowCenterID == centersList[i].centerid){
			$("#jrrlInput").val(centersList[i].centerSjrl);
			$("#inputuser").val(centersList[i].inputuser);
			$("#inputuserPhone").val(centersList[i].inputuserPhone);
			$("#checkuser").val(centersList[i].checkuser);
			$("#checkuserPhone").val(centersList[i].checkuserPhone);
			$("#companyuser").val(centersList[i].companyuser);
			$("#companyuserPhone").val(centersList[i].companyuserPhone);
			$("#remark").val(centersList[i].remark);
			break;
		}
	}
	
	$("#fdzjInput").val(fdrl.toFixed(2));
	$("#gfzjInput").val(gfrl.toFixed(2));
	
	$("#fdgsInput").val(fdgs);
	$("#gfgsInput").val(gfgs);
	if(rl>0){
		$("#jrlInput").val((((gfrl+fdrl)/rl)*100).toFixed(2)+"%");
	}else{
		$("#jrlInput").val("0%");
	}
	
	
}

function allCompanyState(){
	$.ajax( {
				type: "POST",
				contentType: "application/json",
				url:baseAjaxURL + "/nnrg/centerOnlineState/ztjrl?dataTime="+getDate(),
				success:function(response) {
					if(response.data.fdjrrl==""){
						response.data.fdjrrl=0;
					}
					if(response.data.fdzrl==""){
						response.data.fdzrl=0;
					}
					if(response.data.fdjrl==""){
						response.data.fdjrl=0;
					}
					if(response.data.gfjrrl==""){
						response.data.gfjrrl=0;
					}
					if(response.data.gfzrl==""){
						response.data.gfzrl=0;
					}
					if(response.data.gfjrl==""){
						response.data.gfjrl=0;
					}
					if(response.data.ztjrl==""){
						response.data.ztjrl=0;
					}
					modalJs({
						icon: -1,
						title: "全集团新能源运营中心接入情况("+getDate()+")：",
						content: "<p>风电接入容量："+response.data.fdjrrl+" MW</p><br/>"
								+"<p>风电总容量："+response.data.fdzrl+" MW</p><br/>"
								+"<p>风电接入率："+response.data.fdjrl+" %</p><br/>"
								+"<p>光伏接入容量："+response.data.gfjrrl+" MW</p><br/>"
								+"<p>光伏总容量："+response.data.gfzrl+" MW</p><br/>"
								+"<p>光伏接入率："+response.data.gfjrl+" %</p><br/>"
								+"<p>整体接入率："+response.data.ztjrl+" %</p><br/>"
					});
				}
			}
		);
	
	
}

function openDownloadDialog(url, saveName)
{
    if(typeof url == 'object' && url instanceof Blob)
    {
        url = URL.createObjectURL(url); // 创建blob地址
    }
    var aLink = document.createElement('a');
    aLink.href = url;
    aLink.download = saveName || ''; // HTML5新增的属性，指定保存文件名，可以不要后缀，注意，file:///模式下不会生效
    var event;
    if(window.MouseEvent) event = new MouseEvent('click');
    else
    {
        event = document.createEvent('MouseEvents');
        event.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
    }
    aLink.dispatchEvent(event);
}

// 将一个sheet转成最终的excel文件的blob对象，然后利用URL.createObjectURL下载
function sheet2blob(sheet, sheetName) {
    sheetName = sheetName || 'sheet1';
    var workbook = {
        SheetNames: [sheetName],
        Sheets: {}
    };
    workbook.Sheets[sheetName] = sheet;
    // 生成excel的配置项
    var wopts = {
        bookType: 'xlsx', // 要生成的文件类型
        bookSST: false, // 是否生成Shared String Table，官方解释是，如果开启生成速度会下降，但在低版本IOS设备上有更好的兼容性
        type: 'binary'
    };
    var wbout = XLSX.write(workbook, wopts);
    var blob = new Blob([s2ab(wbout)], {type:"application/octet-stream"});
    // 字符串转ArrayBuffer
    function s2ab(s) {
        var buf = new ArrayBuffer(s.length);
        var view = new Uint8Array(buf);
        for (var i=0; i!=s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
        return buf;
    }
    return blob;
}

function exportXls(){
	/* var sheet = XLSX.utils.aoa_to_sheet(hot.getData());
	openDownloadDialog(sheet2blob(sheet), '导出.xlsx'); */
	$("#centerid").val(nowCenterID);
	$("#dataTime").val(getDate());
	$("#exportForm").attr("action", ajaxUtils.baseAjaxURL + "/nnrg/centerOnlineState/export").submit();
}