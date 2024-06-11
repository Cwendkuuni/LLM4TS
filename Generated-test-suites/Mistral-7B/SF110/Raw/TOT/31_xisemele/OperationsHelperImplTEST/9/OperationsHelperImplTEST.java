import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.sf.xisemele.impl.OperationsHelperImpl;

public class OperationsHelperImplTest {

    @org.junit.Test
    public void testChildren() {
        Document mockDocument = mock(Document.class);
        Element rootElement = mock(Element.class);
        Node childElement1 = mock(Element.class);
        Node childElement2 = mock(Element.class);
        NodeList mockNodeList = mock(NodeList.class);

        when(mockDocument.getDocumentElement()).thenReturn(rootElement);
        when(rootElement.getChildNodes()).thenReturn(mockNodeList);
        when(mockNodeList.getLength()).thenReturn(2);
        when(mockNodeList.item(0)).thenReturn(childElement1);
        when(mockNodeList.item(1)).thenReturn(childElement2);
        when(childElement1.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(childElement2.getNodeType()).thenReturn(Node.ELEMENT_NODE);

        OperationsHelper helper = new OperationsHelperImpl();
        List<Node> result = helper.children(mockDocument);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(childElement1, result.get(0));
        assertEquals(childElement2, result.get(1));
    }
}