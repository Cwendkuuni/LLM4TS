package org.junit.tests;

import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {
    @Test
    public void testParseEmptyQuery() {
        String query = "";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator.matchesEmpty());
    }

    @Test
    public void testParseTagSelector() {
        String query = "div";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) evaluator).getTagName());
    }

    @Test
    public void testParseClassSelector() {
        String query = ".class-name";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("class-name", ((Evaluator.Class) evaluator).getClassValue());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidSelector() {
        String query = "div#invalid-id";
        QueryParser parser = new QueryParser(query);
        parser.parse(); // should throw an exception
    }

    @Test
    public void testParseCombinator() {
        String query = "div > p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and = (CombiningEvaluator.And) evaluator;
        assertTrue(and.getLeft() instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) and.getLeft()).getTagName());
        assertTrue(and.getRight() instanceof Evaluator.Tag);
        assertEquals("p", ((Evaluator.Tag) and.getRight()).getTagName());
    }

    @Test
    public void testParseDescendantSelector() {
        String query = "div p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and = (CombiningEvaluator.And) evaluator;
        assertTrue(and.getLeft() instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) and.getLeft()).getTagName());
        assertTrue(and.getRight() instanceof StructuralEvaluator.Descendant);
        assertEquals("p", ((StructuralEvaluator.Descendant) and.getRight()).getTagName());
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "input[type=text]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Attribute);
        Evaluator.Attribute attribute = (Evaluator.Attribute) evaluator;
        assertEquals("type", attribute.getAttributeName());
        assertEquals("text", attribute.getAttributeValue());
    }

    @Test
    public void testParseAttributeWithValueSelector() {
        String query = "input[type='text']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        Evaluator.AttributeWithValue attribute = (Evaluator.AttributeWithValue) evaluator;
        assertEquals("type", attribute.getAttributeName());
        assertEquals("text", attribute.getAttributeValue());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidAttributeSelector() {
        String query = "input[type=text]#invalid-id";
        QueryParser parser = new QueryParser(query);
        parser.parse(); // should throw an exception
    }
}