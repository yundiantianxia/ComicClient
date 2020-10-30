package com.yuntech.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.LRESULT;

import javax.script.ScriptException;

/**
 * Author : wuyy
 * Date: 2020-03-11
 * Des:  关闭窗口类
 */
public class CloseWinFolder {
	

	/**
	 * 
	 * Func: 通过bat方式 关闭指定的文件夹窗口   不过该方法  适合关闭特定的程序，不过关闭窗口 勿用
	 * Date: 2020-03-23
	 * @param path   batfile文件所在的路径(包含文件名）
	 * @param foldername   要关闭的文件夹名称
	 * @return
	 */
    public boolean runbat(String path,String foldername) {

    	String cmd = "cscript -nologo -e:jscript "+ path + " CloseOpenWindow " + foldername;
        try {
            Process ps = Runtime.getRuntime().exec(cmd);
            InputStream in = ps.getInputStream();
            int c;
            while ((c = in.read()) != -1) {
                System.out.print(c);// 如果不需要看输出，这行可以注销掉
            }
            in.close();
            if(ps.exitValue() == 0){ 
        	   System.out.println("OK"); 
        	}else{ 
        	   System.out.println("ERROR"); 
        	}
            ps.waitFor();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        System.out.println("child thread donn");
        return true;
    }
	
	/**
	 * 
	 * Func:  通过bat方式 关闭指定的文件夹窗口   不过该方法  适合关闭特定的程序，不过关闭窗口 勿用
	 * Data: 2020-03-23
	 * @param foldername   batfile 被放到Class路径下，一起打包
	 * @return
	 */
    public String runbatt(String foldername) {
    	
    	ClassLoader classLoader = CloseWinFolder.class.getClassLoader();
    	String batfile = (classLoader.getResource("").getPath()+ "/test.bat");
    	batfile=batfile.substring(1, batfile.length());
    	StringBuilder result = new StringBuilder();
    	String cmd = "cscript -nologo -e:jscript "+ batfile + " CloseOpenWindow " + foldername;
//    	String cmd = "cmd /c start /b "+ batfile + "    " + foldername;
        try {
            Process ps = Runtime.getRuntime().exec(cmd);
     
    		// 获取shell返回流
    		BufferedInputStream in = new BufferedInputStream(ps.getInputStream());
    		// 字符流转换字节流
    		BufferedReader br = new BufferedReader(new InputStreamReader(in,"GBK"));
    		// 这里也可以输出文本日志
     
    		String lineStr;
    		while ((lineStr = br.readLine()) != null) {
    			result.append(lineStr).append('\n');
    		}
    		// 关闭输入流
    		br.close();
    		in.close();
            // 销毁子进程
            if (ps != null) {
                ps.destroy();
            }
        } catch (Exception oe) {
            oe.printStackTrace();
        } 
//        	System.out.println("child thread done");
            // 返回执行结果
            return result.toString();
    }
    
	   /**
	    * 
	    * Func:通过窗口标题获取窗口句柄
	    * Data: 2020-03-23
	    * @param windowName
	    * @return
	    */
	    public static void closewindow(String windowName){
	    	//通过窗口标题获取窗口句柄
	        WinDef.HWND hWnd = com.sun.jna.platform.win32.User32.INSTANCE.FindWindow("CabinetWClass" ,windowName);
	        if (hWnd==null)   //throw new RuntimeException("窗口不存在,请先运行程序");
	        	System.out.println("窗口不存在，请继续等待");
	        else {
	        	// 0x10 关闭窗口信号    lresult 0 关闭成功
	        	LRESULT lresult= com.sun.jna.platform.win32.User32.INSTANCE.SendMessage(hWnd, 0X10, null, null);
	        	System.out.println("lresult:"+lresult);
	        }
	    }

    public static void main(String[] args) throws Exception, ScriptException {
    	
//    	while(true) {
////    		closewindow("test01");
////    		TimeUnit.MILLISECONDS.sleep(5000);
////    	}
    }
}
