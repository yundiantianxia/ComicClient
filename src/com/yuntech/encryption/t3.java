package com.yuntech.encryption;


import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
 
import javax.swing.*;
 
public class t3 extends JFrame{
	
	private static final long serialVersionUID = 1L; 
	
	public t3() {
		setTitle("Hern");
		setBounds(400, 400, 400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addWindowStateListener(new WindowStateListener() {
			
			@Override
			public void windowStateChanged(WindowEvent e) {
				// TODO Auto-generated method stub
				int oldState = e.getOldState();// 获得窗体以前的状态
				int newState = e.getNewState();// 获得窗体现在的状态
				
				String from;// 标识窗体以前状态的中文字符串
				String to;// 标识窗体现在状态的中文字符串
				
				switch (oldState) {// 判断窗台以前的状态
					case Frame.NORMAL:// 窗体处于正常化
						from = "正常化";
						break;
					case Frame.MAXIMIZED_BOTH:// 窗体处于最大化
						from = "最大化";
						break;
					default:// 窗体处于最小化
						from = "最小化";
				}
				
				switch (newState) {// 判断窗台现在的状态
					case Frame.NORMAL:// 窗体处于正常化
						to = "正常化";
						break;
					case Frame.MAXIMIZED_BOTH:// 窗体处于最大化
						to = "最大化";
						break;
					default:// 窗体处于最小化
						to = "最小化";
				}
				System.out.println(from + "——>" + to);
			}
		});
		
		setVisible(true);
	}
 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		t3 test = new t3(); 
 
	}
 
}
