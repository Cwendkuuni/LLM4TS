import org.junit.Test;
import org.jdom2.JDOMException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Content;
import org.jdom2.input.SAXBuilder;
import org.jdom2.transform.JDOMResult;
import org.xml.sax.SAXException;

import java.io.StringReader;

public class JDOMResultTest {

 private JDOMResult result;
 private SAXBuilder saxBuilder;

 @Test
 public void testGetResultEmpty() throws JDOMException, SAXException {
 // Test case for getting an empty result list when no content is set
 JDOMResult testResult = new JDOMResult();
 result = testResult;

 saxBuilder = new SAXBuilder();
 String xmlString = "<root></root>";
 saxBuilder.build(new StringReader(xmlString), result);

 List<Content> resultList = result.getResult();
 assert (resultList.size() == 0);
 }

 @Test
 public void testGetResultWithContent() throws JDOMException, SAXException {
 // Test case for getting the content list when content is set
 JDOMResult testResult = new JDOMResult();
 result = testResult;

 saxBuilder = new SAXBuilder();
 String xmlString = "<root><child1>Content 1</child1><child2>Content 2</child2></root>";
 saxBuilder.build(new StringReader(xmlString), result);

 List<Content> resultList = result.getResult();
 assert (resultList.size() == 2);

 Content child1 = resultList.get(0);
 assert (child1 instanceof Element);
 Element element1 = (Element) child1;
 assert element1.getName().equals("child1");
 assert element1.getText().equals("Content 1");

 Content child2 = resultList.get(1);
 assert (child2 instanceof Element);
 Element element2 = (Element) child2;
 assert element2.getName().equals("child2");
 assert element2.getText().equals("Content 2");
 }

 @Test(expected = java.util.NoSuchElementException.class)
 public void testGetResultWithDocument() throws JDOMException, SAXException {
 // Test case for getting the content list when a document is set
 JDOMResult testResult = new JDOMResult();
 result = testResult;

 Document doc = new Document();
 doc.setContent(Arrays.asList(new Element("root"), new Element("child")));
 result.setDocument(doc);
 result.getResult(); // This line should throw NoSuchElementException as resultlist is null
 }

 @Test(expected = NullPointerException.class)
 public void testSetDocumentNull() {
 // Test case for setting a null document
 JDOMResult testResult = new JDOMResult();
 result = testResult;
 result.setDocument(null); // This line should throw NullPointerException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetResultNull() {
 // Test case for setting a null result list
 JDOMResult testResult = new JDOMResult();
 result = testResult;
 result.setResult(null); // This line should throw IllegalArgumentException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetResultEmptyList() {
 // Test case for setting an empty result list
 JDOMResult testResult = new JDOMResult();
 result = testResult;
 List<Content> emptyResultList = Collections.emptyList();
 result.setResult(emptyResultList); // This line should throw IllegalArgumentException
 }
}