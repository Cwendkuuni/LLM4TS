import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationWithVersionAndEncoding() {
 String html = "<xml version=\"1.0\" encoding=\"UTF-8\" ><root></root></xml>";
 Document doc = Jsoup.parse(html);
 XmlDeclaration declaration = doc.xml();

 assertEquals("#declaration", declaration.nodeName());
 assertEquals("xml", declaration.name());
 assertEquals("version=\"1.0\" encoding=\"UTF-8\"", declaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithoutVersionOrEncoding() {
 String html = "<xml><root></root></xml>";
 Document doc = Jsoup.parse(html);
 XmlDeclaration declaration = doc.xml();

 assertEquals("#declaration", declaration.nodeName());
 assertEquals("xml", declaration.name());
 assertEquals("xml", declaration.getWholeDeclaration());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullName() {
 String html = "<xml></xml>"; // missing name attribute
 Document doc = Jsoup.parse(html);
 XmlDeclaration declaration = new XmlDeclaration(null, "", true);

 try {
 declaration.getWholeDeclaration();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyName() {
 String html = "<xml></xml>"; // missing name attribute
 Document doc = Jsoup.parse(html);
 XmlDeclaration declaration = new XmlDeclaration("", "", true);

 try {
 declaration.getWholeDeclaration();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testToString() {
 String html = "<xml version=\"1.0\" encoding=\"UTF-8\" ><root></root></xml>";
 Document doc = Jsoup.parse(html);
 XmlDeclaration declaration = doc.xml();

 assertEquals("<!DOCTYPE xml PUBLIC \"-//W3C//DTD XML 1.0//EN\" \"http://www.w3.org/TR/xml/\">\n<xml version=\"1.0\" encoding=\"UTF-8\">\n<root></root>\n", declaration.toString());
 }
}