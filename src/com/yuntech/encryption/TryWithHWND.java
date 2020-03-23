package com.yuntech.encryption;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
	
	 
	public class  TryWithHWND {
	   public interface User32 extends StdCallLibrary {
	      User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);
	      boolean EnumWindows(WinUser.WNDENUMPROC lpEnumFunc, Pointer arg);
	      boolean EnumChildWindows(WinUser.WNDENUMPROC lpEnumFunc, Pointer arg);
	      int GetWindowTextA(HWND hWnd, byte[] lpString, int nMaxCount);
	   }

	   /**
	    * 
	    * Func:通过窗口标题获取窗口句柄
	    * Data: 2020-03-23
	    * @param windowName
	    * @return
	    */
	    public static WinDef.HWND getWindowHandle(String windowName){
	        WinDef.HWND hwnd = com.sun.jna.platform.win32.User32.INSTANCE.FindWindow(null,windowName);
	        if (hwnd==null)   //throw new RuntimeException("窗口不存在,请先运行程序");
	        	System.out.println("sdfsf");
	        else {
	            //打开窗口
//	            boolean showed = User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_RESTORE );
	            //关闭窗口
	            boolean showed = com.sun.jna.platform.win32.User32.INSTANCE.CloseWindow(hwnd);
	            System.out.println(showed);
	        }
	        return hwnd;
	    }
	    public static void enumwindow() {
	    	final User32 user32 = User32.INSTANCE;
	    	user32.EnumWindows(new WNDENUMPROC() {
		         int count = 0;
		         @Override
		         public boolean callback(HWND hWnd, Pointer arg1) {
		            byte[] windowText = new byte[512];
		            user32.GetWindowTextA(hWnd, windowText, 512);
		            String wText = Native.toString(windowText);
		 
		            // get rid of this if block if you want all windows regardless of whether
		            // or not they have text
		            if (wText.isEmpty()) {
		               return true;
		            }
		            if(wText.contains("test01")) {
		            	// 补充相关操作   匹配到地址栏一样，则关闭资源管理器的该文件窗口  closeWindown操作无效 请问为什么？
		            	System.out.println("testFolder:"+ wText+ " testHWND:"+hWnd);
//		            	getWindowHandle(wText);
//		            	com.sun.jna.platform.win32.User32.INSTANCE.CloseWindow(hWnd);
//		            	user32.EnumChildWindows(new WNDENUMPROC() { 
//		            		@Override 
//		            		public boolean callback(HWND hWnd, Pointer arg1) {
//		            			user32.GetWindowTextA(hWnd, windowText, 512);
//		        	            String wText = Native.toString(windowText);
//		        	            if (wText.isEmpty()) {
//		        	               return true;
//		        	            }else {
//		        	            	System.out.println("wText:"+ wText);
//		        	            }
//		            			return true;
//		            			}
//		            		},null);
//		            	if(com.sun.jna.platform.win32.User32.INSTANCE.DestroyWindow(  hWnd )  ) {
		            		IntByReference ir = new IntByReference();
		            		int i = com.sun.jna.platform.win32.User32.INSTANCE.GetWindowThreadProcessId(hWnd, ir);
//		            		WPARAM wParam =new WPARAM();
//		            		LPARAM lParam = new LPARAM();
		            		// W_CLOSE  0x10  关闭信号
		            		 com.sun.jna.platform.win32.User32.INSTANCE.SendMessage(hWnd, 16, null, null);
		            		System.out.println("Destroy is suc"+ir + "---" + i);
//		            		System.out.println(com.sun.jna.platform.win32.Kernel32.GetCurrentProcessId());
//		            		System.Diagnostics.Process p = System.Diagnostics.Process.GetProcessById(k);
		            
//		            		try {
//								Runtime.getRuntime().exec(" cmd /c taskkill /pid %d -f" + i);
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								System.out.println("sdfs");
//								e.printStackTrace();
//							}
//		            		Kernel32.INSTANCE.GetProcessId(process);
//		            		Kernel32.INSTANCE.TerminateProcess(hProcess, uExitCode);
//		            	    Kernel32.INSTANCE.FindWindowEx(HWND parent, HWND child, String className, String window);
//		            	    Kernel32.INSTANCE.EnumChildWindows(hWnd, WNDENUMPROC lpEnumFunc, Pointer data);
//		            	}else{
//		            		System.out.println("Destroy is fail");
//		            		System.out.println(Kernel32.INSTANCE.GetLastError());
//		            	};
//		            	HWND AddressBarWnd=FindControlWnd(hWnd,41477);
		            }
		            System.out.println("Found window with text " + hWnd + ", total " + ++count
		                  + " Text: " + wText);
		            return true;
		         }
		      }, null);
	    }
	   public static void main(String[] args) throws Exception {
		   
		   while(true) {
			   WinDef.HWND hWnd = getWindowHandle("test01");
			   com.sun.jna.platform.win32.User32.INSTANCE.SendMessage(hWnd, 16, null, null);
			   TimeUnit.MILLISECONDS.sleep(100);
		   }
	      
	   }
	}

