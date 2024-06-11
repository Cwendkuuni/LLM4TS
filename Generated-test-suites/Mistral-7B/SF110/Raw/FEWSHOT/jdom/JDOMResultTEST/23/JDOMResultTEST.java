package org.jdom2.transform;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.input.sax.*;
import org.junit.Test;

public class JDOMResultTest {

    @Test
    public void testGetResult_EmptyResult() {
        JDOMResult result = new JDOMResult();
        List<Content> expectedResult = Collections.emptyList();
        List<Content> actualResult = result.getResult();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetResult_SetResultList() {
        JDOMResult result = new JDOMResult();
        List<Content> resultList = new ArrayList<>();
        result.setResult(resultList);
        List<Content> expectedResult = resultList;
        List<Content> actualResult = result.getResult();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetResult_SetDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new JDOMFactory().newDocument("root");
        result.setDocument(document);
        List<Content> expectedResult = document.getContent();
        List<Content> actualResult = result.getResult();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        result.setFactory(factory);
        JDOMFactory actualFactory = result.getFactory();
        assertEquals(factory, actualFactory);
    }

    @Test
    public void testRetrieveResult() {
        JDOMResult result = new JDOMResult();
        List<Content> resultList = new ArrayList<>();
        DocumentBuilder builder = new DocumentBuilder();
        builder.setHandler(new FragmentHandler(result.getFactory()));
        String xmlString = "<root><child></child></root>";
        SAXBuilder saxBuilder = new SAXBuilder();
        saxBuilder.setExpandEntityReferences(false);
        saxBuilder.setValidation(false);
        InputSource inputSource = new InputSource(new StringReader(xmlString));
        saxBuilder.build(inputSource, builder);
        result.retrieveResult();
        List<Content> expectedResult = builder.getResult();
        List<Content> actualResult = result.getResult();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetDocument_EmptyResultList() {
        JDOMResult result = new JDOMResult();
        Document expectedDocument = null;
        Document actualDocument = result.getDocument();
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testGetDocument_SetResultList() {
        JDOMResult result = new JDOMResult();
        List<Content> resultList = new ArrayList<>();
        result.setResult(resultList);
        Document document = new JDOMFactory().document(null);
        document.setContent(resultList);
        result.setDocument(document);
        List<Content> expectedResult = resultList;
        Document actualDocument = result.getDocument();
        assertEquals(expectedResult, actualDocument.getContent());
    }

    @Test
    public void testGetDocument_EmptyDocument() {
        JDOMResult result = new JDOMResult();
        List<Content> resultList = new ArrayList<>();
        result.setResult(resultList);
        Document expectedDocument = null;
        Document actualDocument = result.getDocument();
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testGetDocument_SetDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new JDOMFactory().document(null);
        result.setDocument(document);
        List<Content> expectedResult = document.getContent();
        Document actualDocument = result.getDocument();
        assertEquals(expectedResult, actualDocument.getContent());
    }

    @Test
    public void testGetDocument_ResultListAndDocument() {
        JDOMResult result = new JDOMResult();
        List<Content> resultList = new ArrayList<>();
        result.setResult(resultList);
        Document document = new JDOMFactory().document(null);
        document.setContent(resultList);
        result.setDocument(document);
        List<Content> expectedResult = resultList;
        Document actualDocument = result.getDocument();
        assertEquals(expectedResult, actualDocument.getContent());
    }
}