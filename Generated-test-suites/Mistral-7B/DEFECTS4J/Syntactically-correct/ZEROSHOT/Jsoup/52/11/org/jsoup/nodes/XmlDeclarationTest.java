import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Test;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationCreation() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 XmlDeclaration processingInstruction = new XmlDeclaration("?>", null, true);

 Assert.assertEquals("#declaration", xmlDeclaration.nodeName());
 Assert.assertEquals("#processing-instruction", processingInstruction.nodeName());
 }

 @Test
 public void testXmlDeclarationName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 XmlDeclaration processingInstruction = new XmlDeclaration("?>", null, true);

 Assert.assertEquals("xml", xmlDeclaration.name());
 Assert.assertNull(processingInstruction.name());
 }

 @Test
 public void testGetWholeDeclaration() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 XmlDeclaration xmlDeclarationWithVersion = new XmlDeclaration("xml", null, false, new Attributes() {{
 setAttribute("version", "1.0");
 }});
 XmlDeclaration xmlDeclarationWithEncoding = new XmlDeclaration("xml", null, false, new Attributes() {{
 setAttribute("encoding", "UTF-8");
 }});
 XmlDeclaration xmlDeclarationWithVersionAndEncoding = new XmlDeclaration("xml", null, false, new Attributes() {{
 setAttribute("version", "1.0");
 setAttribute("encoding", "UTF-8");
 }});
 XmlDeclaration processingInstruction = new XmlDeclaration("?!", null, true);

 Assert.assertEquals("xml", xmlDeclaration.getWholeDeclaration());
 Assert.assertEquals("xml version=\"1.0\"", xmlDeclarationWithVersion.getWholeDeclaration());
 Assert.assertEquals("xml encoding=\"UTF-8\"", xmlDeclarationWithEncoding.getWholeDeclaration());
 Assert.assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclarationWithVersionAndEncoding.getWholeDeclaration());
 Assert.assertEquals("", processingInstruction.getWholeDeclaration());
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 String expectedXmlDeclaration = "<declaration xmlversion=\"1.0\" encoding=\"UTF-8\">";

 Document document = new Document();
 Element rootElement = new Element("root");
 rootElement.outerHtmlHead(document.body(), 0, null);
 rootElement.appendChild(xmlDeclaration);
 document.close();

 String actualXmlDeclaration = document.body().html();

 Assert.assertEquals(expectedXmlDeclaration, actualXmlDeclaration);
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);

 Document document = new Document();
 Element rootElement = new Element("root");
 rootElement.outerHtmlHead(document.body(), 0, null);
 rootElement.appendChild(xmlDeclaration);
 rootElement.outerHtmlTail(document.body(), 0, null);
 document.close();

 String actualXmlDeclaration = document.body().html();
 String expectedXmlDeclaration = "<declaration xmlversion=\"1.0\" encoding=\"UTF-8\">";

 Assert.assertEquals(expectedXmlDeclaration, actualXmlDeclaration);
 }

 @Test
 public void testToString() throws IOException {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);

 Assert.assertEquals(xmlDeclaration.toString(), xmlDeclaration.outerHtml());
 }
}