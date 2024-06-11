package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {

 private static final String VALID_HTML = "<html><body><p id=\"p1\">Hello <b>World</b></p></body></html>";
 private static final String INVALID_HTML = "<html><body onload=\"alert('XSS');\"><p id=\"p1\">Hello <script>alert('XSS');</script></p></body></html>";

 @BeforeClass
 public static void setup() {
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCreateCleaner() {
 Cleaner cleaner = new Cleaner(null);
 assertNull(cleaner.whitelist);

 cleaner = new Cleaner(new Whitelist());
 assertNotNull(cleaner.whitelist);
 }

 @Test
 public void testCreateDocumentShell() {
 Document dirtyDocument = Jsoup.parseBodyFragment(VALID_HTML);
 Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
 assertNotNull(cleanDocument);
 assertNull(cleanDocument.head());
 assertNull(cleanDocument.title());
 assertNotNull(cleanDocument.body());
 }

 @Test
 public void testCleanDocument() {
 Document dirtyDocument = Jsoup.parseBodyFragment(VALID_HTML);
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals(VALID_HTML, cleanDocument.body().html());
 assertEquals("p", cleanDocument.body().tagName());
 assertNull(cleanDocument.body().head());
 assertNotNull(cleanDocument.body().getElementById("p1"));
 }

 @Test
 public void testIsValidDocument() {
 Document dirtyDocument = Jsoup.parseBodyFragment(VALID_HTML);
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());

 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidInvalidDocument() {
 Document dirtyDocument = Jsoup.parseBodyFragment(INVALID_HTML);
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());

 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCopySafeNodes() {
 Element sourceElement = Jsoup.parseBodyFragment(VALID_HTML).body().getElementById("p1");
 Element destElement = new Element(Tag.P);
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);

 assertEquals(VALID_HTML, destElement.html());
 assertEquals("p", destElement.tagName());
 assertNotNull(destElement.getElementById("p1"));
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesInvalidElement() {
 Element sourceElement = Jsoup.parseBodyFragment(INVALID_HTML).body();
 Element destElement = new Element(Tag.P);
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);

 assertEquals(0, numDiscarded);
 assertNull(destElement.html());
 }

 @Test
 public void testCopySafeNodesInvalidAttribute() {
 Element sourceElement = Jsoup.parseBodyFragment(VALID_HTML + "<p id=\"p1\" onload=\"alert('XSS')\" />").body().getElementById("p1");
 Element destElement = new Element(Tag.P);
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);

 assertEquals(1, numDiscarded);
 assertNull(destElement.html());
 }

 @Test
 public void testCopySafeNodesInvalidTag() {
 Element sourceElement = Jsoup.parseBodyFragment(INVALID_HTML).body();
 Element destElement = new Element(Tag.P);
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);

 assertEquals(1, numDiscarded);
 assertNull(destElement.html());
 }

 @Test
 public void testCreateSafeElement() {
 Element sourceElement = Jsoup.parseBodyFragment(VALID_HTML).body().getElementById("p1");
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

 assertEquals("p", elementMeta.el.tagName());
 assertEquals(0, elementMeta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementInvalidAttribute() {
 Element sourceElement = Jsoup.parseBodyFragment(VALID_HTML + "<p id=\"p1\" onload=\"alert('XSS')\" />").body().getElementById("p1");
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

 assertEquals("p", elementMeta.el.tagName());
 assertEquals(1, elementMeta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementInvalidTag() {
 Element sourceElement = Jsoup.parseBodyFragment(INVALID_HTML).body();
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

 assertEquals(null, elementMeta.el);
 }

 @Test
 public void testIsSafeTag() {
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());

 assertTrue(cleaner.whitelist.isSafeTag("p"));
 assertTrue(cleaner.whitelist.isSafeTag("b"));
 assertFalse(cleaner.whitelist.isSafeTag("script"));
 }

 @Test
 public void testIsSafeAttribute() {
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());

 assertTrue(cleaner.whitelist.isSafeAttribute("p", "id", "p1"));
 assertFalse(cleaner.whitelist.isSafeAttribute("p", "onload", "alert('XSS')"));
 }

 @Test
 public void testGetEnforcedAttributes() {
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());

 List<String> pTagAttributes = Arrays.asList("id");
 List<String> pTagEnforcedAttributes = cleaner.whitelist.getEnforcedAttributes("p");

 assertEquals(pTagAttributes, pTagEnforcedAttributes);
 }
}