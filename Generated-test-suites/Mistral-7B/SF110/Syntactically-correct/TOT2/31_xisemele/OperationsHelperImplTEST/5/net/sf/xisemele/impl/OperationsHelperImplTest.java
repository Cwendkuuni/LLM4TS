import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OperationsHelperImplTest {
    @Test
    public void testChildren() {
        final Document document = new DocumentImpl();
        final Element root = document.createElement("root");
        document.appendChild(root);
        root.appendChild(document.createElement("child1"));
        root.appendChild(document.createElement("child2"));

        final OperationsHelper helper = new OperationsHelperImpl();
        final List<Node> children = helper.children(root);
        assertEquals(Arrays.asList("child1", "child2"), children);
    }

    @Test
    public void testFind() {
        final Document document = new DocumentImpl();
        final Element root = document.createElement("root");
        document.appendChild(root);
        root.appendChild(document.createElement("child1"));
        root.appendChild(document.createElement("child2"));
        root.appendChild(document.createElement("grandchild1"));
        root.appendChild(document.createElement("grandchild1"));

        final OperationsHelper helper = new OperationsHelperImpl();
        final Node found = helper.find(document, "root/child1");
        assertNotNull(found);
        assertEquals("child1", found.getNodeName());

        final Node notFound = helper.find(document, "root/grandchild1/grandchild1");
        assertNull(notFound);
    }

    @Test
    public void testNodeWithName() {
        final Document document = new DocumentImpl();
        final Element root = document.createElement("root");
        document.appendChild(root);
        root.appendChild(document.createElement("child1"));
        root.appendChild(document.createElement("child2"));

        final OperationsHelper helper = new OperationsHelperImpl();
        final Node node = helper.nodeWithName(Arrays.asList(root, root.getChildNodes().item(0)), "child1");
        assertNotNull(node);
        assertEquals("child1", node.getNodeName());

        final Node notFound = helper.nodeWithName(Arrays.asList(root, root.getChildNodes().item(0)), "nonexistent");
        assertNull(notFound);
    }

    @Test
    public void testSublistWithName() {
        final Document document = new DocumentImpl();
        final Element root = document.createElement("root");
        document.appendChild(root);
        root.appendChild(document.createElement("child1"));
        root.appendChild(document.createElement("child2"));
        root.appendChild(document.createElement("grandchild1"));
        root.appendChild(document.createElement("grandchild1"));

        final OperationsHelper helper = new OperationsHelperImpl();
        final List<Node> sublist = helper.sublistWithName(Arrays.asList(root, root.getChildNodes().item(0)), "child");
        assertEquals(Arrays.asList(root.getChildNodes().item(0)), sublist);

        final List<Node> emptySublist = helper.sublistWithName(Arrays.asList(root), "grandchild");
        assertEquals(Arrays.asList(), emptySublist);
    }
}