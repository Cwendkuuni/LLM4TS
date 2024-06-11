package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.helper.Whitelist;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class CleanerTest {

 @Test
 public void testCreateCleaner() {
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 assertNotNull(cleaner);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateCleanerNullWhitelist() {
 Cleaner cleaner = new Cleaner(null);
 }

 @Test
 public void testCleanDocument() {
 Document dirtyDoc = Document.createFromString("<html><body><h1 id='title' onclick='alert(\"XSS\");'>Test</h1></body></html>", "http://test.com");
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 Document cleanDoc = cleaner.clean(dirtyDoc);

 assertEquals("<html><body><h1>Test</h1></body></html>", cleanDoc.html());
 assertFalse(cleanDoc.body().hasAttrs());
 }

 @Test
 public void testCleanDocumentWithWhiteList() {
 Document dirtyDoc = Document.createFromString("<html><body><h1 id='title' onclick='alert(\"XSS\");'>Test</h1><p class='test'>Some text</p></body></html>", "http://test.com");
 Whitelist whitelist = new Whitelist.SimpleTagBasedWhitelist(Tag.H1, Tag.P);
 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanDoc = cleaner.clean(dirtyDoc);

 assertEquals("<html><body><h1>Test</h1><p>Some text</p></body></html>", cleanDoc.html());
 assertFalse(cleanDoc.body().hasAttrs());
 }

 @Test
 public void testIsValidDocument() {
 Document dirtyDoc = Document.createFromString("<html><body><h1 id='title' onclick='alert(\"XSS\");'>Test</h1><p class='test'>Some text</p></body></html>", "http://test.com");
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());

 assertTrue(cleaner.isValid(dirtyDoc));
 }

 @Test
 public void testIsValidDocumentWithInvalidAttributes() {
 Document dirtyDoc = Document.createFromString("<html><body><h1 id='title' onclick='alert(\"XSS\");'>Test</h1><p class='test' onclick='alert(\"XSS\");'>Some text</p></body></html>", "http://test.com");
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());

 assertFalse(cleaner.isValid(dirtyDoc));
 }

 @Test
 public void testIsValidDocumentWithValidAttributes() {
 Document dirtyDoc = Document.createFromString("<html><body><h1 id='title' class='highlight'>Test</h1><p class='test'>Some text</p></body></html>", "http://test.com");
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());

 assertTrue(cleaner.isValid(dirtyDoc));
 }

 @Test
 public void testCopySafeNodes() {
 Element root = new Element(Tag.BODY, "http://test.com");
 Element dest = new Element(Tag.BODY, "http://test.com");
 int numDiscarded = Cleaner.copySafeNodes(root, dest);

 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithInvalidTag() {
 Element root = new Element(Tag.SCRIPT, "http://test.com");
 Element dest = new Element(Tag.BODY, "http://test.com");
 int numDiscarded = Cleaner.copySafeNodes(root, dest);

 assertEquals(1, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithValidTag() {
 Element root = new Element(Tag.DIV, "http://test.com");
 Element dest = new Element(Tag.BODY, "http://test.com");
 int numDiscarded = Cleaner.copySafeNodes(root, dest);

 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithAttributes() {
 Element root = new Element(Tag.DIV, "http://test.com").attr("class", "test").attr("id", "testId");
 Element dest = new Element(Tag.BODY, "http://test.com");
 int numDiscarded = Cleaner.copySafeNodes(root, dest);

 assertEquals(2, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithValidAttributes() {
 Element root = new Element(Tag.DIV, "http://test.com").attr("class", "test").attr("id", "testId").attr("data-test", "testData");
 Element dest = new Element(Tag.BODY, "http://test.com");
 Whitelist whitelist = new Whitelist.SimpleAttributeBasedWhitelist("class", "test");
 Cleaner cleaner = new Cleaner(whitelist);
 int numDiscarded = cleaner.copySafeNodes(root, dest);

 assertEquals(1, numDiscarded);
 }

 @Test
 public void testCreateSafeElement() {
 Element sourceEl = new Element(Tag.DIV, "http://test.com").attr("class", "test").attr("id", "testId");
 Element dest = new Element(Tag.BODY, "http://test.com");
 int numDiscarded = Cleaner.copySafeNodes(sourceEl, dest);

 assertEquals(2, numDiscarded);

 ElementMeta meta = Cleaner.createSafeElement(sourceEl);
 Element destChild = meta.el;

 assertEquals(Tag.DIV, destChild.tagName());
 assertEquals("test", destChild.classes());
 assertEquals(0, destChild.intAttribute("id").getValue());
 }

}