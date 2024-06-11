import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.transform.JDOMResult;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class JDOMResultTest {

    @Test
    public void testSetResult() throws JDOMException, SAXException {
        JDOMResult jdomResult = new JDOMResult();
        List<JDOMResult.Content> expectedContent = new ArrayList<>();
        expectedContent.add(new JDOMResult.Content("test1", "test1 value"));
        expectedContent.add(new JDOMResult.Content("test2", "test2 value"));

        SAXBuilder builder = new SAXBuilder();
        String xmlString = "<root><content name=\"test1\">test1 value</content><content name=\"test2\">test2 value</content></root>";
        jdomResult.setResult(expectedContent);
        StringReader reader = new StringReader(xmlString);
        builder.build(reader);
        List<JDOMResult.Content> actualContent = jdomResult.getResult();

        Assert.assertArrayEquals(expectedContent.toArray(), actualContent.toArray());
    }

    @Test
    public void testGetDocument() throws JDOMException, SAXException {
        JDOMResult jdomResult = new JDOMResult();
        Document expectedDocument = new org.jdom2.Document("testDocument");
        expectedDocument.setContent(new ArrayList<JDOMResult.Content>() {{
            add(new JDOMResult.Content("test1", "test1 value"));
            add(new JDOMResult.Content("test2", "test2 value"));
        }});

        SAXBuilder builder = new SAXBuilder();
        String xmlString = "<root><content name=\"test1\">test1 value</content><content name=\"test2\">test2 value</content></root>";
        jdomResult.setResult(null);
        StringReader reader = new StringReader(xmlString);
        builder.build(reader);
        jdomResult.setDocument(expectedDocument);
        Document actualDocument = jdomResult.getDocument();

        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test(expected = NullPointerException.class)
    public void testGetDocumentNullResult() throws JDOMException, SAXException {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setResult(null);
        jdomResult.getDocument();
    }

    @Test(expected = NullPointerException.class)
    public void testGetDocumentNullDocument() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setDocument(null);
        jdomResult.getDocument();
    }

    @Test
    public void testSetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        JDOMFactory actualFactory = jdomResult.getFactory();

        Assert.assertEquals(factory, actualFactory);
    }

    @Test
    public void testGetFactoryNull() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setFactory(null);
        JDOMFactory actualFactory = jdomResult.getFactory();

        Assert.assertNull(actualFactory);
    }
}