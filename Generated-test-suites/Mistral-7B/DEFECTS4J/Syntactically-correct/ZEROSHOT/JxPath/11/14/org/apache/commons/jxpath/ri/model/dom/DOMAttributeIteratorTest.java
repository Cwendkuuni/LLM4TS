import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class DOMAttributeIteratorTest {

    @org.junit.Before
    public void setUp() {
        // Initialize mocks
        Element elementMock = mock(Element.class);
        NamedNodeMap nodeMapMock = mock(NamedNodeMap.class);
        Attr attr1Mock = mock(Attr.class);
        Attr attr2Mock = mock(Attr.class);
        Attr attr3Mock = mock(Attr.class);

        when(elementMock.getAttributes()).thenReturn(nodeMapMock);
        when(nodeMapMock.getLength()).thenReturn(3);
        when(nodeMapMock.item(0)).thenReturn(attr1Mock);
        when(nodeMapMock.item(1)).thenReturn(attr2Mock);
        when(nodeMapMock.item(2)).thenReturn(attr3Mock);

        QName qname1 = new QName("ns1", "attr1");
        QName qname2 = new QName("ns2", "attr2");
        QName qname3 = new QName("ns3", "attr3");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(elementMock), qname1);
    }

    @org.junit.Test
    public void testDOMAttributeIterator_shouldReturnAttributeWithMatchingNameAndNamespace_whenGivenParentElementAndName() {
        // Given

        // When
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();

        // Then
        assertSame(attr1Mock, attributePointer.getNode());
        assertSame(qname1, attributePointer.getName());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_shouldReturnNull_whenGivenParentElementAndNameDoesNotMatchAnyAttribute() {
        // Given
        iterator = new DOMAttributeIterator(new DOMPointer(elementMock), new QName("ns4", "attr4"));

        // When
        assertNull(iterator.getNodePointer());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_shouldReturnNull_whenGivenNullParentPointer() {
        // Given
        iterator = new DOMAttributeIterator(null, qname1);

        // When
        assertNull(iterator.getNodePointer());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_shouldReturnNull_whenPositionIsOutOfBounds() {
        // Given
        iterator = new DOMAttributeIterator(new DOMPointer(elementMock), qname1);

        // When
        assertFalse(iterator.setPosition(5));

        // Then
        assertNull(iterator.getNodePointer());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_shouldReturnCorrectNodePointer_whenPositionIsValid() {
        // Given
        iterator = new DOMAttributeIterator(new DOMPointer(elementMock), qname1);

        // When
        assertTrue(iterator.setPosition(1));

        // Then
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
        assertSame(attr2Mock, attributePointer.getNode());
        assertSame(qname1, attributePointer.getName());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_shouldReturnCorrectPosition_whenSettingPositionToTheLastAttribute() {
        // Given
        iterator = new DOMAttributeIterator(new DOMPointer(elementMock), qname1);

        // When
        for (int i = 0; i < 3; i++) {
            assertTrue(iterator.setPosition(i + 1));
        }

        // Then
        assertTrue(iterator.setPosition(4));
        assertSame(attr3Mock, iterator.getNodePointer().getNode());
        assertSame(qname1, iterator.getNodePointer().getName());
        assertTrue(iterator.setPosition(0));
        assertSame(attr1Mock, iterator.getNodePointer().getNode());
        assertSame(qname1, iterator.getNodePointer().getName());
    }
}