import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationWithNameAndVersion() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.0");
 String expectedDeclaration = "<declaration version=\"1.0\">";
 assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithNameEncodingAndVersion() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 String expectedDeclaration = "<declaration version=\"1.0\" encoding=\"UTF-8\">";
 assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithEmptyName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);
 String expectedDeclaration = "<declaration>";
 assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
 String expectedDeclaration = "<?xml>";
 assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testXmlDeclarationWithNullName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);
 assertNull(xmlDeclaration.name());
 }

 @Test
 public void testXmlDeclarationToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 String expectedDeclaration = "<declaration version=\"1.0\" encoding=\"UTF-8\">";
 String actualDeclaration = xmlDeclaration.toString();
 assertEquals(expectedDeclaration, actualDeclaration);
 }

 @Test
 public void testXmlDeclarationOuterHtml() throws IOException {
 Document document = Jsoup.parse("", new Document.OutputSettings().indentAmount(2));
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 Element xmlElement = document.createElement("#declaration");
 xmlElement.content(xmlDeclaration);
 String expectedHtml = "<declaration version=\"1.0\" encoding=\"UTF-8\">\n";
 StringBuilder stringBuilder = new StringBuilder();
 xmlElement.outerHtml(stringBuilder, 0, null);
 assertEquals(expectedHtml, stringBuilder.toString());
 }
}