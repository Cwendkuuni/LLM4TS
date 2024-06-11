package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.select.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.jsoup.helper.StringUtil;

public class QueryParserTest {
    @Test
    public void testParseEmptyQuery() {
        String query = "";
        Evaluator actual = QueryParser.parse(query);
        assertTrue(actual instanceof Evaluator.AllElements);
    }

    @Test
    public void testParseTagQuery() {
        String query = "div";
        Evaluator actual = QueryParser.parse(query);
        assertTrue(actual instanceof Evaluator.Tag);
        assertEquals(Evaluator.Tag.class, actual.getClass());
        assertEquals("div", ((Evaluator.Tag) actual).getTagName());
    }

    @Test
    public void testParseIdQuery() {
        String query = "#header";
        Evaluator actual = QueryParser.parse(query);
        assertTrue(actual instanceof Evaluator.Id);
        assertEquals("header", ((Evaluator.Id) actual).getId());
    }

    @Test
    public void testParseClassQuery() {
        String query = ".button";
        Evaluator actual = QueryParser.parse(query);
        assertTrue(actual instanceof Evaluator.Class);
        assertEquals("button", ((Evaluator.Class) actual).getClassName());
    }

    @Test
    public void testParseAttributeQuery() {
        String query = "[title='Test']";
        Evaluator actual = QueryParser.parse(query);
        assertTrue(actual instanceof Evaluator.Attribute);
        assertEquals("title", ((Evaluator.Attribute) actual).getAttributeName());
        assertEquals("Test", ((Evaluator.Attribute) actual).getAttributeValue());
    }

    @Test
    public void testParseCombinatorQuery() {
        String query = "div > ul > li";
        Evaluator actual = QueryParser.parse(query);
        assertTrue(actual instanceof CombiningEvaluator.And);
        assertEquals(2, ((CombiningEvaluator.And) actual).getChildren().size());
        assertTrue(((CombiningEvaluator.And) actual).getChildren().get(0) instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) ((CombiningEvaluator.And) actual).getChildren().get(0)).getTagName());
        assertTrue(((CombiningEvaluator.And) actual).getChildren().get(1) instanceof CombiningEvaluator.And);
        assertEquals(2, ((CombiningEvaluator.And) ((CombiningEvaluator.And) actual).getChildren().get(1)).getChildren().size());
        assertTrue(((CombiningEvaluator.And) ((CombiningEvaluator.And) actual).getChildren().get(1)).getChildren().get(0) instanceof Evaluator.Tag);
        assertEquals("ul", ((Evaluator.Tag) ((CombiningEvaluator.And) ((CombiningEvaluator.And) actual).getChildren().get(1)).getChildren().get(0)).getTagName());
        assertTrue(((CombiningEvaluator.And) ((CombiningEvaluator.And) actual).getChildren().get(1)).getChildren().get(1) instanceof Evaluator.Tag);
        assertEquals("li", ((Evaluator.Tag) ((CombiningEvaluator.And) ((CombiningEvaluator.And) actual).getChildren().get(1)).getChildren().get(1)).getTagName());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "div#header:not(#footer)#test";
        Evaluator actual = QueryParser.parse(query);
        fail("This query should throw a Selector.SelectorParseException");
    }

    @Test
    public void testParseComplexQuery() {
        String query = "div > ul > li:first-child > a[href^='http'][target='_blank']";
        Evaluator actual = QueryParser.parse(query);
        // Complex logic to assert the parsed query
        assertTrue(actual instanceof CombiningEvaluator.And);
        assertEquals(4, ((CombiningEvaluator.And) actual).getChildren().size());
        assertTrue(((CombiningEvaluator.And) actual).getChildren().get(0) instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) ((CombiningEvaluator.And) actual).getChildren().get(0)).getTagName());
        assertTrue(((CombiningEvaluator.And) actual).getChildren().get(1) instanceof CombiningEvaluator.And);
        assertEquals(3, ((CombiningEvaluator.And) ((CombiningEvaluator.And) actual).getChildren().get(1)).getChildren().size());
        assertTrue(((CombiningEvaluator.And) ((CombiningEvaluator.And) actual).getChildren().get(1)).getChildren().get(0) instanceof StructuralEvaluator.ImmediateChild);
        assertEquals("ul", ((Evaluator.Tag) ((CombiningEvaluator.And) ((CombiningEvaluator.And) actual).getChildren().get(1)).getChildren().get(0)).getTagName());
        assertTrue(((CombiningEvaluator.And) ((CombiningEvaluator.And) actual).getChildren().get(1)).getChildren().get(1) instanceof Evaluator.Class);
        assertEquals("li", ((Evaluator.Tag) ((CombiningEvaluator.And) ((CombiningEvaluator.And) actual).getChildren().get(1)).getChildren().get(2)).getTagName());
        assertTrue(((CombiningEvaluator.And) ((CombiningEvaluator.And) actual).getChildren().get(1)).getChildren().get(2) instanceof CombiningEvaluator.And);
        assertEquals(2, ((CombiningEvaluator.And) ((CombiningEvaluator.And) actual).getChildren().get(1)).getChildren().get(2).getChildren().size());
        assertTrue(((CombiningEvaluator.And) ((CombiningEvaluator.And) actual).getChildren().get(1)).getChildren().get(2).getChildren().get(0) instanceof Evaluator.Attribute);
        assertEquals("href", ((Evaluator.Attribute) ((CombiningEvaluator.And) ((CombiningEvaluator.And) actual).getChildren().get(1)).getChildren().get(2).getChildren().get(0)).getAttributeName());
        assertTrue(((Evaluator.Attribute) ((CombiningEvaluator.And) ((CombiningEvaluator.And) actual).getChildren().get(1)).getChildren().get(2).getChildren().get(0)).getAttributeValue().startsWith("http"));
        assertTrue(((CombiningEvaluator.And) ((CombiningEvaluator.And) actual).getChildren().get(1)).getChildren().get(2).getChildren().get(1) instanceof Evaluator.Attribute);
        assertEquals("target", ((Evaluator.Attribute) ((CombiningEvaluator.And) ((CombiningEvaluator.And) actual).getChildren().get(1)).getChildren().get(2).getChildren().get(1)).getAttributeName());
        assertEquals("_blank", ((Evaluator.Attribute) ((CombiningEvaluator.And) ((CombiningEvaluator.And) actual).getChildren().get(1)).getChildren().get(2).getChildren().get(1)).getAttributeValue());
    }
}