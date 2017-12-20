package Command;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;

/**
 * Created by satopi on 2017/12/18.
 */
public class CommandSpecific {

    @Parameter(description = "The source code file to Analyze.")
    private ArrayList<String> files = new ArrayList<>();
    public ArrayList<String> getFiles() {
        return this.files;
    }

    @Parameter(names={"--output", "-o"})
    private boolean output=false;
    public boolean isOutput() { return this.output; }
}
