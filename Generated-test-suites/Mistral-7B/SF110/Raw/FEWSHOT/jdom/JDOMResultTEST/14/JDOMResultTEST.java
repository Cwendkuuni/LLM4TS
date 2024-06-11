package org.jdom2.transform;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.junit.Test;

public class JDOMResultTest {

    @Test
    public void testGetResult_WhenResultlistIsNotNull_ReturnsResultlist() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> resultList = new ArrayList<>();
        resultList.add(new Element("element1"));
        jdomResult.setResult(resultList);

        List<Content> result = jdomResult.getResult();

        assertEquals(resultList, result);
    }

    @Test
    public void testGetResult_WhenResultdocIsNotNullAndNotQueriedYet_ReturnsDocumentContent() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new JDOMFactory().document(new Element("root"));
        jdomResult.setDocument(document);

        List<Content> result = jdomResult.getResult();

        assertEquals(document.getContent(), result);
    }

    @Test
    public void testGetResult_WhenBothResultlistAndResultdocAreNotNull_ReturnsResultlist() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> resultList = new ArrayList<>();
        resultList.add(new Element("element1"));
        Document document = new JDOMFactory().document(new Element("root"));
        jdomResult.setDocument(document);
        jdomResult.setResult(resultList);

        List<Content> result = jdomResult.getResult();

        assertEquals(resultList, result);
    }

    @Test
    public void testSetResult_SetsTheResultlist() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> resultList = new ArrayList<>();
        jdomResult.setResult(resultList);

        List<Content> expectedResultList = new ArrayList<>();
        expectedResultList.add(new Element("expectedElement"));
        jdomResult.setResult(expectedResultList);

        List<Content> result = jdomResult.getResult();

        assertEquals(expectedResultList, result);
    }

    @Test
    public void testSetDocument_SetsTheDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new JDOMFactory().document(new Element("root"));
        jdomResult.setDocument(document);

        Document expectedDocument = new JDOMFactory().document(new Element("expectedRoot"));
        jdomResult.setDocument(expectedDocument);

        Document result = jdomResult.getDocument();

        assertEquals(expectedDocument, result);
    }

    @Test
    public void testGetDocument_WhenResultlistIsNotNull_ReturnsEmptyDocument() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> resultList = new ArrayList<>();
        jdomResult.setResult(resultList);

        Document result = jdomResult.getDocument();

        assertNull(result);
    }

    @Test
    public void testGetDocument_WhenResultdocIsNotNullAndQueried_ReturnsDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new JDOMFactory().document(new Element("root"));
        jdomResult.setDocument(document);
        jdomResult.setResult(Collections.emptyList());
        jdomResult.quered = true; // Set queried flag manually since it's private

        Document result = jdomResult.getDocument();

        assertEquals(document, result);
    }

    @Test(expected = RuntimeException.class)
    public void testGetDocument_WhenResultdocAndResultlistAreNotNull_ThrowsRuntimeException() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> resultList = new ArrayList<>();
        resultList.add(new Element("element1"));
        Document document = new JDOMFactory().document(new Element("root"));
        jdomResult.setDocument(document);
        jdomResult.setResult(resultList);

        Document result = jdomResult.getDocument();
    }

    @Test
    public void testGetFactory_ReturnsFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        jdomResult.setFactory(factory);

        JDOMFactory result = jdomResult.getFactory();

        assertEquals(factory, result);
    }

    @Test
    public void testSetFactory_SetsTheFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        jdomResult.setFactory(factory);

        JDOMFactory expectedFactory = new JDOMFactory();
        jdomResult.setFactory(expectedFactory);

        JDOMFactory result = jdomResult.getFactory();

        assertEquals(expectedFactory, result);
    }

    @Test
    public void testRetrieveResult_WhenBothResultlistAndResultdocAreNull_SetsResultlist() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.retrieveResult();

        List<Content> result = jdomResult.getResult();

        assertNotNull(result);
    }
}