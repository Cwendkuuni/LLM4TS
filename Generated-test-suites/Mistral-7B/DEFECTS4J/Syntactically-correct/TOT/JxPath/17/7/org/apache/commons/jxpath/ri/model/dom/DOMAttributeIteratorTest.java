import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class DOMAttributeIteratorTest {

    @Test
    public void testEmptyElement() {
        Element element = mock(Element.class);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(mock(Document.class), null), null);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testSelfAttribute() {
        String prefix = "xmlns";
        String localName = "xmlns:xlink";
        String value = "http://www.w3.org/1999/xlink";
        Element element = mock(Element.class);
        when(element.getAttributeNodeNS(eq("http://www.w3.org/2000/xmlns/"), eq(new QName(prefix, localName)))).thenReturn(createAttr("xmlns:xlink", value));

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(mock(Document.class), element), new QName(prefix, localName));
        Assert.assertEquals(new DOMAttributePointer(new DOMPointer(mock(Document.class), element), createAttr("xmlns:xlink", value)), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testGlobalAttribute() {
        String prefix = null;
        String localName = "xmlns:xlink";
        String value = "http://www.w3.org/1999/xlink";
        Element element = mock(Element.class);
        when(element.getAttributeNode(localName)).thenReturn(createAttr(localName, value));

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(mock(Document.class), element), new QName(prefix, localName));
        Assert.assertEquals(new DOMAttributePointer(new DOMPointer(mock(Document.class), element), createAttr(localName, value)), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testNamespacedAttribute() {
        String prefix = "x";
        String localName = "name";
        String value = "value";
        Element element = mock(Element.class);
        when(element.getAttributeNodeNS(eq("http://example.com/ns"), eq(new QName(prefix, localName)))).thenReturn(createAttr("http://example.com/ns:name", value));

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(mock(Document.class), element), new QName(prefix, localName));
        Assert.assertEquals(new DOMAttributePointer(new DOMPointer(mock(Document.class), element), createAttr("http://example.com/ns:name", value)), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalStateException.class)
    public void testNullParent() {
        new DOMAttributeIterator(null, new QName("", ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        new DOMAttributeIterator(new DOMPointer(mock(Document.class), mock(Element.class)), null);
    }

    @Test
    public void testMultipleAttributes() {
        String prefix = null;
        String localName1 = "class";
        String value1 = "foo";
        String localName2 = "id";
        String value2 = "bar";
        Element element = mock(Element.class);
        when(element.getAttributes()).thenReturn(createNodeList(createAttr(localName1, value1), createAttr(localName2, value2)));

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(mock(Document.class), element), null);
        Assert.assertEquals(new DOMAttributePointer(new DOMPointer(mock(Document.class), element), createAttr(localName1, value1)), iterator.next());
        Assert.assertEquals(new DOMAttributePointer(new DOMPointer(mock(Document.class), element), createAttr(localName2, value2)), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testFilteringNamespace() {
        String prefix = null;
        String localName1 = "class";
        String value1 = "foo";
        String prefix2 = "xmlns";
        String localName2 = "xmlns:xlink";
        String value2 = "http://www.w3.org/1999/xlink";
        Element element = mock(Element.class);
        when(element.getAttributes()).thenReturn(createNodeList(createAttr(localName1, value1), createAttr(localName2, value2)));

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(mock(Document.class), element), new QName(prefix2, localName2));
        Assert.assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(new DOMPointer(mock(Document.class), element), new QName(prefix, localName1));
        Assert.assertEquals(new DOMAttributePointer(new DOMPointer(mock(Document.class), element), createAttr(localName1, value1)), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testFilteringPrefix() {
        String prefix = "x";
        String localName1 = "name";
        String value1 = "value1";
        String prefix2 = "y";
        String localName2 = "name";
        String value2 = "value2";
        Element element = mock(Element.class);
        when(element.getAttributes()).thenReturn(createNodeList(createAttr("x:name", value1), createAttr("y:name", value2)));

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(mock(Document.class), element), new QName(prefix, localName1));
        Assert.assertEquals(new DOMAttributePointer(new DOMPointer(mock(Document.class), element), createAttr("x:name", value1)), iterator.next());
        Assert.assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(new DOMPointer(mock(Document.class), element), new QName(prefix2, localName1));
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testFilteringPrefixAndNamespace() {
        String prefix = "x";
        String localName = "name";
        String value = "value";
        String prefix2 = "y";
        String namespace = "http://example.com/ns";
        Element element = mock(Element.class);
        when(element.getAttributes()).thenReturn(createNodeList(createAttr("x:name", value), createAttr("y:name", value), createAttr(namespace + ":name", value)));

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(mock(Document.class), element), new QName(prefix, localName, namespace));
        Assert.assertEquals(new DOMAttributePointer(new DOMPointer(mock(Document.class), element), createAttr(namespace + ":name", value)), iterator.next());
        Assert.assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(new DOMPointer(mock(Document.class), element), new QName(prefix2, localName));
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testFilteringPrefixAndNamespace2() {
        String prefix = "x";
        String localName = "name";
        String value = "value";
        String prefix2 = "y";
        String namespace = "http://example.com/ns";
        Element element = mock(Element.class);
        when(element.getAttributes()).thenReturn(createNodeList(createAttr("x:name", value), createAttr("y:name", value), createAttr("z:" + localName, value), createAttr(namespace + ":name", value)));

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(mock(Document.class), element), new QName(prefix, localName, namespace));
        Assert.assertEquals(new DOMAttributePointer(new DOMPointer(mock(Document.class), element), createAttr(namespace + ":name", value)), iterator.next());
        Assert.assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(new DOMPointer(mock(Document.class), element), new QName(prefix2, localName));
        Assert.assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(new DOMPointer(mock(Document.class), element), new QName(prefix, localName));
        Assert.assertEquals(new DOMAttributePointer(new DOMPointer(mock(Document.class), element), createAttr("x:name", value)), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testFilteringNamespaces() {
        String prefix = null;
        String localName = "class";
        String value = "foo";
        String prefix2 = "xmlns";
        String localName2 = "xmlns:xlink";
        String value2 = "http://www.w3.org/1999/xlink";
        String namespace = "http://example.com/ns";
        Element element = mock(Element.class);
        when(element.getAttributes()).thenReturn(createNodeList(createAttr(localName, value), createAttr(localName2, value2), createAttr(namespace + ":class", value)));

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(mock(Document.class), element), new QName(prefix, localName));
        Assert.assertEquals(new DOMAttributePointer(new DOMPointer(mock(Document.class), element), createAttr(localName, value)), iterator.next());
        Assert.assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(new DOMPointer(mock(Document.class), element), new QName(prefix2, localName2));
        Assert.assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(new DOMPointer(mock(Document.class), element), new QName(prefix, localName, namespace));
        Assert.assertEquals(new DOMAttributePointer(new DOMPointer(mock(Document.class), element), createAttr(namespace + ":class", value)), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNextAfterLast() {
        String prefix = null;
        String localName = "class";
        String value = "foo";
        Element element = mock(Element.class);
        when(element.getAttributes()).thenReturn(createNodeList(createAttr(localName, value)));

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(mock(Document.class), element), new QName(prefix, localName));
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNextBeforeFirst() {
        String prefix = null;
        String localName = "class";
        String value = "foo";
        Element element = mock(Element.class);
        when(element.getAttributes()).thenReturn(createNodeList(createAttr(localName, value)));

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(mock(Document.class), element), new QName(prefix, localName));
        Assert.assertFalse(iterator.hasNext());
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNextAfterLast2() {
        String prefix = null;
        String localName1 = "class";
        String value1 = "foo";
        String localName2 = "id";
        String value2 = "bar";
        Element element = mock(Element.class);
        when(element.getAttributes()).thenReturn(createNodeList(createAttr(localName1, value1), createAttr(localName2, value2)));

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(mock(Document.class), element), new QName(prefix, localName1));
        iterator.next();
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
    }

    private static Node createAttr(String name, String value) {
        Attr attr = mock(Attr.class);
        when(attr.getNodeName()).thenReturn(name);
        when(attr.getValue()).thenReturn(value);
        return attr;
    }

    private static NodeList createNodeList(Node... nodes) {
        NodeList nodeList = mock(NodeList.class);
        when(nodeList.getLength()).thenReturn(nodes.length);
        for (int i = 0; i < nodes.length; i++) {
            when(nodeList.item(i)).thenReturn(nodes[i]);
        }
        return nodeList;
    }
}