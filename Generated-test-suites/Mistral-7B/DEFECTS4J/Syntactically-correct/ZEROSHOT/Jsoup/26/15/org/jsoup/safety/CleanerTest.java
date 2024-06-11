package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for Cleaner.java
 */
public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setUp() {
 whitelist = new Whitelist();
 cleaner = new Cleaner(whitelist);

 String html = "<html><body><div id='test' class='test' data-test='test'><h1>Test <script>alert('XSS');</script></h1><p>Hello <b id='bold'>World</b></p></div></body></html>";
 dirtyDocument = Jsoup.parse(html);
 cleanDocument = Jsoup.parse("<html><body></body></html>");
 }

 @Test
 public void testClean() {
 Document cleanedDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><div id='test' class='test'><h1></h1><p><b id='bold'>World</b></p></div></body></html>", cleanedDocument.html());

 Element testDiv = cleanedDocument.selectFirst("#test");
 assertNotNull(testDiv);
 assertNull(testDiv.select("script").size());
 }

 @Test
 public void testIsValid() {
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidWithScriptTag() {
 Document scriptDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 assertFalse(cleaner.isValid(scriptDocument));
 }

 @Test
 public void testCopySafeNodes() {
 cleanDocument.body().appendChild(cleanDocument.newElement("div", "testDiv"));
 Element testDiv = cleanDocument.selectFirst("div.testDiv");
 assertEquals(0, cleaner.copySafeNodes(dirtyDocument.body(), testDiv).intValue());

 Element testDivWithScript = cleanDocument.selectFirst("#test > div");
 cleanDocument.body().appendChild(testDivWithScript.clone());
 cleaner.copySafeNodes(dirtyDocument.body(), testDivWithScript);
 assertEquals(1, testDivWithScript.select("script").size());
 assertNull(testDivWithScript.select("script").get(0).text());
 }
}