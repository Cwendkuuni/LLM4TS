package org.junit.test;

import org.jsoup.nodes.Document;
import org.w3c.dom.Document;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.jsoup.helper.W3CDom.*;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        Document jsoupDocument = new Document();
        Document w3cDocument = fromJsoup(jsoupDocument);
        assertNotNull(w3cDocument);
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoupWithNullJsoupDocument() {
        Document w3cDocument = fromJsoup(null);
    }

    @Test
    public void testConvert() throws ParserConfigurationException {
        Document jsoupDocument = new Document();
        Document w3cDocument = fromJsoup(jsoupDocument);
        convert(jsoupDocument, w3cDocument);
        assertEquals("<document></document>", asString(w3cDocument));
    }

    @Test
    public void testFromJsoupWithEmptyJsoupDocument() {
        Document jsoupDocument = new Document();
        Document w3cDocument = fromJsoup(jsoupDocument);
        assertNotNull(w3cDocument);
    }

    @Test(expected = ParserConfigurationException.class)
    public void testFromJsoupWithInvalidFactory() {
        W3CDom w3CDom = new W3CDom() {
            @Override
            protected DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            @Override
            public DocumentBuilderFactory getFactory() {
                throw new ParserConfigurationException("InvalidFactoryException");
            }
        };
        Document w3cDocument = w3CDom.fromJsoup(new Document());
    }

    @Test
    public void testConvertWithEmptyJsoupDocument() throws ParserConfigurationException {
        Document jsoupDocument = new Document();
        Document w3cDocument = fromJsoup(jsoupDocument);
        convert(jsoupDocument, w3cDocument);
        assertEquals("<document></document>", asString(w3cDocument));
    }

    @Test
    public void testConvertWithJsoupDocumentWithChildren() throws ParserConfigurationException {
        Document jsoupDocument = new Document();
        Element root = jsoupDocument.createElement("root");
        jsoupDocument.appendChild(root);
        Element child = jsoupDocument.createElement("child");
        root.appendChild(child);

        Document w3cDocument = new Document();
        convert(jsoupDocument, w3cDocument);
        assertEquals("<root><child/></root>", asString(w3cDocument));
    }

    @Test
    public void testFromJsoupWithJsoupDocumentWithComplexStructure() {
        Document jsoupDocument = new Document();
        Element root = jsoupDocument.createElement("root");
        jsoupDocument.appendChild(root);
        Element child1 = jsoupDocument.createElement("child1");
        child1.setAttribute("data1", "value1");
        root.appendChild(child1);
        Element child2 = jsoupDocument.createElement("child2");
        child2.setAttribute("data2", "value2");
        child1.appendChild(child2);

        Document w3cDocument = W3CDom.fromJsoup(jsoupDocument);
        assertEquals("<root><child1 data1=\"value1\"><child2/></child1></root>", asString(w3cDocument));
    }

    @Test
    public void testFromJsoupWithJsoupDocumentWithEmptyTextNodes() {
        Document jsoupDocument = new Document();
        Element root = jsoupDocument.createElement("root");
        jsoupDocument.appendChild(root);
        Element textNode1 = jsoupDocument.createTextNode(" ");
        root.appendChild(textNode1);
        Element textNode2 = jsoupDocument.createTextNode("   ");
        root.appendChild(textNode2);

        Document w3cDocument = W3CDom.fromJsoup(jsoupDocument);
        assertEquals("<root>&nbsp;</root>", asString(w3cDocument));
    }

    @Test
    public void testConvertWithJsoupDocumentWithEmptyTextNodesAndComments() throws ParserConfigurationException {
        Document jsoupDocument = new Document();
        Element root = jsoupDocument.createElement("root");
        jsoupDocument.appendChild(root);
        Element textNode1 = jsoupDocument.createTextNode(" ");
        root.appendChild(textNode1);
        Element textNode2 = jsoupDocument.createTextNode("   ");
        root.appendChild(textNode2);
        Element comment = jsoupDocument.createComment("This is a comment.");
        root.appendChild(comment);

        Document w3cDocument = new Document();
        convert(jsoupDocument, w3cDocument);
        assertEquals("<root>&nbsp;</root><!--This is a comment-->", asString(w3cDocument));
    }
}