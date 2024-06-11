import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;
import java.io.StringWriter;
import static org.junit.Assert.*;

public class XMLOutputterTest {

 private Document createSampleDocument() {
 final Element root = new Element("root");
 final Element element1 = new Element("element1");
 element1.setText("Element 1 content");
 root.addContent(element1);
 final Element element2 = new Element("element2");
 element2.setText("Element 2 content");
 root.addContent(element2);
 return new Document(new Element("document"), root);
 }

 @Test
 public void testOutputDocument() throws IOException {
 final XMLOutputter xmlo = new XMLOutputter();
 final Document doc = createSampleDocument();
 final StringWriter out = new StringWriter();
 xmlo.output(doc, out);
 final String expectedXml = "<document>\n<root>\n<element1>Element 1 content</element1>\n<element2>Element 2 content</element2>\n</root>\n</document>";
 assertEquals(expectedXml, out.toString());
 }

 @Test
 public void testOutputDocumentWithEmptyElements() throws IOException {
 final Document doc = new Document(new Element("document"), new Element("root").setText(""));
 final StringWriter out = new StringWriter();
 final XMLOutputter xmlo = new XMLOutputter(Format.getPrettyFormat());
 xmlo.setExpandEmptyElements(true);
 xmlo.output(doc, out);
 final String expectedXml = "<document>\n<root />\n</document>";
 assertEquals(expectedXml, out.toString());
 }

 @Test(expected = IOException.class)
 public void testOutputDocumentWithNullDocument() {
 final XMLOutputter xmlo = new XMLOutputter();
 final StringWriter out = new StringWriter();
 xmlo.output(null, out);
 }

 @Test(expected = IOException.class)
 public void testOutputDocumentWithNullOutputStream() {
 final XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(new Document(), null);
 }

 @Test
 public void testOutputElement() throws IOException {
 final XMLOutputter xmlo = new XMLOutputter();
 final Element element = new Element("element");
 element.setText("Element content");
 final StringWriter out = new StringWriter();
 xmlo.output(element, out);
 final String expectedXml = "<element>Element content</element>";
 assertEquals(expectedXml, out.toString());
 }

 @Test
 public void testOutputEmptyElement() throws IOException {
 final XMLOutputter xmlo = new XMLOutputter(Format.getPrettyFormat());
 xmlo.setExpandEmptyElements(true);
 final Element element = new Element("element");
 final StringWriter out = new StringWriter();
 xmlo.output(element, out);
 final String expectedXml = "<element />";
 assertEquals(expectedXml, out.toString());
 }

 @Test(expected = IOException.class)
 public void testOutputElementWithNullElement() {
 final XMLOutputter xmlo = new XMLOutputter();
 final StringWriter out = new StringWriter();
 xmlo.output(null, out);
 }

 @Test(expected = IOException.class)
 public void testOutputElementWithNullOutputStream() {
 final XMLOutputter xmlo = new XMLOutputter();
 final Element element = new Element("element");
 xmlo.output(element, null);
 }

 @Test
 public void testSetFormat() throws IOException {
 final XMLOutputter xmlo = new XMLOutputter();
 final Format format = new Format();
 format.setOmitDeclaration(true);
 xmlo.setFormat(format);
 final Document doc = createSampleDocument();
 final StringWriter out = new StringWriter();
 xmlo.output(doc, out);
 final String expectedXml = "<document>\n<root>\n<element1>Element 1 content</element1>\n<element2>Element 2 content</element2>\n</root>\n";
 assertEquals(expectedXml, out.toString());
 }

 @Test
 public void testSetXMLOutputProcessor() throws IOException {
 final XMLOutputter xmlo = new XMLOutputter();
 final XMLOutputProcessor processor = new MyCustomXMLOutputProcessor();
 xmlo.setXMLOutputProcessor(processor);
 final Document doc = createSampleDocument();
 final StringWriter out = new StringWriter();
 xmlo.output(doc, out);
 // The behavior depends on MyCustomXMLOutputProcessor
 }

}

class MyCustomXMLOutputProcessor extends XMLOutputProcessor {
 // Implement custom processing behavior here
}