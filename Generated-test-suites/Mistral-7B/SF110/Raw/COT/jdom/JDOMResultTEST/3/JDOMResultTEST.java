import org.junit.Test;
import org.junit.Assert;
import org.jdom2.*;
import org.jdom2.transform.JDOMResult;
import org.jdom2.input.sax.SAXBuilder;
import org.xml.sax.SAXException;

public class JDOMResultTest {

    @Test
    public void testConstructor() {
        JDOMResult jdomResult = new JDOMResult();
        Assert.assertNull(jdomResult.getResult());
        Assert.assertNull(jdomResult.getDocument());
        Assert.assertFalse(jdomResult.isQueried());
        Assert.assertNull(jdomResult.getFactory());
    }

    @Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("test"));
        jdomResult.setResult(result);
        Assert.assertEquals(result, jdomResult.getResult());
        Assert.assertNull(jdomResult.getDocument());
        Assert.assertFalse(jdomResult.isQueried());
        Assert.assertNull(jdomResult.getFactory());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetResultNull() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setResult(null);
    }

    @Test
    public void testGetDocumentNullResult() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setResult(null);
        Assert.assertNull(jdomResult.getDocument());
    }

    @Test
    public void testGetDocumentEmptyResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        jdomResult.setResult(result);
        Assert.assertNull(jdomResult.getDocument());
    }

    @Test
    public void testGetDocumentWithResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("test"));
        jdomResult.setResult(result);
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        Document doc = jdomResult.getDocument();
        Assert.assertNotNull(doc);
        Assert.assertEquals(result.get(0), doc.getRootElement());
        jdomResult.retrieveResult();
        Assert.assertTrue(jdomResult.isQueried());
    }

    @Test(expected = RuntimeException.class)
    public void testGetDocumentWithNullFactory() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("test"));
        jdomResult.setResult(result);
        jdomResult.setFactory(null);
        Document doc = jdomResult.getDocument();
    }

    @Test
    public void testSetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document doc = new Document();
        jdomResult.setDocument(doc);
        Assert.assertEquals(doc, jdomResult.getDocument());
        Assert.assertNull(jdomResult.getResult());
        Assert.assertFalse(jdomResult.isQueried());
        Assert.assertNull(jdomResult.getFactory());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDocumentNull() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setDocument(null);
    }

    @Test
    public void testRetrieveResultNullResultAndDocument() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setResult(null);
        jdomResult.setDocument(null);
        jdomResult.retrieveResult();
        Assert.assertNull(jdomResult.getResult());
        Assert.assertNull(jdomResult.getDocument());
        Assert.assertFalse(jdomResult.isQueried());
    }

    @Test
    public void testRetrieveResultEmptyResultAndDocument() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        jdomResult.setResult(result);
        Document doc = new Document();
        jdomResult.setDocument(doc);
        jdomResult.retrieveResult();
        Assert.assertSame(result, jdomResult.getResult());
        Assert.assertEquals(doc, jdomResult.getDocument());
        Assert.assertTrue(jdomResult.isQueried());
    }

    @Test(expected = SAXException.class)
    public void testDocumentBuilder() throws SAXException {
        JDOMResult jdomResult = new JDOMResult();
        SAXBuilder builder = new SAXBuilder();
        jdomResult.setHandler(builder);
        builder.build(new StringReader(""));
    }
}