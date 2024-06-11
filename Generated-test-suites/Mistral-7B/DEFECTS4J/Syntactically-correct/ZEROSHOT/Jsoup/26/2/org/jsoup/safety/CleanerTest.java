package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Elements;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.List;

/**
 * Test class for Cleaner class
 */
public class CleanerTest {

 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @BeforeClass
 public static void setupClass() {
 // Initialize the whitelist for testing
 whitelist = Whitelist.simpleText();
 }

 @Before
 public void setUp() {
 // Create a dirty document for testing
 String html = "<html><body><h1 id='title' class='title' onclick='alert(\"Hello\")'>Test</h1>" +
 " <p id='paragraph'>This is a <span style='color:red'>test</span></p>" +
 " <script src='http://example.com/malicious.js'></script></body></html>";
 dirtyDocument = Jsoup.parse(html);

 // Create a clean document
 cleanDocument = new Cleaner(whitelist).clean(dirtyDocument);
 }

 @Test
 public void testCreateCleaner() {
 // Test constructor
 Cleaner cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 @Test
 public void testClean() {
 // Test clean method
 Document expectedCleanDocument = Jsoup.parse("<html><body><h1 class='title'>Test</h1>" +
 " <p>This is a <span>test</span></p></body></html>");

 assertNotNull(cleanDocument);
 assertFalse(cleanDocument.bodyElements().isEmpty());
 Elements actualCleanElements = cleanDocument.bodyElements();
 Elements expectedCleanElements = expectedCleanDocument.bodyElements();

 // Compare the original dirty document with the cleaned document
 for (Element actualCleanElement : actualCleanElements) {
 Element expectedCleanElement = expectedCleanElements.get(actualCleanElement.elementIndex());

 assertEquals("Element names should be equal", expectedCleanElement.tagName(), actualCleanElement.tagName());
 assertEquals("Element text should be equal", expectedCleanElement.text(), actualCleanElement.text());

 List<Attribute> actualCleanAttributes = actualCleanElement.attributes();
 List<Attribute> expectedCleanAttributes = expectedCleanElement.attributes();

 // Compare attributes
 for (Attribute actualCleanAttribute : actualCleanAttributes) {
 Attribute expectedCleanAttribute = expectedCleanAttributes.get(actualCleanAttribute.attributeNameIndex());
 assertEquals("Attribute names should be equal", expectedCleanAttribute.attributeName(), actualCleanAttribute.attributeName());
 assertEquals("Attribute values should be equal", expectedCleanAttribute.attributeValue(), actualCleanAttribute.attributeValue());
 }
 }
 }

 @Test
 public void testIsValid() {
 // Test isValid method
 Document invalidDocument = Jsoup.parse("<html><body><h1 onclick='alert(\"Hello\")'>Test</h1></body></html>");

 assertFalse(new Cleaner(whitelist).isValid(invalidDocument));
 }

 @Test
 public void testCopySafeNodes() {
 // Test copySafeNodes method
 Element sourceElement = dirtyDocument.body().element(0);
 Element destElement = cleanDocument.body().element(0);
 int numDiscarded = new Cleaner(whitelist).copySafeNodes(sourceElement, destElement);

 assertEquals("Number of discarded elements should be equal", 1, numDiscarded);
 }

 @Test(expected = NullPointerException.class)
 public void testInvalidDocument() {
 // Test with null document
 new Cleaner(whitelist).clean(null);
 }

 @Test(expected = NullPointerException.class)
 public void testInvalidWhitelist() {
 // Test with null whitelist
 new Cleaner(null);
 }

}