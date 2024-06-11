import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlDeclarationTest {

 private XmlDeclaration xmlDeclaration;
 private Document document;

 @Before
 public void setUp() throws Exception {
 document = new Document();
 xmlDeclaration = new XmlDeclaration("xml", document.baseUri(), false);
 }

 @Test
 public void testXmlDeclarationName() {
 assertEquals("#declaration", xmlDeclaration.nodeName());
 }

 @Test
 public void testXmlDeclarationNameWithAttributes() throws Exception {
 Attribute versionAttr = new Attribute("version", "1.0");
 Attribute encodingAttr = new Attribute("encoding", "UTF-8");
 xmlDeclaration = new XmlDeclaration("xml", document.baseUri(), false);
 xmlDeclaration.attributes().add(versionAttr);
 xmlDeclaration.attributes().add(encodingAttr);
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationNameWithoutAttributes() throws Exception {
 assertEquals("xml", xmlDeclaration.name());
 }

 @Test
 public void testXmlDeclarationToString() throws Exception {
 assertEquals("<declaration xml:version=\"1.0\" xml:encoding=\"UTF-8\"></declaration>", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationProcessingInstruction() throws Exception {
 XmlDeclaration pi = new XmlDeclaration("<?xml", document.baseUri(), true);
 assertEquals("<?xml>", pi.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationOuterHtmlHead() throws Exception {
 Element element = new Element("root");
 element.appendChild(xmlDeclaration);
 document.setRootElement(element);
 Appendable appendable = new StringBuilder();
 xmlDeclaration.outerHtmlHead(appendable, 0, null);
 assertEquals("<!DOCTYPE root [<!ATTLIST root xml:version CDATA #REQUIRED>" +
         "<!ATTLIST root xml:encoding CDATA #REQUIRED>" +
         "<!ENTITY % xml \"xml version=\"version\" encoding=\"encoding\">" +
         "<!ENTITY % xml-decl \"<?xml version=\"version\" encoding=\"encoding\"?>\">" +
         "<!ENTITY % xml-encoding SYSTEM \"about:legacy-compat\">" +
         "<!ENTITY % xml-standalone SYSTEM \"about:legacy-compat\">" +
         "]>" +
         "<root>" +
         "<declaration xml:version=\"version\" xml:encoding=\"encoding\"></declaration>",
         appendable.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtmlTail() throws Exception {
 }
}