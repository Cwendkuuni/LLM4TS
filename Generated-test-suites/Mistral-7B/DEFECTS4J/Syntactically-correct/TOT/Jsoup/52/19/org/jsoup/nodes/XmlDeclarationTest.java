import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationWithValidNameAndVersion() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals("xml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithProcessingInstructionAndValidNameAndVersion() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals("<?xml version=\"1.0\"?>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithEmptyName() {
 try {
 new XmlDeclaration(null, null, false);
 fail("Should have thrown NullPointerException for null name");
 } catch (NullPointerException e) {
 //expected
 }
 }

 @Test
 public void testXmlDeclarationWithEmptyNameAndProcessingInstruction() {
 try {
 new XmlDeclaration(null, null, true);
 fail("Should have thrown NullPointerException for null name");
 } catch (NullPointerException e) {
 //expected
 }
 }

 @Test
 public void testXmlDeclarationWithOnlyVersionAttribute() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.1");
 assertEquals("xml version=\"1.1\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithOnlyEncodingAttribute() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("xml encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithBothVersionAndEncodingAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.1");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("xml version=\"1.1\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithInvalidName() {
 try {
 new XmlDeclaration("invalid", null, false);
 fail("Should have thrown IllegalArgumentException for invalid name");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test
 public void testXmlDeclarationWithInvalidNameAndProcessingInstruction() {
 try {
 new XmlDeclaration("invalid", null, true);
 fail("Should have thrown IllegalArgumentException for invalid name");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test
 public void testXmlDeclarationToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("<xml version=\"1.0\">", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationToStringWithProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
 assertEquals("<?xml version=\"1.0\"?>", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtml() throws IOException {
 Document doc = Jsoup.parse("<html><head><![CDATA[%s]]></head><body></body></html>", xmlDeclaration.toString());
 assertEquals("<html><head><![CDATA[<?xml version=\"1.0\"?>]]></head><body></body></html>", doc.html());
 }

}