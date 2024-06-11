package org.jsoup.select.test;

import org.junit.Test;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import static org.junit.Assert.*;

public class QueryParserTest {

 @Test
 public void testParseSimpleSelector() {
 String query = "div.class";
 Evaluator evaluator = QueryParser.parse(query);
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);

 StructuralEvaluator.And and = new StructuralEvaluator.And(evaluators);
 StructuralEvaluator.Tag tag = new StructuralEvaluator.Tag("div");
 StructuralEvaluator.ClassSelector classSelector = new StructuralEvaluator.ClassSelector("class");

 assertEquals(and, and.and(tag).and(classSelector).parse());
 }

 @Test
 public void testParseComplexSelectorWithCombinators() {
 String query = "div > ul > li:nth-child(2) > a[href='http://www.example.com']";
 Evaluator evaluator = QueryParser.parse(query);
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);

 StructuralEvaluator.Tag divTag = new StructuralEvaluator.Tag("div");
 StructuralEvaluator.ImmediateParent immediateParentDiv = new StructuralEvaluator.ImmediateParent(divTag);
 StructuralEvaluator.Tag ulTag = new StructuralEvaluator.Tag("ul");
 StructuralEvaluator.Tag liTag = new StructuralEvaluator.Tag("li");
 StructuralEvaluator.NthChild nthChild = new StructuralEvaluator.NthChild(2);
 StructuralEvaluator.Tag aTag = new StructuralEvaluator.Tag("a");
 Attributes attributes = new Attributes();
 attributes.put("href", "http://www.example.com");
 Evaluator hrefAttributeEvaluator = new AttributesWithValue(attributes, "href");

 assertEquals(hrefAttributeEvaluator, aTag.and(immediateParentLi).and(nthChild).parse());
 assertEquals(liTag.and(immediateParentUl).and(divTag), immediateParentLi.getParent());
 }

 @Test
 public void testParseAttributeSelectors() {
 String query = "p[class='red']";
 Evaluator evaluator = QueryParser.parse(query);
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);

 StructuralEvaluator.Tag pTag = new StructuralEvaluator.Tag("p");
 StructuralEvaluator.ClassSelector classSelector = new StructuralEvaluator.ClassSelector("red");

 assertEquals(classSelector, classSelector.parse());
 assertEquals(pTag.and(classSelector), evaluator);
 }

 @Test
 public void testParseInvalidQuery() {
 String query = "div class"; // missing combinator
 try {
 QueryParser.parse(query);
 fail();
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Invalid CSS query: div class";
 String actualMessage = e.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }
 }

 @Test
 public void testParseUnknownPseudoClass() {
 String query = "div:foo"; // unknown pseudo class
 try {
 QueryParser.parse(query);
 fail();
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Invalid CSS query: div:foo";
 String actualMessage = e.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }
 }

 @Test
 public void testParseInvalidAttributeSelector() {
 String query = "div[foo]"; // missing attribute name
 try {
 QueryParser.parse(query);
 fail();
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Invalid CSS query: div[foo]";
 String actualMessage = e.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }
 }

 @Test
 public void testParseInvalidAttributeSelectorWithInvalidAttributeValue() {
 String query = "div[foo='bar']"; // invalid attribute value
 try {
 QueryParser.parse(query);
 fail();
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Invalid CSS query: div[foo='bar']";
 String actualMessage = e.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }
 }

}