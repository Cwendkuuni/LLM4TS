package org.jsoup.select;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParserTest {

    @org.junit.Test
    public void testParseSimpleSelector() {
        String query = "div";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertInstanceOf(Evaluator.Tag.class, evaluator);
        Evaluator.Tag tag = (Evaluator.Tag) evaluator;
        assertEquals("div", tag.getTagName());
    }

    @org.junit.Test
    public void testParseComplexSelectorWithId() {
        String query = "#header #menu a";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        CombiningEvaluator and = (CombiningEvaluator) evaluator;
        Evaluator idEvaluator = and.getLeftMostEvaluator();
        assertEquals(Evaluator.Id.class, idEvaluator.getClass());
        Evaluator.Id id = (Evaluator.Id) idEvaluator;
        assertEquals("header", id.getId());

        Evaluator tagEvaluator = and.getRightMostEvaluator();
        Evaluator.Tag tag = (Evaluator.Tag) tagEvaluator;
        assertEquals("a", tag.getTagName());
    }

    @org.junit.Test
    public void testParseComplexSelectorWithClass() {
        String query = ".btn.active";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        CombiningEvaluator and = (CombiningEvaluator) evaluator;
        Evaluator classEvaluator1 = and.getLeftMostEvaluator();
        assertEquals(Evaluator.Class.class, classEvaluator1.getClass());
        Evaluator.Class class1 = (Evaluator.Class) classEvaluator1;
        assertEquals("btn", class1.getCssClass());

        Evaluator classEvaluator2 = and.getRightMostEvaluator();
        Evaluator.Class class2 = (Evaluator.Class) classEvaluator2;
        assertEquals("active", class2.getCssClass());
    }

    @org.junit.Test
    public void testParseComplexSelectorWithAttribute() {
        String query = "input[type='text']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        CombiningEvaluator and = (CombiningEvaluator) evaluator;
        Evaluator attributeEvaluator = and.getLeftMostEvaluator();
        Equals attribute = (Equals) attributeEvaluator;
        assertEquals("input", attribute.getLeftOperand().getClass());
        assertEquals("type", attribute.getRightOperand().getClass().getSimpleName());
        assertEquals("text", ((String) attribute.getRightOperand()).trim());
    }

    @org.junit.Test
    public void testParseComplexSelectorWithCombination() {
        String query = "div > ul > li > a";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        CombiningEvaluator and = (CombiningEvaluator) evaluator;
        Evaluator immediateParentEvaluator = and.getLeftMostEvaluator();
        ImmediateParent immediateParent = (ImmediateParent) immediateParentEvaluator;
        assertEquals("a", immediateParent.getChildEvaluator().getClass().getSimpleName());
        Evaluator parentEvaluator = immediateParent.getParentEvaluator();
        StructuralEvaluator.Parent parent = (StructuralEvaluator.Parent) parentEvaluator;
        assertEquals("li", parent.getChildEvaluator().getClass().getSimpleName());
        Evaluator grandParentEvaluator = parent.getParentEvaluator();
        StructuralEvaluator.Parent grandParent = (StructuralEvaluator.Parent) grandParentEvaluator;
        assertEquals("ul", grandParent.getChildEvaluator().getClass().getSimpleName());
        Evaluator rootEvaluator = grandParent.getParentEvaluator();
        assertEquals(StructuralEvaluator.Root.class, rootEvaluator.getClass());
    }

    @org.junit.Test
    public void testParseComplexSelectorWithAttributeAndValue() {
        String query = "input[type='password'][name='password']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        CombiningEvaluator and = (CombiningEvaluator) evaluator;
        Equals typeEquals = (Equals) and.getLeftMostEvaluator();
        assertEquals("input", typeEquals.getLeftOperand().getClass());
        assertEquals("type", typeEquals.getRightOperand().getClass().getSimpleName());
        assertEquals("password", ((String) typeEquals.getRightOperand()).trim());

        Evaluator nameEquals = (Equals) and.getRightMostEvaluator();
        assertEquals("input", nameEquals.getLeftOperand().getClass());
        assertEquals("name", nameEquals.getRightOperand().getClass().getSimpleName());
        assertEquals("password", ((String) nameEquals.getRightOperand()).trim());
    }

    @org.junit.Test
    public void testParseComplexSelectorWithAttributeAndRegexValue() {
        String query = "input[name~='user']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        CombiningEvaluator and = (CombiningEvaluator) evaluator;
        Equals nameContains = (Equals) and.getLeftMostEvaluator();
        assertEquals("input", nameContains.getLeftOperand().getClass());
        assertEquals("name", nameContains.getRightOperand().getClass().getSimpleName());

        Matches regexMatcher = (Matches) nameContains.getRightOperand();
        Pattern pattern = regexMatcher.getPattern();
        Matcher matcher = pattern.matcher("user123");
        assertTrue(matcher.matches());
    }

    @org.junit.Test
    public void testParseComplexSelectorWithPseudoClass() {
        String query = "p:first-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        IsFirstChild isFirstChild = (IsFirstChild) evaluator;
        assertEquals("p", isFirstChild.getTagName());
    }

    @org.junit.Test
    public void testParseComplexSelectorWithNthChild() {
        String query = "li:nth-child(3)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        IsNthChild isNthChild = (IsNthChild) evaluator;
        assertEquals("li", isNthChild.getTagName());
        assertEquals(3, isNthChild.getNthIndex());
        assertTrue(isNthChild.isForwardSelect());
    }

    @org.junit.Test
    public void testParseComplexSelectorWithNthLastChild() {
        String query = "li:nth-last-child(-n+2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        IsNthLastChild isNthLastChild = (IsNthLastChild) evaluator;
        assertEquals("li", isNthLastChild.getTagName());
        assertEquals(2, isNthLastChild.getNthIndex());
        assertFalse(isNthLastChild.isForwardSelect());
    }

    @org.junit.Test
    public void testParseComplexSelectorWithNthOfType() {
        String query = "li:nth-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        IsNthOfType isNthOfType = (IsNthOfType) evaluator;
        assertEquals("li", isNthOfType.getTagName());
        assertEquals(2, isNthOfType.getNthIndex());
        assertTrue(isNthOfType.isForwardSelect());
    }

    @org.junit.Test
    public void testParseComplexSelectorWithNthLastOfType() {
        String query = "li:nth-last-of-type(-n+2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        IsNthLastOfType isNthLastOfType = (IsNthLastOfType) evaluator;
        assertEquals("li", isNthLastOfType.getTagName());
        assertEquals(2, isNthLastOfType.getNthIndex());
        assertFalse(isNthLastOfType.isForwardSelect());
    }

    @org.junit.Test
    public void testParseComplexSelectorWithHas() {
        String query = "div:has(p)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        Has has = (Has) evaluator;
        assertEquals("p", has.getSubSelector().getClass().getSimpleName());
    }

    @org.junit.Test
    public void testParseComplexSelectorWithContains() {
        String query = "div:contains(hello)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        ContainsText containsText = (ContainsText) evaluator;
        assertEquals("hello", containsText.getTextToMatch());
    }

    @org.junit.Test
    public void testParseComplexSelectorWithContainsOwn() {
        String query = "div:containsOwn(hello)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        ContainsOwnText containsOwnText = (ContainsOwnText) evaluator;
        assertEquals("hello", containsOwnText.getTextToMatch());
    }

    @org.junit.Test
    public void testParseComplexSelectorWithContainsData() {
        String query = "input:containsData(value)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        ContainsData containsData = (ContainsData) evaluator;
        assertEquals("value", containsData.getDataToMatch());
    }

    @org.junit.Test
    public void testParseComplexSelectorWithMatches() {
        String query = "input:matches(.+\\d+)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        Matches matches = (Matches) evaluator;
        Pattern pattern = matches.getPattern();
        String regex = ".*\\d+";
        Matcher matcher = pattern.matcher("input123");
        assertTrue(matcher.matches());
    }

    @org.junit.Test
    public void testParseComplexSelectorWithMatchesOwn() {
        String query = "input:matchesOwn(.+\\d+)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        MatchesOwn matchesOwn = (MatchesOwn) evaluator;
        Pattern pattern = matchesOwn.getPattern();
        String regex = ".*\\d+";
        Matcher matcher = pattern.matcher("input123");
        assertTrue(matcher.matches());
    }

    @org.junit.Test
    public void testParseComplexSelectorWithNot() {
        String query = "input:not([type='text'])";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        Not not = (Not) evaluator;
        Equals equals = (Equals) not.getSubSelector();
        assertEquals("input", equals.getLeftOperand().getClass());
        assertEquals("type", equals.getRightOperand().getClass().getSimpleName());
        assertEquals("text", ((String) equals.getRightOperand()).trim());
    }
}