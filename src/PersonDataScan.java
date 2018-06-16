import java.io.*;

import java.util.regex.*;


import java.util.ArrayList;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * 讀入PDF檔案和個資判別函式的類別
 * 
 * @author shwbrent
 * 
 */
public class PersonDataScan {
	
	/*如果開啟資料夾，這裡會將資料夾下所有檔案(包含子資料夾)存進去*/
	protected ArrayList<String> BufferedFolder;
	
    private boolean ifEmail;
    
    private boolean ifCashCard;
    
    private boolean ifCellPhone;
    
    private boolean ifIdentity;
    /**
     * 經正規表示法篩選後的暫存資訊
     */
    private Matcher m;
    /**
     * 電子郵件的數量 
     */
    private int EmailCount;
    /**
     * 信用卡數量
     */
    private int CashCardCount;
    /**
     * 身份證數量 
     */
    private int IdentityCount;
    /**
     * 手機號碼數量 
     */
    private int CellphoneCount;
    /**
     * 電子郵件的容器大小
     */
    private int Email_resultCount;
    /**
     * 信用卡容器的大小
     */
    private int CashCard_resultCount;
    /**
     * 身份證容器的大小
     */
    private int Identity_resultCount;
    /**
     * 手機號碼容器的大小
     */
    private int Cellphone_resultCount;
    /**
     * Excel的編碼方式
     */
    private String CsvEncoding = "MS950";
    /**
     * 手機號碼電信商的暫存字串
     */
    private String infoPhone;
    /**
     * 未經篩選的原始資料
     */
    private String data;
    /**
     * 電子郵件的容器
     */
    private final ArrayList<String> Email_result = new ArrayList<>();
    /**
     * 信用卡的容器
     */
    private final ArrayList<String> CashCard_result = new ArrayList<>();
    /**
     * 身份證的容器
     */
    private final ArrayList<String> Identity_result = new ArrayList<>();
    /**
     * 手機號碼的容器
     */
    private final ArrayList<String> Cellphone_result = new ArrayList<>();
    
    protected void PersonDataClear(){
        this.Email_result.clear();
        this.Identity_result.clear();
        this.CashCard_result.clear();
        this.Cellphone_result.clear();
    }
    
    protected void setIfEmail(boolean ifEmail){
        this.ifEmail = ifEmail;
    }
    
    protected void setIfCashCard(boolean ifCashCard){
        this.ifCashCard = ifCashCard;
    }

    protected void setIfIdentity(boolean ifIdentity){
        this.ifIdentity = ifIdentity;
    }
    
    protected void setIfCellPhone(boolean ifCellPhone){
        this.ifCellPhone = ifCellPhone;
    }

    private void setMEmail(){
        this.m = Pattern.compile("\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z]+").matcher(this.data);
    }
    
    private void setMCashCard(){
        this.m = Pattern.compile("((\\d{4}-){3}\\d{3,4})|\\d{14,16}").matcher(this.data);
    }
    
    private void setMIdentity(){
        this.m = Pattern.compile("[A-Z][0-9]{9}").matcher(this.data);
    }
    
    private void setMCellphone(){
        this.m = Pattern.compile("09[0-9]{8}").matcher(this.data);
    }
    
    private void setEmailCount(){
        this.EmailCount = Email_result.size()/4;
        this.Email_resultCount = Email_result.size();
    }
    
    private void setCashCardCount(){
        this.CashCardCount = CashCard_result.size()/4;
        this.CashCard_resultCount = CashCard_result.size();
    }
    
    private void setIdentityCount(){
        this.IdentityCount = Identity_result.size()/4;
        this.Identity_resultCount = Identity_result.size();
    }
    
    private void setCellphoneCount(){
        this.CellphoneCount = Cellphone_result.size()/5;
        this.Cellphone_resultCount = Cellphone_result.size();
    }
    
    private void setInfoPhone(String infoPhone){
        this.infoPhone = infoPhone;
    }
    
    public void setCsvEncoding(String CsvEncoding){
        this.CsvEncoding = CsvEncoding;
    }
   
    public String getCsvEncoding(){
        return this.CsvEncoding;
    }
    
  
    public int getEmailCount(){
        return this.EmailCount;
    }
 
    public int getCashCardCount(){
        return this.CashCardCount;
    }
    
    public int getIdentityCount(){
        return this.IdentityCount;
    }
    
