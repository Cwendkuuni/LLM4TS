import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationWithNameAndVersion() {
 String xmlDeclaration = "<declaration version=\"1.0\">";
 Document doc = Jsoup.parseBodyFragment(xmlDeclaration);
 XmlDeclaration xmlDecl = (XmlDeclaration) doc.child(0);

 assertEquals("#declaration", xmlDecl.nodeName());
 assertEquals("declaration", xmlDecl.name());
 assertEquals("version=\"1.0\"", xmlDecl.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithNameAndEncoding() {
 String xmlDeclaration = "<declaration encoding=\"UTF-8\">";
 Document doc = Jsoup.parseBodyFragment(xmlDeclaration);
 XmlDeclaration xmlDecl = (XmlDeclaration) doc.child(0);

 assertEquals("#declaration", xmlDecl.nodeName());
 assertEquals("declaration", xmlDecl.name());
 assertEquals("encoding=\"UTF-8\"", xmlDecl.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithoutAttributes() {
 String xmlDeclaration = "<declaration>";
 Document doc = Jsoup.parseBodyFragment(xmlDeclaration);
 XmlDeclaration xmlDecl = (XmlDeclaration) doc.child(0);

 assertEquals("#declaration", xmlDecl.nodeName());
 assertEquals("declaration", xmlDecl.name());
 assertEquals("declaration", xmlDecl.getWholeDeclaration());
 }

 @Test
 public void testXmlProcessingInstruction() {
 String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Document doc = Jsoup.parseBodyFragment(xmlDeclaration);
 XmlDeclaration xmlDecl = (XmlDeclaration) doc.child(0);

 assertEquals("#declaration", xmlDecl.nodeName());
 assertEquals("declaration", xmlDecl.name());
 assertEquals("version=\"1.0\" encoding=\"UTF-8\"", xmlDecl.getWholeDeclaration());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullName() {
 XmlDeclaration xmlDecl = new XmlDeclaration(null, "", false);
 }

 @Test
 public void testToString() {
 String xmlDeclaration = "<declaration version=\"1.0\">";
 Document doc = Jsoup.parseBodyFragment(xmlDeclaration);
 XmlDeclaration xmlDecl = (XmlDeclaration) doc.child(0);

 assertEquals(xmlDeclaration, xmlDecl.toString());
 }
}