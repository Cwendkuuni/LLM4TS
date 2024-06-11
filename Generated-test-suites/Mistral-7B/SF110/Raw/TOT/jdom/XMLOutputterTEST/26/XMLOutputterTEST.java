import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import static org.junit.Assert.*;

public class XMLOutputterTest {

 private XMLOutputter outputter;
 private Format format;
 private ByteArrayOutputStream byteStream;
 private StringWriter stringWriter;

 @Before
 public void setUp() {
 format = new Format();
 format.setOmitDeclaration(false);
 format.setEncoding("UTF-8");
 format.setLineSeparator("\n");
 byteStream = new ByteArrayOutputStream();
 stringWriter = new StringWriter();
 }

 @Test
 public void testOutputDocument() throws Exception {
 Document document = new Document();
 document.setRootElement(new Element("root"));
 document.getRootElement().addContent(new Element("child1").setText("Child 1"));
 document.getRootElement().addContent(new Element("child2").setText("Child 2"));

 outputter = new XMLOutputter(format);
 outputter.output(document, byteStream);
 String expectedXml = "<root><child1>Child 1</child1><child2>Child 2</child2></root>";
 assertEquals(expectedXml, new String(byteStream.toByteArray(), "UTF-8"));
 }

 @Test
 public void testOutputDocType() throws Exception {
 DocType docType = new DocType("root", null, new Element[] {
 new Element("child", null, new Attribute("data", "type"), null),
 new Element("anotherChild", null, null, null)
 });

 outputter = new XMLOutputter(format);
 outputter.output(docType, byteStream);
 String expectedDocType = "<!DOCTYPE root [ <!ATTLIST child data CDATA #REQUIRED>\n<!ATTLIST anotherChild EMPTY>\n]>";
 assertEquals(expectedDocType, new String(byteStream.toByteArray(), "UTF-8"));
 }

 @Test
 public void testOutputElement() throws Exception {
 Element element = new Element("element");
 element.setText("Text inside element");

 outputter = new XMLOutputter(format);
 outputter.output(element, byteStream);
 String expectedElement = "<element>Text inside element</element>";
 assertEquals(expectedElement, new String(byteStream.toByteArray(), "UTF-8"));
 }

 @Test
 public void testOutputWithNullDocument() throws Exception {
 outputter = new XMLOutputter(format);
 outputter.output(null, byteStream);
 assertEquals("", new String(byteStream.toByteArray(), "UTF-8"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOutputWithNullFormat() throws Exception {
 outputter = new XMLOutputter(null, format);
 outputter.output(new Document(), byteStream);
 }

 @Test(expected = NullPointerException.class)
 public void testOutputWithNullOutputStream() throws Exception {
 outputter = new XMLOutputter(format);
 outputter.output(new Document(), null);
 }
}