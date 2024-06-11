import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.transform.JDOMResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class JDOMResultTest {

 private JDOMResult result;
 private JDOMFactory factory;

 @Before
 public void setUp() {
 factory = new DefaultJDOMFactory();
 result = new JDOMResult();
 }

 @Test
 public void testEmptyXML() throws SAXException, JDOMException {
 String xml = "<root></root>";
 SAXBuilder builder = new SAXBuilder();
 builder.setContentHandler(result);
 builder.parse(new StringReader(xml));

 List<Content> nodes = result.getResult();
 Assert.assertEquals(0, nodes.size());
 }

 @Test
 public void testSimpleXML() throws SAXException, JDOMException {
 String xml = "<root><element>Hello World</element></root>";
 SAXBuilder builder = new SAXBuilder();
 builder.setContentHandler(result);
 builder.parse(new StringReader(xml));

 List<Content> nodes = result.getResult();
 Element element = (Element) nodes.get(0);
 Assert.assertEquals("Hello World", element.getText());
 }

 @Test
 public void testErrorXML() throws SAXException, JDOMException {
 String xml = "<root><element><script>alert('Hello World')</script></element></root>";
 SAXBuilder builder = new SAXBuilder();
 builder.setContentHandler(result);
 try {
 builder.parse(new StringReader(xml));
 Assert.fail("Expected JDOMException or SAXException to be thrown");
 } catch (JDOMException | SAXException e) {
 // Expected
 }

 List<Content> nodes = result.getResult();
 Assert.assertEquals(0, nodes.size());
 }

 @Test
 public void testMultipleElements() throws SAXException, JDOMException {
 String xml = "<root><element1>Hello</element1><element2>World</element2></root>";
 SAXBuilder builder = new SAXBuilder();
 builder.setContentHandler(result);
 builder.parse(new StringReader(xml));

 List<Content> nodes = result.getResult();
 Assert.assertEquals(2, nodes.size());
 Element element1 = (Element) nodes.get(0);
 Assert.assertEquals("Hello", element1.getText());
 Element element2 = (Element) nodes.get(1);
 Assert.assertEquals("World", element2.getText());
 }

 @Test
 public void testDocument() throws SAXException, JDOMException {
 Document doc = new SimpleApiDocument("Test document");
 result.setDocument(doc);
 List<Content> nodes = result.getResult();
 Assert.assertEquals(doc.getRootElement().getContent(), nodes);
 }
}