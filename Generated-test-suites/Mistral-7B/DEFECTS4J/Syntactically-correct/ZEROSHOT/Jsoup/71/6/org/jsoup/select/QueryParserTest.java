package org.jsoup.select;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseTagSelector() {
        String query = "div";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Evaluator tagEvaluator = new Evaluator.Tag("div");
        expectedEvaluators.add(tagEvaluator);

        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseClassSelector() {
        String query = ".class1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Evaluator classEvaluator = new Evaluator.Class("class1");
        expectedEvaluators.add(classEvaluator);

        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseIdSelector() {
        String query = "#id1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Evaluator idEvaluator = new Evaluator.Id("id1");
        expectedEvaluators.add(idEvaluator);

        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "[href='https://example.com']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Evaluator attributeEvaluator = new Evaluator.AttributeWithValue("a", "href", "https://example.com");
        expectedEvaluators.add(attributeEvaluator);

        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseCombinatorSelector() {
        String query = "div > p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Evaluator tag1Evaluator = new Evaluator.Tag("div");
        Evaluator combinatorEvaluator = new CombiningEvaluator.And(
                new StructuralEvaluator.ImmediateChildOf(tag1Evaluator),
                new Evaluator.Tag("p"));

        expectedEvaluators.add(combinatorEvaluator);

        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseCombinatorSelectorWithMultipleSelectors() {
        String query = "div > p > span";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Evaluator tag1Evaluator = new Evaluator.Tag("div");
        Evaluator combinator1Evaluator = new CombiningEvaluator.And(
                new StructuralEvaluator.ImmediateChildOf(tag1Evaluator),
                new Evaluator.Tag("p"));

        Evaluator tag2Evaluator = new Evaluator.Tag("span");
        Evaluator combinator2Evaluator = new CombiningEvaluator.And(
                new StructuralEvaluator.ImmediateChildOf(combinator1Evaluator),
                tag2Evaluator);

        expectedEvaluators.add(combinator2Evaluator);

        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseAttributeSelectorWithDifferentComparators() {
        String query = "[src='https://example.com' | title='Example']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Evaluator attributeEvaluator1 = new Evaluator.AttributeWithValue("a", "src", "https://example.com");
        Evaluator orEvaluator = new CombiningEvaluator.Or(attributeEvaluator1, new Evaluator.AttributeWithValue("a", "title", "Example"));

        expectedEvaluators.add(orEvaluator);

        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParsePseudoClassSelector() {
        String query = ":first-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Evaluator firstChildEvaluator = new Evaluator.IsFirstChild();

        expectedEvaluators.add(firstChildEvaluator);

        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParsePseudoClassSelectorWithAttribute() {
        String query = ":checked";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Evaluator checkedEvaluator = new Evaluator.IsTagWithAttribute("input", "checked", true);

        expectedEvaluators.add(checkedEvaluator);

        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParsePseudoClassSelectorWithNthChild() {
        String query = "li:nth-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Evaluator nthChildEvaluator = new Evaluator.IsNthChild(1, 2);

        expectedEvaluators.add(nthChildEvaluator);

        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParsePseudoClassSelectorWithNthOfType() {
        String query = "li:nth-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Evaluator nthOfTypeEvaluator = new Evaluator.IsNthOfType(1, 2);

        expectedEvaluators.add(nthOfTypeEvaluator);

        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParsePseudoClassSelectorWithContainsText() {
        String query = "p:contains(Java)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Evaluator containsTextEvaluator = new Evaluator.ContainsText("Java");

        expectedEvaluators.add(containsTextEvaluator);

        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParsePseudoClassSelectorWithMatches() {
        String query = "li:matches(^li\\s+[0-9]+$)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Evaluator matchesEvaluator = new Evaluator.Matches(Pattern.compile("^li\\s+[0-9]+$"));

        expectedEvaluators.add(matchesEvaluator);

        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParsePseudoClassSelectorWithNot() {
        String query = ":not(.error)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Evaluator notEvaluator = new StructuralEvaluator.Not(new Evaluator.Class("error"));

        expectedEvaluators.add(notEvaluator);

        assertEquals(expectedEvaluators, evaluator.getChildren());
    }
}