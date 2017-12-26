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
    String afterComments="";

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

    private void determineWhetherCommentIsNecessary(ParserRuleContext ctx) {
        Token startToken = ctx.getStart();
        Token stopToken = ctx.getStop();
        List<Token> beforeCommentChannel = getBeforeHiddenTokens(ctx, 1);
        List<Token> afterCommentChannel = getAfterHiddenTokens(ctx, 1);


        //TODO 条件分岐が複雑すぎる．真理値表を参照．

        if (beforeCommentChannel == null && afterCommentChannel == null) {
            outPutWhereNeedToComments(startToken);
        } else if (beforeCommentChannel == null && afterCommentChannel != null) {
            //後にあるコメントがステートメントに同じ行にない．
            if (afterCommentChannel.get(0).getLine() != stopToken.getLine()) {
                outPutWhereNeedToComments(startToken);
            } else {
                //後にあるコメントが同じ行にあるときは，そのコメントは前のステートメントに対するコメントである．
                afterComments = afterCommentChannel.get(0).getText();
            }
        } else if (beforeCommentChannel != null && afterCommentChannel == null) {
            if (!(afterComments.equals(beforeCommentChannel.get(0).getText()))) {
                outPutWhereNeedToComments(startToken);
            }
        } else {
            if (afterCommentChannel.get(0).getLine() != stopToken.getLine()) {
                if (!(afterComments.equals(beforeCommentChannel.get(0).getText()))) {
                    outPutWhereNeedToComments(startToken);
                }
            } else {
                afterComments = afterCommentChannel.get(0).getText();
            }

        }
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


