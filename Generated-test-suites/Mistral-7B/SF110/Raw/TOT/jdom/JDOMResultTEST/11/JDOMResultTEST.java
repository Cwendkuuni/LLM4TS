import org.junit.Before;
import org.junit.Test;
import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.xml.sax.SAXException;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private JDOMFactory factory;
 private Document document;
 private List<Content> expectedResult;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 factory = new DefaultJDOMFactory();
 jdomResult.setFactory(factory);
 }

 @Test
 public void testGetResult_EmptyResultlist() {
 jdomResult.setResult(Collections.emptyList());
 List<Content> result = jdomResult.getResult();
 assertNull(result);
 }

 @Test
 public void testGetResult_EmptyDocument() {
 jdomResult.setDocument(DocumentHelper.createDocument(""));
 List<Content> result = jdomResult.getResult();
 assertNull(result);
 }

 @Test
 public void testGetResult_NonEmptyResultlist() throws SAXException {
 String xml = "<root><element1>Test1</element1><element2>Test2</element2></root>";
 InputSource inputSource = new InputSource(new StringReader(xml));
 SAXBuilder builder = new SAXBuilder();
 document = builder.build(inputSource);
 jdomResult.setResult(document.getContent());
 List<Content> result = jdomResult.getResult();
 expectedResult = new ArrayList<>();
 expectedResult.add(document.getRootElement());
 expectedResult.add(document.getRootElement().getChild("element1"));
 expectedResult.add(document.getRootElement().getChild("element2"));
 assertArrayEquals(expectedResult.toArray(), result.toArray());
 }

 @Test(expected = RuntimeException.class)
 public void testGetResult_NullDocument() {
 jdomResult.setDocument(null);
 List<Content> result = jdomResult.getResult();
 assertNull(result);
 }
}