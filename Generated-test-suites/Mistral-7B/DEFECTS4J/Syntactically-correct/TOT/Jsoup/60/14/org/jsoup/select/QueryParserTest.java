import org.jsoup.parser.QueryParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.class, evaluator.getClass());
        Assert.assertTrue(evaluator.isEmpty());
    }

    @Test
    public void testParseTagQuery() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.Tag.class, evaluator.getClass());
        Assert.assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseClassQuery() {
        String query = ".button";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.Class.class, evaluator.getClass());
        Assert.assertEquals(".button", evaluator.toString());
    }

    @Test
    public void testParseIdQuery() {
        String query = "#header";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.Id.class, evaluator.getClass());
        Assert.assertEquals("#header", evaluator.toString());
    }

    @Test
    public void testParseAttributeQuery() {
        String query = "[href]";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.Attribute.class, evaluator.getClass());
        Assert.assertEquals("[href]", evaluator.toString());
    }

    @Test
    public void testParseSimpleCombinationQuery() {
        String query = "div.button";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        Assert.assertTrue(evaluator instanceof CombiningEvaluator.And);
        List<Evaluator> children = ((CombiningEvaluator.And) evaluator).getChildren();
        Assert.assertEquals(2, children.size());
        Assert.assertEquals(Evaluator.Tag.class, children.get(0).getClass());
        Assert.assertEquals("div", children.get(0).toString());
        Assert.assertEquals(Evaluator.Class.class, children.get(1).getClass());
        Assert.assertEquals(".button", children.get(1).toString());
    }

    @Test
    public void testParseComplexCombinationQuery() {
        String query = "ul > li.error > a[href^='mailto:']";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        Assert.assertTrue(evaluator instanceof CombiningEvaluator.And);
        List<Evaluator> children = ((CombiningEvaluator.And) evaluator).getChildren();
        Assert.assertEquals(4, children.size());
        Assert.assertEquals(CombiningEvaluator.And.class, children.get(0).getClass());
        List<Evaluator> grandChildren = ((CombiningEvaluator.And) children.get(0)).getChildren();
        Assert.assertEquals(2, grandChildren.size());
        Assert.assertEquals(Evaluator.Tag.class, grandChildren.get(0).getClass());
        Assert.assertEquals("ul", grandChildren.get(0).toString());
        Assert.assertEquals(Evaluator.Tag.class, grandChildren.get(1).getClass());
        Assert.assertEquals("li", grandChildren.get(1).toString());
        Assert.assertEquals(CombiningEvaluator.And.class, children.get(1).getClass());
        List<Evaluator> children2 = ((CombiningEvaluator.And) children.get(1)).getChildren();
        Assert.assertEquals(2, children2.size());
        Assert.assertEquals(Evaluator.Class.class, children2.get(0).getClass());
        Assert.assertEquals(".error", children2.get(0).toString());
        Assert.assertEquals(Evaluator.Tag.class, children2.get(1).getClass());
        Assert.assertEquals("a", children2.get(1).toString());
        Assert.assertEquals(Evaluator.Attribute.class, children.get(2).getClass());
        Assert.assertEquals("[href]", children.get(2).toString());
        Assert.assertEquals(AttributeEvaluator.StartsWith.class, children.get(3).getClass());
        Assert.assertEquals("mailto:", children.get(3).toString());
    }

    @Test
    public void testParseInvalidQuery() {
        String query = ":invalid";
        try {
            Evaluator evaluator = QueryParser.parse(query);
            Assert.fail("Expected Selector.SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            Assert.assertEquals("Unknown selector token: :invalid", e.getMessage());
        }
    }

    @Test
    public void testParseComplexQueryWithParentReference() {
        String query = "li > input:last-child > input";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        Assert.assertTrue(evaluator instanceof CombiningEvaluator.And);
        List<Evaluator> children = ((CombiningEvaluator.And) evaluator).getChildren();
        Assert.assertEquals(3, children.size());
        Assert.assertEquals(Evaluator.Tag.class, children.get(0).getClass());
        Assert.assertEquals("li", children.get(0).toString());
        Assert.assertEquals(CombiningEvaluator.And.class, children.get(1).getClass());
        List<Evaluator> children2 = ((CombiningEvaluator.And) children.get(1)).getChildren();
        Assert.assertEquals(2, children2.size());
        Assert.assertEquals(Evaluator.Tag.class, children2.get(0).getClass());
        Assert.assertEquals("input", children2.get(0).toString());
        Assert.assertEquals(CombiningEvaluator.LastChild.class, children2.get(1).getClass());
        Assert.assertEquals("input", children.get(2).toString());
    }
}