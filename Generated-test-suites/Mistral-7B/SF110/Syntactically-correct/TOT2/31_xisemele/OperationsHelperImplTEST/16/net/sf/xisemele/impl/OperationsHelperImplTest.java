import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import net.sf.xisemele.impl.OperationsHelperImpl;
import org.w3c.dom.*;
import org.junit.Test;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        Document doc = createTestDocument();
        Node root = doc.getDocumentElement();

        List<Node> children = helper.children(root);

        assertNotNull(children);
        assertEquals(6, children.size());
        assertTrue(children.get(0) instanceof Element);
        for (Node node : children) {
            assertTrue(node.getNodeType() == Node.ELEMENT_NODE);
        }
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void testChildrenWithNullNode() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        Node node = null;

        List<Node> children = helper.children(node);

        assertNull(children);
    }

    @Test
    public void testChildrenWithEmptyList() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        Node root = null;

        List<Node> children = helper.children(root);

        assertNotNull(children);
        assertTrue(children.isEmpty());
    }

    @Test
    public void testChildrenWithMixedNodeTypes() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        Document doc = createTestDocument();
        Node root = doc.getDocumentElement();
        Node commentNode = doc.createComment("This is a comment");
        Node textNode = doc.createTextNode("Some text");

        root.appendChild(commentNode);
        root.appendChild(textNode);

        List<Node> children = helper.children(root);

        assertEquals(3, children.size());
        assertTrue(children.get(0) instanceof Comment);
        assertTrue(children.get(1) instanceof Text);
        assertTrue(children.get(2) instanceof Element);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void testFindWithNullDocument() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        String path = "/notADocument";
        Document document = null;

        Node node = helper.find(document, path);

        assertNull(node);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void testFindWithNullPath() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        Document document = createTestDocument();

        Node node = helper.find(document, null);

        assertNull(node);
    }

    @Test
    public void testFindWithEmptyDocument() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        Document document = createEmptyDocument();
        String path = "/root/element1";

        Node node = helper.find(document, path);

        assertNull(node);
    }

    @Test
    public void testFindWithPathContainingInvalidNodes() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        Document doc = createTestDocument();
        String path = "/root/invalidElement/element1";

        Node node = helper.find(doc, path);

        assertNull(node);
    }

    @Test
    public void testFindWithMissingNodes() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        Document doc = createTestDocument();
        String path = "/root/missingElement/element1";

        Node node = helper.find(doc, path);

        assertNull(node);
    }

    @Test
    public void testFindWithMultipleNodesOfSameName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        Document doc = createTestDocumentWithDuplicateElements();
        String path = "/root/element1";

        Node node1 = helper.find(doc, path);
        Node node2 = helper.find(doc, path);

        assertNotNull(node1);
        assertNotNull(node2);
        assertEquals(node1, node2);
    }

    @Test
    public void testNodeWithNameWithEmptyList() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        List<Node> nodes = Collections.emptyList();
        String name = "element1";

        Node node = helper.nodeWithName(nodes, name);

        assertNull(node);
    }

    @Test
    public void testNodeWithNameWithNullNodeList() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        List<Node> nodes = null;
        String name = "element1";

        Node node = helper.nodeWithName(nodes, name);

        assertNull(node);
    }

    @Test
    public void testNodeWithNameWithMultipleNodesOfSameName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        Document doc = createTestDocumentWithDuplicateElements();
        List<Node> nodes = doc.getDocumentElement().getChildNodes();
        String name = "element1";

        Node node = helper.nodeWithName(nodes, name);

        assertNotNull(node);
    }

    @Test
    public void testSublistWithNameWithEmptyList() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        List<Node> nodes = Collections.emptyList();
        String name = "element1";

        List<Node> sublist = helper.sublistWithName(nodes, name);

        assertTrue(sublist.isEmpty());
    }

    @Test
    public void testSublistWithNameWithNullNodeList() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        List<Node> nodes = null;
        String name = "element1";

        List<Node> sublist = helper.sublistWithName(nodes, name);

        assertTrue(sublist.isEmpty());
    }

    @Test
    public void testSublistWithNameWithMultipleNodesOfSameName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        Document doc = createTestDocumentWithDuplicateElements();
        List<Node> nodes = doc.getDocumentElement().getChildNodes();
        String name = "element1";

        List<Node> sublist = helper.sublistWithName(nodes, name);

        assertEquals(2, sublist.size());
    }

    private Document createTestDocument() {
        Document doc = createDocument();
        Element root = doc.createElement("root");
        Element element1 = doc.createElement("element1");
        Element element2 = doc.createElement("element2");
        Element element3 = doc.createElement("element3");
        element3.appendChild(doc.createTextNode("Some text"));
        root.appendChild(element1);
        root.appendChild(element2);
        root.appendChild(element3);
        doc.appendChild(root);
        return doc;
    }

    private Document createDocument() {
        Document doc = new DocumentImpl();
        return doc;
    }

    private Document createEmptyDocument() {
        Document doc = createDocument();
        doc.normalize();
        return doc;
    }

    private Document createTestDocumentWithDuplicateElements() {
        Document doc = createDocument();
        Element root = doc.createElement("root");
        Element element1 = doc.createElement("element1");
        Element element2 = doc.createElement("element1");
        root.appendChild(element1);
        root.appendChild(element2);
        doc.appendChild(root);
        return doc;
    }
}

//