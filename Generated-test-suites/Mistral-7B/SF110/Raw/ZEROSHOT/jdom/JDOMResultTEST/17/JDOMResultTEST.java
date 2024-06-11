import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Content;
import org.jdom2.filter.Filters;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JDOMResultTest {

 private JDOMResult result;
 private List<Content> contentList;
 private Document document;
 private JDOMFactory factory;

 @Before
 public void setup() {
 factory = new DefaultJDOMFactory();
 result = new JDOMResult();
 }

 @Test
 public void testSetResult() {
 contentList = new ArrayList<>();
 contentList.add(new Element("element1"));
 result.setResult(contentList);
 assertEquals(contentList, result.getResult());
 }

 @Test
 public void testGetResultWhenResultDocIsNotNull() throws JDOMException {
 Document inputDocument = new SAXBuilder().build(getClass().getResource("/test.xml"));
 result.setDocument(inputDocument);
 List<Content> expectedResult = new ArrayList<>();
 expectedResult.addAll(Filters.elementFilter("elementName", inputDocument.getRootElement().getChildren()));
 assertEquals(expectedResult, result.getResult());
 }

 @Test
 public void testGetResultWhenResultDocIsNull() {
 result.setResult(null);
 assertNotNull(result.getResult());
 assertEquals(Collections.emptyList(), result.getResult());
 }

 @Test
 public void testSetDocument() {
 Document inputDocument = new SAXBuilder().build(getClass().getResource("/test.xml"));
 result.setDocument(inputDocument);
 Document expectedDocument = result.getDocument();
 assertEquals(inputDocument, expectedDocument);
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocumentWhenResultListIsNotNull() throws JDOMException {
 result.setResult(new ArrayList<>());
 result.setDocument(null);
 result.getDocument();
 }

 @Test
 public void testGetFactory() {
 JDOMFactory expectedFactory = new DefaultJDOMFactory();
 result.setFactory(expectedFactory);
 JDOMFactory actualFactory = result.getFactory();
 assertEquals(expectedFactory, actualFactory);
 }

 @Test
 public void testRetrieveResult() {
 result.retrieveResult();
 assertNotNull(result.getResult());
 }
}