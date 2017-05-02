package com.example.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;



public class UnZipUtil {
	private static final byte[] _byte = new byte[4096 * 1024];

	//private static byte[] _byte = new byte[1024] ;
	private static File rootFile = null;
	private static File hwt_zipFile = null;
	private static String herePath = null;

	public UnZipUtil(){
		rootFile = new File("");
		herePath = rootFile.getAbsolutePath();
	}

	public String getHwtFile(){
		File hereFileList = new File(herePath);
		File[] filelist = hereFileList.listFiles();
		for (File f:filelist){
			String hwt_name = f.getName();
			System.out.println(hwt_name);
			if(hwt_name.indexOf(".hwt") < 0){
				System.out.println("非hwt");
				continue;
			}
			if(hwt_name != null && !hwt_name.isEmpty()){
				System.out.println(hwt_name);
				int lenth = hwt_name.length();
				if(lenth <= 4){
					System.out.println("文件名至少两位字符");
					return null;
				}
				//System.out.println(hwt_name.indexOf(".hwt") + 4 + " + " + lenth);
				if(hwt_name.indexOf(".hwt.zip") + 8 == lenth || hwt_name.indexOf(".hwt") + 4 == lenth){
					return hwt_name;
				}
			}
		}
		return null;
	}

	public void deleteDirectionFile(){
		System.out.println("正在删除旧文件夹");
		File contactsFile = new File(herePath + "/com.android.contacts(1)");
		File incalluiFile = new File(herePath + "/com.android.incallui(1)");
		File mmsFile = new File(herePath + "/com.android.mms(1)");
		File phoneFile = new File(herePath + "/com.android.phone(1)");
		File recorderFile = new File(herePath + "/com.android.phone.recorder(1)");
		File telecomFile = new File(herePath + "/com.android.server.telecom(1)");
		File systemuiFile = new File(herePath + "/com.android.systemui(1)");
		File launcherFile = new File(herePath + "/com.huawei.android.launcher(1)");
		File iconsFile = new File(herePath + "/icons(1)");
		File previewFile = new File(herePath + "/preview");
		File unlockFile = new File(herePath + "/unlock");
		File wallpaperFile = new File(herePath + "/wallpaper");
		File descriptionFile = new File(herePath + "/description.xml");
		List<File> fileList = new ArrayList<File>();
		fileList.add(contactsFile);
		fileList.add(incalluiFile);
		fileList.add(mmsFile);
		fileList.add(phoneFile);
		fileList.add(recorderFile);
		fileList.add(telecomFile);
		fileList.add(systemuiFile);
		fileList.add(launcherFile);
		fileList.add(iconsFile);
		fileList.add(previewFile);
		fileList.add(unlockFile);
		fileList.add(wallpaperFile);
		fileList.add(descriptionFile);
		for (File f:fileList){
			if(f != null && f.exists()){
				deletefile(f.getAbsolutePath());
			}else{
				System.out.println("找不到" + f.getName());
			}
		}
	}


	public void deleteZipFile(){
		System.out.println("正在删除旧zip文件");
		File contactsFile = new File(herePath + "/com.android.contacts.zip");
		File incalluiFile = new File(herePath + "/com.android.incallui.zip");
		File mmsFile = new File(herePath + "/com.android.mms.zip");
		File phoneFile = new File(herePath + "/com.android.phone.zip");
		File recorderFile = new File(herePath + "/com.android.phone.recorder.zip");
		File telecomFile = new File(herePath + "/com.android.server.telecom.zip");
		File systemuiFile = new File(herePath + "/com.android.systemui.zip");
		File launcherFile = new File(herePath + "/com.huawei.android.launcher.zip");
		File iconsFile = new File(herePath + "/icons.zip");
		List<File> fileList = new ArrayList<File>();
		fileList.add(contactsFile);
		fileList.add(incalluiFile);
		fileList.add(mmsFile);
		fileList.add(phoneFile);
		fileList.add(recorderFile);
		fileList.add(telecomFile);
		fileList.add(systemuiFile);
		fileList.add(launcherFile);
		fileList.add(iconsFile);

		for (File f:fileList){
			if(f != null && f.exists()){
				f.delete();
			}else{
				System.out.println("找不到" + f.getName());
			}
		}
	}

