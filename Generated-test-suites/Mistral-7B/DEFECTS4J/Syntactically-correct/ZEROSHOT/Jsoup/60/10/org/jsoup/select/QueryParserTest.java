package org.jsoup.select.test;

import static org.junit.Assert.*;
import org.jsoup.parser.QueryParser;
import org.jsoup.select.Evaluator;

import java.util.List;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParseBasicSelectors() {
        String query = "div.class1";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluators = new ArrayList<Evaluator>();
        evaluators.add(evaluator);
        assertEquals(Evaluator.Class.class, evaluators.get(0).getClass());
        assertEquals("class1", evaluators.get(0).asClass().getValue());
    }

    @Test
    public void testParseAttributeSelectors() {
        String query = "div[id='id1']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluators = new ArrayList<Evaluator>();
        evaluators.add(evaluator);
        assertEquals(Evaluator.Id.class, evaluators.get(0).getClass());
        assertEquals("id1", evaluators.get(0).asId().getValue());

        query = "div[class='class1'][title='title1']";
        evaluator = QueryParser.parse(query);
        evaluators.clear();
        evaluators.add(evaluator);
        Evaluator classEvaluator = evaluators.get(0).asClass();
        Evaluator titleEvaluator = evaluators.get(0).asAttributeWithValue("title", "title1");
        assertTrue(classEvaluator.getValue().equals("class1"));
        assertTrue(titleEvaluator.getValue().equals("title1"));
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "div > p.class1";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluators = new ArrayList<Evaluator>();
        evaluators.add(evaluator);

        StructuralEvaluator structuralEvaluator = (StructuralEvaluator) evaluators.get(0);
        assertEquals(StructuralEvaluator.ImmediateParent.class, structuralEvaluator.getParentEvaluator().getClass());
        assertEquals(Evaluator.Class.class, structuralEvaluator.getChildEvaluator().getClass());
        assertEquals("class1", structuralEvaluator.getChildEvaluator().asClass().getValue());
    }

    @Test
    public void testParsePseudoSelectors() {
        String query = "div:first-child";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluators = new ArrayList<Evaluator>();
        evaluators.add(evaluator);
        assertEquals(Evaluator.IsFirstChild.class, evaluators.get(0).getClass());

        query = "div:has(p:contains(text))";
        evaluator = QueryParser.parse(query);
        evaluators.clear();
        evaluators.add(evaluator);
        assertEquals(Evaluator.IsHas.class, evaluators.get(0).getClass());
        StructuralEvaluator hasEvaluator = (StructuralEvaluator) evaluators.get(0);
        Evaluator childEvaluator = hasEvaluator.getChildEvaluator();
        assertEquals(Evaluator.ContainsText.class, childEvaluator.getClass());
        assertEquals("text", childEvaluator.asContainsText().getValue());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "div:asdf";
        QueryParser.parse(query);
    }
}