import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.jsoup.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        // An empty query should return an empty Evaluator
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
    }

    @Test
    public void testParseSimpleTagSelector() {
        String query = "p";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("p"));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
    }

    @Test
    public void testParseClassSelector() {
        String query = ".class1";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Class("class1"));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
    }

    @Test
    public void testParseIDSelector() {
        String query = "#id1";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Id("id1"));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "[title='Test']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.AttributeWithValue("title", "Test"));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "div.class1 p#id1";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new Evaluator.Class("class1"));
        expectedEvaluators.add(new Evaluator.Tag("p"));
        expectedEvaluators.add(new Evaluator.Id("id1"));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "div.class1#id1 p";
        // The query "div.class1#id1 p" is invalid because it contains both a class and an ID selector on the same element
        Evaluator evaluator = QueryParser.parse(query);
        // The parser should throw an exception when trying to parse an invalid query
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidQueryWithIllegalArgumentException() {
        String query = "div.*class1#id1 p";
        // The query "div.*class1#id1 p" is invalid because it contains a wildcard (.) in the class name
        Evaluator evaluator = null;
        try {
            evaluator = QueryParser.parse(query);
        } catch (Selector.SelectorParseException se) {
            // If the parser throws a SelectorParseException, it means the query is syntactically incorrect
            // In this case, we're testing for a different error: an IllegalArgumentException caused by a wildcard in the class name
            throw new RuntimeException("Unexpected SelectorParseException", se);
        }
        // The parser should throw an IllegalArgumentException when encountering a wildcard in the class name
    }

    @Test
    public void testParsePseudoClasses() {
        String query = "p:first-child";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsFirstChild());
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
    }

    @Test
    public void testParsePseudoElements() {
        String query = "p:first-of-type";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsFirstOfType());
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
    }

    @Test
    public void testParseNthChildSelectors() {
        String query = "p:nth-child(2)";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsNthChild(1, 1));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
    }

    @Test
    public void testParseNthOfTypeSelectors() {
        String query = "p:nth-of-type(2)";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsNthOfType(1, 1));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
    }

    @Test
    public void testParseNthLastChildSelectors() {
        String query = "p:nth-last-child(2)";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsNthLastChild(1, 1));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
    }

    @Test
    public void testParseNthLastOfTypeSelectors() {
        String query = "p:nth-last-of-type(2)";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsNthLastOfType(1, 1));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
    }
}