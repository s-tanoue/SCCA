package ParserOfSpecificCommand.Dicitonary;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class  CommentsDictionary {

    private HashMap<String,ArrayList<String>>  wordMap = new HashMap<String,ArrayList<String>>();
    private HashMap<String,ArrayList<String>>  regularExpressionMap = new HashMap<String,ArrayList<String>>();

    //データ構造の作成
    public CommentsDictionary() {
        //xml の読みこみ
        Document document = null;
        try {
            document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse("Config/RegExpDetectingSpecificComments.xml");
        } catch (SAXException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        //commentsを取得
        Node rootNode = (Node) (document.getDocumentElement());
        //commentの最初のnodeを取得
        Node commentNode = rootNode.getFirstChild();
        while (commentNode != null) {
            ArrayList<String> wordList = new ArrayList<String>();
            ArrayList<String> regularExpressionList = new ArrayList<String>();
            if (!commentNode.getNodeName().equals("comment")) {
                commentNode = commentNode.getNextSibling();
                continue;
            }
            Node wordNode = commentNode.getFirstChild();
            while (wordNode != null) {
                if (wordNode.getNodeName().equals("word")) {
                    Node node = wordNode.getFirstChild();
                    if (node != null && node.getNodeValue() != null) {
                        wordList.add(node.getNodeValue());
                    }
                }else if(wordNode.getNodeName().equals("regExp") ){
                    Node node = wordNode.getFirstChild();
                    if (node != null && node.getNodeValue() != null) {
                        regularExpressionList.add(node.getNodeValue());
                    }
                }
                //nullじゃないとき，ハッシュマップに，コメントの属性値とワードのリストをセットする．
                if(wordList != null && commentNode.getAttributes().getNamedItem("type") != null){
                    wordMap.put(commentNode.getAttributes().getNamedItem("type").getNodeValue(),wordList);
                }
                if(regularExpressionList != null && commentNode.getAttributes().getNamedItem("type") != null){
                    regularExpressionMap.put(commentNode.getAttributes().getNamedItem("type").getNodeValue(),regularExpressionList);
                }
                wordNode = wordNode.getNextSibling();
            }
            commentNode = commentNode.getNextSibling();
        }
    }
    //不適切なコメントであったらtrue
    public boolean isInappropriateComment(String target) {
        if( isWord(target) || isRegularExpression(target)){
            return true;
        }
        return false;
    }
    //単語と一致したらtrue
    public boolean isWord(String target){
        for (String key : wordMap.keySet()) {
            //手に入れたListをそれぞれ，wに展開
            for(String w: wordMap.get(key)){
                if(target.matches(".*"+ Pattern.quote(w) +".*")){
                    return true;
                }
            }
        }
        return false;
    }
    //正規表現に一致したらtrue
    public boolean isRegularExpression(String target){
        for (String key : regularExpressionMap.keySet()) {
            for(String r: regularExpressionMap.get(key)){
                Pattern p = Pattern.compile(r);
                Matcher m = p.matcher(target);
                if(m.find()){
                    return true;
                }
            }
        }
        return false;
    }

}