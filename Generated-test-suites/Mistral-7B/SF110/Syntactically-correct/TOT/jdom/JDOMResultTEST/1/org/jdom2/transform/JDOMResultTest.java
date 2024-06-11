import org.jdom2.JDOMException;
import org.jdom2.transform.JDOMResult;
import org.jdom2.transform.SAXTransformer;
import org.jdom2.Document;
import org.jdom2.Element;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private SAXTransformer transformer;
 private Document inputDocument;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 transformer = new SAXTransformer(new JDOMResultHandler());
 inputDocument = new Document("rootElement");
 jdomResult.setFactory(new JDOMFactory());
 }

 @Test
 public void testGetResult_EmptyList() {
 jdomResult.setResult(Collections.emptyList());
 List<Content> result = jdomResult.getResult();
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testGetResult_Document() {
 jdomResult.setDocument(inputDocument);
 List<Content> result = jdomResult.getResult();
 assertEquals(inputDocument.getContent(), result);
 }

 @Test
 public void testGetResult_ListAndDocument() {
 List<Content> list = new ArrayList<>();
 list.add(new Element("testElement"));
 jdomResult.setResult(list);
 List<Content> result = jdomResult.getResult();
 assertEquals(list, result);
 }

 @Test(expected = JDOMException.class)
 public void testGetResult_NullList() {
 jdomResult.setResult(null);
 List<Content> result = jdomResult.getResult();
 fail("Expected JDOMException when result is null");
 }

 @Test
 public void testSetResult_List() {
 List<Content> list = new ArrayList<>();
 list.add(new Element("testElement"));
 jdomResult.setResult(list);
 List<Content> result = jdomResult.getResult();
 assertEquals(list, result);
 }

 @Test
 public void testSetResult_Document() {
 Document doc = new Document("rootElement");
 jdomResult.setDocument(doc);
 List<Content> result = jdomResult.getResult();
 assertEquals(doc.getContent(), result);
 }

 @Test
 public void testSetFactory() {
 JDOMFactory factory = new JDOMFactory();
 jdomResult.setFactory(factory);
 JDOMFactory resultFactory = jdomResult.getFactory();
 assertEquals(factory, resultFactory);
 }

 @Test
 public void testRetrieveResult() {
 jdomResult.retrieveResult();
 List<Content> result = jdomResult.getResult();
 assertTrue(resultlist != null || resultdoc != null);
 }

 @Test
 public void testTransformer_EmptyInput() {
 InputSource inputSource = new InputSource(new StringReader(""));
 transformer.transform(inputSource, jdomResult);
 List<Content> result = jdomResult.getResult();
 assertTrue(result.isEmpty());
 }

 @Test
 public void testTransformer_InputWithElement() {
 InputSource inputSource = new InputSource(new StringReader("<root><test></test></root>"));
 transformer.transform(inputSource, jdomResult);
 List<Content> result = jdomResult.getResult();
 assertEquals(1, result.size());
 Content element = result.get(0);
 assertTrue(element instanceof Element);
 assertEquals("test", element.getName());
 }
}