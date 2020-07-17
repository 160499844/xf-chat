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
// 发送数据
function send(message) {

	var obj = {
		"msg" : message,
		"name" : userName,
		"type" : "T",
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