package Command; /**
 * Created by satopi on 2017/12/15.
 */
import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

//コマンドライン引数のパーサー
public class CommandMain{

        @Parameter(names={"--output", "-o"})
        private boolean output=false;

        public boolean isOutput() {
                return this.output;
        }

        @Parameter(names = {"--help","-h"},help = true)
        private boolean help = false;
}
