import com.google.gson.Gson;
import org.antlr.v4.gui.Trees;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String filePath;
        if(args.length == 1){
            filePath = args[0];
            start(filePath);
        }else{
            System.out.println("引数の数が違います");
        }
    }
    private static void start(String filePath){
        // create a CharStream that reads from standard input
        try {
            CharStream input = CharStreams.fromFileName(filePath);
            // create a lexer that feeds off of input CharStream
            CPP14Lexer lexer = new CPP14Lexer(input);
            // create a buffer of tokens pulled from the lexer
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            // create a parser that feeds off the tokens buffer
            CPP14Parser parser = new CPP14Parser(tokens);
            ParseTree tree = parser.translationunit();    // begin parsing at rule
            //walker
            ParseTreeWalker walker = new ParseTreeWalker();
            //h.ファイルと，cppファイルでリスナーを分ける．
            //リスナーをわけないと，木構造的に上手く動かない事がある．

            CommentsListener extractor = new CommentsListener(tokens,parser);
            walker.walk(extractor,tree);
            //結果をファイルに出力
            File f = new File(filePath);
            FileOutPuter fo = new FileOutPuter(f);
            fo.outPutToFile(extractor.getResults());

            //構文木を表示
//            Trees.inspect(tree,parser);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
