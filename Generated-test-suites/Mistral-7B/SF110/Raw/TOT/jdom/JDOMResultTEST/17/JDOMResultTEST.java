import org.jdom2.*;
import org.jdom2.input.sax.JDOMInputFactory;
import org.jdom2.output.XMLOutputter;
import org.jdom2.transform.JDOMResult;
import org.jdom2.transform.Templates;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private JDOMFactory factory;
 private Document document;
 private List<Content> expectedResult;

 @Before
 public void setUp() {
 factory = new DefaultJDOMFactory();
 jdomResult = new JDOMResult();
 expectedResult = new ArrayList<>();
 expectedResult.add(new Element("test", null, null));
 expectedResult.add(new Text("Hello World"));
 document = factory.newDocument(expectedResult);
 }

 @Test
 public void testGetResultEmptyList() throws SAXException {
 jdomResult.setResult(null);
 assertEquals(Collections.emptyList(), jdomResult.getResult());
 }

 @Test
 public void testGetResultDocument() throws SAXException {
 jdomResult.setDocument(document);
 assertEquals(expectedResult, jdomResult.getResult());
 }

 @Test
 public void testGetResultList() {
 jdomResult.setResult(expectedResult);
 assertEquals(expectedResult, jdomResult.getResult());
 }

 @Test
 public void testSetFactory() {
 JDOMResult jdomResult2 = new JDOMResult();
 JDOMFactory factory2 = new JDOMFactory() {};
 jdomResult2.setFactory(factory2);
 assertSame(factory2, jdomResult2.getFactory());
 }

 @Test
 public void testTransformerChain() throws SAXException, TransformerException {
 StringWriter stringWriter = new StringWriter();
 Templates template = new Templates();
 XMLOutputter outputter = new XMLOutputter(factory);
 template.addTemplateCommand("identity-transform", new StringIdentityTransformer(), null);
 template.addTemplateCommand("string-replace", new StringReplaceTransformer("Hello", "World"), null);
 template.transform(document.getRootElement(), outputter, stringWriter);
 List<Content> result = jdomResult.getResult();
 assertEquals(expectedResult.get(0).getName(), result.get(0).getName());
 assertEquals(expectedResult.get(1).getText(), result.get(1).getText());
 assertEquals("WorldWorld", result.get(1).getText());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDocumentNullResult() {
 jdomResult.setResult(null);
 assertNull(jdomResult.getDocument());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDocumentNullFactory() {
 JDOMResult jdomResult2 = new JDOMResult();
 jdomResult2.setFactory(null);
 assertNull(jdomResult2.getDocument());
 }

 @Test
 public void testRetrieveResult() throws SAXException {
 jdomResult.setHandler(new DocumentBuilder());
 SAXInputFactory inputFactory = new JDOMInputFactory();
 StringReader reader = new StringReader("<root><test>Hello World</test></root>");
 InputSource inputSource = new InputSource(reader);
 inputFactory.setFeature("http://xml.org/sax/features/namespaces", true);
 inputFactory.setFeature("http://xml.org/sax/features/validation", false);
 jdomResult.parse(inputSource);
 List<Content> result = jdomResult.getResult();
 assertEquals(expectedResult.size(), result.size());
 for (Content content : result) {
 if (content instanceof Element) {
 assertEquals(expectedResult.get(0).getName(), content.getName());
 }
 if (content instanceof Text) {
 assertEquals(expectedResult.get(1).getText(), content.getText());
 }
 }
 }
}