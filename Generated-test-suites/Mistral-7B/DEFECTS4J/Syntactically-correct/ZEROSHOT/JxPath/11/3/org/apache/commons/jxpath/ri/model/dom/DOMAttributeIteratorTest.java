import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class DOMAttributeIteratorTest {

    @Mock
    private NodePointer parentMock;

    @Mock
    private Element elementMock;

    @Mock
    private QName nameMock;

    @Mock
    private Attr attrMock1;

    @Mock
    private Attr attrMock2;

    @Mock
    private Attr attrMock3;

    @Mock
    private NamedNodeMap namedNodeMapMock;

    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        when(parentMock.getNode()).thenReturn(new Object() {
            Node NODE = mock(Node.class);

            Node getNode() {
                return NODE;
            }

            Node getAttributes() {
                return namedNodeMapMock;
            }

            Node getElement() {
                return elementMock;
            }

            String getNodeName() {
                return "elementName";
            }

            String getNamespaceURI(String prefix) {
                return "http://test.com";
            }
        });

        when(elementMock.getAttributeNodeNS(String.class, "namespace:localName")).thenReturn(attrMock1);
        when(elementMock.getAttributeNode("localName")).thenReturn(attrMock2);
        when(namedNodeMapMock.getLength()).thenReturn(3);
        when(namedNodeMapMock.item(0)).thenReturn(attrMock1);
        when(namedNodeMapMock.item(1)).thenReturn(attrMock2);
        when(namedNodeMapMock.item(2)).thenReturn(attrMock3);

        iterator = new DOMAttributeIterator(parentMock, nameMock);
    }

    @Test
    public void testConstructor() {
        assertEquals("parent", parentMock, iterator.getParent());
        assertEquals("name", nameMock, iterator.getName());
        assertEquals(0, iterator.getPosition());

        assertTrue(iterator instanceof NodeIterator);
    }

    @Test
    public void testGetNodePointer() {
        when(nameMock.getName()).thenReturn("localName");

        // Test when position is 1
        NodePointer nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer instanceof DOMAttributePointer);
        assertEquals("parent", parentMock, nodePointer.getParent());
        assertEquals("attr1", attrMock1, nodePointer.getNode());
        assertEquals(1, iterator.getPosition());

        // Test when position is 2
        PowerMockito.when(iterator.setPosition(2)).thenReturn(true);
        nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer instanceof DOMAttributePointer);
        assertEquals("parent", parentMock, nodePointer.getParent());
        assertEquals("attr3", attrMock3, nodePointer.getNode());
        assertEquals(2, iterator.getPosition());

        // Test when position is 0
        PowerMockito.when(iterator.setPosition(0)).thenReturn(true);
        nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer instanceof DOMAttributePointer);
        assertEquals("parent", parentMock, nodePointer.getParent());
        assertEquals("attr1", attrMock1, nodePointer.getNode());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());

        PowerMockito.when(iterator.setPosition(3)).thenReturn(false);
        assertEquals(0, iterator.getPosition());

        PowerMockito.when(iterator.setPosition(1)).thenReturn(true);
        assertEquals(1, iterator.getPosition());

        PowerMockito.when(iterator.setPosition(2)).thenReturn(true);
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(2));
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(4));
    }
}