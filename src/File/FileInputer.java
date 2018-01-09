package File;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Created by satopi on 2017/12/28.
 */
public class FileInputer {
    private String filePath;
    private String inputStrings;
    private InfoForFunctionDetectSpecificComments infoForFunctionDetectSpecificCommentsObj;
    private InfoForNecessaryComments infoForNecessaryCommentsObj;

    public String getInputStrings() { return inputStrings; }
    public InfoForNecessaryComments getInfoForNecessaryCommentsObj() { return infoForNecessaryCommentsObj; }
    public InfoForFunctionDetectSpecificComments getInfoForFunctionDetectSpecificCommentsObj() { return infoForFunctionDetectSpecificCommentsObj; }

    public FileInputer(){
    }
    public FileInputer(String path){
        try {
            this.inputStrings = Files.lines(Paths.get(path), Charset.forName("UTF-8"))
                    .collect(Collectors.joining(System.getProperty("line.separator")));
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    public void readSettingFileForNecessaryComments(String path){
        //BufferedReaderを作成．
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //ファイルから読み込む
        StringBuilder builder = getStringBuilder(bufferedReader);

        String json = builder.toString();
        Gson gson = new Gson();
        this.infoForNecessaryCommentsObj = gson.fromJson(json, InfoForNecessaryComments.class);
    }

    public void readSettingFileForFunctionDetectSpecificComments(String path){
        //BufferedReaderを作成．
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //ファイルから読み込む
        StringBuilder builder = getStringBuilder(bufferedReader);

        String json = builder.toString();
        Gson gson = new Gson();
        this.infoForFunctionDetectSpecificCommentsObj = gson.fromJson(json, InfoForFunctionDetectSpecificComments.class);
    }
    //ファイルから読み込む
    private StringBuilder getStringBuilder(BufferedReader bufferedReader) {
        StringBuilder builder = new StringBuilder();
        String string = null;
        try {
            string = bufferedReader.readLine();
            while (string != null){
                builder.append(string + System.getProperty("line.separator"));
                string = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder;
    }
}
