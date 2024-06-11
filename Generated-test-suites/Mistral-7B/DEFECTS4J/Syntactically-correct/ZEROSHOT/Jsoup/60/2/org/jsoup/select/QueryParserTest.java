package org.jsoup.select;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.jsoup.parser.TokenQueue;

import org.junit.Test;

/**
 * Tests for QueryParser class.
 */
public class QueryParserTest {

    @Test
    public void testParsingSimpleSelectors() {
        String query = "div#foo.bar";
        Evaluator evaluator = QueryParser.parse(query);

        List<Evaluator> expected = Arrays.asList(
                new Evaluator.Id("foo"),
                new Evaluator.Class("bar")
        );

        assertEquals(expected.size(), evaluator.size());
        assertTrue(evaluator.evaluate(new Element("div", null, null, null, null)).isMatched());
    }

    @Test
    public void testParsingComplexSelectors() {
        String query = "div#foo > ul > li:nth-child(2) > a[href^='http']";
        Evaluator evaluator = QueryParser.parse(query);

        List<Evaluator> expected = Arrays.asList(
                new Evaluator.Id("foo"),
                new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("ul")),
                new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("li")),
                new StructuralEvaluator.Index(1),
                new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("a")),
                new Evaluator.AttributeWithValueStarting("href", "http")
        );

        assertEquals(expected.size(), evaluator.size());
        assertTrue(evaluator.evaluate(new Element("div", null, null, null, null)).isMatched());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParsingInvalidSelectors() {
        String query = "div#foo > ul > li:nth-child(-2) > a[href^='http']";
        QueryParser.parse(query);
    }

    @Test
    public void testParsingPseudoSelectors() {
        String query = "div:empty";
        Evaluator evaluator = QueryParser.parse(query);

        List<Evaluator> expected = Arrays.asList(new Evaluator.IsEmpty());

        assertEquals(expected.size(), evaluator.size());
        assertTrue(evaluator.evaluate(new Element("div", null, null, null, null)).isMatched());
    }
}