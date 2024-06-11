import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import static org.junit.Assert.assertEquals;

public class OperationsHelperImplTest {
    @Test
    public void testChildren() {
        Document document = createTestDocument();
        Node root = document.getDocumentElement();
        List<Node> expectedChildren = Arrays.asList(
                createTestNode("child1"),
                createTestNode("child2"),
                createTestNode("child3")
        );
        List<Node> actualChildren = new OperationsHelperImpl().children(root);
        assertEquals(expectedChildren, actualChildren);
    }

    private Document createTestDocument() {
        // Create a test document with the given structure:
        // document -> element1 -> child1 -> text1
        //                         -> child2 -> text2
        //                         -> child3
        Document document = createTestDocument("element1");
        Element element1 = (Element) document.getDocumentElement();
        element1.setTagName("element1");
        Node child1 = createTestNode("child1");
        child1.setNodeValue("text1");
        Node child2 = createTestNode("child2");
        child2.setNodeValue("text2");
        Node child3 = createTestNode("child3");
        element1.appendChild(child1);
        element1.appendChild(child2);
        element1.appendChild(child3);
        return document;
    }

    private Document createTestDocument(String tagName) {
        Document document = createEmptyDocument();
        Element root = document.createProcessingInstruction("xml", "version=\"1.0\"");
        Element documentElement = document.createElement(tagName);
        documentElement.setNodeValue(tagName);
        document.appendChild(root);
        document.appendChild(documentElement);
        return document;
    }

    private Node createTestNode(String nodeName) {
        Element node = document.createElement(nodeName);
        node.setNodeName(nodeName);
        return node;
    }

    private Document createEmptyDocument() {
        Document document = new DocumentImpl();
        return document;
    }
}