import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.QueryParser;
import org.jsoup.select.Evaluator;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.class, evaluator.getClass());
        Assert.assertFalse(evaluator.matches(null));
    }

    @Test
    public void testParseSimpleTagQuery() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.Tag.class, evaluator.getClass());
        Assert.assertTrue(evaluator.matches(new org.jsoup.nodes.Element("div")));
    }

    @Test
    public void testParseTagWithIdQuery() {
        String query = "div#myId";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.Id.class, evaluator.getClass());
        Assert.assertTrue(evaluator.matches(new org.jsoup.nodes.Element("div", "myId")));
    }

    @Test
    public void testParseTagWithClassQuery() {
        String query = "div.myClass";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.Class.class, evaluator.getClass());
        Assert.assertTrue(evaluator.matches(new org.jsoup.nodes.Element("div", "class", "myClass")));
    }

    @Test
    public void testParseMultipleSelectorsQuery() {
        String query = "div#myId.myClass";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        Assert.assertTrue(evaluator.matches(new org.jsoup.nodes.Element("div", "id", "myId", "class", "myClass")));
    }

    @Test
    public void testParseDescendantSelectorQuery() {
        String query = "div > ul > li";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(StructuralEvaluator.ImmediateParent.class, evaluator.getClass());
        Assert.assertTrue(evaluator.matches(new org.jsoup.nodes.Element("li", new org.jsoup.nodes.Element("ul", new org.jsoup.nodes.Element("div")))));
    }

    @Test
    public void testParseAttributeQuery() {
        String query = "input[type='text']";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.Attribute.class, evaluator.getClass());
        Assert.assertTrue(evaluator.matches(new org.jsoup.nodes.Element("input", "type", "text")));
    }

    @Test
    public void testParseAttributeValueQuery() {
        String query = "input[type='text'][value='Search']";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.AttributeWithValue.class, evaluator.getClass());
        Assert.assertTrue(evaluator.matches(new org.jsoup.nodes.Element("input", "type", "text", "value", "Search")));
    }

    @Test
    public void testParsePseudoClassQuery() {
        String query = "a:link";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.PseudoClass.class, evaluator.getClass());
        Assert.assertTrue(evaluator.matches(new org.jsoup.nodes.Element("a")));
    }

    @Test
    public void testParsePseudoClassNegatedQuery() {
        String query = "a:not(:link)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(StructuralEvaluator.Not.class, evaluator.getClass());
        Assert.assertTrue(evaluator.matches(new org.jsoup.nodes.Element("a", new Evaluator.PseudoClass(":link"))));
    }

    @Test
    public void testParseComplexQuery() {
        String query = "li.active:hover > ul > li.highlight:containsOwn('Useful')";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        Assert.assertTrue(evaluator.matches(new org.jsoup.nodes.Element("li", "class", "active", new Evaluator.Event("hover"))));
        Assert.assertTrue(evaluator.matches(new org.jsoup.nodes.Element("li", "class", "highlight", new Evaluator.ContainsOwnText("Useful"))));
    }
}