	public void beginToUnZip() {
		// TODO Auto-generated method stub

		System.out.println("正在当前目录下搜索\".hwt\"后缀的主题包文件------");
		File hereFileList = new File(herePath);
		File[] filelist = hereFileList.listFiles();
		for (File f:filelist){
			String hwt_name = f.getName();
			if(!hwt_name.endsWith(".hwt")){
				continue;
			}
			System.out.println("找到hwt后缀的文件: " + hwt_name + "------");
			if(hwt_name != null && !hwt_name.isEmpty()){
				System.out.println(hwt_name);
				int lenth = hwt_name.length();
				if(lenth <= 4){
					System.out.println("文件名至少两位字符------");
					return;
				}
				//System.out.println(hwt_name.indexOf(".hwt") + 4 + " + " + lenth);
				if(hwt_name.indexOf(".hwt") + 4 == lenth){
					System.out.println("正在重命名------");
					f.renameTo(new File(f.getName() + ".zip"));
					hwt_zipFile = new File(f.getName() + ".zip");
					System.out.println("重命名成功，正在进行解压------");
					List<File> fileList = upzipFile(hwt_zipFile, rootFile.getAbsolutePath());
					if(fileList != null && fileList.size() > 0){
						System.out.println("第一层解压成功，正在进行第二层解压------");
						renameFileList(fileList);
					}else{
						System.out.println("主题包解压失败,请检查主题包命名是否正确------");
					}
				}else{
					System.out.println(hwt_name + "文件无效或格式不正确,请检查主题包------");
				}
			}else{
				System.out.println("hwt主题包文件名不能为空------");
			}
		}
		//System.out.println(hwt_zipFile.getAbsolutePath());
	}

