package org.apache.commons.jxpath.ri.model.dom.test;

import org.apache.commons.jxpath.ri.model.*;
import org.apache.commons.jxpath.ri.model.dom.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class DOMAttributeIteratorTest {

    private NodePointer parentPointer;
    private QName name;

    @BeforeEach
    public void setup() {
        parentPointer = new NodePointer(null, null, null, new Element("testElement"));
        name = new QName("testName");
    }

    // Test for method 1: DOMAttributeIterator(NodePointer parent, QName name)

    @Test
    public void testDOMAttributeIteratorConstruction() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorConstructionWithElement() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
        assertNotNull(attributePointer);
        assertEquals("testElement", attributePointer.getNode().getTagName());
        assertEquals("testName", attributePointer.getName().getName());
    }

    // Test for method 2: testAttr(Attr attr, QName testName)

    @Test
    public void testTestAttr() {
        Attr attr = new Attr("testAttr", "testValue");
        QName testName = new QName("testNS", "testLocalName");

        assertFalse(DOMAttributeIterator.testAttr(attr, testName));

        testName = new QName("testNS", "*");
        assertTrue(DOMAttributeIterator.testAttr(attr, testName));
    }

    // Test for method 3: equalsStrings(String s1, String s2)

    @Test
    public void testEqualStrings() {
        String s1 = "testString1";
        String s2 = "testString1";
        assertTrue(DOMAttributeIterator.equalStrings(s1, s2));

        s1 = "testString1";
        s2 = "testString2";
        assertFalse(DOMAttributeIterator.equalStrings(s1, s2));

        s1 = null;
        s2 = "testString1";
        assertFalse(DOMAttributeIterator.equalStrings(s1, s2));

        s1 = "testString1";
        s2 = null;
        assertFalse(DOMAttributeIterator.equalStrings(s1, s2));
    }

    // Test for method 4: getAttribute(Element element, QName name)

    @Test
    public void testGetAttribute() {
        Element element = new Element("testElement");
        QName name = new QName("testNS", "testLocalName");
        Attr attr = new Attr("testAttr", "testValue");
        element.setAttributeNode(attr);

        Attr result = DOMAttributeIterator.getAttribute(element, name);
        assertEquals(attr, result);

        element.removeAttributeNode(attr);
        result = DOMAttributeIterator.getAttribute(element, name);
        assertNull(result);
    }

    // Test for method 5: setPosition(int position)

    @Test
    public void testSetPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);

        assertFalse(iterator.setPosition(0));
        assertNull(iterator.getNodePointer());

        iterator.next();
        assertTrue(iterator.setPosition(1));
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
        assertEquals(1, iterator.getPosition());
        assertNotNull(attributePointer);

        iterator.setPosition(4); // Past the end of the list
        assertFalse(iterator.setPosition(4));
        assertNull(iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());
    }

    // Test for method 6: getPosition()

    @Test
    public void testGetPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);

        iterator.next();
        assertEquals(1, iterator.getPosition());

        iterator.next();
        assertEquals(2, iterator.getPosition());

        iterator.setPosition(0);
        assertEquals(1, iterator.getPosition());
    }

    // Test for method 1: DOMAttributeIterator(NodePointer parent, QName name) with null parent

    @Test
    public void testDOMAttributeIteratorConstructionWithNullParent() {
        QName name = new QName("testName");
        NodePointer parentPointer = new NodePointer(null, null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);
        assertNull(iterator.getNodePointer());
    }

    // Test for method 1: DOMAttributeIterator(NodePointer parent, QName name) with null name

    @Test
    public void testDOMAttributeIteratorConstructionWithNullName() {
        NodePointer parentPointer = new NodePointer(null, null, null, new Element("testElement"));
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, null);
        assertNull(iterator.getNodePointer());
    }

    // Test for error handling

    @Test
    public void testDOMAttributeIteratorExceptionHandling() {
        NodePointer parentPointer = new NodePointer(null, null, null, null);
        QName name = new QName("testName");

        assertThrows(IllegalStateException.class, () -> new DOMAttributeIterator(parentPointer, name));
    }

}