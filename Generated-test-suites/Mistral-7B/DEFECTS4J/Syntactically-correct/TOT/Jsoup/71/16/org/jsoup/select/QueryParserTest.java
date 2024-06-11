package org.jsoup.select.test;

import org.junit.Test;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Test class for QueryParser.
 */
public class QueryParserTest {

    /**
     * Test parsing a simple CSS query into an Evaluator.
     */
    @Test
    public void testParseSimpleQuery() {
        String query = "div.class1";
        Evaluator evaluator = QueryParser.parse(query);
        assertThat(evaluator, instanceOf(QueryParser.StructuralEvaluator.Class.class));
        assertThat(evaluator.toString(), is("Class(class1)"));
    }

    /**
     * Test parsing a query with multiple selectors.
     */
    @Test
    public void testParseMultipleSelectorsQuery() {
        String query = "div.class1 p.class2";
        Evaluator evaluator = QueryParser.parse(query);
        assertThat(evaluator, instanceOf(QueryParser.CombiningEvaluator.And.class));
        assertEquals(evaluator.toString(), "And(Class(class1), Class(class2))");
    }

    /**
     * Test parsing a query with combinator selectors.
     */
    @Test
    public void testParseCombinatorQuery() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        assertThat(evaluator, instanceOf(QueryParser.CombiningEvaluator.And.class));
        assertEquals(evaluator.toString(), "And(StructuralEvaluator.ImmediateParent(Tag(div)), Tag(p))");
    }

    /**
     * Test parsing a query with attribute selectors.
     */
    @Test
    public void testParseAttributeQuery() {
        String query = "input[type=text]";
        Evaluator evaluator = QueryParser.parse(query);
        assertThat(evaluator, instanceOf(QueryParser.Evaluator.Attribute.class));
        assertEquals(evaluator.toString(), "Attribute(type, text)");
    }

    /**
     * Test parsing a query with pseudo-classes.
     */
    @Test
    public void testParsePseudoClassQuery() {
        String query = "li:last-child";
        Evaluator evaluator = QueryParser.parse(query);
        assertThat(evaluator, instanceOf(QueryParser.Evaluator.IsLastOfType.class));
        assertEquals(evaluator.toString(), "IsLastOfType(li, 0)");
    }

    /**
     * Test parsing a query with a non-existent pseudo-class.
     */
    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidPseudoClassQuery() {
        String query = "li:invalid";
        QueryParser.parse(query);
    }

    /**
     * Test parsing a query with a query string that is empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyQuery() {
        String query = "";
        QueryParser.parse(query);
    }

    /**
     * Test parsing a query with an invalid query string.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidQuery() {
        String query = "abcd";
        QueryParser.parse(query);
    }

    /**
     * Test parsing a complex query with multiple selectors, combinators, and pseudo-classes.
     */
    @Test
    public void testParseComplexQuery() {
        String query = "div.class1 > p:first-child:not(.class2)";
        Evaluator evaluator = QueryParser.parse(query);
        assertThat(evaluator, instanceOf(QueryParser.CombiningEvaluator.Not.class));
        assertEquals(evaluator.toString(), "Not(And(StructuralEvaluator.ImmediateParent(Tag(div)), Tag(p)), Class(class2))");
    }
}