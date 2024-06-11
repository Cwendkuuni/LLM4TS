import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.transform.JDOMResult;
import org.jdom2.transform.JDOMResult.FragmentHandler;
import org.junit.Assert;
import org.junit.Test;

public class JDOMResultTest {

 @Test
 public void testSetResult() throws JDOMException {
 JDOMResult result = new JDOMResult();
 List<JDOMResult.Content> expectedResult = new ArrayList<>();
 expectedResult.add(new JDOMResult.Element("element1", null, null));
 expectedResult.add(new JDOMResult.Text("text1"));
 expectedResult.add(new JDOMResult.Element("element2", null, null));
 result.setResult(expectedResult);
 List<JDOMResult.Content> actualResult = result.getResult();
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSetDocument() throws JDOMException {
 SAXBuilder builder = new SAXBuilder();
 Document document = builder.build(getClass().getResourceAsStream("/test.xml"));
 JDOMResult result = new JDOMResult();
 result.setDocument(document);
 Document actualDocument = result.getDocument();
 Assert.assertEquals(document, actualDocument);
 }

 @Test
 public void testEmptyResult() {
 JDOMResult result = new JDOMResult();
 result.setResult(null);
 List<JDOMResult.Content> actualResult = result.getResult();
 Assert.assertEquals(Collections.emptyList(), actualResult);
 }

 @Test(expected = NullPointerException.class)
 public void testNullFactory() {
 JDOMResult result = new JDOMResult();
 result.setFactory(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullDocument() {
 JDOMResult result = new JDOMResult();
 result.setDocument(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullResultlist() {
 JDOMResult result = new JDOMResult();
 result.setResult(null);
 result.getResult();
 }

 @Test(expected = IllegalStateException.class)
 public void testQueriedTwice() {
 JDOMResult result = new JDOMResult();
 result.setResult(Collections.emptyList());
 List<JDOMResult.Content> firstResult = result.getResult();
 result.setResult(Collections.singletonList(new JDOMResult.Element("test", null, null)));
 List<JDOMResult.Content> secondResult = result.getResult();
 Assert.assertNotSame(firstResult, secondResult);
 result.getResult(); // Queries the result a third time, causing an IllegalStateException
 }

 @Test
 public void testFragmentHandlerResult() throws JDOMException, SAXException {
 JDOMResult result = new JDOMResult();
 FragmentHandler handler = new FragmentHandler(new DefaultJDOMFactory());
 result.setHandler(handler);
 SAXBuilder builder = new SAXBuilder();
 builder.setEntityResolver(new DefaultSAXEntityResolver());
 builder.setContentHandler(handler);
 builder.build(getClass().getResourceAsStream("/test.xml"));
 List<JDOMResult.Content> actualResult = result.getResult();
 List<JDOMResult.Content> expectedResult = handler.getResult();
 Assert.assertEquals(expectedResult, actualResult);
 }
}