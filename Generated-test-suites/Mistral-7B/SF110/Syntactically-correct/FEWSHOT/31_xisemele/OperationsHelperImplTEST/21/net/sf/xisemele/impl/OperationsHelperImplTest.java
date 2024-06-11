package net.sf.xisemele.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        Document document = createMockDocument();
        Node root = document.createProcessingInstruction("xml", "version='1.0'");
        Node child1 = document.createElement("child1");
        Node child2 = document.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);

        OperationsHelper helper = new OperationsHelperImpl();
        List<Node> result = helper.children(root);

        assertEquals(2, result.size());
        assertEquals(child1, result.get(0));
        assertEquals(child2, result.get(1));
    }

    @Test
    public void testChildrenEmpty() {
        Document document = createMockDocument();
        Node root = document.createProcessingInstruction("xml", "version='1.0'");

        OperationsHelper helper = new OperationsHelperImpl();
        List<Node> result = helper.children(root);

        assertEquals(0, result.size());
    }

    @Test
    public void testFind() {
        Document document = createMockDocument();
        Element root = document.createProcessingInstruction("xml", "version='1.0'");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        Element target = document.createElement("target");
        child1.appendChild(target);

        OperationsHelper helper = new OperationsHelperImpl();
        Node result = helper.find(document, "/child1/target");

        assertEquals(target, result);
    }

    @Test
    public void testFindNullDocument() {
        OperationsHelper helper = new OperationsHelperImpl();
        Node result = helper.find(null, "/child1/target");

        assertNull(result);
    }

    @Test
    public void testFindNullPath() {
        Document document = createMockDocument();
        OperationsHelper helper = new OperationsHelperImpl();
        Node result = helper.find(document, null);

        assertNull(result);
    }

    @Test
    public void testFindNonExistingPath() {
        Document document = createMockDocument();
        OperationsHelper helper = new OperationsHelperImpl();
        Node result = helper.find(document, "/nonExistingPath");

        assertNull(result);
    }

    @Test
    public void testNodeWithName() {
        Document document = createMockDocument();
        Element root = document.createProcessingInstruction("xml", "version='1.0'");
        Element child1 = document.createElement("child1");
        Element target1 = document.createElement("target1");
        child1.appendChild(target1);
        Element child2 = document.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);

        OperationsHelper helper = new OperationsHelperImpl();
        Node result = helper.nodeWithName(Arrays.asList(child1, child2), "target1");

        assertEquals(target1, result);
    }

    @Test
    public void testNodeWithNameNullList() {
        OperationsHelper helper = new OperationsHelperImpl();
        Node result = helper.nodeWithName(null, "target1");

        assertNull(result);
    }

    @Test
    public void testNodeWithNameNonExistingName() {
        Document document = createMockDocument();
        Element root = document.createProcessingInstruction("xml", "version='1.0'");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);

        OperationsHelper helper = new OperationsHelperImpl();
        Node result = helper.nodeWithName(Arrays.asList(child1, child2), "nonExistingName");

        assertNull(result);
    }

    @Test
    public void testSublistWithName() {
        Document document = createMockDocument();
        Element root = document.createProcessingInstruction("xml", "version='1.0'");
        Element child1 = document.createElement("child1");
        Element target1 = document.createElement("target1");
        Element target2 = document.createElement("target2");
        child1.appendChild(target1);
        child1.appendChild(target2);
        Element child2 = document.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);

        OperationsHelper helper = new OperationsHelperImpl();
        List<Node> result = helper.sublistWithName(Arrays.asList(child1, child2), "target1");

        assertEquals(1, result.size());
        assertEquals(target1, result.get(0));
    }

    @Test
    public void testSublistWithNameEmptyList() {
        OperationsHelper helper = new OperationsHelperImpl();
        List<Node> result = helper.sublistWithName(Collections.emptyList(), "target1");

        assertEquals(0, result.size());
    }

    @Test
    public void testSublistWithNameNonExistingName() {
        Document document = createMockDocument();
        Element root = document.createProcessingInstruction("xml", "version='1.0'");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);

        OperationsHelper helper = new OperationsHelperImpl();
        List<Node> result = helper.sublistWithName(Arrays.asList(child1, child2), "nonExistingName");

        assertEquals(0, result.size());
    }

    private Document createMockDocument() {
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
        } catch (DocumentInitializationException e) {
            e.printStackTrace();
        }
        return document;
    }
}