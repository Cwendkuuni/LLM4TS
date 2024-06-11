import org.jdom2.JDOMFactory;
import org.jdom2.Document;
import org.jdom2.Content;
import org.jdom2.Element;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private List<Content> emptyList;
 private List<Content> listWithContents;
 private Document document;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 emptyList = Collections.emptyList();
 listWithContents = new ArrayList<>();
 listWithContents.add(new Element("content"));
 document = new JDOMFactory().document(new Element("root"));
 }

 @Test
 public void testConstructor() {
 JDOMResult jdomResult = new JDOMResult();
 assertNotNull(jdomResult.getFactory());
 assertNull(jdomResult.getResult());
 assertFalse(jdomResult.isQueried());
 }

 @Test
 public void testSetResult() {
 jdomResult.setResult(listWithContents);
 assertSame(listWithContents, jdomResult.getResult());
 assertFalse(jdomResult.isQueried());
 }

 @Test
 public void testGetResultEmptyList() {
 jdomResult.setResult(emptyList);
 assertEquals(emptyList, jdomResult.getResult());
 assertFalse(jdomResult.isQueried());
 }

 @Test
 public void testGetResultDocument() {
 jdomResult.setDocument(document);
 assertEquals(document.getContent(), jdomResult.getResult());
 assertFalse(jdomResult.isQueried());
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocumentNullResult() {
 jdomResult.setResult(null);
 jdomResult.getDocument();
 }

 @Test
 public void testGetDocumentEmptyResult() {
 jdomResult.setResult(emptyList);
 Document doc = jdomResult.getDocument();
 assertNull(doc);
 }

 @Test
 public void testGetDocumentWithContents() {
 jdomResult.setResult(listWithContents);
 Document doc = jdomResult.getDocument();
 assertNotNull(doc);
 assertEquals(listWithContents, doc.getContent());
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocumentWithoutFactory() {
 jdomResult.setResult(listWithContents);
 jdomResult.setFactory(null);
 jdomResult.getDocument();
 }

 @Test
 public void testGetFactory() {
 JDOMFactory factory = new JDOMFactory();
 jdomResult.setFactory(factory);
 JDOMFactory resultFactory = jdomResult.getFactory();
 assertSame(factory, resultFactory);
 }

 @Test
 public void testRetrieveResult() {
 jdomResult.setResult(null);
 jdomResult.retrieveResult();
 assertNotNull(jdomResult.getResult());
 assertFalse(jdomResult.isQueried());
 }

 @Test
 public void testSetHandler() {
 // The method is empty, no testing needed
 }

 @Test
 public void testSetLexicalHandler() {
 // The method is empty, no testing needed
 }

}