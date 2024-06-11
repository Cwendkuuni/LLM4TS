package org.jsoup.select;

import static org.junit.Assert.*;

import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;

import java.util.List;

public class QueryParserTest {

    @org.junit.Test
    public void testParseSimpleSelector() {
        String query = "div";
        QueryParser parser = QueryParser.parse(query);
        List<Evaluator> evaluators = parser.evaluators();

        assertEquals(1, evaluators.size());
        Evaluator evaluator = evaluators.get(0);
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) evaluator).tagName());
    }

    @org.junit.Test
    public void testParseIdSelector() {
        String query = "#myId";
        QueryParser parser = QueryParser.parse(query);
        List<Evaluator> evaluators = parser.evaluators();

        assertEquals(1, evaluators.size());
        Evaluator evaluator = evaluators.get(0);
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("myId", ((Evaluator.Id) evaluator).id());
    }

    @org.junit.Test
    public void testParseClassSelector() {
        String query = ".myClass";
        QueryParser parser = QueryParser.parse(query);
        List<Evaluator> evaluators = parser.evaluators();

        assertEquals(1, evaluators.size());
        Evaluator evaluator = evaluators.get(0);
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("myClass", ((Evaluator.Class) evaluator).className());
    }

    @org.junit.Test
    public void testParseTagWithAttributeSelector() {
        String query = "input[type='text']";
        QueryParser parser = QueryParser.parse(query);
        List<Evaluator> evaluators = parser.evaluators();

        assertEquals(1, evaluators.size());
        Evaluator evaluator = evaluators.get(0);
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("type", ((Evaluator.Attribute) evaluator).attributeName());
        assertEquals("text", ((Evaluator.Attribute) evaluator).attributeValue());
    }

    @org.junit.Test
    public void testParseCombinedSelector() {
        String query = "div.myClass > input[type='submit']";
        QueryParser parser = QueryParser.parse(query);
        List<Evaluator> evaluators = parser.evaluators();

        assertEquals(3, evaluators.size());
        Evaluator rootEvaluator = evaluators.get(0);
        assertTrue(rootEvaluator instanceof CombiningEvaluator.And);

        List<Evaluator> rootChildren = ((CombiningEvaluator.And) rootEvaluator).children();
        Evaluator tagEvaluator = rootChildren.get(0);
        assertTrue(tagEvaluator instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) tagEvaluator).tagName());

        Evaluator classEvaluator = rootChildren.get(1);
        assertTrue(classEvaluator instanceof Evaluator.Class);
        assertEquals("myClass", ((Evaluator.Class) classEvaluator).className());

        List<Evaluator> childrenOfClass = ((CombiningEvaluator.And) rootEvaluator).children().get(1).children();
        Evaluator tagAndAttributeEvaluator = childrenOfClass.get(0);
        assertTrue(tagAndAttributeEvaluator instanceof CombiningEvaluator.And);

        Evaluator tagEvaluator2 = ((CombiningEvaluator.And) tagAndAttributeEvaluator).children().get(0);
        assertTrue(tagEvaluator2 instanceof Evaluator.Tag);
        assertEquals("input", ((Evaluator.Tag) tagEvaluator2).tagName());

        Evaluator attributeEvaluator = ((CombiningEvaluator.And) tagAndAttributeEvaluator).children().get(1);
        assertTrue(attributeEvaluator instanceof Evaluator.Attribute);
        assertEquals("type", ((Evaluator.Attribute) attributeEvaluator).attributeName());
        assertEquals("submit", ((Evaluator.Attribute) attributeEvaluator).attributeValue());
    }
}