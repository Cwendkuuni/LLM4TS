package org.jsoup.select.test;

import org.junit.Test;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import static org.junit.Assert.*;

public class QueryParserTest {

 @Test
 public void testParseSimpleSelector() {
 String query = "p.class1.class2";
 Evaluator evaluator = QueryParser.parse(query);
 List<Evaluator> expected = new ArrayList<>();
 expected.add(new Evaluator.Class("class1"));
 expected.add(new Evaluator.Class("class2"));
 expected.add(new Evaluator.Tag("p"));
 assertEquals(expected.size(), evaluator.size());
 for (Evaluator eval : evaluator) {
 assertEquals(eval.getClass(), expected.get(expected.size() - 1).getClass());
 }
 }

 @Test
 public void testParseCombinator() {
 String query = "p > div";
 Evaluator evaluator = QueryParser.parse(query);
 List<Evaluator> expected = new ArrayList<>();
 expected.add(new Evaluator.Tag("p"));
 expected.add(new StructuralEvaluator.ImmediateParent(new Evaluator.Tag("div")));
 assertEquals(expected.size(), evaluator.size());
 for (Evaluator eval : evaluator) {
 assertEquals(eval.getClass(), expected.get(expected.size() - 1).getClass());
 }
 }

 @Test
 public void testParseAttributeSelector() {
 String query = "input[type=text]";
 Evaluator evaluator = QueryParser.parse(query);
 List<Evaluator> expected = new ArrayList<>();
 expected.add(new Evaluator.Tag("input"));
 expected.add(new Evaluator.AttributeWithValue("type", "text"));
 assertEquals(expected.size(), evaluator.size());
 for (Evaluator eval : evaluator) {
 assertEquals(eval.getClass(), expected.get(expected.size() - 1).getClass());
 }
 }

 @Test
 public void testParsePseudoClassSelector() {
 String query = "p:empty";
 Evaluator evaluator = QueryParser.parse(query);
 Evaluator expected = new Evaluator.IsEmpty();
 assertEquals(expected.getClass(), evaluator.getClass());
 }

 @Test
 public void testParseComplexSelector() {
 String query = "div.class1 p.class2:empty";
 Evaluator evaluator = QueryParser.parse(query);
 List<Evaluator> expected = new ArrayList<>();
 expected.add(new Evaluator.Tag("div"));
 expected.add(new Evaluator.Class("class1"));
 expected.add(new Evaluator.Tag("p"));
 expected.add(new Evaluator.IsEmpty());
 assertEquals(expected.size(), evaluator.size());
 for (Evaluator eval : evaluator) {
 assertEquals(eval.getClass(), expected.get(expected.size() - 1).getClass());
 }
 }

 @Test
 public void testParseInvalidSelector() {
 String query = "div class1";
 try {
 QueryParser.parse(query);
 fail();
 } catch (Selector.SelectorParseException e) {
 String expectedMessage = "Unexpected ID or class name in query: class1";
 assertEquals(expectedMessage, e.getMessage());
 }
 }

 @Test
 public void testParseSubSelector() {
 String query = "div > p > span:containsOwn(text)";
 Evaluator parent = new Evaluator.Tag("div");
 Evaluator child = new Evaluator.Tag("p");
 Evaluator sibling = new StructuralEvaluator.ImmediatePreviousSibling(new Evaluator.Tag("span"));
 Evaluator grandChild = new Evaluator.ContainsOwnText("text");
 Evaluator expected = new StructuralEvaluator.Has(grandChild);
 expected.setParent(sibling);
 expected.setParent(child);
 expected.setParent(parent);
 Evaluator evaluator = QueryParser.parse(query);
 assertEquals(evaluator, expected);
 }
}