	public void beginToAddZip(){
		if(herePath == null || herePath.isEmpty()){
			System.out.println("当前路径错误,请换个路径试试------");
			return;
		}
		List<File> files = new ArrayList<File>();
		File contactsFile = new File(herePath + "/com.android.contacts");
		if(contactsFile.exists()){
			zip(contactsFile.getAbsolutePath() + ".zip", contactsFile.listFiles());
		}
		contactsFile = null;
		File incalluiFile = new File(herePath + "/com.android.incallui");
		if(incalluiFile.exists()){
			zip(incalluiFile.getAbsolutePath() + ".zip", incalluiFile.listFiles());
		}
		incalluiFile = null;
		File mmsFile = new File(herePath + "/com.android.mms");
		if(mmsFile.exists()){
			zip(mmsFile.getAbsolutePath() + ".zip", mmsFile.listFiles());
		}
		mmsFile = null;
		File phoneFile = new File(herePath + "/com.android.phone");
		if(phoneFile.exists()){
			zip(phoneFile.getAbsolutePath() + ".zip", phoneFile.listFiles());
		}
		phoneFile = null;
		File recorderFile = new File(herePath + "/com.android.phone.recorder");
		if(recorderFile.exists()){
			zip(recorderFile.getAbsolutePath() + ".zip", recorderFile.listFiles());
		}
		recorderFile = null;
		File telecomFile = new File(herePath + "/com.android.server.telecom");
		if(telecomFile.exists()){
			zip(telecomFile.getAbsolutePath() + ".zip", telecomFile.listFiles());
		}
		telecomFile = null;
		File systemuiFile = new File(herePath + "/com.android.systemui");
		if(systemuiFile.exists()){
			zip(systemuiFile.getAbsolutePath() + ".zip", systemuiFile.listFiles());
		}
		systemuiFile = null;
		File launcherFile = new File(herePath + "/com.huawei.android.launcher");
		if(launcherFile.exists()){
			zip(launcherFile.getAbsolutePath() + ".zip", launcherFile.listFiles());
		}
		launcherFile = null;
		File iconsFile = new File(herePath + "/icons");
		if(iconsFile.exists()){
			zip(iconsFile.getAbsolutePath() + ".zip", iconsFile.listFiles());
		}
		iconsFile = null;
		String packageName = getHwtFile();
		if(packageName != null && !packageName.isEmpty()){
			int filesIndex = 0;
			File file = new File(packageName);
			if(packageName.endsWith(".hwt")){
				File newFile = new File(file.getAbsolutePath() + ".zip");
				file.renameTo(newFile);
				file = newFile;
			}
			File[] zipFiles = new File[13];
			System.out.println(file.getAbsolutePath() + " ---+ ");

			contactsFile = new File(herePath + "/com.android.contacts.zip");
			if(contactsFile.exists()){
				//zip(file.getAbsolutePath(), contactsFile.listFiles());
				zipFiles[filesIndex] = contactsFile;
				filesIndex +=1;
			}
			contactsFile = null;
			incalluiFile = new File(herePath + "/com.android.incallui.zip");
			if(incalluiFile.exists()){
				//zip(file.getAbsolutePath(), incalluiFile.listFiles());
				zipFiles[filesIndex] = incalluiFile;
				filesIndex +=1;
			}
			incalluiFile = null;
			mmsFile = new File(herePath + "/com.android.mms.zip");
			if(mmsFile.exists()){
				//zip(file.getAbsolutePath(), mmsFile.listFiles());
				zipFiles[filesIndex] = mmsFile;
				filesIndex +=1;
			}
			mmsFile = null;
			phoneFile = new File(herePath + "/com.android.phone.zip");
			if(phoneFile.exists()){
				//zip(file.getAbsolutePath(), phoneFile.listFiles());
				zipFiles[filesIndex] = phoneFile;
				filesIndex +=1;
			}
			phoneFile = null;
			recorderFile = new File(herePath + "/com.android.phone.recorder.zip");
			if(recorderFile.exists()){
				//zip(file.getAbsolutePath(), recorderFile.listFiles());
				zipFiles[filesIndex] = recorderFile;
				filesIndex +=1;
			}
			recorderFile = null;
			telecomFile = new File(herePath + "/com.android.server.telecom.zip");
			if(telecomFile.exists()){
				//zip(file.getAbsolutePath(), telecomFile.listFiles());
				zipFiles[filesIndex] = telecomFile;
				filesIndex +=1;
			}
			telecomFile = null;
			systemuiFile = new File(herePath + "/com.android.systemui.zip");
			if(systemuiFile.exists()){
				//zip(file.getAbsolutePath(), systemuiFile.listFiles());
				zipFiles[filesIndex] = systemuiFile;
				filesIndex +=1;
			}
			systemuiFile = null;
			launcherFile = new File(herePath + "/com.huawei.android.launcher.zip");
			if(launcherFile.exists()){
				//zip(file.getAbsolutePath(), launcherFile.listFiles());
				zipFiles[filesIndex] = launcherFile;
				filesIndex +=1;
			}
			launcherFile = null;
			iconsFile = new File(herePath + "/icons.zip");
			if(iconsFile.exists()){
				//zip(file.getAbsolutePath(), iconsFile.listFiles());
				zipFiles[filesIndex] = iconsFile;
				filesIndex +=1;
			}
			iconsFile = null;
			File previewFile = new File(herePath + "/preview");
			if(previewFile.exists()){
				//zip(file.getAbsolutePath(), previewFile.listFiles());
				zipFiles[filesIndex] = previewFile;
				filesIndex +=1;
			}
			previewFile = null;
			File unclockFile = new File(herePath + "/unlock");
			if(unclockFile.exists()){
				//zip(file.getAbsolutePath(), unclockFile.listFiles());
				zipFiles[filesIndex] = unclockFile;
				filesIndex +=1;
			}
			unclockFile = null;
			File descriptFile = new File(herePath + "/description.xml");
			if(descriptFile.exists()){
				//zip(file.getAbsolutePath(), descriptFile.listFiles());
				zipFiles[filesIndex] = descriptFile;
				filesIndex +=1;
			}
			descriptFile = null;
			File walpFile = new File(herePath + "/wallpaper");
			if(walpFile.exists()){
				//zip(file.getAbsolutePath(), walpFile.listFiles());
				zipFiles[filesIndex] = walpFile;
				filesIndex +=1;
			}
			walpFile = null;

			if(zip(file.getAbsolutePath(), zipFiles)){
				file.renameTo(new File(file.getAbsolutePath().replace(".zip", "")));
				System.out.println("主题打包成功");
			}
		}

	}

