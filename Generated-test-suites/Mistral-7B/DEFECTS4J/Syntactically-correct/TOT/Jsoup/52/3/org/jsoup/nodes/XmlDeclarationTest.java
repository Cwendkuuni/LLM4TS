import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationWithNameAndBaseUri() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals("<?xml>\n", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithNameEncodingAndBaseUri() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals("<?xml encoding=\"UTF-8\">\n", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithNameVersionAndBaseUri() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
 xmlDeclaration.attributes().put("version", "1.0");
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals("<?xml version=\"1.0\">\n", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithNameProcessingInstructionAndBaseUri() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals("<?xml>\n", xmlDeclaration.getWholeDeclaration());
 }

 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationWithNullName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, "http://example.com", false);
 }

 @Test
 public void testXmlDeclarationToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
 Document document = Jsoup.parse("<root>" + xmlDeclaration.toString() + "</root>");
 XmlDeclaration parsedXmlDeclaration = document.selectFirst("declaration");
 assertEquals("#declaration", parsedXmlDeclaration.nodeName());
 assertEquals("xml", parsedXmlDeclaration.name());
 assertEquals("<?xml>\n", parsedXmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithEmptyName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("", "http://example.com", false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertNull(xmlDeclaration.name());
 assertEquals("", xmlDeclaration.getWholeDeclaration());
 }
}