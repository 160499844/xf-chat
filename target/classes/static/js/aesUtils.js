var key = CryptoJS.enc.Utf8.parse("1538663015386630"); // 秘钥
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
	return encryptedData;
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
			return pair[1];
		}
	}
	return null;
}

var socket;
var userName = GetQueryValue("name");
var groupId = GetQueryValue("groupId");
// 如果浏览器支持WebSocket
if (window.WebSocket) {
	// 参数就是与服务器连接的地址
	socket = new WebSocket("ws://192.168.0.178:8000/ws");

	// 客户端收到服务器消息的时候就会执行这个回调方法
	socket.onmessage = function(event) {
		// var ta = document.getElementById("responseText");
		// var countSpan = $("#count");
		// var usersSpan = $("#users");
		// 将接收的json字符串转对象
		var content = event.data;
		content = AESdecrypt(content);
		console.log("解密消息", content);
		var result = JSON.parse(content);
		console.log("收到消息", result);
		// ta.value = ta.value + "\n" + result.content;
		messageElement(result);
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
		var ta = document.getElementById("responseText");
		ta.value = ta.value + "\n" + "服务器中断";
	}
} else {
	alert("浏览器不支持WebSocket！");
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
		+ "<img src='http://tva1.sinaimg.cn/crop.0.0.180.180.180/86b15b6cjw1e8qgp5bmzyj2050050aa8.jpg' />"
		+ "<cite><i>" + messageVo.time + "</i>&nbsp;" + messageVo.name
		+ "</cite>" + "</div>";
	html += "<div class='im-chat-text'>";
	if ("T" === type) {
		html += messageVo.msg;
	}else if("P" === type){
		html += "<img data-action='zoom' src='"+messageVo.msg+"'/>"
	}
	html += "</div></li>";
	$("#msg_list").append(html);
	//滚动条到最底部(还有其他方法吗？)
	document.getElementById("msg_list").scrollTop = msgList.scrollHeight
			+ msgList.offsetHeight;
}
// 发送数据
function send(message,type) {
	if("" === type){
		type = 'T';
	}
	var obj = {
		"msg" : message,
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
		content = AESencrypt(content);
		console.log("加密后:", content);
		socket.send(content);
	} else {
		alert("服务器连接失败");
	}
}

