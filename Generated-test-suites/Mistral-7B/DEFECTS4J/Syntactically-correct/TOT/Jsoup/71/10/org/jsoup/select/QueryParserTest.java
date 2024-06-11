package org.jsoup.select.test;

import org.junit.Test;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.select.StructuralEvaluator;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator.IsFirstChild;
import org.jsoup.select.Evaluator.IsLastChild;
import org.jsoup.select.Evaluator.IsFirstOfType;
import org.jsoup.select.Evaluator.IsLastOfType;
import org.jsoup.select.Evaluator.IsOnlyOfType;
import org.jsoup.select.Evaluator.IsOnlyChild;
import org.jsoup.select.Evaluator.IsEmpty;
import org.jsoup.select.Evaluator.IsRoot;
import org.jsoup.select.Evaluator.Tag;
import org.jsoup.select.Evaluator.Class;
import org.jsoup.select.Evaluator.Id;
import org.jsoup.select.Evaluator.Attribute;
import org.jsoup.select.Evaluator.AttributeStarting;
import org.jsoup.select.Evaluator.AttributeWithValue;
import org.jsoup.select.Evaluator.AttributeWithValueStarting;
import org.jsoup.select.Evaluator.AttributeWithValueEnding;
import org.jsoup.select.Evaluator.AttributeWithValueContaining;
import org.jsoup.select.Evaluator.AttributeWithValueMatching;
import org.jsoup.select.Evaluator.IndexLessThan;
import org.jsoup.select.Evaluator.IndexGreaterThan;
import org.jsoup.select.Evaluator.IndexEquals;
import org.jsoup.select.Evaluator.Has;
import org.jsoup.select.Evaluator.ContainsText;
import org.jsoup.select.Evaluator.ContainsOwnText;
import org.jsoup.select.Evaluator.ContainsData;
import org.jsoup.select.Evaluator.Matches;
import org.jsoup.select.Evaluator.MatchesOwn;
import org.jsoup.select.Evaluator.Not;
import org.jsoup.select.StructuralEvaluator.Root;
import org.jsoup.select.StructuralEvaluator.Parent;
import org.jsoup.select.StructuralEvaluator.ImmediateParent;
import org.jsoup.select.StructuralEvaluator.ImmediatePreviousSibling;
import org.jsoup.select.StructuralEvaluator.PreviousSibling;
import org.jsoup.select.StructuralEvaluator.Has;
import org.jsoup.select.StructuralEvaluator.And;
import org.jsoup.select.StructuralEvaluator.Or;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelectors() {
        String query = "div.class1";
        Evaluator eval = QueryParser.parse(query);
        assertEquals("div", eval.getTag().getName());
        assertTrue(eval.getClass().isInstanceOf(Class.class));
        assertEquals("class1", ((Class) eval).getAttributeValue());
    }

    @Test
    public void testParseSimpleSelectorsWithId() {
        String query = "#myId";
        Evaluator eval = QueryParser.parse(query);
        assertEquals("", eval.getTag().getName());
        assertTrue(eval.getId().getValue().equals("myId"));
    }

    @Test
    public void testParseSimpleSelectorsWithTag() {
        String query = "input";
        Evaluator eval = QueryParser.parse(query);
        assertEquals("input", eval.getTag().getName());
    }

    @Test
    public void testParseComplexSelectors() {
        String query = "div.class1> ul > li:first-child";
        Evaluator eval = QueryParser.parse(query);
        StructuralEvaluator rootEval = (StructuralEvaluator) eval;
        assertEquals(Tag.DIV, rootEval.getTag().getName());
        assertTrue(rootEval.getClass().isInstanceOf(StructuralEvaluator.And.class));
        StructuralEvaluator andEval = (StructuralEvaluator) rootEval;
        Evaluator leftEval = andEval.getLeft();
        assertTrue(leftEval.getClass().isInstanceOf(StructuralEvaluator.And.class));
        StructuralEvaluator leftAndEval = (StructuralEvaluator) leftEval;
        Evaluator leftTagEval = leftAndEval.getLeft();
        assertEquals(Tag.DIV, leftTagEval.getTag().getName());
        assertTrue(leftTagEval.getClass().isInstanceOf(StructuralEvaluator.Tag.class));
        Evaluator leftClassEval = leftAndEval.getRight();
        assertTrue(leftClassEval.getClass().isInstanceOf(Class.class));
        assertEquals("class1", leftClassEval.getAttributeValue());
        Evaluator rightEval = leftAndEval.getRight();
        assertTrue(rightEval.getClass().isInstanceOf(StructuralEvaluator.Combinator.class));
        StructuralEvaluator combinatorEval = (StructuralEvaluator) rightEval;
        assertEquals(Combinator.GT, combinatorEval.getCombinator());
        assertTrue(combinatorEval.getLeft().getClass().isInstanceOf(StructuralEvaluator.Tag.class));
        assertEquals(Tag.UL, ((StructuralEvaluator) combinatorEval.getLeft()).getTag().getName());
        Evaluator rightRightEval = combinatorEval.getRight();
        assertTrue(rightRightEval.getClass().isInstanceOf(Index.class));
        Index indexEval = (Index) rightRightEval;
        assertEquals(1, indexEval.getIndex());
        assertEquals(IndexType.FIRST_CHILD, indexEval.getIndexType());
    }

    @Test
    public void testParseComplexSelectorsWithAttribute() {
        String query = "div[title='Test']";
        Evaluator eval = QueryParser.parse(query);
        StructuralEvaluator rootEval = (StructuralEvaluator) eval;
        assertEquals(Tag.DIV, rootEval.getTag().getName());
        assertTrue(rootEval.getClass().isInstanceOf(StructuralEvaluator.And.class));
        StructuralEvaluator andEval = (StructuralEvaluator) rootEval;
        Evaluator leftEval = andEval.getLeft();
        assertTrue(leftEval.getClass().isInstanceOf(StructuralEvaluator.Tag.class));
        assertEquals(Tag.DIV, ((StructuralEvaluator) leftEval).getTag().getName());
        Evaluator rightEval = andEval.getRight();
        assertTrue(rightEval.getClass().isInstanceOf(StructuralEvaluator.Attribute.class));
        Attribute attrEval = (Attribute) rightEval;
        assertEquals("title", attrEval.getAttributeName());
        assertEquals("Test", attrEval.getAttributeValue());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidSelectors() {
        String query = "div[title='Test'<";
        QueryParser.parse(query);
    }

    @Test
    public void testParseInvalidCombinators() {
        String query = "div>+<ul";
        try {
            QueryParser.parse(query);
            fail("Expected Selector.SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Unknown combinator: <", e.getMessage());
        }
    }
}