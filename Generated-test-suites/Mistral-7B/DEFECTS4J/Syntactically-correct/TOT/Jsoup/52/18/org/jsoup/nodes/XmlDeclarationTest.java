import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationCreation() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertNull(xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithVersion() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 xmlDeclaration.attributes().put("version", "1.0");
 assertEquals("xml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("xml encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithVersionAndEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml-pi", "baseUri", true);
 assertEquals("#processing-instruction", xmlDeclaration.nodeName());
 assertEquals("xml-pi", xmlDeclaration.name());
 assertEquals("<?xml-pi \"\"\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 String xml = xmlDeclaration.toString();
 Document document = Jsoup.parse(xml);
 Element xmlDeclarationElement = document.head().selectFirst("#declaration");
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclarationElement.outerHtml());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXmlDeclarationNullName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, "baseUri", false);
 }
}