	private void renameFileList(List<File> fileList) {
		// TODO Auto-generated method stub
		if(fileList != null && fileList.size() > 0){
			for (File f:fileList){
				if(f.getName().equals("com.android.contacts") ||
						f.getName().equals("com.android.incallui") ||
						f.getName().equals("com.android.mms") ||
						f.getName().equals("com.android.phone") ||
						f.getName().equals("com.android.phone.recorder") ||
						f.getName().equals("com.android.server.telecom") ||
						f.getName().equals("com.android.systemui") ||
						f.getName().equals("com.huawei.android.launcher")||
						f.getName().equals("icons")
						){
					String filename = f.getName();
					System.out.println("正在重命名： " + filename);
					File newZipFile = new File(filename + ".zip");
					f.renameTo(newZipFile);
					upzipFile(newZipFile, rootFile.getAbsolutePath() + "/" + newZipFile.getName().replace(".zip", "(1)"));
					newZipFile = null;
					/*newZipFile = new File(rootFile.getAbsolutePath() + "/" + filename + ".zip");
					if(newZipFile.delete()){
						System.out.println("删除" + newZipFile.getName() + "成功------");
					}else{
						System.out.println("删除失败");
					}*/
					f = null;
				}
			}
			System.out.println("第二层解压成功, 主题包解压成功，您可以尽情的修改主题了，------\n");

			System.out.println("该工具由 ---小虫之家---开发---");
			System.out.println("该工具由 ---小虫之家---开发---");
			System.out.println("该工具由 ---小虫之家---开发---");
			System.out.println("该工具由 ---小虫之家---开发---");
			System.out.println("该工具由 ---小虫之家---开发---");
			System.out.println("该工具由 ---小虫之家---开发---");
			System.out.println("解压成功，您可以修改了主题资源后再通过该工具一键打包成\".hwt\"文件");
		}else{
			System.out.println("主题包数据不符合规范，第二层解压失败------");
		}

		if(fileList != null && fileList.size() > 0){
			for (File f:fileList){
				if(f.getName().equals("com.android.contacts") ||
						f.getName().equals("com.android.incallui") ||
						f.getName().equals("com.android.mms") ||
						f.getName().equals("com.android.phone") ||
						f.getName().equals("com.android.phone.recorder") ||
						f.getName().equals("com.android.server.telecom") ||
						f.getName().equals("com.android.systemui") ||
						f.getName().equals("com.huawei.android.launcher")||
						f.getName().equals("icons")
						){
					String filename = f.getName();
					System.out.println("正在删除： " + filename + ".zip");
					File newZipFile = new File(rootFile.getAbsolutePath() + "/" + filename + ".zip");
					if(newZipFile.delete()){
						System.out.println("删除" + newZipFile.getName() + "成功------");
					}else{
						System.out.println("删除失败");
					}
					f = null;
				}
			}
			System.out.println("第二层解压成功, 主题包解压成功，您可以尽情的修改主题了，------\n");

			System.out.println("该工具由 ---小虫之家---开发---");
			System.out.println("该工具由 ---小虫之家---开发---");
			System.out.println("该工具由 ---小虫之家---开发---");
			System.out.println("该工具由 ---小虫之家---开发---");
			System.out.println("该工具由 ---小虫之家---开发---");
			System.out.println("该工具由 ---小虫之家---开发---");
			System.out.println("解压成功，您可以修改了主题资源后再通过该工具一键打包成\".hwt\"文件");
		}else{
			System.out.println("主题包数据不符合规范，第二层解压失败------");
		}
	}

