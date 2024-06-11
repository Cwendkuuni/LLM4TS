import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.transform.JDOMResult;
import org.jdom2.transform.SAXTransformer;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JDOMResultTest {

 private JDOMResult result;
 private SAXTransformer transformer;
 private Document inputDocument;

 @Before
 public void setUp() throws JDOMException {
 result = new JDOMResult();
 transformer = new SAXTransformer();
 inputDocument = new SAXBuilder().build(new StringReader("<root><child1><grandchild1/><grandchild2/></child1><child2/></root>"));
 }

 @Test
 public void testGetResult_WhenDocumentIsSet_ReturnDocumentContents() throws Exception {
 result.setDocument(inputDocument);
 List<Content> actualResult = result.getResult();
 List<Content> expectedResult = inputDocument.getContent();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testGetResult_WhenResultListIsSet_ReturnResultList() {
 result.setResult(Collections.singletonList(new Element("element")));
 List<Content> actualResult = result.getResult();
 List<Content> expectedResult = Collections.singletonList(new Element("element"));
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testGetResult_WhenBothDocumentAndResultListAreSet_ReturnDocumentContents() throws Exception {
 result.setDocument(inputDocument);
 result.setResult(Collections.singletonList(new Element("element")));
 List<Content> actualResult = result.getResult();
 List<Content> expectedResult = inputDocument.getContent();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testGetDocument_WhenResultListIsSet_CreateDocumentFromResultList() throws Exception {
 result.setResult(Collections.singletonList(new Element("element")));
 Document actualDocument = result.getDocument();
 Document expectedDocument = new SAXBuilder().build(new ByteArrayInputStream(result.getResult().toXML().getBytes())).getDocument();
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testGetDocument_WhenDocumentIsSet_ReturnDocument() {
 Document actualDocument = result.getDocument();
 Document expectedDocument = inputDocument;
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testGetDocument_WhenNeitherDocumentNorResultListIsSet_ReturnNull() {
 Document actualDocument = result.getDocument();
 assertNull(actualDocument);
 }

 @Test(expected = RuntimeException.class)
 public void testGetFactory_WhenFactoryIsNotSet_ThrowRuntimeException() throws Exception {
 result.getFactory();
 }
}