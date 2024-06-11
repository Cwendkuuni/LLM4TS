import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.jsoup.parser.TokenQueue;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div.class";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Selector expectedSelector = new Selector("div").attr("class", "class");
        Assert.assertEquals(expectedSelector, evaluator);
    }

    @Test
    public void testParseSelectorWithCombinatorGreaterThan() {
        String query = "div > p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Selector expectedSelector = new Selector("div").descendant("p");
        Assert.assertEquals(expectedSelector, evaluator);
    }

    @Test
    public void testParseSelectorWithCombinatorSpace() {
        String query = "div #id1 .class1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Selector expectedSelector = new Selector("div").id("id1").descendants("p").attr("class", "class1");
        Assert.assertEquals(expectedSelector, evaluator);
    }

    @Test
    public void testParseSelectorWithCombinatorPlus() {
        String query = "div + p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Selector expectedSelector = new Selector("div").nextSibling("p");
        Assert.assertEquals(expectedSelector, evaluator);
    }

    @Test
    public void testParseSelectorWithCombinatorTilde() {
        String query = "div ~ p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Selector expectedSelector = new Selector("div").previousSibling("p");
        Assert.assertEquals(expectedSelector, evaluator);
    }

    @Test
    public void testParseSelectorWithId() {
        String query = "#id1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Selector expectedSelector = new Selector("#id1");
        Assert.assertEquals(expectedSelector, evaluator);
    }

    @Test
    public void testParseSelectorWithClass() {
        String query = ".class1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Selector expectedSelector = new Selector("p").attr("class", "class1");
        Assert.assertEquals(expectedSelector, evaluator);
    }

    @Test
    public void testParseSelectorWithTag() {
        String query = "p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Selector expectedSelector = new Selector("p");
        Assert.assertEquals(expectedSelector, evaluator);
    }

    @Test
    public void testParseSelectorWithAttribute() {
        String query = "[title]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Selector expectedSelector = new Selector("p[title]");
        Assert.assertEquals(expectedSelector, evaluator);
    }

    @Test
    public void testParseSelectorWithAttributeValue() {
        String query = "[title='test']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Selector expectedSelector = new Selector("p[title='test']");
        Assert.assertEquals(expectedSelector, evaluator);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseSelectorWithUnknownCombinator() {
        String query = "div ? p";
        QueryParser parser = new QueryParser(query);
        parser.parse();
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseSelectorWithUnbalancedBrackets() {
        String query = "div [title] (p";
        QueryParser parser = new QueryParser(query);
        parser.parse();
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseSelectorWithUnknownAttributeSelector() {
        String query = "div [abc]";
        QueryParser parser = new QueryParser(query);
        parser.parse();
    }
}