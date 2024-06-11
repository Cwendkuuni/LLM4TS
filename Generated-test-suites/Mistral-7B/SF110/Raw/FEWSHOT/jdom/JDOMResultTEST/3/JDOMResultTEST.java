package org.jdom2.transform;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.junit.Test;

public class JDOMResultTest {

    @Test
    public void testGetResult() {
        JDOMResult result = new JDOMResult();
        Document doc = new Document();
        Content content = new Element("test");
        doc.setContent(new Element[]{content});
        result.setDocument(doc);
        List<Content> expectedResult = new ArrayList<Content>() {{
            add(content);
        }};
        List<Content> actualResult = result.getResult();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetDocument() {
        JDOMResult result = new JDOMResult();
        Document doc = new Document();
        Content content = new Element("test");
        doc.setContent(new Element[]{content});
        result.setDocument(doc);
        Document expectedDocument = new Document();
        expectedDocument.setContent(new Element[]{content});
        Document actualDocument = result.getDocument();
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> contentList = new ArrayList<Content>() {{
            add(new Element("test1"));
            add(new Element("test2"));
        }};
        result.setResult(contentList);
        List<Content> expectedResult = contentList;
        List<Content> actualResult = result.getResult();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testRetrieveResult() {
        JDOMResult result = new JDOMResult();
        DocumentBuilder builder = new DocumentBuilder();
        String xmlString = "<root><test1/><test2/></root>";
        builder.parse(new InputSource(new StringReader(xmlString)));
        result.setHandler(builder);
        result.retrieveResult();
        List<Content> expectedResult = new ArrayList<Content>() {{
            add(builder.getResult().getDocument().getRootElement());
        }};
        List<Content> actualResult = result.getResult();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        result.setFactory(factory);
        JDOMFactory expectedFactory = factory;
        JDOMFactory actualFactory = result.getFactory();
        assertEquals(expectedFactory, actualFactory);
    }
}