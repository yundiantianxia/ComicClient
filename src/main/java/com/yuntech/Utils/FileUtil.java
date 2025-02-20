package com.yuntech.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import com.yuntech.Utils.CloseWinFolder;


public class FileUtil {


	
	/**
	 * 解包方法：解包指定文件到指定文件夹下
	 * 逆向恢复文件 如果加密 则还原文件，恢复文件属性和原文件名
	 *
	 * @throws Exception 
	 */
	public static boolean unPackege() throws Exception {
		String[] mode= new String[2];
		mode[0]=".{645FF040-5081-101B-9F08-00AA002F954E}";
		mode[1]=".{2559a1f2-21d7-11d4-bdaf-00c04f60b9f0}";
		
        File file = null;
        String path = folderChooser();
        if(path==null) return false;
        
        if(path.contains(mode[1])) {
        	if(!reFileDirName(path, path.replace(mode[1], ""))) {
        		System.out.println(path + " -S -H Failed");
        		return false;
        	}
        	Runtime.getRuntime().exec("attrib " + path.replace(mode[1], "") + " -S -H ");
 			System.out.println(path.replace(mode[1], "") + " -S -H Suced");
        }
        path=path.replace(mode[1], "");
        file = new File(path) ;
        File[] fs = file.listFiles();
        
        for(File lfs : fs){
        	if(!lfs.exists()) {
        		;//throw new RuntimeException(lfs.getName()+" Is No Exist");
        	}
        	if(lfs.isDirectory()&&lfs.getPath().contains(mode[1])) {
        		Runtime.getRuntime().exec("attrib " +lfs.getPath()+ " -S -H ");
        		System.out.println(lfs.getPath() + " -S -H Suced");
        		if(!reFileDirName(lfs.getPath(),lfs.getPath().replace(mode[1], ""))) {
        			System.out.println(lfs.getPath().replace(mode[1], "") + " -S -H Failed");
        			return false;
        		}
    			
        	}else if(lfs.isFile()){
        		decryption(lfs.getPath());
        		System.out.println(lfs.getPath()+" haved be UnArchived");
        	}else {
        		System.out.println("File will Be locked ");
        	}
        }
		return true;
	}
	/**
	 * 打包方法：将指定文件夹下所有文件夹做名称处理
	 * 将指定文进行加密处理 首字节取反操作
	 * @throws IOException 
	 * 
	 * */
	public static boolean packege() throws IOException {
		String[] mode= new String[2];

		// windows安全文件的类标识符 加密策略之一
		mode[0]=".{645FF040-5081-101B-9F08-00AA002F954E}";
		mode[1]=".{2559a1f2-21d7-11d4-bdaf-00c04f60b9f0}";
		
//		C盘可以存放文件
		if(ShowBasicNameSpace()) {
//			System.out.println("Create File Is Sucs!");
		}
		File file =  null;
		String path = folderChooser();

		if(path != null) {
			file=new File(path);
		}else {
			return false;
		}
        File[] fs = file.listFiles();
        
        if(!file.exists()) {
        	throw new RuntimeException(file.getPath()+"file is no exist");
        }
        if (fs != null) {
	        for(File lfs : fs){
	        	if(!lfs.exists()) {
	        		throw new RuntimeException(lfs.getPath() +" is no exist");
	        	}
	        	if(lfs.isDirectory()&&!lfs.getPath().contains(mode[1])) {
	        		if(!reFileDirName(lfs.getPath(), lfs.getPath()+mode[1])) {
	        			System.out.println(lfs.getPath() + "---Haved Be S H  Failed To---" + lfs.getPath()+mode[1]);
	        			return false;
	        		}
	        		// 文件夹属性更改  S 系统文件  H 隐藏文件
	        		Runtime.getRuntime().exec("attrib "+ lfs.getPath()+mode[1] + " +S +H " );
	    			System.out.println(lfs.getPath() + "---Haved Be S H  Suced To---" + lfs.getPath()+mode[1]);
	        	}else if(lfs.isFile()){
	        		//文件加密
	        		encryption(lfs.getPath());
	        		System.out.println(lfs.getPath()+"---haved be Archived");
	        	}else {
	        		System.out.println("Packeged Failed");
	        		return false;
	        	}
	        }
        }
        
        if(file.isDirectory()&&!path.contains(mode[1])) {
        	if(!reFileDirName(path, path+mode[1])) {
    			System.out.println(path+"---Haved Be S H Failed To---" + path + mode[1]);
        		return false;
        	}
        	Runtime.getRuntime().exec("attrib " + path + mode[1]  + " +S +H ");
			System.out.println(path+"---Haved Be S H Suced To---" + path + mode[1]);
        }
        
		return true;
	}
	
    
	/** 
	 * Des:C盘下新建文件
	 * 条件：C盘大于5G
	 */
	public static boolean ShowBasicNameSpace() {

		String	str = ShowNameSpace(new File("c:"));
		
		//获取文件大小去掉 单位符
		str= str.substring(0, str.length()-1);
		
		if (Double.valueOf(str) > 5.00) {
			return true;
		}
		return false;
	}
	
	
	/**	获取硬盘的指定盘符名和剩余大小
	 * file 磁盘名
	 */    
	public static String ShowNameSpace(File file){        
		//	 当前文件系统类              
		return FormetFileSize(file.getFreeSpace());
		}
	
