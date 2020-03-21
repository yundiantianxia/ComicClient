package com.yuntech.encryption;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class FileUtil {


	
	/**
	 * 解包方法：解包指定文件到指定文件夹下
	 * @throws IOException 
	 */
	public static boolean unPackege() throws IOException {
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
        		throw new RuntimeException(lfs.getName()+" Is No Exist");
        	}
        	if(lfs.isDirectory()&&lfs.getPath().contains(mode[1])) {
        		Runtime.getRuntime().exec("attrib " +lfs.getPath()+ " -S -H ");
        		System.out.println(lfs.getPath() + " -S -H Suced");
        		if(!reFileDirName(lfs.getPath(),lfs.getPath().replace(mode[1], ""))) {
        			System.out.println(lfs.getPath().replace(mode[1], "") + " -S -H Failed");
        			return false;
        		}
    			
        	}else if(lfs.isFile()){
        		decryption(lfs.getPath(), 1);
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
	        		Runtime.getRuntime().exec("attrib "+ lfs.getPath()+mode[1] + " +S +H " );
	    			System.out.println(lfs.getPath() + "---Haved Be S H  Suced To---" + lfs.getPath()+mode[1]);
	        	}else if(lfs.isFile()){
	        		encryption(lfs.getPath(), 1, 1);
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
	  * Func: 随机字符，breath个，depth层，文件名
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
	 * Func:加密方式 针对文件内容前num个字节取反，在文件头随机加key个字节，
	 *    加密的工具，要考虑实现的功能情况:
	 *	 1、文件只有没加密的情况下才能进行加密。
	 *   2、文件之后再加密的情况下才能进行解密。
	 *   3、必须能够判断密钥是否正确，因为如果用一个假的密钥进行解密的话会将原本的数据毁掉。
	 *	 4、递归加密的时候必须能够忽略掉那些已经加密的文件，解密的时候忽略掉那些没有加密的文件。
	 * Data:2020-03-15 Time:下午5:29:01
	 * @param s_File
	 * @param key
	 * @throws IOException
	 */
	public static void encryption(String s_File,int key,int num) throws IOException {
		
		File inFile = new File(s_File);
		FileInputStream input = new FileInputStream(inFile);
		byte[] b = null;;
		if(inFile != null) {
			if( num > inFile.length()) {
				b = new byte[(int)inFile.length()];
			}else {
				b = new byte[num];
			}
		}
		int in = input.read(b);
		input.close();
		
		inFile = new File(s_File);
		RandomAccessFile output = new RandomAccessFile(s_File, "rw");
		for(int i=0;i < b.length;i++){
            int temp = b[i];
            b[i] = (byte) (temp^key);
        }
		output.write(b);
		output.close();
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
	 * @param s_File
	 * @param key
	 * @throws IOException
	 */
	public static void decryption(String s_File,int key) throws IOException {
		
		File inFile = new File(s_File);
		FileInputStream input = new FileInputStream(inFile);
		int content= input.read();
		input.close();
		
		inFile = new File(s_File);
		RandomAccessFile output = new RandomAccessFile(s_File, "rw");
		output.write(content^key);
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
     * Func: 通过文件夹选择器修改文件夹路径直接修改文件名
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
     * 
     * Func: 文件夹选择器,默认桌面路径
     * Data:2020-03-03 Time:下午8:47:37
     * @param src
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
     * Func:遍历所有的文件或文件夹  0文件  1文件夹  2文件以及文件夹
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
	
	public static void closeWinFolder() {
		
		String path = folderChooser();
		
		new Thread(){
			public void run(){
				while(true) {
					ArrayList<String> listFileName = new ArrayList<String>();
					getAllFileName(path, listFileName, 2);
					CloseWinFolder closeWinFolder = new CloseWinFolder();
					for(String foldername: listFileName) {
						System.out.println(foldername+":"+closeWinFolder.runbatt(foldername));
					}
				}
			}	
		}.start();
	}
	
/**
 * 
 * Func:往文件头里写入特定字节数的内容
 * Data:2020-03-16 Time:下午8:07:28
 */
	    public static  void bigFileAddHead(String filename){
	        // 将282兆的文件内容头部添加一行字符  "This is a head!"
	        String strHead = "1234567890" ; // 添加的头部内容
	        String srcFilePath = "D:/BaiduNetdiskDownload/test/4.html的文档设置标记上（格式标记）.mp4" ; // 原文件路径
//	        String srcFilePath = "D:/BaiduNetdiskDownload/test/134.txt" ; // 原文件路径
	        String destFilePath = "D:/BaiduNetdiskDownload/test/134-1.txt" ; // 添加头部后文件路径 （最终添加头部生成的文件路径）
	        long startTime = System.currentTimeMillis();
	        try {
	            // 映射原文件到内存
	            RandomAccessFile srcRandomAccessFile = new RandomAccessFile(srcFilePath, "rw");
	            FileChannel srcAccessFileChannel = srcRandomAccessFile.getChannel();
	            long srcLength = srcAccessFileChannel.size();
	            System.out.println("src file size:" + srcLength);  // src file size:296354010
	            MappedByteBuffer  srcMap = null;
	            /** 判断是否大于2G **/
	            if(srcLength > (1L << 31)) {
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
	            destAccessFileChannel.close();
	            srcRandomAccessFile.close();
	            System.out.println("dest real file size:"+new RandomAccessFile(destFilePath,"r").getChannel().size());
	            System.out.println("total time :" + (System.currentTimeMillis() - startTime));// 貌似时间不准确，异步操作？
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
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
		
//		final String fileType = getFileType("C:/Users/wyy/Desktop/ComicClient/1.txt");
//		System.out.println(fileType);

  		encryption("C:/Users/wyy/Desktop/ComicClient/1.txt", 98, 22);
//		encryption("D:/BaiduNetdiskDownload/test/04 「远离金融陷阱」文化艺术品、古董等.avi", 1, 50000);
	}
}