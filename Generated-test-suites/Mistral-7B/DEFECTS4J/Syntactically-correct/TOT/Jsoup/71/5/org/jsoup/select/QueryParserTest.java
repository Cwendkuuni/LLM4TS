package org.jsoup.select.test;

import org.junit.Test;
import org.jsoup.helper.StringUtil;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.parser.TokenQueue;
import static org.junit.Assert.*;

public class QueryParserTest {

 @Test
 public void testParseSimpleSelector() {
 String query = "div.red";
 Evaluator evaluator = QueryParser.parse(query);
 List<Evaluator> expectedElements = new ArrayList<>();
 expectedElements.add(new Evaluator.Class("red"));
 expectedElements.add(new Evaluator.Tag("div"));
 assertEquals(new CombiningEvaluator.And(expectedElements), evaluator);
 }

 @Test
 public void testParseComplexSelector() {
 String query = "div.red:not(.blue):has(input[type='text']):first-child";
 Evaluator evaluator = QueryParser.parse(query);

 List<Evaluator> expectedElements = new ArrayList<>();

 // div with class "red"
 expectedElements.add(new Evaluator.Class("red"));
 expectedElements.add(new Evaluator.Tag("div"));
 // not div with class "blue"
 expectedElements.add(new StructuralEvaluator.Not(new Evaluator.Class("blue")));
 // div has input of type "text"
 expectedElements.add(new StructuralEvaluator.Has(new Evaluator.Tag("input")));
 expectedElements.add(new Evaluator.Attribute(new AttributeEvaluator("type", "text")));
 // first child
 expectedElements.add(new Evaluator.IsFirstChild());

 assertEquals(new CombiningEvaluator.And(expectedElements), evaluator);
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidSelector() {
 String query = "div.red:not(.blue):has(input[type='text':])";
 QueryParser.parse(query);
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseMissingSelector() {
 String query = ":test";
 try {
 QueryParser.parse(query);
 fail("Expected Selector.SelectorParseException");
 } catch (Selector.SelectorParseException e) {
 // expected
 }
 }
}