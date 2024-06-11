package org.jsoup.select;

import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;

import java.util.List;

public class QueryParserTest {

    @org.junit.Test
    public void testParseSimpleSelector() {
        String query = "div";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        assertEquals(expectedEvaluators, evaluator.getSubTree());
    }

    @org.junit.Test
    public void testParseIdSelector() {
        String query = "#myId";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Id("myId"));
        assertEquals(expectedEvaluators, evaluator.getSubTree());
    }

    @org.junit.Test
    public void testParseClassSelector() {
        String query = ".myClass";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Class("myClass"));
        assertEquals(expectedEvaluators, evaluator.getSubTree());
    }

    @org.junit.Test
    public void testParseMultiSelector() {
        String query = "div.myClass";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new Evaluator.Class("myClass"));
        Evaluator expectedCombiner = new CombiningEvaluator.And(expectedEvaluators.get(0), expectedEvaluators.get(1));
        assertEquals(expectedCombiner, evaluator);
    }

    @org.junit.Test
    public void testParseDescendantSelector() {
        String query = "div > p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new StructuralEvaluator.ImmediateChild());
        expectedEvaluators.add(new Evaluator.Tag("p"));
        Evaluator expectedCombiner = new CombiningEvaluator.And(expectedEvaluators.get(0), expectedEvaluators.get(1));
        assertEquals(expectedCombiner, evaluator);
    }

    @org.junit.Test
    public void testParseAdjacentSiblingSelector() {
        String query = "div > p + p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new StructuralEvaluator.ImmediateChild());
        expectedEvaluators.add(new Evaluator.Tag("p"));
        expectedEvaluators.add(new StructuralEvaluator.ImmediatePreviousSibling());
        Evaluator expectedCombiner = new CombiningEvaluator.And(expectedEvaluators.get(0), new CombiningEvaluator.And(expectedEvaluators.get(1), expectedEvaluators.get(2)));
        assertEquals(expectedCombiner, evaluator);
    }

    @org.junit.Test
    public void testParseAttributeSelector() {
        String query = "div[class='myClass']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new Evaluator.AttributeWithValue(new Attribute("class"), "myClass"));
        Evaluator expectedCombiner = new CombiningEvaluator.And(expectedEvaluators.get(0), expectedEvaluators.get(1));
        assertEquals(expectedCombiner, evaluator);
    }

    @org.junit.Test
    public void testParseAttributeSelectorWithValue() {
        String query = "div[class='myClass' && title='My Title']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new Evaluator.Attribute(new Attribute("class")));
        expectedEvaluators.add(new Evaluator.AttributeWithValue(new Attribute("class"), "myClass"));
        expectedEvaluators.add(new StructuralEvaluator.And());
        expectedEvaluators.add(new Evaluator.Attribute(new Attribute("title")));
        expectedEvaluators.add(new Evaluator.AttributeWithValue(new Attribute("title"), "My Title"));
        Evaluator expectedCombiner = new CombiningEvaluator.And(expectedEvaluators.get(0), expectedEvaluators.get(1));
        assertEquals(expectedCombiner, evaluator.getSubTree().get(0));
        assertEquals(expectedEvaluators.get(3), evaluator.getSubTree().get(1));
        assertEquals(expectedEvaluators.get(5), evaluator.getSubTree().get(2));
    }

    @org.junit.Test
    public void testParsePsedoElementSelector() {
        String query = "input:checked";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.PseudoElement("input"));
        expectedEvaluators.add(new Evaluator.PseudoElementValue("checked"));
        Evaluator expectedCombiner = new CombiningEvaluator.And(expectedEvaluators.get(0), expectedEvaluators.get(1));
        assertEquals(expectedCombiner, evaluator);
    }

    @org.junit.Test
    public void testParseNegatedSelector() {
        String query = "div:not(.myClass)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new StructuralEvaluator.Not());
        expectedEvaluators.add(new Evaluator.Class("myClass"));
        Evaluator expectedCombiner = new CombiningEvaluator.And(expectedEvaluators.get(0), expectedEvaluators.get(1));
        assertEquals(expectedCombiner, evaluator);
    }

    @org.junit.Test
    public void testParseNthChildSelector() {
        String query = "li:nth-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("li"));
        Evaluator nthChildEvaluator = new Evaluator.NthChild(1, 2);
        expectedEvaluators.add(nthChildEvaluator);
        Evaluator expectedCombiner = new CombiningEvaluator.And(expectedEvaluators.get(0), expectedEvaluators.get(1));
        assertEquals(expectedCombiner, evaluator);
    }

    @org.junit.Test
    public void testParseNthOfTypeSelector() {
        String query = "li:nth-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("li"));
        Evaluator nthOfTypeEvaluator = new Evaluator.NthOfType(1, 2);
        expectedEvaluators.add(nthOfTypeEvaluator);
        Evaluator expectedCombiner = new CombiningEvaluator.And(expectedEvaluators.get(0), expectedEvaluators.get(1));
        assertEquals(expectedCombiner, evaluator);
    }

    @org.junit.Test
    public void testParseNthLastChildSelector() {
        String query = "li:nth-last-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("li"));
        Evaluator nthLastChildEvaluator = new Evaluator.NthLastChild(false, 1, 2);
        expectedEvaluators.add(nthLastChildEvaluator);
        Evaluator expectedCombiner = new CombiningEvaluator.And(expectedEvaluators.get(0), expectedEvaluators.get(1));
        assertEquals(expectedCombiner, evaluator);
    }

    @org.junit.Test
    public void testParseNthLastOfTypeSelector() {
        String query = "li:nth-last-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("li"));
        Evaluator nthLastOfTypeEvaluator = new Evaluator.NthLastOfType(true, 1, 2);
        expectedEvaluators.add(nthLastOfTypeEvaluator);
        Evaluator expectedCombiner = new CombiningEvaluator.And(expectedEvaluators.get(0), expectedEvaluators.get(1));
        assertEquals(expectedCombiner, evaluator);
    }
}