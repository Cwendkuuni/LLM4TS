import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.select.QueryParser;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;

import java.util.List;
import java.util.ArrayList;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        // An empty query should return an empty list
        assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseSimpleTagSelector() {
        String query = "p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("p"));
        assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseClassSelector() {
        String query = ".class-name";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Class("class-name"));
        assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseIdSelector() {
        String query = "#id";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Id("id"));
        assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "[href='http://example.com']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.AttributeWithValue("href", "http://example.com"));
        assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseAttributeSelectorWithOperators() {
        String query = "[src='image.png']:not([class='disabled'])";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new StructuralEvaluator.Not(new Evaluator.Class("disabled")));
        expected.add(new Evaluator.AttributeWithValue("src", "image.png"));
        assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "p.class-name#id > div.another-class > ul li";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("div"), new Evaluator.Class("another-class")));
        expected.add(new Evaluator.Tag("ul"));
        expected.add(new Evaluator.Tag("li"));
        expected.add(new Evaluator.Id("id"));
        expected.add(new Evaluator.Tag("p"));
        expected.add(new Evaluator.Class("class-name"));
        assertEquals(expected, evaluator.getChildren());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "invalid-query";
        new QueryParser(query).parse();
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidAttributeSelector() {
        String query = "[attr='value'=]";
        new QueryParser(query).parse();
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidCombinator() {
        String query = "p~invalid-combinator > div.another-class";
        new QueryParser(query).parse();
    }
}