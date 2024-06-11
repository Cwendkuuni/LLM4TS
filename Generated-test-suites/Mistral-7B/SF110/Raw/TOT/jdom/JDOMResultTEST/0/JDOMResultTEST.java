import org.jdom2.JDOMException;
import org.jdom2.input.sax.SAXBuilder;
import org.jdom2.transform.JDOMResult;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private SAXBuilder saxBuilder;

 @Before
 public void setup() {
 jdomResult = new JDOMResult();
 saxBuilder = new SAXBuilder();
 }

 @Test
 public void testGetResult_EmptyResultList() throws JDOMException, SAXException {
 jdomResult.setResult(Collections.emptyList());
 List<Content> result = jdomResult.getResult();
 Assert.assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testGetResult_ResultDocument() throws JDOMException, SAXException {
 Document document = saxBuilder.build(new StringReader("<root><child></child></root>"));
 jdomResult.setDocument(document);
 List<Content> result = jdomResult.getResult();
 Assert.assertNotEquals(Collections.emptyList(), result);
 }

 @Test
 public void testGetResult_NullResultListAndDocument() {
 jdomResult.queried = false;
 List<Content> result = jdomResult.getResult();
 Assert.assertEquals(Collections.emptyList(), result);
 }

 @Test(expected = NullPointerException.class)
 public void testGetResult_NullFactory() {
 jdomResult.setFactory(null);
 jdomResult.getDocument();
 }

 @Test
 public void testGetFactory_NullFactory() {
 JDOMFactory factory = null;
 jdomResult.setFactory(factory);
 JDOMFactory resultFactory = jdomResult.getFactory();
 Assert.assertNotNull(resultFactory);
 }

 @Test
 public void testGetFactory_DefaultFactory() {
 JDOMFactory factory = jdomResult.getFactory();
 Assert.assertTrue(factory instanceof DefaultJDOMFactory);
 }

 @Test
 public void testGetDocument_EmptyResultList() {
 jdomResult.setResult(Collections.emptyList());
 Document resultDocument = jdomResult.getDocument();
 Assert.assertNull(resultDocument);
 }

 @Test
 public void testGetDocument_ResultDocument() throws JDOMException, SAXException {
 Document document = saxBuilder.build(new StringReader("<root><child></child></root>"));
 jdomResult.setDocument(document);
 Document resultDocument = jdomResult.getDocument();
 Assert.assertEquals(document, resultDocument);
 }

 @Test
 public void testGetDocument_NullResultListAndDocument() {
 jdomResult.queried = false;
 Document resultDocument = jdomResult.getDocument();
 Assert.assertNull(resultDocument);
 }
}