	/**
	 94      * 对.zip文件进行解压缩
	 95      * @param zipFile  解压缩文件
	 96      * @param descDir  压缩的目标地址，如：D:\\测试 或 /mnt/d/测试
	 97      * @return
	 98      */
	public List<File> upzipFile(File zipFile, String descDir) {
		List<File> _list = new ArrayList<File>() ;
		try {
			ZipFile _zipFile = new ZipFile(zipFile) ;
			for( Enumeration entries = _zipFile.getEntries() ; entries.hasMoreElements() ; ){
				ZipEntry entry = (ZipEntry)entries.nextElement() ;
				File _file = new File(descDir + File.separator + entry.getName()) ;
				if( entry.isDirectory() ){
					_file.mkdirs() ;
				}else{
					File _parent = _file.getParentFile() ;
					if( !_parent.exists() ){
						_parent.mkdirs() ;
					}
					InputStream _in = _zipFile.getInputStream(entry);
					OutputStream _out = new FileOutputStream(_file) ;
					int len = 0 ;
					while( (len = _in.read(_byte)) > 0){
						_out.write(_byte, 0, len);
					}
					_in.close(); 
					_out.flush();
					_out.close();
					_list.add(_file) ;
				}
			}
		} catch (IOException e) {
		}
		return _list ;
	}

