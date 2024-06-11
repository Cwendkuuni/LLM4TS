package org.jsoup.select.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.helper.StringUtil;

public class QueryParserTest {

 @Test
 public void testParseEmptyQuery() {
 QueryParser parser = new QueryParser("");
 Evaluator evaluator = parser.parse();
 assertNull(evaluator);
 }

 @Test
 public void testParseRootQuery() {
 QueryParser parser = new QueryParser("*");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.AllElements.class, evaluator.getClass());
 }

 @Test
 public void testParseTagQuery() {
 QueryParser parser = new QueryParser("div");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Tag.class, evaluator.getClass());
 assertEquals(Evaluator.Tag.class, evaluator.getClass());
 assertEquals("div", evaluator.toString());
 }

 @Test
 public void testParseClassQuery() {
 QueryParser parser = new QueryParser(".class");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Class.class, evaluator.getClass());
 assertEquals("class", evaluator.toString());
 }

 @Test
 public void testParseIdQuery() {
 QueryParser parser = new QueryParser("#id");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Id.class, evaluator.getClass());
 assertEquals("id", evaluator.toString());
 }

 @Test
 public void testParseCombinedQuery() {
 QueryParser parser = new QueryParser("div.class");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 assertEquals(Evaluator.Tag.class, ((CombiningEvaluator.And) evaluator).getLeft().getClass());
 assertEquals(Evaluator.Class.class, ((CombiningEvaluator.And) evaluator).getRight().getClass());
 }

 @Test
 public void testParseSelectorWithAttributes() {
 QueryParser parser = new QueryParser("input[type='text']");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 Evaluator attributeEvaluator = ((CombiningEvaluator.And) evaluator).getLeft();
 assertEquals(Evaluator.Tag.class, attributeEvaluator.getClass());
 assertEquals("input", attributeEvaluator.toString());
 Evaluator attributeCondition = ((CombiningEvaluator.And) evaluator).getRight();
 assertTrue(attributeCondition instanceof Evaluator.Attribute);
 assertEquals("type", attributeCondition.toString());
 assertEquals("text", ((Evaluator.Attribute) attributeCondition).getValue());
 }

 @Test
 public void testParseComplexSelectorWithCombinedSelectors() {
 QueryParser parser = new QueryParser("div.class1 > div.class2 > input[type='text']");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 Evaluator left = ((CombiningEvaluator.And) evaluator).getLeft();
 assertTrue(left instanceof CombiningEvaluator.And);
 Evaluator leftLeft = ((CombiningEvaluator.And) left).getLeft();
 assertEquals(Evaluator.Tag.class, leftLeft.getClass());
 assertEquals("div", leftLeft.toString());
 Evaluator leftRight = ((CombiningEvaluator.And) left).getRight();
 assertTrue(leftRight instanceof CombiningEvaluator.And);
 Evaluator rightLeft = ((CombiningEvaluator.And) leftRight).getLeft();
 assertEquals(Evaluator.Class.class, rightLeft.getClass());
 assertEquals("class1", rightLeft.toString());
 Evaluator rightRight = ((CombiningEvaluator.And) leftRight).getRight();
 assertTrue(rightRight instanceof StructuralEvaluator.ImmediateChild);
 assertEquals(Evaluator.Tag.class, rightRight.getLeft().getClass());
 assertEquals("div", rightRight.getLeft().toString());
 Evaluator rightRightRight = rightRight.getRight();
 assertEquals(Evaluator.Class.class, rightRightRight.getClass());
 assertEquals("class2", rightRightRight.toString());
 Evaluator rightRightRightRight = rightRightRight.getRight();
 assertEquals(Evaluator.Tag.class, rightRightRightRight.getClass());
 assertEquals("input", rightRightRightRight.toString());
 assertEquals("text", ((Evaluator.Attribute) rightRightRightRight).getValue());
 }

 @Test
 public void testParseMultipleSelectorsWithCombinator() {
 QueryParser parser = new QueryParser("div.class1, div.class2");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof CombiningEvaluator.Or);
 Evaluator left = ((CombiningEvaluator.Or) evaluator).getLeft();
 assertEquals(Evaluator.Class.class, left.getClass());
 assertEquals("class1", left.toString());
 Evaluator right = ((CombiningEvaluator.Or) evaluator).getRight();
 assertEquals(Evaluator.Class.class, right.getClass());
 assertEquals("class2", right.toString());
 }

 @Test
 public void testParseInvalidQuery() {
 QueryParser parser = new QueryParser("/* comment */");
 try {
 parser.parse();
 fail();
 } catch (Selector.SelectorParseException e) {
 // expected
 }
 }

 @Test
 public void testParseQueryWithInvalidSelectorType() {
 QueryParser parser = new QueryParser("div#id[type='text']:invalid");
 try {
 parser.parse();
 fail();
 } catch (Selector.SelectorParseException e) {
 // expected
 }
 assertEquals("Unknown pseudo class: invalid", e.getMessage());
 }

 @Test
 public void testParseQueryWithInvalidCombiner() {
 QueryParser parser = new QueryParser("div.class1 ~:div.class2");
 try {
 parser.parse();
 fail();
 } catch (Selector.SelectorParseException e) {
 // expected
 }
 assertEquals("Unknown combinator: ~:", e.getMessage());
 }

 @Test
 public void testParseQueryWithInvalidAttributeSelector() {
 QueryParser parser = new QueryParser("div[invalid=attribute]");
 try {
 parser.parse();
 fail();
 } catch (Selector.SelectorParseException e) {
 // expected
 }
 assertEquals("Invalid attribute name: invalid", e.getMessage());
 }
}