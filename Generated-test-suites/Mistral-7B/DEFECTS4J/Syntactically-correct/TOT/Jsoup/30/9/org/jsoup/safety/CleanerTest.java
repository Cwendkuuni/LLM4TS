package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

 private Whitelist allowedTagsWhitelist = Whitelist.simpleTextOnlyWhitelist();
 private Whitelist disallowedTagsWhitelist = Whitelist.simpleMaliciousWhitelist();

 @Test
 public void testCleanMethodWithAllowedTags() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World!</h1></body></html>");
 Cleaner cleaner = new Cleaner(allowedTagsWhitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element body = cleanDocument.body();
 assertEquals("Hello World!", body.text());
 }

 @Test
 public void testCleanMethodWithDisallowedTags() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 Cleaner cleaner = new Cleaner(allowedTagsWhitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element body = cleanDocument.body();
 assertTrue(body.text().isEmpty());
 }

 @Test
 public void testIsValidMethodWithAllowedTags() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World!</h1></body></html>");
 Cleaner cleaner = new Cleaner(allowedTagsWhitelist);
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidMethodWithDisallowedTags() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 Cleaner cleaner = new Cleaner(allowedTagsWhitelist);
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCleanMethodWithInvalidAttributes() {
 Document dirtyDocument = Jsoup.parse("<html><body><div id='myDiv' onclick='alert(\"XSS\");'></div></body></html>");
 Cleaner cleaner = new Cleaner(allowedTagsWhitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element body = cleanDocument.body();
 Element div = body.getElementById("myDiv");
 assertNull(div.attributes().getNamedItem("onclick"));
 }

 @Test
 public void testCleanMethodWithValidAttributes() {
 Document dirtyDocument = Jsoup.parse("<html><body><div id='myDiv' class='myClass'></div></body></html>");
 Cleaner cleaner = new Cleaner(allowedTagsWhitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element body = cleanDocument.body();
 Element div = body.getElementById("myDiv");
 assertEquals("myClass", div.tagName());
 assertNotNull(div.attributes().getNamedItem("id"));
 }

 @Test
 public void testCleanMethodWithEmptyDocument() {
 Document dirtyDocument = Jsoup.parse(" ");
 Cleaner cleaner = new Cleaner(allowedTagsWhitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertTrue(cleanDocument.text().isEmpty());
 }

 @Test
 public void testCleanMethodWithNullDocument() {
 Cleaner cleaner = new Cleaner(allowedTagsWhitelist);
 assertNull(cleaner.clean(null));
 }

 @Test
 public void testIsValidMethodWithNullDocument() {
 Cleaner cleaner = new Cleaner(allowedTagsWhitelist);
 assertFalse(cleaner.isValid(null));
 }

 @Test
 public void testCleanMethodWithMaliciousWhiteList() {
 Cleaner cleaner = new Cleaner(disallowedTagsWhitelist);
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World!</h1></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element body = cleanDocument.body();
 assertTrue(body.text().isEmpty());
 }
}