import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XmlDeclarationTest {

 private Document document;

 @Before
 public void setUp() {
 document = Jsoup.parse("<!doctype html public \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
 "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
 "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
 "<head><title>Test</title></head>" +
 "<body>");
 }

 @Test
 public void testGetName() {
 XmlDeclaration xmlDeclaration = document.xml();
 assertEquals("#declaration", xmlDeclaration.nodeName());
 }

 @Test
 public void testGetWholeDeclaration() {
 XmlDeclaration xmlDeclaration = document.xml();
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationEmpty() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("xml", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
 assertEquals("<?xml?>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationWithAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.1");
 xmlDeclaration.attributes().put("encoding", "UTF-16");
 assertEquals("<!DOCTYPE xml PUBLIC \"-//W3C//DTD XML 1.1//EN\" \"http://www.w3.org/TR/xml11/DTD/xml11.dtd\" version=\"1.1\" encoding=\"UTF-16\">", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 Appendable accum = new StringBuilder();
 XmlDeclaration xmlDeclaration = document.xml();
 xmlDeclaration.outerHtmlHead(accum, 0, null);
 assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
 "<!xml version=\"1.0\" encoding=\"UTF-8\">", accum.toString());
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 Appendable accum = new StringBuilder();
 XmlDeclaration xmlDeclaration = document.xml();
 xmlDeclaration.outerHtmlTail(accum, 0, null);
 assertEquals("", accum.toString());
 }

 @Test
 public void testToString() {
 XmlDeclaration xmlDeclaration = document.xml();
 assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
 "<!xml version=\"1.0\" encoding=\"UTF-8\">", xmlDeclaration.toString());
 }

 @Test
 public void testNullName() {
 try {
 new XmlDeclaration(null, null, false);
 } catch (IllegalArgumentException e) {
 assertEquals("name is marked non-null but is null", e.getMessage());
 }
 }

 @Test
 public void testNullBaseUri() {
 try {
 new XmlDeclaration("xml", null, false);
 } catch (NullPointerException e) {
 assertEquals("baseUri is marked non-null but is null", e.getMessage());
 }
 }

 @Test
 public void testIsProcessingInstructionFalse() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertNull(xmlDeclaration.isProcessingInstruction());
 }

 @Test
 public void testIsProcessingInstructionTrue() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
 assertTrue(xmlDeclaration.isProcessingInstruction());
 }
}