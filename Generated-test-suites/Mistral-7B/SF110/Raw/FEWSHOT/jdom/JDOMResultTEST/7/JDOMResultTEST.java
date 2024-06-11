package org.jdom2.transform;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.input.sax.*;
import org.xml.sax.helpers.*;
import org.xml.sax.*;

public class JDOMResultTest {

    @Test
    public void testGetResult_WhenResultListIsNotNull_ReturnsResultList() {
        List<Content> result = new ArrayList<>();
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setResult(result);

        List<Content> actualResult = jdomResult.getResult();

        assertSame(result, actualResult);
    }

    @Test
    public void testGetResult_WhenResultDocIsNotNullAndQueriedIsFalse_ReturnsDocumentContent() {
        Document document = new Document();
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setDocument(document);

        List<Content> actualResult = jdomResult.getResult();

        List<Content> expectedResult = document.getContent();
        assertEquals(expectedResult.size(), actualResult.size());
        for (int i = 0; i < expectedResult.size(); i++) {
            Content expectedContent = expectedResult.get(i);
            Content actualContent = actualResult.get(i);
            assertEquals(expectedContent, actualContent);
        }
    }

    @Test
    public void testGetResult_WhenResultDocIsNotNullAndQueriedIsTrue_ReturnsQueriedResultList() {
        Document document = new Document();
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setDocument(document);

        jdomResult.getResult(); // Retrieve the result once to set queried to true

        List<Content> actualResult = jdomResult.getResult();

        assertFalse(document.getContent().isEmpty()); // Check that the original document content is not empty
        List<Content> expectedResult = new ArrayList<>(document.getContent().size());
        for (Content content : document.getContent()) {
            expectedResult.add(content);
        }
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSetResult_SetsResultList() {
        List<Content> result = new ArrayList<>();
        JDOMResult jdomResult = new JDOMResult();

        jdomResult.setResult(result);

        List<Content> actualResult = jdomResult.getResult();

        assertSame(result, actualResult);
    }

    @Test
    public void testSetDocument_SetsResultDoc() {
        Document document = new Document();
        JDOMResult jdomResult = new JDOMResult();

        jdomResult.setDocument(document);

        Document actualDocument = jdomResult.getDocument();

        assertSame(document, actualDocument);
    }

    @Test
    public void testGetFactory_ReturnsFactory() {
        JDOMFactory factory = new DefaultJDOMFactory();
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setFactory(factory);

        JDOMFactory actualFactory = jdomResult.getFactory();

        assertSame(factory, actualFactory);
    }

    @Test
    public void testRetrieveResult_RetrievesResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        ContentHandler handler = new ContentHandler() {};
        jdomResult.setHandler(handler);

        DocumentBuilder documentBuilder = jdomResult.getDocumentBuilder();
        documentBuilder.startElement("root", null, null, null); // Start root element
        documentBuilder.characters("Test".toCharArray(), 0, 4); // Add characters
        documentBuilder.endElement("root"); // End root element

        jdomResult.retrieveResult();

        List<Content> actualResult = jdomResult.getResult();

        assertEquals(1, actualResult.size());
        Content actualContent = actualResult.get(0);
        Element actualElement = (Element) actualContent;
        assertEquals("root", actualElement.getName());
        assertEquals("Test", actualElement.getTextNormalized());
    }
}