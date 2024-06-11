package org.jsoup.select.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;

import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Selector;
import org.jsoup.parser.Token;

import org.junit.Test;

/**
 * Test cases for QueryParser class.
 */
public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        Evaluator parser = QueryParser.parse(query);
        assertNull(parser);
    }

    @Test
    public void testParseSingleTagSelector() {
        String query = "p";
        Evaluator parser = QueryParser.parse(query);
        Evaluator expected = new Evaluator.Tag("p");
        assertEquals(expected, parser);
    }

    @Test
    public void testParseMultiTagSelector() {
        String query = "p, div, ul";
        Evaluator parser = QueryParser.parse(query);
        Evaluator expected = new CombiningEvaluator.Or(
                new Evaluator.Tag("p"),
                new Evaluator.Tag("div"),
                new Evaluator.Tag("ul")
        );
        assertEquals(expected, parser);
    }

    @Test
    public void testParseElementWithID() {
        String query = "#elementID";
        Evaluator parser = QueryParser.parse(query);
        Evaluator expected = new Evaluator.Id("elementID");
        assertEquals(expected, parser);
    }

    @Test
    public void testParseElementWithClass() {
        String query = ".className";
        Evaluator parser = QueryParser.parse(query);
        Evaluator expected = new Evaluator.Class("className");
        assertEquals(expected, parser);
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "[att='val']";
        Evaluator parser = QueryParser.parse(query);
        Evaluator expected = new Evaluator.AttributeWithValue("att", "val");
        assertEquals(expected, parser);
    }

    @Test
    public void testParseCombinatorSelector() {
        String query = "p > div > ul";
        Evaluator parser = QueryParser.parse(query);
        Evaluator expected = new CombiningEvaluator.And(
                new Evaluator.Tag("p"),
                new StructuralEvaluator.ImmediateChildOf(new Evaluator.Tag("div")),
                new Evaluator.Tag("ul")
        );
        assertEquals(expected, parser);
    }

    @Test
    public void testParseDescendantSelector() {
        String query = "p ~ div";
        Evaluator parser = QueryParser.parse(query);
        Evaluator expected = new StructuralEvaluator.PreviousSibling(new Evaluator.Tag("p"));
        assertEquals(expected, parser);
    }

    @Test
    public void testParseSelectorWithMultipleCombinators() {
        String query = "p > div > ul ~ li";
        Evaluator parser = QueryParser.parse(query);
        Evaluator expected = new CombiningEvaluator.And(
                new Evaluator.Tag("p"),
                new StructuralEvaluator.ImmediateChildOf(new Evaluator.Tag("div")),
                new Evaluator.Tag("ul"),
                new StructuralEvaluator.PreviousSibling(new Evaluator.Tag("li"))
        );
        assertEquals(expected, parser);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseUnknownCombinator() {
        String query = "p @ div"; // unknown combinator "@"
        Evaluator parser = QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseMalformedQuery() {
        String query = "p > div ul"; // missing space between "div" and "ul"
        Evaluator parser = QueryParser.parse(query);
    }

    @Test
    public void testParseComplexSelector() {
        String query = "input:not([disabled]):enabled:first-child:focus";
        Evaluator parser = QueryParser.parse(query);
        Evaluator expected = new CombiningEvaluator.And(
                new StructuralEvaluator.Not(new Evaluator.AttributeWithValue("disabled", "")),
                new Evaluator.Tag("input"),
                new Evaluator.IsFirstChild(),
                new Evaluator.HasFocus()
        );
        assertEquals(expected, parser);
    }
}