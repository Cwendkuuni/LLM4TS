import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.helper.ElementMatcher;
import org.jsoup.nodes.Document;

import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {
    @Test
    public void testAnd() {
        ElementMatcher<Element> andEvaluator = new And(new ElementMatcher<Element>() {}, new ElementMatcher<Element>() {});
        Document doc = org.jsoup.Jsoup.parse("<html><body></body></html>");
        Element body = doc.body();
        assertTrue(andEvaluator.matches(body, body));
    }
}