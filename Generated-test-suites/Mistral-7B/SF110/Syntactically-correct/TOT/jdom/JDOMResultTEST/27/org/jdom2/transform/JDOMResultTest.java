import org.junit.Before;
import org.junit.Test;
import org.jdom2.*;
import org.jdom2.transform.JDOMResult;
import org.xml.sax.SAXException;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private List<Content> expectedResult;
 private Document inputDocument;

 @Before
 public void setup() {
 jdomResult = new JDOMResult();
 expectedResult = new ArrayList<>();
 inputDocument = new JAXBDocument.JAXBDocumentParser().parse(new StringReader("<root><element1>Content1</element1><element2><element3>Content3</element3></element2></root>"));
 }

 @Test
 public void testGetResultFromList() {
 jdomResult.setResult(expectedResult);
 List<Content> result = jdomResult.getResult();
 assertEquals("Size of the result list should be equal to the expected result list", expectedResult.size(), result.size());
 assertEquals("The content of the first element in the result list should be equal to the expected result", expectedResult.get(0).getQualifiedName(), result.get(0).getQualifiedName());
 }

 @Test
 public void testGetResultFromDocument() throws SAXException {
 jdomResult.setDocument(inputDocument);
 List<Content> result = jdomResult.getResult();
 assertEquals("Size of the result list should be equal to the number of elements in the input document", inputDocument.getRootElement().getChildren().size(), result.size());
 assertNotNull("The result list should not be empty", result);
 }

 @Test(expected = NullPointerException.class)
 public void testGetResultWhenBothListAndDocumentAreNull() {
 jdomResult.retrieveResult();
}

 @Test(expected = IllegalStateException.class)
 public void testGetResultTwice() {
 jdomResult.setDocument(inputDocument);
 List<Content> result1 = jdomResult.getResult();
 List<Content> result2 = jdomResult.getResult();
 assertEquals("The result list should be the same for each call", result1, result2);
 }

 @Test
 public void testSetFactory() {
 JDOMFactory factory = new DefaultJDOMFactory();
 jdomResult.setFactory(factory);
 assertEquals("The set factory method should set the factory instance variable", factory, jdomResult.getFactory());
 }
}