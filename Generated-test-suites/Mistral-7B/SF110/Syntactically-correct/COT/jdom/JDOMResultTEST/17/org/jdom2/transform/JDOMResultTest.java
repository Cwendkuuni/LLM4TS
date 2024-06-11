import org.junit.Test;
import org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.transform.JDOMResult;
import org.jdom2.input.sax.SAXBuilder;
import org.xml.sax.SAXException;

public class JDOMResultTest {

 private JDOMResult result;
 private List<Content> expectedResult;
 private Document document;

 @Before
 public void setUp() {
 result = new JDOMResult();
 expectedResult = new ArrayList<>();
 try {
 SAXBuilder builder = new SAXBuilder();
 document = builder.build(getClass().getResource("/test.xml"));
 result.setDocument(document);
 } catch (JDOMException | SAXException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testGetResult_WhenResultListIsNotEmpty_ReturnsResultList() {
 result.retrieveResult();
 List<Content> actualResult = result.getResult();
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = NullPointerException.class)
 public void testGetResult_WhenDocumentIsNotSet_ReturnsNull() {
 result.retrieveResult();
 result.setDocument(null);
 List<Content> actualResult = result.getResult();
 assertNull(actualResult);
 }

 @Test
 public void testGetDocument_WhenResultListIsNotEmpty_ReturnsDocument() {
 Document actualDoc = result.getDocument();
 assertEquals(document, actualDoc);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDocument_WhenDocumentIsNotSet_ReturnsNull() {
 result.setDocument(null);
 Document actualDoc = result.getDocument();
 assertNull(actualDoc);
 }

 @Test
 public void testGetFactory_WhenFactoryIsSet_ReturnsFactory() {
 JDOMFactory actualFactory = result.getFactory();
 assertEquals(result.factory, actualFactory);
 }

 @Test(expected = NullPointerException.class)
 public void testGetFactory_WhenFactoryIsNotSet_ReturnsNull() {
 result.setFactory(null);
 JDOMFactory actualFactory = result.getFactory();
 assertNull(actualFactory);
 }

 @Test
 public void testSetResult_SetsTheResultList() {
 List<Content> testResult = new ArrayList<>();
 result.setResult(testResult);
 List<Content> actualResult = result.getResult();
 assertEquals(testResult, actualResult);
 }

 @Test
 public void testSetDocument_SetsTheDocument() {
 Document testDocument = new JDOMDocument();
 result.setDocument(testDocument);
 Document actualDocument = result.getDocument();
 assertEquals(testDocument, actualDocument);
 }

 @Test(expected = NullPointerException.class)
 public void testSetDocument_ThrowsExceptionWhenDocumentIsNull() {
 result.setDocument(null);
 }

 @Test
 public void testRetrieveResult_RetrievesResultFromHandler() {
 result.retrieveResult();
 result.setDocument(null);
 result.setResult(null);
 JDOMFactory factory = new DefaultJDOMFactory();
 result.setFactory(factory);
 ContentHandler contentHandler = new ContentHandler() {
 @Override
 public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
 expectedResult.add(new Element(qName));
 }
 };
 result.setHandler(contentHandler);
 try {
 SAXBuilder saxBuilder = new SAXBuilder();
 Document doc = saxBuilder.build(getClass().getResource("/test.xml"));
 SAXResult saxResult = new SAXResult();
 result.setDocument(doc);
 result.setHandler(contentHandler);
 TransformerFactory transformerFactory = TransformerFactory.newInstance();
 Transformer transformer = transformerFactory.newTransformer();
 Source source = new SAXSource(saxResult);
 transformer.transform(source, result);
 } catch (JDOMException | SAXException e) {
 e.printStackTrace();
 }
 List<Content> actualResult = result.getResult();
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocument_ThrowsRuntimeExceptionWhenFailedToCreateDocument() {
 result.setFactory(null);
 Document actualDoc = result.getDocument();
 assertNull(actualDoc);
 }
}