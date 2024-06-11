package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.simple();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanValidDocument() {
 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><head></head><body><h1></h1></body></html>", cleanDocument.html());
 assertNull(cleanDocument.head());
 }

 @Test
 public void testCleanInvalidDocument() {
 String html = "<html><head><script>alert('XSS');</script></head><body><h1>Hello World</h1></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 boolean isValid = cleaner.isValid(dirtyDocument);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertFalse(isValid);
 assertEquals("<html><head></head><body><h1></h1></body></html>", cleanDocument.html());
 assertNull(cleanDocument.head());
 }

 @Test
 public void testCleanWithCustomWhitelist() {
 Whitelist customWhitelist = Whitelist.simple();
 customWhitelist.addTags("p", "span");
 customWhitelist.addAttributes("p", "style");
 cleaner = new Cleaner(customWhitelist);

 String html = "<html><head><body><p style='color:red;'>Hello World<span>123</span></p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><head></head><body><p style='color:red;'><span></span></p></body></html>", cleanDocument.html());
 assertNull(cleanDocument.head());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullWhitelist() {
 cleaner = new Cleaner(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullDocument() {
 cleaner.clean(null);
 }
}