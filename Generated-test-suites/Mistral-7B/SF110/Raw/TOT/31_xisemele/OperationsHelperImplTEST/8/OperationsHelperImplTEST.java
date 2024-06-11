
package net.sf.xisemele.impl;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        Document document = createTestDocument();
        Node root = document.getDocumentElement();
        List<Node> expectedChildren = Arrays.asList(createElement("element1"), createElement("element2"));
        List<Node> actualChildren = new OperationsHelperImpl().children(root);
        assertNotNull(actualChildren);
        assertEquals(expectedChildren, actualChildren);
    }

    @Test
    public void testFind() {
        Document document = createTestDocument();
        List<String> pathsToTest = Arrays.asList("/root/element1", "/root/element2", "/root/element3");
        for (String path : pathsToTest) {
            Node actualNode = new OperationsHelperImpl().find(document, path);
            if (path.equals("/root/element1")) {
                assertEquals(createElement("element1"), actualNode);
            } else if (path.equals("/root/element2")) {
                assertEquals(createElement("element2"), actualNode);
            } else if (path.equals("/root/element3")) {
                assertNull(actualNode);
            } else {
                fail("Invalid path: " + path);
            }
        }
    }

    @Test
    public void testNodeWithNameAndSublistWithName() {
        Document document = createTestDocument();
        List<Node> nodes = new ArrayList<Node>() {{
            add(createElement("element1"));
            add(createElement("element2"));
            add(createElement("element3"));
            add(createElement("element4"));
            add(createElement("element1"));
        }};
        List<Node> expectedSublist = Arrays.asList(createElement("element1"), createElement("element1"));

        Node actualNode = new OperationsHelperImpl().nodeWithName(nodes, "element1");
        assertTrue(actualNode != null);
        assertEquals(createElement("element1"), actualNode);

        List<Node> actualSublist = new OperationsHelperImpl().sublistWithName(nodes, "element1");
        assertEquals(expectedSublist, actualSublist);
    }

    private Document createTestDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document;
        try {
            document = factory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Element root = document.createElement("root");
        for (String elementName : Arrays.asList("element1", "element2", "element3", "element4")) {
            Element element = document.createElement(elementName);
            root.appendChild(element);
        }
        document.appendChild(root);
        return document;
    }

    private Element createElement(String tagName) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document;
        try {
            document = factory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Element element = document.createElement(tagName);
        return element;
    }
}
