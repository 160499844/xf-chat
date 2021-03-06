var key; // 秘钥
var sessionId = "";
var userName = "";
var groupId = "";
userName = GetQueryValue("userName");
var qrCode = GetQueryValue("code");
var headImg = "<img src='images/demo8.jpg' />";//初始化头像
//公钥
var PUBLIC_KEY = '';
//私钥
var PRIVATE_KEY = '';
var socket;
var encrypt = new JSEncrypt();
//初始化
init();

function init(){
	$.ajax({
		"async" : false,
		"url" : "user/getSession",
		"type" : "POST",
			"data" : {},
		"dataType" : "json",
		"success" : function(data) {
			var session = data.content;
			setSession(session);
		}
	});
}
/**
 * aes解密
 * 
 * @param msg
 * @returns
 */
function AESdecrypt(word) {
	var decrypt = CryptoJS.AES.decrypt(word, key, {
		mode : CryptoJS.mode.ECB,
		padding : CryptoJS.pad.Pkcs7
	});
	return CryptoJS.enc.Utf8.stringify(decrypt).toString();
}
/**
 * aes加密
 * 
 * @param plaintText
 * @returns
 */
function AESencrypt(plaintText) {

	var encryptedData = CryptoJS.AES.encrypt(plaintText, key, {
		mode : CryptoJS.mode.ECB,
		padding : CryptoJS.pad.Pkcs7
	});

	var a = plaintText + '';// 加密前：
	var b = encryptedData + '';// 加密后：
	console.log("加密前：" + plaintText + '');
	console.log("加密后：" + encryptedData + '');
	return encryptedData + '';
}
/**
 * [通过参数名获取url中的参数值]
 * 示例URL:http://htmlJsTest/getrequest.html?uid=admin&rid=1&fid=2&name=小明
 * 
 * @param {[string]}
 *            queryName [参数名]
 * @return {[string]} [参数值]
 */
function GetQueryValue(queryName) {
	var query = decodeURI(window.location.search.substring(1));
	var vars = query.split("&");
	for (var i = 0; i < vars.length; i++) {
		var pair = vars[i].split("=");
		if (pair[0] == queryName) {
			console.log("获取参数" + queryName + "=", pair[1]);
			return pair[1];
		}
	}
	return null;
}
/**
 * 初始化
 * @returns
 */
function socketInit(url){
	// 如果浏览器支持WebSocket
	if (window.WebSocket) {
		// 参数就是与服务器连接的地址
		socket = new WebSocket(url);
		//socket = new WebSocket("ws://y-xiaofeng.top:8900/ws");
		// 客户端收到服务器消息的时候就会执行这个回调方法
		socket.onmessage = function(event) {
			// var ta = document.getElementById("responseText");
			// var countSpan = $("#count");
			// var usersSpan = $("#users");
			// 将接收的json字符串转对象
			var content = event.data;
			//content = AESdecrypt(content);
			//console.log("解密消息", content);
			var result = JSON.parse(content);
			console.log("收到消息", result);
			// ta.value = ta.value + "\n" + result.content;
			messageElement(result);

			updateGroupInfo(result);
			// countSpan.html(result.info.group_count);
			// usersSpan.val(result.info.gourp_users);
		}

		// 连接建立的回调函数
		socket.onopen = function(event) {
			var ta = document.getElementById("responseText");
			// ta.value = "连接服务器成功";
			send("加入群聊","T");
		}

		// 连接断掉的回调函数
		socket.onclose = function(event) {
			//var ta = document.getElementById("responseText");
			//mui.toast("该功能正在开发中，敬请期待");
			//ta.value = ta.value + "\n" + "服务器中断";
			mui.toast("服务器连接失败");
		}
	} else {
		mui.toast("当前环境不支持加密通信");
	}
	
}
/**
 * 更新组信息
 * @param messageVo
 * @returns
 */
function updateGroupInfo(messageVo){
	var type = messageVo.type;
	if("SR"===type || "SA" === type){
		var title = "";
		title += messageVo.groupId;
		title += "("+messageVo.info.group_count+")";
		$("#title").html(title);
	}

}
function getPic(fileName){
	var base64;
	$.ajax({
		"async" : false,
		"url" : "file/img",
		"type" : "GET",
			"data" : {
				"fileName":fileName
				
			},
		"dataType" : "json",
		"success" : function(data) {
			var result = data.content;
			base64 = result;
		}
	});
	return base64;
}
/**
 * 组装消息组件
 * 
 * @param messageVo
 * @returns
 */
