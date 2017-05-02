package com.example.jfram;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import com.example.util.FileOperation;

public class PackUtilFram extends JFrame{
	private static PackUtilFram instance = null;


	public String log_str = "";
	private static final byte[] _byte = new byte[4096 * 1024];

	public Button pack_button = null;
	public Button unPack_button = null;
	public Button syncFile_button = null;
	public Button help_button = null;
	public Button clean_log = null;
	public JScrollPane logPane = null;
	public JTextArea logText = null;
	public JLabel power = null;

	public String window_title = "华为主题自动打包工具";
	public String use_help_str = "(1)该工具的开发环境是基于java jdk-1.6开发的，低于1.6版本不确定功能正常\n" +
			"(2) 使用\"打包主题\"功能之前，您还需要确认您的电脑已经安装了“破解版或者正式\n" +
			"    版的WinRAR”，\n\n" +
			"(3) 使用说明\n" +
			"   首先把该工具放在和你要解压的主题包同目录下\n\n" + 
			"   ‘解压主题’---把该工具放在您需要解压的.hwt后缀或者.hwt.zip后缀的主题包\n" + 
			"   同目录下，点击‘解压主题’即可一键解压您的主题包，会解压出zip文件和二级解压\n" + 
			"   的带”(1)“后缀的文件夹(为了不和打包时生成的文件名称冲突，所以加了（1）后缀)\n" + 
			"   ，如果您要使用一键打包的功能，请不要修改这些文件夹的名称\n\n" +
			"   ‘打包主题’---主题自动打包功能，能把通过该工具解压出来的文件夹进行一键打包成\n" + 
			" \".hwt\"后缀的主题包，只需要点击‘打包主题’按钮即可，请勿重命名解压出来的\n" + 
			"   带\"（1）\"后缀的文件夹\n\n" +
			"   同步Contacts到其它所有文件夹---该功能是针对华为5.0主题包的特性的功能，在\n" + 
			"   Contacts中的framework-res-hwext资源与其它子模块中的文件可以是一样的\n" + 
			"   资源，如果您没有特殊需求，可以通过该功能进行一键同步，免去重复复制粘贴的麻烦。 \n\n" +
			"\n------该工具由   小虫之家   设计与开发------\n\n";

	public String here_path_string = "";	//工作目录

	public String winrar_path = "C:/Program Files/WinRAR/WinRAR.exe";

	public String contacts_folder = "com.android.contacts(1)"; 
	public String incallui_folder = "com.android.incallui(1)";
	public String mms_folder = "com.android.mms(1)";
	public String phone_folder = "com.android.phone(1)";
	public String recorder_folder = "com.android.phone.recorder(1)";
	public String telecom_folder = "com.android.server.telecom(1)";
	public String systemui_folder = "com.android.systemui(1)";
	public String launcher_folder = "com.huawei.android.launcher(1)";
	public String icon_folder = "icons(1)";
	public String preview_folder = "preview";
	public String unlock_folder = "unlock";
	public String wallpaper_folder = "wallpaper";
	public String description_folder = "description.xml";

	public String contacts_zip = "com.android.contacts.zip"; 
	public String incallui_zip = "com.android.incallui.zip";
	public String mms_zip = "com.android.mms.zip";
	public String phone_zip = "com.android.phone.zip";
	public String recorder_zip = "com.android.phone.recorder.zip";
	public String telecom_zip = "com.android.server.telecom.zip";
	public String systemui_zip = "com.android.systemui.zip";
	public String launcher_zip = "com.huawei.android.launcher.zip";
	public String icon_zip = "icons.zip";
	public String preview_zip = "preview";
	public String unlock_zip = "unlock";
	public String wallpaper_zip = "wallpaper";
	public String description_zip = "description.xml";

	public static PackUtilFram getInstance(){
		if(instance == null){
			instance = new PackUtilFram();
		}
		return instance;
	}

