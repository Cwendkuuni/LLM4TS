import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.jdom2.transform.JDOMResult;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private JDOMFactory factory;
 private Document document;
 private List<Content> resultList;

 @Before
 public void setup() {
 jdomResult = new JDOMResult();
 factory = new JDOMFactory();
 document = new SAXBuilder().build(getClass().getResource("/test.xml")).getDocument();
 jdomResult.setDocument(document);
 }

 @Test
 public void testSetResult() {
 List<Content> newResultList = new ArrayList<>();
 jdomResult.setResult(newResultList);
 assertEquals(newResultList, jdomResult.getResult());
 }

 @Test
 public void testGetResultWhenResultlistIsNotNull() {
 resultList = jdomResult.getResult();
 assertNotNull(resultList);
 }

 @Test
 public void testGetResultWhenDocumentIsNotNull() {
 Document doc = jdomResult.getDocument();
 assertNotNull(doc);
 }

 @Test
 public void testGetResultWhenBothResultlistAndDocumentAreNotNull() {
 resultList = new ArrayList<>();
 document.setContent(resultList);
 jdomResult.setDocument(document);
 List<Content> getResult = jdomResult.getResult();
 assertEquals(resultList, getResult);
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocumentWhenResultlistIsNullAndQueriedIsFalse() {
 jdomResult.retrieveResult();
 jdomResult.queried = false;
 jdomResult.getDocument();
 }

 @Test
 public void testGetDocumentWhenResultlistIsNullAndQueriedIsTrue() {
 jdomResult.retrieveResult();
 jdomResult.queried = true;
 Document doc = jdomResult.getDocument();
 assertNotNull(doc);
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocumentWhenDocumentIsNotNullAndFactoryIsNull() {
 jdomResult.setDocument(document);
 jdomResult.getDocument();
 }

 @Test
 public void testSetFactory() {
 JDOMFactory newFactory = new JDOMFactory();
 jdomResult.setFactory(newFactory);
 assertEquals(newFactory, jdomResult.getFactory());
 }

 @Test
 public void testRetrieveResultWhenResultlistIsNull() {
 jdomResult.setDocument(document);
 jdomResult.retrieveResult();
 assertNotNull(jdomResult.getResult());
 }

 @Test
 public void testRetrieveResultWhenDocumentIsNull() {
 jdomResult.setResult(resultList);
 jdomResult.retrieveResult();
 assertNotNull(jdomResult.getDocument());
 }

 @Test
 public void testRetrieveResultWhenResultlistIsNotNull() {
 jdomResult.setResult(resultList);
 jdomResult.retrieveResult();
 assertNotNull(jdomResult.getDocument());
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocumentWhenFactoryIsNull() {
 jdomResult.setFactory(null);
 jdomResult.getDocument();
 }

}