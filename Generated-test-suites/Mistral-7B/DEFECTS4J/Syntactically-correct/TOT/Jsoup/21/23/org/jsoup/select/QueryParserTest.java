package org.jsoup.select.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;

public class QueryParserTest {

 @Test
 public void testParseEmptyQuery() {
 QueryParser parser = new QueryParser("");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.AllElements);
 }

 @Test
 public void testParseSingleTagQuery() {
 QueryParser parser = new QueryParser("div");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.Tag);
 assertEquals("div", evaluator.toString());
 }

 @Test
 public void testParseTagWithClassQuery() {
 QueryParser parser = new QueryParser("div.class");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 Evaluator tagEvaluator = ((CombiningEvaluator.And) evaluator).getChildren().get(0);
 assertTrue(tagEvaluator instanceof Evaluator.Tag);
 assertEquals("div", tagEvaluator.toString());
 Evaluator classEvaluator = ((CombiningEvaluator.And) evaluator).getChildren().get(1);
 assertTrue(classEvaluator instanceof Evaluator.Class);
 assertEquals("class", classEvaluator.toString());
 }

 @Test
 public void testParseTagWithIdQuery() {
 QueryParser parser = new QueryParser("#id");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.Id);
 assertEquals("id", evaluator.toString());
 }

 @Test
 public void testParseTagWithIdAndClassQuery() {
 QueryParser parser = new QueryParser("#id.class");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 Evaluator idEvaluator = ((CombiningEvaluator.And) evaluator).getChildren().get(0);
 assertTrue(idEvaluator instanceof Evaluator.Id);
 assertEquals("id", idEvaluator.toString());
 Evaluator classEvaluator = ((CombiningEvaluator.And) evaluator).getChildren().get(1);
 assertTrue(classEvaluator instanceof Evaluator.Class);
 assertEquals("class", classEvaluator.toString());
 }

 @Test
 public void testParseTagWithAttributeQuery() {
 QueryParser parser = new QueryParser("input[type='text']");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 Evaluator tagEvaluator = ((CombiningEvaluator.And) evaluator).getChildren().get(0);
 assertTrue(tagEvaluator instanceof Evaluator.Tag);
 assertEquals("input", tagEvaluator.toString());
 Evaluator attributeEvaluator = ((CombiningEvaluator.And) evaluator).getChildren().get(1);
 assertTrue(attributeEvaluator instanceof Evaluator.Attribute);
 assertEquals("type", attributeEvaluator.toString());
 assertEquals("text", ((Evaluator.Attribute) attributeEvaluator).getValue());
 }

 @Test
 public void testParseTagWithAttributeValueQuery() {
 QueryParser parser = new QueryParser("input[type='text'][value='Example']");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 Evaluator tagEvaluator = ((CombiningEvaluator.And) evaluator).getChildren().get(0);
 assertTrue(tagEvaluator instanceof Evaluator.Tag);
 assertEquals("input", tagEvaluator.toString());
 Evaluator attributeEvaluator = ((CombiningEvaluator.And) evaluator).getChildren().get(1);
 assertTrue(attributeEvaluator instanceof Evaluator.AttributeWithValue);
 assertEquals("type", attributeEvaluator.toString());
 assertEquals("text", ((Evaluator.AttributeWithValue) attributeEvaluator).getName());
 assertEquals("Example", ((Evaluator.AttributeWithValue) attributeEvaluator).getValue());
 }

 @Test
 public void testParseComplexQuery() {
 QueryParser parser = new QueryParser("div.class1 > ul > li:first-child > a[href='https://example.com']");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 Evaluator rootEvaluator = ((CombiningEvaluator.And) evaluator).getChildren().get(0);
 assertTrue(rootEvaluator instanceof StructuralEvaluator.ImmediateParent);
 Evaluator parentEvaluator = ((StructuralEvaluator.ImmediateParent) rootEvaluator).getChild();
 assertTrue(parentEvaluator instanceof Evaluator.Tag);
 assertEquals("ul", parentEvaluator.toString());
 Evaluator liEvaluator = ((CombiningEvaluator.And) evaluator).getChildren().get(1);
 assertTrue(liEvaluator instanceof CombiningEvaluator.And);
 Evaluator firstChildEvaluator = ((CombiningEvaluator.And) liEvaluator).getChildren().get(0);
 assertTrue(firstChildEvaluator instanceof StructuralEvaluator.ImmediateChild);
 Evaluator aEvaluator = ((CombiningEvaluator.And) evaluator).getChildren().get(1);
 assertTrue(aEvaluator instanceof Evaluator.AttributeWithValue);
 assertEquals("href", aEvaluator.toString());
 assertEquals("https://example.com", ((Evaluator.AttributeWithValue) aEvaluator).getValue());
 }

 @Test
 public void testParseInvalidQuery() {
 QueryParser parser = new QueryParser("div.class^"); // Misspelled combinator
 Evaluator evaluator = parser.parse();
 fail("Expected Selector.SelectorParseException");
 }
}