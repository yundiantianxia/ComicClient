package com.yuntech.encryption;


import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;


/**
 * windows 窗口操作类
 */
public class WindowHandle {
    

    /**
     * 获取窗口句柄
     */
    public WinDef.HWND getWindowHandle(String windowName){
        WinDef.HWND hwnd = User32.INSTANCE.FindWindow(null,windowName);
        if (hwnd==null)   //throw new RuntimeException("窗口不存在,请先运行程序");
        	System.out.println("sdfsf");
        else {
            //打开窗口
            boolean showed = User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_RESTORE );
            //关闭窗口
//            boolean showed = User32.INSTANCE.CloseWindow(hwnd);
            System.out.println(showed);
        }
        return hwnd;
    }

    public void findChildrenWindow(WinDef.HWND hwnd){
        User32.INSTANCE.FindWindowEx(hwnd,null , null , null);

    }

    public int[] getWindowXy(WinDef.HWND hwnd) {
        WinDef.RECT rect = new  WinDef.RECT();
        User32.INSTANCE.GetWindowRect(hwnd, rect);
        System.out.println(rect.left + ":" + rect.top);
        int [] xy = new int[] {rect.left , rect.top};
        return xy;
    }

    public static void main(String []args){
        WindowHandle w = new WindowHandle();
        WinDef.HWND hwnd = w.getWindowHandle("QQ");

        User32.INSTANCE.CloseWindow(hwnd);


    }

}