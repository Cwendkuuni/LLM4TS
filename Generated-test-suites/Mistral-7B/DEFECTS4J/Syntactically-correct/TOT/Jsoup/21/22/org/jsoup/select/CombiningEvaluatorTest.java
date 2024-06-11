import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;
import org.jsoup.Jsoup;

@RunWith(JUnit4.class)
public class CombiningEvaluatorTest {

    @Test
    public void testMatches_And_BothEvaluatorsPass() {
        Element root = Jsoup.parse("<html><body><div id='test'></div></body></html>");
        Evaluator evaluator1 = new ElementEvaluator("div");
        Evaluator evaluator2 = new ClassNameEvaluator("test");
        And and = new And(Arrays.asList(evaluator1, evaluator2));
        assertTrue(and.matches(root, root.getElementById("test")));
    }
}