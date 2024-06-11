package net.sf.xisemele.impl;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.*;
import org.w3c.dom.NodeList;
import java.util.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        Document document = createTestDocument();
        Node root = document.getDocumentElement();

        List<Node> children = OperationsHelperImpl.getInstance().children(root);

        Assert.assertNotNull(children);
        Assert.assertEquals(4, children.size());
        Assert.assertTrue(children.contains(document.getDocumentElement().getElementsByTagName("a")
                .item(0)));
        Assert.assertTrue(children.contains(document.getDocumentElement().getElementsByTagName("b")
                .item(0)));
        Assert.assertTrue(children.contains(document.getDocumentElement().getElementsByTagName("c")
                .item(0)));
        Assert.assertTrue(children.contains(document.getDocumentElement().getElementsByTagName("d")
                .item(0)));
    }

    @Test
    public void testFind() {
        Document document = createTestDocument();

        Node result = OperationsHelperImpl.getInstance().find(document, "/a/c");

        Assert.assertNotNull(result);
        Assert.assertEquals("c", result.getNodeName());

        result = OperationsHelperImpl.getInstance().find(document, "/b/e");
        Assert.assertNull(result);
    }

    @Test
    public void testNodeWithName() {
        Document document = createTestDocument();
        List<Node> nodes = OperationsHelperImpl.getInstance().children(document.getDocumentElement());

        Node result = OperationsHelperImpl.getInstance().nodeWithName(nodes, "a");

        Assert.assertNotNull(result);
        Assert.assertEquals("a", result.getNodeName());

        result = OperationsHelperImpl.getInstance().nodeWithName(nodes, "e");
        Assert.assertNull(result);
    }

    @Test
    public void testSublistWithName() {
        Document document = createTestDocument();
        List<Node> nodes = OperationsHelperImpl.getInstance().children(document.getDocumentElement());

        List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(nodes, "b");

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.contains(document.getDocumentElement().getElementsByTagName("b")
                .item(0)));

        result = OperationsHelperImpl.getInstance().sublistWithName(nodes, "e");
        Assert.assertEquals(0, result.size());
    }

    private Document createTestDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        Document document = null;
        try {
            document = builder.newDocument();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

        document.appendChild(document.createProcessingInstruction("xml", "version=\"1.0\""));
        document.appendChild(document.createElement("a"));
        document.appendChild(document.createElement("a"));
        document.appendChild(document.createElement("b"));
        document.getElementsByTagName("a").item(0).appendChild(document.createElement("c"));
        document.getElementsByTagName("b").item(0).appendChild(document.createElement("d"));
        document.getElementsByTagName("a").item(1).appendChild(document.createComment("comment"));

        return document;
    }
}