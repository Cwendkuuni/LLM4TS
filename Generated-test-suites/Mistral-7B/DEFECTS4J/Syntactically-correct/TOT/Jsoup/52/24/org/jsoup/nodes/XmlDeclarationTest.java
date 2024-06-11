import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationCreation() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals(xmlDeclaration.toString(), "<declaration></declaration>");
 }

 @Test
 public void testXmlDeclarationWithAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals(xmlDeclaration.toString(), "<declaration version=\"1.0\" encoding=\"UTF-8\"/>");
 }

 @Test
 public void testXmlProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("<?xml", null, true);
 assertEquals("#processing-instruction", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals(xmlDeclaration.toString(), "<?xml?>");
 }

 @Test
 public void testXmlDeclarationWithEmptyName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("", null, false);
 assertNull(xmlDeclaration.name());
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals(xmlDeclaration.toString(), "<declaration></declaration>");
 }

 @Test
 public void testXmlDeclarationWithInvalidName() {
 try {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("invalid", null, false);
 } catch (IllegalArgumentException e) {
 assertEquals("Name cannot be null or empty: invalid", e.getMessage());
 }
 }

 @Test
 public void testXmlDeclarationToString() throws IOException {
 Document document = Jsoup.parse("<html><head><!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"><xml version=\"1.0\" encoding=\"UTF-8\"></xml></head></html>");
 Element xmlDeclarationElement = document.selectFirst("xml");
 XmlDeclaration xmlDeclaration = (XmlDeclaration) xmlDeclarationElement;
 assertEquals(xmlDeclaration.toString(), "<declaration version=\"1.0\" encoding=\"UTF-8\"/>");
 }

 @Test
 public void testXmlDeclarationOuterHtml() throws IOException {
 Document document = Jsoup.parse("<html><head><!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"><xml version=\"1.0\" encoding=\"UTF-8\"></xml></head></html>");
 String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n<declaration version=\"1.0\" encoding=\"UTF-8\"/>";
 Element xmlDeclarationElement = document.selectFirst("xml");
 XmlDeclaration xmlDeclaration = (XmlDeclaration) xmlDeclarationElement;
 StringBuilder accum = new StringBuilder();
 xmlDeclaration.outerHtmlHead(accum, 0, null);
 xmlDeclaration.outerHtmlTail(accum, 0, null);
 assertEquals(expected, accum.toString());
 }
}