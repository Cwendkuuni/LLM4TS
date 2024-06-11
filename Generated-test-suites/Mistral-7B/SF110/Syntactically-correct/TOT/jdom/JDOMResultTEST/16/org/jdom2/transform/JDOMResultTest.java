package org.jdom2.transform.test;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.transform.JDOMResult;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JDOMResultTest {

    @Test
    public void testGetResultEmpty() {
        JDOMResult result = new JDOMResult();
        List<JDOMResult.Content> actual = result.getResult();
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    public void testGetResultList() throws SAXException, JDOMException {
        String xml = "<root><elem1/><elem2><elem3/></elem2></root>";
        SAXBuilder builder = new SAXBuilder();
        JDOMResult result = new JDOMResult();
        builder.setEventHandler(result);
        builder.build(new StringReader(xml));

        List<JDOMResult.Content> actual = result.getResult();
        assertNotNull(actual);
        assertEquals(3, actual.size());

        assertEquals("elem1", actual.get(0).getName());
        assertEquals("elem2", actual.get(1).getName());
        assertEquals("elem3", actual.get(2).getName());
    }

    @Test
    public void testGetResultDocument() throws SAXException, JDOMException {
        String xml = "<root><elem1/><elem2><elem3/></elem2></root>";
        SAXBuilder builder = new SAXBuilder();
        JDOMResult result = new JDOMResult();
        builder.setEventHandler(result);
        Document doc = builder.build(new StringReader(xml));
        result.setDocument(doc);

        List<JDOMResult.Content> actual = result.getResult();
        assertNotNull(actual);
        assertEquals(3, actual.size());

        assertEquals("elem1", actual.get(0).getName());
        assertEquals("elem2", actual.get(1).getName());
        assertEquals("elem3", actual.get(2).getName());
    }

    @Test
    public void testGetResultNull() {
        JDOMResult result = new JDOMResult();
        result.setDocument(null);
        List<JDOMResult.Content> actual = result.getResult();
        assertNull(actual);
    }

    @Test
    public void testGetResultQueried() throws SAXException, JDOMException {
        String xml = "<root><elem1/><elem2><elem3/></elem2></root>";
        SAXBuilder builder = new SAXBuilder();
        JDOMResult result = new JDOMResult();
        builder.setEventHandler(result);
        builder.build(new StringReader(xml));

        List<JDOMResult.Content> actualFirst = result.getResult();
        assertNotNull(actualFirst);
        assertEquals(3, actualFirst.size());

        result.setDocument(null);
        List<JDOMResult.Content> actualSecond = result.getResult();
        assertNull(actualSecond);

        result.setDocument(new Document());
        result.setResult(new ArrayList<>());
        List<JDOMResult.Content> actualThird = result.getResult();
        assertNotNull(actualThird);
        assertEquals(0, actualThird.size());

        result.queried = false;
        List<JDOMResult.Content> actualFourth = result.getResult();
        assertNotNull(actualFourth);
        assertEquals(0, actualFourth.size());
    }
}