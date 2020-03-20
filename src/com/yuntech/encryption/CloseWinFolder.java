package com.yuntech.encryption;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.xvolks.test.Ldcnlib.Test;

/**
 * Author : wuyy
 * Version: 2020-03-11
 *
 */
public class CloseWinFolder {
	

    public boolean runbat(String path,String foldername) {

    	String cmd = "cscript -nologo -e:jscript "+ path + " CloseOpenWindow " + foldername;
        try {
            Process ps = Runtime.getRuntime().exec(cmd);
            InputStream in = ps.getInputStream();
            int c;
            while ((c = in.read()) != -1) {
                System.out.print(c);// 如果你不需要看输出，这行可以注销掉
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
	public String runbatFile(String foldername) {
		
		
		return null;
		
	}
    public String runbatt(String foldername) {
    	
    	ClassLoader classLoader = CloseWinFolder.class.getClassLoader();
    	String batfile = (classLoader.getResource("").getPath()+"/test.bat");
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
    public static void main(String[] args) throws Exception, ScriptException  {
    	
    	CloseWinFolder closewinfolder=new CloseWinFolder();
    	String foldername = "C:/Users/jcy/Desktop";
    	
    	String bl = closewinfolder.runbatt(foldername);
        System.out.println(foldername+" 执行结果: "+ bl);
    }
}
