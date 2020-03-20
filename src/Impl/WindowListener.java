package Impl;

import java.awt.event.WindowEvent;
import java.util.EventListener;

/**
 * Author : wuyy
 * Version: 2020-03-11
 *
 */

public interface WindowListener extends EventListener {
    public void windowActivated(WindowEvent e); //窗体被激活时触发
    public void windowOpened(WindowEvent e);//窗体被打开时触发
    public void windowIconified(WindowEvent e); //窗体被图标化时触发
    public void windowDeiconified(WindowEvent e);//窗体被非图标化时触发
    public void windowClosing(WindowEvent e);//窗体将要被关闭时触发
    public void windowDeactivated(WindowEvent e);//窗体不再处于激活状态时触发
    public void windowClosed(WindowEvent e);//窗体已经被关闭时触发
}
