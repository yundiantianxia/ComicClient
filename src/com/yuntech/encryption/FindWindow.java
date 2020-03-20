package com.yuntech.encryption;

import java.io.UnsupportedEncodingException;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;

/**
 * Author : wuyy
 * Version: 2020-03-11
 *
 */


		interface HelloInter extends Library{
		 int toupper(int ch);
		 double pow(double x,double y);
		 void printf(String format,Object... args);
		}
		
		public class FindWindow {
		 
		 public static void main(String [] args){
		 HelloInter INSTANCE =
		(HelloInter)Native.loadLibrary(
				Platform.isWindows()?"msvcrt":"c",
				HelloInter.class);
		 INSTANCE.printf("Hello, Worldn");
		 String [] strs = new String[]{"芙蓉","如花","凤姐"};
		 for (int i=0;i < strs.length;i++) {
		 INSTANCE.printf("人物 %d: %sn", i, strs[i]);
		}
		 
		 System.out.println("pow(2d,3d)=="+INSTANCE.pow(2d, 3d));
		 System.out.println("toupper('a')=="+(char)INSTANCE.toupper((int)'a'));
		}
	}		 
		 
//		//遍历所有子窗口的子窗口 , Z序遍历
//		 void print_window2(HWND parent , int level)
//		 {
//		 	HWND child = NULL;
//		 	TCHAR buf[MAX_PATH];
//		 	DWORD pid = 0, tid = 0;
//		 	do{
//		 		child = FindWindowEx(parent, child, NULL, NULL);
//		 		int ret = GetWindowText(child, buf, MAX_PATH);
//		 		buf[ret] = 0;
//		 		tid = GetWindowThreadProcessId(child, &pid);
//		 		for (int i = 0; i < level; ++i)
//		 			_tprintf(L"\t");
//		 		_tprintf(L"%s ,  pid:%d, tid:%d\n", buf, pid, tid);
//		 		if (child)
//		 			print_window2(child , level + 1);
//		 	} while (child);
//		 }
//		  
//		 //遍历所有 explore 下的窗口 , Z序遍历
//		 void print_window()
//		 {
//		 	HWND child = NULL;
//		 	TCHAR buf[MAX_PATH];
//		 	DWORD pid = 0, tid = 0;
//		  
//		 	do{
//		         //查找 Explore 下的一个窗口,如果能找到则根据 Explore 下的child 继续找
//		 		child = FindWindowEx(null, child, null, null);
//		 		int ret = GetWindowText(child, buf, MAX_PATH);
//		 		buf[ret] = 0;
//		 		tid = GetWindowThreadProcessId(child, &pid);
//		 		_tprintf(L"%s ,  pid:%d, tid:%d\n", buf, pid, tid);
//		         
//		         //遍历子窗口们
//		 		if (child)
//		 			print_window2(child, 1);
//		 	} while (child);
//		 }



		


