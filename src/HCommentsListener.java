import org.antlr.v4.runtime.*;

import java.lang.reflect.Member;
import java.util.List;

/**
 * Created by satopi on 2017/09/26.
 */
public class HCommentsListener extends CPP14BaseListener {
    BufferedTokenStream tokens;
    CPP14Parser parser;

    public HCommentsListener(BufferedTokenStream tokens, CPP14Parser parser) {
        //TODO 下の２行 これいる?
        this.tokens = tokens;
        this.parser = parser;
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

    //classの前
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

    @Override
    public void enterMemberdeclaration(CPP14Parser.MemberdeclarationContext ctx) {
        //TODO memberdeclartionで，simpletypespecifierになってるやつが変数名になる？
        //pctxはmemberdeclartion context
//        ParserRuleContext pctx = ctx.getParent().getParent().getParent().getParent().getParent().getParent();
        //simpletypespecifierだったら，
            Token startToken = ctx.getStart();
            if(startToken.getType() == CPP14Parser.Simp)
            int i = startToken.getTokenIndex();
            //隠れているトークンを取得
            getHiddenTokens(startToken, i);
        }
    }

    //変数宣言の前
    @Override
    public void enterSimpletypespecifier(CPP14Parser.SimpletypespecifierContext ctx){
        //FunctionDefinitionの最も左側のTokenを取得

       // VocabularyImpl vocabulary = new VocabularyImpl();
        Token startToken = ctx.getStart();
        int i = startToken.getTokenIndex();

        //隠れているトークンを取得
        getHiddenTokens(startToken, i);
    }
    //Loopの前
    @Override
    public void enterIterationstatement(CPP14Parser.IterationstatementContext ctx){
        //Iterationstatementの最も左側のTokenを取得
        Token startToken = ctx.getStart();
        int i = startToken.getTokenIndex();
        //隠れているトークンを取得
        getHiddenTokens(startToken, i);
    }
    //if,switchの前
    @Override
    public void enterSelectionstatement(CPP14Parser.SelectionstatementContext ctx){
        Token startToken = ctx.getStart();
        int i = startToken.getTokenIndex();
        //隠れているトークンを取得
        getHiddenTokens(startToken, i);
    }


    //隠れているトークンを取得
    //判断し出力する．
    private void getHiddenTokens(Token startToken, int i) {
        List<Token> blockCommentChannel = tokens.getHiddenTokensToLeft(i, CPP14Lexer.BLOCKCOMMENT);
        List<Token> lineCommentChannel= tokens.getHiddenTokensToLeft(i, CPP14Lexer.LINECOMMENT);
        //判断し出力する．
        outPutWhereNeedToComments(startToken, blockCommentChannel,lineCommentChannel);
    }

    private void outPutWhereNeedToComments(Token startToken, List<Token> blockCommentChannel,List<Token> lineCommentChannel) {
        //隠れているトークンがあった場合は，コメントがあると出力する
        if (blockCommentChannel != null ) {

        }else if(lineCommentChannel != null ) {
            //隠れているトークンがなかった場合は，コメントが必要であると出力する
        }else {
            System.out.println(startToken.getLine() + "行目の"+startToken.getText()+"の前にコメントが必要です");
        }
    }

}