	private PackUtilFram(){
		setLayout((LayoutManager) new FlowLayout(FlowLayout.CENTER));
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		int screenWidth = toolkit.getScreenSize().width;
		int screenHeight = toolkit.getScreenSize().height;
		setLocation(screenWidth/2 - 225,screenHeight/2 - 250);
		setSize(450, 510);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack_button = new Button("打包主题");
		pack_button.setPreferredSize(new Dimension(400, 30));
		unPack_button = new Button("解压主题");
		unPack_button.setPreferredSize(new Dimension(400, 30));
		syncFile_button = new Button("同步Contacts中的framework-res-hwext到其它文件夹");
		syncFile_button.setPreferredSize(new Dimension(400, 30));
		help_button = new Button("使用帮助");
		help_button.setPreferredSize(new Dimension(400, 30));
		power = new JLabel("该工具由“小虫之家”开发者所有---有问题加群：361808483");

		clean_log = new Button("清空日志");

		logText = new JTextArea(3, 20);  
		logText.setLineWrap(true);// 如果内容过长。自动换行
		logPane = new JScrollPane(logText,  
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,  
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);  
		logPane.setPreferredSize(new Dimension(440, 300));
		setTitle(window_title);
		add(pack_button);
		add(unPack_button);
		add(syncFile_button);
		add(help_button);
		add(logPane);
		add(clean_log);
		add(power);
		File here_file = new File("");
		here_path_string = here_file.getAbsolutePath();
		here_file = null;	//释放资源


		addActionListener();
	}

