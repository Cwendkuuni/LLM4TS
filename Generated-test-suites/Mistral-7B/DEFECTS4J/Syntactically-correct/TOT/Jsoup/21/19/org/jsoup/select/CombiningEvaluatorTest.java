import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;

public class AndTest {
    @Test
    public void testAnd() {
        Element root = Element.parse("<html><body><div class='class1'><p>Test</p></div>" +
                "<div class='class2'><p>Test</p></div>" +
                "<div class='class1 class2'><p>Test</p></div></body></html>");

        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(
                new ElementSelector("div.class1"),
                new ElementSelector("div.class2")
        );

        Elements result = andEvaluator.select(root);
        assertEquals(1, result.size());
        Element element = result.get(0);
        assertTrue(element.tagName().equals("div"));
        assertTrue(element.classNames().containsAll(Arrays.asList("class1", "class2")));
    }
}