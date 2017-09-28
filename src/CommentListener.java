import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.Token;

import java.util.List;

/**
 * Created by satopi on 2017/09/26.
 */
public class CommentListener extends CPP14BaseListener {
    BufferedTokenStream tokens;
    CPP14Parser parser;

    public CommentListener(BufferedTokenStream tokens, CPP14Parser parser) {
        this.tokens = tokens;
        this.parser = parser;
    }

    @Override
    public void enterClassspecifier(CPP14Parser.ClassspecifierContext ctx) {
        //Tokenを取得
        Token startToken = ctx.getStart();
        int i = startToken.getTokenIndex();

        List<Token> blockCommentChannel = tokens.getHiddenTokensToLeft(i, CPP14Lexer.BLOCKCOMMENT);
        List<Token> lineCommentChannel= tokens.getHiddenTokensToLeft(i, CPP14Lexer.LINECOMMENT);
        printTokens(startToken, blockCommentChannel,lineCommentChannel);
    }


    @Override
    public void enterMemberdeclaration(CPP14Parser.MemberdeclarationContext ctx) {
        //左のTokenを取得
        Token startToken = ctx.getStart();
        int i = startToken.getTokenIndex();

        //隠れているトークンを取得
        List<Token> blockCommentChannel = tokens.getHiddenTokensToLeft(i, CPP14Lexer.BLOCKCOMMENT);
        List<Token> lineCommentChannel= tokens.getHiddenTokensToLeft(i, CPP14Lexer.LINECOMMENT);
        printTokens(startToken, blockCommentChannel,lineCommentChannel);
    }

    private void printTokens(Token startToken, List<Token> blockCommentChannel,List<Token> lineCommentChannel) {
        //隠れているトークンが合った場合は，コメントがあると出力する
        //隠れているトークンがなかった場合は，コメントが必要であると出力する
        if (blockCommentChannel != null ) {
            Token cmt = blockCommentChannel.get(0);
            String text = cmt.getText().replace("\n", "");
            System.out.println(cmt.getLine() + "行目に" + text + "のコメントがあります");

        }else if(lineCommentChannel != null ) {
            Token cmt = lineCommentChannel.get(0);
            String text = cmt.getText().replace("\n", "");
            System.out.println(cmt.getLine() + "行目に" + text + "のコメントがあります");
        } else {
            System.out.println(startToken.getLine() + "行目の"+startToken.getText()+"の前にコメントが必要です");
        }
    }

}
