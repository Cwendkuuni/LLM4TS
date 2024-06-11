package org.jsoup.nodes;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc);
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testEmptyDocumentTitle() {
 Document doc = new Document("http://example.com");
 assertEquals("", doc.title());
 }

 @Test
 public void testDocumentTitle() {
 Document doc = Document.createShell("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element link = doc.createElement("a");
 assertNotNull(link);
 assertEquals("a", link.nodeName());
 }

 @Test
 public void testNormaliseDocument() throws IOException {
 Document doc = Jsoup.parse("<html><head><title>Test</title></head></html>", "http://example.com");
 doc.normalise();
 Element body = doc.body();
 Element title = doc.title();

 assertEquals("<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>", doc.outerHtml());
 assertEquals("Test", title.text());
 assertNotNull(body);
 assertFalse(body.text().isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyBaseUri() {
 Document doc = new Document("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Document doc = new Document("http://example.com");
 try {
 doc.createElement("invalidTag");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testTitleTrimming() {
 Document doc = Document.createShell("http://example.com");
 doc.title(" Test Title ");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testTitleSettingWithExistingTitle() {
 Document doc = Document.createShell("http://example.com");
 doc.title("Test Title");
 doc.title("New Title");
 assertEquals("New Title", doc.title());
 }
}