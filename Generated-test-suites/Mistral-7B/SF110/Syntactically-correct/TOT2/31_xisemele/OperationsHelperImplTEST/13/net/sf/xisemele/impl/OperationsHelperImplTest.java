import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import static org.junit.Assert.assertEquals;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        Document doc = createDocument();
        Node root = doc.getDocumentElement();
        List<Node> expectedChildren = Arrays.asList(new Node[] { createElement("child1"), createElement("child2") });
        List<Node> actualChildren = new OperationsHelperImpl().children(root);
        assertEquals(expectedChildren, actualChildren);
    }

    // ... helper methods for creating Node, Document, etc.
}