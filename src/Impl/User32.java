package Impl;



import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

//提供 获取窗口句柄  获取窗口矩形坐标 鼠标控制 
public interface User32 extends StdCallLibrary{

 User32 INSTANCE = (User32)Native.loadLibrary("User32",User32.class);
 
 int PostMessageA(int a,int b,int c,int d);
 
 int FindWindowA(String a,String b);
 
// int GetWindowRect(int hwnd,Rect r);
 
}
