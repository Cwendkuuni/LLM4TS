import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

import java.io.IOException;

import static org.junit.Assert.*;

public class XmlDeclarationTest {

 private XmlDeclaration xmlDeclaration;
 private Document document;

 @Before
 public void setUp() {
 document = new Document();
 xmlDeclaration = new XmlDeclaration("xml", document.baseUri(), false);
 }

 @Test
 public void testNodeName() {
 String expected = "#declaration";
 assertEquals(expected, xmlDeclaration.nodeName());
 }

 @Test
 public void testName() {
 String expected = "xml";
 assertEquals(expected, xmlDeclaration.name());
 }

 @Test
 public void testGetWholeDeclaration() {
 String expected = "<xml version=\"1.0\" encoding=\"UTF-8\">";
 XmlDeclaration xmlDeclarationWithVersionAndEncoding = new XmlDeclaration("xml", document.baseUri(), false);
 xmlDeclarationWithVersionAndEncoding.attributes().put("version", "1.0");
 xmlDeclarationWithVersionAndEncoding.attributes().put("encoding", "UTF-8");
 String actual = xmlDeclarationWithVersionAndEncoding.getWholeDeclaration();
 assertEquals(expected, actual);

 String actualWithoutVersionAndEncoding = xmlDeclaration.getWholeDeclaration();
 assertEquals("xml", actualWithoutVersionAndEncoding);
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 Appendable expectedAccum = new StringBuilder().append("<?xml version=\"1.0\" encoding=\"UTF-8\">");
 Document documentForOuterHtmlTest = new Document();
 XmlDeclaration xmlDeclarationForOuterHtmlTest = new XmlDeclaration("xml", documentForOuterHtmlTest.baseUri(), false);
 xmlDeclarationForOuterHtmlTest.attributes().put("version", "1.0");
 xmlDeclarationForOuterHtmlTest.attributes().put("encoding", "UTF-8");

 StringBuilder actualAccum = new StringBuilder();
 xmlDeclarationForOuterHtmlTest.outerHtmlHead(actualAccum, 0, null);

 assertEquals(expectedAccum.toString(), actualAccum.toString());
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 Appendable expectedAccum = new StringBuilder();
 XmlDeclaration xmlDeclarationForOuterHtmlTailTest = new XmlDeclaration("xml", document.baseUri(), false);

 xmlDeclarationForOuterHtmlTailTest.outerHtmlTail(expectedAccum, 0, null);

 assertTrue(expectedAccum.toString().isEmpty());
 }

 @Test
 public void testToString() {
 String expected = "<xml version=\"1.0\" encoding=\"UTF-8\">";
 XmlDeclaration xmlDeclarationWithVersionAndEncoding = new XmlDeclaration("xml", document.baseUri(), false);
 xmlDeclarationWithVersionAndEncoding.attributes().put("version", "1.0");
 xmlDeclarationWithVersionAndEncoding.attributes().put("encoding", "UTF-8");
 String actual = xmlDeclarationWithVersionAndEncoding.toString();
 assertEquals(expected, actual);

 String actualWithoutVersionAndEncoding = xmlDeclaration.toString();
 assertEquals("<?xml>", actualWithoutVersionAndEncoding);
 }

 @Test
 public void testCreateWithProcessingInstruction() {
 XmlDeclaration processingInstructionXmlDeclaration = new XmlDeclaration("xml-pi", document.baseUri(), true);
 String expected = "<?xml-pi>";
 String actual = processingInstructionXmlDeclaration.toString();
 assertEquals(expected, actual);
 }
}