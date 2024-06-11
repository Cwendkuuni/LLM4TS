import org.jsoup.nodes.Element;
import org.jsoup.nodes.Elements;
import org.junit.Test;
import static org.junit.Assert.*;

public class AndTest {
    @Test
    public void testAndEvaluator() {
        Element root = Element.parse("<html><body><div id='div1' class='class1'></div><div id='div2' class='class2'></div></body></html>");

        Evaluator evaluator1 = new ElementSelector("div.class1");
        Evaluator evaluator2 = new ElementSelector("div.class2");
        And andEvaluator = new And(Arrays.asList(evaluator1, evaluator2));

        Elements matchedElements = andEvaluator.findAll(root);
        assertEquals(0, matchedElements.size());

        root.classBuf().add("class3");
        matchedElements = andEvaluator.findAll(root);
        assertEquals(0, matchedElements.size());

        root.classBuf().remove("class1");
        matchedElements = andEvaluator.findAll(root);
        assertEquals(1, matchedElements.size());
        assertEquals(div2.toString(), matchedElements.get(0).toString());
    }
}