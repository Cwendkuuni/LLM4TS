package net.sf.xisemele.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        Document document = createDocument();
        Element root = document.createElement("root");
        document.appendChild(root);

        Element child1 = document.createElement("child1");
        child1.setTextContent("Child 1");
        root.appendChild(child1);

        Element child2 = document.createElement("child2");
        child2.setTextContent("Child 2");
        root.appendChild(child2);

        List<Node> expectedChildren = Arrays.asList(child1, child2);
        List<Node> actualChildren = new OperationsHelperImpl().children(root);

        assertEquals(expectedChildren, actualChildren);
    }

    @Test
    public void testFindNullDocument() {
        assertNull(new OperationsHelperImpl().find(null, ""));
    }

    @Test
    public void testFindEmptyDocument() {
        Document document = createDocument();
        Node result = new OperationsHelperImpl().find(document, "");
        assertNull(result);
    }

    @Test
    public void testFindRoot() {
        Document document = createDocument();
        Node root = document.getDocumentElement();
        Node result = new OperationsHelperImpl().find(document, root.getNodeName());
        assertEquals(root, result);
    }

    @Test
    public void testFindNonExistentPath() {
        Document document = createDocument();
        Element root = document.getDocumentElement();
        assertNull(new OperationsHelperImpl().find(document, "nonexistent/path"));
    }

    @Test
    public void testFindExistentPath() {
        Document document = createDocument();
        Element root = document.getDocumentElement();

        Element child1 = document.createElement("child1");
        child1.setTextContent("Child 1");
        root.appendChild(child1);

        Element child2 = document.createElement("child2");
        child2.setTextContent("Child 2");
        root.appendChild(child2);

        Element grandchild = document.createElement("grandchild");
        child2.appendChild(grandchild);

        Node result = new OperationsHelperImpl().find(document, "child1/child2/grandchild");
        assertEquals(grandchild, result);
    }

    @Test
    public void testNodeWithNameNullNodes() {
        assertNull(new OperationsHelperImpl().nodeWithName(null, "name"));
    }

    @Test
    public void testNodeWithNameEmptyList() {
        assertNull(new OperationsHelperImpl().nodeWithName(Collections.emptyList(), "name"));
    }

    @Test
    public void testNodeWithNameSingleNode() {
        Document document = createDocument();
        Element root = document.getDocumentElement();
        Element node = document.createElement("node");
        root.appendChild(node);

        Node result = new OperationsHelperImpl().nodeWithName(Arrays.asList(node), "node");
        assertEquals(node, result);
    }

    @Test
    public void testNodeWithNameMultipleNodes() {
        Document document = createDocument();
        Element root = document.getDocumentElement();

        Element node1 = document.createElement("node1");
        root.appendChild(node1);

        Element node2 = document.createElement("node2");
        root.appendChild(node2);

        Node result = new OperationsHelperImpl().nodeWithName(Arrays.asList(node1, node2), "node1");
        assertEquals(node1, result);
    }

    @Test
    public void testNodeWithNameNonexistentName() {
        Document document = createDocument();
        Element root = document.getDocumentElement();

        Element node1 = document.createElement("node1");
        root.appendChild(node1);

        Node result = new OperationsHelperImpl().nodeWithName(Arrays.asList(root), "nonexistent");
        assertNull(result);
    }

    @Test
    public void testSublistWithNameNullNodes() {
        assertEmpty(new OperationsHelperImpl().sublistWithName(null, "name"));
    }

    @Test
    public void testSublistWithNameEmptyList() {
        assertEmpty(new OperationsHelperImpl().sublistWithName(Collections.emptyList(), "name"));
    }

    @Test
    public void testSublistWithNameSingleNode() {
        Document document = createDocument();
        Element root = document.getDocumentElement();
        Element node = document.createElement("node");
        root.appendChild(node);

        List<Node> result = new OperationsHelperImpl().sublistWithName(Arrays.asList(node), "node");
        assertEquals(Collections.singletonList(node), result);
    }

    @Test
    public void testSublistWithNameMultipleNodes() {
        Document document = createDocument();
        Element root = document.getDocumentElement();

        Element node1 = document.createElement("node1");
        root.appendChild(node1);

        Element node2 = document.createElement("node2");
        root.appendChild(node2);

        List<Node> result = new OperationsHelperImpl().sublistWithName(Arrays.asList(node1, node2), "node1");
        assertEquals(Collections.singletonList(node1), result);
    }

    @Test
    public void testSublistWithNameNonexistentName() {
        Document document = createDocument();
        Element root = document.getDocumentElement();

        Element node1 = document.createElement("node1");
        root.appendChild(node1);

        List<Node> result = new OperationsHelperImpl().sublistWithName(Arrays.asList(root), "node2");
        assertEmpty(result);
    }

    private Document createDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        return builder.newDocument();
    }

    private void assertEquals(List<Node> expected, List<Node> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getNodeName(), actual.get(i).getNodeName());
            assertEquals(expected.get(i).getTextContent(), actual.get(i).getTextContent());
        }
    }

    private void assertEmpty(List<Node> list) {
        assertTrue(list.isEmpty());
    }
}