	private String getConfigText(){
		StringBuilder result = new StringBuilder();
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(here_path_string + "/" + "config.txt")));//构造一个BufferedReader类来读取文件
			String s = null;
			while((s = br.readLine())!=null){//使用readLine方法，一次读一行
				result.append(System.lineSeparator()+s);
			}
			outputLog(result.toString());
			br.close(); 
			return result.toString();
		}catch(Exception e){
			outputLog(e.getMessage());
			return null;
		}
	}

	private void addActionListener() {
		// TODO Auto-generated method stub
		pack_button.addActionListener(new ActionListener() {		//打包主题
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				deleteRZI();
				toAddThem();
			}
		});
		unPack_button.addActionListener(new ActionListener() {		//解压主题

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				deleteRZI();
				String themPackName = getThemPackPath();
				if(themPackName == null || themPackName.isEmpty()){
					outputLog("找不到.hwt或者.hwt.zip后缀的主题包,请检查主题包名称格式是否正确");
				}else{
					outputLog("找到主题包：" + getThemPackPath() + "   开始解压");
					toUnThemPack(themPackName);
				}
			}
		});
		syncFile_button.addActionListener(new ActionListener() {		//同步文件
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				deleteRZI();
				syncContactFile();
			}
		});
		help_button.addActionListener(new ActionListener() {		//帮助

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				deleteRZI();
				outputLog(use_help_str);
				JOptionPane.showMessageDialog(null, use_help_str);
			}
		});
		clean_log.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				deleteRZI();
				logText.setText("");
			}
		});
	}


	public void deleteDefaultFile(){
		outputLog("正在删除旧文件文件");
		File contactsFile = new File(here_path_string + "/com.android.contacts");
		File incalluiFile = new File(here_path_string + "/com.android.incallui");
		File mmsFile = new File(here_path_string + "/com.android.mms");
		File phoneFile = new File(here_path_string + "/com.android.phone");
		File recorderFile = new File(here_path_string + "/com.android.phone.recorder");
		File telecomFile = new File(here_path_string + "/com.android.server.telecom");
		File systemuiFile = new File(here_path_string + "/com.android.systemui");
		File launcherFile = new File(here_path_string + "/com.huawei.android.launcher");
		File iconsFile = new File(here_path_string + "/icons");

		File contactsFile_1 = new File(here_path_string + "/" + contacts_folder);
		File incalluiFile_1 = new File(here_path_string + "/" + incallui_folder);
		File mmsFile_1 = new File(here_path_string + "/" + mms_folder);
		File phoneFile_1 = new File(here_path_string + "/" + phone_folder);
		File recorderFile_1 = new File(here_path_string + "/" + recorder_folder);
		File telecomFile_1 = new File(here_path_string + "/" + telecom_folder);
		File systemuiFile_1 = new File(here_path_string + "/" + systemui_folder);
		File launcherFile_1 = new File(here_path_string + "/" + launcher_folder);
		File iconsFile_1 = new File(here_path_string + "/" + icon_folder);

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

		List<File> file_1_list = new ArrayList<File>();
		file_1_list.add(contactsFile_1);
		file_1_list.add(incalluiFile_1);
		file_1_list.add(mmsFile_1);
		file_1_list.add(phoneFile_1);
		file_1_list.add(recorderFile_1);
		file_1_list.add(telecomFile_1);
		file_1_list.add(systemuiFile_1);
		file_1_list.add(launcherFile_1);
		file_1_list.add(iconsFile_1);

		for (int i = 0; i < fileList.size(); ++i){
			File f = fileList.get(i);
			File f_1 = file_1_list.get(i);
			if(f.exists() && f.isFile()){
				if(deletefile(f.getAbsolutePath())){
					outputLog("删除" + f.getName() + "成功");
				}else{
					outputLog("删除" + f.getName() + "失败");
				}
			}else if(f.exists() && f.isDirectory()){
				if(!f_1.exists()){
					if(f.renameTo(f_1)){
						outputLog(f.getName() + "重命名为" + f_1.getName() + "成功");
					}else{
						outputLog(f.getName() + "重命名为" + f_1.getName() + "失败");
					}
				}else{
					if(deletefile(f.getAbsolutePath())){
						outputLog(f.getName() + "已删除成功");
					}else{
						outputLog(f.getName() + "删除失败");
					}
				}
			}else{
				outputLog("删除多余文件成功");
			}
		}
	}

	private void toAddThem() {
		// TODO Auto-generated method stub
		deleteDefaultFile();
		deleteZipFile();
		try {
			packageSourse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			outputLog("打包异常：" + e.getMessage());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			outputLog("打包异常：" + e.getMessage());
		}
	}

	public void packageSourse() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		outputLog("开始生成hwt------");
		File winrarFile = new File(winrar_path);
		if(winrarFile.exists() && winrarFile.getName().toLowerCase().equals("winrar.exe")){
			beginToApck();
		}else{
			File configFile = new File("D:/config.txt");
			if(configFile.exists()){
				try {
					String winrarstr = FileOperation.readTxtFile(configFile);
					if(winrarstr != null && !winrarstr.isEmpty()){
						System.out.println(winrarstr);
						winrar_path = winrarstr.replaceAll("////", "/");
						System.out.println(winrar_path);
						beginToApck();
					}else{

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				try {
					CreateNewConfig();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			}
		}
	}

	private void CreateNewConfig() throws Exception {
		File winrarFile;
		outputLog("默认路径下找不到WinRAR。exe程序，请手动选择");
		JOptionPane.showMessageDialog(null, winrar_path + " 找不到,请手动选择您的WinRAR.exe程序" );
		outputLog(winrar_path);
		JFileChooser jfc=new JFileChooser();  
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  

		jfc.showDialog(new JLabel(), "手动选择WinRAR.exe程序");  
		File file=jfc.getSelectedFile();  
		if(file == null || file.isDirectory()){  
			outputLog("不是有效的WinRAR.exe程序");  
		}else if(file.isFile()){  
			winrar_path = file.getAbsolutePath().replaceAll("////", "/");
			if(winrar_path.toLowerCase().endsWith("winrar.exe")){
				File configFile = new File("c:/config.txt");
				if(configFile.exists()){
					FileOperation.contentToTxt(configFile.getAbsolutePath(), winrar_path);
				}else{
					FileOperation.createFile(configFile);
					FileOperation.contentToTxt(configFile.getAbsolutePath(), winrar_path);
				}
				winrarFile = new File(winrar_path);
				if(winrarFile.exists()){
					beginToApck();
				}
			}else{
				outputLog("不是有效的WinRAR.exe程序");
			}
		}  
		//System.out.println(jfc.getSelectedFile().getName());
	}

	private void beginToApck() throws IOException, InterruptedException {
		outputLog("已经找到WinRAR.exe, 开始打包------");
		File contactsFile = new File(here_path_string + "/com.android.contacts(1)");
		File incalluiFile = new File(here_path_string + "/com.android.incallui(1)");
		File mmsFile = new File(here_path_string + "/com.android.mms(1)");
		File phoneFile = new File(here_path_string + "/com.android.phone(1)");
		File recorderFile = new File(here_path_string + "/com.android.phone.recorder(1)");
		File telecomFile = new File(here_path_string + "/com.android.server.telecom(1)");
		File systemuiFile = new File(here_path_string + "/com.android.systemui(1)");
		File launcherFile = new File(here_path_string + "/com.huawei.android.launcher(1)");
		File iconsFile = new File(here_path_string + "/icons(1)");
		File previewFile = new File(here_path_string + "/preview");
		File unlockFile = new File(here_path_string + "/unlock");
		File wallpallFile = new File(here_path_string + "/wallpaper");
		File descriptFile = new File(here_path_string + "/description.xml");




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

		List<File> flodList = new ArrayList<File>();
		flodList.add(previewFile);
		flodList.add(unlockFile);
		flodList.add(wallpallFile);
		flodList.add(descriptFile);

		for (File f:files){
			if(f.exists()){
				String packageName = f.getName().replace("(1)", "");
				StringBuffer file_list_buffer = new StringBuffer();
				File[] subFileList = f.listFiles();
				if(subFileList != null && subFileList.length > 0){
					for (int i =0; i < subFileList.length; ++i){
						if(subFileList[i].getName().indexOf(" ") >= 0){
							outputLog("存在可能异常的文件，请检查文件名：" + subFileList[i].getName() + ",已经跳过该文件的打包，如果您确定该文件没有问题，请手动添加该文件到相应的包中");
							continue;
						}
						file_list_buffer.append(" " + subFileList[i].getName());
					}
					File workFile = new File(f.getName());
					outputLog("正在打包" + packageName);
					Process process = Runtime.getRuntime().exec(winrar_path + " a " + packageName + ".zip" + file_list_buffer.toString(), null, workFile);
					try {
						process.waitFor();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						outputLog("打包超时---打包失败，请检查文件名,或者反馈该错误给“小虫之家”");
					}
					File subFile = new File(f.getAbsolutePath() + "/" + packageName + ".zip");
					if(subFile.renameTo(new File(here_path_string + "/" + packageName.replace(".zip", "")))){
						String them_pack_name = getThemPackPath();
						if(them_pack_name != null && !them_pack_name.isEmpty()){
							if(them_pack_name.endsWith(".hwt")){
								File them_pack_file = new File(here_path_string + "/" + them_pack_name);
								if(them_pack_file.renameTo(new File(here_path_string + "/" + them_pack_name + ".zip"))){
									outputLog("主题包" + them_pack_name + "重命名为" + them_pack_name + ".zip成功");
									them_pack_name = them_pack_name + ".zip";
								}else{
									outputLog("主题包" + them_pack_name + "重命名为" + them_pack_name + ".zip失败，请手动修改主题包为.hwt.zip后缀");
									return;
								}
							}
						}else{
							them_pack_name = "newThemPack.hwt.zip";
						}
						String subPckageName = packageName;
						Process process1 = null;
						process1 = Runtime.getRuntime().exec(winrar_path + " a " + them_pack_name + " " + subPckageName);
						process1.waitFor();
						outputLog(subPckageName + "打包成功");
					}else{
						System.out.println(packageName + " 打包失败");
					}
				}
			}else{
				outputLog("找不到 " + f.getName() + " 该主题可能不是完整的华为大主题");
			}
		}

		StringBuffer folder_str = new StringBuffer();
		String them_pack_name = getThemPackPath();
		if(them_pack_name != null && !them_pack_name.isEmpty()){
			if(them_pack_name.endsWith(".hwt")){
				File them_pack_file = new File(here_path_string + "/" + them_pack_name);
				if(them_pack_file.renameTo(new File(here_path_string + "/" + them_pack_name + ".zip"))){
					outputLog("主题包" + them_pack_name + "重命名为" + them_pack_name + ".zip成功");
					them_pack_name = them_pack_name + ".zip";
				}else{
					outputLog("主题包" + them_pack_name + "重命名为" + them_pack_name + ".zip失败，请手动修改主题包为.hwt.zip后缀");
					return;
				}
			}
		}else{
			them_pack_name = "newThemPack.hwt.zip";
		}
		for (File f:flodList){
			if(f.exists()){
				folder_str = folder_str.append(" " + f.getName());
			}else{
				outputLog("找不到 " + f.getName() + " 该主题可能不是完整的华为大主题");
			}
		}
		Process process2 = Runtime.getRuntime().exec(winrar_path + " a " + them_pack_name + folder_str);
		outputLog("打包成功，正在进行最后的改名操作---");
		File latestPack_zip = new File(here_path_string + "/" + them_pack_name);
		File latestPack = new File(latestPack_zip.getAbsolutePath().replace(".zip", ""));
		if(latestPack.renameTo(latestPack)){
			outputLog("主题包打包成功： " + them_pack_name.replace(".zip", ""));
		}else{
			JOptionPane.showMessageDialog(null, "不好意思，最后一个环节出了点小问题，请手动把" + them_pack_name + "重命\n名为 " + them_pack_name.replace(".zip", "") + "就大功告成啦");  
			outputLog("不好意思，最后一个环节出了点小问题，请手动把" + them_pack_name + "重命名为 " + them_pack_name.replace(".zip", "") + "就大功告成啦");
		}
	}

	public void deleteRZI(){
		File hereFile = new File(here_path_string);
		File[] rzi_list = hereFile.listFiles();
		if(rzi_list != null && rzi_list.length > 0){
			for (File f:rzi_list){
				if(f.getName().indexOf("rzi") >= 0){
					deletefile(f.getAbsolutePath());
				}
			}
		}
	}

	private void toUnThemPack(String themPackName) {
		// TODO Auto-generated method stub
		deleteFold();
		deleteZipFile();
		deletePUWDFold();
		if(isCleanSuucess()){
			outputLog("\n准备就绪，正在解压，请稍等\n");
			unThemPack(themPackName);
		}else{
			outputLog("");
		}
	}

	private void unThemPack(String themPackName) {
		// TODO Auto-generated method stub
		if(themPackName.endsWith(".hwt")){
			outputLog("正在重命名 " + themPackName + "为" + themPackName + ".zip");
			File packFile = new File(here_path_string + "/" + themPackName);
			File newFile = new File(here_path_string + "/" + themPackName + ".zip");
			if(packFile.renameTo(newFile)){
				outputLog("重命名成功");
				themPackName = themPackName + ".zip";
			}else{
				outputLog("重命名失败，文件可能被其它程序占用中,或者手动添加.zip后缀");
				return;
			}
		}
		File zipPackFile = new File(here_path_string + "/" + themPackName);
		List<File> packFileList = upzipFile(zipPackFile, here_path_string);
		if(packFileList != null && packFileList.size() > 0){
			outputLog("\n第一层解压成功，正在进行第二层解压------\n");
			unSubZipFileList(packFileList);
		}else{
			outputLog("主题包内容为空");
		}
	}

	public void syncContactFile() {
		// TODO Auto-generated method stub
		outputLog("正在同步contacts模块的framework-res-hwext文件夹到其它模块\n");
		String fromPath = here_path_string + "/com.android.contacts(1)/framework-res-hwext";
		if(copyDirectory(fromPath, here_path_string + "/com.android.incallui(1)/framework-res-hwext", true)){
			outputLog("从Contactscom.android.contacts(1)  同步到        com.android.incallui(1)成功");
		}else{
			outputLog("从Contactscom.android.contacts(1)  同步到        com.android.incallui(1)失败");
		}
		if(copyDirectory(fromPath, here_path_string + "/com.android.mms(1)/framework-res-hwext", true)){
			outputLog("从Contactscom.android.contacts(1)  同步到        com.android.mms(1)成功");
		}else{
			outputLog("从Contactscom.android.contacts(1)  同步到        com.android.mms(1)失败");
		}
		if(copyDirectory(fromPath, here_path_string + "/com.android.phone(1)/framework-res-hwext", true)){
			outputLog("从Contactscom.android.contacts(1)  同步到        com.android.phone(1)成功");
		}else{
			outputLog("从Contactscom.android.contacts(1)  同步到        com.android.phone(1)失败");
		}
		if(copyDirectory(fromPath, here_path_string + "/com.android.phone.recorder(1)/framework-res-hwext", true)){
			outputLog("从Contactscom.android.contacts(1)  同步到        com.android.phone.recorder(1)成功");
		}else{
			outputLog("从Contactscom.android.contacts(1)  同步到        com.android.phone.recorder(1)失败");
		}
		if(copyDirectory(fromPath, here_path_string + "/com.android.server.telecom(1)/framework-res-hwext", true)){
			outputLog("从Contactscom.android.contacts(1)  同步到        com.android.server.telecom(1)成功");
		}else{
			outputLog("从Contactscom.android.contacts(1)  同步到        com.android.server.telecom(1)失败");
		}
		if(copyDirectory(fromPath, here_path_string + "/com.android.systemui(1)/framework-res-hwext", true)){
			outputLog("从Contactscom.android.contacts(1)  同步到       com.android.systemui(1)成功\n");
		}else{
			outputLog("从Contactscom.android.contacts(1)  同步到        com.android.systemui(1)失败\n");
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

	private void unSubZipFileList(List<File> fileList) {
		// TODO Auto-generated method stub
		if(fileList != null && fileList.size() > 0){
			for (File f:fileList){
				String filename = f.getName();
				if(filename.equals("com.android.contacts") ||
						filename.equals("com.android.incallui") ||
						filename.equals("com.android.mms") ||
						filename.equals("com.android.phone") ||
						filename.equals("com.android.phone.recorder") ||
						filename.equals("com.android.server.telecom") ||
						filename.equals("com.android.systemui") ||
						filename.equals("com.huawei.android.launcher")||
						filename.equals("icons")){

					outputLog("正在重命名： " + filename + "为" + filename + ".zip");
					File newZipFile = new File(here_path_string + "/" + filename + ".zip");
					f.renameTo(newZipFile);
					List<File> subFileList = upzipFile(newZipFile, here_path_string + "/" + filename + "(1)");
					if(subFileList != null && subFileList.size() > 0){
						outputLog("二级解压 " + filename + " 解压成功");
					}else{
						outputLog("二级解压 " + filename + " 解压失败");
					}
					newZipFile = null;
					f = null;
				}
			}
		}else{
			outputLog("主题包数据不符合规范，第二层解压失败------");
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

	private boolean isCleanSuucess() {
		// TODO Auto-generated method stub
		File contactsFile_folder = new File(here_path_string + "/" + contacts_folder);
		File incalluiFile_folder = new File(here_path_string + "/" + incallui_folder);
		File mmsFile_folder = new File(here_path_string + "/" + mms_folder);
		File phoneFile_folder = new File(here_path_string + "/" + phone_folder);
		File recorderFile_folder = new File(here_path_string + "/" + recorder_folder);
		File telecomFile_folder = new File(here_path_string + "/" + telecom_folder);
		File systemuiFile_folder = new File(here_path_string + "/" + systemui_folder);
		File launcherFile_folder = new File(here_path_string + "/" + launcher_folder);
		File iconsFile_folder = new File(here_path_string + "/" + icon_folder);
		List<File> fileList = new ArrayList<File>();
		fileList.add(contactsFile_folder);
		fileList.add(incalluiFile_folder);
		fileList.add(mmsFile_folder);
		fileList.add(phoneFile_folder);
		fileList.add(recorderFile_folder);
		fileList.add(telecomFile_folder);
		fileList.add(systemuiFile_folder);
		fileList.add(launcherFile_folder);
		fileList.add(iconsFile_folder);

		File contactsFile_zip = new File(here_path_string + "/" + contacts_zip);
		File incalluiFile_zip = new File(here_path_string + "/" + incallui_zip);
		File mmsFile_zip = new File(here_path_string + "/" + mms_zip);
		File phoneFile_zip = new File(here_path_string + "/" + phone_zip);
		File recorderFile_zip = new File(here_path_string + "/" + recorder_zip);
		File telecomFile_zip = new File(here_path_string + "/" + telecom_zip);
		File systemuiFile_zip = new File(here_path_string + "/" + systemui_zip);
		File launcherFile_zip = new File(here_path_string + "/" + launcher_zip);
		File iconsFile_zip = new File(here_path_string + "/" + icon_zip);
		fileList.add(contactsFile_zip);
		fileList.add(incalluiFile_zip);
		fileList.add(mmsFile_zip);
		fileList.add(phoneFile_zip);
		fileList.add(recorderFile_zip);
		fileList.add(telecomFile_zip);
		fileList.add(systemuiFile_zip);
		fileList.add(launcherFile_zip);
		fileList.add(iconsFile_zip);

		File previewFile_zip = new File(here_path_string + "/" + preview_zip);
		File unclockFile_zip = new File(here_path_string + "/" + unlock_zip);
		File wallperFile_zip = new File(here_path_string + "/" + wallpaper_zip);
		File descripFile_zip = new File(here_path_string + "/" + description_zip);

		fileList.add(previewFile_zip);
		fileList.add(unclockFile_zip);
		fileList.add(wallperFile_zip);
		fileList.add(descripFile_zip);
		boolean isClean = true;
		for (File f:fileList){
			if(f != null && f.exists()){
				outputLog(f.getName() + " 文件没有删除成功，请手动删除后再继续");
				isClean = false;
			}
		}
		return isClean;
	}

	private void deleteFold() {
		outputLog("正在删除旧的(1)后缀文件夹");
		File contactsFile = new File(here_path_string + "/" + contacts_folder);
		File incalluiFile = new File(here_path_string + "/" + incallui_folder);
		File mmsFile = new File(here_path_string + "/" + mms_folder);
		File phoneFile = new File(here_path_string + "/" + phone_folder);
		File recorderFile = new File(here_path_string + "/" + recorder_folder);
		File telecomFile = new File(here_path_string + "/" + telecom_folder);
		File systemuiFile = new File(here_path_string + "/" + systemui_folder);
		File launcherFile = new File(here_path_string + "/" + launcher_folder);
		File iconsFile = new File(here_path_string + "/" + icon_folder);
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
				if(deletefile(f.getAbsolutePath())){
					outputLog("旧的" + f.getName() + "刪除成功");
				}else{
					outputLog("旧的" + f.getName() + "刪除失败，请检查是否被占用");
				}
			}else{
				outputLog("找不到" + f.getName() + ",跳过该文件");
			}
		}
	}

	public void outputLog(String str){
		String log = "\n" + str;
		logText.append(log);
	}

	public void unZipFile(){

	}

	/**
	 * 对临时生成的文件夹和文件夹下的文件进行删除
	 */
	public boolean deletefile(String delpath) {
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
			return false;
		}
		return true;
	}

	public void deletePUWDFold(){
		outputLog("正在删除旧的preview、unclock、wallper文件夹");
		File previewFile = new File(here_path_string + "/" + preview_zip);
		File unclockFile = new File(here_path_string + "/" + unlock_zip);
		File wallperFile = new File(here_path_string + "/" + wallpaper_zip);
		File descripFile = new File(here_path_string + "/" + description_zip);
		List<File> fileList = new ArrayList<File>();
		fileList.add(previewFile);
		fileList.add(unclockFile);
		fileList.add(wallperFile);
		fileList.add(descripFile);

		for (File f:fileList){
			if(f != null && f.exists()){
				if(deletefile(f.getAbsolutePath())){
					outputLog("旧的" + f.getName() + "刪除成功");
				}
			}else{
				outputLog("找不到" + f.getName() + ",跳过该文件夹");
			}
		}
	}

	public void deleteZipFile(){
		outputLog("正在删除旧zip文件");
		File contactsFile = new File(here_path_string + "/" + contacts_zip);
		File incalluiFile = new File(here_path_string + "/" + incallui_zip);
		File mmsFile = new File(here_path_string + "/" + mms_zip);
		File phoneFile = new File(here_path_string + "/" + phone_zip);
		File recorderFile = new File(here_path_string + "/" + recorder_zip);
		File telecomFile = new File(here_path_string + "/" + telecom_zip);
		File systemuiFile = new File(here_path_string + "/" + systemui_zip);
		File launcherFile = new File(here_path_string + "/" + launcher_zip);
		File iconsFile = new File(here_path_string + "/" + icon_zip);
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
				if(f.delete()){
					outputLog("旧的" + f.getName() + "刪除成功");
				}else{
					outputLog("旧的" + f.getName() + "刪除删除失败，请手动删除，再次重新操作");
				}
			}else{
				outputLog("找不到" + f.getName() + ",跳过该文件");
			}
		}
	}

	/**
	 * 获取当前路径的主题包文件名，可以识别hwt或者hwt.zip后缀的主题包文件
	 * @return 当前路径下找到的第一个主题包文件名
	 */
	public String getThemPackPath(){
		File here_file = new File(here_path_string);
		File[] here_file_list = here_file.listFiles();
		if(here_file_list != null && here_file_list.length > 0){
			for(File f:here_file_list){
				String f_name = f.getName();
				if(f_name.endsWith(".hwt") || f_name.endsWith(".hwt.zip")){
					return f_name;
				}
			}
		}
		return null;
	}



}
