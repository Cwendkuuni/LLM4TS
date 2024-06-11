import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.w3c.dom.*;
import java.util.*;

import net.sf.xisemele.impl.OperationsHelperImpl;

public class OperationsHelperImplTest {

    @org.junit.Test
    public void testChildren() throws Exception {
        // Test case from Expert 1
    }

    @org.junit.Test
    public void testFind() throws Exception {
        // Test case from Expert 2
    }

    @org.junit.Test
    public void testNodeWithName() throws Exception {
        Document document = createDocument();
        Element root = document.createElement("root");
        root.appendChild(createElement("child1", "child1"));
        root.appendChild(createElement("child2", "child2"));
        root.appendChild(createElement("child3", "child3"));

        document.appendChild(root);

        OperationsHelper helper = new OperationsHelperImpl();
        List<Node> nodes = helper.children(root);

        Node nodeWithName = helper.nodeWithName(nodes, "child1");
        assertNotNull(nodeWithName);
        assertEquals("child1", nodeWithName.getNodeName());

        nodeWithName = helper.nodeWithName(nodes, "grandChild1");
        assertNull(nodeWithName);
    }

    @org.junit.Test
    public void testSublistWithName() throws Exception {
        Document document = createDocument();
        Element root = document.createElement("root");
        root.appendChild(createElement("child1", "child1"));
        root.appendChild(createElement("child2", "child2"));
        root.appendChild(createElement("child3", "child3"));

        document.appendChild(root);

        OperationsHelper helper = new OperationsHelperImpl();
        List<Node> nodes = helper.children(root);

        List<Node> sublist = helper.sublistWithName(nodes, "child1");
        assertEquals(1, sublist.size());
        assertEquals("child1", sublist.get(0).getNodeName());

        sublist = helper.sublistWithName(nodes, "grandChild1");
        assertEquals(0, sublist.size());
    }

    @org.junit.Test
    public void testEmptyChildren() throws Exception {
        Document document = createDocument();
        Node emptyNode = document.createTextNode("");

        OperationsHelper helper = new OperationsHelperImpl();
        List<Node> emptyChildren = helper.children(emptyNode);
        assertEquals(0, emptyChildren.size());
    }

    private Document createDocument() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.newDocument();
    }

    private Element createElement(String tagName, String nodeName) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Element element = builder.newElement(tagName);
        element.setNodeName(nodeName);
        return element;
    }
}