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

	public String window_title = "��Ϊ�����Զ��������";
	public String use_help_str = "(1)�ù��ߵĿ��������ǻ���java jdk-1.6�����ģ�����1.6�汾��ȷ����������\n" +
			"(2) ʹ��\"�������\"����֮ǰ��������Ҫȷ�����ĵ����Ѿ���װ�ˡ��ƽ�������ʽ\n" +
			"    ���WinRAR����\n\n" +
			"(3) ʹ��˵��\n" +
			"   ���ȰѸù��߷��ں���Ҫ��ѹ�������ͬĿ¼��\n\n" + 
			"   ����ѹ���⡯---�Ѹù��߷�������Ҫ��ѹ��.hwt��׺����.hwt.zip��׺�������\n" + 
			"   ͬĿ¼�£��������ѹ���⡯����һ����ѹ��������������ѹ��zip�ļ��Ͷ�����ѹ\n" + 
			"   �Ĵ���(1)����׺���ļ���(Ϊ�˲��ʹ��ʱ���ɵ��ļ����Ƴ�ͻ�����Լ��ˣ�1����׺)\n" + 
			"   �������Ҫʹ��һ������Ĺ��ܣ��벻Ҫ�޸���Щ�ļ��е�����\n\n" +
			"   ��������⡯---�����Զ�������ܣ��ܰ�ͨ���ù��߽�ѹ�������ļ��н���һ�������\n" + 
			" \".hwt\"��׺���������ֻ��Ҫ�����������⡯��ť���ɣ�������������ѹ������\n" + 
			"   ��\"��1��\"��׺���ļ���\n\n" +
			"   ͬ��Contacts�����������ļ���---�ù�������Ի�Ϊ5.0����������ԵĹ��ܣ���\n" + 
			"   Contacts�е�framework-res-hwext��Դ��������ģ���е��ļ�������һ����\n" + 
			"   ��Դ�������û���������󣬿���ͨ���ù��ܽ���һ��ͬ������ȥ�ظ�����ճ�����鷳�� \n\n" +
			"\n------�ù�����   С��֮��   ����뿪��------\n\n";

	public String here_path_string = "";	//����Ŀ¼

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
		pack_button = new Button("�������");
		pack_button.setPreferredSize(new Dimension(400, 30));
		unPack_button = new Button("��ѹ����");
		unPack_button.setPreferredSize(new Dimension(400, 30));
		syncFile_button = new Button("ͬ��Contacts�е�framework-res-hwext�������ļ���");
		syncFile_button.setPreferredSize(new Dimension(400, 30));
		help_button = new Button("ʹ�ð���");
		help_button.setPreferredSize(new Dimension(400, 30));
		power = new JLabel("�ù����ɡ�С��֮�ҡ�����������---�������Ⱥ��361808483");

		clean_log = new Button("�����־");

		logText = new JTextArea(3, 20);  
		logText.setLineWrap(true);// ������ݹ������Զ�����
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
		here_file = null;	//�ͷ���Դ


		addActionListener();
	}

	private String getConfigText(){
		StringBuilder result = new StringBuilder();
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(here_path_string + "/" + "config.txt")));//����һ��BufferedReader������ȡ�ļ�
			String s = null;
			while((s = br.readLine())!=null){//ʹ��readLine������һ�ζ�һ��
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
		pack_button.addActionListener(new ActionListener() {		//�������
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				deleteRZI();
				toAddThem();
			}
		});
		unPack_button.addActionListener(new ActionListener() {		//��ѹ����

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				deleteRZI();
				String themPackName = getThemPackPath();
				if(themPackName == null || themPackName.isEmpty()){
					outputLog("�Ҳ���.hwt����.hwt.zip��׺�������,������������Ƹ�ʽ�Ƿ���ȷ");
				}else{
					outputLog("�ҵ��������" + getThemPackPath() + "   ��ʼ��ѹ");
					toUnThemPack(themPackName);
				}
			}
		});
		syncFile_button.addActionListener(new ActionListener() {		//ͬ���ļ�
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				deleteRZI();
				syncContactFile();
			}
		});
		help_button.addActionListener(new ActionListener() {		//����

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
		outputLog("����ɾ�����ļ��ļ�");
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
					outputLog("ɾ��" + f.getName() + "�ɹ�");
				}else{
					outputLog("ɾ��" + f.getName() + "ʧ��");
				}
			}else if(f.exists() && f.isDirectory()){
				if(!f_1.exists()){
					if(f.renameTo(f_1)){
						outputLog(f.getName() + "������Ϊ" + f_1.getName() + "�ɹ�");
					}else{
						outputLog(f.getName() + "������Ϊ" + f_1.getName() + "ʧ��");
					}
				}else{
					if(deletefile(f.getAbsolutePath())){
						outputLog(f.getName() + "��ɾ���ɹ�");
					}else{
						outputLog(f.getName() + "ɾ��ʧ��");
					}
				}
			}else{
				outputLog("ɾ�������ļ��ɹ�");
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
			outputLog("����쳣��" + e.getMessage());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			outputLog("����쳣��" + e.getMessage());
		}
	}

	public void packageSourse() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		outputLog("��ʼ����hwt------");
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
		outputLog("Ĭ��·�����Ҳ���WinRAR��exe�������ֶ�ѡ��");
		JOptionPane.showMessageDialog(null, winrar_path + " �Ҳ���,���ֶ�ѡ������WinRAR.exe����" );
		outputLog(winrar_path);
		JFileChooser jfc=new JFileChooser();  
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  

		jfc.showDialog(new JLabel(), "�ֶ�ѡ��WinRAR.exe����");  
		File file=jfc.getSelectedFile();  
		if(file == null || file.isDirectory()){  
			outputLog("������Ч��WinRAR.exe����");  
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
				outputLog("������Ч��WinRAR.exe����");
			}
		}  
		//System.out.println(jfc.getSelectedFile().getName());
	}

	private void beginToApck() throws IOException, InterruptedException {
		outputLog("�Ѿ��ҵ�WinRAR.exe, ��ʼ���------");
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
							outputLog("���ڿ����쳣���ļ��������ļ�����" + subFileList[i].getName() + ",�Ѿ��������ļ��Ĵ���������ȷ�����ļ�û�����⣬���ֶ���Ӹ��ļ�����Ӧ�İ���");
							continue;
						}
						file_list_buffer.append(" " + subFileList[i].getName());
					}
					File workFile = new File(f.getName());
					outputLog("���ڴ��" + packageName);
					Process process = Runtime.getRuntime().exec(winrar_path + " a " + packageName + ".zip" + file_list_buffer.toString(), null, workFile);
					try {
						process.waitFor();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						outputLog("�����ʱ---���ʧ�ܣ������ļ���,���߷����ô������С��֮�ҡ�");
					}
					File subFile = new File(f.getAbsolutePath() + "/" + packageName + ".zip");
					if(subFile.renameTo(new File(here_path_string + "/" + packageName.replace(".zip", "")))){
						String them_pack_name = getThemPackPath();
						if(them_pack_name != null && !them_pack_name.isEmpty()){
							if(them_pack_name.endsWith(".hwt")){
								File them_pack_file = new File(here_path_string + "/" + them_pack_name);
								if(them_pack_file.renameTo(new File(here_path_string + "/" + them_pack_name + ".zip"))){
									outputLog("�����" + them_pack_name + "������Ϊ" + them_pack_name + ".zip�ɹ�");
									them_pack_name = them_pack_name + ".zip";
								}else{
									outputLog("�����" + them_pack_name + "������Ϊ" + them_pack_name + ".zipʧ�ܣ����ֶ��޸������Ϊ.hwt.zip��׺");
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
						outputLog(subPckageName + "����ɹ�");
					}else{
						System.out.println(packageName + " ���ʧ��");
					}
				}
			}else{
				outputLog("�Ҳ��� " + f.getName() + " ��������ܲ��������Ļ�Ϊ������");
			}
		}

		StringBuffer folder_str = new StringBuffer();
		String them_pack_name = getThemPackPath();
		if(them_pack_name != null && !them_pack_name.isEmpty()){
			if(them_pack_name.endsWith(".hwt")){
				File them_pack_file = new File(here_path_string + "/" + them_pack_name);
				if(them_pack_file.renameTo(new File(here_path_string + "/" + them_pack_name + ".zip"))){
					outputLog("�����" + them_pack_name + "������Ϊ" + them_pack_name + ".zip�ɹ�");
					them_pack_name = them_pack_name + ".zip";
				}else{
					outputLog("�����" + them_pack_name + "������Ϊ" + them_pack_name + ".zipʧ�ܣ����ֶ��޸������Ϊ.hwt.zip��׺");
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
				outputLog("�Ҳ��� " + f.getName() + " ��������ܲ��������Ļ�Ϊ������");
			}
		}
		Process process2 = Runtime.getRuntime().exec(winrar_path + " a " + them_pack_name + folder_str);
		outputLog("����ɹ������ڽ������ĸ�������---");
		File latestPack_zip = new File(here_path_string + "/" + them_pack_name);
		File latestPack = new File(latestPack_zip.getAbsolutePath().replace(".zip", ""));
		if(latestPack.renameTo(latestPack)){
			outputLog("���������ɹ��� " + them_pack_name.replace(".zip", ""));
		}else{
			JOptionPane.showMessageDialog(null, "������˼�����һ�����ڳ��˵�С���⣬���ֶ���" + them_pack_name + "����\n��Ϊ " + them_pack_name.replace(".zip", "") + "�ʹ󹦸����");  
			outputLog("������˼�����һ�����ڳ��˵�С���⣬���ֶ���" + them_pack_name + "������Ϊ " + them_pack_name.replace(".zip", "") + "�ʹ󹦸����");
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
			outputLog("\n׼�����������ڽ�ѹ�����Ե�\n");
			unThemPack(themPackName);
		}else{
			outputLog("");
		}
	}

	private void unThemPack(String themPackName) {
		// TODO Auto-generated method stub
		if(themPackName.endsWith(".hwt")){
			outputLog("���������� " + themPackName + "Ϊ" + themPackName + ".zip");
			File packFile = new File(here_path_string + "/" + themPackName);
			File newFile = new File(here_path_string + "/" + themPackName + ".zip");
			if(packFile.renameTo(newFile)){
				outputLog("�������ɹ�");
				themPackName = themPackName + ".zip";
			}else{
				outputLog("������ʧ�ܣ��ļ����ܱ���������ռ����,�����ֶ����.zip��׺");
				return;
			}
		}
		File zipPackFile = new File(here_path_string + "/" + themPackName);
		List<File> packFileList = upzipFile(zipPackFile, here_path_string);
		if(packFileList != null && packFileList.size() > 0){
			outputLog("\n��һ���ѹ�ɹ������ڽ��еڶ����ѹ------\n");
			unSubZipFileList(packFileList);
		}else{
			outputLog("���������Ϊ��");
		}
	}

	public void syncContactFile() {
		// TODO Auto-generated method stub
		outputLog("����ͬ��contactsģ���framework-res-hwext�ļ��е�����ģ��\n");
		String fromPath = here_path_string + "/com.android.contacts(1)/framework-res-hwext";
		if(copyDirectory(fromPath, here_path_string + "/com.android.incallui(1)/framework-res-hwext", true)){
			outputLog("��Contactscom.android.contacts(1)  ͬ����        com.android.incallui(1)�ɹ�");
		}else{
			outputLog("��Contactscom.android.contacts(1)  ͬ����        com.android.incallui(1)ʧ��");
		}
		if(copyDirectory(fromPath, here_path_string + "/com.android.mms(1)/framework-res-hwext", true)){
			outputLog("��Contactscom.android.contacts(1)  ͬ����        com.android.mms(1)�ɹ�");
		}else{
			outputLog("��Contactscom.android.contacts(1)  ͬ����        com.android.mms(1)ʧ��");
		}
		if(copyDirectory(fromPath, here_path_string + "/com.android.phone(1)/framework-res-hwext", true)){
			outputLog("��Contactscom.android.contacts(1)  ͬ����        com.android.phone(1)�ɹ�");
		}else{
			outputLog("��Contactscom.android.contacts(1)  ͬ����        com.android.phone(1)ʧ��");
		}
		if(copyDirectory(fromPath, here_path_string + "/com.android.phone.recorder(1)/framework-res-hwext", true)){
			outputLog("��Contactscom.android.contacts(1)  ͬ����        com.android.phone.recorder(1)�ɹ�");
		}else{
			outputLog("��Contactscom.android.contacts(1)  ͬ����        com.android.phone.recorder(1)ʧ��");
		}
		if(copyDirectory(fromPath, here_path_string + "/com.android.server.telecom(1)/framework-res-hwext", true)){
			outputLog("��Contactscom.android.contacts(1)  ͬ����        com.android.server.telecom(1)�ɹ�");
		}else{
			outputLog("��Contactscom.android.contacts(1)  ͬ����        com.android.server.telecom(1)ʧ��");
		}
		if(copyDirectory(fromPath, here_path_string + "/com.android.systemui(1)/framework-res-hwext", true)){
			outputLog("��Contactscom.android.contacts(1)  ͬ����       com.android.systemui(1)�ɹ�\n");
		}else{
			outputLog("��Contactscom.android.contacts(1)  ͬ����        com.android.systemui(1)ʧ��\n");
		}
	}

	/** 
	 * ��������Ŀ¼������ 
	 *  
	 * @param srcDirName 
	 *            ������Ŀ¼��Ŀ¼�� 
	 * @param destDirName 
	 *            Ŀ��Ŀ¼�� 
	 * @param overlay 
	 *            ���Ŀ��Ŀ¼���ڣ��Ƿ񸲸� 
	 * @return ������Ƴɹ�����true�����򷵻�false 
	 */  
	public static boolean copyDirectory(String srcDirName, String destDirName,  
			boolean overlay) {  
		// �ж�ԴĿ¼�Ƿ����  
		File srcDir = new File(srcDirName);  
		if (!srcDir.exists()) {  
			MESSAGE = "����Ŀ¼ʧ�ܣ�ԴĿ¼" + srcDirName + "�����ڣ�";  
			JOptionPane.showMessageDialog(null, MESSAGE);  
			return false;  
		} else if (!srcDir.isDirectory()) {  
			MESSAGE = "����Ŀ¼ʧ�ܣ�" + srcDirName + "����Ŀ¼��";  
			JOptionPane.showMessageDialog(null, MESSAGE);  
			return false;  
		}  

		// ���Ŀ��Ŀ¼���������ļ��ָ�����β��������ļ��ָ���  
		if (!destDirName.endsWith(File.separator)) {  
			destDirName = destDirName + File.separator;  
		}  
		File destDir = new File(destDirName);  
		// ���Ŀ���ļ��д���  
		if (destDir.exists()) {  
			// �����������ɾ���Ѵ��ڵ�Ŀ��Ŀ¼  
			if (overlay) {  
				new File(destDirName).delete();  
			} else {  
				MESSAGE = "����Ŀ¼ʧ�ܣ�Ŀ��Ŀ¼" + destDirName + "�Ѵ��ڣ�";  
				JOptionPane.showMessageDialog(null, MESSAGE);
				return false;  
			}  
		} else {  
			// ����Ŀ��Ŀ¼  
			System.out.println("Ŀ��Ŀ¼�����ڣ�׼������������");  
			if (!destDir.mkdirs()) {  
				System.out.println("����Ŀ¼ʧ�ܣ�����Ŀ��Ŀ¼ʧ�ܣ�");  
				return false;  
			}  
		}  

		boolean flag = true;  
		File[] files = srcDir.listFiles();  
		for (int i = 0; i < files.length; i++) {  
			// �����ļ�  
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
			MESSAGE = "����Ŀ¼" + srcDirName + "��" + destDirName + "ʧ�ܣ�";  
			JOptionPane.showMessageDialog(null, MESSAGE);  
			return false;  
		} else {
			return true;  
		}  
	}  

	private static String MESSAGE = ""; 

	/** 
	 * ���Ƶ����ļ� 
	 *  
	 * @param srcFileName 
	 *            �����Ƶ��ļ��� 
	 * @param descFileName 
	 *            Ŀ���ļ��� 
	 * @param overlay 
	 *            ���Ŀ���ļ����ڣ��Ƿ񸲸� 
	 * @return ������Ƴɹ�����true�����򷵻�false 
	 */  
	public static boolean copyFile(String srcFileName, String destFileName,  
			boolean overlay) {  
		File srcFile = new File(srcFileName);  

		// �ж�Դ�ļ��Ƿ����  
		if (!srcFile.exists()) {  
			MESSAGE = "Դ�ļ���" + srcFileName + "�����ڣ�";  
			JOptionPane.showMessageDialog(null, MESSAGE);  
			return false;  
		} else if (!srcFile.isFile()) {  
			MESSAGE = "�����ļ�ʧ�ܣ�Դ�ļ���" + srcFileName + "����һ���ļ���";  
			JOptionPane.showMessageDialog(null, MESSAGE);  
			return false;  
		}  

		// �ж�Ŀ���ļ��Ƿ����  
		File destFile = new File(destFileName);  
		if (destFile.exists()) {  
			// ���Ŀ���ļ����ڲ�������  
			if (overlay) {  
				// ɾ���Ѿ����ڵ�Ŀ���ļ�������Ŀ���ļ���Ŀ¼���ǵ����ļ�  
				new File(destFileName).delete();  
			}  
		} else {  
			// ���Ŀ���ļ�����Ŀ¼�����ڣ��򴴽�Ŀ¼  
			if (!destFile.getParentFile().exists()) {  
				// Ŀ���ļ�����Ŀ¼������  
				if (!destFile.getParentFile().mkdirs()) {  
					// �����ļ�ʧ�ܣ�����Ŀ���ļ�����Ŀ¼ʧ��  
					return false;  
				}  
			}  
		}  

		// �����ļ�  
		int byteread = 0; // ��ȡ���ֽ���  
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

					outputLog("������������ " + filename + "Ϊ" + filename + ".zip");
					File newZipFile = new File(here_path_string + "/" + filename + ".zip");
					f.renameTo(newZipFile);
					List<File> subFileList = upzipFile(newZipFile, here_path_string + "/" + filename + "(1)");
					if(subFileList != null && subFileList.size() > 0){
						outputLog("������ѹ " + filename + " ��ѹ�ɹ�");
					}else{
						outputLog("������ѹ " + filename + " ��ѹʧ��");
					}
					newZipFile = null;
					f = null;
				}
			}
		}else{
			outputLog("��������ݲ����Ϲ淶���ڶ����ѹʧ��------");
		}

	}

	/**
	 94      * ��.zip�ļ����н�ѹ��
	 95      * @param zipFile  ��ѹ���ļ�
	 96      * @param descDir  ѹ����Ŀ���ַ���磺D:\\���� �� /mnt/d/����
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
				outputLog(f.getName() + " �ļ�û��ɾ���ɹ������ֶ�ɾ�����ټ���");
				isClean = false;
			}
		}
		return isClean;
	}

	private void deleteFold() {
		outputLog("����ɾ���ɵ�(1)��׺�ļ���");
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
					outputLog("�ɵ�" + f.getName() + "�h���ɹ�");
				}else{
					outputLog("�ɵ�" + f.getName() + "�h��ʧ�ܣ������Ƿ�ռ��");
				}
			}else{
				outputLog("�Ҳ���" + f.getName() + ",�������ļ�");
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
	 * ����ʱ���ɵ��ļ��к��ļ����µ��ļ�����ɾ��
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
		outputLog("����ɾ���ɵ�preview��unclock��wallper�ļ���");
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
					outputLog("�ɵ�" + f.getName() + "�h���ɹ�");
				}
			}else{
				outputLog("�Ҳ���" + f.getName() + ",�������ļ���");
			}
		}
	}

	public void deleteZipFile(){
		outputLog("����ɾ����zip�ļ�");
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
					outputLog("�ɵ�" + f.getName() + "�h���ɹ�");
				}else{
					outputLog("�ɵ�" + f.getName() + "�h��ɾ��ʧ�ܣ����ֶ�ɾ�����ٴ����²���");
				}
			}else{
				outputLog("�Ҳ���" + f.getName() + ",�������ļ�");
			}
		}
	}

	/**
	 * ��ȡ��ǰ·����������ļ���������ʶ��hwt����hwt.zip��׺��������ļ�
	 * @return ��ǰ·�����ҵ��ĵ�һ��������ļ���
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