function messageElement(messageVo) {
	var type = messageVo.type;
	var html = "";
	var name = messageVo.name;
	var msg = AESdecrypt(messageVo.msg);
	console.log("解密服务器广播消息",msg);
	var className = "im-chat-user";
	/*
	 * if(name===userName){ className = "im-chat-mine"; }
	 */
	if (name == userName) {
		html = '<li class="im-chat-mine">';
	} else {
		html = "<li>";
		
	}
	html += "<div class='"
		+ className
		+ "'>"
		+ headImg
		+ "<cite><i>" + messageVo.time + "</i>&nbsp;" + messageVo.name
		+ "</cite>" + "</div>";
	html += "<div class='im-chat-text'>";
	if ("T" === type) {
		html += msg;
	}else if("P" === type){
		//请求图片地址
		var bs64 = getPic(msg);
		html += "<img data-action='zoom' src='"+bs64+"'/>"
		
		//html += "<img  data-preview-src='' data-preview-group='1' src='"+messageVo.msg+"'/>"
	}else{
		html = "<div class='sys-message-div'><div class='im-chat-user sys-message-info'><cite><i>"+msg+"</i></cite></div></div>";
	}
	html += "</div></li>";
	$("#msg_list").append(html);
	//滚动条到最底部(还有其他方法吗？)
	document.getElementById("msg_list").scrollTop = msgList.scrollHeight
			+ msgList.offsetHeight;
}
// 发送数据
function send(message,type) {
	//限制内容长度
	if(message.length>2000){
		message = message.substring(2000);
	}
	
	var msg;
	if("" === type){
		type = 'T';
	}
	if("T" === type){
		 msg = AESencrypt(message);
	}else{
		msg = message;
	}
	//msg = AESencrypt(message);
	var obj = {
		"sessionId" : sessionId,
		"msg" : msg,
		"name" : userName,
		"type" : type,
		"groupId" : groupId
	}
	if (!window.WebSocket) {
		return;
	}

	// 当websocket状态打开
	if (socket.readyState == WebSocket.OPEN) {
		var content = JSON.stringify(obj);
		//content = AESencrypt(content);
		//console.log("加密后:", content);
		socket.send(content);
	} else {
		mui.toast("服务器连接失败");
	}
}

//校验密码
function checkPassword(password){
	var c = false;
	$.ajax({
		"async" : false,
		"url" : "group/checkPassword",
		"type" : "POST",
			"data" : {
				"password":password
				
			},
		"dataType" : "json",
		"success" : function(data) {
			var result = data.content;
			if(result==true){
				c = true;
			}
		}
	});
	return c;
}
//获取公钥
/*function getPrivateKey(){
	$.ajax({
		"async" : false,
		"url" : "group/checkPassword",
		"type" : "POST",
			"data" : {
				"password":password
			},
		"dataType" : "json",
		"success" : function(data) {
			var result = data.content;
			return result;
		}
	});
	return "";
}*/
function setSession(s){
	sessionId = s;
}
/**
 * 临时用户名
 * @param userName
 * @returns
 */
function updateUserName(userName){
	var check = false;
	$.ajax({
		"async" : false,
		"url" : "user/updateInfo",
		"type" : "POST",
			"data" : {
				"userName":userName

			},
		"dataType" : "json",
		"success" : function(data) {
			console.log("data",data);
			if(data.code===0){
				//校验通过
				check= true;
			}else{
				//校验失败
				check= false;
			}
		}
	});
	return check;
}
function getGroupInfo(code){
	var content;
	$.ajax({
		"async" : false,
		"url" : "group/getGroupInfo",
		"type" : "POST",
			"data" : {"code":code},
			"dataType" : "json",
			"success" : function(data) {
			content = data.content;
			//CryptoJS.enc.Utf8.parse(1538663015386630);
			key = CryptoJS.enc.Utf8.parse(content.key);
			console.log("key:",key);
			groupId = content.n;
			userName = content.userName;
			$("#groupidinput").val(groupId);
			//修改groupId
		}
	});
	return content;
}
function getGroupName(code){
	var content;
	$.ajax({
		"async" : false,
		"url" : "group/getGroupName",
		"type" : "POST",
			"data" : {"code":code},
			"dataType" : "json",
			"success" : function(data) {
			content = data.content;
			//CryptoJS.enc.Utf8.parse(1538663015386630);
			//key = CryptoJS.enc.Utf8.parse(content.key);
			//console.log("key:",key);
			groupId = content.n;
			$("#groupidinput").val(groupId);
			//修改groupId
		}
	});
	return content;
}