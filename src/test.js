/**
 * 
 */

var fun1 = function CloseOpenWindow(str) {
	print("当前传入参数为 : " + str);
	var oShell = new ActiveXObject("WScript.Shell");
	var ret = oShell.AppActivate(str);
	print("打开状态 : " + ret);
	if (ret) {
		/// 打开状态为真时，关闭它
		oShell.SendKeys("%{F4}");
		print("存在打开文件夹 " + str + " ，关闭之");
	}
}