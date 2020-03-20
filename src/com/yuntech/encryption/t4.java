package com.yuntech.encryption;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
 
import javax.swing.*;
 
public class t4 extends JFrame{
	
	private static final long serialVersionUID = 1L; 
	
	public t4() {
		setTitle("Hern");
		setBounds(400, 400, 400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {//窗体被打开时触发
				// TODO Auto-generated method stub
				System.out.println("窗口被打开！");
			}
			
			@Override
			public void windowIconified(WindowEvent e) {//窗体被最小化时触发
				// TODO Auto-generated method stub
				System.out.println("窗口被最小化！");
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {//窗体被非最小化时触发
				// TODO Auto-generated method stub
				System.out.println("窗口被非最小化！");
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {//窗体不再处于激活状态时触发
				// TODO Auto-generated method stub
				System.out.println("窗口不再处于激活状态！");
			}
			
			@Override
			public void windowClosing(WindowEvent e) {//窗体将要被关闭时触发
				// TODO Auto-generated method stub
				System.out.println("窗口将要被关闭！");
			}
			
			@Override
			public void windowClosed(WindowEvent e) {//窗体已经被关闭时触发
				// TODO Auto-generated method stub
				System.out.println("窗口已经被关闭！");
			}
			
			@Override
			public void windowActivated(WindowEvent e) {//窗体被激活时触发
				// TODO Auto-generated method stub
				System.out.println("窗口被激活！");
			}
		});
		
		setVisible(true);
	}
 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		t3 test = new t3();
 
	}
 
}
