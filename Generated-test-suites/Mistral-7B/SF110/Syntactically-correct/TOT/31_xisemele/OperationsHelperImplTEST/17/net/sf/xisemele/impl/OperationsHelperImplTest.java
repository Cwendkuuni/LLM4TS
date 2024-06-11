import static org.junit.Assert.*;
import static org.w3c.dom.Node.*;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        Document document = createDocument();
        Element root = document.createElement("root");
        root.appendChild(createElement("child1"));
        root.appendChild(createElement("child2"));
        root.appendChild(createElement("grandchild1"));
        root.appendChild(createElement("grandchild2"));

        List<Node> result = new OperationsHelperImpl().children(root);

        assertEquals(4, result.size());
        assertEquals(root, result.get(0));
        assertEquals("child1", result.get(1).getNodeName());
        assertEquals("child2", result.get(2).getNodeName());
        assertEquals("grandchild1", result.get(3).getNodeName());
    }
}