import Parser.*;
import com.beust.jcommander.JCommander;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import File.*;
import Command.*;

import java.io.*;

public class Main {
    public static void main(String[] argv) throws Exception {
        //jcommander : http://jcommander.org/#_overview
        CommandMain commandMain = new CommandMain();
        CommandNeed need = new CommandNeed();
        CommandSpecific specific = new CommandSpecific();
        JCommander jCommander = JCommander.newBuilder() .addObject(commandMain) .addCommand("need",need) .addCommand("specific",specific) .build();
        jCommander.parse(argv);

        if(jCommander.getParsedCommand() != null){
            //コマンドラインオプションによって機能の切り替え
            if(jCommander.getParsedCommand().equals("need")) {
                for (String file : need.getFiles()) {
                    CommentsListener extractor = start(file);
                    //結果をファイルに出力
                    if (commandMain.isOutput() || need.isOutput()) {
                        File f = new File(file);
                        FileOutPuter fo = new FileOutPuter(f);
                        fo.outPutToFile(extractor.getResults());
                    }
                }
            }else if(jCommander.getParsedCommand().equals("specific")) {
                //TODO 特定のコメントを解析する機能をスタートさせる．
            }
        }else{
            //TODO exceptionが出たときにもhelpを出すようにするべき？
            jCommander.usage();
        }
    }
    private static CommentsListener start(String filePath){
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
            //構文木を表示
            //Trees.inspect(tree,parser);
            return extractor;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //これnullを返していいのか？
        return null;
    }
}
