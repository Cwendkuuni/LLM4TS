package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse_emptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        assertNull(evaluator);
    }

    @Test
    public void testParse_simpleSelector() {
        String query = "#myId";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("#myId", evaluator.toString());
    }

    @Test
    public void testParse_classSelector() {
        String query = ".myClass";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals(".myClass", evaluator.toString());
    }

    @Test
    public void testParse_tagSelector() {
        String query = "div";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParse_tagWithNamespaceSelector() {
        String query = "html div";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("html|div", evaluator.toString());
    }

    @Test
    public void testParse_idWithClassSelector() {
        String query = "#myId.myClass";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        List<Evaluator> andChildren = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(2, andChildren.size());
        Evaluator firstChild = andChildren.get(0);
        assertTrue(firstChild instanceof Evaluator.Id);
        assertEquals("#myId", firstChild.toString());
        Evaluator secondChild = andChildren.get(1);
        assertTrue(secondChild instanceof Evaluator.Class);
        assertEquals(".myClass", secondChild.toString());
    }

    @Test
    public void testParse_combinator_descendant() {
        String query = "div > p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        List<Evaluator> andChildren = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(2, andChildren.size());
        Evaluator firstChild = andChildren.get(0);
        assertTrue(firstChild instanceof Evaluator.Tag);
        assertEquals("div", firstChild.toString());
        Evaluator secondChild = andChildren.get(1);
        assertTrue(secondChild instanceof StructuralEvaluator.ImmediateChild);
        assertEquals("p", ((StructuralEvaluator.ImmediateChild) secondChild).getElementSelector());
    }

    @Test
    public void testParse_combinator_adjacent() {
        String query = "div + p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        List<Evaluator> andChildren = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(2, andChildren.size());
        Evaluator firstChild = andChildren.get(0);
        assertTrue(firstChild instanceof Evaluator.Tag);
        assertEquals("div", firstChild.toString());
        Evaluator secondChild = andChildren.get(1);
        assertTrue(secondChild instanceof StructuralEvaluator.PreviousSibling);
        assertEquals("p", ((StructuralEvaluator.PreviousSibling) secondChild).getElementSelector());
    }

    @Test
    public void testParse_combinator_generalSibling() {
        String query = "div ~ p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        List<Evaluator> andChildren = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(2, andChildren.size());
        Evaluator firstChild = andChildren.get(0);
        assertTrue(firstChild instanceof Evaluator.Tag);
        assertEquals("div", firstChild.toString());
        Evaluator secondChild = andChildren.get(1);
        assertTrue(secondChild instanceof StructuralEvaluator.PreviousSibling);
        assertEquals("p", ((StructuralEvaluator.PreviousSibling) secondChild).getElementSelector());
    }

    @Test
    public void testParse_combinator_OR() {
        String query = "div, p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.Or);
        List<Evaluator> orChildren = ((CombiningEvaluator.Or) evaluator).getChildren();
        assertEquals(2, orChildren.size());
        Evaluator firstChild = orChildren.get(0);
        assertTrue(firstChild instanceof Evaluator.Tag);
        assertEquals("div", firstChild.toString());
        Evaluator secondChild = orChildren.get(1);
        assertTrue(secondChild instanceof Evaluator.Tag);
        assertEquals("p", secondChild.toString());
    }

    @Test
    public void testParse_attributeSelector() {
        String query = "div[class='myClass']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        List<Evaluator> andChildren = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(2, andChildren.size());
        Evaluator firstChild = andChildren.get(0);
        assertTrue(firstChild instanceof Evaluator.Tag);
        assertEquals("div", firstChild.toString());
        Evaluator secondChild = andChildren.get(1);
        assertTrue(secondChild instanceof Evaluator.Attribute);
        assertEquals("class", secondChild.getAttributeName());
        assertEquals("myClass", secondChild.getAttributeValue());
    }

    @Test
    public void testParse_attributeSelector_startsWith() {
        String query = "div[class^='my']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeStarting);
        assertEquals("class", evaluator.getAttributeName());
        assertEquals("my", evaluator.getAttributeValueStartsWith());
    }

    @Test
    public void testParse_attributeSelector_endsWith() {
        String query = "div[class$='foo']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeEnding);
        assertEquals("class", evaluator.getAttributeName());
        assertEquals("foo", evaluator.getAttributeValueEndsWith());
    }

    @Test
    public void testParse_attributeSelector_contains() {
        String query = "div[class*='foo']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeContaining);
        assertEquals("class", evaluator.getAttributeName());
        assertEquals("foo", evaluator.getAttributeValueContains());
    }

    @Test
    public void testParse_attributeSelector_match() {
        String query = "div[title~='foo']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
        assertEquals("title", evaluator.getAttributeName());
        assertEquals(Pattern.compile("foo"), evaluator.getAttributeValueRegex());
    }

    @Test
    public void testParse_pseudoClassSelector_firstChild() {
        String query = "div > :first-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IsFirstChild);
    }

    @Test
    public void testParse_pseudoClassSelector_lastChild() {
        String query = "div > :last-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IsLastChild);
    }

    @Test
    public void testParse_pseudoClassSelector_onlyChild() {
        String query = "div:only-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IsOnlyChild);
    }

    @Test
    public void testParse_pseudoClassSelector_onlyOfType() {
        String query = "div:only-of-type";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IsOnlyOfType);
    }

    @Test
    public void testParse_pseudoClassSelector_empty() {
        String query = "div:empty";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IsEmpty);
    }

    @Test
    public void testParse_pseudoClassSelector_root() {
        String query = ":root";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IsRoot);
    }

    @Test
    public void testParse_pseudoClassSelector_nthChild() {
        String query = "div:nth-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IsNthChild);
        assertEquals(2, ((Evaluator.IsNthChild) evaluator).getIndex());
    }

    @Test
    public void testParse_pseudoClassSelector_nthLastChild() {
        String query = "div:nth-last-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IsNthLastChild);
        assertEquals(2, ((Evaluator.IsNthLastChild) evaluator).getIndex());
    }

    @Test
    public void testParse_pseudoClassSelector_nthOfType() {
        String query = "div:nth-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IsNthOfType);
        assertEquals(2, ((Evaluator.IsNthOfType) evaluator).getIndex());
    }

    @Test
    public void testParse_pseudoClassSelector_nthLastOfType() {
        String query = "div:nth-last-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IsNthLastOfType);
        assertEquals(2, ((Evaluator.IsNthLastOfType) evaluator).getIndex());
    }

    @Test
    public void testParse_pseudoClassSelector_has() {
        String query = "div:has(p)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
        assertEquals(1, ((StructuralEvaluator.Has) evaluator).getChildEvaluator().getChildren().size());
    }

    @Test
    public void testParse_pseudoClassSelector_contains() {
        String query = "div:contains(text)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.ContainsText);
        assertEquals("text", evaluator.toString());
    }

    @Test
    public void testParse_pseudoClassSelector_containsOwn() {
        String query = "div:containsOwn(text)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.ContainsOwnText);
        assertEquals("text", evaluator.toString());
    }

    @Test
    public void testParse_pseudoClassSelector_containsData() {
        String query = "div:containsData(data)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.ContainsData);
        assertEquals("data", evaluator.toString());
    }

    @Test
    public void testParse_pseudoClassSelector_matches() {
        String query = "div:matches(regex)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Matches);
        assertEquals(Pattern.compile(query.substring(query.indexOf("(") + 1, query.length() - 1)), evaluator.getAttributeValueRegex());
    }

    @Test
    public void testParse_pseudoClassSelector_matchesOwn() {
        String query = "div:matchesOwn(regex)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.MatchesOwn);
        assertEquals(Pattern.compile(query.substring(query.indexOf("(") + 1, query.length() - 1)), evaluator.getAttributeValueRegex());
    }

    @Test
    public void testParse_pseudoClassSelector_not() {
        String query = "div:not(p)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
        assertEquals(1, ((StructuralEvaluator.Not) evaluator).getChildEvaluator().getChildren().size());
    }
}