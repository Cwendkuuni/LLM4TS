package org.jsoup.select.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.select.StructuralEvaluator;
import org.jsoup.select.Evaluator.Id;
import org.jsoup.select.Evaluator.Class;
import org.jsoup.select.Evaluator.Tag;
import org.jsoup.select.Evaluator.Attribute;
import org.jsoup.select.Evaluator.AttributeWithValue;
import org.jsoup.select.Evaluator.AttributeStarting;
import org.jsoup.select.Evaluator.AttributeWithValueStarting;
import org.jsoup.select.Evaluator.AttributeWithValueEnding;
import org.jsoup.select.Evaluator.AttributeWithValueContaining;
import org.jsoup.select.Evaluator.AttributeWithValueMatching;
import org.jsoup.select.Evaluator.AllElements;
import org.jsoup.select.Evaluator.IndexLessThan;
import org.jsoup.select.Evaluator.IndexGreaterThan;
import org.jsoup.select.Evaluator.IndexEquals;
import org.jsoup.select.StructuralEvaluator.Has;
import org.jsoup.select.StructuralEvaluator.ImmediateParent;
import org.jsoup.select.StructuralEvaluator.Parent;
import org.jsoup.select.StructuralEvaluator.ImmediatePreviousSibling;
import org.jsoup.select.StructuralEvaluator.PreviousSibling;
import org.jsoup.select.StructuralEvaluator.Not;

public class QueryParserTest {

 @Test
 public void testParse() {
 String query = "div.class1 > p.class2:contains(hello)";
 Evaluator evaluator = QueryParser.parse(query);

 List<Evaluator> evaluators = evaluator.getChildren();
 assertEquals("name of the first evaluator should be StructuralEvaluator.And", StructuralEvaluator.And.class, evaluators.get(0).getClass());

 List<Evaluator> andEvaluators = (List<Evaluator>) evaluators.get(0);
 assertEquals("number of evaluators in StructuralEvaluator.And should be 2", 2, andEvaluators.size());

 Evaluator firstEvaluator = andEvaluators.get(0);
 assertEquals("name of the first evaluator should be StructuralEvaluator.ImmediateParent", StructuralEvaluator.ImmediateParent.class, firstEvaluator.getClass());

 Evaluator secondEvaluator = andEvaluators.get(1);
 assertEquals("name of the second evaluator should be Evaluator.Class", Evaluator.Class.class, secondEvaluator.getClass());

 List<String> classes = ((Class) secondEvaluator).getClasses();
 assertEquals("number of classes for Evaluator.Class should be 1", 1, classes.size());

 assertEquals("first class should be class2", "class2", classes.get(0));

 Evaluator thirdEvaluator = andEvaluators.get(2);
 assertEquals("name of the third evaluator should be StructuralEvaluator.Has", StructuralEvaluator.Has.class, thirdEvaluator.getClass());

 Evaluator hasEvaluator = thirdEvaluator.getChild(0);
 assertEquals("name of the child evaluator of StructuralEvaluator.Has should be Evaluator.ContainsText", Evaluator.ContainsText.class, hasEvaluator.getClass());

 List<String> texts = ((Evaluator.ContainsText) hasEvaluator).getTexts();
 assertEquals("number of texts for Evaluator.ContainsText should be 1", 1, texts.size());

 assertEquals("text should be hello", "hello", texts.get(0));
 }

 @Test
 public void testParseEmptyQuery() {
 String query = "";
 Evaluator evaluator = QueryParser.parse(query);
 assertNull("Evaluator should be null for empty query", evaluator);
 }

 @Test
 public void testParseInvalidQuery() {
 String query = "div:not(>p#class1";
 Evaluator evaluator = QueryParser.parse(query);
 assertNull("Evaluator should be null for invalid query", evaluator);
 }

 @Test
 public void testParseInvalidQuery2() {
 String query = "div:not(>p#class1:has(p.class2)";
 Evaluator evaluator = QueryParser.parse(query);
 assertNull("Evaluator should be null for invalid query", evaluator);
 }

 @Test
 public void testParseInvalidQuery3() {
 String query = "div:has(>p#class1:not(>p.class2)";
 Evaluator evaluator = QueryParser.parse(query);
 assertNull("Evaluator should be null for invalid query", evaluator);
 }

}