	/**	返回文件大小 */
	public static String FormetFileSize(long fileS) {        
		DecimalFormat df = new DecimalFormat("#.00");       	
		String fileSizeString ="";  

		if (fileS < 1024) {           
			fileSizeString = df.format((double) fileS) + "B";       
		} else if (fileS < 1048576) {   
			fileSizeString = df.format((double) fileS / 1024) + "K";  
			} else if (fileS < 1073741824) {   
				fileSizeString = df.format((double) fileS / 1048576) + "M";        
				} else {    
					fileSizeString = df.format((double) fileS / 1073741824) + "G";   
					}   
					return fileSizeString;  
		} 
	
	/**
	 *	26个字符
	 */
	 private static String[] capital = {
			   "A","B","C","D","E","F","G","H","I","J","K",
			   "L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"}; 
	 private static Random random = new Random();
	 
	/**
	 * 
	 * Func: 获取随机数文件名
	 * Data: 2020-03-22
	 * @param num
	 * @return
	 */
	 public static String getRandom(int num){
		  ArrayList<String> arstr = new ArrayList<String>();
		  StringBuffer code = new StringBuffer();
		  arstr.addAll(Arrays.asList(capital));
		  
		  for (int i = 0; i < num; i++) {
		   code.append(arstr.get(random.nextInt(arstr.size())));
		  }
		  return code.toString();
		 }
	 

	 /**
	  * 
	  * Func: num -> 随机字符，breath -> 个，depth ->层，filename -> 文件名
	  * Data:2020-03-15 Time:下午5:26:51
	  * @param num
	  * @param breath
	  * @param depth
	  * @param filename
	  * @return
	  */
	public static String[] CreateFileName(int num,int breath,int depth,String[] filename) {
		String[] str = new String[breath];
		//只有一层 直接打印
		
		//list 记录当前层的结果
		List<String[]> list=new ArrayList<String[]>();
		
		if(depth == 1) {
				for(String lfile:filename) {
					str=createFile(num,breath,lfile);
				}
				return str;
		}else {
			//打印当前层
			for(String lstr:filename) {
				str=createFile(num,breath,lstr);
				list.add(str);
			}	
			
			for(String[] lstr :  list) {
				//将当前层结果给str
				str=CreateFileName(num,breath,depth-1,lstr);
//				list.add(str);
			}
				//打印下一层
		}
		return str;
	}
	
	

	public static String[] createFile(int num,int breath,String filename) {
		 String[] istr=new String[breath];
		 
		 for (int i = 0; i < breath; i++) {
			istr[i]=filename+getRandom(num)+"-"+i+"/";
			File file= new File(istr[i]);
			if(!file.exists()&&!file.isDirectory()){//如果文件夹不存在
				System.out.print(istr[i]+"\n");
				file.mkdirs();//创建文件夹
				}
		 }
		 return istr;
	}

