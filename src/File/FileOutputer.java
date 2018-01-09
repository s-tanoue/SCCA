package File;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by satopi on 2017/12/03.
 */
public class FileOutputer {
    private String fileName;
    private File filePath;
    //書き込むファイルのファイルパス
    private File newFilePath;

    public FileOutputer(File filePath){
        this.filePath = filePath;
        this.fileName = filePath.getName();

        this.newFilePath = new File("Results/" +fileName);
    }
    public void outPutToFile(List<String> results)  {
        try{
            if (checkBeforeWriteFile(newFilePath)){
                processToWriteToFile(results, newFilePath);
            }else if(newFilePath.createNewFile()){
                processToWriteToFile(results, newFilePath);
            }else{
                System.out.println("ファイルに書き込めません");
            }
        }catch(IOException e){
            System.out.println(e);
        }

    }

    //ファイルに書き込む処理
    private void processToWriteToFile(List<String> results, File file) throws IOException {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

        //日付を追加する．
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        pw.println(sdf.format(c.getTime()));

        for(String result:results){
            pw.println(result);
        }
        pw.close();
    }

    private boolean checkBeforeWriteFile(File file){
        if (file.exists()) {
            if (file.isFile() && file.canWrite()) {
                return true;
            }
        }
        return false;
    }

}
