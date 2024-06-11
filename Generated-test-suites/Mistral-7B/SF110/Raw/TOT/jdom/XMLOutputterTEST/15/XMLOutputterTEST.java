import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XMLOutputterTest {

 private Document doc;
 private XMLOutputter xmlo;

 @Before
 public void setup() {
 doc = new JAXBBuilder().buildDocument(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<root>\n" +
         "<element attr=\"value\">Content</element>\n" +
         "<comment>This is a comment</comment>\n" +
         "<text>Hello World</text>\n" +
         "</root>"));
 xmlo = new XMLOutputter();
 }

 @Test
 public void testOutputDocument() throws IOException {
 Writer out = new StringWriter();
 xmlo.output(doc, out);
 String expected = "<root xmlns=\"http://www.w3.org/2000/xmlns/\">\n" +
         "<element attr=\"value\">Content</element>\n" +
         "</root>";
 assertEquals(expected, out.toString());
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = doc.getRootElement().getChild(0);
 Writer out = new StringWriter();
 xmlo.output(element, out);
 String expected = "<element attr=\"value\">Content</element>";
 assertEquals(expected, out.toString());
 }

 @Test
 public void testOutputEmptyElement() throws IOException {
 Element emptyElement = doc.getRootElement().getChild(0).getChild(0).getChildren().get(0).detach();
 Writer out = new StringWriter();
 xmlo.output(emptyElement, out);
 String expected = "<element />";
 assertEquals(expected, out.toString());
 }

 @Test
 public void testOutputNullElement() throws IOException {
 Writer out = new StringWriter();
 xmlo.output(null, out);
 String expected = "";
 assertEquals(expected, out.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testOutputNullDocument() throws IOException {
 Writer out = new StringWriter();
 xmlo.output(null, out);
 }

 @Test
 public void testOutputString() {
 String expected = "<root xmlns=\"http://www.w3.org/2000/xmlns/\">\n" +
         "<element attr=\"value\">Content</element>\n" +
         "</root>";
 assertEquals(xmlo.outputString(doc), expected);
 }

 @Test
 public void testOutputStringEmpty() {
 assertEquals("", xmlo.outputString(new Document()));
 }

}