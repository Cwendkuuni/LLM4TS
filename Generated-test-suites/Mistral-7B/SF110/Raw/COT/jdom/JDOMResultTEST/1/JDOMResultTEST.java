import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.junit.Test;
import org.junit.Assert.*;
import org.xml.sax.SAXException;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMResultTest {

 private JDOMResult result;
 private JDOMFactory factory;
 private Document document;

 @Before
 public void setup() {
 factory = new DefaultJDOMFactory();
 result = new JDOMResult();
 document = new Document("root");
 }

 @Test
 public void testSetResult() {
 List<Content> list = new ArrayList<>();
 list.add(new Element("test1"));
 result.setResult(list);
 List<Content> resultList = result.getResult();
 assertEquals(list, resultList);
 }

 @Test
 public void testGetResultWhenResultDocIsSet() throws SAXException {
 result.setDocument(document);
 List<Content> resultList = result.getResult();
 assertEquals(Collections.singletonList(document), resultList);
 }

 @Test
 public void testGetDocumentWhenResultListIsSet() {
 result.setResult(Collections.singletonList(new Element("test")));
 Document doc = result.getDocument();
 assertEquals(new Element("test"), doc.getRootElement());
 }

 @Test
 public void testGetDocumentWhenResultDocIsSetAndResultListIsEmpty() {
 result.setDocument(document);
 result.setResult(Collections.emptyList());
 Document doc = result.getDocument();
 assertNull(doc);
 }

 @Test
 public void testRetrieveResultWhenResultIsSet() {
 result.setResult(Collections.singletonList(new Element("test")));
 result.retrieveResult();
 List<Content> resultList = result.getResult();
 assertEquals(Collections.singletonList(new Element("test")), resultList);
 }

 @Test
 public void testRetrieveResultWhenResultDocIsSetAndResultListIsEmpty() {
 result.setDocument(document);
 result.retrieveResult();
 List<Content> resultList = result.getResult();
 assertEquals(Collections.singletonList(document), resultList);
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocumentWhenFactoryIsNull() {
 result.setResult(Collections.singletonList(new Element("test")));
 result.setFactory(null);
 Document doc = result.getDocument();
 fail("RuntimeException expected");
 }

 @Test
 public void testSetFactory() {
 JDOMFactory factory = new DefaultJDOMFactory();
 result.setFactory(factory);
 JDOMFactory resultFactory = result.getFactory();
 assertEquals(factory, resultFactory);
 }

 @Test
 public void testGetFactoryWhenResultDocIsSet() {
 result.setDocument(document);
 JDOMFactory resultFactory = result.getFactory();
 assertEquals(factory, resultFactory);
 }

 @Test
 public void testGetFactoryWhenResultListIsSet() {
 result.setResult(Collections.singletonList(new Element("test")));
 JDOMFactory resultFactory = result.getFactory();
 assertEquals(factory, resultFactory);
 }

 @Test
 public void testGetFactoryWhenResultIsNull() {
 result.setFactory(null);
 JDOMFactory resultFactory = result.getFactory();
 assertNull(resultFactory);
 }

 @Test
 public void testSetHandler() {
 // JDOMResult does not implement ContentHandler, so this method is not tested here
 }

 @Test
 public void testSetLexicalHandler() {
 // JDOMResult does not implement LexicalHandler, so this method is not tested here
 }

 @Test
 public void testFragmentHandlerGetResult() throws SAXException {
 SAXBuilder builder = new SAXBuilder();
 StringReader reader = new StringReader("<root><test></test></root>");
 builder.setContentHandler(result.new FragmentHandler(factory));
 builder.build(reader);
 List<Content> resultList = result.getResult();
 assertEquals(1, resultList.size());
 assertEquals(new Element("test"), resultList.get(0));
 }

 @Test
 public void testDocumentBuilderGetResult() throws SAXException {
 String xmlString = "<root><test><subtest></subtest></test></root>";
 SAXBuilder builder = new SAXBuilder();
 builder.setEntityResolver(new DefaultEntityResolver());
 result.setFactory(new DefaultJDOMFactory());
 result.setHandler(new SAXParserAdapter() {
 @Override public void handleStartDocument(final SAXParseEvent event) throws SAXException {
 super.handleStartDocument(event);
 result.setDocument(builder.getDocument());
 }
 });
 StringReader reader = new StringReader(xmlString);
 builder.setContentHandler(result.new DocumentBuilder());
 builder.build(reader);
 Document doc = result.getDocument();
 assertEquals(new Element("root"), doc.getRootElement());
 }
}