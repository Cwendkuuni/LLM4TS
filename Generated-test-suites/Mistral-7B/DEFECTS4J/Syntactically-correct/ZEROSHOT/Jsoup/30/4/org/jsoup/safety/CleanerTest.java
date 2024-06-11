package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.helper.Validators;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {

 private Cleaner cleaner;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setUp() {
 Whitelist whitelist = Whitelist.simpleTextOnly();
 cleaner = new Cleaner(whitelist);

 String html = "<html><head></head><body>" +
 " <h1 id=\"title\" class=\"title\">Test</h1>" +
 " <script>alert('XSS');</script>" +
 " <div id=\"content\">Hello World!</div>" +
 " <img src=\"image.jpg\" alt=\"Image\"></img>" +
 "</body></html>";

 dirtyDocument = Jsoup.parse(html);
 cleanDocument = cleaner.clean(dirtyDocument);
 }

 @Test
 public void testCleaner_ValidDocument() {
 // Test that the cleaner returns a valid document when given a valid document
 assertTrue(cleanDocument.outerHtml().equals("<html><head></head><body><h1 class=\"title\">Test</h1><div id=\"content\">Hello World!</div></body></html>"));
 }

 @Test(expected = NullPointerException.class)
 public void testCleaner_NullDocument() {
 // Test that the cleaner throws a NullPointerException when given a null document
 cleaner.clean(null);
 }

 @Test
 public void testCleaner_InvalidDocument() {
 // Test that the cleaner returns a valid document when given an invalid document
 assertFalse(cleaner.isValid(dirtyDocument));
 assertTrue(cleanDocument.outerHtml().equals("<html><head></head><body></body></html>"));
 }

 @Test
 public void testCopySafeNodes_SafeElement() {
 // Test that the copySafeNodes method correctly copies a safe element
 Element sourceEl = dirtyDocument.selectFirst("h1");
 Element destEl = cleanDocument.body().elementSibling(0);
 int numDiscarded = cleaner.copySafeNodes(sourceEl, destEl);
 assertEquals(0, numDiscarded);
 assertEquals(sourceEl.outerHtml(), destEl.outerHtml());
 }

 @Test
 public void testCopySafeNodes_InvalidElement() {
 // Test that the copySafeNodes method correctly discards an invalid element
 Element sourceEl = dirtyDocument.selectFirst("script");
 Element destEl = cleanDocument.body().elementSibling(0);
 int numDiscarded = cleaner.copySafeNodes(sourceEl, destEl);
 assertEquals(1, numDiscarded);
 assertTrue(cleanDocument.body().children().size() == cleanDocument.body().childNodes().size());
 }

 @Test
 public void testCopySafeNodes_TextNode() {
 // Test that the copySafeNodes method correctly copies a text node
 TextNode sourceText = dirtyDocument.body().text();
 TextNode destText = cleanDocument.body().child(1);
 int numDiscarded = cleaner.copySafeNodes(null, null);
 assertEquals(0, numDiscarded);
 assertEquals(sourceText.getWholeText(), destText.text());
 }

 @Test
 public void testCreateSafeElement_SafeElement() {
 // Test that the createSafeElement method correctly creates a safe element
 Element sourceEl = dirtyDocument.selectFirst("h1");
 Element destEl = new Element(Tag.H1, sourceEl.baseUri(), new Attributes());
 ElementMeta meta = cleaner.createSafeElement(sourceEl);
 assertEquals(sourceEl.outerHtml(), meta.el.outerHtml());
 assertEquals(0, meta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElement_InvalidElement() {
 // Test that the createSafeElement method correctly discards an invalid element
 Element sourceEl = dirtyDocument.selectFirst("script");
 Element destEl = new Element(Tag.H1, sourceEl.baseUri(), new Attributes());
 ElementMeta meta = cleaner.createSafeElement(sourceEl);
 assertEquals(0, meta.el.childNodes().size());
 assertTrue(meta.numAttribsDiscarded > 0);
 }
}