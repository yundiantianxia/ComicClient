package com.yuntech.encryption;
import java.util.EventObject;

/**
* Title: 事件处理类，继承了事件基类
* Description: 
* Copyright: Copyright (c) 2005
* Company: cuijiang
* @author not attributable
* @version 1.0
*/
public class ShutDownFolderEvent extends EventObject
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Object obj;
	private String sName;
	
	  public ShutDownFolderEvent(Object source,String sName)  {
	   super(source);
	   obj = source;
	   this.sName=sName;
	  }
  
	  public Object getSource()
	  {
	   return obj;
	  }
  
	  public void say()
	  {
	   System.out.println("这个是 say 方法...");
	  }
  
	  public String getName()
	  {
	   return sName;
	  }
}