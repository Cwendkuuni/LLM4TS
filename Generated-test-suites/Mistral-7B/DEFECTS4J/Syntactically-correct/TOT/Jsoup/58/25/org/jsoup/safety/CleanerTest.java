package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import static org.junit.Assert.*;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = new Whitelist.SimpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 boolean isValid = cleaner.isValid(dirtyDocument);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertTrue(isValid);
 assertEquals("<body></body>", cleanDocument.body().html());
 }

 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<body><h1>Hello World</h1></body>", cleanDocument.body().html());
 }

 @Test
 public void testIsValidValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertTrue(isValid);
 }

 @Test
 public void testIsValidInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }

 @Test
 public void testCleanWithEmptyDocument() {
 Document dirtyDocument = Jsoup.parse("<html></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html></html>", cleanDocument.html());
 }

 @Test
 public void testCleanWithFramesetDocument() {
 Document dirtyDocument = Jsoup.parse("<html><frameset><frame src=\"about:blank\"></frameset></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><frameset></frameset></html>", cleanDocument.html());
 }

 @Test
 public void testCleanWithDocumentContainingScriptTag() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<body></body>", cleanDocument.body().html());
 }

 @Test
 public void testCleanWithDocumentContainingScriptElement() {
 Document dirtyDocument = Jsoup.parse("<html><body><script id=\"script\"></script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<body></body>", cleanDocument.body().html());
 }

 @Test
 public void testCleanWithDocumentContainingStylesheetLink() {
 Document dirtyDocument = Jsoup.parse("<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"></head><body><p>Hello World</p></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<body><p>Hello World</p></body>", cleanDocument.body().html());
 }

 @Test
 public void testCleanWithDocumentContainingStylesheetTag() {
 Document dirtyDocument = Jsoup.parse("<html><head><style type=\"text/css\">body { color: red; }</style></head><body><p>Hello World</p></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<body><p>Hello World</p></body>", cleanDocument.body().html());
 }

 @Test
 public void testCleanWithDocumentContainingMetaTag() {
 Document dirtyDocument = Jsoup.parse("<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></head><body><p>Hello World</p></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<body><p>Hello World</p></body>", cleanDocument.body().html());
 }

 @Test
 public void testCleanWithDocumentContainingUnknownTag() {
 Document dirtyDocument = Jsoup.parse("<html><body><zoo></zoo></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<body></body>", cleanDocument.body().html());
 }

 @Test
 public void testCleanWithDocumentContainingUnknownAttribute() {
 Document dirtyDocument = Jsoup.parse("<html><body><div data-xss=\"evil\"></div></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<body></body>", cleanDocument.body().html());
 }

 @Test
 public void testCleanWithDocumentContainingTagWithInvalidAttribute() {
 Document dirtyDocument = Jsoup.parse("<html><body><button onclick=\"alert('XSS')\">Click me</button></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<body><button></button></body>", cleanDocument.body().html());
 }

 @Test
 public void testCleanWithDocumentContainingTagWithSafeAttribute() {
 Document dirtyDocument = Jsoup.parse("<html><body><a href=\"https://www.google.com\">Google</a></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<body><a href=\"https://www.google.com\">Google</a></body>", cleanDocument.body().html());
 }

 @Test
 public void testCleanWithDocumentContainingTagWithEnforcedAttribute() {
 Document dirtyDocument = Jsoup.parse("<html><body><img src=\"image.jpg\" alt=\"Alternate Text\"></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<body><img src=\"image.jpg\" alt=\"Alternate Text\"></body>", cleanDocument.body().html());
 }
}