	/**
	 * 
	 * Func:加密方式 针对文件内容 随机key 随机个字节取反，在文件首追加文件头信息个字节，
	 *    加密的工具，要考虑实现的功能情况:
	 *	 1、文件只有没加密的情况下才能进行加密。
	 *   2、文件之后再加密的情况下才能进行解密。
	 *   3、必须能够判断密钥是否正确，因为如果用一个假的密钥进行解密的话会将原本的数据毁掉。
	 *	 4、递归加密的时候必须能够忽略掉那些已经加密的文件，解密的时候忽略掉那些没有加密的文件。
	 * Data:2020-03-15 Time:下午5:29:01
	 * @param s_File
	 ** key 小于  ~128  127
	 * @throws IOException
	 */
	public static void encryption(String s_File) throws IOException {
		
		File inFile = new File(s_File);
		
		byte[] b = null;
		Random r = new Random();
		int key = 0;
		int num = 0;
		if(!inFile.exists()) {
			System.out.println("文件不存在");
			return;
		}
		if(inFile.length() > (1L << 30)) {
			System.out.println("文件大于1G");
			
			RandomAccessFile sFRandomAccessFile = new RandomAccessFile(s_File, "rw");			
			//key 加密的秘钥
			key = 1;
			//num 待加密的字节数
			num = r.nextInt(1000);
			if(inFile != null) {
				if( num > inFile.length()) {
					b = new byte[(int)inFile.length()];
				}else {
					b = new byte[num];
				}
			}
//			byte[] bt = new byte[500];
//			sFRandomAccessFile.seek(sFRandomAccessFile.length()-500);
//			int in= sFRandomAccessFile.read(bt);
//			System.out.println("in:"+in);
//
			sFRandomAccessFile.close();
//			
//			// 对文件大于1G的文件   加文件尾 加密字节
//			if((new String(bt)).contains("wuyun,.tianxia")) {
//				System.out.println("File haved been encryped"+ s_File);
//				return ;
//			}
			//文件首 加密字节
			RandomAccessFile output = new RandomAccessFile(s_File, "rw");
			FileInputStream input = new FileInputStream(s_File);
			int keyvalue=1;
			for(int i=0;i < b.length;i++){
	            int temp = input.read();
	            output.write(temp^keyvalue);
	        }
			
			input.close();
			output.close();
//			// 追加文件信息
			String d_File = inFile.getParent()+"\\"+getRandom(8);
	    	String strtail = "wuyun,.tianxia" + "$&"+ s_File + "$&" + key + "$&" + num +"$&" ; // 添加的尾部内容
	    	System.out.println("strtail:"+strtail);
//	    	output.seek(output.length());
//	    	output.write(strtail.getBytes());
			
	    	//产生加密信息文件
	    	bigFileCreatTemp(s_File, strtail);
	    	
	    	reFileName(s_File+".temp",d_File+".temp");
	    	reFileName(s_File, d_File);
	    	
	    	
	    	/*****小文件操作****/
		}else {
			System.out.println("文件小于1G");
			
			FileInputStream input = new FileInputStream(inFile);
			//key 加密的秘钥
			key = 1;
			//num 待加密的字节数
			num = r.nextInt(1000);
			if(inFile != null) {
				if( num > inFile.length()) {
					b = new byte[(int)inFile.length()];
				}else {
					b = new byte[num];
				}
			}
			input.read(b);
			input.close();
			
			//判断该文件是否被加密 如果已被加密 则直接返回
			if((new String(b)).contains("yundian,.tianxia")) {
				System.out.println("File haved been encryped"+ s_File);
				return ;
			}
			
			// 文件小于1G的文件  加文件头 做字节加密操作
			RandomAccessFile output = new RandomAccessFile(s_File, "rw");
			int keyvalue=1;
			for(int i=0;i < b.length;i++){
	            int temp = b[i];
	            output.write(temp^keyvalue);
	        }
			output.close();

			// 追加头文件信息
			String d_File = inFile.getParent()+"/"+getRandom(8);
	    	String strHead = "yundian,.tianxia" + "$&"+ s_File + "$&" + key + "$&" + num +"$&" ; // 添加的头部内容
	    	littleFileAddHead(s_File, 0, strHead);
	    	reFileName(s_File,d_File);
	    	
		}
	}
	
//	public static void Archive(File inFile,int key) throws IOException {
//		
//		FileInputStream input = new FileInputStream(inFile);
//		int content= input.read();
//		input.close();
//		
//		inFile = new File(inFile.getName());
//		RandomAccessFile output = new RandomAccessFile(inFile.getName(), "rw");
//		output.write(content^key);
//		output.close();
//	}
	/**
	 * 
	 * Func: 解密程序
	 * Data:2020-03-15 Time:下午5:47:59
	 * @param s_File 解析源文件的头
	 * @throws Exception 
	 * @throws IOException
	 */
//	public static void decryption(String s_File,int key) throws IOException {
//		
//		File inFile = new File(s_File);
//		FileInputStream input = new FileInputStream(inFile);
//		int content= input.read();
//		input.close();
//		
//		inFile = new File(s_File);
//		RandomAccessFile output = new RandomAccessFile(s_File, "rw");
//		output.write(content^key);
//		output.close();
//	}
	
	/**
	 * 
	 * Func:分析文件头1024个字节取出要素
	 * Data: 2020-03-24
	 * @param s_File
	 * @param split
	 * @param spitstr
	 * @return 0 小文件   1 大文件  2 新文件 无需解压
	 * @throws Exception
	 */
	public static int flagEncryFile(String s_File,String split,ArrayList<String> spitstr) throws Exception {
		FileReader fr= new FileReader(s_File);
		char[] buf = new char[1024];
		
		fr.read(buf);
		//对字符串进一步处理
		String[] sptstr = String.valueOf(buf).split(split);
		for(String str:sptstr) {
			spitstr.add(str);
			System.out.println("str: "+str);
		}
		fr.close();
		if(spitstr.size()>=4 && spitstr.get(0).contains("yundian,.tianxia")) {
			System.out.println("小文件 已加密");
			return 0;
		}else {
			spitstr.clear();
			sptstr = null;
			
			//从配置文件获取文件头
			fr= new FileReader(s_File+".temp");
			buf = new char[512];
			fr.read(buf);
			//对字符串进一步处理
			sptstr = String.valueOf(buf).split(split);
			for(String str:sptstr) {
				spitstr.add(str);
				System.out.println("str: "+str);
			}
			fr.close();
			if(spitstr.size()>=4 && (spitstr.get(0)).contains("wuyun,.tianxia")) {
				return 1;
			}
			
//			RandomAccessFile output = new RandomAccessFile(s_File, "rw");
//			System.out.println("output.length():"+output.length());
//			if((output.length()-1024)>0) {
//				output.seek(output.length()-1024);
//			}else {
//				output.seek(0);
//			}
//			System.out.println("FilePointer: "+output.getFilePointer());
//			byte[] bt = new byte[1024];
//			int tm = output.read(bt);
//			System.out.println("bt:"+new String(bt)+"tm:"+tm);
//			sptstr = (new String(bt)).split(split);
//			for(String str:sptstr) {
//				spitstr.add(str);
//				System.out.println("str: "+str);
//			}
//			output.close();
//			if(spitstr.size()>=4 && (new String(bt)).contains("wuyun,.tianxia")) {
//				System.out.println("大文件 已加密");
//				return 1;
//			}
		}
		return 2;
	}
	
