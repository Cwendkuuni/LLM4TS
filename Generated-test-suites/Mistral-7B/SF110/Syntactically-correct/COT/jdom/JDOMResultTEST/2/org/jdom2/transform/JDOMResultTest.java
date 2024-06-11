import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.jdom2.transform.JDOMResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private List<Content> emptyList;
 private List<Content> resultList;
 private Document document;
 private JDOMFactory factory;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 emptyList = Collections.emptyList();
 resultList = new ArrayList<>();
 document = new JDOMBuilder().buildDocument();
 factory = new JDOMFactory();
 }

 @Test
 public void testSetResult() {
 jdomResult.setResult(resultList);
 Assert.assertEquals(resultList, jdomResult.getResult());
 }

 @Test
 public void testGetResult_whenResultlistIsNotNull() {
 jdomResult.setResult(resultList);
 Assert.assertTrue(resultList.size() > 0);
 Assert.assertEquals(resultList, jdomResult.getResult());
 }

 @Test
 public void testGetResult_whenResultdocIsNotNull() throws SAXException {
 jdomResult.setDocument(document);
 resultList = document.getContent();
 jdomResult.retrieveResult();
 Assert.assertEquals(resultList, jdomResult.getResult());
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocument_whenResultlistIsNullAndQueriedIsFalse_shouldReturnNull() {
 jdomResult.setResult(null);
 Assert.assertNull(jdomResult.getDocument());
 }

 @Test
 public void testGetDocument_whenResultlistIsNotNull() throws SAXException {
 jdomResult.setResult(resultList);
 Document doc = jdomResult.getDocument();
 Assert.assertNotNull(doc);
 }

 @Test(expected = NullPointerException.class)
 public void testSetFactory_whenFactoryIsNull_shouldThrowException() {
 jdomResult.setFactory(null);
 }

 @Test
 public void testSetFactory_whenFactoryIsNotNull() {
 jdomResult.setFactory(factory);
 JDOMFactory resultFactory = jdomResult.getFactory();
 Assert.assertEquals(factory, resultFactory);
 }

 @Test
 public void testRetrieveResult_whenResultlistIsNullAndResultdocIsNull() throws SAXException {
 jdomResult = new JDOMResult();
 jdomResult.setHandler(null);
 jdomResult.setLexicalHandler(null);
 SAXBuilder builder = new SAXBuilder();
 builder.build(getClass().getResourceAsStream("/test.xml"), jdomResult);
 jdomResult.retrieveResult();
 List<Content> retrievedResult = jdomResult.getResult();
 Assert.assertEquals(emptyList, retrievedResult);
 }

 @Test(expected = SAXException.class)
 public void testRetrieveResult_whenHandlerIsNull_shouldThrowException() {
 jdomResult = new JDOMResult();
 jdomResult.setResult(null);
 jdomResult.setFactory(null);
 jdomResult.setHandler(null);
 SAXBuilder builder = new SAXBuilder();
 builder.build(getClass().getResourceAsStream("/test.xml"), jdomResult);
 }

}