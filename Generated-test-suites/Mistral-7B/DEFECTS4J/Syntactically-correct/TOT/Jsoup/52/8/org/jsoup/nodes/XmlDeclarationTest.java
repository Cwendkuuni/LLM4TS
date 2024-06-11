import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationWithValidArguments() {
 String name = "xml";
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = false;
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals(name, xmlDeclaration.name());
 assertEquals("<?xml>\n", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithProcessingInstruction() {
 String name = "xml";
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = true;
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals(name, xmlDeclaration.name());
 assertEquals("<?xml>\n", xmlDeclaration.getWholeDeclaration());
 try {
 xmlDeclaration.outerHtmlHead(null, 0, null);
 assertEquals("<!<?xml>\n", xmlDeclaration.outerHtml());
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException was thrown");
 }
 }

 @Test
 public void testXmlDeclarationWithVersionAttribute() {
 String name = "xml";
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = false;
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
 xmlDeclaration.attributes().put("version", "1.0");
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals(name, xmlDeclaration.name());
 assertEquals("<?xml version=\"1.0\">\n", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithEncodingAttribute() {
 String name = "xml";
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = false;
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals(name, xmlDeclaration.name());
 assertEquals("<?xml encoding=\"UTF-8\">\n", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithVersionAndEncodingAttributes() {
 String name = "xml";
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = false;
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals(name, xmlDeclaration.name());
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\">\n", xmlDeclaration.getWholeDeclaration());
 }

 @Test(expected = NullPointerException.class)
 public void testNullNameArgument() {
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = false;
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, baseUri, isProcessingInstruction);
 fail("NullPointerException was not thrown");
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUriArgument() {
 String name = "xml";
 boolean isProcessingInstruction = false;
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, null, isProcessingInstruction);
 fail("NullPointerException was not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyNameArgument() {
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = false;
 XmlDeclaration xmlDeclaration = new XmlDeclaration("", baseUri, isProcessingInstruction);
 fail("IllegalArgumentException was not thrown");
 }
}