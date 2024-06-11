package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class CleanerTest {

 @Test
 public void testCleanerCreation() {
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 assertNotNull(cleaner);
 }

 @Test(expected = NullPointerException.class)
 public void testCleanerCreationWithNullWhitelist() {
 Cleaner cleaner = new Cleaner(null);
 }

 @Test
 public void testDocumentCleaning() {
 String html = "<html><head></head><body><script>alert('XSS');</script><p>Hello</p></body></html>";
 Document dirtyDocument = Parser.parseBodyFragment(html);
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals(0, cleanDocument.body().children().size());
 }

 @Test
 public void testDocumentValidation() {
 String html = "<html><head></head><body><script>alert('XSS');</script><p>Hello</p></body></html>";
 Document dirtyDocument = Parser.parseBodyFragment(html);
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }

 @Test
 public void testValidDocument() {
 String html = "<html><head></head><body><p>Hello</p></body></html>";
 Document dirtyDocument = Parser.parseBodyFragment(html);
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertTrue(isValid);
 }

 @Test
 public void testCopySafeNodes() {
 String html = "<html><head></head><body><script>alert('XSS');</script><p id=\"test\" class=\"test\" title=\"test\">Hello</p></body></html>";
 Document dirtyDocument = Parser.parseBodyFragment(html);
 Whitelist whitelist = Whitelist.simpleText();
 Element root = dirtyDocument.body();
 Element dest = new Element("div");
 int numDiscarded = Cleaner.this.copySafeNodes(root, dest);
 assertEquals(1, numDiscarded);
 assertEquals(1, dest.childNodes().size());
 }

 @Test
 public void testCopySafeNodesWithEmptyDocument() {
 String html = "<html></html>";
 Document dirtyDocument = Parser.parseBodyFragment(html);
 Whitelist whitelist = Whitelist.simpleText();
 Element root = dirtyDocument.body();
 Element dest = new Element("div");
 int numDiscarded = Cleaner.this.copySafeNodes(root, dest);
 assertEquals(0, numDiscarded);
 assertEquals(0, dest.childNodes().size());
 }

 @Test
 public void testCopySafeNodesWithScriptTag() {
 String html = "<html><head></head><body><script>alert('XSS');</script></body></html>";
 Document dirtyDocument = Parser.parseBodyFragment(html);
 Whitelist whitelist = Whitelist.simpleText();
 Element root = dirtyDocument.body();
 Element dest = new Element("div");
 int numDiscarded = Cleaner.this.copySafeNodes(root, dest);
 assertEquals(1, numDiscarded);
 assertEquals(0, dest.childNodes().size());
 }

 @Test
 public void testCopySafeNodesWithAllowedCustomTag() {
 String html = "<html><head></head><body><mytag></mytag></body></html>";
 Whitelist whitelist = new Whitelist();
 whitelist.addTags("mytag");
 Cleaner cleaner = new Cleaner(whitelist);
 Document dirtyDocument = Parser.parseBodyFragment(html);
 Element root = dirtyDocument.body();
 Element dest = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(root, dest);
 assertEquals(1, numDiscarded);
 assertEquals(1, dest.childNodes().size());
 }

 @Test
 public void testCopySafeNodesWithAllowedCustomTagAndAttribute() {
 String html = "<html><head></head><body><mytag data-test=\"test\"></mytag></body></html>";
 Whitelist whitelist = new Whitelist();
 whitelist.addTags("mytag").addAttribute("mytag", "data-test", "^test$");
 Cleaner cleaner = new Cleaner(whitelist);
 Document dirtyDocument = Parser.parseBodyFragment(html);
 Element root = dirtyDocument.body();
 Element dest = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(root, dest);
 assertEquals(1, numDiscarded);
 assertEquals(1, dest.childNodes().size());
 assertTrue(dest.childNode(0).hasAttr("data-test"));
 assertEquals("test", dest.childNode(0).attr("data-test"));
 }

 @Test
 public void testCopySafeNodesWithMultipleAllowedCustomTags() {
 String html = "<html><head></head><body><mytag1></mytag1><mytag2></mytag2></body></html>";
 Whitelist whitelist = new Whitelist();
 whitelist.addTags("mytag1", "mytag2");
 Cleaner cleaner = new Cleaner(whitelist);
 Document dirtyDocument = Parser.parseBodyFragment(html);
 Element root = dirtyDocument.body();
 Element dest = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(root, dest);
 assertEquals(2, numDiscarded);
 assertEquals(2, dest.childNodes().size());
 }

 @Test
 public void testCopySafeNodesWithMultipleAllowedCustomTagsAndAttributes() {
 String html = "<html><head></head><body><mytag1 data-test1=\"test1\"></mytag1><mytag2 data-test2=\"test2\"></mytag2></body></html>";
 Whitelist whitelist = new Whitelist();
 whitelist.addTags("mytag1", "mytag2")
 .addAttribute("mytag1", "data-test1", "^test1$")
 .addAttribute("mytag2", "data-test2", "^test2$");
 Cleaner cleaner = new Cleaner(whitelist);
 Document dirtyDocument = Parser.parseBodyFragment(html);
 Element root = dirtyDocument.body();
 Element dest = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(root, dest);
 assertEquals(2, numDiscarded);
 assertEquals(2, dest.childNodes().size());
 assertTrue(dest.childNode(0).hasAttr("data-test1"));
 assertEquals("test1", dest.childNode(0).attr("data-test1"));
 assertTrue(dest.childNode(1).hasAttr("data-test2"));
 assertEquals("test2", dest.childNode(1).attr("data-test2"));
 }

}