import org.jdom2.*;
import org.jdom2.output.SAXOutputter;
import org.jdom2.output.support.SAXTarget;
import org.xml.sax.SAXException;
import junit.framework.TestCase;

public class SAXOutputterTest extends TestCase {

 private SAXOutputter outputter;
 private Document document;

 @Before
 public void setUp() {
 JDOMFactory factory = JDOMFactory.instance();
 document = factory.makeDocument("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 "<root>\n" +
 "<element attr=\"value\">Content</element>\n" +
 "</root>");
 outputter = new SAXOutputter();
 }

 @Test
 public void testOutputDocument() throws JDOMException, SAXException {
 SAXTarget target = outputter.buildTarget(document);
 outputter.setSAXOutputProcessor(new DefaultSAXOutputProcessor());
 outputter.setFormat(Format.getPrettyFormat());
 outputter.output(document);
 String expectedOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 "<root xmlns=\"##default\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"##default\">\n" +
 "<element attr=\"value\">Content</element>\n" +
 "</root>";
 verifyXmlEqual(expectedOutput, new StringReader(new StringReader(outputter.getSAXOutputProcessor().getSystemId()).readAll()));
 }

 @Test
 public void testOutputEmptyDocument() throws JDOMException, SAXException {
 outputter.output(null);
 }

 @Test
 public void testOutputListOfContent() throws JDOMException, SAXException {
 List<Content> nodes = new ArrayList<>();
 nodes.add(new Element("element").setText("Content"));
 nodes.add(new Comment("This is a comment"));
 outputter.output(nodes);
 String expectedOutput = "<!--This is a comment-->\n<element>Content</element>";
 verifyXmlEqual(expectedOutput, new StringReader(new StringReader(outputter.getSAXOutputProcessor().getSystemId()).readAll()));
 }

 @Test
 public void testOutputElement() throws JDOMException, SAXException {
 Element node = new Element("element").setText("Content");
 outputter.outputFragment(node);
 String expectedOutput = "<element>Content</element>";
 verifyXmlEqual(expectedOutput, new StringReader(new StringReader(outputter.getSAXOutputProcessor().getSystemId()).readAll()));
 }

 @Test
 public void testOutputCDATA() throws JDOMException, SAXException {
 CDATA cdata = new CDATA("<![CDATA[This is CDATA]]>");
 outputter.outputFragment(cdata);
 String expectedOutput = "<![CDATA[This is CDATA]]>";
 verifyXmlEqual(expectedOutput, new StringReader(new StringReader(outputter.getSAXOutputProcessor().getSystemId()).readAll()));
 }

 @Test
 public void testOutputProcessingInstruction() throws JDOMException, SAXException {
 ProcessingInstruction pi = new ProcessingInstruction("xml-misc", "This is a processing instruction");
 outputter.outputFragment(pi);
 String expectedOutput = "<?xml-misc This is a processing instruction>";
 verifyXmlEqual(expectedOutput, new StringReader(new StringReader(outputter.getSAXOutputProcessor().getSystemId()).readAll()));
 }

 @Test(expected = JDOMException.class)
 public void testOutputNullContent() throws JDOMException, SAXException {
 outputter.outputFragment(null);
 }

 @Test(expected = SAXException.class)
 public void testOutputWithErrorHandler() throws JDOMException, SAXException {
 outputter.setErrorHandler(new ErrorHandler() {
 @Override
 public void error(SAXParseException exception) throws SAXException {
 fail("Error handler should be called with an error");
 }
 });
 outputter.output(document);
 }

 @Test(expected = SAXException.class)
 public void testOutputWithLexicalHandler() throws JDOMException, SAXException {
 outputter.setLexicalHandler(new LexicalHandler() {
 @Override
 public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException {
 throw new SAXException("Lexical handler should not be called during document output");
 }
 });
 outputter.output(document);
}

 @After
 public void tearDown() {
 outputter = null;
 document = null;
 }

 private void verifyXmlEqual(String expected, Reader actual) throws IOException {
 StringReader expectedReader = new StringReader(expected);
 StringReader actualReader = new StringReader(actual);
 assertEquals(expectedReader.readLine(), actualReader.readLine());
 String line;
 while ((line = expectedReader.readLine()) != null && (line = actualReader.readLine()) != null) {
 assertEquals(line, line);
 }
}
}