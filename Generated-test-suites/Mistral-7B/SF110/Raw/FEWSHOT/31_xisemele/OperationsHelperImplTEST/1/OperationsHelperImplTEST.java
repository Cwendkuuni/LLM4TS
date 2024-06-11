package net.sf.xisemele.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        Document document = createDocument();
        Node root = document.getDocumentElement();
        root.appendChild(createElement("child1"));
        root.appendChild(createElement("child2"));
        root.appendChild(createElement("child3"));

        List<Node> children = OperationsHelperImpl. OperationsHelperImpl.getInstance().children(root);
        assertEquals(3, children.size());
        assertEquals("child1", children.get(0).getNodeName());
        assertEquals("child2", children.get(1).getNodeName());
        assertEquals("child3", children.get(2).getNodeName());
    }

    @Test
    public void testFind() {
        Document document = createDocument();
        Node root = document.getDocumentElement();
        Node child1 = createElement("child1");
        Node child2 = createElement("child2");
        Node grandChild = createElement("grandChild");

        root.appendChild(child1);
        child1.appendChild(grandChild);

        Node found = OperationsHelperImpl. OperationsHelperImpl.getInstance().find(document, "child1/grandChild");
        assertNotNull(found);
        assertEquals("grandChild", found.getNodeName());

        found = OperationsHelperImpl. OperationsHelperImpl.getInstance().find(document, "child3/grandChild");
        assertNull(found);
    }

    @Test
    public void testNodeWithName() {
        Document document = createDocument();
        Node root = document.getDocumentElement();
        Node child1 = createElement("child1");
        Node child2 = createElement("child2");
        Node nodeWithName = createElement("nodeWithName");

        root.appendChild(child1);
        root.appendChild(child2);
        root.appendChild(nodeWithName);

        List<Node> nodes = OperationsHelperImpl. OperationsHelperImpl.getInstance().children(root);

        Node found = OperationsHelperImpl. OperationsHelperImpl.getInstance().nodeWithName(nodes, "child1");
        assertNotNull(found);
        assertEquals("child1", found.getNodeName());

        found = OperationsHelperImpl. OperationsHelperImpl.getInstance().nodeWithName(nodes, "nodeWithName");
        assertNotNull(found);
        assertEquals("nodeWithName", found.getNodeName());

        found = OperationsHelperImpl. OperationsHelperImpl.getInstance().nodeWithName(nodes, "child3");
        assertNull(found);
    }

    @Test
    public void testSublistWithName() {
        Document document = createDocument();
        Node root = document.getDocumentElement();
        Node child1 = createElement("child1");
        Node child2 = createElement("child2");
        Node nodeWithName1 = createElement("nodeWithName1");
        Node nodeWithName2 = createElement("nodeWithName2");

        root.appendChild(child1);
        root.appendChild(child2);
        child1.appendChild(nodeWithName1);
        child1.appendChild(nodeWithName2);

        List<Node> nodes = OperationsHelperImpl. OperationsHelperImpl.getInstance().children(root);

        List<Node> sublist = OperationsHelperImpl. OperationsHelperImpl.getInstance().sublistWithName(nodes, "child1");
        assertEquals(2, sublist.size());
        assertNotNull(OperationsHelperImpl. OperationsHelperImpl.getInstance().nodeWithName(sublist, "nodeWithName1"));
        assertNotNull(OperationsHelperImpl. OperationsHelperImpl.getInstance().nodeWithName(sublist, "nodeWithName2"));

        sublist = OperationsHelperImpl. OperationsHelperImpl.getInstance().sublistWithName(nodes, "child3");
        assertTrue(sublist.isEmpty());
    }

    private Document createDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new ExceptionInInitializerError(e);
        }
        return builder.newDocument();
    }

    private Element createElement(String tagName) {
        Document document = createDocument();
        Element element = document.createElement(tagName);
        document.appendChild(element);
        return element;
    }
}