package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setUp() throws Exception {
 whitelist = new Whitelist.SimpleText(); // create a simple whitelist for testing
 cleaner = new Cleaner(whitelist);
 Parser parser = new Parser.MalformedEncodingParser(); // use malformed encoding parser for testing
 dirtyDocument = parser.parse("<html><head><script>alert('XSS')</script></head><body><h1 id='test'>Hello <script>alert('XSS')</script></h1></body></html>");
 cleanDocument = Document.createShell(dirtyDocument.baseUri());
 }

 @Test
 public void testCreateCleanerWithNullWhitelist() {
 try {
 new Cleaner(null);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testCleanMethod() throws Exception {
 Document cleanedDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanedDocument);
 assertFalse(cleanedDocument.body().containsElement(dirtyDocument.body().selectFirst("script")));
 Element h1 = cleanedDocument.body().selectFirst("h1");
 assertNotNull(h1);
 assertEquals("Hello", h1.text());
 }

 @Test
 public void testIsValidMethodWithValidDocument() throws Exception {
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidMethodWithInvalidDocument() throws Exception {
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCleaningVisitor() {
 Element root = dirtyDocument.body();
 Element destination = new Element("div");
 CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);
 NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
 traversor.traverse(root);

 Element clonedElement = cleaningVisitor.destination.child(0);
 assertEquals("h1", clonedElement.tagName());
 assertTrue(clonedElement.tags().size() == 1);
 assertEquals("test", clonedElement.id());
 assertFalse(clonedElement.hasAttr("script"));
 List<String> attributes = Arrays.asList("id", "class");
 for (String attribute : attributes) {
 assertTrue(clonedElement.hasAttr(attribute));
 }
 }

 @Test
 public void testCreateSafeElement() {
 Element sourceElement = new Element("div", null);
 sourceElement.attr("id", "test");
 sourceElement.attr("class", "test");
 sourceElement.attr("script", "alert('XSS')");
 ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
 assertNotNull(elementMeta.el);
 assertEquals("div", elementMeta.el.tagName());
 assertTrue(elementMeta.el.tags().size() == 1);
 assertEquals("test", elementMeta.el.id());
 assertTrue(elementMeta.el.hasAttr("class"));
 assertFalse(elementMeta.el.hasAttr("script"));
 }

 @Test
 public void testCopySafeNodes() throws Exception {
 Element source = dirtyDocument.body();
 Element destination = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 assertEquals(2, numDiscarded);

 Element clonedElement = destination.child(0);
 assertEquals("h1", clonedElement.tagName());
 assertTrue(clonedElement.tags().size() == 1);
 assertFalse(clonedElement.hasText());
 assertTrue(cleanDocument.body().containsElement(clonedElement));
 }

 @Test
 public void testCopySafeNodesWithEmptyDocument() throws Exception {
 Element source = Document.createShell(cleanDocument.baseUri()).body();
 Element destination = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithScriptTagInParent() throws Exception {
 Element parent = new Element("script", null).text("alert('XSS')");
 Element source = new Element("div", null).append(new TextNode("Test"));
 source.parent().appendChild(parent);
 Element destination = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 assertEquals(1, numDiscarded);
 assertNull(cleanDocument.body().child(0));
 }

 @Test
 public void testCopySafeNodesWithDataNodeInParent() throws Exception {
 Element parent = new Element("script", null).text("alert('XSS')");
 Element source = new Element("div", null).append(new TextNode("Test"));
 Element dataNode = new DataNode("[1,2,3]");
 source.parent().appendChild(parent);
 source.parent().appendChild(dataNode);
 Element destination = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 assertEquals(2, numDiscarded);
 assertNull(cleanDocument.body().child(0));
 }

 @Test
 public void testCopySafeNodesWithCommentInParent() throws Exception {
 Element parent = new Comment("<!-- XSS -->");
 Element source = new Element("div", null).append(new TextNode("Test"));
 source.parent().appendChild(parent);
 Element destination = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 assertEquals(0, numDiscarded);
 assertTrue(cleanDocument.body().containsElement(source.clone()));
 }

 @Test
 public void testCopySafeNodesWithProcessingInstructionInParent() throws Exception {
 Element parent = new ProcessingInstruction("xml", "<?xml version='1.0'?>");
 Element source = new Element("div", null).append(new TextNode("Test"));
 source.parent().appendChild(parent);
 Element destination = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 assertEquals(0, numDiscarded);
 assertTrue(cleanDocument.body().containsElement(source.clone()));
 }

}