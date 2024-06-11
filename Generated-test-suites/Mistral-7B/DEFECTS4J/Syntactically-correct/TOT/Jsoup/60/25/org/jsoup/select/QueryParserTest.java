import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div.class1";
        Selector expectedSelector = new Selector("div.class1");
        Selector actualSelector = QueryParser.parse(query).getSelector();
        assertEquals(expectedSelector, actualSelector);
    }

    @Test
    public void testParseComplexSelector() {
        String query = "ul > li:nth-child(2) > a[href^='https']:not(:containsOwn(error)):contains(login)";
        Selector expectedSelector = new Selector("ul > li:nth-child(2) > a[href^='https']:not(:containsOwn(error)):contains(login)");
        Selector actualSelector = QueryParser.parse(query).getSelector();
        assertEquals(expectedSelector, actualSelector);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidSelector() {
        String query = "ul > li:nth-child(2) > a[href^='https']:not(:containsOwn(error)):contains(login) x:1";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidAttributeSelector() {
        String query = "ul > li:nth-child(2) > a[invalid-attribute=1]";
        QueryParser.parse(query);
    }

    @Test
    public void testParsePseudoClassSelector() {
        String query = ":empty";
        Selector expectedSelector = new Selector(".empty");
        Selector actualSelector = QueryParser.parse(query).getSelector();
        assertEquals(expectedSelector, actualSelector);
    }

    @Test
    public void testParseClassSelectorWithMultiClass() {
        String query = "ul.class1.class2";
        Selector expectedSelector = new Selector("ul.class1.class2");
        Selector actualSelector = QueryParser.parse(query).getSelector();
        assertEquals(expectedSelector, actualSelector);
    }

    @Test
    public void testParseIdSelector() {
        String query = "#myId";
        Selector expectedSelector = new Selector("#myId");
        Selector actualSelector = QueryParser.parse(query).getSelector();
        assertEquals(expectedSelector, actualSelector);
    }

    @Test
    public void testParseTagSelector() {
        String query = "input";
        Selector expectedSelector = new Selector("input");
        Selector actualSelector = QueryParser.parse(query).getSelector();
        assertEquals(expectedSelector, actualSelector);
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "[title='Test']";
        Selector expectedSelector = new Selector(":has(title:contains(Test))");
        Selector actualSelector = QueryParser.parse(query).getSelector();
        assertEquals(expectedSelector, actualSelector);
    }

    @Test
    public void testParseAttributeSelectorWithStartingValue() {
        String query = "[class^='prefix']";
        Selector expectedSelector = new Selector(":has(class, starts-with(class, prefix))");
        Selector actualSelector = QueryParser.parse(query).getSelector();
        assertEquals(expectedSelector, actualSelector);
    }

    @Test
    public void testParseAttributeSelectorWithEndingValue() {
        String query = "[class$='suffix']";
        Selector expectedSelector = new Selector(":has(class, ends-with(class, suffix))");
        Selector actualSelector = QueryParser.parse(query).getSelector();
        assertEquals(expectedSelector, actualSelector);
    }

    @Test
    public void testParseAttributeSelectorWithContainingValue() {
        String query = "[class*='prefix']";
        Selector expectedSelector = new Selector(":has(class, contains(class, prefix))");
        Selector actualSelector = QueryParser.parse(query).getSelector();
        assertEquals(expectedSelector, actualSelector);
    }

    @Test
    public void testParseAttributeSelectorWithMatchingValue() {
        String query = "[class~='prefix']";
        Selector expectedSelector = new Selector(":has(class, matches(class, ~, prefix))");
        Selector actualSelector = QueryParser.parse(query).getSelector();
        assertEquals(expectedSelector, actualSelector);
    }

    @Test
    public void testParsePseudoElementSelector() {
        String query = "a:first-child";
        Selector expectedSelector = new Selector("a:is(first-of-type)");
        Selector actualSelector = QueryParser.parse(query).getSelector();
        assertEquals(expectedSelector, actualSelector);
    }

    @Test
    public void testParsePseudoElementSelectorWithLast() {
        String query = "a:last-child";
        Selector expectedSelector = new Selector("a:is(last-of-type)");
        Selector actualSelector = QueryParser.parse(query).getSelector();
        assertEquals(expectedSelector, actualSelector);
    }

    @Test
    public void testParseCombinedSelector() {
        String query = "ul > li:nth-child(2) > a:not([href])";
        Selector expectedSelector = new Selector("ul > li:nth-child(2) > a:not(:empty) > a[href]:empty");
        Selector actualSelector = QueryParser.parse(query).getSelector();
        assertEquals(expectedSelector, actualSelector);
    }

    @Test
    public void testParseNegatedPseudoClass() {
        String query = "ul > li:not(:first-child)";
        Selector expectedSelector = new Selector("ul > li:not(:is(first-of-type))");
        Selector actualSelector = QueryParser.parse(query).getSelector();
        assertEquals(expectedSelector, actualSelector);
    }

    @Test
    public void testParseNegatedAttributeSelector() {
        String query = "ul > li > a:not([href='http://example.com'])";
        Selector expectedSelector = new Selector("ul > li > a:not([href:contains(http://example.com]])");
        Selector actualSelector = QueryParser.parse(query).getSelector();
        assertEquals(expectedSelector, actualSelector);
    }
}