    public int getCellphoneCount(){
        return this.CellphoneCount;
    }
   
    public ArrayList<String> getEmailData(){
        return this.Email_result;
    }
   
    public ArrayList<String> getCashCardData(){
        return this.CashCard_result;
    }
   
    public ArrayList<String> getIdentityData(){
        return this.Identity_result;
    }
    
    public ArrayList<String> getCellphoneData(){
        return this.Cellphone_result;
    }
    
    public boolean CellPhone(String str){
        boolean result=false;
        for(String ch:CellData.cellphone_chungwha){
            if(ch.regionMatches(0, str, 0, ch.length())){
                setInfoPhone("Chung_Wha");
                result = true;
                break;
            }
        }
        for(String ch:CellData.cellphone_taiwan){
            if(ch.regionMatches(0, str, 0, ch.length())){
                setInfoPhone("Taiwan_Mobile");
                result = true;
                break;
            }
        }
        for(String ch:CellData.cellphone_farestTone){
            if(ch.regionMatches(0, str, 0, ch.length())){
                setInfoPhone("Farest Tone");
                result = true;
                break;
            }
        }
        for(String ch:CellData.cellphone_AsiaPacificTelecom){
            if(ch.regionMatches(0, str, 0, ch.length())){
                setInfoPhone("Asia Pacific Telecom");
                result = true;
                break;
            }
        }
        for(String ch:CellData.cellphone_TaiwanStarTelecom){
            if(ch.regionMatches(0, str, 0, ch.length())){
                setInfoPhone("Taiwan Star Telecom");
                result = true;
                break;
            }
        }
    return result;
    }
   
    public static boolean IdentityCard(String ID){
        boolean result;
        int[] IntArray =new int[11];
        int sum = 0;
        for(int i=0; i<26; i++){
            if(CellData.IdentityCard_enNum.charAt(i) == ID.charAt(0)){
               IntArray[0]=(10+i)/10;
               IntArray[1]=(10+i)%10;
               break;
            }
        }
        for(int i=1; i<ID.length(); i++){
           IntArray[i+1] = ID.charAt(i)-'0';
        }   
        for(int i=0; i<IntArray.length; i++){
           sum += IntArray[i]*CellData.IdentityCard_idSeq[i];
        }
        result = sum % 10 == 0;
        return result;
    }
 
    public static String CashTestToString(String str){
        String result;
        if(str.matches("^(\\d{4}-){3}\\d{3,4}$")){
            StringBuilder sb = new StringBuilder(str);
            for(int i=1; i<=3; i++){
                sb.deleteCharAt(4*i);
            }
            result = sb.toString();
        }
        else if(str.matches("^\\d{14,16}$")){
            result = str;
        }
        else{
            result = "";
        }
        return result;
    }
   
    public String CashCardType(String str){
        String result="";
        String test = str;
        
        if(CashCard(str)){
            test = test.substring(0, 4);
            int num = Integer.parseInt(test);

            if(num >= 3000 && num <= 3059 || 
               num >= 3600 && num <= 3699 ||
               num >= 3800 && num <= 3889){
                result = "Diners Club";
            }
            else if(
                    num >= 3400 && num <= 3499 || 
                    num >= 3700 && num <= 3799){
                    result = "American Express";
            }
            else if(num >= 3528 && num <= 3589 || num ==2131 || num ==1800){
                result = "JCB";
            }
            else if(num >= 3890 && num <= 3899){
                result = "Carte Blanche";
            }
            else if(num >= 4000 && num <= 4999){
                result = "Visa";
            }
            else if(num >= 5100 && num <= 5599){
                result = "MasterCard";
            }
            else if(num == 6011){
                result = "Discover / Novus";
            }
            else{
                result = "符合判別規則，可能是未知的銀行!";
            }
        }
        return result;
    }
   
    public static boolean CashCard(String str){
        String test = CashTestToString(str);
        int []num = new int[test.length()];
        int temp=0;
        boolean result ;
        if(!test.isEmpty()){
            int count = 0;
            for(int i=count; i<test.length(); i++){
                num[i] = test.charAt(i)-'0';
            }
            for(int i=num.length-2; i>=0; i-=2){
                num[i]*=2;
            }
            for(int i=0; i<num.length; i++){
                temp += num[i]>9 ? num[i]-9:num[i];
            }
            result = ( temp%10 == 0 );
        }else{
            result = false;
        }
        return result;
    }
    
