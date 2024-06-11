package org.junit.junit4;

import static org.junit.Assert.*;

import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator.isEmpty());
    }

    @Test
    public void testParseTagQuery() {
        QueryParser parser = new QueryParser("div");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Tag.class, evaluator.getClass());
        assertEquals("div", evaluator.tagName);
    }

    @Test
    public void testParseIdQuery() {
        QueryParser parser = new QueryParser("#myId");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Id.class, evaluator.getClass());
        assertEquals("myId", evaluator.attributeValue);
    }

    @Test
    public void testParseClassQuery() {
        QueryParser parser = new QueryParser(".myClass");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Class.class, evaluator.getClass());
        assertEquals("myClass", evaluator.attributeValue);
    }

    @Test
    public void testParseCombinedQuery() {
        QueryParser parser = new QueryParser("div.myClass");
        Evaluator evaluator = parser.parse();

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("div"));
        evaluators.add(new Evaluator.Class("myClass"));

        Evaluator expectedEvaluator = new Evaluator.And(evaluators);

        assertEquals(expectedEvaluator.getClass(), evaluator.getClass());
        assertEquals(expectedEvaluator.toString(), evaluator.toString());
    }

    @Test
    public void testParseNthChildQuery() {
        QueryParser parser = new QueryParser("div:nth-child(2)");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.IsNthChild.class, evaluator.getClass());
        assertEquals(2, evaluator.n);
        assertEquals(0, evaluator.fromType);
    }

    @Test
    public void testParseNthOfTypeQuery() {
        QueryParser parser = new QueryParser("div:nth-of-type(2)");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.IsNthOfType.class, evaluator.getClass());
        assertEquals(2, evaluator.n);
        assertEquals(false, evaluator.isBackwards);
    }

    @Test
    public void testParseNthLastChildQuery() {
        QueryParser parser = new QueryParser("div:nth-last-child(2)");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.IsNthLastChild.class, evaluator.getClass());
        assertEquals(2, evaluator.n);
        assertEquals(false, evaluator.isBackwards);
    }

    @Test
    public void testParseNthLastOfTypeQuery() {
        QueryParser parser = new QueryParser("div:nth-last-of-type(2)");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.IsNthLastOfType.class, evaluator.getClass());
        assertEquals(2, evaluator.n);
        assertEquals(true, evaluator.isBackwards);
    }

    @Test
    public void testParseAttributeQuery() {
        QueryParser parser = new QueryParser("input[type='text']");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Attribute.class, evaluator.getClass());
        assertEquals("type", evaluator.attributeName);
        assertEquals("text", evaluator.attributeValue);
    }

    @Test
    public void testParseAttributeWithValueQuery() {
        QueryParser parser = new QueryParser("input[type='text'][value='Search']");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.AttributeWithValue.class, evaluator.getClass());
        assertEquals("type", evaluator.attributeName);
        assertEquals("text", evaluator.attributeValue);
        assertEquals("Search", evaluator.value);
    }

    @Test
    public void testParseAttributeWithValueContainingQuery() {
        QueryParser parser = new QueryParser("input[type='text'][value*='Search']");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.AttributeWithValueContaining.class, evaluator.getClass());
        assertEquals("type", evaluator.attributeName);
        assertEquals("text", evaluator.attributeValue);
        assertEquals("Search.*", evaluator.regexPattern.pattern());
    }

    @Test
    public void testParseAttributeWithValueMatchingQuery() {
        QueryParser parser = new QueryParser("input[type='text']:matches(.*Search.*)");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Matches.class, evaluator.getClass());
        assertEquals("Search.*", evaluator.regexPattern.pattern());
    }

    @Test
    public void testParseAttributeWithValueStartingQuery() {
        QueryParser parser = new QueryParser("input[type='text'][value^='Search']");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.AttributeWithValueStarting.class, evaluator.getClass());
        assertEquals("type", evaluator.attributeName);
        assertEquals("text", evaluator.attributeValue);
        assertEquals("Search", evaluator.value);
    }

    @Test
    public void testParseAttributeWithValueEndingQuery() {
        QueryParser parser = new QueryParser("input[type='text'][value$='Search']");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.AttributeWithValueEnding.class, evaluator.getClass());
        assertEquals("type", evaluator.attributeName);
        assertEquals("text", evaluator.attributeValue);
        assertEquals("Search", evaluator.value);
    }

    @Test
    public void testParseAttributeWithValueNotQuery() {
        QueryParser parser = new QueryParser("input:not([type='text'])");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Not.class, evaluator.getClass());
        assertEquals(Evaluator.Tag.class, evaluator.evaluator.getClass());
        assertEquals("input", evaluator.evaluator.tagName);
        assertEquals("type", evaluator.attributeName);
        assertEquals("text", evaluator.attributeValue);
    }

    @Test
    public void testParseAttributeStartingQuery() {
        QueryParser parser = new QueryParser("input[data-search^='name']");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.AttributeStarting.class, evaluator.getClass());
        assertEquals("data-search", evaluator.attributeName);
        assertEquals("name", evaluator.value);
    }

    @Test
    public void testParseAttributeEndingQuery() {
        QueryParser parser = new QueryParser("input[data-search$='name']");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.AttributeEnding.class, evaluator.getClass());
        assertEquals("data-search", evaluator.attributeName);
        assertEquals("name", evaluator.value);
    }

    @Test
    public void testParseAttributeContainsQuery() {
        QueryParser parser = new QueryParser("input[data-search*='name']");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.AttributeWithValueContaining.class, evaluator.getClass());
        assertEquals("data-search", evaluator.attributeName);
        assertEquals("name", evaluator.value);
    }

    @Test
    public void testParseAttributeMatchesQuery() {
        QueryParser parser = new QueryParser("input:matches(.*name.*)");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Matches.class, evaluator.getClass());
        assertEquals("input", evaluator.tagName);
        assertEquals("(.*name.*)", evaluator.regexPattern.pattern());
    }

    @Test
    public void testParseAttributeMatchesOwnQuery() {
        QueryParser parser = new QueryParser("input:matchesOwn(.*name.*)");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.MatchesOwn.class, evaluator.getClass());
        assertEquals("input", evaluator.tagName);
        assertEquals("(.*name.*)", evaluator.regexPattern.pattern());
    }

    @Test
    public void testParseHasQuery() {
        QueryParser parser = new QueryParser("div:has(p)");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Has.class, evaluator.getClass());
        assertEquals(Evaluator.Tag.class, evaluator.evaluator.getClass());
        assertEquals("div", evaluator.evaluator.tagName);
        assertEquals(Evaluator.Tag.class, evaluator.childEvaluator.getClass());
        assertEquals("p", evaluator.childEvaluator.tagName);
    }

    @Test
    public void testParseContainsTextQuery() {
        QueryParser parser = new QueryParser("div:contains(text)");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.ContainsText.class, evaluator.getClass());
        assertEquals("text", evaluator.value);
    }

    @Test
    public void testParseContainsOwnTextQuery() {
        QueryParser parser = new QueryParser("div:containsOwn(text)");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.ContainsOwnText.class, evaluator.getClass());
        assertEquals("text", evaluator.value);
    }

    @Test
    public void testParseContainsDataQuery() {
        QueryParser parser = new QueryParser("div:containsData(data)");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.ContainsData.class, evaluator.getClass());
        assertEquals("data", evaluator.value);
    }

    @Test
    public void testParseNotQuery() {
        QueryParser parser = new QueryParser("div:not(p)");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Not.class, evaluator.getClass());
        assertEquals(Evaluator.Tag.class, evaluator.evaluator.getClass());
        assertEquals("div", evaluator.evaluator.tagName);
        assertEquals("p", evaluator.attributeValue);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        QueryParser parser = new QueryParser("div:not[type='text']");
        parser.parse();
        fail("Expected SelectorParseException");
    }
}