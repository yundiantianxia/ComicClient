@if (0)==(0) echo off

set strFolder=%1
::echo %strFolder%

for /f "tokens=*" %%i in ('dir /s/b/ad %strFolder%') do (
::	echo %%~ni
	cscript -nologo -e:jscript %~s0 CloseOpenWindow "%%~ni"
	echo,
)
::pause
goto :EOF
@end

var func = WScript.Arguments(0);
var str = WScript.Arguments(1);

switch (func) {
	case "CloseOpenWindow":
//		WScript.Echo("1-"+str);
		CloseOpenWindow(str);
		break;
	default:;
}

function CloseOpenWindow(str) {
//	WScript.Echo("The filename: " + str);
	var oShell = new ActiveXObject("WScript.Shell");
	var ret = oShell.AppActivate(str);
	
//	WScript.Echo("Filename: "+str+" OpenStat: " + ret);
	if (ret) {
		/// 打开状态为真时，关闭它
		oShell.SendKeys("%{F4}");
		WScript.Echo("true");
	//	WScript.Echo("The OpenFile:" + str + " Haved Closed");
	} 
	 else {
		WScript.Echo("false");
	}
}