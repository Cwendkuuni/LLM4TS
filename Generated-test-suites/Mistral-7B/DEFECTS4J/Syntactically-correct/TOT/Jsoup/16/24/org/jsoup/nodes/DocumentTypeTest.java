import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;

public class DocumentTypeTest {

 // Test case for constructor
 @Test
 public void testDocumentTypeConstructor() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", "/baseUri");
 Assert.assertEquals("#doctype", docType.nodeName());
 Assert.assertEquals("html", docType.attr("name"));
 Assert.assertEquals("publicId", docType.attr("publicId"));
 Assert.assertEquals("systemId", docType.attr("systemId"));
 Assert.assertEquals("/baseUri", docType.baseUri());
 }

 // Test case for nodeName method
 @Test
 public void testNodeName() {
 Document doc = Jsoup.parse("<!DOCTYPE html>\n<html></html>");
 DocumentType docType = doc.docType();
 Assert.assertEquals("#doctype", docType.nodeName());
 }

 // Test case for outerHtmlHead method with publicId and systemId
 @Test
 public void testOuterHtmlHeadWithPublicIdAndSystemId() {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"systemId\">");
 DocumentType docType = doc.docType();
 StringBuilder expected = new StringBuilder();
 expected.append("<!DOCTYPE html PUBLIC ");
 Attributes attrs = docType.attributes();
 for (String name : attrs.keySet()) {
 expected.append("\"");
 expected.append(name);
 expected.append("=\"");
 expected.append(attrs.get(name));
 expected.append("\"");
 }
 expected.append('>');
 StringBuilder actual = new StringBuilder();
 docType.outerHtmlHead(actual, 0, null);
 Assert.assertEquals(expected.toString(), actual.toString());
 }

 // Test case for outerHtmlHead method with no publicId and systemId
 @Test
 public void testOuterHtmlHeadWithNoPublicIdAndSystemId() {
 Document doc = Jsoup.parse("<!DOCTYPE html>");
 DocumentType docType = doc.docType();
 StringBuilder expected = new StringBuilder();
 expected.append("<!DOCTYPE html>");
 StringBuilder actual = new StringBuilder();
 docType.outerHtmlHead(actual, 0, null);
 Assert.assertEquals(expected.toString(), actual.toString());
 }

 // Test case for outerHtmlTail method
 @Test
 public void testOuterHtmlTail() {
 Document doc = Jsoup.parse("<!DOCTYPE html>\n<html></html>");
 DocumentType docType = doc.docType();
 StringBuilder expected = new StringBuilder();
 StringBuilder actual = new StringBuilder();
 docType.outerHtmlTail(actual, 0, null);
 Assert.assertEquals("", actual.toString());
 }
}