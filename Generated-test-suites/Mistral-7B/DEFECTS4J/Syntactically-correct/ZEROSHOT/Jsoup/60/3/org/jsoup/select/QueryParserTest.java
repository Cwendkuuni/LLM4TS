package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;

/**
 * Tests for QueryParser class.
 */
public class QueryParserTest {

    @Test
    public void testParseSimpleSelectors() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.TAG, evaluator.getType());
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseClassSelectors() {
        String query = ".myClass";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.CLASS, evaluator.getType());
        assertEquals(".myClass", evaluator.toString());
    }

    @Test
    public void testParseIdSelectors() {
        String query = "#myId";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ID, evaluator.getType());
        assertEquals("#myId", evaluator.toString());
    }

    @Test
    public void testParseMultiSelectors() {
        String query = "div.myClass#myId";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.COMPOUND, evaluator.getType());
        assertEquals("div.myClass#myId", evaluator.toString());
    }

    @Test
    public void testParseDescendantSelectors() {
        String query = "div ul li";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.DESCENDANT, evaluator.getType());
        assertEquals("div > ul > li", evaluator.toString());
    }

    @Test
    public void testParseChildSelectors() {
        String query = "div > ul > li";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.CHILD, evaluator.getType());
        assertEquals("div > ul > li", evaluator.toString());
    }

    @Test
    public void testParseAdjacentSiblingSelectors() {
        String query = "div + ul > li";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ADJACENT_SIBLING, evaluator.getType());
        assertEquals("div + ul > li", evaluator.toString());
    }

    @Test
    public void testParseGeneralSiblingSelectors() {
        String query = "div ~ ul > li";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.GENERAL_SIBLING, evaluator.getType());
        assertEquals("div ~ ul > li", evaluator.toString());
    }

    @Test
    public void testParseAttributeSelectors() {
        String query = "input[type=text]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ATTRIBUTE, evaluator.getType());
        assertEquals("input[type='text']", evaluator.toString());
    }

    @Test
    public void testParseAttributeSelectorsWithValues() {
        String query = "input[type='text'][name='username']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ATTRIBUTE_WITH_VALUE, evaluator.getType());
        assertEquals("input[type='text'][name='username']", evaluator.toString());
    }

    @Test
    public void testParseAttributeSelectorsWithMultipleValues() {
        String query = "input[type='text'][name='username' or name='password']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.OR, evaluator.getType());
        List<Evaluator> evaluators = (List<Evaluator>) evaluator;
        assertEquals(2, evaluators.size());
        Evaluator firstEvaluator = evaluators.get(0);
        assertEquals(Evaluator.Type.ATTRIBUTE_WITH_VALUE, firstEvaluator.getType());
        Evaluator secondEvaluator = evaluators.get(1);
        assertEquals(Evaluator.Type.ATTRIBUTE_WITH_VALUE, secondEvaluator.getType());
        assertEquals("input[type='text'][name='username']", evaluators.get(0).toString());
        assertEquals("input[type='text'][name='password']", evaluators.get(1).toString());
    }

    @Test
    public void testParsePseudoClassSelectors() {
        String query = "input:disabled";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.PSEUDO_CLASS, evaluator.getType());
        assertEquals("input:disabled", evaluator.toString());
    }

    @Test
    public void testParsePseudoElementSelectors() {
        String query = "input::-ms-clear";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.PSEUDO_ELEMENT, evaluator.getType());
        assertEquals("input::-ms-clear", evaluator.toString());
    }

    @Test
    public void testParseNegatedSelectors() {
        String query = "input:not([type='text'])";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.NOT, evaluator.getType());
        Evaluator subEvaluator = ((StructuralEvaluator.Not) evaluator).getSubEvaluator();
        assertEquals(Evaluator.Type.ATTRIBUTE_WITH_VALUE, subEvaluator.getType());
        assertEquals("input:not([type='text'])", evaluator.toString());
    }

    @Test
    public void testParseNthChildSelectors() {
        String query = "li:nth-child(2)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.NTH_CHILD, evaluator.getType());
        assertEquals("li:nth-child(2)", evaluator.toString());
    }

    @Test
    public void testParseNthLastChildSelectors() {
        String query = "li:nth-last-child(2)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.NTH_LAST_CHILD, evaluator.getType());
        assertEquals("li:nth-last-child(2)", evaluator.toString());
    }

    @Test
    public void testParseNthOfTypeSelectors() {
        String query = "li:nth-of-type(2)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.NTH_OF_TYPE, evaluator.getType());
        assertEquals("li:nth-of-type(2)", evaluator.toString());
    }

    @Test
    public void testParseNthLastOfTypeSelectors() {
        String query = "li:nth-last-of-type(2)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.NTH_LAST_OF_TYPE, evaluator.getType());
        assertEquals("li:nth-last-of-type(2)", evaluator.toString());
    }

    @Test
    public void testParseHasSelectors() {
        String query = "li:has(.selected)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.HAS, evaluator.getType());
        assertEquals("li:has(.selected)", evaluator.toString());
    }

    @Test
    public void testParseContainsSelectors() {
        String query = "li:contains(test)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.CONTAINS_TEXT, evaluator.getType());
        assertEquals("li:contains(test)", evaluator.toString());
    }

    @Test
    public void testParseContainsOwnSelectors() {
        String query = "li:containsOwn(test)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.CONTAINS_OWN_TEXT, evaluator.getType());
        assertEquals("li:containsOwn(test)", evaluator.toString());
    }

    @Test
    public void testParseMatchesSelectors() {
        String query = "li:matches(a:b)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.MATCHES, evaluator.getType());
        assertEquals("li:matches(a:b)", evaluator.toString());
    }

    @Test
    public void testParseMatchesOwnSelectors() {
        String query = "li:matchesOwn(a:b)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.MATCHES_OWN, evaluator.getType());
        assertEquals("li:matchesOwn(a:b)", evaluator.toString());
    }

    @Test
    public void testParseNotSelectors() {
        String query = "li:not(.selected)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.NOT, evaluator.getType());
        Evaluator subEvaluator = ((StructuralEvaluator.Not) evaluator).getSubEvaluator();
        assertEquals(Evaluator.Type.CLASS, subEvaluator.getType());
        assertEquals("li:not(.selected)", evaluator.toString());
    }

}