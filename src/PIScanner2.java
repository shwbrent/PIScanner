import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.*;
import java.util.Arrays;

import javax.swing.filechooser.FileNameExtensionFilter;


public class PIScanner2 implements ActionListener, PropertyChangeListener{
	
	BackgroundCalculator AnalyTask = new BackgroundCalculator();//concurrency
    File[] selectedFiles;
    boolean checkBoxNotNull;//判斷checkbox內容不為空
    File dir;
    JPanel pan = new JPanel();
    private JProgressBar progressBar = new JProgressBar(0,100);
    ImageIcon icon1 = new ImageIcon(getClass().getResource("/fd1.png"));//開檔Icon pixel=50*50
    ImageIcon icon2 = new ImageIcon(getClass().getResource("/save1.png"));//存檔Icon
    ImageIcon icon3 = new ImageIcon(getClass().getResource("/button1.png"));//開始掃描Icon
    ImageIcon icon4 = new ImageIcon(getClass().getResource("/personal1.png"));//JFrame Icon
    ImageIcon icon5 = new ImageIcon(getClass().getResource("/Cancel.png"));//Cancel Icon
    
    JFrame fr1 = new JFrame("可攜式文件之掃描個人機密資訊");
    Container cp=fr1.getContentPane();
    JMenuBar mb1 = new JMenuBar();
    JMenu mn1 = new JMenu("檔案");
    JMenu mn2 = new JMenu("說明");
    JMenuItem m11 = new JMenuItem("開啟檔案");
    JMenuItem m12 = new JMenuItem("儲存檔案");
    JMenuItem m21 = new JMenuItem("關於");
    JLabel jl1 = new JLabel("檔案",SwingConstants.CENTER);
    JLabel jl2 = new JLabel("選取個資種類",SwingConstants.CENTER);
    JLabel jl3 = new JLabel("編號：24　　指導老師：楊吳泉　副教授　　 製作組員：蘇泓瑋、何承澤、蔡忠霖、吳柏賢");
    JLabel jl4 = new JLabel("輸出結果：");
    JButton b1=new JButton("開始掃描");
    JButton b2=new JButton("取消");

    JCheckBox jcb1 = new JCheckBox("身分證字號");
    JCheckBox jcb2 = new JCheckBox("手機號碼");
    JCheckBox jcb3 = new JCheckBox("信用卡號");
    JCheckBox jcb4 = new JCheckBox("電子郵件信箱");
    JTextArea ta = new JTextArea();
    Font f1 = new Font("Arival Uncode MS", Font.BOLD, 24);
    Font f2 = new Font("Arival Uncode MS", Font.PLAIN, 22);
    Font f3 = new Font("標楷體", Font.PLAIN, 22);
    Font f4 = new Font("Arival Uncode MS", Font.PLAIN, 12);
    Font f5 = new Font("Arival Uncode MS", Font.PLAIN, 14);
    Font f6 = new Font("標楷體", Font.PLAIN, 20);
    Font f7 = new Font("標楷體", Font.PLAIN, 32);
    Font f8 = new Font("標楷體", Font.BOLD, 26);
    TextArea t1 =new TextArea("", 50, 40, TextArea.SCROLLBARS_VERTICAL_ONLY );
    PersonDataScan PDS = new PersonDataScan();    
   
    
    public PIScanner2() {
    	progressBar.setBounds(400, 10, 350, 25);
    	progressBar.setStringPainted(true);
    	cp.add(progressBar);
    	cp.add(pan);
    	pan.setBounds(25,45, 200, 300);
        pan.setBackground(Color.getHSBColor((float) 0.333333333334,(float) 0.13 ,(float) 1.00));
        pan.setBorder(BorderFactory.createLineBorder(Color.red, 1));
        pan.setLayout(null);
        Border line_a = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        TitledBorder fi = BorderFactory.createTitledBorder(line_a, "選取個資種類", TitledBorder.CENTER, TitledBorder.CENTER, f8, Color.BLUE);
        pan.setBorder(fi);
        pan.add(jcb1);
        pan.add(jcb2);
        pan.add(jcb3);
        pan.add(jcb4);
    	mn1.add(m11);
        mn1.add(m12);
        mn2.add(m21);
        mb1.add(mn1);
        mb1.add(mn2);	    	
        mn1.setFont(f1);
        m11.setFont(f2);
        m12.setFont(f2);
        mn2.setFont(f1);
        m21.setFont(f2);
        m11.addActionListener(this);
        m12.addActionListener(this);
        m21.addActionListener(this);	
        m11.setIcon(icon1);
        m12.setIcon(icon2);
        m11.setBackground(Color.getHSBColor((float) 0.334,(float) 0.76 ,(float) 0.80));
        m12.setBackground(Color.getHSBColor((float) 0.334,(float) 0.76 ,(float) 0.80));
        m21.setBackground(Color.getHSBColor((float) 0.334,(float) 0.76 ,(float) 0.80));
        mb1.setBackground(Color.getHSBColor((float) 0.334,(float) 0.76 ,(float) 0.80)); //JmenuBar的背景顏色
        ta.setFont(f2);
        ta.setEditable(false);
        fr1.setLocation(100, 100);
        fr1.setSize(1000, 800);
        fr1.setResizable(false);	    	
        fr1.getContentPane().setBackground(Color.getHSBColor((float) 0.333333333334,(float) 0.13 ,(float) 1.00)); //frame的背景顏色
        fr1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fr1.setLocationRelativeTo(null);
        fr1.setIconImage(icon4.getImage());
        b1.setIcon(icon3);
        b2.setIcon(icon5);
        fr1.setJMenuBar(mb1);
        fr1.setBounds(0,0,800,600);        
        cp.setLayout(null);  //取消預設之 BorderLayout
        t1.setBounds(250,40,500,400);
        t1.setFont(f2);
        t1.setEditable(false);
        t1.setText("請開啟要選的檔案!!");
        jcb1.setOpaque(false); //反之社TRUE時會設置背景圖
        
        jcb2.setOpaque(false);
        jcb3.setOpaque(false);
        jcb4.setOpaque(false);
        jl1.setFont(f3);
        jl3.setFont(f4);
        jl4.setFont(f8);
        b1.setFont(f4);
        b2.setFont(f4);
        jcb1.setFont(f6);
        jcb2.setFont(f6);
        jcb3.setFont(f6);
        jcb4.setFont(f6);
        b1.addActionListener(this);
        b2.addActionListener(this);
        jcb1.addActionListener(this);
        jcb2.addActionListener(this);
        jcb3.addActionListener(this);
        jcb4.addActionListener(this);
        jl3.setBounds(40,490,500,30);
        jl4.setBounds(250,10,150,30);
        b1.setBounds(70,380,110,60);
        b2.setBounds(70,380,110,60);
        b1.setBackground(Color.getHSBColor((float) 0.333333333334,(float) 0.13 ,(float) 1.00));
        b2.setBackground(Color.getHSBColor((float) 0.333333333334,(float) 0.13 ,(float) 1.00));
        jcb1.setBounds(30,40,180,50);
        jcb2.setBounds(30,100,180,50);
        jcb3.setBounds(30,160,180,50);
        jcb4.setBounds(30,220,180,50);
        jl1.setForeground(Color.BLUE);
        jl3.setForeground(Color.BLUE);
        jl4.setForeground(Color.BLUE);

        cp.add(t1);
        cp.add(jl1);
        cp.add(jl2);
        cp.add(jl3);
        cp.add(jl4);
        cp.add(b1);
        cp.add(b2);

        fr1.setVisible(true); 
        b2.setVisible(false);
    }

