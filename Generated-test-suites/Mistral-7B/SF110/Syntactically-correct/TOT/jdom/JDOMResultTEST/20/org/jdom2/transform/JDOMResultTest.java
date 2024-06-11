import org.junit.Before;
import org.junit.Test;
import org.jdom2.JDOMException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Content;
import org.jdom2.input.SAXBuilder;
import org.jdom2.transform.JDOMResult;
import static org.junit.Assert.*;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private Document document;
 private SAXBuilder saxBuilder;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 saxBuilder = new SAXBuilder();
 try {
 document = saxBuilder.build(getClass().getResource("/test.xml"));
 } catch (JDOMException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testGetResultEmptyList() {
 jdomResult.setResult(null);
 List<Content> result = jdomResult.getResult();
 assertEquals(0, result.size());
 }

 @Test
 public void testGetResultList() {
 jdomResult.setResult(Arrays.asList(new Element("element1"), new Element("element2")));
 List<Content> result = jdomResult.getResult();
 assertEquals(2, result.size());
 assertEquals("element1", result.get(0).getName());
 assertEquals("element2", result.get(1).getName());
 }

 @Test
 public void testGetResultDocument() {
 jdomResult.setDocument(document);
 List<Content> result = jdomResult.getResult();
 assertEquals(document.getRootElement().getChildren().size(), result.size());
 for (Content content : result) {
 assertEquals(document.getRootElement().getChild(content.getIndex()), content);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testGetResultTwice() {
 jdomResult.setDocument(document);
 List<Content> result1 = jdomResult.getResult();
 List<Content> result2 = jdomResult.getResult();
 fail("Expected IllegalStateException but got " + result2);
 }

 @Test
 public void testSetFactory() {
 JDOMFactory factory = new DefaultJDOMFactory();
 jdomResult.setFactory(factory);
 assertEquals(factory, jdomResult.getFactory());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetFactoryNull() {
 jdomResult.setFactory(null);
 fail("Expected IllegalArgumentException but got null");
 }
}