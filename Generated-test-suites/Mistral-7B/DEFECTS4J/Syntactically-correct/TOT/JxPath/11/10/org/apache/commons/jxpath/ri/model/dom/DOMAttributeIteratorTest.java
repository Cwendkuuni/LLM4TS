package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import static org.apache.commons.jxpath.ri.model.dom.DOMUtils.createDocument;

public class DOMAttributeIteratorTest {

    private NodePointer root;
    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        Document doc = createDocument();
        Element element = doc.createElement("root");
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:x", "http://www.example.com/x");
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:y", "http://www.example.com/y");
        element.setAttributeNS("", "attr1", "value1");
        element.setAttributeNS("", "attr2", "value2");
        element.setAttributeNS("", "attr3", "value3");
        root = new DOMPoint(doc.createDocumentFragment(), element);
    }

    @Test
    public void testEmptyIterator() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(null, null), null);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithOneAttribute() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(root, new QName("", "attr1"));
        Assert.assertNotNull(iterator.next());
        Assert.assertEquals("value1", iterator.next().getNodeValue());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithNamespaceAwareAttribute() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(root, new QName("http://www.w3.org/2000/xmlns/", "xmlns:x"));
        Assert.assertNull(iterator.next());
        iterator.next(); // skip xmlns:y
        Assert.assertEquals("http://www.example.com/x", iterator.next().getNodeValue());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithWildcardName() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(root, new QName("", "*"));
        for (int i = 0; i < 3; i++) {
            Assert.assertNotNull(iterator.next());
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithNullName() {
        new DOMAttributeIterator(root, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithNullParent() {
        new DOMAttributeIterator(null, new QName("", "attr1"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIteratorOutOfBounds() {
        iterator = new DOMAttributeIterator(root, new QName("", "attr1"));
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next(); // should throw IndexOutOfBoundsException
    }
}