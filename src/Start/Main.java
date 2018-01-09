package Start;

import ParserOfNeedCommand.Generated.CPP14Lexer;
import ParserOfNeedCommand.Generated.CPP14Parser;
import ParserOfNeedCommand.Listener.CommentsListener;
import ParserOfSpecificCommand.Get.GettingAllCommentsWithLineNumber;
import ParserOfSpecificCommand.Dicitonary.CommentsDictionary;

import com.beust.jcommander.JCommander;

import com.google.gson.Gson;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import File.*;
import Command.*;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] argv) throws Exception {
        //jcommander : http://jcommander.org/#_overview
        CommandMain commandMain = new CommandMain();
        CommandNecessary commandNecessary = new CommandNecessary();
        CommandSpecific commandSpecific = new CommandSpecific();
        JCommander jCommander = JCommander.newBuilder() .addObject(commandMain) .addCommand("necessary",commandNecessary) .addCommand("specific",commandSpecific) .build();
        jCommander.parse(argv);

        if(jCommander.getParsedCommand() != null){
            //コマンドラインオプションによって機能の切り替え
            if(jCommander.getParsedCommand().equals("necessary")) {
                for (String file : commandNecessary.getFiles()) {
                    CommentsListener extractor = startDefectWhetherCommentsAreNecessary(file);
                    //結果をファイルに出力
                    if (commandMain.isOutput() || commandNecessary.isOutput()) {
                        File f = new File(file);
                        FileOutputer fo = new FileOutputer(f);
                        fo.outPutToFile(extractor.getResults());
                    }
                }
            }else if(jCommander.getParsedCommand().equals("specific")) {
                for (String file : commandSpecific.getFiles()) {
                    startDefectSpecificComments(file);
                }
            }
        }else{
            //TODO exceptionが出たときにもhelpを出すようにするべき？
            jCommander.usage();
        }
    }
    public static CommentsListener startDefectWhetherCommentsAreNecessary(String filePath){
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
        //これnullを返していいの？
        return null;
    }
    public static void startDefectSpecificComments(String filePath){

        FileInputer fileInPuter = new FileInputer(filePath);
        String inputStrings = fileInPuter.getInputStrings();
        GettingAllCommentsWithLineNumber result = new GettingAllCommentsWithLineNumber(inputStrings);

        ArrayList<String> outPutList = new ArrayList<String>();

//JSONファイルへ対応させる
//        InfoForFunctionDetectSpecificComments infoObj;
//        FileInputer fileInputer = new FileInputer();
        //fileInputer.readSettingFileForFunctionDetectSpecificComments("Config/RegExpDetectingSpecificComments.json");
//        infoObj = fileInputer.getInfoForFunctionDetectSpecificCommentsObj();
//        System.out.println(infoObj.getComment());

        //全てのコメントを表示するまで繰り返す．
        for(int i = 0; i < result.getCommentSize();  i++) {
            ArrayList<String> comment = result.getMap().get(result.getKeyValue().get(i));
            for(int j = 0; j < comment.size(); j++){
                CommentsDictionary dictionary = new CommentsDictionary();
                //適切なコメントかどうか判断する．
                if (dictionary.isInappropriateComment(comment.get(j))) {
                    outPutList.add(String.valueOf(result.getKeyValue().get(i)) + ":" +comment.get(j).replaceAll("\n", "") + " は不適切なコメントの可能性があります．");
                }

            }
        }
        for(String r:outPutList){
            System.out.printf(r);
        }

    }
}
