package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test file for Cleaner class
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

 String html = "<html><body><h1 id=\"title\" class=\"title\" onclick=\"alert('XSS')\">Title</h1>" +
 "<p><script src=\"malicious.js\"></script></p>" +
 "<a href=\"javascript:alert('XSS')\">Link</a></body></html>";
 dirtyDocument = Jsoup.parse(html);
 cleanDocument = Document.createShell(dirtyDocument.baseUri());
 }

 @Test
 public void testCreateCleaner() {
 Validate.notNull(cleaner);
 }

 @Test
 public void testCreateCleanDocument() {
 assertNotNull(cleanDocument);
 }

 @Test
 public void testCleanDocument() {
 Document cleanedDocument = cleaner.clean(dirtyDocument);

 assertEquals(cleanDocument.body().childNodeSize(), cleanedDocument.body().childNodeSize());

 List<Node> cleanNodes = cleanedDocument.body().childNodes();
 for (Node cleanNode : cleanNodes) {
 if (cleanNode instanceof Element) {
 Element cleanElement = (Element) cleanNode;
 assertFalse(cleanElement.tagName().equalsIgnoreCase("script"));
 assertFalse(cleanElement.tagName().equalsIgnoreCase("a"));
 assertFalse(cleanElement.tagName().equalsIgnoreCase("h1") && cleanElement.attr("onclick") != null);
 } else if (cleanNode instanceof TextNode) {
 TextNode cleanText = (TextNode) cleanNode;
 assertNull(cleanText.getWholeText().contains("XSS"));
 }
 }
 }

 @Test
 public void testIsValidDocument() {
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidValidDocument() {
 String validHtml = "<html><body><h1>Title</h1><p>Content</p></body></html>";
 Document validDocument = Jsoup.parse(validHtml);

 assertTrue(cleaner.isValid(validDocument));
 }

 @Test
 public void testCopySafeNodes() {
 Element sourceEl = dirtyDocument.body().getElementsByTag("h1").first();
 Element destEl = cleanDocument.body().addElement(Tag.H1);
 cleaner.copySafeNodes(sourceEl, destEl);

 assertEquals(sourceEl.text(), destEl.text());
 }

 @Test
 public void testIsSafeTag() {
 assertTrue(cleaner.whitelist.isSafeTag("h1"));
 assertFalse(cleaner.whitelist.isSafeTag("script"));
 }

 @Test
 public void testIsSafeAttribute() {
 Element sourceEl = dirtyDocument.body().getElementsByTag("h1").first();
 String sourceAttrName = "class";
 String sourceAttrValue = "title";
 sourceEl.attr(sourceAttrName, sourceAttrValue);

 Attributes sourceAttrs = sourceEl.attributes();
 Attribute sourceAttr = sourceAttrs.get(sourceAttrName);

 assertTrue(cleaner.whitelist.isSafeAttribute("h1", sourceEl, sourceAttr));
 }

 @Test
 public void testGetEnforcedAttributes() {
 List<String> expectedAttributes = new ArrayList<>();
 expectedAttributes.add("class");
 expectedAttributes.add("id");

 Attributes enforcedAttributes = cleaner.whitelist.getEnforcedAttributes("h1");
 for (String attrName : expectedAttributes) {
 assertTrue(enforcedAttributes.keySet().contains(attrName));
 }
 }
}