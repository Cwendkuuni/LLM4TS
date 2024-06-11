package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setUp() {
 whitelist = new Whitelist();
 cleaner = new Cleaner(whitelist);

 dirtyDocument = Parser.parseBodyFragment("<html><body><div id='test'><script>alert('xss')</script><p>Hello World</p></div></body></html>", null);
 cleanDocument = Document.createShell(dirtyDocument.baseUri());
 }

 @Test
 public void testCreateNewCleaner() {
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanDocument() {
 cleaner.clean(dirtyDocument);
 assertEquals("<html><body><p>Hello World</p></body></html>", cleanDocument.body().html());
 assertFalse(cleanDocument.body().child(0).hasText());
 assertNull(cleanDocument.body().child(0).child(0));
 }

 @Test
 public void testIsValidDocument() {
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test(expected = NullPointerException.class)
 public void testCleanerWithNullWhitelist() {
 assertThrows(NullPointerException.class, () -> new Cleaner(null));
 }

 @Test(expected = NullPointerException.class)
 public void testCleanDocumentWithNullDocument() {
 assertThrows(NullPointerException.class, () -> cleaner.clean(null));
 }

 @Test
 public void testCopySafeNodes() {
 Element source = dirtyDocument.body().child(0);
 Element destination = cleanDocument.body();
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 assertEquals(3, numDiscarded);
 }
}

class ElementMeta {
 Element el;
 int numAttribsDiscarded;

 ElementMeta(Element el, int numAttribsDiscarded) {
 this.el = el;
 this.numAttribsDiscarded = numAttribsDiscarded;
 }
}

class CleaningVisitor implements NodeVisitor {
 private int numDiscarded;
 private Element root;
 private Element destination;

 public CleaningVisitor(Element root, Element destination) {
 this.root = root;
 this.destination = destination;
 }

 @Override
 public void head(Node source, int depth) {
 if (source instanceof Element) {
 Element sourceEl = (Element) source;
 if (cleaner.whitelist.isSafeTag(sourceEl.tagName())) {
 ElementMeta meta = cleaner.cleaner.createSafeElement(sourceEl);
 Element destChild = meta.el;
 destination.appendChild(destChild);
 numDiscarded += meta.numAttribsDiscarded;
 destination = destChild;
 } else if (source != root) {
 numDiscarded++;
 }
 } else if (source instanceof TextNode) {
 TextNode sourceText = (TextNode) source;
 TextNode destText = new TextNode(sourceText.getWholeText(), source.baseUri());
 destination.appendChild(destText);
 } else if (source instanceof DataNode && cleaner.whitelist.isSafeTag(source.parent().nodeName())) {
 DataNode sourceData = (DataNode) source;
 DataNode destData = new DataNode(sourceData.getWholeData(), source.baseUri());
 destination.appendChild(destData);
 } else {
 numDiscarded++;
 }
 }

 @Override
 public void tail(Node source, int depth) {
 if (source instanceof Element && cleaner.whitelist.isSafeTag(source.nodeName())) {
 destination = destination.parent();
 }
 }
}