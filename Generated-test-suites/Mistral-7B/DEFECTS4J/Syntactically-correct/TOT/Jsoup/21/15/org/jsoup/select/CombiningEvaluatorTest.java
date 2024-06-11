import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator() {
        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(
                new ElementSelector("p"),
                new ElementSelector("strong")
        );

        Document doc = Jsoup.parse("<p><strong>Test</strong></p>");
        Elements matchedElements = andEvaluator.select(doc, "p strong");
        assertEquals(1, matchedElements.size());
        assertSame(doc.select("p strong").get(0), matchedElements.get(0));

        Document doc2 = Jsoup.parse("<p><strong>Test</strong></p>" +
                "<p><em>Test</em></p>");
        Elements matchedElements2 = andEvaluator.select(doc2, "p strong");
        assertEquals(1, matchedElements2.size());
        assertSame(doc.select("p strong").get(0), matchedElements2.get(0));

        Document doc3 = Jsoup.parse("<p><strong>Test</strong></p>" +
                "<p><em>Test</em></p>" +
                "<p><strong>Test</strong></p>");
        Elements matchedElements3 = andEvaluator.select(doc3, "p strong");
        assertEquals(2, matchedElements3.size());

        Document doc4 = Jsoup.parse("<p><strong>Test</strong></p>" +
                "<p><em>Test</em></p>" +
                "<p></p>");
        Elements matchedElements4 = andEvaluator.select(doc4, "p strong");
        assertEquals(1, matchedElements4.size());
        assertSame(doc.select("p strong").get(0), matchedElements4.get(0));
    }

    @Test
    public void testOrEvaluator() {
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or();
        orEvaluator.add(new ElementSelector("p"));
        orEvaluator.add(new ElementSelector("strong"));

        Document doc = Jsoup.parse("<p><strong>Test</strong></p>");
        Elements matchedElements = orEvaluator.select(doc, "p, strong");
        assertEquals(2, matchedElements.size());
        assertTrue(matchedElements.contains(doc.select("p").get(0)));
        assertTrue(matchedElements.contains(doc.select("strong").get(0)));

        Document doc2 = Jsoup.parse("<p><em>Test</em></p>");
        Elements matchedElements2 = orEvaluator.select(doc2, "p, strong");
        assertEquals(0, matchedElements2.size());

        Document doc3 = Jsoup.parse("<p><strong>Test</strong></p>" +
                "<p><em>Test</em></p>" +
                "<p><strong>Test</strong></p>");
        Elements matchedElements3 = orEvaluator.select(doc3, "p, strong");
        assertEquals(3, matchedElements3.size());

        Document doc4 = Jsoup.parse("<p></p>");
        Elements matchedElements4 = orEvaluator.select(doc4, "p, strong");
        assertEquals(0, matchedElements4.size());
    }
}