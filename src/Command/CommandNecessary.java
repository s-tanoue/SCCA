package Command;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satopi on 2017/12/18.
 */
public class CommandNecessary {
    @Parameter(description = "The source code file to Analyze.")
    private ArrayList<String> files = new ArrayList<>();
    public ArrayList<String> getFiles() {
        return this.files;
    }
    //TODO CommandMainだけの記述でできるようにしたい．
    //specific ファイル名 -oでも動くようにしたい．mainに必要か？
    @Parameter(names={"--output", "-o"})
    private boolean output=false;

    public boolean isOutput() {
        return this.output;
    }
}