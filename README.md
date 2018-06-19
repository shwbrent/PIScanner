# 個人資料掃描器PIScanner(Personal Information Scanner)<br>
![PIScanner](https://github.com/shwbrent/PIScanner/blob/master/Readme_IMG/UI.PNG "UI")<br>

****

## 個人資訊種類(Personal Information Category)
* 身份證字號(Taiwan Identification)<br>
* 台灣手機號碼(Taiwan mobile phone number)<br>
* 信用卡(Credit Card)<br>
* 電子郵件(Email)<br>

---

## 使用方式(Usage)
1. 開啟要掃描的資料夾或檔案(Open folders or files)<br>
![PIScanner](https://github.com/shwbrent/PIScanner/blob/master/Readme_IMG/Open.png "First Step")
![PIScanner](https://github.com/shwbrent/PIScanner/blob/master/Readme_IMG/OpenFinished.png "First Step")<br>
2. 選取要掃描的個資種類，點擊開始掃描(Select personal information category, Click start button.)<br>
![PIScanner](https://github.com/shwbrent/PIScanner/blob/master/Readme_IMG/StartButton.png "UI")<br>
3. 掃描完畢後，儲存檔案以匯出CSV報表(Finished, then save file to CSV)<br>
![PIScanner](https://github.com/shwbrent/PIScanner/blob/master/Readme_IMG/Save.png "UI")<br>
4. 開啟csv檔案檢視含有個資疑慮的檔案(Open CSV check having doubts files)<br>
![PIScanner](https://github.com/shwbrent/PIScanner/blob/master/Readme_IMG/Result.png "UI")<br>

---

###程式重點說明
1. 在選擇一個以上(含)的檔案/資料夾，會通過下列程式段**以遞迴方式將全部資料夾下的檔案回傳**
```java
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
```
2. 在介面(UI)中，巢狀類別繼承(extends)SwingWorker<T,V>覆寫doInBackground、done來實現子執行緒掃描
```java
	    class BackgroundCalculator extends SwingWorker<Void, Void>{
		
		/*此處省略*/
		......
		
    	@Override
    	protected Void doInBackground() throws Exception {
    		// TODO Auto-generated method stub
    		InitTotalFiles();
    		progressBar.setMaximum(100);
    		progressBar.setMinimum(0);
    		progressBar.setStringPainted(true);
            try{
			
					/*此處進度條和資料解析片段省略...*/
                    ......
					
                    	/*
                    	 * 在dpInBackgrepound.cancel 發生後
                    	 * Thread如果還在sleep，會造成thread sleep interruption
                    	 * 因此在thread.sleep 要多加一個try-catch 捕捉例外
                    	 * 否則用到上一層try-catch 將無法繼續Cancel後的處理
						 * sleep可讓程式休息，避免造成程式卡頓
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
    	/*doInBackground執行完後執行此處*/
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
```
---
| Author	| 蘇泓瑋、何承澤、吳柏賢、蔡忠霖|
| --------- | :-------------:	|
| Email		| shwbrent@gmail.com |