import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setUp() {
 document = new Document();
 }

 @Test
 public void testDocumentTypeCreation() {
 String name = "html";
 String publicId = "‐‐‐‐‐‐‐‐";
 String systemId = "systemId";
 String baseUri = "http://example.com";
 DocumentType htmlDoctype = new DocumentType(name, publicId, systemId, baseUri);
 assertEquals("#doctype", htmlDoctype.nodeName());
 assertEquals(name, htmlDoctype.attr(NAME));
 assertEquals(publicId, htmlDoctype.attr(PUBLIC_ID));
 assertEquals(systemId, htmlDoctype.attr(SYSTEM_ID));
 assertEquals(baseUri, htmlDoctype.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithEmptyAttributes() {
 DocumentType emptyDoctype = new DocumentType("", "", "", "");
 assertEquals("#doctype", emptyDoctype.nodeName());
 assertNull(emptyDoctype.attr(NAME));
 assertNull(emptyDoctype.attr(PUBLIC_ID));
 assertNull(emptyDoctype.attr(SYSTEM_ID));
 assertNull(emptyDoctype.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithAllAttributes() {
 String name = "html";
 String publicId = "‐‐‐‐‐‐‐‐";
 String systemId = "systemId";
 String baseUri = "http://example.com";
 DocumentType htmlDoctype = new DocumentType(name, publicId, systemId, baseUri);

 Element html = document.createElement("html");
 Attributes htmlAttributes = html.attributes();
 htmlAttributes.put("xmlns:html", "http://www.w3.org/1999/xhtml");
 DocumentType html5Doctype = new DocumentType(name, publicId, systemId, baseUri, html);

 assertEquals("#doctype", html5Doctype.nodeName());
 assertEquals(name, html5Doctype.attr(NAME));
 assertEquals(publicId, html5Doctype.attr(PUBLIC_ID));
 assertEquals(systemId, html5Doctype.attr(SYSTEM_ID));
 assertEquals(baseUri, html5Doctype.baseUri());
 assertEquals(html, html5Doctype.parentNode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeCreationWithNullBaseUri() {
 String name = "html";
 String publicId = "‐‐‐‐‐‐‐‐";
 String systemId = "systemId";
 DocumentType htmlDoctype = new DocumentType(name, publicId, systemId, null);
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 DocumentType htmlDoctype = new DocumentType("html", "‐‐‐‐‐‐‐‐", "systemId", "http://example.com");
 Appendable accumulator = new StringBuilder();
 document.setOutputSettings(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 htmlDoctype.outerHtmlHead(accumulator, 0, document.outputSettings());
 assertEquals("<!doctype html>\n", accumulator.toString());
 }

 @Test
 public void testOuterHtmlHeadWithNoPublicIdAndNoSystemId() throws IOException {
 DocumentType htmlDoctype = new DocumentType("html", "", "", "http://example.com");
 Appendable accumulator = new StringBuilder();
 document.setOutputSettings(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 htmlDoctype.outerHtmlHead(accumulator, 0, document.outputSettings());
 assertEquals("<!doctype html>\n", accumulator.toString());
 }

 @Test
 public void testOuterHtmlHeadWithPublicIdAndSystemId() throws IOException {
 DocumentType htmlDoctype = new DocumentType("html", "‐‐‐‐‐‐‐‐", "systemId", "http://example.com");
 Appendable accumulator = new StringBuilder();
 document.setOutputSettings(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 htmlDoctype.outerHtmlHead(accumulator, 0, document.outputSettings());
 assertEquals("<!DOCTYPE html PUBLIC \"‐‐‐‐‐‐‐‐\" systemId>\n", accumulator.toString());
 }
}