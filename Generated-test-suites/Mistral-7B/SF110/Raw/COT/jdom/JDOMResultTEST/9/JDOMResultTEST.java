import org.junit.Test;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;
import org.jdom2.input.sax.SAXBuilder;
import org.junit.Assert.*;
import org.xml.sax.SAXException;

public class JDOMResultTest {

 private JDOMResult result;
 private JDOMFactory factory;
 private Document document;

 @Before
 public void setUp() {
 factory = new JDOMFactory();
 result = new JDOMResult();
 }

 @Test
 public void testConstructor() {
 // Check if the constructor is called correctly
 result = new JDOMResult();
 assertNotNull(result.getFactory());
 assertNull(result.getResultlist());
 assertNull(result.getResultdoc());
 assertFalse(result.isQueried());
 }

 @Test
 public void testSetResult() {
 // Test setting result list
 List<Content> list = new ArrayList<>();
 list.add(new Element("test"));
 result.setResult(list);
 assertSame(list, result.getResult());
 // Test setting document
 document = new SAXBuilder().build(new StringReader("<test/>")).getDocument();
 result.setDocument(document);
 assertSame(document, result.getDocument());
 // Test setting result list again after setting document
 list = new ArrayList<>();
 list.add(new Element("test"));
 result.setResult(list);
 assertSame(list, result.getResult());
 }

 @Test
 public void testGetResult() throws SAXException {
 // Test getting empty result
 assertSame(Collections.emptyList(), result.getResult());
 // Test getting result from list
 result.setResult(new ArrayList<Content>() {{
 add(new Element("test"));
 }});
 assertSame(Collections.singletonList(new Element("test")), result.getResult());
 // Test getting result from document
 document = new SAXBuilder().build(new StringReader("<test/>")).getDocument();
 result.setDocument(document);
 assertSame(document.getContent(), result.getResult());
 }

 @Test
 public void testSetDocument() {
 // Test setting document
 document = new SAXBuilder().build(new StringReader("<test/>")).getDocument();
 result.setDocument(document);
 assertSame(document, result.getDocument());
 // Test setting document again after setting result list
 List<Content> list = new ArrayList<>();
 list.add(new Element("test"));
 result.setResult(list);
 result.setDocument(document);
 assertSame(document, result.getDocument());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDocumentNullResult() {
 result.setResult(null);
 result.getDocument();
 }

 @Test(expected = NullPointerException.class)
 public void testGetDocumentNullFactory() {
 result.setFactory(null);
 result.getDocument();
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocumentNullDocument() {
 JDOMFactory factory = new DefaultJDOMFactory();
 result.setFactory(factory);
 result.setDocument(null);
 result.getDocument();
 }

 @Test
 public void testRetrieveResult() {
 // Test retrieving result from document when result list is null
 result.resultdoc = document;
 result.retrieveResult();
 assertNotNull(result.getResultlist());
 // Test retrieving result from result list when document is null
 result.resultlist = new ArrayList<Content>() {{
 add(new Element("test"));
 }};
 result.resultdoc = null;
 result.retrieveResult();
 assertNull(result.getResultdoc());
 }

}