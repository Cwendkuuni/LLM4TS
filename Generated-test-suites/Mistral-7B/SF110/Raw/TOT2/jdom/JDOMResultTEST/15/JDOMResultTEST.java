
import org.junit.Test;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.sax.SAXBuilder;
import org.jdom2.transform.JDOMResult;
import org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JDOMResultTest {

   @Test
   public void testConstructor() {
      new JDOMResult();
   }

   @Test
   public void testSetResult() {
      JDOMResult result = new JDOMResult();
      List<Content> resultlist = new ArrayList<>();
      result.setResult(resultlist);
      List<Content> actualResult = result.getResult();

      assertEquals(resultlist, actualResult);
   }

   @Test
   public void testGetResultEmptyResult() {
      JDOMResult result = new JDOMResult();
      result.retrieveResult();
      List<Content> actualResult = result.getResult();

      assertNotNull(actualResult);
      assertEquals(Collections.emptyList(), actualResult);
   }

   @Test
   public void testGetResultWithDocument() {
      JDOMResult result = new JDOMResult();
      SAXBuilder builder = new SAXBuilder();
      Document doc = builder.build(getClass().getResource("input.xml"));
      result.setDocument(doc);
      result.retrieveResult();
      List<Content> actualResult = result.getResult();

      assertNotNull(actualResult);
      assertEquals(doc.getRootElement().getContent(), actualResult);
   }

   @Test(expected = JDOMException.class)
   public void testGetDocumentNullResultdoc() {
      JDOMResult result = new JDOMResult();
      result.setDocument(null);
      Document doc = result.getDocument();
      assertNull(doc);
   }

   @Test
   public void testGetDocumentEmptyResultlist() {
      JDOMResult result = new JDOMResult();
      Document doc = new Document();
      result.setDocument(doc);
      result.setResult(Collections.emptyList());
      Document actualDoc = result.getDocument();

      assertNotNull(actualDoc);
      assertEquals(doc, actualDoc);
   }

   @Test
   public void testGetDocumentEmptyResultlistQueried() {
      JDOMResult result = new JDOMResult();
      Document doc = new Document();
      result.setDocument(doc);
      result.retrieveResult();
      result.setResult(Collections.emptyList());
      Document actualDoc = result.getDocument();

      assertNotNull(actualDoc);
      assertEquals(doc, actualDoc);
   }

   @Test
   public void testGetDocumentResultlistNotEmptyQueried() {
      JDOMResult result = new JDOMResult();
      Document doc = new Document();
      List<Content> resultlist = new ArrayList<>();
      result.setDocument(doc);
      result.setResult(resultlist);
      result.retrieveResult();
      Document actualDoc = result.getDocument();

      assertNotNull(actualDoc);
      assertNotEquals(doc, actualDoc);
   }

   @Test(expected = JDOMException.class)
   public void testGetDocumentResultdocNotEmptyQueried() {
      JDOMResult result = new JDOMResult();
      Document doc = new Document();
      Document resultdoc = new Document();
      result.setDocument(doc);
      result.setResult(null);
      result.retrieveResult();
      result.setDocument(resultdoc);
      Document actualDoc = result.getDocument();

      assertNotNull(actualDoc);
      assertEquals(resultdoc, actualDoc);
   }
}
