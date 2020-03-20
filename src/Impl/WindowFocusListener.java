package Impl;

import java.awt.event.WindowEvent;
import java.util.EventListener;

/**
 * Author : wuyy
 * Version: 2020-03-11
 *
 */

public interface WindowFocusListener extends EventListener {
	 
	public void windowGainedFocus(WindowEvent e); //窗体获得焦点时被触发
 
	public void windowLostFocus(WindowEvent e); //窗体失去焦点时被触发
 
}