	/*
	 * BackgroundCalculator is using for Analysis PDF and check the progress Bar
	 * 
	 * */
    private class BackgroundCalculator extends SwingWorker<Void, Void>{

    	private int filesCount;
    	private float progressDist;
    	private String CalculatorDone = "掃描完成!!\n請選擇儲存資料夾!!";
    	private String CalculatorCancel = "掃描取消!!!!\n請重新開啟資料!!\n";
    	private long totalFiles;

    	/*
    	 * 設置每個檔案佔進度條的百分比
    	 * */
    	private void setFilesDist(long totalFiles) {
    		this.progressDist = PDS.getProgressBarDist(totalFiles);
    	}
    	
    	private void setFilesCount() {
    		filesCount = PDS.getProgressBarCount();
    	}
    	
    	private void setTotalFiles() {
    		totalFiles += filesCount;
    	}
    	
    	private void InitTotalFiles() {
    		totalFiles = 0;
    	}

    	@Override
    	protected Void doInBackground() throws Exception {
    		// TODO Auto-generated method stub
    		InitTotalFiles();
    		progressBar.setMaximum(100);
    		progressBar.setMinimum(0);
    		progressBar.setStringPainted(true);
            try{
                t1.setText("正在掃描...\n");
                for(File fs : selectedFiles) {
                	PDS.setFiles(fs.getAbsolutePath());
                	setFilesCount();
                	setTotalFiles();
                }
                setProgress(0);
                float progress = 0;
                for(File fs : selectedFiles){
                	t1.append(fs.getAbsolutePath()+"\n");
                	PDS.setFiles(fs.getAbsolutePath());
                	setFilesCount();
                	setFilesDist(totalFiles);
                	
                	for(int index=0; index < filesCount ; index++) {
                		if(isCancelled())return null;
                        PDS.AnalysisPDF(index);
                    	progress += progressDist;
                		System.out.println(progress+","+progressDist);
                    	setProgress(Math.min((int)Math.ceil(progress), 100));
                    	
                    	/*
                    	 * 在dpInBackgrepound.cancel 發生後
                    	 * Thread如果還在sleep，會造成thread sleep interruption
                    	 * 因此在thread.sleep 要多加一個try-catch 補嘬例外
                    	 * 否則用到上一層try-catch 將無法繼續Camce;事件
                    	 * */
                    	try {
                    		Thread.sleep(100);
                    	}
                    	catch(InterruptedException e) {
                    		Thread.currentThread().interrupt();
                    	}     
                	}            
                }
            }
            catch(Exception err){
            	t1.append("\n掃描錯誤...");
                System.out.println("在掃描時發生錯誤: "+err.toString());
            }      
    		return null;
    	}
    	/*
    	 * doInBackground執行完後會執行此處
    	 * 
    	 * */
    	protected void done() {
    		try{
    			t1.append( isCancelled() ? CalculatorCancel: CalculatorDone );
    			b2.setVisible(false);
    			b1.setVisible(true);
    			finalize();
    		}catch(Exception e) {
    			e.printStackTrace();
    		}catch(Throwable e) {
    			e.printStackTrace();
    		}
    	}
    }

