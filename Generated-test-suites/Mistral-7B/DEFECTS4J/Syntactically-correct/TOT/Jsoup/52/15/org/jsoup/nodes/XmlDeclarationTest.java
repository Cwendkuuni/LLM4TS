import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationWithNameAndVersion() {
 String xmlDeclaration = "<declaration version=\"1.0\">";
 XmlDeclaration xmlDeclarationNode = Jsoup.parse(xmlDeclaration).body().xmlDeclaration();
 XmlDeclaration expectedXmlDeclaration = new XmlDeclaration("declaration", null, false) {{
 setAttribute("version", "1.0");
 }};
 assertEquals(expectedXmlDeclaration, xmlDeclarationNode);
 }

 @Test
 public void testXmlDeclarationWithNameEncodingAndVersion() {
 String xmlDeclaration = "<declaration encoding=\"UTF-8\" version=\"1.0\">";
 XmlDeclaration xmlDeclarationNode = Jsoup.parse(xmlDeclaration).body().xmlDeclaration();
 XmlDeclaration expectedXmlDeclaration = new XmlDeclaration("declaration", null, false) {{
 setAttribute("encoding", "UTF-8");
 setAttribute("version", "1.0");
 }};
 assertEquals(expectedXmlDeclaration, xmlDeclarationNode);
 }

 @Test
 public void testXmlDeclarationWithoutAttributes() {
 String xmlDeclaration = "<declaration>";
 XmlDeclaration xmlDeclarationNode = Jsoup.parse(xmlDeclaration).body().xmlDeclaration();
 XmlDeclaration expectedXmlDeclaration = new XmlDeclaration("declaration", null, false);
 assertEquals(expectedXmlDeclaration, xmlDeclarationNode);
 }

 @Test(expected = NullPointerException.class)
 public void testNullName() {
 String xmlDeclaration = "<declaration>";
 try {
 XmlDeclaration xmlDeclarationNode = Jsoup.parse(xmlDeclaration).body().xmlDeclaration(null, null, false);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 String xmlDeclaration = "<declaration>";
 try {
 XmlDeclaration xmlDeclarationNode = new XmlDeclaration(null, null, false);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testGetWholeDeclaration() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false) {{
 setAttribute("version", "1.0");
 setAttribute("encoding", "UTF-8");
 }};
 String expectedXmlDeclaration = "<declaration version=\"1.0\" encoding=\"UTF-8\">";
 String actualXmlDeclaration = xmlDeclaration.getWholeDeclaration();
 assertEquals(expectedXmlDeclaration, actualXmlDeclaration);
 }

 @Test
 public void testToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false) {{
 setAttribute("version", "1.0");
 setAttribute("encoding", "UTF-8");
 }};
 String expectedXmlDeclaration = "<declaration version=\"1.0\" encoding=\"UTF-8\">";
 String actualXmlDeclaration = xmlDeclaration.toString();
 assertEquals(expectedXmlDeclaration, actualXmlDeclaration);
 }
}