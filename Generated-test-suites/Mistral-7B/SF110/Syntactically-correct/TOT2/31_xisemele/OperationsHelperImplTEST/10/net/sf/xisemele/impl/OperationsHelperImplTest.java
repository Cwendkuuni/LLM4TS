import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.*;
import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {
    @Test
    public void testChildren() {
        Document document = createDocumentWithExpectedNodes();
        List<Node> children = new OperationsHelperImpl().children(document.getDocumentElement());
        Assert.assertEquals(numberOfChildrenInDocument, children.size()); // replace numberOfChildrenInDocument with an actual number
        for (Node child : children) {
            Assert.assertNotNull(child);
            Assert.assertEquals(Node.ELEMENT_NODE, child.getNodeType());
        }
    }

    @Test
    public void testFind() {
        Document document = createDocumentWithExpectedNodes();
        String path = "/root/child1/child2";
        Node foundNode = new OperationsHelperImpl().find(document, path);
        Assert.assertNotNull(foundNode);
        // additional assertions for edge cases
    }

    @Test
    public void testNodeWithName() {
        Document document = createDocumentWithExpectedNodes();
        List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
        Node expectedNode = document.createElement("expectedName");
        nodes.add(expectedNode);
        Node node = new OperationsHelperImpl().nodeWithName(nodes, "expectedName");
        Assert.assertEquals(expectedNode, node);
    }

    @Test
    public void testSublistWithName() {
        Document document = createDocumentWithExpectedNodes();
        List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
        String name = "expectedName";
        List<Node> sublist = new OperationsHelperImpl().sublistWithName(nodes, name);
        Assert.assertEquals(numberOfNodesWithExpectedName, sublist.size()); // replace numberOfNodesWithExpectedName with an actual number
        for (Node node : sublist) {
            Assert.assertEquals(name, node.getNodeName());
        }
    }

    @Test
    public void testCaseForEmptyDocument() {
        Document document = null;
        String path = "/root/child1/child2";
        List<Node> nodes = new ArrayList<>();
        Node foundNode = new OperationsHelperImpl().find(document, path);
        List<Node> expectedNodes = new OperationsHelperImpl().children(document.getDocumentElement());
        List<Node> sublist = new OperationsHelperImpl().sublistWithName(expectedNodes, "child1");
        Node nodeWithName = new OperationsHelperImpl().nodeWithName(sublist, "child2");
        List<Node> emptyChildren = new OperationsHelperImpl().children(nodeWithName);
        Assert.assertEquals(0, emptyChildren.size());
        foundNode = new OperationsHelperImpl().find(document, path);
        Assert.assertNull(foundNode);
    }

    @Test
    public void testCaseForIncorrectPathFormat() {
        Document document = createDocumentWithExpectedNodes();
        String path = "/root/child1/child2/";
        Node foundNode = new OperationsHelperImpl().find(document, path);
        Assert.assertNull(foundNode);
    }

    @Test
    public void testCaseForNonExistentNode() {
        Document document = createDocumentWithExpectedNodes();
        String path = "/root/nonExistentChild/child2";
        Node foundNode = new OperationsHelperImpl().find(document, path);
        Assert.assertNull(foundNode);
    }

    @Test
    public void testCaseForMultipleNodesWithSameName() {
        Document document = createDocumentWithExpectedNodes();
        List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
        String name = "expectedName";
        int count = 3;
        for (int i = 0; i < count; i++) {
            Node expectedNode = document.createElement("expectedName");
            nodes.add(expectedNode);
        }
        List<Node> sublist = new OperationsHelperImpl().sublistWithName(nodes, name);
        Assert.assertEquals(count, sublist.size());
        List<Node> wrongSublist = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            wrongSublist.add(nodes.get(i));
        }
        Node wrongNode = new OperationsHelperImpl().nodeWithName(wrongSublist, name);
        Assert.assertNull(wrongNode);
    }

    private Document createDocumentWithExpectedNodes() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        document.appendChild(documentBuilder.createElement("root"));
        Node root = document.getDocumentElement();
        Node child1 = documentBuilder.createElement("child1");
        root.appendChild(child1);
        Node child2 = documentBuilder.createElement("child2");
        child1.appendChild(child2);
        return document;
    }

    private int numberOfChildrenInDocument = 1;
    private int numberOfNodesWithExpectedName = 1;
}