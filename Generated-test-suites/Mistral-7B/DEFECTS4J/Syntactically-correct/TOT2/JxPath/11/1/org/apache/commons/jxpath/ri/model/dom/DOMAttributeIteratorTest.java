
import org.junit.*;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.MethodSorters;
import org.w3c.dom.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(BlockJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DOMAttributeIteratorTest {
    private static final String TEST_NAMESPACE_URI = "test-namespace-uri";
    private static final String TEST_PREFIX = "test-prefix";
    private static final String TEST_LOCAL_NAME = "test-local-name";

    @Before
    public void setUp() {
        System.setProperty("javax.xml.parser.ErrorReportingPropertyFile", "org/apache/commons/jxpath/ri/model/dom/DOMAttributeIteratorTest-errors.properties");
    }

    @After
    public void tearDown() {
        System.clearProperty("javax.xml.parser.ErrorReportingPropertyFile");
    }

    @Test
    public void testConstructorWithEmptyNode() {
        NodePointer nodePointer = new DOMPointedNode(mock(Node.class));
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("test"));

        assertTrue(iterator.isEmpty());
    }

    @Test
    public void testConstructorWithSingleAttribute() {
        Node node = mock(Node.class);
        Element element = mock(Element.class);
        when(node.getParentNode()).thenReturn(element);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(element.getAttributeNode((QName) anyObject())).thenReturn(mock(Attr.class));

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointedNode(node), new QName("test"));
        assertFalse(iterator.isEmpty());
    }

    @Test
    public void testNext() {
        Node node = mock(Node.class);
        Element element = mock(Element.class);
        when(node.getParentNode()).thenReturn(element);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(element.getAttributeNode((QName) anyObject())).thenReturn(mock(Attr.class));
        when(element.getAttributeNodeNS(eq(TEST_NAMESPACE_URI), eq(TEST_LOCAL_NAME))).thenReturn(mock(Attr.class));
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointedNode(node), new QName("test"));
        iterator.next();
        iterator.next();

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNext() {
        Node node = mock(Node.class);
        Element element = mock(Element.class);
        when(node.getParentNode()).thenReturn(element);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(element.getAttributeNode((QName) anyObject())).thenReturn(mock(Attr.class));
        when(element.getAttributeNodeNS(eq(TEST_NAMESPACE_URI), eq(TEST_LOCAL_NAME))).thenReturn(mock(Attr.class));
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointedNode(node), new QName("test"));
        iterator.next();
        iterator.next();

        assertFalse(iterator.hasNext());
    }

    // Add remaining test cases here.

    @Test
    public void testToString() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(mock(NodePointer.class), new QName("test"));

        assertEquals("DOMAttributeIterator[position=0, iteratorSize=3]", iterator.toString());
    }
}
