package ParserOfNeedCommand.Listener;

import File.FileInputer;
import File.InfoForNecessaryComments;
import ParserOfNeedCommand.Generated.CPP14BaseListener;
import ParserOfNeedCommand.Generated.CPP14Parser;
import org.antlr.v4.runtime.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Satoshi Tanoue on 2017/09/26.
 */
public class CommentsListener extends CPP14BaseListener {
    private CommonTokenStream tokens;
    private CPP14Parser parser;

    private ArrayList<String> results = new ArrayList<String>();
    public List<String> getResults(){ return this.results; }

    private InfoForNecessaryComments infoObj;
    String afterComments="";

    public CommentsListener(CommonTokenStream tokens, CPP14Parser parser) {
        this.tokens = tokens;
        this.parser = parser;
        //TODO 設定ファイルをオプションで選択できるようにした方がいい？
        FileInputer fileInPuter = new FileInputer();
        fileInPuter.readSettingFileForNecessaryComments("Config/SettingForNecessaryComments.json");
        infoObj = fileInPuter.getInfoForNecessaryCommentsObj();
    }

    //enumの前にコメントが有るかどうか
    @Override
    public void enterEnumhead(CPP14Parser.EnumheadContext ctx) {
        determineWhetherCommentIsNecessary(ctx);
    }
    //クラス名の前
    @Override
    public void enterClasshead(CPP14Parser.ClassheadContext ctx){ determineWhetherCommentIsNecessary(ctx); }

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

    //ここがもっとも重要なコード
    private void determineWhetherCommentIsNecessary(ParserRuleContext ctx) {
        Token startToken = ctx.getStart();
        Token stopToken = ctx.getStop();
        List<Token> beforeCommentChannel = getBeforeHiddenTokens(ctx, 1);
        List<Token> afterCommentChannel = getAfterHiddenTokens(ctx, 1);

        int afterIndex = 0;
        int beforeIndex = 0;
        if(beforeCommentChannel != null){
             beforeIndex = beforeCommentChannel.size() -1 ;
        }
        if(afterCommentChannel != null) {
             afterIndex = afterCommentChannel.size() - 1;
        }
        //TODO 条件分岐が複雑すぎる．真理値表を参照．

        if (beforeCommentChannel == null && afterCommentChannel == null) {
            outPutWhereNeedToComments(startToken);
        } else if (beforeCommentChannel == null && afterCommentChannel != null) {
            //後にあるコメントがステートメントに同じ行にない．
            if (afterCommentChannel.get(afterIndex).getLine() != stopToken.getLine()) {
                outPutWhereNeedToComments(startToken);
            } else {
                //後にあるコメントが同じ行にあるときは，そのコメントは前のステートメントに対するコメントである．
                afterComments = afterCommentChannel.get(afterIndex).getText();
            }
        } else if (beforeCommentChannel != null && afterCommentChannel == null) {
            //保持しているコメントと，以前のコメントが一緒じゃない．
            if (!(afterComments.equals( beforeCommentChannel.get(beforeIndex).getText() ))) {
            }else{
                outPutWhereNeedToComments(startToken);
            }
        } else {
            if (afterCommentChannel.get(afterIndex).getLine() != stopToken.getLine()) {
                if (!(afterComments.equals(beforeCommentChannel.get(beforeIndex).getText()))) {
                }else{
                    outPutWhereNeedToComments(startToken);
                }
            } else {
                afterComments = afterCommentChannel.get(afterIndex).getText();
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


