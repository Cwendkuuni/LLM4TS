import org.jdom2.JDOMFactory;
import org.jdom2.input.SAXBuilder;
import org.jdom2.transform.JDOMResult;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private JDOMFactory factory;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 factory = new DefaultJDOMFactory();
 }

 @Test
 public void testSetResult() throws SAXException {
 // Test setting a valid list of Content
 List<JDOMResult.Content> contentList = new ArrayList<>();
 contentList.add(new JDOMResult.Content("test1"));
 jdomResult.setResult(contentList);
 List<JDOMResult.Content> result = jdomResult.getResult();
 Assert.assertEquals(contentList, result);

 // Test setting a null list
 jdomResult.setResult(null);
 result = jdomResult.getResult();
 Assert.assertEquals(Collections.EMPTY_LIST, result);
 }

 @Test
 public void testSetDocument() throws SAXException {
 // Test setting a valid Document
 Document document = new SAXBuilder().build(new StringReader("<root><test>Test</test></root>"));
 jdomResult.setDocument(document);
 Document resultDoc = jdomResult.getDocument();
 Assert.assertEquals(document, resultDoc);

 // Test setting a null Document
 jdomResult.setDocument(null);
 Document result = jdomResult.getDocument();
 Assert.assertNull(result);
 }

 @Test
 public void testGetFactory() {
 JDOMFactory resultFactory = jdomResult.getFactory();
 Assert.assertNotNull(resultFactory);
 }

 @Test(expected = RuntimeException.class)
 public void testGetFactoryWithException() {
 jdomResult.setFactory(null);
 JDOMFactory resultFactory = jdomResult.getFactory();
 }

 @Test
 public void testRetrieveResult() throws SAXException {
 // Test retrieving the result when both resultlist and resultdoc are null
 jdomResult.retrieveResult();
 List<JDOMResult.Content> result = jdomResult.getResult();
 Assert.assertEquals(Collections.EMPTY_LIST, result);

 // Test retrieving the result when resultlist is not null
 List<JDOMResult.Content> contentList = new ArrayList<>();
 contentList.add(new JDOMResult.Content("test1"));
 jdomResult.setResult(contentList);
 result = jdomResult.getResult();
 Assert.assertEquals(contentList, result);

 // Test retrieving the result when resultdoc is not null
 Document document = new SAXBuilder().build(new StringReader("<root><test>Test</test></root>"));
 jdomResult.setDocument(document);
 result = jdomResult.getResult();
 Assert.assertEquals(document.getContent(), result);
 }
}