import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.*;
import static org.junit.Assert.*;

public class SAXOutputterTest {

 private SAXOutputter outputter;
 private ContentHandler handler;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setUp() {
 handler = new SAXOutputHandler();
 outputStream = new ByteArrayOutputStream();
 outputter = new SAXOutputter(handler, new SAXOutputter.DefaultSAXOutputProcessor(), outputStream);
 }

 @Test
 public void testOutputDocument() throws JDOMException {
 Document document = new Document();
 Element root = new Element("root");
 document.setRootElement(root);
 root.addContent(new Text("Hello World!"));
 outputter.output(document);
 assertEquals("<?xml version=\"1.0\"?>\n<root>\n<![CDATA[Hello World!]]>\n</root>", outputStream.toString());
 }

 @Test
 public void testOutputEmptyDocument() throws JDOMException {
 Document document = new Document();
 outputter.output(document);
 assertEquals("", outputStream.toString());
 }

 @Test
 public void testOutputElement() throws JDOMException {
 Element element = new Element("element");
 outputter.output(element);
 assertEquals("<element/>\n", outputStream.toString());
 }

 @Test
 public void testOutputEmptyElement() throws JDOMException {
 Element element = new Element("element");
 outputter.output(element);
 assertEquals("<element/>\n", outputStream.toString());
 }

 @Test
 public void testOutputList() throws JDOMException {
 List<Element> elements = new JDomNodeList<>();
 elements.add(new Element("element1"));
 elements.add(new Element("element2"));
 outputter.output(elements);
 assertEquals("<element1/>\n<element2/>\n", outputStream.toString());
 }

 @Test
 public void testOutputCDATA() throws JDOMException {
 CDATA cdata = new CDATA("<![CDATA[Hello World!]]>");
 outputter.outputFragment(cdata);
 assertEquals("<![CDATA[Hello World!]]>\n", outputStream.toString());
 }

 @Test
 public void testOutputComment() throws JDOMException {
 Comment comment = new Comment("This is a comment");
 outputter.outputFragment(comment);
 assertEquals("<!--This is a comment-->\n", outputStream.toString());
 }

 @Test
 public void testOutputProcessingInstruction() throws JDOMException {
 ProcessingInstruction pi = new ProcessingInstruction("xml-mapping", "foo bar");
 outputter.outputFragment(pi);
 assertEquals("<?xml-mapping foo bar?>\n", outputStream.toString());
 }

 @Test
 public void testOutputText() throws JDOMException {
 Text text = new Text("Hello World!");
 outputter.outputFragment(text);
 assertEquals("<text>Hello World!</text>\n", outputStream.toString());
 }

 @Test(expected = JDOMException.class)
 public void testOutputInvalidElementContent() throws JDOMException {
 CDATA cdata = new CDATA(new Element("root"));
 outputter.outputFragment(cdata);
 }
}