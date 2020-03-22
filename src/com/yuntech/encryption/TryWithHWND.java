package com.yuntech.encryption;


import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;
import com.sun.jna.win32.StdCallLibrary;
	
	 
	public class  TryWithHWND {
	   public interface User32 extends StdCallLibrary {
	      User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);
	      boolean EnumWindows(WinUser.WNDENUMPROC lpEnumFunc, Pointer arg);
	      int GetWindowTextA(HWND hWnd, byte[] lpString, int nMaxCount);
	   }
	    /**
	     * 获取窗口句柄
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
	   public static void main(String[] args) {
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
	            if(wText.contains("Sougou")) {
	            	// 补充相关操作   匹配到地址栏一样，则关闭资源管理器的该文件窗口  closeWindown操作无效 请问为什么？
	            	System.out.println("testFolder:"+ wText);
//	            	getWindowHandle(wText);
	            	com.sun.jna.platform.win32.User32.INSTANCE.CloseWindow(hWnd);
//	            	HWND AddressBarWnd=FindControlWnd(hWnd,41477);
	            }
	            System.out.println("Found window with text " + hWnd + ", total " + ++count
	                  + " Text: " + wText);
	            return true;
	         }
	      }, null);
	   }
	}

