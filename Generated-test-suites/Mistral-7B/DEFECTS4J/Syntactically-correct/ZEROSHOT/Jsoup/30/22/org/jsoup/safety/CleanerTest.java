package org.jsoup.safety;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {

 private static final String HTML_WITH_SAFE_TAGS = "<html><body><h1 id='title'>Test</h1><p>Safe content</p></body></html>";
 private static final String HTML_WITH_UNSAFE_TAGS = "<html><body><h1 id='title'>Test</h1><script>alert('XSS');</script></body></html>";
 private static final String HTML_WITH_UNSAFE_TAGS_AND_VALID_ATTRIBUTES = "<html><body><h1 id='title' data-foo='bar'>Test</h1><script>alert('XSS');</script></body></html>";

 @Before
 public void setUp() {
 Whitelist whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 private Cleaner cleaner;

 @Test
 public void testClean_ValidHtml_ReturnsNewDocument() {
 Document dirtyDocument = Jsoup.parse(HTML_WITH_SAFE_TAGS);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals(HTML_WITH_SAFE_TAGS, cleanDocument.html());
 assertEquals(0, cleanDocument.body().childNodes().size());
 }

 @Test
 public void testClean_InvalidHtml_ReturnsNewDocumentWithDiscardedNodes() {
 Document dirtyDocument = Jsoup.parse(HTML_WITH_UNSAFE_TAGS);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals(HTML_WITH_SAFE_TAGS, cleanDocument.html());
 assertEquals(0, cleanDocument.body().childNodes().size());
 }

 @Test
 public void testIsValid_ValidHtml_ReturnsTrue() {
 Document dirtyDocument = Jsoup.parse(HTML_WITH_SAFE_TAGS);
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertTrue(isValid);
 }

 @Test
 public void testIsValid_InvalidHtmlContainingSafeAttributes_ReturnsTrue() {
 Document dirtyDocument = Jsoup.parse(HTML_WITH_UNSAFE_TAGS_AND_VALID_ATTRIBUTES);
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertTrue(isValid);
 }

 @Test
 public void testIsValid_InvalidHtml_ReturnsFalse() {
 Document dirtyDocument = Jsoup.parse(HTML_WITH_UNSAFE_TAGS);
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertFalse(isValid);
 }

 @Test
 public void testCopySafeNodes_SafeTagWithSafeAttributes_NoNodesDiscarded() {
 Element root = Jsoup.parse(HTML_WITH_SAFE_TAGS).body();
 Element destination = new Element(Tag.BODY);
 int numDiscarded = cleaner.copySafeNodes(root, destination);

 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodes_SafeTagWithInvalidAttributes_NodesDiscarded() {
 Element root = Jsoup.parse(HTML_WITH_UNSAFE_TAGS).body();
 Element destination = new Element(Tag.BODY);
 int numDiscarded = cleaner.copySafeNodes(root, destination);

 assertEquals(1, numDiscarded);
 }

 @Test
 public void testCopySafeNodes_UnsafeTag_NodesDiscarded() {
 Element root = Jsoup.parse(HTML_WITH_UNSAFE_TAGS).body();
 Element destination = new Element(Tag.BODY);
 int numDiscarded = cleaner.copySafeNodes(root, destination);

 assertEquals(1, numDiscarded);
 }

 @Test
 public void testCreateSafeElement_SafeTagWithSafeAttributes_NoAttributesDiscarded() {
 Element sourceEl = Jsoup.parse(HTML_WITH_SAFE_TAGS).body().getElementsByTag("h1").first();
 ElementMeta meta = cleaner.createSafeElement(sourceEl);

 assertEquals(sourceEl.tagName(), meta.el.tagName());
 assertEquals(0, meta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElement_SafeTagWithInvalidAttributes_AttributesDiscarded() {
 Element sourceEl = Jsoup.parse(HTML_WITH_UNSAFE_TAGS_AND_VALID_ATTRIBUTES).body().getElementsByTag("h1").first();
 ElementMeta meta = cleaner.createSafeElement(sourceEl);

 assertEquals(sourceEl.tagName(), meta.el.tagName());
 assertEquals(1, meta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElement_UnsafeTag_NodesAndAttributesDiscarded() {
 Element sourceEl = Jsoup.parse(HTML_WITH_UNSAFE_TAGS).body().getElementsByTag("script").first();
 ElementMeta meta = cleaner.createSafeElement(sourceEl);

 assertEquals(Tag.SCRIPT.name(), meta.el.tagName());
 assertEquals(1, meta.numAttribsDiscarded);
 }

 @Test
 public void testIsWhiteListAllowed_AllowedTag() {
 String tagName = "h1";
 boolean isAllowed = cleaner.isWhiteListAllowed(tagName);

 assertTrue(isAllowed);
 }

 @Test
 public void testIsWhiteListAllowed_DisallowedTag() {
 String tagName = "script";
 boolean isAllowed = cleaner.isWhiteListAllowed(tagName);

 assertFalse(isAllowed);
 }

 @Test
 public void testIsWhiteListAllowed_AllowedAttribute() {
 String tagName = "h1";
 String attributeName = "id";
 String attributeValue = "title";
 boolean isAllowed = cleaner.isWhiteListAllowed(tagName, null, attributeName, attributeValue);

 assertTrue(isAllowed);
 }

 @Test
 public void testIsWhiteListAllowed_DisallowedAttribute() {
 String tagName = "h1";
 String attributeName = "onclick";
 String attributeValue = "alert('XSS')";
 boolean isAllowed = cleaner.isWhiteListAllowed(tagName, null, attributeName, attributeValue);

 assertFalse(isAllowed);
 }

 private static class ElementMeta {
 Element el;
 int numAttribsDiscarded;

 ElementMeta(Element el, int numAttribsDiscarded) {
 this.el = el;
 this.numAttribsDiscarded = numAttribsDiscarded;
 }
 }
}