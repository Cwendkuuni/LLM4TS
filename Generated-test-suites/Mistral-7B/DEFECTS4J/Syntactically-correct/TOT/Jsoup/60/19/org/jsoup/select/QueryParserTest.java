import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div.class1";
        Evaluator evaluator = QueryParser.parse(query);
        List<Selector> expectedSelectors = new ArrayList<>();
        expectedSelectors.add(new Selector("div"));
        expectedSelectors.add(new Selector("class: class1"));
        Assert.assertEquals(expectedSelectors, evaluator.getSelectors());
    }

    @Test
    public void testParseComplexSelector() {
        String query = "ul > li:first-child:has(a[href='#contact'])";
        Evaluator evaluator = QueryParser.parse(query);
        List<Selector> expectedSelectors = new ArrayList<>();
        expectedSelectors.add(new Selector("ul"));
        expectedSelectors.add(new StructuralEvaluator.Child(1));
        expectedSelectors.add(new StructuralEvaluator.Has(new Selector("a")));
        expectedSelectors.add(new AttributeEvaluator.Href("contact"));

        Assert.assertEquals(expectedSelectors, evaluator.getSelectors());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidSelector() {
        String query = "div #id:not(x)"; // The not pseudo-class must be followed by a valid selector
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidAttributeSelector() {
        String query = "div[x=1 class]"; // The attribute name should not contain spaces
        QueryParser.parse(query);
    }

    @Test
    public void testParseNegativeIndexSelector() {
        String query = "li:nth-child(-1)";
        Evaluator evaluator = QueryParser.parse(query);
        List<Selector> expectedSelectors = new ArrayList<>();
        expectedSelectors.add(new StructuralEvaluator.IsLastOfType("li"));
        Assert.assertEquals(expectedSelectors, evaluator.getSelectors());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidNthIndexSelector() {
        String query = "li:nth-child(abc)"; // The nth-index value should be a number or a number followed by an "n"
        QueryParser.parse(query);
    }
}