	/**
	 * Func: 解析文件头，判断是否需要解压，如果不需要直接返回return
	 * @param s_File
	 * @throws Exception
	 */
	public static void decryption(String s_File) throws Exception {

		File inFile= new File(s_File);
		String filename = null;
		String key = null;
		String num = null;
		ArrayList<String> list = new ArrayList<String> ();
		if(null == inFile || !inFile.exists()) {
			return;
		}
		int flag = flagEncryFile(s_File,"\\$&", list);
		
		//判断是否是小文件 判定文件头
		if(flag == 0) {
			
			System.out.println("小文件");
			filename = list.get(1);
			key = list.get(2);
			num = list.get(3);
			//移除小文件头
			littleFileRemoveHead(s_File, s_File+"1",list);
			inFile.deleteOnExit();
			reFileName(s_File+"1",filename);
			
			
		//判断是否是大文件 判定是否存在 加密信息文件
		}else if(flag == 1){			
			System.out.println("大文件");
//			filename = new String(list.get(1).getBytes("UTF-8"),"UTF-8");
//			System.out.println("filename:"+filename);
//			key = list.get(2);
//			num = list.get(3);
//			//移除大文件尾
//			RandomAccessFile rs=new RandomAccessFile(s_File, "rw");
//			String str = "wuyun,.dianxia" +"$&"+list.get(1) + "$&" + list.get(2) + "$&" + list.get(3) + "$&"; // 尾部内容
//			rs.seek(rs.length()-str.getBytes().length);
//			while(rs.getFilePointer()< rs.length()) {
//				rs.write('\0');
//			}
//			rs.close();


			
			filename=list.get(1);
			key = list.get(2);
			num = list.get(3);
			File temp = new File(s_File+".temp");
			temp.delete();
			inFile.deleteOnExit();
			reFileName(s_File,filename);
			
		}else {
			System.out.println("无需解压");
			return;
		}
		
		//解密字符串
		RandomAccessFile output = new RandomAccessFile(filename, "rw");
		inFile = new File(filename);
		FileInputStream input = new FileInputStream(inFile);
		byte[] b = null;
		if(inFile != null) {
			if( Integer.valueOf(num) > inFile.length()) {
				b = new byte[(int)inFile.length()];
			}else {
				b = new byte[Integer.valueOf(num)];
			}
		}
		int in = input.read(b);
		input.close();
		int keyvalue = 1;
		for(int i=0;i < b.length;i++){
            int temp = b[i];
            output.write(temp^keyvalue);
        }
		output.close();
		
	}
//	public static void UnArchive(File inFile,int key) throws IOException {
//		
//		FileInputStream input = new FileInputStream(inFile);
//		int content= input.read();
//		input.close();
//		
//		inFile = new File(inFile.getName());
//		RandomAccessFile output = new RandomAccessFile(inFile.getName(), "rw");
//		output.write(content^key);
//		output.close();
//	}
	
    /**
     * Func: 修改文件夹路径直接修改文件名
     * Data:2020-03-03 Time:下午8:26:33
	 * @param srcFolderPath 需要修改的文件夹的完整路径
	 * @param desFolderName 需要修改的文件夹的名称
     * @return
     */
    public static Boolean reFileDirName(String srcFolderPath, String desFolderName) {
        File f = new File(srcFolderPath);
        
        // 判断原文件是否存在（防止文件名冲突）
        if (!f.exists()) { 
            return false;
        }
        
        //目标文件名不为空
        if(desFolderName != null ) {
        	desFolderName = desFolderName.trim();
        }else if("".equals(desFolderName)) {
        	return false;
        }
        
        // 判断是否为文件夹
        String newFilePath = null;
        if (f.isDirectory()) { 
            newFilePath =  desFolderName;
        } else {
        	throw new RuntimeException(srcFolderPath +"---Is Not A DirPath");
        }
        File nf = new File(newFilePath);
        try {
        	// 修改文件名
            if(f.renameTo(nf)) {
            	System.out.println("Renamed Suced---"+nf.getName());
            	return true;
            }else{
            	System.out.println("Renamed Failed---"+nf.getName());
            }
        } catch (Exception err) {
            err.printStackTrace();
            return false;
        }
        return false;
    }
    /**
     * Func:
     * @param srcFilePath
     * @param desFileName
     * @return
     */
    public static Boolean reFileName(String srcFilePath, String desFileName) {
        File f = new File(srcFilePath);
        
        // 判断原文件是否存在（防止文件名冲突）
        if (!f.exists()) { 
            return false;
        }
        
        //目标文件名不为空
        if(desFileName != null ) {
        	desFileName = desFileName.trim();
        }else if("".equals(desFileName)) {
        	return false;
        }
        
        // 判断是否为文件夹
        String newFilePath = null;
        if (f.isFile()) { 
            newFilePath =  desFileName;
        } else {
        	throw new RuntimeException(srcFilePath +"---Is Not A Filename");
        }
        File nf = new File(newFilePath);
        try {
        	// 修改文件名
            if(f.renameTo(nf)) {
            	System.out.println("Renamed Suced---"+nf.getName());
            	return true;
            }else{
            	System.out.println("Renamed Failed---"+nf.getName());
            }
        } catch (Exception err) {
            err.printStackTrace();
            return false;
        }
        return false;
    }
    /**
     * 
     * Func: 文件夹选择器,默认桌面路径
     * Data:2020-03-03 Time:下午8:47:37
     * @return
     */
    public static String folderChooser() {
		
    	int result = 0;
        String path = "";
        
        JFileChooser fileChooser = new JFileChooser();
        FileSystemView fsv = FileSystemView.getFileSystemView();  //注意了，这里重要的一句
        path=fsv.getHomeDirectory().getAbsolutePath();
        System.out.println(fsv.getHomeDirectory());                //得到桌面路径
//        fileChooser.setCurrentDirectory(new File(path));
        fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
        fileChooser.setDialogTitle("请选择文件夹...");
        fileChooser.setApproveButtonText("确定");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); //文件夹
        result = fileChooser.showOpenDialog(null); //显示位置 屏幕中央  
        if (JFileChooser.APPROVE_OPTION == result) { //选择确认返回值
           path=fileChooser.getSelectedFile().getPath();
           System.out.println("path: "+path);
           return path;
        }
        
