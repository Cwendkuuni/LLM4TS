package org.jsoup.select;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        QueryParser queryParser = new QueryParser("");
        Assert.assertEquals(Evaluator.class, queryParser.parse().getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidQuery() {
        QueryParser queryParser = new QueryParser("invalidQuery");
    }

    @Test
    public void testParseTagSelector() {
        String query = "div";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        Assert.assertEquals(Evaluator.Tag.class, evaluator.getClass());
        Assert.assertEquals("div", ((Evaluator.Tag) evaluator).getElementName());
    }

    @Test
    public void testParseClassSelector() {
        String query = ".myClass";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        Assert.assertEquals(Evaluator.Class.class, evaluator.getClass());
        Assert.assertEquals("myClass", ((Evaluator.Class) evaluator).getCssClassName());
    }

    @Test
    public void testParseIdSelector() {
        String query = "#myId";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        Assert.assertEquals(Evaluator.Id.class, evaluator.getClass());
        Assert.assertEquals("myId", ((Evaluator.Id) evaluator).getId());
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "[title='Hello World']";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        Assert.assertEquals(Evaluator.Attribute.class, evaluator.getClass());
        Assert.assertEquals("title", ((Evaluator.Attribute) evaluator).getAttributeName());
        Assert.assertEquals("Hello World", ((Evaluator.Attribute) evaluator).getAttributeValue());
    }

    @Test
    public void testParseAttributeWithValueStartingSelector() {
        String query = "[data-src^='image']";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        Assert.assertEquals(Evaluator.AttributeWithValueStarting.class, evaluator.getClass());
        Assert.assertEquals("data-src", ((Evaluator.AttributeWithValueStarting) evaluator).getAttributeName());
        Assert.assertEquals("image", ((Evaluator.AttributeWithValueStarting) evaluator).getAttributeValueStart());
    }

    @Test
    public void testParseAttributeWithValueEndingSelector() {
        String query = "[data-src$='.png']";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        Assert.assertEquals(Evaluator.AttributeWithValueEnding.class, evaluator.getClass());
        Assert.assertEquals("data-src", ((Evaluator.AttributeWithValueEnding) evaluator).getAttributeName());
        Assert.assertEquals(".png", ((Evaluator.AttributeWithValueEnding) evaluator).getAttributeValueEnd());
    }

    @Test
    public void testParseAttributeWithValueContainingSelector() {
        String query = "[data-src*='image']";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        Assert.assertEquals(Evaluator.AttributeWithValueContaining.class, evaluator.getClass());
        Assert.assertEquals("data-src", ((Evaluator.AttributeWithValueContaining) evaluator).getAttributeName());
        Assert.assertEquals("image", ((Evaluator.AttributeWithValueContaining) evaluator).getAttributeValueContaining());
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "div.myClass > ul > li:first-child";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();

        List<Evaluator> evaluators = new ArrayList<>();
        Evaluator tagEvaluator = new Evaluator.Tag("div");
        Evaluator classEvaluator = new Evaluator.Class("myClass");
        Evaluator structuralEvaluator1 = new StructuralEvaluator.ImmediateChild();
        Evaluator tagUL = new Evaluator.Tag("ul");
        Evaluator structuralEvaluator2 = new StructuralEvaluator.IsFirstChild();

        evaluators.add(tagEvaluator);
        evaluators.add(classEvaluator);
        evaluators.add(structuralEvaluator1);
        evaluators.add(tagUL);
        evaluators.add(structuralEvaluator2);

        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(evaluators.subList(0, evaluators.size() - 1).toArray(new Evaluator[0]));
        evaluators.set(evaluators.size() - 1, andEvaluator);

        Assert.assertEquals(evaluators.get(evaluators.size() - 1).getClass(), evaluator.getClass());
        Assert.assertTrue(evaluators.get(evaluators.size() - 1).equals(evaluator));
    }
}