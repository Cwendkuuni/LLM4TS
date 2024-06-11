import org.jdom2.JDOMFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Content;
import org.jdom2.input.SAXBuilder;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private JDOMFactory factory;
 private Document document;
 private List<Content> result;
 private List<Content> expectedResult;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 factory = new JDOMFactory();
 }

 @Test
 public void testSetResultAndGetResult() throws SAXException {
 result = new ArrayList<>();
 result.add(new Element("test"));
 jdomResult.setResult(result);
 expectedResult = jdomResult.getResult();
 assertEquals(result, expectedResult);
 }

 @Test
 public void testSetDocumentAndGetDocument() throws SAXException {
 document = new SAXBuilder().build(this.getClass().getResourceAsStream("input.xml"));
 jdomResult.setDocument(document);
 Document actualDocument = jdomResult.getDocument();
 assertNotNull(actualDocument);
 assertEquals(document, actualDocument);
 }

 @Test
 public void testGetResultWhenResultIsEmptyAndDocumentIsSet() throws SAXException {
 jdomResult.setDocument(document);
 List<Content> actualResult = jdomResult.getResult();
 assertFalse(actualResult.isEmpty());
 }

 @Test
 public void testGetResultWhenResultIsNotNullAndDocumentIsNull() throws SAXException {
 result = new ArrayList<>();
 result.add(new Element("test"));
 jdomResult.setResult(result);
 List<Content> actualResult = jdomResult.getResult();
 assertEquals(result, actualResult);
 assertNull(jdomResult.getDocument());
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocumentWhenResultIsEmptyAndDocumentIsNull() throws SAXException {
 Document actualDocument = jdomResult.getDocument();
 assertNull(actualDocument);
 }

 @Test
 public void testGetDocumentWhenResultIsNotNull() throws SAXException {
 result = new ArrayList<>();
 result.add(new Element("test"));
 jdomResult.setResult(result);
 Document actualDocument = jdomResult.getDocument();
 Element root = actualDocument.getRootElement();
 assertEquals("test", root.getName());
 }

 @Test
 public void testGetFactory() {
 JDOMFactory actualFactory = jdomResult.getFactory();
 assertEquals(factory, actualFactory);
 }

 @Test
 public void testRetrieveResult() throws SAXException {
 jdomResult.setDocument(document);
 List<Content> actualResult = jdomResult.getResult();
 assertFalse(actualResult.isEmpty());
 }
}