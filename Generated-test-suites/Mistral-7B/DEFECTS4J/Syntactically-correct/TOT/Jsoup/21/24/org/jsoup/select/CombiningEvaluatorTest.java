import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.helper.ElementFinder;
import org.jsoup.nodes.Document;

import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {
    @Test
    public void testAndMatcher() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new ElementMatcher(Tag.CLASS_NAME.equals("class1")));
        evaluators.add(new ElementMatcher(Tag.CLASS_NAME.equals("class2")));

        Document doc = new JsoupScanner().parse("<html><body><div class='class1 class2'></div></body></html>");
        Element root = doc.body();
        Element element = ElementFinder.findFirst(root, evaluators);

        And andEvaluator = new And(evaluators);
        assertTrue(andEvaluator.matches(root, element));
    }
}