package org.jdom2.transform;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.xml.sax.helpers.DefaultHandler;

import java.io.StringReader;
import java.util.List;

public class JDOMResultTest extends TestCase {

 private JDOMResult jdomResult;
 private SAXBuilder saxBuilder;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 saxBuilder = new SAXBuilder();
 }

 @Test
 public void testSetResult() {
 // Given: an empty JDOMResult object
 List<Content> expectedResult = new ArrayList<>();
 Content textContent = new Text("Hello World");
 expectedResult.add(textContent);

 // When: setting a non-null list of Contents as result
 List<Content> inputResult = new ArrayList<>();
 inputResult.add(textContent);
 jdomResult.setResult(inputResult);

 // Then: the getResult method should return the set result
 List<Content> actualResult = jdomResult.getResult();
 assertEquals("Size of the result list should be equal", expectedResult.size(), actualResult.size());
 for (int i = 0; i < expectedResult.size(); i++) {
 Content expectedContent = expectedResult.get(i);
 Content actualContent = actualResult.get(i);
 assertEquals("Content of the result list should be equal", expectedContent, actualContent);
 }
 }

 @Test
 public void testSetDocument() {
 // Given: an empty JDOMResult object
 Document expectedDocument = new JDOMFactory().newDocument();
 expectedDocument.setContent(new Element("root", null, null).addContent(new Text("Hello World")));

 // When: setting a Document as result
 jdomResult.setDocument(expectedDocument);

 // Then: the getDocument method should return the set Document
 Document actualDocument = jdomResult.getDocument();
 assertEquals("The Document objects should be equal", expectedDocument, actualDocument);
 }

 @Test
 public void testGetResultWhenQueriedForTheFirstTime() {
 // Given: an empty JDOMResult object with no result yet

 // When: calling getResult for the first time
 List<Content> actualResult = jdomResult.getResult();

 // Then: an empty list should be returned
 assertTrue("The result list should be empty", actualResult.isEmpty());
 }

 @Test
 public void testGetResultWhenQueriedForTheSecondTime() {
 // Given: an JDOMResult object with a set Document
 Document document = new JDOMFactory().newDocument();
 document.setContent(new Element("root", null, null).addContent(new Text("Hello World")));
 jdomResult.setDocument(document);

 // When: calling getResult for the second time
 List<Content> actualResult = jdomResult.getResult();

 // Then: the result list should contain the Document's content
 List<Content> expectedResult = new ArrayList<>(document.getContent().size());
 Content actualDocumentContent = document.getContent().get(0);
 expectedResult.add(actualDocumentContent);
 assertEquals("The result list should be equal to the Document's content", expectedResult, actualResult);
 }

 @Test
 public void testSetFactory() {
 // Given: an empty JDOMResult object
 JDOMFactory factory = new DefaultJDOMFactory();

 // When: setting a custom JDOMFactory
 jdomResult.setFactory(factory);

 // Then: the getFactory method should return the set factory
 JDOMFactory actualFactory = jdomResult.getFactory();
 assertEquals("The factories should be equal", factory, actualFactory);
 }

 @Test
 public void testRetrieveResult() {
 // Given: an empty JDOMResult object with no result yet

 // When: calling getResult multiple times
 List<Content> firstResult = jdomResult.getResult();
 List<Content> secondResult = jdomResult.getResult();
 List<Content> thirdResult = jdomResult.getResult();

 // Then: the result should be the same for all calls
 assertEquals("The result should be the same for all calls", firstResult, secondResult);
 assertEquals("The result should be the same for all calls", firstResult, thirdResult);
 }

 @Test
 public void testHandlingEmptyDocument() {
 // Given: an empty JDOMResult object with no result yet
 // and no Document set
 jdomResult.setDocument(null);

 // When: calling getResult
 List<Content> actualResult = jdomResult.getResult();

 // Then: an empty list should be returned
 assertTrue("The result list should be empty", actualResult.isEmpty());
 }

 @Test
 public void testHandlingNullResultList() {
 // Given: an empty JDOMResult object with no result yet
 // and a null result list set
 jdomResult.setResult(null);
 // When: calling getResult
 List<Content> actualResult = jdomResult.getResult();
 // Then: an empty list should be returned
 assertTrue("The result list should be empty", actualResult.isEmpty());
 }
}