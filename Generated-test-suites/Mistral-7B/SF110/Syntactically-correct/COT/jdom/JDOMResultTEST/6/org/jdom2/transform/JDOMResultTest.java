import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;
import org.jdom2.input.sax.FragmentHandler;
import org.jdom2.transform.JDOMResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private Document document;
 private JDOMFactory factory;

 @Before
 public void setup() {
 jdomResult = new JDOMResult();
 document = new Document();
 factory = new DefaultJDOMFactory();
 }

 @Test
 public void testSetResult() {
 List<Content> result = new ArrayList<>();
 result.add(new Element("test"));
 jdomResult.setResult(result);
 List<Content> expectedResult = jdomResult.getResult();
 Assert.assertEquals(result.size(), expectedResult.size());
 for (int i = 0; i < result.size(); i++) {
 Content actualContent = expectedResult.get(i);
 Content expectedContent = result.get(i);
 Assert.assertEquals(expectedContent, actualContent);
 }
 }

 @Test
 public void testGetResultWhenResultDocIsSet() throws SAXException {
 List<Content> content = new ArrayList<>();
 content.add(new Element("test"));
 document.setContent(content);
 jdomResult.setDocument(document);
 List<Content> expectedResult = jdomResult.getResult();
 Assert.assertEquals(content.size(), expectedResult.size());
 for (int i = 0; i < content.size(); i++) {
 Content actualContent = expectedResult.get(i);
 Content expectedContent = content.get(i);
 Assert.assertEquals(expectedContent, actualContent);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testGetResultWhenBothResultListAndResultDocAreNull() {
 jdomResult.retrieveResult();
 jdomResult.getResult();
 }

 @Test
 public void testSetFactory() {
 JDOMFactory givenFactory = new JDOMFactory() {
 @Override
 public Document document(final Node node) throws SAXException {
 return new Document();
 }
 };
 jdomResult.setFactory(givenFactory);
 JDOMFactory expectedFactory = jdomResult.getFactory();
 Assert.assertEquals(givenFactory, expectedFactory);
 }

 @Test
 public void testGetFactoryWhenFactoryIsNotSet() {
 JDOMFactory expectedFactory = new DefaultJDOMFactory();
 JDOMFactory actualFactory = jdomResult.getFactory();
 Assert.assertEquals(expectedFactory, actualFactory);
 }

 @Test(expected = NullPointerException.class)
 public void testSetHandlerWithNullHandler() {
 jdomResult.setHandler(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetLexicalHandlerWithNullLexicalHandler() {
 jdomResult.setLexicalHandler(null);
 }

 @Test
 public void testRetrieveResultWhenResultListIsSet() {
 List<Content> result = new ArrayList<>();
 result.add(new Element("test"));
 jdomResult.setResult(result);
 jdomResult.retrieveResult();
 List<Content> expectedResult = jdomResult.getResult();
 Assert.assertEquals(result.size(), expectedResult.size());
 for (int i = 0; i < result.size(); i++) {
 Content actualContent = expectedResult.get(i);
 Content expectedContent = result.get(i);
 Assert.assertEquals(expectedContent, actualContent);
 }
 }

 @Test
 public void testRetrieveResultWhenResultDocIsSet() throws SAXException {
 List<Content> content = new ArrayList<>();
 content.add(new Element("test"));
 document.setContent(content);
 jdomResult.setDocument(document);
 jdomResult.retrieveResult();
 List<Content> expectedResult = jdomResult.getResult();
 Assert.assertEquals(content.size(), expectedResult.size());
 for (int i = 0; i < content.size(); i++) {
 Content actualContent = expectedResult.get(i);
 Content expectedContent = content.get(i);
 Assert.assertEquals(expectedContent, actualContent);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testGetDocumentWhenBothResultListAndResultDocAreSet() {
 jdomResult.setResult(new ArrayList<>());
 jdomResult.setDocument(document);
 jdomResult.getDocument();
 }

 @Test
 public void testGetDocumentWhenBothResultListAndResultDocAreNull() {
 jdomResult.retrieveResult();
 Document expectedDocument = jdomResult.getDocument();
 Assert.assertNull(expectedDocument);
 }

 @Test
 public void testGetDocumentWhenResultListIsSet() throws SAXException {
 List<Content> result = new ArrayList<>();
 result.add(new Element("test"));
 jdomResult.setResult(result);
 Document expectedDocument = jdomResult.getDocument();
 Document actualDocument = new Document();
 actualDocument.setContent(result);
 Assert.assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testGetDocumentWhenResultDocIsSet() throws SAXException {
 List<Content> content = new ArrayList<>();
 content.add(new Element("test"));
 document.setContent(content);
 jdomResult.setDocument(document);
 Document expectedDocument = jdomResult.getDocument();
 Document actualDocument = document;
 Assert.assertEquals(expectedDocument, actualDocument);
 }
}