package org.jsoup.safety;

import org.junit.After;
import org.junit.Before;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class CleanerTest {

 private Parser parser;
 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setup() {
 parser = Jsoup.parser();
 whitelist = Whitelist.relaxed();
 cleaner = new Cleaner(whitelist);
 }

 @After
 public void teardown() {
 cleaner = null;
 whitelist = null;
 parser = null;
 }

 /**
 * Test for the constructor of Cleaner class
 */
 @org.junit.Test
 public void testCleanerConstruction() {
 Document dirtyDocument = parser.parse(new File("test.html")); //Assuming test.html exists
 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanDoc = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDoc);
 }

 /**
 * Test for clean() method with a valid document
 */
 @org.junit.Test
 public void testCleanValidDocument() {
 String html = "<html><body><h1>Test</h1><p>Hello World</p></body></html>";
 Document dirtyDocument = parser.parseBodyFragment(html);
 Document cleanDoc = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><h1></h1><p></p></body></html>", cleanDoc.body().html());
 }

 /**
 * Test for clean() method with an invalid document
 */
 @org.junit.Test
 public void testCleanInvalidDocument() {
 String html = "<html><body><script>alert('XSS');</script></body></html>";
 Document dirtyDocument = parser.parseBodyFragment(html);
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 Document cleanDoc = cleaner.clean(dirtyDocument);
 assertEquals("<html><body></body></html>", cleanDoc.body().html());
 }

 /**
 * Test for isValid() method with a valid document
 */
 @org.junit.Test
 public void testIsValidValidDocument() {
 String html = "<html><body><h1>Test</h1><p>Hello World</p></body></html>";
 Document dirtyDocument = parser.parseBodyFragment(html);
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertTrue(isValid);
 }

 /**
 * Test for isValid() method with an invalid document
 */
 @org.junit.Test
 public void testIsValidInvalidDocument() {
 String html = "<html><body><script>alert('XSS');</script></body></html>";
 Document dirtyDocument = parser.parseBodyFragment(html);
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }

 /**
 * Test for copySafeNodes() method with a valid document
 */
 @org.junit.Test
 public void testCopySafeNodesValidDocument() {
 String html = "<html><body><h1>Test<span id='test'>Hello World</span></h1></body></html>";
 Document dirtyDocument = parser.parseBodyFragment(html);
 Document cleanDoc = cleaner.clean(dirtyDocument);
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDoc.body());
 assertEquals(0, numDiscarded);
 }

 /**
 * Test for copySafeNodes() method with an invalid document
 */
 @org.junit.Test
 public void testCopySafeNodesInvalidDocument() {
 String html = "<html><body><script>alert('XSS');</script></body></html>";
 Document dirtyDocument = parser.parseBodyFragment(html);
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDoc.body());
 assertEquals(1, numDiscarded);
 }

 /**
 * Test for isSafeTag() and isSafeAttribute() methods in Whitelist class
 */
 @org.junit.Test
 public void testWhitelistMethods() {
 List<String> allowedTags = Arrays.asList("h1", "p", "span");
 Whitelist whitelist = Whitelist.simpleText();
 for (String tag : allowedTags) {
 boolean isSafeTag = whitelist.isSafeTag(tag);
 assertTrue(isSafeTag);
 }
 List<String> disallowedTags = Arrays.asList("script");
 for (String tag : disallowedTags) {
 boolean isSafeTag = whitelist.isSafeTag(tag);
 assertFalse(isSafeTag);
 }
 List<String> allowedAttributes = Arrays.asList("id");
 String tag = "span";
 boolean isSafeAttribute = whitelist.isSafeAttribute(tag, null, "id");
 assertTrue(isSafeAttribute);
 List<String> disallowedAttributes = Arrays.asList("onclick");
 boolean isSafeAttribute = whitelist.isSafeAttribute(tag, null, "onclick");
 assertFalse(isSafeAttribute);
 }
}