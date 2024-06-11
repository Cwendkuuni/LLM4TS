import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationCreation() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
 assertNotNull(xmlDeclaration);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 }

 @Test
 public void testXmlDeclarationCreationWithProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
 assertNotNull(xmlDeclaration);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 }

 @Test
 public void testXmlDeclarationCreationWithNameAndVersion() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
 xmlDeclaration.attributes().put("version", "1.0");
 String expectedDeclaration = "<declaration version=\"1.0\">";
 assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationCreationWithNameEncodingAndVersion() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 xmlDeclaration.attributes().put("version", "1.0");
 String expectedDeclaration = "<declaration encoding=\"UTF-8\" version=\"1.0\">";
 assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
 }

 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationCreationWithNullName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, "http://example.com", false);
 }

 @Test
 public void testXmlDeclarationOuterHtmlHead() throws IOException {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 Document document = Jsoup.parse("<root>" + xmlDeclaration.outerHtml() + "</root>");
 assertEquals("<declaration encoding=\"UTF-8\" version=\"1.0\"></declaration>", document.select("root > declaration").text());
 }

 @Test
 public void testXmlDeclarationOuterHtmlTail() throws IOException {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
 Document document = Jsoup.parse("<root>" + xmlDeclaration.outerHtml() + "</root>");
 assertEquals("", document.select("root > declaration:last-child").text());
 }

 @Test
 public void testXmlDeclarationToString() throws IOException {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("<declaration encoding=\"UTF-8\" version=\"1.0\"></declaration>", xmlDeclaration.toString());
 }
}