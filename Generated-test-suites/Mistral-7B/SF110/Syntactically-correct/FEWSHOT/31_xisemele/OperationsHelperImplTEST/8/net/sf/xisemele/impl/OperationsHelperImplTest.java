package net.sf.xisemele.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        final Document document = createDocument();
        final Element root = document.getDocumentElement();
        root.setNodeName("root");

        final Node child1 = createElement("child1");
        root.appendChild(child1);

        final Node child2 = createElement("child2");
        root.appendChild(child2);

        final Node child3 = createElement("child3");
        child2.appendChild(child3);

        final List<Node> result = OperationsHelperImpl.getInstance().children(root);

        assertEquals(3, result.size());
        assertSame(root, result.get(0));
        assertSame(child1, result.get(1));
        assertSame(child2, result.get(2));
    }

    @Test
    public void testChildrenEmpty() {
        final Document document = createDocument();
        final Element root = document.getDocumentElement();

        final List<Node> result = OperationsHelperImpl.getInstance().children(root);

        assertEquals(0, result.size());
    }

    @Test
    public void testChildrenNull() {
        final List<Node> result = OperationsHelperImpl.getInstance().children(null);

        assertEquals(0, result.size());
        assertNull(result);
    }

    @Test
    public void testFind() {
        final Document document = createDocument();
        final Element root = document.getDocumentElement();
        root.setNodeName("root");

        final Element child1 = createElement("child1");
        root.appendChild(child1);
        child1.setNodeName("child1");

        final Element child2 = createElement("child2");
        root.appendChild(child2);
        child2.setNodeName("child2");

        final Element child3 = createElement("child3");
        child2.appendChild(child3);
        child3.setNodeName("child3");

        final Element expected = child3;

        final Node result = OperationsHelperImpl.getInstance().find(document, "/root/child2/child3");

        assertSame(expected, result);
    }

    @Test
    public void testFindNullDocument() {
        final String path = "/root/child2/child3";
        assertNull(OperationsHelperImpl.getInstance().find(null, path));
    }

    @Test
    public void testFindNullPath() {
        final Document document = createDocument();
        assertNull(OperationsHelperImpl.getInstance().find(document, null));
    }

    @Test
    public void testFindEmptyDocument() {
        final Document document = createDocument();
        assertNull(OperationsHelperImpl.getInstance().find(document, "/"));
    }

    @Test
    public void testFindNotExistingPath() {
        final Document document = createDocument();
        assertNull(OperationsHelperImpl.getInstance().find(document, "/notExisting/node"));
    }

    @Test
    public void testFindSameName() {
        final Document document = createDocument();
        final Element root = document.getDocumentElement();
        root.setNodeName("root");

        final Element child1 = createElement("child1");
        root.appendChild(child1);
        child1.setNodeName("child1");

        final Element child2 = createElement("child1");
        root.appendChild(child2);
        child2.setNodeName("child1");

        final Node result = OperationsHelperImpl.getInstance().find(document, "/root/child1");

        assertNotNull(result);
        assertSame(child1, result);
    }

    @Test
    public void testFindLastName() {
        final Document document = createDocument();
        final Element root = document.getDocumentElement();
        root.setNodeName("root");

        final Element child1 = createElement("child1");
        root.appendChild(child1);
        child1.setNodeName("child1");

        final Element child2 = createElement("child2");
        root.appendChild(child2);
        child2.setNodeName("child2");

        final Element child3 = createElement("child1");
        child2.appendChild(child3);
        child3.setNodeName("child1");

        final Element expected = child3;

        final Node result = OperationsHelperImpl.getInstance().find(document, "/root/child2/child1");

        assertSame(expected, result);
    }

    @Test
    public void testNodeWithName() {
        final Document document = createDocument();
        final NodeList nodes = createNodeList(createElement("node1"), createElement("node2"), createElement("node3"));

        final List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(Arrays.asList(nodes), "node1");

        assertEquals(1, result.size());
        assertSame(nodes.item(0), result.get(0));
    }

    @Test
    public void testNodeWithNameEmptyList() {
        final Document document = createDocument();
        final List<Node> nodes = Collections.emptyList();

        final List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(nodes, "node1");

        assertEquals(0, result.size());
        assertNull(result);
    }

    @Test
    public void testNodeWithNameNullList() {
        final List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(null, "node1");

        assertEquals(0, result.size());
        assertNull(result);
    }

    @Test
    public void testNodeWithNameNullName() {
        final Document document = createDocument();
        final NodeList nodes = document.getChildNodes();

        final List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(Arrays.asList(nodes), null);

        assertEquals(0, result.size());
        assertNull(result);
    }

    @Test
    public void testNodeWithNameMultipleMatches() {
        final Document document = createDocument();
        final NodeList nodes = createNodeList(createElement("node1"), createElement("node1"), createElement("node3"));

        final List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(Arrays.asList(nodes), "node1");

        assertEquals(2, result.size());
        assertThat(result, is(Arrays.asList(nodes.item(0), nodes.item(1))));
    }

    @Test
    public void testSublistWithNameEmptyList() {
        final Document document = createDocument();
        final NodeList nodes = document.getChildNodes();

        final List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(Collections.emptyList(), "node1");

        assertEquals(0, result.size());
        assertNull(result);
    }

    @Test
    public void testSublistWithNameNullList() {
        final List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(null, "node1");

        assertEquals(0, result.size());
        assertNull(result);
    }

    @Test
    public void testSublistWithNameNullName() {
        final Document document = createDocument();
        final NodeList nodes = document.getChildNodes();

        final List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(nodes, null);

        assertEquals(0, result.size());
        assertNull(result);
    }

    @Test
    public void testSublistWithNameMultipleMatches() {
        final Document document = createDocument();
        final NodeList nodes = createNodeList(createElement("node1"), createElement("node1"), createElement("node3"));

        final List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(nodes, "node1");

        assertEquals(2, result.size());
        assertThat(result, is(Arrays.asList(nodes.item(0), nodes.item(1))));
    }

    private static Document createDocument() {
        final DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return documentBuilder.newDocument();
    }

    private static Node createElement(String tagName) {
        final DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return documentBuilder.newElement(tagName);
    }

    private static NodeList createNodeList(Node... nodes) {
        final DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        final Document document = documentBuilder.newDocument();
        document.appendChild(documentBuilder.newElement("root"));

        for (final Node node : nodes) {
            final Element root = (Element) document.getDocumentElement();
            root.appendChild(node);
        }

        return document.getDocumentElement().getChildNodes();
    }
}