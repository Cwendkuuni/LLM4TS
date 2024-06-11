import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import net.sf.xisemele.impl.OperationsHelperImpl;

public class ChildrenTest {

    @org.junit.Test
    public void testChildren() {
        Document document = createDocument();
        Node rootNode = document.getDocumentElement();
        List<Node> children = new OperationsHelperImpl().children(rootNode);

        assertNotNull(children);
        assertEquals(3, children.size());

        for (Node node : children) {
            assertNotNull(node);
            assertEquals(1, node.getNodeType());
        }
    }

    private Document createDocument() {
        // Create and return a test document here
    }
}