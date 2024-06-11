package org.jsoup.select;

import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

import org.jsoup.select.QueryParser;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class QueryParserTest {

 private static final String[] TEST_QUERIES = {
 // Test simple selectors
 "div",
 "div.testClass",
 "div.testClass.testSubClass",
 "div#testId",
 "div#testId.testClass",
 "div.testClass#testId",
 "div[title]",
 "div[title='Sample Title']",
 "div[title^='Sample']",
 "div[title$='Title']",
 "div[title*='Sample']",
 "div:first-child",
 "div:last-child",
 "div:only-child",
 "div:nth-child(2)",
 "div:nth-child(odd)",
 "div:nth-child(even)",
 "div:not(.testClass)",

 // Test complex selectors
 "div > p > ul > li > a",
 "div > p > ul > li > a:contains('Link Text')",
 "div > p > ul > li > a[href='http://example.com']",
 "div > p > ul > li > a[href^='http']",
 "div > p > ul > li > a[href$='.html']",
 "div > p > ul > li > a[href*='example']",
 "div > p > ul > li > a:first-child",
 "div > p > ul > li > a:last-child",
 "div > p > ul > li > a:only-child",
 "div > p > ul > li > a:nth-child(2)",
 "div > p > ul > li > a:nth-child(odd)",
 "div > p > ul > li > a:nth-child(even)",
 "div > p > ul > li > a:not(:first-child)",
 "div > p > ul > li > a:not(:last-child)",
 "div > p > ul > li > a:not(.testClass)",
 "div > p > ul > li > a:not([href='http://example.com'])",
 "div > p > ul > li > a:not([href^='http'])",
 "div > p > ul > li > a:not([href$='.html'])",
 "div > p > ul > li > a:not([href*='example'])",
 "div > p > ul > li > a:not(:first-child):not(:last-child)",
 "div > p > ul > li > a:not(.testClass):not([href='http://example.com'])",
 "div > p > ul > li > a:not(.testClass):not([href^='http']):not([href$='.html'])",
 "div > p > ul > li > a:not(.testClass):not([href*='example'])",
 "div > p > ul > li > a:not(.testClass):not(:first-child):not(:last-child)",
 "div > p > ul > li > a:not(.testClass):not([href='http://example.com']):not([href^='http']):not([href$='.html'])",
 "div > p > ul > li > a:not(.testClass):not([href*='example']):not(:first-child):not(:last-child)",
 "div > p > ul > li > a:not(.testClass):not([href='http://example.com']):not([href^='http']):not([href$='.html']):not(:first-child):not(:last-child)"
 };

 @Test
 public void testParseQuery() {
 for (String query : TEST_QUERIES) {
 System.out.println("Testing query: " + query);
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 // TODO: Add more tests here to cover all possible Evaluator types
 assertEquals("Root evaluator should be returned for empty queries", Evaluator.Id.class, evaluator.getClass(), query);
 }
 }

 @Test
 public void testCombineQueries() {
 String query1 = "div > p > ul > li > a:first-child";
 String query2 = "div > p > ul > li > a:last-child";
 QueryParser parser1 = new QueryParser(query1);
 QueryParser parser2 = new QueryParser(query2);
 Evaluator evaluator1 = parser1.parse();
 Evaluator evaluator2 = parser2.parse();
 // TODO: Add more tests here to cover all possible Evaluator combinations
 CombiningEvaluator.And and = new CombiningEvaluator.And(evaluator1, evaluator2);
 Evaluator result = and.parse();
 // TODO: Add more tests here to check that the result matches the expected selector
 assertTrue("Expected combinator AND to be returned", result instanceof CombiningEvaluator.And);
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testInvalidQuery() {
 String invalidQuery = "div > p > ul > li > a:not(.testClass):not([href*='example']):not:not:not";
 QueryParser parser = new QueryParser(invalidQuery);
 parser.parse();
 }
}