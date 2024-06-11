package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Whitelist.AllowAllElementFilter;
import org.jsoup.safety.Whitelist.AllowAllAttributesFilter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setup() {
 whitelist = Whitelist.simpleText();
 whitelist.addTags("p", new AllowAllElementFilter());
 whitelist.addAttributes("p[*]", new AllowAllAttributesFilter());
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanNewDocument() {
 Document dirtyDocument = Document.createShell("http://example.com");
 Element p = new Element(Tag.P, "Test content.", dirtyDocument.baseUri());
 p.text("This is a test.");
 dirtyDocument.body().appendChild(p);

 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element cleanP = cleanDocument.body().getFirstElement();
 assertNotNull(cleanP);
 assertEquals(Tag.P, cleanP.tagName());
 assertEquals("Test content.", cleanP.text());
 }

 @Test
 public void testCleanInvalidDocument() {
 Document dirtyDocument = Document.createShell("http://example.com");
 Element script = new Element(Tag.SCRIPT, "alert('XSS');", dirtyDocument.baseUri());
 dirtyDocument.body().appendChild(script);

 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }

 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Document.createShell("http://example.com");
 Element p = new Element(Tag.P, "Test content.", dirtyDocument.baseUri());
 p.text("This is a test.");
 dirtyDocument.body().appendChild(p);

 boolean isValid = cleaner.isValid(dirtyDocument);
 assertTrue(isValid);
 }

 @Test
 public void testCleanWithComplexDocument() {
 Document dirtyDocument = Document.createShell("http://example.com");
 Element p1 = new Element(Tag.P, "Test content 1.", dirtyDocument.baseUri());
 p1.text("This is a test 1.");
 Element p2 = new Element(Tag.P, "Test content 2.", dirtyDocument.baseUri());
 p2.text("This is a test 2.");
 p2.attr("class", "test");
 p1.appendChild(p2);
 dirtyDocument.body().appendChild(p1);

 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 Element cleanP = cleanDocument.body().getFirstElement();
 assertEquals(Tag.P, cleanP.tagName());
 Element cleanP1 = cleanP.getFirstElement();
 assertEquals(Tag.P, cleanP1.tagName());
 assertEquals("Test content 1.", cleanP1.text());
 assertNull(cleanP1.getAttributes()); // No attributes should be present
 }
}