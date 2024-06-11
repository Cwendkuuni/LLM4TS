import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.And;
import org.jsoup.select.Elements;
import org.jsoup.select.Or;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CombiningEvaluator.
 */
public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator() {
        Evaluator evaluatorA = new And(Arrays.asList(
                new ElementsSelector("p"),
                new ElementsSelector("a")
        ));

        Document doc = Jsoup.parse("<html><body><p>Text1 <a href=\"link1\">Link1</a></p>" +
                "<p>Text2 <a href=\"link2\">Link2</a></p></body></html>");

        Elements elements = doc.select(evaluatorA.toString());

        assertTrue(elements.size() == 2);
    }

    @Test
    public void testAndEvaluator_noMatch() {
        Evaluator evaluatorA = new And(Arrays.asList(
                new ElementsSelector("p"),
                new ElementsSelector("div")
        ));

        Document doc = Jsoup.parse("<html><body><p>Text1 <a href=\"link1\">Link1</a></p>" +
                "<p>Text2 <div>Div1</div></p></body></html>");

        Elements elements = doc.select(evaluatorA.toString());

        assertFalse(elements.isEmpty());
    }

    @Test
    public void testOrEvaluator() {
        Evaluator evaluatorA = new ElementsSelector("p");
        Evaluator evaluatorB = new ElementsSelector("a");

        Or orEvaluator = new Or();
        orEvaluator.add(evaluatorA);
        orEvaluator.add(evaluatorB);

        Document doc = Jsoup.parse("<html><body><p>Text1 <a href=\"link1\">Link1</a></p>" +
                "<p>Text2</p></body></html>");

        Elements elements = doc.select(orEvaluator.toString());

        assertTrue(elements.size() == 2);
    }

    @Test
    public void testOrEvaluator_noMatch() {
        Evaluator evaluatorA = new ElementsSelector("p");
        Evaluator evaluatorB = new ElementsSelector("div");

        Or orEvaluator = new Or();
        orEvaluator.add(evaluatorA);
        orEvaluator.add(evaluatorB);

        Document doc = Jsoup.parse("<html><body><p>Text1 <a href=\"link1\">Link1</a></p>" +
                "<p>Text2</p></body></html>");

        Elements elements = doc.select(orEvaluator.toString());

        assertFalse(elements.isEmpty());
    }

    @Test
    public void testAndOrEvaluator() {
        Evaluator evaluatorA = new And(
                new ElementsSelector("p"),
                new ElementsSelector("a")
        );

        Or evaluatorB = new Or();
        evaluatorB.add(new ElementsSelector("p"));
        evaluatorB.add(new ElementsSelector("div"));

        And andEvaluator = new And();
        andEvaluator.add(evaluatorA);
        andEvaluator.add(evaluatorB);

        Document doc = Jsoup.parse("<html><body>" +
                "<p>Text1 <a href=\"link1\">Link1</a></p>" +
                "<p>Text2 <div>Div1</div></p>" +
                "</body></html>");

        Elements elements = doc.select(andEvaluator.toString());

        assertTrue(elements.isEmpty());
    }
}