import org.jdom2.*;
import org.jdom2.output.SAXOutputter;
import org.junit.jupiter.api.*;
import org.xml.sax.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SAXOutputterTest {

 @BeforeAll
 public void setup() {
 SAXOutputter saxOutputter = new SAXOutputter();
 saxOutputter.setContentHandler(new MyContentHandler());
 saxOutputter.setErrorHandler(new MyErrorHandler());
 saxOutputter.setDTDHandler(new MyDTDHandler());
 saxOutputter.setEntityResolver(new MyEntityResolver());
 saxOutputter.setLexicalHandler(new MyLexicalHandler());
 saxOutputter.setDeclHandler(new MyDeclHandler());
 saxOutputter.setReportNamespaceDeclarations(true);
 saxOutputter.setReportDTDEvents(true);
 }

 @Test
 public void testOutputDocument() throws Exception {
 Document document = new JDOMFactory().createDocument();
 document.setDocType(new DocumentType("Test", null, null));
 SAXOutputter saxOutputter = new SAXOutputter();
 saxOutputter.output(document);
 assertNotNull(saxOutputter.getSAXOutputProcessor());
 assertNotNull(saxOutputter.getFormat());
 }

 @Test
 public void testOutputElement() throws Exception {
 Element root = new Element("root");
 Document document = new JDOMFactory().createDocument(root);
 SAXOutputter saxOutputter = new SAXOutputter();
 saxOutputter.output(document);
 verify(myContentHandler, times(1)).startElement("root", null);
 }

 @Test
 public void testOutputText() throws Exception {
 Element root = new Element("root");
 root.addContent(new Text("Hello World"));
 Document document = new JDOMFactory().createDocument(root);
 SAXOutputter saxOutputter = new SAXOutputter();
 saxOutputter.output(document);
 verify(myLexicalHandler, times(1)).characters("Hello World".toCharArray(), 0, "Hello World".length());
 }

 @Test
 public void testOutputCDATA() throws Exception {
 Element root = new Element("root");
 root.addContent(new CDATA("<![CDATA[Hello World]]>"));
 Document document = new JDOMFactory().createDocument(root);
 SAXOutputter saxOutputter = new SAXOutputter();
 saxOutputter.output(document);
 verify(myContentHandler, times(1)).characters("<![CDATA[Hello World]]>".getBytes(), 0, "<![CDATA[Hello World]]>".length());
 }

 @Test
 public void testOutputComment() throws Exception {
 Element root = new Element("root");
 root.addContent(new Comment("Hello World"));
 Document document = new JDOMFactory().createDocument(root);
 SAXOutputter saxOutputter = new SAXOutputter();
 saxOutputter.output(document);
 verify(myContentHandler, times(1)).comment("Hello World");
 }

 @Test
 public void testOutputProcessingInstruction() throws Exception {
 Element root = new Element("root");
 root.addProcessingInstruction("xml-declaration", "version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"");
 Document document = new JDOMFactory().createDocument(root);
 SAXOutputter saxOutputter = new SAXOutputter();
 saxOutputter.output(document);
 verify(myProcessingInstructionHandler, times(1)).processingInstruction("xml-declaration", "version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"");
 }

 @Test
 public void testOutputEntityRef() throws Exception {
 Element root = new Element("root");
 root.addContent(new EntityRef("entity"));
 Document document = new JDOMFactory().createDocument(root);
 SAXOutputter saxOutputter = new SAXOutputter();
 saxOutputter.setEntityResolver(new MyEntityResolver());
 saxOutputter.output(document);
 verify(myEntityResolver, times(1)).getEntity("entity");
 }

 @Test
 public void testHandleErrors() throws Exception {
 Document document = new JDOMFactory().createDocument();
 DocumentType docType = new DocumentType("Test", null, null);
 document.setDocType(docType);
 SAXOutputter saxOutputter = new SAXOutputter();
 saxOutputter.setErrorHandler(new ThrowsExceptionErrorHandler());
 try {
 saxOutputter.output(document);
 fail();
 } catch (JDOMException e) {
 // expected
 }
 }

 private static class MyContentHandler implements ContentHandler {
 // implementation
 }

 private static class MyEntityResolver implements EntityResolver {
 // implementation
 }

 private static class MyErrorHandler implements ErrorHandler {
 // implementation
 }

 private static class MyDTDHandler implements DTDHandler {
 // implementation
 }

 private static class MyLexicalHandler implements LexicalHandler {
 // implementation
 }

 private static class MyDeclHandler implements DeclHandler {
 // implementation
 }

 private static class MyProcessingInstructionHandler implements ProcessingInstructionHandler {
 // implementation
 }

 private static class ThrowsExceptionErrorHandler implements ErrorHandler {
 @Override
 public void error(SAXParseException exception) throws SAXException {
 throw exception;
 }
 }
}