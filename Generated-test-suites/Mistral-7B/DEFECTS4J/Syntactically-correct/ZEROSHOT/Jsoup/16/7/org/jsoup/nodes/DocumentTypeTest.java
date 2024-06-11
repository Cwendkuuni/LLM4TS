import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.helper.StringUtil;
import static org.junit.Assert.*;

import org.junit.Test;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("publicId", docType.attr("publicId"));
 assertEquals("systemId", docType.attr("systemId"));
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html", "", "", null);
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 StringBuilder accum = new StringBuilder();
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
 doc.appendDoc(docType);
 doc.setBaseUri("/base/path");
 doc.outputSettings().indentAmount(0);

 docType.outerHtmlHead(accum, 0, doc.outputSettings());

 assertEquals("<!DOCTYPE htmlPUBLIC \"publicId\" systemId>\r\n", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithPublicIdOnly() {
 StringBuilder accum = new StringBuilder();
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "publicId", "", null);
 doc.appendDoc(docType);
 doc.setBaseUri("/base/path");
 doc.outputSettings().indentAmount(0);

 docType.outerHtmlHead(accum, 0, doc.outputSettings());

 assertEquals("<!DOCTYPE htmlPUBLIC \"publicId\"\r\n", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithSystemIdOnly() {
 StringBuilder accum = new StringBuilder();
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "", "systemId", null);
 doc.appendDoc(docType);
 doc.setBaseUri("/base/path");
 doc.outputSettings().indentAmount(0);

 docType.outerHtmlHead(accum, 0, doc.outputSettings());

 assertEquals("<!DOCTYPE htmlSYSTEM systemId>\r\n", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithBothPublicIdAndSystemId() {
 StringBuilder accum = new StringBuilder();
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
 doc.appendDoc(docType);
 doc.setBaseUri("/base/path");
 doc.outputSettings().indentAmount(0);

 docType.outerHtmlHead(accum, 0, doc.outputSettings());

 assertEquals("<!DOCTYPE htmlPUBLIC \"publicId\" systemId>\r\n", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 StringBuilder accum = new StringBuilder();
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "", "", null);
 doc.appendDoc(docType);

 docType.outerHtmlTail(accum, 0, doc.outputSettings());

 assertTrue(accum.toString().isEmpty());
 }

 @Test
 public void testDocumentTypeToString() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "", "", null);
 doc.appendDoc(docType);

 assertEquals("<!DOCTYPE html>", docType.toString());
 }

 @Test
 public void testDocumentTypeWithElementChild() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "", "", null);
 Element childElement = new Element("html");
 doc.appendDoc(docType);
 doc.appendDoc(childElement);

 assertEquals("<!DOCTYPE html>\n<html></html>", doc.outerHtml());
 }
}