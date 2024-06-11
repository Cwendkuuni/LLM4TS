package org.jsoup.select.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.AllElements.class, evaluator.getClass());
    }

    @Test
    public void testParseSimpleTagQuery() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.Tag.class, evaluator.getClass());
        Assert.assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseIdQuery() {
        String query = "#id123";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.Id.class, evaluator.getClass());
        Assert.assertEquals("#id123", evaluator.toString());
    }

    @Test
    public void testParseClassQuery() {
        String query = ".class1";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.Class.class, evaluator.getClass());
        Assert.assertEquals(".class1", evaluator.toString());
    }

    @Test
    public void testParseMultipleClassesQuery() {
        String query = ".class1.class2";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        List<Evaluator> evals = ((CombiningEvaluator.And) evaluator).getEvaluators();
        Assert.assertEquals(2, evals.size());
        Assert.assertEquals(Evaluator.Class.class, evals.get(0).getClass());
        Assert.assertEquals(".class1", evals.get(0).toString());
        Assert.assertEquals(Evaluator.Class.class, evals.get(1).getClass());
        Assert.assertEquals(".class2", evals.get(1).toString());
    }

    @Test
    public void testParseDescendantQuery() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        List<Evaluator> evals = ((CombiningEvaluator.And) evaluator).getEvaluators();
        Assert.assertEquals(2, evals.size());
        Assert.assertEquals(Evaluator.Tag.class, evals.get(0).getClass());
        Assert.assertEquals("div", evals.get(0).toString());
        Assert.assertEquals(StructuralEvaluator.ImmediateParent.class, evals.get(1).getClass());
    }

    @Test
    public void testParseAdjacentQuery() {
        String query = "div + p";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        List<Evaluator> evals = ((CombiningEvaluator.And) evaluator).getEvaluators();
        Assert.assertEquals(2, evals.size());
        Assert.assertEquals(Evaluator.Tag.class, evals.get(0).getClass());
        Assert.assertEquals("div", evals.get(0).toString());
        Assert.assertEquals(StructuralEvaluator.ImmediatePreviousSibling.class, evals.get(1).getClass());
    }

    @Test
    public void testParseSiblingQuery() {
        String query = "div ~ p";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        List<Evaluator> evals = ((CombiningEvaluator.And) evaluator).getEvaluators();
        Assert.assertEquals(2, evals.size());
        Assert.assertEquals(Evaluator.Tag.class, evals.get(0).getClass());
        Assert.assertEquals("div", evals.get(0).toString());
        Assert.assertEquals(StructuralEvaluator.PreviousSibling.class, evals.get(1).getClass());
    }

    @Test
    public void testParseAttributeQuery() {
        String query = "[href]";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.Attribute.class, evaluator.getClass());
        Assert.assertEquals("[href]", evaluator.toString());
    }

    @Test
    public void testParseAttributeValueQuery() {
        String query = "[href='https://www.example.com']";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.AttributeWithValue.class, evaluator.getClass());
        Assert.assertEquals("[href='https://www.example.com']", evaluator.toString());
    }

    @Test
    public void testParseAttributeStartingQuery() {
        String query = "[href^='https']";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.AttributeStarting.class, evaluator.getClass());
        Assert.assertEquals("[href^='https']", evaluator.toString());
    }

    @Test
    public void testParseAttributeEndingQuery() {
        String query = "[href$='.pdf']";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.AttributeEnding.class, evaluator.getClass());
        Assert.assertEquals("[href$='.pdf']", evaluator.toString());
    }

    @Test
    public void testParseAttributeContainsQuery() {
        String query = "[href*='example']";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.AttributeWithValueContaining.class, evaluator.getClass());
        Assert.assertEquals("[href*='example']", evaluator.toString());
    }

    @Test
    public void testParseAttributeMatchingQuery() {
        String query = "[href~='example']";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.AttributeWithValueMatching.class, evaluator.getClass());
        Assert.assertEquals("[href~='example']", evaluator.toString());
    }

    @Test
    public void testParsePseudoClassQuery() {
        String query = ":first-child";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.PseudoClass.FirstChild.class, evaluator.getClass());
        Assert.assertEquals(":first-child", evaluator.toString());
    }

    @Test
    public void testParseMultiplePseudoClassQuery() {
        String query = ":first-child:last-child";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        List<Evaluator> evals = ((CombiningEvaluator.And) evaluator).getEvaluators();
        Assert.assertEquals(2, evals.size());
        Assert.assertEquals(Evaluator.PseudoClass.FirstChild.class, evals.get(0).getClass());
        Assert.assertEquals(":first-child", evals.get(0).toString());
        Assert.assertEquals(Evaluator.PseudoClass.LastChild.class, evals.get(1).getClass());
        Assert.assertEquals(":last-child", evals.get(1).toString());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "div^p";
        QueryParser.parse(query);
    }
}