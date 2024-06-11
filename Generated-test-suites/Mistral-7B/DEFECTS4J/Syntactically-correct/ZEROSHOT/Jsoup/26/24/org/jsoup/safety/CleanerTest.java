package org.jsoup.safety;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import java.util.Arrays;
import java.util.List;

/**
 * Test file for Cleaner class
 */
public class CleanerTest {

 private static final String HTML_SAMPLE = "<html><body><div id='test'><p>Hello <script>alert('XSS')</script> World</p>" +
         "<p id='anotherTest'>This is a test <img src='image.jpg' alt='Image'></p></div></body></html>";

 /**
 * Test constructor
 */
 @org.junit.Test
 public void testConstructor() {
     Whitelist whitelist = Whitelist.simpleTextOnly();
     Cleaner cleaner = new Cleaner(whitelist);
     assertNotNull(cleaner);
 }

 /**
 * Test clean method with a simple text only whitelist
 */
 @org.junit.Test
 public void testCleanWithSimpleTextOnlyWhitelist() {
     Whitelist whitelist = Whitelist.simpleTextOnly();
     Cleaner cleaner = new Cleaner(whitelist);
     Document dirtyDocument = Jsoup.parse(HTML_SAMPLE);
     Document cleanDocument = cleaner.clean(dirtyDocument);
     assertEquals("<html><body><div id='test'><p>Hello  </p><p id='anotherTest'>This is a test </p></div></body></html>", cleanDocument.html());
 }

 /**
 * Test isValid method with a simple text only whitelist
 */
 @org.junit.Test
 public void testIsValidWithSimpleTextOnlyWhitelist() {
     Whitelist whitelist = Whitelist.simpleTextOnly();
     Cleaner cleaner = new Cleaner(whitelist);
     Document dirtyDocument = Jsoup.parse(HTML_SAMPLE);
     assertTrue(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test clean method with a basic whitelist
 */
 @org.junit.Test
 public void testCleanWithBasicWhitelist() {
     Whitelist whitelist = Whitelist.basic();
     Cleaner cleaner = new Cleaner(whitelist);
     Document dirtyDocument = Jsoup.parse(HTML_SAMPLE);
     Document cleanDocument = cleaner.clean(dirtyDocument);
     assertEquals("<html><body><div id='test'><p>Hello </p><p id='anotherTest'>This is a test <img alt='Image' src='image.jpg'></p></div></body></html>", cleanDocument.html());
 }

 /**
 * Test isValid method with a basic whitelist
 */
 @org.junit.Test
 public void testIsValidWithBasicWhitelist() {
     Whitelist whitelist = Whitelist.basic();
     Cleaner cleaner = new Cleaner(whitelist);
     Document dirtyDocument = Jsoup.parse(HTML_SAMPLE);
     assertTrue(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test clean method with an invalid whitelist
 */
 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testCleanWithInvalidWhitelist() {
     Whitelist whitelist = new Whitelist() {}; // empty constructor, this is an invalid whitelist
     Cleaner cleaner = new Cleaner(whitelist);
     Document dirtyDocument = Jsoup.parse(HTML_SAMPLE);
     cleaner.clean(dirtyDocument); // exception should be thrown here
 }

 /**
 * Test copySafeNodes method with a simple text only whitelist
 */
 @org.junit.Test
 public void testCopySafeNodesWithSimpleTextOnlyWhitelist() {
     Whitelist whitelist = Whitelist.simpleTextOnly();
     Cleaner cleaner = new Cleaner(whitelist);
     Element dirtyElement = Jsoup.parseBodyFragment("<script>alert('XSS')</script>").first();
     Element cleanElement = new Element(Tag.valueOf("span"), dirtyElement.baseUri(), new Attributes());
     int numDiscarded = cleaner.copySafeNodes(dirtyElement, cleanElement);
     assertEquals(1, numDiscarded);
     assertNull(cleanElement.children());
 }

 /**
 * Test copySafeNodes method with a basic whitelist
 */
 @org.junit.Test
 public void testCopySafeNodesWithBasicWhitelist() {
     Whitelist whitelist = Whitelist.basic();
     Cleaner cleaner = new Cleaner(whitelist);
     Document dirtyDocument = Jsoup.parse(HTML_SAMPLE);
     Element cleanElement = new Element(Tag.valueOf("div"), dirtyDocument.baseUri(), new Attributes());
     int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanElement);
     assertEquals(2, numDiscarded);
     List<Node> cleanChildNodes = cleanElement.childNodes();
     assertEquals(2, cleanChildNodes.size());
     assertEquals("Hello", cleanChildNodes.get(0).text());
     assertEquals("This is a test", cleanChildNodes.get(1).text());
 }
}