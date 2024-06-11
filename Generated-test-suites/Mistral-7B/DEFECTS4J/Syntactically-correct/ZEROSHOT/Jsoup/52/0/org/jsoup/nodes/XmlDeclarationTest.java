import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlDeclarationTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = new Document();
 }

 @Test
 public void testXmlDeclarationCreation() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertNotNull(xmlDeclaration);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 }

 @Test
 public void testXmlDeclarationCreationWithProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
 assertNotNull(xmlDeclaration);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 }

 @Test
 public void testXmlDeclarationCreationWithAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\">", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtmlHead() throws Exception {
 Appendable accum = new StringBuilder();
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.outerHtmlHead(accum, 0, null);
 assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\">", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtmlTail() throws Exception {
 Appendable accum = new StringBuilder();
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.outerHtmlTail(accum, 0, null);
 assertEquals("", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationProcessingInstructionOuterHtmlHead() throws Exception {
 Appendable accum = new StringBuilder();
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
 xmlDeclaration.outerHtmlHead(accum, 0, null);
 assertEquals("<?xml>", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationWithNameOtherThanXml() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("myxml", null, false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("myxml", xmlDeclaration.name());
 }
}