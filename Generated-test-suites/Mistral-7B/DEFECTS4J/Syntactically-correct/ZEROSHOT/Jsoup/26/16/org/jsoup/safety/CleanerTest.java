package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;

import java.util.List;

public class CleanerTest {

 private static final String HTML_SAMPLE = "<html><body><div id=\"test\"><h1>Test<h1><script>alert('XSS');</script></div></body></html>";
 private static final String WHITELIST_SAMPLE = "whitelist { h1, div }";

 @org.junit.Before
 public void setUp() {
 Whitelist whitelist = new Whitelist.Static(WHITELIST_SAMPLE);
 cleaner = new Cleaner(whitelist);
 }

 private Cleaner cleaner;

 @org.junit.Test
 public void testCreateCleanerWithValidWhitelist() {
 Cleaner cleaner = new Cleaner(new Whitelist.Static(WHITELIST_SAMPLE));
 assertNotNull(cleaner);
 }

 @org.junit.Test(expected = NullPointerException.class)
 public void testCreateCleanerWithNullWhitelist() {
 Cleaner cleaner = new Cleaner(null);
 }

 @org.junit.Test
 public void testCleanMethod() {
 Document dirtyDocument = Jsoup.parse(HTML_SAMPLE);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertFalse(cleanDocument.body().childNodes().hasText());
 }

 @org.junit.Test
 public void testCleanMethodWithScriptTag() {
 Document dirtyDocument = Jsoup.parse(HTML_SAMPLE);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertTrue(cleanDocument.body().children().size() <= dirtyDocument.body().children().size());
 assertNull(cleanDocument.body().child(0).child(0).child(0).child(0).child(0).attr("src"));
 }

 @org.junit.Test
 public void testIsValidMethodWithValidHtml() {
 Document dirtyDocument = Jsoup.parse(HTML_SAMPLE);
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @org.junit.Test
 public void testIsValidMethodWithInvalidHtml() {
 Document dirtyDocument = Jsoup.parse("<html><body><div id=\"test\"><h1>Test<h1><script>alert('XSS');</script></div></body></html>");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @org.junit.Test
 public void testCopySafeNodesMethodWithSafeTag() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Test</h1></body></html>");
 Element dest = new Element(Tag.H1, dirtyDocument.baseUri(), new Attributes());
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body().get(0), dest);
 assertTrue(numDiscarded == 0);
 assertNotNull(dest.child(0));
 }

 @org.junit.Test
 public void testCopySafeNodesMethodWithUnsafeTag() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 Element dest = new Element(Tag.H1, dirtyDocument.baseUri(), new Attributes());
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body().get(0), dest);
 assertTrue(numDiscarded > 0);
 assertNull(dest.child(0));
 }

 @org.junit.Test
 public void testCreateSafeElementMethod() {
 Element source = new Element("script", null, new Attributes());
 ElementMeta meta = cleaner.createSafeElement(source);
 assertNull(meta.el);
 assertTrue(meta.numAttribsDiscarded > 0);
 }

 @org.junit.Test
 public void testCreateSafeElementMethodWithSafeTag() {
 Element source = new Element("h1", null, new Attributes());
 Element dest = new Element(Tag.H1, source.baseUri(), new Attributes());
 ElementMeta meta = cleaner.createSafeElement(source);
 assertNotNull(meta.el);
 assertFalse(meta.numAttribsDiscarded > 0);
 assertEquals(source, meta.el);
 }
}