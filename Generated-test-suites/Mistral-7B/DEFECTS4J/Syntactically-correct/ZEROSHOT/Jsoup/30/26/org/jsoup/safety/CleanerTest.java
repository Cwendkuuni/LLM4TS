package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CleanerTest {

 private Cleaner cleaner;
 private Document dirtyDocument;
 private Document cleanDocument;
 private Whitelist whitelist;

 @BeforeEach
 public void setup() {
 // Initialize Cleaner with a whitelist
 whitelist = new Whitelist.SimpleTextWhitelist();
 cleaner = new Cleaner(whitelist);

 // Create a dirty document for testing
 Element body = new Element("body");
 body.text("This is a dirty document.");
 Element p = new Element("p");
 p.text("This is a test paragraph.");
 body.appendChild(p);
 dirtyDocument = new Document(new File(""), "http://example.com", body);
 }

 @Test
 public void testCleanDocument() {
 // Test cleaning a valid document
 cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("", cleanDocument.body().text(), "This is a test paragraph.");
 assertTrue(cleanDocument.body().getElementsByTag("body").isEmpty());
 assertTrue(cleanDocument.body().getElementsByTag("p").isPresent());
 }

 @Test
 public void testInvalidDocument() {
 // Test cleaning an invalid document
 Element malformedBody = new Element("body");
 Element script = new Element("script");
 script.src("http://malicious.site/script.js");
 malformedBody.appendChild(script);
 dirtyDocument.setBody(malformedBody);
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }

 @Test
 public void testIsValidDocument() {
 // Test validating a valid document
 cleanDocument = cleaner.clean(dirtyDocument);
 assertTrue(cleaner.isValid(cleanDocument));
 }

 @AfterEach
 public void teardown() {
 // Cleanup
 dirtyDocument = null;
 cleanDocument = null;
 cleaner = null;
 whitelist = null;
 }

}