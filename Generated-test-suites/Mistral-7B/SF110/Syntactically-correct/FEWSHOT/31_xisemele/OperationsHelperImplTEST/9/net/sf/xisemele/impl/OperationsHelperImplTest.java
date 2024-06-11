package net.sf.xisemele.impl;

import org.w3c.dom.*;
import org.junit.Assert;
import java.util.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        Document document = createTestDocument();
        Node root = document.getDocumentElement();
        List<Node> children = OperationsHelperImpl.OperationsHelperImplInstance.children(root);

        Assert.assertEquals(4, children.size());
        Assert.assertEquals("root", children.get(0).getNodeName());
        Assert.assertEquals("element1", children.get(1).getNodeName());
        Assert.assertEquals("element2", children.get(2).getNodeName());
        Assert.assertEquals("element3", children.get(3).getNodeName());
    }

    @Test
    public void testFindNullDocument() {
        Assert.assertNull(OperationsHelperImpl.OperationsHelperImplInstance.find(null, "/"));
    }

    @Test
    public void testFindNullPath() {
        Document document = createTestDocument();
        Assert.assertNull(OperationsHelperImpl.OperationsHelperImplInstance.find(document, null));
    }

    @Test
    public void testFindRoot() {
        Document document = createTestDocument();
        Assert.assertEquals("root", OperationsHelperImpl.OperationsHelperImplInstance.find(document, "").getNodeName());
    }

    @Test
    public void testFindExactMatch() {
        Document document = createTestDocument();
        Assert.assertEquals("element3", OperationsHelperImpl.OperationsHelperImplInstance.find(document, "/element3").getNodeName());
    }

    @Test
    public void testFindNoMatch() {
        Document document = createTestDocument();
        Assert.assertNull(OperationsHelperImpl.OperationsHelperImplInstance.find(document, "/nonexistent"));
    }

    @Test
    public void testFindInSubtree() {
        Document document = createTestDocument();
        Node element2 = document.getElementsByTagName("element2").item(0);
        Assert.assertEquals("element3", OperationsHelperImpl.OperationsHelperImplInstance.find(document, "element2/element3").getNodeName());
    }

    @Test
    public void testNodeWithNameNullList() {
        Assert.assertNull(OperationsHelperImpl.OperationsHelperImplInstance.nodeWithName(null, "element1"));
    }

    @Test
    public void testNodeWithNameEmptyList() {
        Document document = createTestDocument();
        List<Node> nodes = OperationsHelperImpl.OperationsHelperImplInstance.children(document.getDocumentElement());
        Assert.assertNull(OperationsHelperImpl.OperationsHelperImplInstance.nodeWithName(nodes, "element1"));
    }

    @Test
    public void testNodeWithNameMatch() {
        Document document = createTestDocument();
        List<Node> nodes = OperationsHelperImpl.OperationsHelperImplInstance.children(document.getDocumentElement());
        Node node = nodes.get(2);
        Assert.assertEquals(node, OperationsHelperImpl.OperationsHelperImplInstance.nodeWithName(nodes, "element2"));
    }

    @Test
    public void testNodeWithNameNoMatch() {
        Document document = createTestDocument();
        List<Node> nodes = OperationsHelperImpl.OperationsHelperImplInstance.children(document.getDocumentElement());
        Assert.assertNull(OperationsHelperImpl.OperationsHelperImplInstance.nodeWithName(nodes, "element3"));
    }

    @Test
    public void testSublistWithNameNullList() {
        Assert.assertEquals(0, OperationsHelperImpl.OperationsHelperImplInstance.sublistWithName(null, "element1").size());
    }

    @Test
    public void testSublistWithNameEmptyList() {
        Document document = createTestDocument();
        List<Node> nodes = OperationsHelperImpl.OperationsHelperImplInstance.children(document.getDocumentElement());
        Assert.assertEquals(0, OperationsHelperImpl.OperationsHelperImplInstance.sublistWithName(nodes, "element1").size());
    }

    @Test
    public void testSublistWithNameMatch() {
        Document document = createTestDocument();
        List<Node> nodes = OperationsHelperImpl.OperationsHelperImplInstance.children(document.getDocumentElement());
        List<Node> sublist = OperationsHelperImpl.OperationsHelperImplInstance.sublistWithName(nodes, "element2");
        Assert.assertEquals(1, sublist.size());
    }

    @Test
    public void testSublistWithNameNoMatch() {
        Document document = createTestDocument();
        List<Node> nodes = OperationsHelperImpl.OperationsHelperImplInstance.children(document.getDocumentElement());
        List<Node> sublist = OperationsHelperImpl.OperationsHelperImplInstance.sublistWithName(nodes, "element3");
        Assert.assertEquals(0, sublist.size());
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

        Element root = document.createElement("root");
        root.setAttribute("id", "root");
        document.appendChild(root);

        Element element1 = document.createElement("element1");
        root.appendChild(element1);

        Element element2 = document.createElement("element2");
        element2.setAttribute("id", "element2");
        element1.appendChild(element2);

        Element element3 = document.createElement("element3");
        element3.setAttribute("id", "element3");
        element2.appendChild(element3);

        return document;
    }
}