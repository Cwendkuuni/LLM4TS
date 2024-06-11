import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlDeclarationTest {

 @Test
 public void testCreateXmlDeclaration() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertNull(xmlDeclaration.getWholeDeclaration());

 XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", null, true);
 assertEquals("#declaration", xmlProcessingInstruction.nodeName());
 assertEquals("xml", xmlProcessingInstruction.name());
 assertEquals("<?xml>", xmlProcessingInstruction.getWholeDeclaration());
 }

 @Test
 public void testCreateXmlDeclarationWithAttributes() {
 Map<String, Attribute> attributes = new HashMap<>();
 attributes.put(new Attribute("version", "1.0"), null);
 attributes.put(new Attribute("encoding", "UTF-8"), null);

 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false, attributes);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testCreateXmlDeclarationWithEmptyName() {
 try {
 new XmlDeclaration(null, null, false);
 assertFalse("Exception expected", true);
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 new XmlDeclaration(null, null, true);
 assertFalse("Exception expected", true);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 Document document = Jsoup.parse("<html><head><![CDATA[<xml/>]]></head><body></body></html>");
 XmlDeclaration xmlDeclaration = document.getElementsByTag("xml").first();
 String expected = "<?xml?>";
 StringBuilder actual = new StringBuilder();
 xmlDeclaration.outerHtmlHead(actual, 0, null);
 assertEquals(expected, actual.toString());
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 Document document = Jsoup.parse("<html><head><![CDATA[<xml/>]]></head><body></body></html>");
 XmlDeclaration xmlDeclaration = document.getElementsByTag("xml").first();
 assertEquals("", xmlDeclaration.outerHtmlTail(null, 0, null).toString());
 }

 @Test
 public void testToString() throws IOException {
 Document document = Jsoup.parse("<html><head><![CDATA[<xml/>]]></head><body></body></html>");
 XmlDeclaration xmlDeclaration = document.getElementsByTag("xml").first();
 String expected = xmlDeclaration.outerHtml();
 assertEquals(expected, xmlDeclaration.toString());
 }

 @Test
 public void testGetWholeDeclarationWithEmptyName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("", null, false);
 assertEquals("", xmlDeclaration.getWholeDeclaration());
 }
}