package org.jsoup.select.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;

import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("Empty query should return empty evaluator", 0, evaluator.size());
    }

    @Test
    public void testParseSimpleTagSelector() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("Tag selector 'div' should return Tag evaluator", Evaluator.Tag.class, evaluator.getClass());
    }

    @Test
    public void testParseIDSelector() {
        String query = "#myId";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("ID selector '#myId' should return Id evaluator", Evaluator.Id.class, evaluator.getClass());
    }

    @Test
    public void testParseClassSelector() {
        String query = ".myClass";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("Class selector '.myClass' should return Class evaluator", Evaluator.Class.class, evaluator.getClass());
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "[href]";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("Attribute selector '[href]' should return Attribute evaluator", Evaluator.Attribute.class, evaluator.getClass());
    }

    @Test
    public void testParseComplexQuery() {
        String query = "#myId > div.myClass:first-child";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("Complex query '#myId > div.myClass:first-child' should return CombiningEvaluator.And evaluator", CombiningEvaluator.And.class, evaluator.getClass());
        Assert.assertEquals("And evaluator should have one left child - Id evaluator", Evaluator.Id.class, evaluator.getLeftChild().getClass());
        Assert.assertEquals("And evaluator should have one right child - CombiningEvaluator.And evaluator", CombiningEvaluator.And.class, evaluator.getRightChild().getClass());
        Assert.assertEquals("Left 'And' evaluator should have one child - StructuralEvaluator.ImmediateParent evaluator", StructuralEvaluator.ImmediateParent.class, evaluator.getLeftChild().getLeftChild().getClass());
        Assert.assertEquals("Right 'And' evaluator should have one child - Evaluator.Class evaluator", Evaluator.Class.class, evaluator.getRightChild().getLeftChild().getClass());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "div#myId > .myClass:foo";
        QueryParser.parse(query);
    }

    @Test
    public void testParseNthChildSelector() {
        String query = "li:nth-child(3)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("Nth child selector 'li:nth-child(3)' should return Evaluator.IsNthChild evaluator", Evaluator.IsNthChild.class, evaluator.getClass());
        Assert.assertEquals("IsNthChild evaluator should have nthChild value 3", 3, evaluator.getIntValue());
    }

    @Test
    public void testParseNthOfTypeSelector() {
        String query = "li:nth-of-type(3)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("Nth of type selector 'li:nth-of-type(3)' should return Evaluator.IsNthOfType evaluator", Evaluator.IsNthOfType.class, evaluator.getClass());
        Assert.assertEquals("IsNthOfType evaluator should have nthOfType value 3", 3, evaluator.getIntValue());
    }

    @Test
    public void testParseNthLastChildSelector() {
        String query = "li:nth-last-child(3)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("Nth last child selector 'li:nth-last-child(3)' should return Evaluator.IsNthLastChild evaluator", Evaluator.IsNthLastChild.class, evaluator.getClass());
        Assert.assertEquals("IsNthLastChild evaluator should have nthLastChild value 3", 3, evaluator.getIntValue());
    }

    @Test
    public void testParseNthLastOfTypeSelector() {
        String query = "li:nth-last-of-type(3)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("Nth last of type selector 'li:nth-last-of-type(3)' should return Evaluator.IsNthLastOfType evaluator", Evaluator.IsNthLastOfType.class, evaluator.getClass());
        Assert.assertEquals("IsNthLastOfType evaluator should have nthLastOfType value 3", 3, evaluator.getIntValue());
    }

    @Test
    public void testParseCombiningSelectors() {
        String query = "div > p, div.myClass > ul > li:nth-child(2)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("Combining selectors 'div > p, div.myClass > ul > li:nth-child(2)' should return CombiningEvaluator.Or evaluator", CombiningEvaluator.Or.class, evaluator.getClass());
        Assert.assertEquals("Left Or evaluator should have one child - StructuralEvaluator.ImmediateChild evaluator", StructuralEvaluator.ImmediateChild.class, evaluator.getLeftChild().getClass());
        Assert.assertEquals("Right Or evaluator should have one child - CombiningEvaluator.And evaluator", CombiningEvaluator.And.class, evaluator.getRightChild().getClass());
        Assert.assertEquals("Left 'And' evaluator should have one left child - Tag evaluator for 'p'", Evaluator.Tag.class, evaluator.getLeftChild().getLeftChild().getClass());
        Assert.assertEquals("Right 'And' evaluator should have one child - StructuralEvaluator.Has evaluator", StructuralEvaluator.Has.class, evaluator.getRightChild().getLeftChild().getClass());
    }

    @Test
    public void testParseAttributeValueSelector() {
        String query = "a[href='https://example.com']";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("Attribute value selector 'a[href='https://example.com']' should return Evaluator.AttributeWithValue evaluator", Evaluator.AttributeWithValue.class, evaluator.getClass());
        Assert.assertEquals("AttributeWithValue evaluator should have attributeName 'href'", "href", evaluator.getStringValue());
        Assert.assertEquals("AttributeWithValue evaluator should have attributeValue 'https://example.com'", "https://example.com", evaluator.getStringValue());
    }

    @Test
    public void testParseAttributeStartsWithSelector() {
        String query = "a[href^='https']";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("Attribute starts with selector 'a[href^='https']' should return Evaluator.AttributeWithValueStarting evaluator", Evaluator.AttributeWithValueStarting.class, evaluator.getClass());
        Assert.assertEquals("AttributeWithValueStarting evaluator should have attributeName 'href'", "href", evaluator.getStringValue());
        Assert.assertEquals("AttributeWithValueStarting evaluator should have attributeValue 'https'", "https", evaluator.getStringValue());
    }

    @Test
    public void testParseAttributeEndsWithSelector() {
        String query = "a[href$='.pdf']";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("Attribute ends with selector 'a[href$='.pdf']' should return Evaluator.AttributeWithValueEnding evaluator", Evaluator.AttributeWithValueEnding.class, evaluator.getClass());
        Assert.assertEquals("AttributeWithValueEnding evaluator should have attributeName 'href'", "href", evaluator.getStringValue());
        Assert.assertEquals("AttributeWithValueEnding evaluator should have attributeValue '.pdf'", ".pdf", evaluator.getStringValue());
    }

    @Test
    public void testParseAttributeContainsSelector() {
        String query = "a[href*='google']";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("Attribute contains selector 'a[href*='google']' should return Evaluator.AttributeWithValueContaining evaluator", Evaluator.AttributeWithValueContaining.class, evaluator.getClass());
        Assert.assertEquals("AttributeWithValueContaining evaluator should have attributeName 'href'", "href", evaluator.getStringValue());
        Assert.assertEquals("AttributeWithValueContaining evaluator should have attributeValue 'google'", "google", evaluator.getStringValue());
    }

    @Test
    public void testParseAttributeMatchingSelector() {
        String query = "a:matches(^google.*$|^yahoo.*$)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("Attribute matching selector 'a:matches(^google.*$|^yahoo.*$)' should return Evaluator.MatchesOwn evaluator", Evaluator.MatchesOwn.class, evaluator.getClass());
        Assert.assertTrue("MatchesOwn evaluator should match 'google.com'", evaluator.matches("google.com"));
        Assert.assertTrue("MatchesOwn evaluator should match 'yahoo.com'", evaluator.matches("yahoo.com"));
        Assert.assertFalse("MatchesOwn evaluator should not match 'bing.com'", evaluator.matches("bing.com"));
    }
}