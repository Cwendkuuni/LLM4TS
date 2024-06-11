
package net.sf.xisemele.impl;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEmpty;

import org.junit.Test;
import org.w3c.dom.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        Document document = createTestDocument();
        List<Node> children = new OperationsHelperImpl().children(document.getDocumentElement());
        assertNotNull(children);
        assertEquals(5, children.size());

        Node firstChild = children.get(0);
        assertNotNull(firstChild);
        assertEquals("tag1", firstChild.getNodeName());

        Node lastChild = children.get(children.size() - 1);
        assertNotNull(lastChild);
        assertEquals("tag5", lastChild.getNodeName());

        Node emptyChild = new OperationsHelperImpl().children(createEmptyNode()).get(0);
        assertNull(emptyChild);
    }

    @Test
    public void testFind() {
        Document document = createTestDocument();
        String path = "tag1/tag3/tag4";
        Node result = new OperationsHelperImpl().find(document, path);
        assertNotNull(result);
        assertEquals("tag4", result.getNodeName());

        Document emptyDocument = createEmptyDocument();
        path = "/tag";
        result = new OperationsHelperImpl().find(emptyDocument, path);
        assertNull(result);

        document = createTestDocument();
        path = "tag1/tag3/tag5";
        result = new OperationsHelperImpl().find(document, path);
        assertNull(result);
    }

    @Test
    public void testNodeWithName() {
        Document document = createTestDocument();
        List<Node> children = new OperationsHelperImpl().children(document.getDocumentElement());
        Node nodeWithName = new OperationsHelperImpl().nodeWithName(children, "tag3");
        assertNotNull(nodeWithName);
        assertEquals("tag3", nodeWithName.getNodeName());

        children = new OperationsHelperImpl().children(createEmptyNode()).subList(0, 1);
        nodeWithName = new OperationsHelperImpl().nodeWithName(children, "tag1");
        assertNull(nodeWithName);
    }

    @Test
    public void testSublistWithName() {
        Document document = createTestDocument();
        List<Node> children = new OperationsHelperImpl().children(document.getDocumentElement());
        List<Node> nodesWithName = new OperationsHelperImpl().sublistWithName(children, "tag3");
        assertNotNull(nodesWithName);
        assertEquals(1, nodesWithName.size());
        assertEquals("tag3", nodesWithName.get(0).getNodeName());

        children = new OperationsHelperImpl().children(createEmptyNode()).subList(0, 1);
        nodesWithName = new OperationsHelperImpl().sublistWithName(children, "tag1");
        assertEmpty(nodesWithName);
    }

    private Document createTestDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = null;
        try {
            document = builder.newDocument();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = document.createElement("tag1");
        document.appendChild(root);
        Element tag2 = document.createElement("tag2");
        root.appendChild(tag2);
        Element tag3 = document.createElement("tag3");
        tag2.appendChild(tag3);
        Element tag4 = document.createElement("tag4");
        tag3.appendChild(tag4);
        Element tag5 = document.createElement("tag5");
        tag4.appendChild(tag5);
        return document;
    }

    private Document createEmptyDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = null;
        try {
            document = builder.newDocument();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return document;
    }

    private void assertEmpty(List<?> list) {
        assertEquals(0, list.size());
    }
}
