package com.yuntech.encryption;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;



/**
 * 
 * @author wuyy
 * @since 2020-2-28
 *
 */
public class Archive {
	
	/**
	 * 文件流, *.tar文件
	 */
	public static void archive(File[] srcs,File tar) {//归档
		try {
			FileOutputStream fos = new FileOutputStream(tar);
			for(int i=0; i<srcs.length; ++i) {
				//获得文件名
				byte[] fileName = srcs[i].getName().getBytes();
				//获得文件名长度
				byte fileNameLen = (byte)fileName.length;
				//获得文件内容
				FileInputStream fis = new FileInputStream(srcs[i]);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int len = -1;
				byte[] buf = new byte[1024];
				while((len = fis.read(buf)) != -1) {
					baos.write(buf, 0, len);
				}
				baos.close();
				fis.close();
				byte[] fileContent = baos.toByteArray();
				//获得文件内容长度
				byte[] fileContentLen = DataUtil.int2bytes(fileContent.length);
				//写入
				fos.write(fileNameLen);
				fos.write(fileName);
				fos.write(fileContentLen);
				fos.write(fileContent);
			}
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 归档当前文件到当前路径下
	 * @param src
	 */
	public static void unArchive(File src) {//解档到当前文件夹
		try {
			FileInputStream fis = new FileInputStream(src);
			int fileNameLen = -1;
			while((fileNameLen = fis.read()) != -1){
				byte[] byteFileName = new byte[fileNameLen];
				fis.read(byteFileName);
				String fileName = src.getParent() +"\\"+ new String(byteFileName);
				@SuppressWarnings("resource")
				FileOutputStream fos = new FileOutputStream(fileName);
				byte[] byteFileContentLen = new byte[4];
				fis.read(byteFileContentLen);
				int fileContentLen = DataUtil.bytes2int(byteFileContentLen);
				int divisorFileContentLen = fileContentLen / 1024;
				int remainderFileContentLen = fileContentLen % 1024;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] divisorBuf = new byte[1024];
				for(int i=0; i<divisorFileContentLen; ++i) {
					fis.read(divisorBuf);
					baos.write(divisorBuf);
				}
				byte[] remainderBuf = new byte[remainderFileContentLen];
				fis.read(remainderBuf);
				baos.write(remainderBuf);
				baos.close();
				byte[] fileContent = baos.toByteArray();
				fos.write(fileContent);
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Unarchive Sucsed");
	}
}