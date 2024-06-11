import org.jdom2.*;
import org.jdom2.input.sax.JDOMBuilder;
import org.jdom2.transform.JDOMResult;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMResultTest {

 private JDOMResult result;
 private JDOMFactory factory;

 @Before
 public void setup() {
 factory = new JDOMFactory();
 result = new JDOMResult();
 }

 @Test
 public void testSetResult() {
 List<Content> expectedContent = new ArrayList<>();
 Content content = new Element("test", new QualifiedName("http://example.com", "test"), null);
 expectedContent.add(content);

 result.setResult(expectedContent);

 List<Content> resultContent = result.getResult();
 assertEquals(expectedContent.size(), resultContent.size());
 for (int i = 0; i < expectedContent.size(); i++) {
 assertEquals(expectedContent.get(i), resultContent.get(i));
 }
 }

 @Test
 public void testSetDocument() {
 Document document = new JDOMBuilder().buildDocument();
 Element root = new Element("root", null, null);
 document.setRootElement(root);
 result.setDocument(document);

 Document resultDocument = result.getDocument();
 assertEquals(document, resultDocument);
 }

 @Test
 public void testGetFactory() {
 JDOMFactory expectedFactory = new JDOMFactory();
 result.setFactory(expectedFactory);
 JDOMFactory actualFactory = result.getFactory();
 assertEquals(expectedFactory, actualFactory);
 }

 @Test(expected = RuntimeException.class)
 public void testGetFactoryWhenNull() {
 result.setFactory(null);
 result.getFactory();
 }

 @Test
 public void testGetResultWhenNull() {
 result.setResult(null);
 List<Content> resultContent = result.getResult();
 assertTrue(resultContent.isEmpty());
 }

 @Test
 public void testGetResultWhenDocument() {
 Document document = new JDOMBuilder().buildDocument();
 result.setDocument(document);
 List<Content> resultContent = result.getResult();
 assertTrue(!resultContent.isEmpty());
 }

 @Test
 public void testRetrieveResult() {
 Document document = new JDOMBuilder().buildDocument();
 result.setDocument(document);
 result.retrieveResult();
 List<Content> resultContent = result.getResult();
 assertTrue(!resultContent.isEmpty());
 }
}