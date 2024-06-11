package org.jsoup.safety;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleTextOnly();
 cleaner = new Cleaner(whitelist);
 }

 @After
 public void tearDown() {
 cleaner = null;
 whitelist = null;
 }

 @Test
 public void testCleanNullDocument() {
 Document dirtyDocument = null;
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNull(cleanDocument);
 }

 @Test
 public void testCleanEmptyDocument() {
 Document dirtyDocument = Jsoup.parseBodyElements("");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("", cleanDocument.body().text());
 }

 @Test
 public void testCleanValidDocument() {
 String html = "<html><body><p>Hello World</p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<p>Hello World</p>", cleanDocument.body().html());
 }

 @Test
 public void testCleanInvalidDocumentWithScript() {
 String html = "<html><body onload='alert(\"XSS\")'><script>alert(\"XSS\")</script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<body></body>", cleanDocument.body().html());
 }

 @Test
 public void testCleanInvalidDocumentWithStyle() {
 String html = "<html><body style='background:url(\"malware.com\")'><script>alert(\"XSS\")</script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<body></body>", cleanDocument.body().html());
 }

 @Test
 public void testCleanInvalidDocumentWithUnknownTag() {
 String html = "<html><body><zork></zork></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<body></body>", cleanDocument.body().html());
 }

 @Test
 public void testIsValidNullDocument() {
 Document dirtyDocument = null;
 boolean valid = cleaner.isValid(dirtyDocument);
 assertFalse(valid);
 }

 @Test
 public void testIsValidEmptyDocument() {
 Document dirtyDocument = Jsoup.parseBodyElements("");
 boolean valid = cleaner.isValid(dirtyDocument);
 assertTrue(valid);
 }

 @Test
 public void testIsValidValidDocument() {
 String html = "<html><body><p>Hello World</p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 boolean valid = cleaner.isValid(dirtyDocument);
 assertTrue(valid);
 }

 @Test
 public void testIsValidInvalidDocumentWithScript() {
 String html = "<html><body onload='alert(\"XSS\")'><script>alert(\"XSS\")</script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 boolean valid = cleaner.isValid(dirtyDocument);
 assertFalse(valid);
 }

 @Test
 public void testIsValidInvalidDocumentWithStyle() {
 String html = "<html><body style='background:url(\"malware.com\")'><script>alert(\"XSS\")</script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 boolean valid = cleaner.isValid(dirtyDocument);
 assertFalse(valid);
 }

 @Test
 public void testIsValidInvalidDocumentWithUnknownTag() {
 String html = "<html><body><zork></zork></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 boolean valid = cleaner.isValid(dirtyDocument);
 assertFalse(valid);
 }

}