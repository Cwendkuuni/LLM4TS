import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

 private XmlDeclaration xmlDeclaration;
 private Document document;

 @Before
 public void setUp() {
 document = new Document();
 xmlDeclaration = new XmlDeclaration("xml", "baseURI", false);
 }

 @Test
 public void testXmlDeclarationName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseURI", false);
 assertEquals("xml", xmlDeclaration.name());
 }

 @Test
 public void testXmlDeclarationGetWholeDeclaration() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseURI", false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\"/>", xmlDeclaration.getWholeDeclaration());

 xmlDeclaration = new XmlDeclaration("xml", "baseURI", true);
 assertEquals("<?xml?>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationOuterHtmlHead() throws IOException {
 Appendable accum = new StringBuilder();
 xmlDeclaration.outerHtmlHead(accum, 0, null);
 assertEquals("<!xml version=\"1.0\" encoding=\"UTF-8\">", accum.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtmlTail() throws IOException {
 Appendable accum = new StringBuilder();
 xmlDeclaration.outerHtmlTail(accum, 0, null);
 assertTrue(accum.toString().isEmpty());
 }

 @Test
 public void testXmlDeclarationToString() {
 assertEquals("<!xml version=\"1.0\" encoding=\"UTF-8\">", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationWithAttributes() {
 xmlDeclaration = new XmlDeclaration("xml", "baseURI", false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 Element root = new Element("root");
 root.appendText("Test content");
 document.appendChild(xmlDeclaration);
 document.appendChild(root);

 assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\nTest content\n</root>\n</xml>", document.outerHtml());
 }
}