	/**
	 * 对临时生成的文件夹和文件夹下的文件进行删除
	 */
	public void deletefile(String delpath) {
		try {
			File file = new File(delpath);
			if (!file.isDirectory()) {
				file.delete();
			} else if (file.isDirectory()) {
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File delfile = new File(delpath + File.separator + filelist[i]);
					if (!delfile.isDirectory()) {
						delfile.delete();
					} else if (delfile.isDirectory()) {
						deletefile(delpath + File.separator + filelist[i]);
					}
				}
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	static int k = 1; // 定义递归次数变量


	/**
	 * 压缩指定的单个或多个文件，如果是目录，则遍历目录下所有文件进行压缩
	 * @param zipFileName ZIP文件名包含全路径
	 * @param files  文件列表
	 */
	public boolean zip(String zipFileName, File[] files) {
		ZipOutputStream out = null;
		BufferedOutputStream bo = null;
		try {
			createDir(zipFileName);
			out = new ZipOutputStream(new FileOutputStream(zipFileName));
			for (int i = 0; i < files.length; i++) {
				if (null != files[i]) {
					zip(out, files[i], files[i].getName());
				}
			}
			out.close(); // 输出流关闭
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 执行压缩
	 * @param out ZIP输入流
	 * @param f   被压缩的文件
	 * @param base  被压缩的文件名
	 */
	private void zip(ZipOutputStream out, File f, String base) { // 方法重载
		if(base.endsWith(".zip")){
			base = base.replace(".zip", "");
		}
		try {
			if (f.isDirectory()) {//压缩目录
				try {
					File[] fl = f.listFiles();
					if (fl.length == 0) {
						out.putNextEntry(new ZipEntry(base + "/"));  // 创建zip实体
					}
					for (int i = 0; i < fl.length; i++) {
						zip(out, fl[i], base + "/" + fl[i].getName()); // 递归遍历子文件夹
					}
					//System.out.println("第" + k + "次递归");
					k++;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{ //压缩单个文件
				out.putNextEntry(new ZipEntry(base)); // 创建zip实体
				FileInputStream in = new FileInputStream(f);
				BufferedInputStream bi = new BufferedInputStream(in);
				int b;
				while ((b = bi.read()) != -1) {
					out.write(b); // 将字节流写入当前zip目录
				}
				out.closeEntry(); //关闭zip实体
				in.close(); // 输入流关闭
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String MESSAGE = "";  

	/** 
	 * 复制单个文件 
	 *  
	 * @param srcFileName 
	 *            待复制的文件名 
	 * @param descFileName 
	 *            目标文件名 
	 * @param overlay 
	 *            如果目标文件存在，是否覆盖 
	 * @return 如果复制成功返回true，否则返回false 
	 */  
	public static boolean copyFile(String srcFileName, String destFileName,  
			boolean overlay) {  
		File srcFile = new File(srcFileName);  

		// 判断源文件是否存在  
		if (!srcFile.exists()) {  
			MESSAGE = "源文件：" + srcFileName + "不存在！";  
			JOptionPane.showMessageDialog(null, MESSAGE);  
			return false;  
		} else if (!srcFile.isFile()) {  
			MESSAGE = "复制文件失败，源文件：" + srcFileName + "不是一个文件！";  
			JOptionPane.showMessageDialog(null, MESSAGE);  
			return false;  
		}  

		// 判断目标文件是否存在  
		File destFile = new File(destFileName);  
		if (destFile.exists()) {  
			// 如果目标文件存在并允许覆盖  
			if (overlay) {  
				// 删除已经存在的目标文件，无论目标文件是目录还是单个文件  
				new File(destFileName).delete();  
			}  
		} else {  
			// 如果目标文件所在目录不存在，则创建目录  
			if (!destFile.getParentFile().exists()) {  
				// 目标文件所在目录不存在  
				if (!destFile.getParentFile().mkdirs()) {  
					// 复制文件失败：创建目标文件所在目录失败  
					return false;  
				}  
			}  
		}  

		// 复制文件  
		int byteread = 0; // 读取的字节数  
		InputStream in = null;  
		OutputStream out = null;  

		try {  
			in = new FileInputStream(srcFile);  
			out = new FileOutputStream(destFile);  
			byte[] buffer = new byte[1024];  

			while ((byteread = in.read(buffer)) != -1) {  
				out.write(buffer, 0, byteread);  
			}  
			return true;  
		} catch (FileNotFoundException e) {  
			return false;  
		} catch (IOException e) {  
			return false;  
		} finally {  
			try {  
				if (out != null)  
					out.close();  
				if (in != null)  
					in.close();  
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}  
	}  

	/** 
	 * 复制整个目录的内容 
	 *  
	 * @param srcDirName 
	 *            待复制目录的目录名 
	 * @param destDirName 
	 *            目标目录名 
	 * @param overlay 
	 *            如果目标目录存在，是否覆盖 
	 * @return 如果复制成功返回true，否则返回false 
	 */  
	public static boolean copyDirectory(String srcDirName, String destDirName,  
			boolean overlay) {  
		// 判断源目录是否存在  
		File srcDir = new File(srcDirName);  
		if (!srcDir.exists()) {  
			MESSAGE = "复制目录失败：源目录" + srcDirName + "不存在！";  
			JOptionPane.showMessageDialog(null, MESSAGE);  
			return false;  
		} else if (!srcDir.isDirectory()) {  
			MESSAGE = "复制目录失败：" + srcDirName + "不是目录！";  
			JOptionPane.showMessageDialog(null, MESSAGE);  
			return false;  
		}  

		// 如果目标目录名不是以文件分隔符结尾，则加上文件分隔符  
		if (!destDirName.endsWith(File.separator)) {  
			destDirName = destDirName + File.separator;  
		}  
		File destDir = new File(destDirName);  
		// 如果目标文件夹存在  
		if (destDir.exists()) {  
			// 如果允许覆盖则删除已存在的目标目录  
			if (overlay) {  
				new File(destDirName).delete();  
			} else {  
				MESSAGE = "复制目录失败：目的目录" + destDirName + "已存在！";  
				JOptionPane.showMessageDialog(null, MESSAGE);
				return false;  
			}  
		} else {  
			// 创建目的目录  
			System.out.println("目的目录不存在，准备创建。。。");  
			if (!destDir.mkdirs()) {  
				System.out.println("复制目录失败：创建目的目录失败！");  
				return false;  
			}  
		}  

		boolean flag = true;  
		File[] files = srcDir.listFiles();  
		for (int i = 0; i < files.length; i++) {  
			// 复制文件  
			if (files[i].isFile()) {  
				flag = copyFile(files[i].getAbsolutePath(),  
						destDirName + files[i].getName(), overlay);  
				if (!flag)  
					break;  
			} else if (files[i].isDirectory()) {  
				flag = copyDirectory(files[i].getAbsolutePath(),  
						destDirName + files[i].getName(), overlay);  
				if (!flag)
					break;  
			}  
		}  
		if (!flag) {  
			MESSAGE = "复制目录" + srcDirName + "至" + destDirName + "失败！";  
			JOptionPane.showMessageDialog(null, MESSAGE);  
			return false;  
		} else {
			return true;  
		}  
	}  


	/**
	 * 目录不存在时，先创建目录
	 * @param zipFileName
	 */
	private void createDir(String zipFileName){
		String filePath = zipFileName.substring(zipFileName.indexOf(".zip"), zipFileName.length());
		File targetFile = new File(filePath);
		if (!targetFile.exists()) {//目录不存在时，先创建目录
			targetFile.mkdirs();
		}
	}

	public void deleteDefaultFile(){
		System.out.println("正在删除已存在文件");
		File contactsFile = new File(herePath + "/com.android.contacts");
		File incalluiFile = new File(herePath + "/com.android.incallui");
		File mmsFile = new File(herePath + "/com.android.mms");
		File phoneFile = new File(herePath + "/com.android.phone");
		File recorderFile = new File(herePath + "/com.android.phone.recorder");
		File telecomFile = new File(herePath + "/com.android.server.telecom");
		File systemuiFile = new File(herePath + "/com.android.systemui");
		File launcherFile = new File(herePath + "/com.huawei.android.launcher");
		File iconsFile = new File(herePath + "/icons");
		/*		File previewFile = new File(herePath + "/preview");
		File unlockFile = new File(herePath + "/unlock");
		File wallpaperFile = new File(herePath + "/wallpaper");
		File descriptionFile = new File(herePath + "/description.xml");*/
		List<File> fileList = new ArrayList<File>();
		fileList.add(contactsFile);
		fileList.add(incalluiFile);
		fileList.add(mmsFile);
		fileList.add(phoneFile);
		fileList.add(recorderFile);
		fileList.add(telecomFile);
		fileList.add(systemuiFile);
		fileList.add(launcherFile);
		fileList.add(iconsFile);
		/*		fileList.add(previewFile);
		fileList.add(unlockFile);
		fileList.add(wallpaperFile);
		fileList.add(descriptionFile);*/
		for (File f:fileList){
			if(f != null && f.exists()){
				deletefile(f.getAbsolutePath());
			}else{
				System.out.println("找不到" + f.getName());
			}
		}
	}

	public void syncContactFile() {
		// TODO Auto-generated method stub
		System.out.println("正在同步contacts模块的framework-res-hwext文件夹到其它模块");
		copyDirectory(rootFile.getAbsolutePath() + "/com.android.contacts(1)/framework-res-hwext", rootFile.getAbsolutePath() + "/com.android.incallui(1)/framework-res-hwext", true);
		copyDirectory(rootFile.getAbsolutePath() + "/com.android.contacts(1)/framework-res-hwext", rootFile.getAbsolutePath() + "/com.android.mms(1)/framework-res-hwext", true);
		copyDirectory(rootFile.getAbsolutePath() + "/com.android.contacts(1)/framework-res-hwext", rootFile.getAbsolutePath() + "/com.android.phone(1)/framework-res-hwext", true);
		copyDirectory(rootFile.getAbsolutePath() + "/com.android.contacts(1)/framework-res-hwext", rootFile.getAbsolutePath() + "/com.android.phone.recorder(1)/framework-res-hwext", true);
		copyDirectory(rootFile.getAbsolutePath() + "/com.android.contacts(1)/framework-res-hwext", rootFile.getAbsolutePath() + "/com.android.server.telecom(1)/framework-res-hwext", true);
		copyDirectory(rootFile.getAbsolutePath() + "/com.android.contacts(1)/framework-res-hwext", rootFile.getAbsolutePath() + "/com.android.systemui(1)/framework-res-hwext", true);
	}

	public void packageHwt() throws InterruptedException{
		String winrarPath = "C:/Program Files/WinRAR/WinRAR.exe";
		/*File winrarFile = new File(winrarPath);
		File contactsFile = new File(herePath + "/com.android.contacts");
		File incalluiFile = new File(herePath + "/com.android.incallui");
		File mmsFile = new File(herePath + "/com.android.mms");
		File phoneFile = new File(herePath + "/com.android.phone");
		File recorderFile = new File(herePath + "/com.android.phone.recorder");
		File telecomFile = new File(herePath + "/com.android.server.telecom");
		File systemuiFile = new File(herePath + "/com.android.systemui");
		File launcherFile = new File(herePath + "/com.huawei.android.launcher");
		File iconsFile = new File(herePath + "/icons");*/
		File previewFile = new File(herePath + "/preview");
		File unlockFile = new File(herePath + "/unlock");
		File wallpaperFile = new File(herePath + "/wallpaper");
		File descriptionFile = new File(herePath + "/description.xml");
		String packageName = getHwtFile();
		if(packageName.endsWith(".hwt")){
			packageName = packageName + ".zip";
		}
		try {
			File indexFile = new File("");
			Process process;
			process = Runtime.getRuntime().exec("C:/Program Files/WinRAR/WinRAR.exe a " + packageName + "preview");
			process.waitFor();
			process = Runtime.getRuntime().exec("C:/Program Files/WinRAR/WinRAR.exe a " + packageName + "com.android.server.telecom.zip unlock");
			process.waitFor();
			process = Runtime.getRuntime().exec("C:/Program Files/WinRAR/WinRAR.exe a " + packageName + "com.android.server.telecom.zip wallpaper");
			process.waitFor();
			process = Runtime.getRuntime().exec("C:/Program Files/WinRAR/WinRAR.exe a " + packageName + "com.android.server.telecom.zip description.xml");
			process.waitFor();
			System.out.println("添加所有包成功" + "成功");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void packageSourse() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("开始生成hwt------");
		String winrarPath = "C:/Program Files/WinRAR/WinRAR.exe";
		File winrarFile = new File(winrarPath);
		if(winrarFile.exists()){
			System.out.println("已经找到WinRAR.exe, 开始打包------");
			File contactsFile = new File(herePath + "/com.android.contacts(1)");
			File incalluiFile = new File(herePath + "/com.android.incallui(1)");
			File mmsFile = new File(herePath + "/com.android.mms(1)");
			File phoneFile = new File(herePath + "/com.android.phone(1)");
			File recorderFile = new File(herePath + "/com.android.phone.recorder(1)");
			File telecomFile = new File(herePath + "/com.android.server.telecom(1)");
			File systemuiFile = new File(herePath + "/com.android.systemui(1)");
			File launcherFile = new File(herePath + "/com.huawei.android.launcher(1)");
			File iconsFile = new File(herePath + "/icons(1)");
			List<File> files = new ArrayList<File>();
			files.add(contactsFile);
			files.add(incalluiFile);
			files.add(mmsFile);
			files.add(phoneFile);
			files.add(recorderFile);
			files.add(telecomFile);
			files.add(systemuiFile);
			files.add(launcherFile);
			files.add(iconsFile);
			for (File f:files){
				if(f.exists()){
					String packageName = f.getName().replace("(1)", "");
					StringBuffer fl = new StringBuffer();
					File[] subFileList = f.listFiles();
					if(subFileList != null && subFileList.length > 0){
						for (int i =0; i < subFileList.length; ++i){
							if(subFileList[i].getName().indexOf(" ") >= 0){
								System.out.println("存在可能异常的文件，请检查文件名：" + subFileList[i].getName());
								continue;
							}
							fl.append(" " + subFileList[i].getName());
						}
						File indexFile = new File(f.getName());
						System.out.println(f.getName());
						System.out.println("正在打包" + packageName);
						System.out.println(winrarPath + " a " + packageName + ".zip" + fl.toString());
						Process process = Runtime.getRuntime().exec(winrarPath + " a " + packageName + ".zip" + fl.toString(), null, indexFile);
						try {
							process.waitFor();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						File eeefile = new File(indexFile.getAbsolutePath() + "/" + packageName + ".zip");
						if(eeefile.renameTo(new File(herePath + "/" + eeefile.getName().replace(".zip", "")))){
							System.out.println(packageName + " 打包成功");
							String path = getHwtFile();
							String subPckageName = packageName;
							Process process1 = null;
							if(path.endsWith(".hwt")){
								process1 = Runtime.getRuntime().exec(winrarPath + " a " + path + ".zip " + subPckageName);
							}else{
								System.out.println("运行winrar:" + winrarPath + " a " + path + " " + subPckageName);
								process1 = Runtime.getRuntime().exec(winrarPath + " a " + path + " " + subPckageName);
							}
							process1.waitFor();
							
						}else{
							System.out.println(packageName + " 打包失败");
						}
					}
				}else{
					System.out.println();
				}
			}

		}else{
			System.out.println("WinRAR.exe 找不到，请确认WinRAR路径是否正确");
		}
	}

}
