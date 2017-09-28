import org.antlr.v4.gui.Trees;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        String filePath = "";
        if(args.length == 3 ){
            filePath = args[0];
        }else if(args.length <= 2){
            filePath = "/Users/satopi/Desktop/programing/java/Comments/src/examples/EtRobocon2017.h";
        }else{
           System.out.println("引数の数が違います");
        }
        start(filePath);
    }
    public static void start(String filePath){

        // create a CharStream that reads from standard input
        try {
            InputStream is = new FileInputStream(filePath);
            ANTLRInputStream input = new ANTLRInputStream(is);
            // create a lexer that feeds off of input CharStream
            CPP14Lexer lexer = new CPP14Lexer(input);
            // create a buffer of tokens pulled from the lexer
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            // create a parser that feeds off the tokens buffer
            CPP14Parser parser = new CPP14Parser(tokens);
            ParseTree tree = parser.translationunit();    // begin parsing at rule

            //walker
            ParseTreeWalker walker = new ParseTreeWalker();
            CommentListener extractor = new CommentListener(tokens,parser);
            walker.walk(extractor,tree);

            //構文木を表示
            Trees.inspect(tree,parser);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
