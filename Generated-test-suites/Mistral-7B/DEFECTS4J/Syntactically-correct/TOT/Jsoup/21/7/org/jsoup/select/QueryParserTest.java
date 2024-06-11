package org.jsoup.select.test;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.jsoup.parser. CSSElements.*;
import static org.jsoup.helper.StringUtil.*;

import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;

public class QueryParserTest {

 @Test
 public void testParseSimpleSelector() {
 String query = "div p";
 Evaluator eval = QueryParser.parse(query);
 assertEquals(Evaluator.Tag.DIV, eval.getClass());
 assertEquals(Evaluator.Tag.P, eval.getNext().getClass());
 }

 @Test
 public void testParseComplexSelector() {
 String query = "#header > #nav > ul > li > a:containsOwn(Home)";
 Evaluator eval = QueryParser.parse(query);
 assertEquals(Evaluator.Id.HEADER, eval.getNext().getNext());
 assertEquals(Evaluator.Tag.UL, eval.getNext().getNext().getNext());
 assertEquals(Evaluator.Tag.LI, eval.getNext().getNext().getNext().getNext());
 assertEquals(Evaluator.ContainsOwnText.class, eval.getNext().getNext().getNext().getNext().getClass());
 assertEquals("Home", eval.getNext().getNext().getNext().getNext().getValue());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidSelector() {
 String query = "div x:y";
 QueryParser.parse(query); // should throw exception
 }

 @Test
 public void testParseAttributeSelector() {
 String query = "input[type=text]";
 Evaluator eval = QueryParser.parse(query);
 assertEquals(Evaluator.Tag.INPUT, eval.getClass());
 assertEquals(Evaluator.Attribute.TYPE, eval.getNext().getClass());
 assertEquals("text", eval.getNext().getValue());
 }

 @Test
 public void testParseMultipleSelectors() {
 String query = "ul#nav li.active a:contains('Contact')";
 Evaluator eval = QueryParser.parse(query);
 assertEquals(Evaluator.Id.NAV, eval.getNext());
 assertEquals(Evaluator.Tag.LI, eval.getNext().getClass());
 assertEquals(Evaluator.Class.ACTIVE, eval.getNext().getNext().getClass());
 assertEquals(Evaluator.ContainsText.class, eval.getNext().getNext().getNext().getClass());
 assertEquals("Contact", eval.getNext().getNext().getNext().getValue());
 }

 @Test
 public void testParseNegatedSelector() {
 String query = "div:not(.error)";
 Evaluator eval = QueryParser.parse(query);
 assertEquals(Evaluator.Tag.DIV, eval.getClass());
 assertEquals(Evaluator.Not.class, eval.getNext().getClass());
 assertEquals(Evaluator.Class.ERROR, eval.getNext().getNext().getClass());
 }
}