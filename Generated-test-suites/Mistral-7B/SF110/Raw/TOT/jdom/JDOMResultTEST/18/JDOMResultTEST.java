import org.jdom2.JDOMException;
import org.jdom2.Document;
import org.jdom2.Content;
import org.jdom2.Element;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import org.jdom2.transform.JDOMResult;
import org.jdom2.input.sax.SAXBuilder;
import org.xml.sax.helpers.DefaultHandler;

public class JDOMResultTest {

 private JDOMResult result;
 private List<Content> expectedResult;
 private Document inputDocument;

 @Test
 public void testGetResult_EmptyResultlist() {
 result = new JDOMResult();
 assertNull(result.getResult());
 }

 @Test
 public void testGetResult_ResultDocNotFound() {
 result = new JDOMResult();
 result.setDocument(null);
 result.retrieveResult();
 assertNotNull(result.getResult());
 assertFalse(result.getDocument() instanceof Document);
 }

 @Test
 public void testGetResult_ResultDocFound() throws JDOMException {
 SAXBuilder builder = new SAXBuilder();
 inputDocument = builder.build("input.xml");
 result = new JDOMResult();
 result.setDocument(inputDocument);
 result.retrieveResult();
 expectedResult = new ArrayList<>();
 expectedResult.add(inputDocument.getRootElement());
 assertNotNull(result.getResult());
 assertEquals(expectedResult.size(), result.getResult().size());
 for (Content c : result.getResult()) {
 assertEquals(expectedResult.get(0), c);
 expectedResult.remove(0);
 }
 }

 @Test(expected = RuntimeException.class)
 public void testGetResult_DocumentCannotBeCreated() throws JDOMException {
 result = new JDOMResult();
 result.setDocument(null);
 result.retrieveResult();
 result.getDocument();
 }

 @Test
 public void testSetResult() {
 List<Content> input = new ArrayList<>();
 input.add(new Element("test"));
 result = new JDOMResult();
 result.setResult(input);
 assertEquals(input, result.getResult());
 }

 @Test
 public void testSetResult_DocumentNotFound() {
 result = new JDOMResult();
 List<Content> input = new ArrayList<>();
 result.setResult(input);
 assertNull(result.getDocument());
 }

 @Test
 public void testSetResult_DocumentPresent() throws JDOMException {
 result = new JDOMResult();
 Document inputDocument = builder.build("input.xml");
 result.setResult(inputDocument.getContent());
 assertNotNull(result.getDocument());
 }

 @Test
 public void testSetFactory() {
 JDOMResult result = new JDOMResult();
 DefaultHandler handler = new DefaultHandler();
 assertNull(result.getFactory());
 result.setFactory(new DefaultJDOMFactory());
 assertEquals(DefaultJDOMFactory.class, result.getFactory().getClass());
 }
}