        return null;
    }
    
    /**
     * 
     * Func:遍历所有的文件或文件夹  1文件  2文件夹  3文件以及文件夹
     * Data:2020-03-14 Time:上午10:48:08
     * @param path
     * @param listFileName
     * @param type
     */
	public  static void getAllFileName(String path,ArrayList<String> listFileName, int type){
		File file = new File(path);
		File [] files = file.listFiles();

		switch(type) {
			case 1:
				for(File a:files){
					if(a.isDirectory()){
						getAllFileName(a.getAbsolutePath()+"/",listFileName,1);
					}else if(a.isFile()) {
						listFileName.add(a.getName());
					}
				}
				break;
			case 2:
				for(File a:files){
					if(a.isDirectory()){//如果文件夹下有子文件夹，获取子文件夹下的所有文件全路径。
						listFileName.add(a.getName());
						getAllFileName(a.getAbsolutePath()+"/",listFileName,2);
					}
				}
				break;
			case 3:
				String [] names = file.list();
//				if(names != null){
//					String [] completNames = new String[names.length];
//					for(int i=0;i<names.length;i++){
//						completNames[i]=path+names[i];
//					}
//					listFileName.addAll(Arrays.asList(completNames));
					for(File a:files){
						listFileName.add(a.getName());
						if(a.isDirectory()){
							getAllFileName(a.getAbsolutePath()+"/",listFileName,3);
						}
					}
				break;
			default:
				
		}
	}
	
	/**
	 * 
	 * Func: 选择文件夹路径，根据遍历深度，返回所有的文件夹名称(不含路径信息) 含当前文件夹 
	 * Data: 2020-03-23
	 */
	public  static void getFolderName(String folderpath,ArrayList<String> listFolderName, int bread){
		File file = new File(folderpath);
		File [] files = file.listFiles();
		if(bread == 0) {
			return ;
		}
		if(file.isDirectory()) {
			bread--;
			listFolderName.add(file.getName());
			for(File a:files){
				if(a.isDirectory()){//如果文件夹下有子文件夹，获取子文件夹下的所有文件全路径。
					listFolderName.add(a.getName());
					getAllFileName(a.getAbsolutePath()+"/",listFolderName, bread);
				}
			}
		}
	}
	
	/**
	 * 
	 * Func: 关闭文件窗口方式 循环遍历
	 * Data: 2020-03-22
	 */
	public static void closeWinFolder() {
		
		final String path = folderChooser();
		
		new Thread(){
			public void run(){
				while(true) {
					ArrayList<String> listFileName = new ArrayList<String>();
					//往下遍历一层
					getFolderName(path, listFileName, 1);
//					CloseWinFolder closeWinFolder = new CloseWinFolder();
					listFileName.add(path);
					for(String foldername: listFileName) {
						System.out.println("foldername:"+foldername);
						CloseWinFolder.closewindow(foldername);
						try {
							TimeUnit.MILLISECONDS.sleep(1);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}	
		}.start();
	}

			/**
			 * Func: 去除文件头
			 * @param filename
			 * @param list
			 */
		    public static  void bigFileRemoveHead(String filename, ArrayList<String> list){

		    	String strHead = list.get(0)+"$&"+list.get(1) + "$&" + list.get(2) + "$&" + list.get(3) + "$&"; // 添加的头部内容
		        String srcFilePath = filename;
		        String destFilePath = list.get(1)+"-1";
		        long startTime = System.currentTimeMillis();
		        try {
		            // 映射原文件到内存
		            RandomAccessFile srcRandomAccessFile = new RandomAccessFile(srcFilePath, "rw");
		            FileChannel srcAccessFileChannel = srcRandomAccessFile.getChannel();
		            long srcLength = srcAccessFileChannel.size()-strHead.length();
		            System.out.println("src file size:" + srcLength);  // src file size:296354010
		            MappedByteBuffer  srcMap = null;
		            /** 判断是否大于2G **/
		            if(srcLength > (1L << 31)) {
		            	long cur = 0L;
		            	long length = 1L << 29;
		            	Random srcrandom = new Random();
		            	try {
		            		cur = strHead.length();
		            		while(cur < (1L << 31) ) {
		            			srcMap = srcAccessFileChannel.map(FileChannel.MapMode.READ_WRITE, cur, length);
		                        IntBuffer intBuffer = srcMap.asIntBuffer();
		                        while ( intBuffer.position() < intBuffer.capacity() ){
		                        	intBuffer.put( srcrandom.nextInt() );
		                        }
		                        cur += length;
		            		}
		            		
		            	}catch(IOException e) {
		            		e.printStackTrace();
		            	}
		            /** 小于2G的文件,直接读入内存 **/
		            }else {
		            	srcMap = srcAccessFileChannel.map(FileChannel.MapMode.READ_WRITE,strHead.length() , srcLength);
		            }
		 
		           // 映射目标文件到内存
		            RandomAccessFile destRandomAccessFile = new RandomAccessFile(destFilePath, "rw");
		            FileChannel destAccessFileChannel = destRandomAccessFile.getChannel();
		            long destLength = srcLength;
		            System.out.println("dest file size:"+ destLength);  // dest file size:296354025
		            MappedByteBuffer destMap = null;
		            /** 判断是否大于2G **/
		            if(destLength > (1L << 31)) {
		            	long cur = 0L;
		            	long length = 1L << 29;
		            	Random desrandom = new Random();
		            	try {
		            		while(cur < (1L << 31) ) {
		            			destMap = destAccessFileChannel.map(FileChannel.MapMode.READ_WRITE, cur, length);
		                        IntBuffer intBuffer = destMap.asIntBuffer();
		                        while ( intBuffer.position() < intBuffer.capacity() ){
		                        	intBuffer.put( desrandom.nextInt() );
		                        }
		                        cur += length;
		            		}
		            	}catch(IOException e) {
		            		e.printStackTrace();
		            	}
		            /** 小于2G的文件,直接读入内存 **/
		            }else {
		            	destMap = destAccessFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, destLength);
		            }
		 
		            // 开始文件追加 : 减去头部内容，再添加原来文件内容
		            destMap.position(0);
		            destMap.put(srcMap);
		            destAccessFileChannel.close();
		            srcRandomAccessFile.close();
		            destRandomAccessFile.close();
		            System.out.println("dest real file size:"+new RandomAccessFile(destFilePath,"r").getChannel().size());
		            System.out.println("total time :" + (System.currentTimeMillis() - startTime));// 貌似时间不准确，异步操作？
		        } catch (FileNotFoundException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }	
		    /**
		     * Func: 小文件移除头文件块
		     * @param filename
		     * @param list
		     */
		    public static  void littleFileRemoveHead(String filename, String desfile, ArrayList<String> list) throws IOException{

		    		String strHead = list.get(0)+"$&"+list.get(1) + "$&" + list.get(2) + "$&" + list.get(3) + "$&"; // 添加的头部内容
		    		int pos = 0;
//		            File tmp=File.createTempFile("tmp", null);
		    		File tmp = new File(desfile);
//					tmp.deleteOnExit();
					//使用临时文件保存插入点后的数据
					RandomAccessFile raf=new RandomAccessFile(filename, "rw");
					FileOutputStream out=new FileOutputStream(tmp);
					FileInputStream in=new FileInputStream(tmp);
					raf.seek(strHead.getBytes().length);
					//----------下面代码将插入点后的内容读入临时文件中保存----------
					byte[] bbuf=new byte[4096*10];
					//用于保存实际读取的字节数
					int hasRead = 0;
					//使用循环方式读取插入点后的数据
					while((hasRead=raf.read(bbuf))>0){
						//将读取的数据写入临时文件
						out.write(bbuf,0,hasRead);
					}
					//-----------下面代码用于插入内容----------
					//把文件记录指针重写定位到pos位置
					raf.seek(pos);

				    //追加临时文件中的内容
					while((hasRead=in.read(bbuf))>0){
						raf.write(bbuf,0, hasRead);
					}
					raf.close();
					out.close();
					in.close();
					
		    }	
		    

		/**
		 * 
		 * Func:往文件头里写入特定字节数的内容
		 * Data:2020-03-16 Time:下午8:07:28
		 * key : ~128 127
		 * num : 加密字节数
		 */
	    public static  void bigFileAddHead(String srcFile,String desFile,String strHead){
	        // 将282兆的文件内容头部添加一行字符  "This is a head!"
	     
//	        String srcFilePath = "D:/BaiduNetdiskDownload/test/4.html的文档设置标记上（格式标记）.mp4" ; // 原文件路径
//	        String srcFilePath = "D:/BaiduNetdiskDownload/test/1.txt" ; // 原文件路径
	        String srcFilePath = srcFile;
	        String destFilePath = desFile; // 添加头部后文件路径 （最终添加头部生成的文件路径）
	        long startTime = System.currentTimeMillis();
	        try {
	            // 映射原文件到内存
	            RandomAccessFile srcRandomAccessFile = new RandomAccessFile(srcFilePath, "rw");
	            FileChannel srcAccessFileChannel = srcRandomAccessFile.getChannel();
	            long srcLength = srcAccessFileChannel.size();
	            System.out.println("src file size:" + srcLength);  // src file size:296354010
	            MappedByteBuffer  srcMap = null;
	            /** 判断是否大于2G **/
	            if(srcLength > (1L << 30)) {
	            	long cur = 0L;
	            	long length = 1L << 29;
	            	Random srcrandom = new Random();
	            	try {
	            		while(cur < (1L << 31) ) {
	            			srcMap = srcAccessFileChannel.map(FileChannel.MapMode.READ_WRITE, cur, length);
	                        IntBuffer intBuffer = srcMap.asIntBuffer();
	                        while ( intBuffer.position() < intBuffer.capacity() ){
	                        	intBuffer.put( srcrandom.nextInt() );
	                        }
	                        cur += length;
	            		}
	            		
	            	}catch(IOException e) {
	            		e.printStackTrace();
	            	}
	            /** 小于2G的文件,直接读入内存 **/
	            }else {
	            	srcMap = srcAccessFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, srcLength);
	            }
	 
	           // 映射目标文件到内存
	            RandomAccessFile destRandomAccessFile = new RandomAccessFile(destFilePath, "rw");
	            FileChannel destAccessFileChannel = destRandomAccessFile.getChannel();
	            long destLength = srcLength + strHead.getBytes().length;
	            System.out.println("dest file size:"+ destLength);  // dest file size:296354025
	            MappedByteBuffer destMap = null;
	            /** 判断是否大于2G **/
	            if(destLength > (1L << 31)) {
	            	long cur = 0L;
	            	long length = 1L << 29;
	            	Random desrandom = new Random();
	            	try {
	            		while(cur < (1L << 31) ) {
	            			destMap = destAccessFileChannel.map(FileChannel.MapMode.READ_WRITE, cur, length);
	                        IntBuffer intBuffer = destMap.asIntBuffer();
	                        while ( intBuffer.position() < intBuffer.capacity() ){
	                        	intBuffer.put( desrandom.nextInt() );
	                        }
	                        cur += length;
	            		}
	            	}catch(IOException e) {
	            		e.printStackTrace();
	            	}
	            /** 小于2G的文件,直接读入内存 **/
	            }else {
	            	destMap = destAccessFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, destLength);
	            }
	 
	            // 开始文件追加 : 先添加头部内容，再添加原来文件内容
	            destMap.position(0);
	            destMap.put(strHead.getBytes());
	            destMap.put(srcMap);

	            // 加上这几行代码,手动unmap 
	    
	            srcMap.clear();
	            destMap.clear();
	            destAccessFileChannel.close();
	            srcAccessFileChannel.close();
	            srcRandomAccessFile.close();
	            destRandomAccessFile.close();
	            
	            System.out.println("dest real file size:"+new RandomAccessFile(destFilePath,"r").getChannel().size());
	            System.out.println("total time :" + (System.currentTimeMillis() - startTime));// 貌似时间不准确，异步操作？
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

		/**
		 * Func:  大文件(大于1G)  临时文件配置情况
		 * @param fileName
		 * @param insertContent
		 * @throws IOException
		 */
		public static void bigFileCreatTemp(String fileName,String insertContent) throws IOException{
			File temp= new File(fileName +".temp");
			String sets = "attrib +H +S "+ temp.getAbsolutePath();
			Runtime.getRuntime().exec(sets);
//			temp.deleteOnExit();
			//使用临时文件保存插入点后的数据
			RandomAccessFile raf=new RandomAccessFile(temp, "rw");
			//-----------下面代码用于插入内容----------
			//把文件记录指针重写定位到pos位置
			raf.seek(0);
			//追加需要插入的内容
			raf.write(insertContent.getBytes());

			raf.close();
		}		
		/**
		 * Func:  小文件(小于1G)  插入文件头
		 * @param fileName
		 * @param pos
		 * @param insertContent
		 * @throws IOException
		 */
		public static void littleFileAddHead(String fileName,long pos,String insertContent) throws IOException{
			File tmp=File.createTempFile("tmp", null);
			tmp.deleteOnExit();
			//使用临时文件保存插入点后的数据
			RandomAccessFile raf=new RandomAccessFile(fileName, "rw");
			FileOutputStream out=new FileOutputStream(tmp);
			FileInputStream in=new FileInputStream(tmp);
			raf.seek(pos);
			//----------下面代码将插入点后的内容读入临时文件中保存----------
			byte[] bbuf=new byte[4096*10];
			//用于保存实际读取的字节数
			int hasRead =0;
			//使用循环方式读取插入点后的数据
			while((hasRead=raf.read(bbuf))>0){
				//将读取的数据写入临时文件
				out.write(bbuf,0,hasRead);
			}
			//-----------下面代码用于插入内容----------
			//把文件记录指针重写定位到pos位置
			raf.seek(pos);
			//追加需要插入的内容
			raf.write(insertContent.getBytes());
		    //追加临时文件中的内容
			while((hasRead=in.read(bbuf))>0){
				raf.write(bbuf,0, hasRead);
			}
			raf.close();
			out.close();
			in.close();
		}
			
	    	// 缓存文件头信息-文件头信息
	    	public static final HashMap<String, String> mFileTypes = new HashMap<String, String>();
	    	static {
	    		// images
	    		mFileTypes.put("FFD8FFE0", "jpg");
	    		mFileTypes.put("89504E47", "png");
	    		mFileTypes.put("47494638", "gif");
	    		mFileTypes.put("49492A00", "tif");
	    		mFileTypes.put("424D", "bmp");
	    		//
	    		mFileTypes.put("41433130", "dwg"); // CAD
	    		mFileTypes.put("38425053", "psd");
	    		mFileTypes.put("7B5C727466", "rtf"); // 日记本
	    		mFileTypes.put("3C3F786D6C", "xml");
	    		mFileTypes.put("68746D6C3E", "html");
	    		mFileTypes.put("44656C69766572792D646174653A", "eml"); // 邮件
	    		mFileTypes.put("D0CF11E0", "doc");
	    		mFileTypes.put("5374616E64617264204A", "mdb");
	    		mFileTypes.put("252150532D41646F6265", "ps");
	    		mFileTypes.put("255044462D312E", "pdf");
	    		mFileTypes.put("504B0304", "docx");
	    		mFileTypes.put("52617221", "rar");
	    		mFileTypes.put("57415645", "wav");
	    		mFileTypes.put("41564920", "avi");
	    		mFileTypes.put("2E524D46", "rm");
	    		mFileTypes.put("000001BA", "mpg");
	    		mFileTypes.put("000001B3", "mpg");
	    		mFileTypes.put("6D6F6F76", "mov");
	    		mFileTypes.put("3026B2758E66CF11", "asf");
	    		mFileTypes.put("4D546864", "mid");
	    		mFileTypes.put("1F8B08", "gz");
	    		mFileTypes.put("4D5A9000", "exe/dll");
	    		mFileTypes.put("75736167", "txt");
	    	}
	    	/**
	    	 * 
	    	 * @param buffer
	    	 * @param channelClass
	    	 */
	        public static void unMapBuffer(MappedByteBuffer buffer, Class channelClass) {
	            if (buffer == null) {
	                return;
	            }
	         
	            try {
	                Method unmap = channelClass.getDeclaredMethod("unmap", MappedByteBuffer.class);
	                unmap.setAccessible(true);
	                unmap.invoke(channelClass, buffer);
	            } catch (NoSuchMethodException e) {
	                e.printStackTrace();
	            } catch (IllegalAccessException e) {
	                e.printStackTrace();
	            } catch (InvocationTargetException e) {
	                e.printStackTrace();
	            }
	        }
	    	/**
	    	 * 根据文件路径获取文件头信息
	    	 * 
	    	 * @param filePath
	    	 *            文件路径
	    	 * @return 文件头信息
	    	 */
	    	public static String getFileType(String filePath) {
//	    		System.out.println(getFileHeader(filePath));
//	    		System.out.println(mFileTypes.get(getFileHeader(filePath)));
	    		return mFileTypes.get(getFileHeader(filePath));
	    	}
	     
	    	/**
	    	 * 根据文件路径获取文件头信息
	    	 * @param filePath  文件路径
	    	 * @return 文件头
	    	 */
	    	public static String getFileHeader(String filePath) {
	    		FileInputStream is = null;
	    		String value = null;
	    		try {
	    			is = new FileInputStream(filePath);
	    			byte[] b = new byte[4];
	    			/*
	    			 * int read() 从此输入流中读取一个数据字节。 int read(byte[] b) 从此输入流中将最多 b.length
	    			 * 个字节的数据读入一个 byte 数组中。 int read(byte[] b, int off, int len)
	    			 * 从此输入流中将最多 len 个字节的数据读入一个 byte 数组中。
	    			 */
	    			is.read(b, 0, b.length);
	    			value = bytesToHexString(b);
	    		} catch (Exception e) {
	    		} finally {
	    			if (null != is) {
	    				try {
	    					is.close();
	    				} catch (IOException e) {
	    				}
	    			}
	    		}
	    		return value;
	    	}
	     
	    	/**
	    	 * 将要读取文件头信息的文件的byte数组转换成string类型表示
	    	 * 
	    	 * @param src
	    	 *            要读取文件头信息的文件的byte数组
	    	 * @return 文件头信息
	    	 */
	    	private static String bytesToHexString(byte[] src) {
	    		StringBuilder builder = new StringBuilder();
	    		if (src == null || src.length <= 0) {
	    			return null;
	    		}
	    		String hv;
	    		for (int i = 0; i < src.length; i++) {
	    			// 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
	    			hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
	    			if (hv.length() < 2) {
	    				builder.append(0);
	    			}
	    			builder.append(hv);
	    		}
//	    		System.out.println(builder.toString());
	    		return builder.toString();
	    	}

	public static void main(String[] args) throws Exception {


		packege();
		unPackege();
//		closeWinFolder();
//		String s_File = "C:/Users/wyy/Desktop/ComicClient/test01/UPCYFHER";
//		RandomAccessFile output = new RandomAccessFile(s_File, "rw");
//		output.seek(output.length()-300);
//		byte[ ] b = new byte[300];
//		while(output.getFilePointer()< output.length()) {
//			System.out.println(output.getFilePointer());
//			int i = output.read(b);
//			System.out.println("output.read:"+ new String(b));
//			System.out.println("output.i:"+i);
//		};

	}
}