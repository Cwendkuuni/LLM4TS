package org.jsoup.select.test;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Elements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class QueryParserTest {

 @Test
 public void testParseSimpleSelector() {
 String query = "div.class1";
 Evaluator evaluator = QueryParser.parse(query);

 assertEquals("div.class1", evaluator.toQueryString());
 assertTrue(evaluator.matches(Jsoup.parseHtml(" <div class='class1'></div> ").select("div.class1").first().toString()));
 assertFalse(evaluator.matches(Jsoup.parseHtml(" <div class='class2'></div> ").select("div.class1").first().toString()));
 }

 @Test
 public void testParseComplexSelectorWithCombinators() {
 String query = "#id1 > div.class1 > ul > li";
 Evaluator evaluator = QueryParser.parse(query);

 Element element = Jsoup.parseHtml(
 "<html id='id1'>" +
 "<body>" +
 "<div class='class1'>" +
 "<ul>" +
 "<li>Test</li>" +
 "</ul>" +
 "</div>" +
 "</body>" +
 "</html>").select(query).first();

 assertEquals("#id1 > div.class1 > ul > li", evaluator.toQueryString());
 assertNotNull(element);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSelector() {
 String query = "div#class1"; // should have a combinator: div > #class1 instead
 Evaluator evaluator = QueryParser.parse(query);
 }

 @Test
 public void testParseAttributeSelectors() {
 String query = "input[type='text']";
 Evaluator evaluator = QueryParser.parse(query);

 Elements elements = Jsoup.parseHtml("<form><input type='text' name='username'><input type='password'></form>").select(query);

 assertEquals("input[type='text']", evaluator.toQueryString());
 assertEquals(elements.size(), evaluator.select(Jsoup.parseHtml("<form><input type='text' name='username'><input type='password'></form>").toString()).size());
 }

 @Test
 public void testParseAttributeSelectorsWithValues() {
 String query = "input[type='text'][name='username']";
 Evaluator evaluator = QueryParser.parse(query);

 Element element = Jsoup.parseHtml("<form><input type='text' name='username'><input type='password' name='password'></form>").select(query).first();

 assertEquals("input[type='text'][name='username']", evaluator.toQueryString());
 assertNotNull(element);
 }

 @Test
 public void testParseAttributeSelectorsWithStartingValues() {
 String query = "input[name^='user']";
 Evaluator evaluator = QueryParser.parse(query);

 Elements elements = Jsoup.parseHtml("<form><input name='user1' type='text'><input name='user2' type='password'><input name='users' type='text'></form>").select(query);

 assertEquals("input[name^='user']", evaluator.toQueryString());
 assertEquals(elements.size(), evaluator.select(Jsoup.parseHtml("<form><input name='user1' type='text'><input name='user2' type='password'><input name='users' type='text'></form>").toString()).size());
 }

 @Test
 public void testParseAttributeSelectorsWithEndingValues() {
 String query = "input[name$='user']";
 Evaluator evaluator = QueryParser.parse(query);

 Elements elements = Jsoup.parseHtml("<form><input name='user1' type='text'><input name='user2' type='password'><input name='users' type='text'></form>").select(query);

 assertEquals("input[name$='user']", evaluator.toQueryString());
 assertEquals(elements.size(), evaluator.select(Jsoup.parseHtml("<form><input name='user1' type='text'><input name='user2' type='password'><input name='users' type='text'></form>").toString()).size());
 }

 @Test
 public void testParseAttributeSelectorsWithContainingValues() {
 String query = "input[name*='user']";
 Evaluator evaluator = QueryParser.parse(query);

 Elements elements = Jsoup.parseHtml("<form><input name='user1' type='text'><input name='user2' type='password'><input name='users' type='text'></form>").select(query);

 assertEquals("input[name*='user']", evaluator.toQueryString());
 assertEquals(elements.size(), evaluator.select(Jsoup.parseHtml("<form><input name='user1' type='text'><input name='user2' type='password'><input name='users' type='text'></form>").toString()).size());
 }

 @Test
 public void testParseAttributeSelectorsWithValuesAndClasses() {
 String query = "input[name='username'][class='textfield']";
 Evaluator evaluator = QueryParser.parse(query);

 Element element = Jsoup.parseHtml("<form><input name='username' type='text' class='textfield'><input name='password' type='password' class='textfield'></form>").select(query).first();

 assertEquals("input[name='username'][class='textfield']", evaluator.toQueryString());
 assertNotNull(element);
 }

 @Test
 public void testParsePseudoClassSelectors() {
 String query = "input:checked";
 Evaluator evaluator = QueryParser.parse(query);

 Element element = Jsoup.parseHtml("<form><input type='checkbox' checked><input type='text'></form>").select(query).first();

 assertEquals("input:checked", evaluator.toQueryString());
 assertNotNull(element);
 }

 @Test
 public void testParsePseudoClassSelectorsWithNegation() {
 String query = "input:not(:checked)";
 Evaluator evaluator = QueryParser.parse(query);

 Element element = Jsoup.parseHtml("<form><input type='checkbox' checked><input type='text'></form>").select(query).first();

 assertEquals("input:not(:checked)", evaluator.toQueryString());
 assertNull(element);
 }

 @Test
 public void testParsePseudoClassSelectorsWithNthChild() {
 String query = "li:nth-child(2)";
 Evaluator evaluator = QueryParser.parse(query);

 Element element = Jsoup.parseHtml("<ul><li>First</li><li>Second</li><li>Third</li></ul>").select(query).first();

 assertEquals("li:nth-child(2)", evaluator.toQueryString());
 assertNotNull(element);
 }

 @Test
 public void testParsePseudoClassSelectorsWithNthOfType() {
 String query = "li:nth-of-type(2)";
 Evaluator evaluator = QueryParser.parse(query);

 Element element = Jsoup.parseHtml("<ul><li>First</li><li>Second</li><li>Third</li></ul>").select(query).first();

 assertEquals("li:nth-of-type(2)", evaluator.toQueryString());
 assertNotNull(element);
 }

 @Test
 public void testParsePseudoClassSelectorsWithNthLastChild() {
 String query = "li:nth-last-child(2)";
 Evaluator evaluator = QueryParser.parse(query);

 Element element = Jsoup.parseHtml("<ul><li>First</li><li>Second</li><li>Third</li></ul>").select(query).first();

 assertEquals("li:nth-last-child(2)", evaluator.toQueryString());
 assertNotNull(element);
 }

 @Test
 public void testParsePseudoClassSelectorsWithNthLastOfType() {
 String query = "li:nth-last-of-type(2)";
 Evaluator evaluator = QueryParser.parse(query);

 Element element = Jsoup.parseHtml("<ul><li>First</li><li>Second</li><li>Third</li></ul>").select(query).first();

 assertEquals("li:nth-last-of-type(2)", evaluator.toQueryString());
 assertNotNull(element);
 }

 @Test
 public void testParsePseudoClassSelectorsWithFirstChild() {
 String query = "li:first-child";
 Evaluator evaluator = QueryParser.parse(query);

 Element element = Jsoup.parseHtml("<ul><li>First</li><li>Second</li><li>Third</li></ul>").select(query).first();

 assertEquals("li:first-child", evaluator.toQueryString());
 assertNotNull(element);
 }

 @Test
 public void testParsePseudoClassSelectorsWithLastChild() {
 String query = "li:last-child";
 Evaluator evaluator = QueryParser.parse(query);

 Element element = Jsoup.parseHtml("<ul><li>First</li><li>Second</li><li>Third</li></ul>").select(query).first();

 assertEquals("li:last-child", evaluator.toQueryString());
 assertNotNull(element);
 }

 @Test
 public void testParsePseudoClassSelectorsWithOnlyChild() {
 String query = "li:only-child";
 Evaluator evaluator = QueryParser.parse(query);

 Element element = Jsoup.parseHtml("<ul><li></li></ul>").select(query).first();

 assertEquals("li:only-child", evaluator.toQueryString());
 assertNotNull(element);
 }

 @Test
 public void testParsePseudoClassSelectorsWithOnlyOfType() {
 String query = "li:only-of-type";
 Evaluator evaluator = QueryParser.parse(query);

 Element element = Jsoup.parseHtml("<ul><li></li></ul>").select(query).first();

 assertEquals("li:only-of-type", evaluator.toQueryString());
 assertNotNull(element);
 }

 @Test
 public void testParsePseudoClassSelectorsWithEmpty() {
 String query = "li:empty";
 Evaluator evaluator = QueryParser.parse(query);

 Element element = Jsoup.parseHtml("<ul><li></li></ul>").select(query).first();

 assertEquals("li:empty", evaluator.toQueryString());
 assertNotNull(element);
 }

 @Test
 public void testParsePseudoClassSelectorsWithRoot() {
 String query = ":root";
 Evaluator evaluator = QueryParser.parse(query);

 Document document = Jsoup.parse("<html lang='en'><head><meta charset='UTF-8'></head><body><div></div></body></html>");

 assertEquals(":root", evaluator.toQueryString());
 assertTrue(evaluator.matches(document.toString()));
 }

 @Test
 public void testParseComplexSelectorWithMultipleCombators() {
 String query = "#id1 > div > ul > li:nth-child(2)";
 Evaluator evaluator = QueryParser.parse(query);

 Element element = Jsoup.parseHtml(
 "<html id='id1'>" +
 "<body>" +
 "<div>" +
 "<ul>" +
 "<li>First</li>" +
 "<li>Second</li>" +
 "</ul>" +
 "</div>" +
 "</body>" +
 "</html>").select(query).first();

 assertEquals("#id1 > div > ul > li:nth-child(2)", evaluator.toQueryString());
 assertNotNull(element);
 }

 @Test
 public void testParseComplexSelectorWithAttributesAndCombators() {
 String query = "input[type='text'][name='username']:not(:disabled)";
 Evaluator evaluator = QueryParser.parse(query);

 Elements elements = Jsoup.parseHtml("<form><input type='text' name='username' disabled><input type='text' name='password'><input type='text' name='username'></form>").select(query);

 assertEquals("input[type='text'][name='username']:not(:disabled)", evaluator.toQueryString());
 assertEquals(elements.size(), evaluator.select(Jsoup.parseHtml("<form><input type='text' name='username' disabled><input type='text' name='password'><input type='text' name='username'></form>").toString()).size());
 }

 @Test
 public void testParseComplexSelectorWithNthChildAndAttributes() {
 String query = "li:nth-child(2) > a[href^='http']";
 Evaluator evaluator = QueryParser.parse(query);

 Element element = Jsoup.parseHtml("<ul><li><a href='http://example.com'>External Link</a></li><li><a href='mailto:test@example.com'>Email Link</a></li></ul>").select(query).first();

 assertEquals("li:nth-child(2) > a[href^='http']", evaluator.toQueryString());
 assertNotNull(element);
 }

 @Test
 public void testParseComplexSelectorWithNthChildAndContainingValues() {
 String query = "li:nth-child(2) > a[href*='example']";
 Evaluator evaluator = QueryParser.parse(query);

 Element element = Jsoup.parseHtml("<ul><li><a href='http://example.com'>External Link</a></li><li><a href='http://www.example.com'>Another Example Link</a></li></ul>").select(query).first();

 assertEquals("li:nth-child(2) > a[href*='example']", evaluator.toQueryString());
 assertNotNull(element);
 }
}