package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test class for Cleaner.java
 */
public class CleanerTest {

 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setUp() {
 whitelist = new Whitelist.SimpleTextWhitelist();
 dirtyDocument = new Document("about:blank");
 cleanDocument = new Document("about:blank");
 }

 @Test
 public void testCleanValidDocument() {
 Element body = new Element("body");
 body.appendText("Hello, this is a test.");
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanedDocument = cleaner.clean(dirtyDocument);

 assertTrue(cleanedDocument.body().text().equals("Hello, this is a test."));
 }

 @Test
 public void testCleanInvalidDocumentWithScriptTag() {
 Element body = new Element("body");
 body.appendElement(new Element("script").attr("src", "evil.js"));
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanedDocument = cleaner.clean(dirtyDocument);

 assertTrue(cleanedDocument.body().children().size() == 0);
 }

 @Test
 public void testCleanInvalidDocumentWithMalformedScriptTag() {
 Element body = new Element("body");
 body.appendElement(new Element("scripet", null).attr("src", "evil.js"));
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanedDocument = cleaner.clean(dirtyDocument);

 assertTrue(cleanedDocument.body().children().size() == 0);
 }

 @Test
 public void testCleanInvalidDocumentWithInlineScript() {
 Element body = new Element("body");
 body.appendElement(new Element("script").text("alert('XSS')"));
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanedDocument = cleaner.clean(dirtyDocument);

 assertTrue(cleanedDocument.body().children().size() == 0);
 }

 @Test
 public void testCleanInvalidDocumentWithStyleTag() {
 Element body = new Element("body");
 body.appendElement(new Element("style").text("body {background-color: red}"));
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanedDocument = cleaner.clean(dirtyDocument);

 assertTrue(cleanedDocument.body().children().size() == 0);
 }

 @Test
 public void testCleanInvalidDocumentWithIframeTag() {
 Element body = new Element("body");
 body.appendElement(new Element("iframe", null).attr("src", "evil.com"));
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanedDocument = cleaner.clean(dirtyDocument);

 assertTrue(cleanedDocument.body().children().size() == 0);
 }

 @Test
 public void testCleanInvalidDocumentWithUnknownTag() {
 Element body = new Element("body");
 body.appendElement(new Element("xhtml", null).attr("data-test", "test"));
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanedDocument = cleaner.clean(dirtyDocument);

 assertTrue(cleanedDocument.body().children().size() == 0);
 }

 @Test
 public void testIsValidValidDocument() {
 Element body = new Element("body");
 body.appendText("Hello, this is a test.");
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertTrue(isValid);
 }

 @Test
 public void testIsValidInvalidDocumentWithScriptTag() {
 Element body = new Element("body");
 body.appendElement(new Element("script").attr("src", "evil.js"));
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertFalse(isValid);
 }

 @Test
 public void testIsValidInvalidDocumentWithMalformedScriptTag() {
 Element body = new Element("body");
 body.appendElement(new Element("scripet", null).attr("src", "evil.js"));
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertFalse(isValid);
 }

 @Test
 public void testIsValidInvalidDocumentWithInlineScript() {
 Element body = new Element("body");
 body.appendElement(new Element("script").text("alert('XSS')"));
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertFalse(isValid);
 }

 @Test
 public void testIsValidInvalidDocumentWithStyleTag() {
 Element body = new Element("body");
 body.appendElement(new Element("style").text("body {background-color: red}"));
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertFalse(isValid);
 }

 @Test
 public void testIsValidInvalidDocumentWithIframeTag() {
 Element body = new Element("body");
 body.appendElement(new Element("iframe", null).attr("src", "evil.com"));
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertFalse(isValid);
 }

 @Test
 public void testIsValidInvalidDocumentWithUnknownTag() {
 Element body = new Element("body");
 body.appendElement(new Element("xhtml", null).attr("data-test", "test"));
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertFalse(isValid);
 }

 @Test
 public void testIsValidValidDocumentWithEnforcedAttributes() {
 Element body = new Element("body");
 body.appendElement(new Element("input").attr("type", "text").attr("disabled", "disabled"));
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertTrue(isValid);
 }

 @Test
 public void testIsValidInvalidDocumentWithInvalidAttribute() {
 Element body = new Element("body");
 body.appendElement(new Element("input").attr("type", "text").attr("onclick", "alert('XSS')"));
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertFalse(isValid);
 }

 @Test
 public void testIsValidInvalidDocumentWithInvalidAttribute2() {
 Element body = new Element("body");
 body.appendElement(new Element("input").attr("type", "text").attr("xss", "alert('XSS')"));
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertFalse(isValid);
 }

 @Test
 public void testIsValidValidDocumentWithEmptyDocument() {
 Element body = new Element("body");
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertTrue(isValid);
 }

 @Test
 public void testIsValidValidDocumentWithEmptyBody() {
 dirtyDocument.setBody(null);

 Cleaner cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertTrue(isValid);
 }

}