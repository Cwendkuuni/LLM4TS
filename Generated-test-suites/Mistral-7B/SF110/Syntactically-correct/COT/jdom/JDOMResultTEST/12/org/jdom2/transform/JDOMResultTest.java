import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;
import org.jdom2.input.sax.SAXBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        List<Content> result = Arrays.asList(new Element("element1"), new Element("element2"));
        jdomResult.setResult(result);
        List<Content> actualResult = jdomResult.getResult();
        Assert.assertSame(result, actualResult);
        Assert.assertFalse(jdomResult.isQueried());
    }

    @Test
    public void testGetResultWhenResultlistIsNotNull() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = Arrays.asList(new Element("element1"), new Element("element2"));
        jdomResult.setResult(result);
        List<Content> actualResult = jdomResult.getResult();
        Assert.assertSame(result, actualResult);
        Assert.assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testGetResultWhenResultdocIsNotNull() throws SAXException, IOException {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        String xmlString = "<root><element1/><element2/></root>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());
        SAXBuilder builder = new SAXBuilder(factory);
        Document document = builder.build(inputStream);
        jdomResult.setDocument(document);
        List<Content> actualResult = jdomResult.getResult();
        Assert.assertNotNull(actualResult);
        Assert.assertEquals(2, actualResult.size());
        Assert.assertTrue(jdomResult.isQueried());
    }

    @Test(expected = RuntimeException.class)
    public void testGetDocumentWhenResultlistIsNullAndQueriedIsFalse() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setResult(null);
        Document actualDocument = jdomResult.getDocument();
        Assert.assertNull(actualDocument);
    }

    @Test(expected = RuntimeException.class)
    public void testGetDocumentWhenResultdocIsNullAndQueriedIsFalse() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setDocument(null);
        Document actualDocument = jdomResult.getDocument();
        Assert.assertNull(actualDocument);
    }

    @Test
    public void testSetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document();
        jdomResult.setDocument(document);
        Document actualDocument = jdomResult.getDocument();
        Assert.assertSame(document, actualDocument);
        Assert.assertFalse(jdomResult.isQueried());
    }

    @Test
    public void testGetDocumentWhenQueriedIsTrue() throws SAXException, IOException {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        String xmlString = "<root><element1/><element2/></root>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());
        SAXBuilder builder = new SAXBuilder(factory);
        Document document = builder.build(inputStream);
        jdomResult.setDocument(document);
        jdomResult.retrieveResult();
        Document actualDocument = jdomResult.getDocument();
        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(2, actualDocument.getContentSize());
        Assert.assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testSetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        JDOMFactory actualFactory = jdomResult.getFactory();
        Assert.assertSame(factory, actualFactory);
    }

    @Test
    public void testGetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        JDOMFactory actualFactory = jdomResult.getFactory();
        Assert.assertSame(factory, actualFactory);
    }

    @Test
    public void testRetrieveResultWhenResultlistIsNullAndResultdocIsNull() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.retrieveResult();
        List<Content> actualResult = jdomResult.getResult();
        Assert.assertNotNull(actualResult);
        Assert.assertEquals(0, actualResult.size());
        Assert.assertFalse(jdomResult.isQueried());
    }

    @Test(expected = RuntimeException.class)
    public void testGetDocumentWhenResultlistIsEmptyAndQueriedIsFalse() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setResult(Collections.emptyList());
        Document actualDocument = jdomResult.getDocument();
        Assert.assertNull(actualDocument);
    }

    @Test(expected = RuntimeException.class)
    public void testGetDocumentWhenResultlistIsEmptyAndQueriedIsTrue() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setResult(Collections.emptyList());
        jdomResult.retrieveResult();
        Document actualDocument = jdomResult.getDocument();
        Assert.assertNull(actualDocument);
    }

    @Test
    public void testGetDocumentWhenResultlistIsNotEmptyAndQueriedIsFalse() throws SAXException, IOException {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        String xmlString = "<root><element1/><element2/></root>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());
        SAXBuilder builder = new SAXBuilder(factory);
        Document document = builder.build(inputStream);
        jdomResult.setResult(Arrays.asList(new Element("element1"), new Element("element2")));
        Document actualDocument = jdomResult.getDocument();
        Assert.assertNull(actualDocument);
    }

    @Test
    public void testGetDocumentWhenResultlistIsNotEmptyAndQueriedIsTrue() throws SAXException, IOException {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        String xmlString1 = "<element1><text>Text 1</text></element1>";
        String xmlString2 = "<element2><text>Text 2</text></element2>";
        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(xmlString1.getBytes());
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(xmlString2.getBytes());
        SAXBuilder builder = new SAXBuilder(factory);
        Document document1 = builder.build(inputStream1);
        Document document2 = builder.build(inputStream2);
        List<Content> result = Arrays.asList(document1, document2);
        jdomResult.setResult(result);
        jdomResult.retrieveResult();
        Document actualDocument = jdomResult.getDocument();
        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(2, actualDocument.getContentSize());
        Assert.assertTrue(jdomResult.isQueried());
    }
}