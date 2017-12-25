package Parser;

import File.InfoForNeedComments;
import com.google.gson.Gson;
import org.antlr.v4.runtime.*;
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
    private CommonTokenStream tokens;
    private CPP14Parser parser;

    private ArrayList<String> results = new ArrayList<String>();
    public List<String> getResults(){ return this.results; }

    private InfoForNeedComments infoObj;

    public CommentsListener(CommonTokenStream tokens, CPP14Parser parser) {
        this.tokens = tokens;
        this.parser = parser;
        //TODO 設定ファイルをオプションで選択できるようにした方がいい？
        readConfigFile("Config/ConfigForNeedComments.json");
    }

    //enumの前にコメントが有るかどうか
    @Override
    public void enterEnumhead(CPP14Parser.EnumheadContext ctx) {
        determineWhetherCommentIsNecessary(ctx);
    }
    //クラス名の前
    @Override
    public void enterClasshead(CPP14Parser.ClassheadContext ctx){
        determineWhetherCommentIsNecessary(ctx);
    }
    //関数定義の前
    @Override
    public void enterFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx){
        determineWhetherCommentIsNecessary(ctx);
    }
    //クラス名，関数宣言，変数宣言の前．
    @Override
    public void enterMemberdeclaration(CPP14Parser.MemberdeclarationContext ctx) {
        //TODO memberdeclartionで，simpletypespecifierになってるやつが変数名になる？
        determineWhetherCommentIsNecessary(ctx);
    }
    //変数宣言の前
    @Override
    public void enterSimpledeclaration(CPP14Parser.SimpledeclarationContext ctx) {
        //TODO memberdeclartionで，simpletypespecifierになってるやつが変数名になる？
        determineWhetherCommentIsNecessary(ctx);
    }

    //Loopの前
    @Override
    public void enterIterationstatement(CPP14Parser.IterationstatementContext ctx){
        //Iterationstatementの最も左側のTokenを取得
        if( infoObj.isIterationsStatement() ) {
            determineWhetherCommentIsNecessary(ctx);
        }
    }
    //if,switchの前
    @Override
    public void enterSelectionstatement(CPP14Parser.SelectionstatementContext ctx){
        if( infoObj.isSelectionsStatement() ) {
            determineWhetherCommentIsNecessary(ctx);
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
        //ファイルから読み込む
        StringBuilder builder = getStringBuilder(bufferedReader);

        String json = builder.toString();
        Gson gson = new Gson();
        this.infoObj = gson.fromJson(json,InfoForNeedComments.class);
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

    private void determineWhetherCommentIsNecessary(ParserRuleContext ctx){
        Token startToken = ctx.getStart();
        Token stopToken = ctx.getStop();
        List<Token> beforeBlockCommentChannel = getBeforeHiddenTokens(ctx,CPP14Lexer.BLOCKCOMMENT);
        List<Token> beforeLineCommentChannel = getBeforeHiddenTokens(ctx,CPP14Lexer.LINECOMMENT);
        
        List<Token> afterBlockCommentChannel = getAfterHiddenTokens(ctx,CPP14Lexer.BLOCKCOMMENT);
        List<Token> afterLineCommentChannel = getAfterHiddenTokens(ctx,CPP14Lexer.LINECOMMENT);


        String afterComments="";

        //TODO 条件分岐が複雑すぎる．真理値表を参照．
        if(beforeBlockCommentChannel == null && beforeLineCommentChannel == null) {
            afterComments = judgeLineComment(startToken, stopToken, afterBlockCommentChannel, afterLineCommentChannel);
        }else if(beforeBlockCommentChannel == null && beforeLineCommentChannel != null ) {
            if(afterComments.equals(beforeLineCommentChannel.get(0).getText())){
            }else{
                afterComments = judgeLineComment(startToken, stopToken, afterBlockCommentChannel, afterLineCommentChannel);
            }
        }else if(beforeBlockCommentChannel != null && beforeLineCommentChannel == null){
            if(afterComments.equals(beforeBlockCommentChannel.get(0).getText())){
            }else{
                afterComments = judgeLineComment(startToken, stopToken, afterBlockCommentChannel, afterLineCommentChannel);
            }
        }else {
            if(afterComments.equals(beforeBlockCommentChannel.get(0).getText()) || afterBlockCommentChannel.equals(beforeLineCommentChannel.get(0).getText())){
            }else{
                afterComments = judgeLineComment(startToken, stopToken, afterBlockCommentChannel, afterLineCommentChannel);
            }
        }

    }

    //TODO　名前が良くない．
    private String judgeLineComment(Token startToken, Token stopToken, List<Token> afterBlockCommentChannel, List<Token> afterLineCommentChannel) {

        if(afterBlockCommentChannel == null  && afterLineCommentChannel == null){
            //あるステートメントに対してのコメントではないので出力する．
            outPutWhereNeedToComments(startToken);
        }else if(afterBlockCommentChannel == null && afterLineCommentChannel != null ){
            //Blockコメントがnullじゃないとき，そのコメントは，同じ行にある可能性がある．
            //同じ行になければ，それはあるステートメントに対するコメントでない．
            int afterLineCommentLine = afterLineCommentChannel.get(0).getLine();

            if(stopToken.getLine() != afterLineCommentLine){
                outPutWhereNeedToComments(startToken);
            }
            return afterLineCommentChannel.get(0).getText();
        }else if(afterBlockCommentChannel != null && afterLineCommentChannel == null){
            //Blockコメントがnullじゃないとき，そのコメントは，同じ行にある可能性がある．
            //同じ行になければ，それはあるステートメントに対するコメントでない．
            int afterBlockCommentLine = afterBlockCommentChannel.get(0).getLine();
            if(stopToken.getLine() != afterBlockCommentLine){
                outPutWhereNeedToComments(startToken);
            }
            return afterBlockCommentChannel.get(0).getText();
            //両方のコメントがあるとき．
        }else{
            int afterLineCommentLine = afterLineCommentChannel.get(0).getLine();
            int afterBlockCommentLine = afterBlockCommentChannel.get(0).getLine();

            //どちらかの一方のコメントが，同じ行にないときは出力する
            if(stopToken.getLine() != afterBlockCommentLine || stopToken.getLine() != afterLineCommentLine){
                outPutWhereNeedToComments(startToken);
            }
            //TODO　これもreturn必要？
        }
        return "";
    }

    //隠れているトークンを取得
    private List<Token> getBeforeHiddenTokens(ParserRuleContext ctx, int type){
        Token token= ctx.getStart();
        int i = token.getTokenIndex();
        //TODO 前にある複数のコメントを取りたい．今は一つだけしか取れない．なぜ
        List<Token> CommentChannel = this.tokens.getHiddenTokensToLeft(i,type);
        return CommentChannel;
    }

    private List <Token> getAfterHiddenTokens(ParserRuleContext ctx, int type){
        Token token = ctx.getStop();
        int i = token.getTokenIndex();
        //TODO 前にある複数のコメントを取りたい．今は一つだけしか取れない．なぜ
        List<Token> CommentChannel = this.tokens.getHiddenTokensToRight(i,type);
        return CommentChannel;
    }
    //コメントの出力の処理
    private void outPutWhereNeedToComments(Token token) {
        String msg = token.getLine() + "行目の " + token.getText()+"の前にコメントが必要です";

        //重複があるときは追加しないようにするための処理
        if(results.isEmpty()) {
            this.results.add(msg);
        } else if(msg.equals (results.get( results.size() -1 ))){ //msgと，esultsの最後の要素が一致してるなら，何もしない．

        } else {
            this.results.add(msg);
        }
    }
}