    public boolean SaveToText(String absolutePath){
        boolean result = true;
        BufferedWriter wr;
        File out_fs = new File(absolutePath+".txt");
        int count=1;
        while(out_fs.exists())out_fs =new File(absolutePath+"("+Integer.toString(count++)+")"+".txt");
        try{
            wr = new BufferedWriter(new FileWriter(out_fs,false));
            for(int i=0; i<Email_resultCount; i++){
                wr.write(Email_result.get(i));
                wr.newLine();
            }
            for(int i=0; i<Identity_resultCount; i++){
                wr.write(Identity_result.get(i));
                wr.newLine();
            }
            for(int i=0; i<CashCard_resultCount; i++){
                wr.write(CashCard_result.get(i));
                wr.newLine();
            }
            for(int i=0; i<Cellphone_resultCount; i++){
                wr.write(Cellphone_result.get(i));
                wr.newLine();
            }
            wr.flush();
            wr.close();
        }
        catch(Exception e){
            System.out.println(e.toString());
            result = false;
        }
        return result;
    }
   
    public boolean SaveToCSV(String absolutePath){
        boolean result = true;
        BufferedWriter wr;
        File out_fs = new File(absolutePath+".csv");
        int count = 1;
        while(out_fs.exists())out_fs =new File(absolutePath+"("+Integer.toString(count++)+")"+".csv");
        try{
            String Separated;
            wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out_fs),CsvEncoding));
            wr.write("檔案所在路徑"+",,,,個資"+",,,,第幾頁"+",第幾行"+",其他資訊");
            wr.write("\n\n");
            if(Email_resultCount!=0)wr.write("電子郵件\n");
            for(int i=0; i<Email_resultCount; i++){
                Separated = (i+4)%4==0||(i+3)%4==0 ? ",,,,":",";          
                wr.write("\""+Email_result.get(i)+"\""+Separated);
                if((i+1)%4==0)wr.newLine();
            }
            wr.newLine();
            if(Identity_resultCount!=0)wr.write("身份證\n");
            for(int i=0; i<Identity_resultCount; i++){
                Separated = (i+4)%4==0||(i+3)%4==0 ? ",,,,":",";
                wr.write("\""+Identity_result.get(i)+"\""+Separated);
                if((i+1)%4==0)wr.newLine();
            }
            wr.newLine();
            if(CashCard_resultCount!=0)wr.write("信用卡\n");
            for(int i=0; i<CashCard_resultCount; i++){
                Separated = (i+5)%5==0||(i+4)%5==0 ? ",,,,":",";
                if(CashCard(CashCard_result.get(i))){
                    wr.write("=\""+CashCard_result.get(i)+"\""+Separated);
                }
                else{
                    wr.write("\""+CashCard_result.get(i)+"\""+Separated);
                }
                if((i+1)%5==0)wr.newLine();
            }
            wr.newLine();
            if(Cellphone_resultCount!=0)wr.write("手機號碼\n");
            for(int i=0; i<Cellphone_resultCount; i++){
                Separated = (i+5)%5==0||(i+4)%5==0 ? ",,,,":",";
                if(Cellphone_result.get(i).matches("^09[0-9]{8}$")){
                    wr.write("=\""+Cellphone_result.get(i)+"\""+Separated);
                }
                else{
                    wr.write("\""+Cellphone_result.get(i)+"\""+Separated);
                }
                if((i+1)%5==0)wr.newLine();
            }
            Separated = Integer.toString(EmailCount+IdentityCount+CashCardCount+CellphoneCount);
            wr.newLine();
            wr.write("個人資訊總數:,,"+Separated+"個\n\n"
            +"電子郵件:,,"+EmailCount+"個\n"
            +"身份證:,,"+IdentityCount+"個\n"
            +"信用卡:,,"+CashCardCount+"個\n"
            +"手機號碼:,,"+CellphoneCount+"個\n");
            wr.flush();
            wr.close();
        }
        catch(Exception e){
            System.out.println(e.toString());
            result = false;
        }
        return result;
    }
    
   
    public static ArrayList<String> StripFolderFiles(String folder){
        ArrayList<String> files = new ArrayList<>();
        try{
            File fs = new File(folder);
            if(fs.exists()&&fs.canRead()){
                if(fs.isDirectory()){
                    for(File str: fs.listFiles()){
                        files.addAll(StripFolderFiles(str.getAbsolutePath()));
                    }
                }else{
                    files.add(folder);
                }
            }else{
                System.out.println("This file isn't exist or permission denied!!");
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        return files;
    }
   
    public void MatchEmailData(String dir, int pageNum, int line){
        this.setMEmail();
        while(this.m.find()){
            Email_result.add(dir);
            Email_result.add(this.m.group());
            Email_result.add(Integer.toString(pageNum));
            Email_result.add(Integer.toString(line));
        }
    }
 
    public void MatchCellphoneData(String dir, int pageNum, int line){
        this.setMCellphone();
        while(this.m.find() && this.CellPhone(this.m.group())){
           Cellphone_result.add(dir);
           Cellphone_result.add(this.m.group());
           Cellphone_result.add(Integer.toString(pageNum));
           Cellphone_result.add(Integer.toString(line));
           Cellphone_result.add(this.infoPhone);
        }
    }
   
    public void MatchIdentityData(String dir, int pageNum, int line){
        this.setMIdentity();
        while(this.m.find() && IdentityCard(this.m.group())){
            Identity_result.add(dir);
            Identity_result.add(this.m.group());
            Identity_result.add(Integer.toString(pageNum));
            Identity_result.add(Integer.toString(line));
        }
    }
    
    
    public void MatchCashCardData(String dir, int pageNum, int line){
        this.setMCashCard();
        while(this.m.find() && CashCard(this.m.group())){
            CashCard_result.add(dir);
            CashCard_result.add(this.m.group());
            CashCard_result.add(Integer.toString(pageNum));
            CashCard_result.add(Integer.toString(line));
            CashCard_result.add(CashCardType(this.m.group()));
        }
    }
    
    public void AnalysisPDF(int index) throws InterruptedException{
        /**
         * Strip all pdf in files
         */
		if(BufferedFolder.get(index).endsWith(".pdf") && !BufferedFolder.isEmpty()){
	        try(PDDocument pd = PDDocument.load(new File(BufferedFolder.get(index)))){
	            PDFTextStripper pdd = new PDFTextStripper();
	            for(int pageNum=1; pageNum<=pd.getNumberOfPages(); pageNum++){
	                //sleep(1);
	                pdd.setStartPage(pageNum);
	                pdd.setEndPage(pageNum);
	                String textToStr = pdd.getText(pd);
	                /******  Read string per line  ******/
	                try(BufferedReader br = new BufferedReader(new CharArrayReader(textToStr.toCharArray()))){
	                    int count = 0;
	                    do{
	                        count++;
	                        this.data=br.readLine();
	                        if(this.data==null){
	                            break;
	                        }
	                        /******  Scan if-else-data position  ******/
	                        if(this.ifEmail){
	                            MatchEmailData(BufferedFolder.get(index),pageNum,count);
	                        }
	                        if(this.ifCellPhone){
	                            MatchCellphoneData(BufferedFolder.get(index),pageNum,count);
	                        }
	                        if(this.ifIdentity){
	                            MatchIdentityData(BufferedFolder.get(index),pageNum,count);
	                        }
	                        if(this.ifCashCard){
	                            MatchCashCardData(BufferedFolder.get(index),pageNum,count);
	                        }
	                    }while(true);
	                    br.close();
	                }
	                catch(Exception e){
	                    System.out.println(e.toString());
	                }
	            }
	            pd.close();
	        }
	        catch(IOException e){
	            System.out.println(e.toString());
	        }
	    };
	        
	    setEmailCount();
	    setCashCardCount();
	    setIdentityCount();
	    setCellphoneCount();
    }
    
    public void setFiles(String indir_infile) {
        this.BufferedFolder = new ArrayList<>(StripFolderFiles(indir_infile));
        this.BufferedFolder.trimToSize();        	
    }

    protected int getProgressBarCount() {
    	return BufferedFolder.size();
    }
    
    protected float getProgressBarDist() {
    	int Count = BufferedFolder.size();
    	float Dist = 0;
		Dist = (float) ( (1.0 / Count )* 100.0 );
    	return Dist;
    }
    
    protected float getProgressBarDist(long TotalCount) {
    	long Count = TotalCount;
    	float Dist = 0;
		Dist = (float) ( (1.0 / Count )* 100.0 );
    	return Dist;
    }
}
