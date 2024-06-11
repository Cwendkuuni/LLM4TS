import static org.junit.Assert.*;
import static net.sf.xisemele.impl.OperationsHelperImpl.children;
import static net.sf.xisemele.impl.OperationsHelperImpl.getNode;

import org.w3c.dom.*;
import org.junit.Test;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        Document doc = createDocument();
        Node root = doc.getDocumentElement();
        Node parent = getNode("/root/parent", doc);
        Node child1 = getNode("/root/parent/child1", doc);
        Node child2 = getNode("/root/parent/child2", doc);

        List<Node> actualChildren = children(parent);

        // typical use case
        assertEquals(2, actualChildren.size());
        assertEquals(child1, actualChildren.get(0));
        assertEquals(child2, actualChildren.get(1));

        // edge case: empty node list
        Node emptyNode = getNode("/root/empty", doc);
        List<Node> actualEmptyChildren = children(emptyNode);
        assertEquals(0, actualEmptyChildren.size());

        // error case: null node
        assertNull(children(null));
    }

    // helper method to create a simple XML document
    private Document createDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element root = doc.createElement("root");
            doc.appendChild(root);

            Element parent = doc.createElement("parent");
            root.appendChild(parent);

            Element child1 = doc.createElement("child1");
            parent.appendChild(child1);

            Element child2 = doc.createElement("child2");
            parent.appendChild(child2);

            Element empty = doc.createElement("empty");
            root.appendChild(empty);

            return doc;
        } catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    private Node getNode(String path, Document doc) {
        Node node = doc.getDocumentElement();
        String[] parts = path.split("/");
        for (String part : parts) {
            node = findNode(node, part);
            if (node == null) {
                fail("Failed to find node with path: " + path);
            }
        }
        return node;
    }

    private Node findNode(Node node, String name) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node child = nodeList.item(i);
            if (child.getNodeName().equals(name)) {
                return child;
            }
        }
        return null;
    }
}