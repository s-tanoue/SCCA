package ParserOfNeedCommand.Listener;

import Start.Main;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by satopi on 2018/01/09.
 */
class CommentsListenerTest {

    @org.junit.jupiter.api.Test
    void getResults() {
        CommentsListener cl = Main.startDefectWhetherCommentsAreNecessary("examples/LeftCourse.h");

        List<String> results = cl.getResults();
        List<String> expected = new ArrayList<>();
        expected.add("20行目の enumの前にコメントが必要です");
        expected.add("27行目の structの前にコメントが必要です");
        expected.add("31行目の doubleの前にコメントが必要です");
        expected.add("40行目の voidの前にコメントが必要です");
        expected.add("41行目の intの前にコメントが必要です");
        expected.add("48行目の voidの前にコメントが必要です");
        expected.add("49行目の voidの前にコメントが必要です");
        expected.add("54行目の Navigationの前にコメントが必要です");
        expected.add("57行目の SelfLocalizationの前にコメントが必要です");
        expected.add("61行目の intの前にコメントが必要です");

         assertEquals(expected,results);
    }
}
