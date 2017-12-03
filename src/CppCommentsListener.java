import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;

import java.util.List;

/**
 * Created by satopi on 2017/09/26.
 */
public class CppCommentsListener extends CPP14BaseListener {
    private BufferedTokenStream tokens;
    private CPP14Parser parser;

    public CppCommentsListener(BufferedTokenStream tokens, CPP14Parser parser) {
        this.tokens = tokens;
        this.parser = parser;
        //TODO ここに設定ファイルをロードする処理を追加する．
    }
    //classの前
    @Override
    public void enterClasshead (CPP14Parser.ClassheadContext ctx){
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

    //変数の前
    @Override
    public void enterSimpledeclaration(CPP14Parser.SimpledeclarationContext ctx){
        //SimpleDeclarationの最も左側のTokenを取得
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
        //SelectionStatementの最も左側のTokenを取得
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
