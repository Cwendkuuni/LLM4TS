import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.junit.Test;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        Document doc = createDocument();
        Element root = doc.createElement("root");
        root.appendChild(createElement("child1", "tag1"));
        root.appendChild(createElement("child2", "tag2"));
        root.appendChild(createElement("child3", "tag3"));
        doc.appendChild(root);

        List<Node> result = new OperationsHelperImpl().children(root);
        assertEquals(3, result.size());
        assertNotNull(result.get(0));
        assertTrue(result.get(0) instanceof Element);
        assertEquals("child1", result.get(0).getNodeName());
        assertEquals("tag1", result.get(0).getNodeValue());

        assertNotNull(result.get(1));
        assertTrue(result.get(1) instanceof Element);
        assertEquals("child2", result.get(1).getNodeName());
        assertEquals("tag2", result.get(1).getNodeValue());

        assertNotNull(result.get(2));
        assertTrue(result.get(2) instanceof Element);
        assertEquals("child3", result.get(2).getNodeName());
        assertEquals("tag3", result.get(2).getNodeValue());
    }

    private Document createDocument() {
        // ... create and return document instance here ...
    }

    private Node createElement(String nodeName, String nodeValue) {
        // ... create and return node instance here ...
    }
}