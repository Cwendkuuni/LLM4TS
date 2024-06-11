package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;

import org.junit.Test;

/**
 * Test class for QueryParser
 */
public class QueryParserTest {

    @Test
    public void testParseSimpleSelectors() {
        String query = "div#main > p:first-child";
        QueryParser parser = QueryParser.parse(query);
        List<Evaluator> evaluators = parser.parse();

        assertNotNull(evaluators);
        assertEquals(2, evaluators.size());

        Evaluator evaluator1 = evaluators.get(0);
        Evaluator evaluator2 = evaluators.get(1);

        assertEquals(Evaluator.Type.STRUCTURAL, evaluator1.getType());
        assertEquals(StructuralEvaluator.Type.IMMEDIATE_PARENT, evaluator1.getTypeSpecific());
        assertEquals("div", evaluator1.getCssQuerySelectorElement().tagName());
        assertEquals("#main", evaluator1.getCssQuerySelectorElement().id());

        assertEquals(Evaluator.Type.ELEMENT, evaluator2.getType());
        assertEquals("p", evaluator2.getCssQuerySelectorElement().tagName());
        assertEquals(Evaluator.IsFirstChild.class, evaluator2.getClass());
    }

    @Test
    public void testParseComplexSelectors() {
        String query = "ul > li > a[href^='http']:contains(Google)";
        QueryParser parser = QueryParser.parse(query);
        List<Evaluator> evaluators = parser.parse();

        assertNotNull(evaluators);
        assertEquals(3, evaluators.size());

        Evaluator evaluator1 = evaluators.get(0);
        Evaluator evaluator2 = evaluators.get(1);
        Evaluator evaluator3 = evaluators.get(2);

        assertEquals(Evaluator.Type.STRUCTURAL, evaluator1.getType());
        assertEquals(StructuralEvaluator.Type.IMMEDIATE_PARENT, evaluator1.getTypeSpecific());
        assertEquals("ul", evaluator1.getCssQuerySelectorElement().tagName());

        assertEquals(Evaluator.Type.ELEMENT, evaluator2.getType());
        assertEquals("li", evaluator2.getCssQuerySelectorElement().tagName());

        assertEquals(Evaluator.Type.ATTRIBUTE, evaluator3.getType());
        assertEquals("a", evaluator3.getCssQuerySelectorElement().tagName());
        assertEquals("href", evaluator3.getAttributeName());
        assertEquals(Evaluator.AttributeWithValueStarting.class, evaluator3.getClass());
        assertEquals("http", evaluator3.getAttributeValue());

        assertEquals(Evaluator.ContainsText.class, evaluator3.getNextElement().getClass());
        assertEquals("Google", evaluator3.getNextElement().getTextValue());
    }

    @Test
    public void testParsePseudoSelectors() {
        String query = ":first-child";
        QueryParser parser = QueryParser.parse(query);
        List<Evaluator> evaluators = parser.parse();

        assertNotNull(evaluators);
        assertEquals(1, evaluators.size());

        Evaluator evaluator = evaluators.get(0);
        assertEquals(Evaluator.Type.PSEUDO, evaluator.getType());
        assertEquals(Evaluator.IsFirstChild.class, evaluator.getClass());

        String query2 = ":root";
        QueryParser parser2 = QueryParser.parse(query2);
        List<Evaluator> evaluators2 = parser2.parse();

        assertNotNull(evaluators2);
        assertEquals(1, evaluators2.size());

        Evaluator evaluator2 = evaluators2.get(0);
        assertEquals(Evaluator.Type.PSEUDO, evaluator2.getType());
        assertEquals(Evaluator.IsRoot.class, evaluator2.getClass());
    }

    @Test
    public void testParseAttributeSelectors() {
        String query = "input[type='text']";
        QueryParser parser = QueryParser.parse(query);
        List<Evaluator> evaluators = parser.parse();

        assertNotNull(evaluators);
        assertEquals(1, evaluators.size());

        Evaluator evaluator = evaluators.get(0);
        assertEquals(Evaluator.Type.ATTRIBUTE, evaluator.getType());
        assertEquals(Evaluator.Attribute.class, evaluator.getClass());
        assertEquals("input", evaluator.getCssQuerySelectorElement().tagName());
        assertEquals("type", evaluator.getAttributeName());
        assertEquals(Evaluator.AttributeWithValue.class, evaluator.getClass());
        assertEquals("text", evaluator.getAttributeValue());
    }

    @Test
    public void testParseClassSelectors() {
        String query = ".class1.class2";
        QueryParser parser = QueryParser.parse(query);
        List<Evaluator> evaluators = parser.parse();

        assertNotNull(evaluators);
        assertEquals(1, evaluators.size());

        Evaluator evaluator = evaluators.get(0);
        assertEquals(Evaluator.Type.CLASS, evaluator.getType());
        assertEquals(Evaluator.Class.class, evaluator.getClass());
        assertEquals(Arrays.asList("class1", "class2"), evaluator.getClassNames());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "div#main > p:foo";
        QueryParser parser = QueryParser.parse(query);
        List<Evaluator> evaluators = parser.parse();
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidCombinator() {
        String query = "div#main > p:bar";
        QueryParser parser = QueryParser.parse(query);
        List<Evaluator> evaluators = parser.parse();
    }
}