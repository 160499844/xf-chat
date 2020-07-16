var key = CryptoJS.enc.Utf8.parse("1538663015386630"); // 秘钥
/**
 * aes解密
 * @param msg
 * @returns
 */
function AESdecrypt(word) {
    var decrypt = CryptoJS.AES.decrypt(word, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
    return CryptoJS.enc.Utf8.stringify(decrypt).toString();
}
/**
 * aes加密
 * @param plaintText
 * @returns
 */
function AESencrypt(plaintText) {
	
    var encryptedData = CryptoJS.AES.encrypt(plaintText, key, {  
        mode: CryptoJS.mode.ECB,  
        padding: CryptoJS.pad.Pkcs7  
    });  

	var a = plaintText + '';//加密前：
	var b = encryptedData + '';//加密后：
	console.log("加密前："+plaintText + '');  
	console.log("加密后："+encryptedData + '');  
    return encryptedData;
}
