import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for XmlDeclaration.
 */
public class XmlDeclarationTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = Jsoup.parse("<declaration version=\"1.0\" encoding=\"UTF-8\"/>");
 }

 /**
 * Test method for XmlDeclaration constructor.
 */
 @Test
 public void testXmlDeclarationConstructor() {
 XmlDeclaration xmlDeclaration = doc.xml();
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("declaration", xmlDeclaration.name());
 assertEquals("version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
 }

 /**
 * Test method for getWholeDeclaration when no attributes.
 */
 @Test
 public void testGetWholeDeclarationNoAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("xml", xmlDeclaration.getWholeDeclaration());
 }

 /**
 * Test method for getWholeDeclaration when has version attribute.
 */
 @Test
 public void testGetWholeDeclarationVersionAttribute() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.1");
 assertEquals("xml version=\"1.1\"", xmlDeclaration.getWholeDeclaration());
 }

 /**
 * Test method for getWholeDeclaration when has encoding attribute.
 */
 @Test
 public void testGetWholeDeclarationEncodingAttribute() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("encoding", "UTF-16");
 assertEquals("xml encoding=\"UTF-16\"", xmlDeclaration.getWholeDeclaration());
 }

 /**
 * Test method for getWholeDeclaration when has version and encoding attributes.
 */
 @Test
 public void testGetWholeDeclarationVersionAndEncodingAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.1");
 xmlDeclaration.attributes().put("encoding", "UTF-16");
 assertEquals("xml version=\"1.1\" encoding=\"UTF-16\"", xmlDeclaration.getWholeDeclaration());
 }

 /**
 * Test method for getWholeDeclaration when name is not xml.
 */
 @Test
 public void testGetWholeDeclarationWrongName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("text", null, false);
 assertNull(xmlDeclaration.getWholeDeclaration());
 }

 /**
 * Test method for getWholeDeclaration when isProcessingInstruction is true.
 */
 @Test
 public void testGetWholeDeclarationIsProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
 assertEquals("<?xml", xmlDeclaration.getWholeDeclaration());
 }
}