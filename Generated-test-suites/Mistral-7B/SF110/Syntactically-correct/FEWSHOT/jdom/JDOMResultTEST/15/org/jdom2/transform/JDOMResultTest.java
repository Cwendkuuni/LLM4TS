package org.jdom2.transform;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.junit.Test;

public class JDOMResultTest {

    @Test
    public void testGetResultEmpty() {
        JDOMResult result = new JDOMResult();
        List<Content> nodes = result.getResult();
        assertEquals(Collections.emptyList(), nodes);
    }

    @Test
    public void testGetResultWithList() {
        JDOMResult result = new JDOMResult();
        Element root = new Element("root", null, null);
        Content text1 = new Text("Text 1");
        Content text2 = new Text("Text 2");
        root.addContent(text1);
        root.addContent(text2);
        result.setResult(new ArrayList<Content>() {{
            add(text1);
            add(text2);
        }});
        List<Content> nodes = result.getResult();
        assertEquals(2, nodes.size());
        assertEquals(text1, nodes.get(0));
        assertEquals(text2, nodes.get(1));
    }

    @Test
    public void testGetResultWithDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new JDOMFactory().document(null);
        Element root = new Element("root", null, null);
        Content text1 = new Text("Text 1");
        Content text2 = new Text("Text 2");
        root.addContent(text1);
        root.addContent(text2);
        document.setContent(Collections.singletonList(root));
        result.setDocument(document);
        List<Content> nodes = result.getResult();
        assertEquals(2, nodes.size());
        assertEquals(text1, nodes.get(0));
        assertEquals(text2, nodes.get(1));
    }

    @Test
    public void testGetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        result.setFactory(factory);
        JDOMFactory getFactory = result.getFactory();
        assertEquals(factory, getFactory);
    }

    @Test
    public void testRetrieveResult() {
        JDOMResult result = new JDOMResult();
        Document document = new JDOMFactory().document(null);
        Element root = new Element("root", null, null);
        Content text1 = new Text("Text 1");
        Content text2 = new Text("Text 2");
        root.addContent(text1);
        root.addContent(text2);
        document.setContent(Collections.singletonList(root));
        result.setDocument(document);
        List<Content> nodes = result.getResult();
        assertEquals(2, nodes.size());
        assertEquals(text1, nodes.get(0));
        assertEquals(text2, nodes.get(1));
        result.retrieveResult();
        nodes = result.getResult();
        assertEquals(2, nodes.size());
        assertEquals(text1, nodes.get(0));
        assertEquals(text2, nodes.get(1));
    }

    @Test
    public void testSetResultAndGetResultAgain() {
        JDOMResult result = new JDOMResult();
        Element root = new Element("root", null, null);
        Content text1 = new Text("Text 1");
        Content text2 = new Text("Text 2");
        root.addContent(text1);
        root.addContent(text2);
        List<Content> nodes = new ArrayList<Content>() {{
            add(text1);
            add(text2);
        }};
        result.setResult(nodes);
        List<Content> resultNodes = result.getResult();
        assertEquals(2, resultNodes.size());
        assertEquals(text1, resultNodes.get(0));
        assertEquals(text2, resultNodes.get(1));
        result.setResult(null);
        resultNodes = result.getResult();
        assertEquals(0, resultNodes.size());
    }

    @Test
    public void testSetDocumentAndGetDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new JDOMFactory().document(null);
        Element root = new Element("root", null, null);
        Content text1 = new Text("Text 1");
        Content text2 = new Text("Text 2");
        root.addContent(text1);
        root.addContent(text2);
        document.setContent(Collections.singletonList(root));
        result.setDocument(document);
        Document resultDocument = result.getDocument();
        assertEquals(document, resultDocument);
        result.setDocument(null);
        resultDocument = result.getDocument();
        assertNull(resultDocument);
    }
}