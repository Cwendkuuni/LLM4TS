import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
 DocumentType htmlDoctype = document.documentType();

 assertEquals("#doctype", htmlDoctype.nodeName());
 assertEquals("html", htmlDoctype.attr("name"));
 assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", htmlDoctype.attr("publicId"));
 assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", htmlDoctype.attr("systemId"));
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 Document document = Jsoup.parse("<html></html>");
 DocumentType documentType = document.documentType();

 assertNull(documentType);
 }

 @Test
 public void testDocumentTypeOuterHtml() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
 DocumentType htmlDoctype = document.documentType();
 Element docTypeElement = document.createElement("#doctype");

 StringBuilder expectedAccum = new StringBuilder();
 htmlDoctype.outerHtmlHead(expectedAccum, 0, null);
 assertEquals("<!DOCTYPE", expectedAccum.toString());

 expectedAccum.setLength(0);
 htmlDoctype.outerHtmlTail(expectedAccum, 0, null);
 assertSame("", expectedAccum.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeOuterHtmlWithNullAccumulator() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
 DocumentType htmlDoctype = document.documentType();
 StringBuilder nullAccumulator = null;

 htmlDoctype.outerHtmlHead(nullAccumulator, 0, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeOuterHtmlWithNullDepth() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
 DocumentType htmlDoctype = document.documentType();

 htmlDoctype.outerHtmlHead(new StringBuilder(), -1, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeOuterHtmlWithNullOutputSettings() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
 DocumentType htmlDoctype = document.documentType();

 htmlDoctype.outerHtmlHead(new StringBuilder(), 0, null);
 }
}