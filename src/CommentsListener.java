import com.google.gson.Gson;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by satopi on 2017/09/26.
 */
public class CommentsListener extends CPP14BaseListener {
    private BufferedTokenStream tokens;
    private CPP14Parser parser;
    private ArrayList<String> results = new ArrayList<String>();
    private InfoForNeedComments infoObj;

    public CommentsListener(BufferedTokenStream tokens, CPP14Parser parser) {
        this.tokens = tokens;
        this.parser = parser;
        readConfigFile("Config/ConfigForNeedComments.json");
        //TODO ここに設定ファイルをロードする処理を追加する．
    }

    //enumの前にコメントが有るかどうか
    @Override
    public void enterEnumhead(CPP14Parser.EnumheadContext ctx) {
        //MemberdeclartionのTokenを取得
        Token startToken = ctx.getStart();
        //        System.out.println(ctx.getText());
        int i = startToken.getTokenIndex();

        getHiddenTokens(startToken, i);
    }

    //クラス名の前
    @Override
    public void enterClasshead(CPP14Parser.ClassheadContext ctx){

        Token startToken = ctx.getStart();
        int i = startToken.getTokenIndex();
        //隠れているトークンを取得
        getHiddenTokens(startToken, i);
    }
    //関数定義の前
    @Override
    public void enterFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx){
        //FunctionDefinitionの最も左側のTokenを取得
        Token startToken = ctx.getStart();
        int i = startToken.getTokenIndex();
        //隠れているトークンを取得
        getHiddenTokens(startToken, i);
    }
    //クラス名，関数宣言，変数宣言の前．
    @Override
    public void enterMemberdeclaration(CPP14Parser.MemberdeclarationContext ctx) {
        //TODO memberdeclartionで，simpletypespecifierになってるやつが変数名になる？
        Token startToken = ctx.getStart();
        int i = startToken.getTokenIndex();

        //隠れているトークンを取得
        getHiddenTokens(startToken, i);
    }
    //変数宣言の前
    @Override
    public void enterSimpledeclaration(CPP14Parser.SimpledeclarationContext ctx) {
        //TODO memberdeclartionで，simpletypespecifierになってるやつが変数名になる？
        Token startToken = ctx.getStart();
        int i = startToken.getTokenIndex();

        //隠れているトークンを取得
        getHiddenTokens(startToken, i);
    }

    //変数宣言の前
//    @Override
//    public void enterSimpletypespecifier(CPP14Parser.SimpletypespecifierContext ctx){
//        //FunctionDefinitionの最も左側のTokenを取得
//
//        // VocabularyImpl vocabulary = new VocabularyImpl();
//        Token startToken = ctx.getStart();
//        int i = startToken.getTokenIndex();
//
//        //隠れているトークンを取得
//        getHiddenTokens(startToken, i);
//    }
    //Loopの前
    @Override
    public void enterIterationstatement(CPP14Parser.IterationstatementContext ctx){
        //Iterationstatementの最も左側のTokenを取得
        if( infoObj.isIterationsStatement() ) {
            Token startToken = ctx.getStart();
            int i = startToken.getTokenIndex();
            //隠れているトークンを取得
            getHiddenTokens(startToken, i);
        }
    }
    //if,switchの前
    @Override
    public void enterSelectionstatement(CPP14Parser.SelectionstatementContext ctx){
        if( infoObj.isSelectionsStatement() ) {
            Token startToken = ctx.getStart();
            int i = startToken.getTokenIndex();
            //隠れているトークンを取得
            getHiddenTokens(startToken, i);

            if(ctx.Else() != null) {
                Token elseToken = ctx.Else().getSymbol();
                getHiddenTokens(elseToken, elseToken.getTokenIndex());
            }
        }
    }

    @Override
    public  void exitTranslationunit(CPP14Parser.TranslationunitContext ctx){
        for(String result:results){
            System.out.println(result);
        }

    }

    public void readConfigFile(String path){

        //BufferedReaderを作成．
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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

        String json = builder.toString();
        Gson gson = new Gson();
        this.infoObj = gson.fromJson(json,InfoForNeedComments.class);
    }

    //隠れているトークンを取得
    //判断し出力する．
    private void getHiddenTokens(Token startToken, int i) {
        List<Token> blockCommentChannel = this.tokens.getHiddenTokensToLeft(i, CPP14Lexer.BLOCKCOMMENT);
        List<Token> lineCommentChannel= this.tokens.getHiddenTokensToLeft(i, CPP14Lexer.LINECOMMENT);
        //判断し出力する．
        outPutWhereNeedToComments(startToken, blockCommentChannel,lineCommentChannel);
    }

    private void outPutWhereNeedToComments(Token startToken, List<Token> blockCommentChannel,List<Token> lineCommentChannel) {
        //隠れているトークンがあった場合は，コメントがあると出力する
        if (blockCommentChannel != null ) {

        }else if(lineCommentChannel != null ) {
            //隠れているトークンがなかった場合は，コメントが必要であると出力する
        }else {
            String msg = startToken.getLine() + "行目の " + startToken.getText()+"の前にコメントが必要です";

            //重複を省くための処理
            if(results.isEmpty()) {
                this.results.add(msg);
            } else if(msg.equals (results.get( results.size() -1 ))){
                //msgと，resultsの最後の要素が一致してるなら，何もしない．
            }else{
                this.results.add(msg);
            }
        }
    }

    public List<String> getResults(){
        return this.results;
    }


}