    /*
     * While status changed and will process progress
     * */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		// TODO Auto-generated method stub
		if("progress" == evt.getPropertyName()) {	
            int progress = (int) evt.getNewValue();
            progressBar.setValue(progress);
		}
	}
	
	/*
	 * 檢查checkbox是否都沒有勾選，TRUE代表至少有一個被勾選
	 * */
	private void setCheckboxNotNull() {
		checkBoxNotNull = (jcb1.isSelected() || jcb2.isSelected() || jcb3.isSelected() || jcb4.isSelected()) ? true:false;
	}

    public void actionPerformed(ActionEvent e) {
 	
    	/*
    	 * Open file
    	 * */
        if (e.getSource() == m11) {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("."));
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "PDF ", "pdf");
            chooser.setFileFilter(filter);
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setMultiSelectionEnabled(true);
            
            try{
                if(!(Arrays.toString(selectedFiles).isEmpty()) && chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ){
                    selectedFiles =  chooser.getSelectedFiles();
                    t1.setText("正在開啟...\n");
                    for(File fs : selectedFiles)   
                        t1.append(fs.getAbsolutePath()+"\n");
                    t1.append("開啟檔案/資料夾完成!!\n"+
                            "請勾選所要掃描的個資後，按下掃描按鈕!");
                }else{
                    t1.setText("未選擇要掃描的檔案/資料夾!!");
                }
            }
            catch(Exception err){
                System.out.println("在開啟檔案時發生錯誤: "+err.toString());
            }
        }

        
        /*
         * Start to Scanning Button 
         * */
        if (e.getSource() == b1) {
        	setCheckboxNotNull();
        	if(selectedFiles != null) {
        		if(checkBoxNotNull) {
        			
	                PDS.setIfIdentity(jcb1.isSelected());
	                PDS.setIfCellPhone(jcb2.isSelected());
	                PDS.setIfCreditCard(jcb3.isSelected());
	                PDS.setIfEmail(jcb4.isSelected());
	                AnalyTask = new BackgroundCalculator();
	            	AnalyTask.addPropertyChangeListener(this);
	                AnalyTask.execute();
	                
	                b1.setVisible(false);
	                b2.setVisible(true);
        		}else {
        			t1.setText("請選取個資種類!!\n");
        		}
        	}else {
        		t1.setText("請開啟要掃描的檔案!!\n");
        	}
        }	
   
        
        /*
         * Cancel Button
         * */
        if(e.getSource() == b2) {
        	selectedFiles = null;
        	AnalyTask.cancel(true);
        	b2.setVisible(false);
        	b1.setVisible(true);
        }
     
        
        /*
         * Save file
         * */
        if(e.getSource()==m12){
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("."));
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            dir = chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION ? chooser.getSelectedFile():null;
            try{
                if(dir.exists()){
                    PDS.SaveToCSV(dir.getAbsolutePath()+"\\個資報表");
                    PDS.PersonDataClear();
                }
                else{
                    dir.mkdirs();
                    t1.setText("由於沒有此資料夾，因此創見了...\n\r"+dir.getAbsolutePath()+"\n");
                    PDS.SaveToCSV(dir.getAbsolutePath()+"\\pdfTest");
                    PDS.PersonDataClear();
                }
                t1.append("Excel報表在底下路徑:\n"+dir.getAbsolutePath()+"\n"+"\n完成存檔!!\n");
            }
            catch(Exception err){
                t1.setText("未選擇儲存的資料夾!!");
                System.out.println("在存檔時發生錯誤: "+err.toString());
            }
        }
        
        
        /*
         * About
         * */
        
        if (e.getSource() == m21) {
            JDialog dl = new JDialog(fr1, "關於本程式");
            JTextArea t2 = new JTextArea();
            t2.setEditable(false);
            t2.setFont(f2);
            t2.setText("編號：24\n" + "可攜式文件之掃描個人機密資訊\n\n" + "組員名單：\n" +
                            "     學號         "+"     姓名         \n"+
                            "10203021A	"+"吳柏賢\n" + 
                            "10203060A	"+"蘇泓瑋\n" + 
                            "10203075A	"+"何承澤\n"+
                            "10203114A	"+"蔡忠霖\n\n"+
                            "指導老師：楊吳泉 教授");
            t2.setBackground(Color.white);
            dl.setLocation(200, 200);
            dl.setSize(400, 400);
            dl.add(t2);
            dl.setVisible(true);
        }
    }
    
    public static void main(String[] args){
        JFrame.setDefaultLookAndFeelDecorated(true);
        PIScanner2 demo = new PIScanner2();
    }

}