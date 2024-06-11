package org.jsoup.select.test;

import org.junit.Test;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParseSimpleQuery() {
        String query = "div.class1";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("class1", evaluator.getClassValue());
    }

    @Test
    public void testParseQueryWithId() {
        String query = "#id1";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("id1", evaluator.getIdValue());
    }

    @Test
    public void testParseQueryWithTag() {
        String query = "p";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("p", evaluator.getTagName());
    }

    @Test
    public void testParseQueryWithMultipleSelectors() {
        String query = "div.class1 p.class2";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.CombiningEvaluator.And);
        Evaluator[] evaluators = ((Evaluator.CombiningEvaluator.And) evaluator).getEvaluators();
        assertEquals(2, evaluators.length);
        assertEquals(evaluators[0].getClass(), Evaluator.Class.class);
        assertEquals(evaluators[0].getClassValue(), "class1");
        assertEquals(evaluators[1].getClass(), Evaluator.Class.class);
        assertEquals(evaluators[1].getClassValue(), "class2");
    }

    @Test
    public void testParseQueryWithAttribute() {
        String query = "a[href]";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("href", evaluator.getAttributeName());
        assertEquals(Evaluator.AttributeType.PRESENCE, evaluator.getAttributeType());
    }

    @Test
    public void testParseQueryWithAttributeValue() {
        String query = "a[href='http://example.com']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        assertEquals("href", evaluator.getAttributeName());
        assertEquals("http://example.com", evaluator.getValue());
    }

    @Test
    public void testParseQueryWithAttributeValueStarting() {
        String query = "a[href^='http://example']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueStarting);
        assertEquals("href", evaluator.getAttributeName());
        assertEquals("http://example", evaluator.getValue().substring(0, 7));
    }

    @Test
    public void testParseQueryWithAttributeValueEnding() {
        String query = "a[href$='.pdf']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
        assertEquals("href", evaluator.getAttributeName());
        assertEquals(".pdf", evaluator.getValue());
    }

    @Test
    public void testParseQueryWithAttributeValueContaining() {
        String query = "a[href*='example']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
        assertEquals("href", evaluator.getAttributeName());
        assertTrue(evaluator.getValue().contains("example"));
    }

    @Test
    public void testParseQueryWithAttributeValueMatching() {
        String query = "a[href~='example']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
        assertEquals("href", evaluator.getAttributeName());
        Pattern pattern = (Pattern) evaluator.getValue();
        assertTrue(pattern.matcher("example.com").matches());
        assertTrue(pattern.matcher("example_page.html").matches());
    }

    @Test
    public void testParseQueryWithCombinatorGreaterThan() {
        String query = "p > div";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.CombiningEvaluator.Descendant);
        assertEquals(Evaluator.CombiningOperator.GT, evaluator.getCombiningOperator());
    }

    @Test
    public void testParseQueryWithCombinatorLessThan() {
        String query = "div < p";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.CombiningEvaluator.Descendant);
        assertEquals(Evaluator.CombiningOperator.LT, evaluator.getCombiningOperator());
    }

    @Test
    public void testParseQueryWithCombinatorAnd() {
        String query = "div.class1.class2";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.CombiningEvaluator.And);
        Evaluator[] evaluators = ((Evaluator.CombiningEvaluator.And) evaluator).getEvaluators();
        assertEquals(2, evaluators.length);
        assertEquals(evaluators[0].getClass(), Evaluator.Class.class);
        assertEquals(evaluators[0].getClassValue(), "class1");
        assertEquals(evaluators[1].getClass(), Evaluator.Class.class);
        assertEquals(evaluators[1].getClassValue(), "class2");
    }

    @Test
    public void testParseQueryWithCombinatorOr() {
        String query = "div.class1, div.class2";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.CombiningEvaluator.Or);
        Evaluator[] evaluators = ((Evaluator.CombiningEvaluator.Or) evaluator).getEvaluators();
        assertEquals(2, evaluators.length);
        assertEquals(evaluators[0].getClass(), Evaluator.Class.class);
        assertEquals(evaluators[0].getClassValue(), "class1");
        assertEquals(evaluators[1].getClass(), Evaluator.Class.class);
        assertEquals(evaluators[1].getClassValue(), "class2");
    }

    @Test
    public void testParseQueryWithPseudoClassFirstChild() {
        String query = "div > p:first-child";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.PseudoClassFirstChild);
    }

    @Test
    public void testParseQueryWithPseudoClassLastChild() {
        String query = "div > p:last-child";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.PseudoClassLastChild);
    }

    @Test
    public void testParseQueryWithPseudoClassLastOfType() {
        String query = "li:last-of-type";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.PseudoClassLastOfType);
    }

    @Test
    public void testParseQueryWithPseudoClassNthOfType() {
        String query = "li:nth-of-type(3)";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.PseudoClassNthOfType);
        assertEquals(3, evaluator.getNthTypeValue());
    }

    @Test
    public void testParseQueryWithPseudoClassNthLastOfType() {
        String query = "li:nth-last-of-type(3)";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.PseudoClassNthLastOfType);
        assertEquals(3, evaluator.getNthTypeValue());
    }

    @Test
    public void testParseQueryWithPseudoClassOnlyChild() {
        String query = "div:only-child";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.PseudoClassOnlyChild);
    }

    @Test
    public void testParseQueryWithPseudoClassOnlyOfType() {
        String query = "div:only-of-type";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.PseudoClassOnlyOfType);
    }

    @Test
    public void testParseQueryWithPseudoClassEmpty() {
        String query = "div:empty";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.PseudoClassEmpty);
    }

    @Test
    public void testParseQueryWithPseudoClassRoot() {
        String query = "html:root";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.PseudoClassRoot);
    }

    @Test
    public void testParseQueryWithPseudoClassHas() {
        String query = "div:has(p)";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.StructuralEvaluator.Has);
    }

    @Test
    public void testParseQueryWithPseudoClassContainsText() {
        String query = "div:contains(text)";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.TextContains);
        assertEquals("text", evaluator.getTextValue());
    }

    @Test
    public void testParseQueryWithPseudoClassContainsOwnText() {
        String query = "div:containsOwn(text)";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.TextContainsOwn);
        assertEquals("text", evaluator.getTextValue());
    }

    @Test
    public void testParseQueryWithPseudoClassContainsData() {
        String query = "div:containsData(data)";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.TextContainsData);
        assertEquals("data", evaluator.getDataValue());
    }

    @Test
    public void testParseQueryWithPseudoClassMatches() {
        String query = "div:matches(regex)";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.TextMatches);
        Pattern pattern = (Pattern) evaluator.getTextPattern();
        assertTrue(pattern.matcher("example text").matches());
    }

    @Test
    public void testParseQueryWithPseudoClassMatchesOwn() {
        String query = "div:matchesOwn(regex)";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.TextMatchesOwn);
        Pattern pattern = (Pattern) evaluator.getTextPattern();
        assertTrue(pattern.matcher("example text").matches());
    }

    @Test
    public void testParseQueryWithPseudoClassNot() {
        String query = "div:not(p)";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.StructuralEvaluator.Not);
    }
}