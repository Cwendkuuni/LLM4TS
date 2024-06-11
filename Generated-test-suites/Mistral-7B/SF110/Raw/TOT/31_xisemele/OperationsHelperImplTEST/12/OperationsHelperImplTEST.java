import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.xisemele.impl.OperationsHelperImpl;

public class OperationsHelperImplTest {

    @org.junit.Test
    public void testChildren() {
        Document document = mock(Document.class);
        Node rootNode = mock(Node.class);
        NodeList nodeList = mock(NodeList.class);
        when(document.getDocumentElement()).thenReturn(rootNode);
        when(rootNode.getChildNodes()).thenReturn(nodeList);
        when(nodeList.getLength()).thenReturn(3);

        OperationsHelper helper = new OperationsHelperImpl();
        List<Node> result = helper.children(document);

        // Test with empty NodeList
        Node emptyNodeList = mock(NodeList.class);
        when(emptyNodeList.getLength()).thenReturn(0);
        result = helper.children(document);
        assertNull(result);

        // Test with valid NodeList
        Node node1 = mock(Node.class);
        Node node2 = mock(Node.class);
        Node node3 = mock(Node.class);
        when(nodeList.item(0)).thenReturn(node1);
        when(nodeList.item(1)).thenReturn(node2);
        when(nodeList.item(2)).thenReturn(node3);

        List<Node> expected = new ArrayList<>(Arrays.asList(node1, node2, node3));
        assertEquals(expected, result);
    }
}