package Impl;


import java.util.EventListener;

import com.yuntech.encryption.ShutDownFolderEvent;

/**
* Title: 监听器接口
* Description: 
* Copyright: Copyright (c) 2005
* Company: cuijiang
* @author not attributable
* @version 1.0
*/
public interface ShutDownFolderListener extends EventListener{
  public void shutDownFolderEvent(ShutDownFolderEvent dm);
}