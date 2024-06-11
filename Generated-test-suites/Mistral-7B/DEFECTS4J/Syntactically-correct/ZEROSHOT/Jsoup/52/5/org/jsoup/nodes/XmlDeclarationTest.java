import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

 private XmlDeclaration xmlDeclaration;
 private Document document;

 @Before
 public void setup() {
 document = new Document();
 xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 document.appendChild(xmlDeclaration);
 }

 @Test
 public void testXmlDeclarationName() {
 assertEquals("xml", xmlDeclaration.name());
 }

 @Test
 public void testXmlDeclarationGetWholeDeclarationWithVersionAttribute() {
 xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 xmlDeclaration.attributes().put("version", "1.0");
 String expected = "<xml version=\"1.0\"></xml>";
 assertEquals(expected, xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationGetWholeDeclarationWithEncodingAttribute() {
 xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 String expected = "<xml encoding=\"UTF-8\"></xml>";
 assertEquals(expected, xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationGetWholeDeclarationWithVersionAndEncodingAttributes() {
 xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 String expected = "<xml version=\"1.0\" encoding=\"UTF-8\"></xml>";
 assertEquals(expected, xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationGetWholeDeclarationWithoutAttributes() {
 String expected = "<xml></xml>";
 assertEquals(expected, xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationOuterHtmlHead() throws IOException {
 Appendable accum = new StringBuilder();
 xmlDeclaration.outerHtmlHead(accum, 0, null);
 String expected = "<!DOCTYPE xml [<!-- default doctype -->]]>\n<xml>";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtmlTail() throws IOException {
 Appendable accum = new StringBuilder();
 xmlDeclaration.outerHtmlTail(accum, 0, null);
 assertEquals("", accum.toString());
 }

 @Test
 public void testXmlDeclarationToString() {
 assertEquals("<xml></xml>", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationNodeName() {
 assertEquals("#declaration", xmlDeclaration.nodeName());
 }

 @Test
 public void testXmlDeclarationIsProcessingInstruction() {
 assertFalse(xmlDeclaration.isProcessingInstruction());
 }

 @Test
 public void testCreateXmlProcessingInstruction() {
 XmlDeclaration xmlPI = new XmlDeclaration("xml-pi", "baseUri", true);
 assertEquals("#processing-instruction(xml-pi)", xmlPI.nodeName());
 assertEquals("<?xml-pi?>", xmlPI.getWholeDeclaration());
 }
}