import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document doc = new Document();
 DocumentType doctype = new DocumentType("html", "publicId", "systemId", doc.baseUri());

 assertEquals("#doctype", doctype.nodeName());
 assertTrue(doctype.hasAttr("name"));
 assertTrue(doctype.hasAttr("publicId"));
 assertTrue(doctype.hasAttr("systemId"));
 assertEquals("publicId", doctype.attr("publicId"));
 assertEquals("systemId", doctype.attr("systemId"));
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 Document doc = new Document();
 DocumentType doctype = new DocumentType("html", "", "", doc.baseUri());

 assertEquals("#doctype", doctype.nodeName());
 assertFalse(doctype.hasAttr("publicId"));
 assertFalse(doctype.hasAttr("systemId"));
 }

 @Test
 public void testNullName() {
 try {
 Document doc = new Document();
 DocumentType doctype = new DocumentType(null, "publicId", "systemId", doc.baseUri());
 fail("Exception expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testOuterHtmlHead() {
 Document doc = new Document();
 DocumentType doctype = new DocumentType("html", "publicId", "systemId", doc.baseUri());
 StringBuilder accum = new StringBuilder();
 doc.appendBody(new Element("html"), new Document.OutputSettings());
 doctype.outerHtmlHead(accum, 0, new Document.OutputSettings());

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId>\n", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithEmptyPublicId() {
 Document doc = new Document();
 DocumentType doctype = new DocumentType("html", "", "systemId", doc.baseUri());
 StringBuilder accum = new StringBuilder();
 doctype.outerHtmlHead(accum, 0, new Document.OutputSettings());

 assertEquals("<!DOCTYPE html>\n", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithEmptySystemId() {
 Document doc = new Document();
 DocumentType doctype = new DocumentType("html", "publicId", "", doc.baseUri());
 StringBuilder accum = new StringBuilder();
 doctype.outerHtmlHead(accum, 0, new Document.OutputSettings());

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \">\n", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithNullName() {
 try {
 Document doc = new Document();
 DocumentType doctype = new DocumentType(null, "publicId", "systemId", doc.baseUri());
 doctype.outerHtmlHead(accum, 0, new Document.OutputSettings());
 fail("Exception expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testOuterHtmlTail() {
 Document doc = new Document();
 DocumentType doctype = new DocumentType("html", "publicId", "systemId", doc.baseUri());
 StringBuilder accum = new StringBuilder();
 doctype.outerHtmlTail(accum, 0, new Document.OutputSettings());

 assertEquals("", accum.toString());
 }
}