import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.dom.DOMDocumentFactory;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.junit.Assert;

public class DOMAttributeIteratorTest {

    @org.junit.Test
    public void testEmptyElement() {
        Document doc = DOMDocumentFactory.newInstance().createDocument("", "");
        Element root = (Element) doc.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(doc, root), new QName("", "*"));
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertNull(iterator.getNodePointer());
    }

    @org.junit.Test
    public void testSingleAttribute() {
        Document doc = DOMDocumentFactory.newInstance().createDocument("", "<elem foo=\"bar\"/>");
        Element root = (Element) doc.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(doc, root), new QName("", "foo"));
        Assert.assertEquals(1, iterator.getPosition());
        NodePointer np = iterator.getNodePointer();
        Assert.assertTrue(np instanceof DOMAttributePointer);
        Assert.assertEquals("bar", ((DOMAttributePointer) np).getValue().getNodeValue());
    }

    @org.junit.Test
    public void testMultipleAttributes() {
        Document doc = DOMDocumentFactory.newInstance().createDocument("", "<elem attr1=\"val1\" attr2=\"val2\"/>");
        Element root = (Element) doc.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(doc, root), new QName("", "*"));
        Assert.assertEquals(2, iterator.getPosition());
        NodePointer np1 = iterator.getNodePointer();
        Assert.assertTrue(np1 instanceof DOMAttributePointer);
        Assert.assertEquals("val1", ((DOMAttributePointer) np1).getValue().getNodeValue());
        iterator.next();
        NodePointer np2 = iterator.getNodePointer();
        Assert.assertTrue(np2 instanceof DOMAttributePointer);
        Assert.assertEquals("val2", ((DOMAttributePointer) np2).getValue().getNodeValue());
    }

    @org.junit.Test
    public void testNamespaceAttribute() {
        Document doc = DOMDocumentFactory.newInstance().createDocument("", "<foo:elem xmlns:foo=\"http://example.com/namespace\" attr=\"val\"/>");
        Element root = (Element) doc.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(doc, root), new QName("http://example.com/namespace", "attr"));
        Assert.assertEquals(1, iterator.getPosition());
        NodePointer np = iterator.getNodePointer();
        Assert.assertTrue(np instanceof DOMAttributePointer);
        Assert.assertEquals("val", ((DOMAttributePointer) np).getValue().getNodeValue());
    }

    @org.junit.Test
    public void testAttributeNotPresent() {
        Document doc = DOMDocumentFactory.newInstance().createDocument("", "<elem/>");
        Element root = (Element) doc.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(doc, root), new QName("", "nonExistentAttr"));
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertNull(iterator.getNodePointer());
    }

    @org.junit.Test
    public void testAttributeWithPrefix() {
        Document doc = DOMDocumentFactory.newInstance().createDocument("", "<foo:elem xmlns:foo=\"http://example.com/namespace\" foo:attr=\"val\"/>");
        Element root = (Element) doc.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(doc, root), new QName("http://example.com/namespace", "foo:attr"));
        Assert.assertEquals(1, iterator.getPosition());
        NodePointer np = iterator.getNodePointer();
        Assert.assertTrue(np instanceof DOMAttributePointer);
        Assert.assertEquals("val", ((DOMAttributePointer) np).getValue().getNodeValue());
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testNextAfterLast() {
        Document doc = DOMDocumentFactory.newInstance().createDocument("", "<elem attr1=\"val1\" attr2=\"val2\"/>");
        Element root = (Element) doc.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(doc, root), new QName("", "*"));
        iterator.next();
        iterator.next();
        iterator.next();
    }

    @org.junit.Test(expected = ClassCastException.class)
    public void testNonAttributeNode() {
        Document doc = DOMDocumentFactory.newInstance().createDocument("", "<elem><child/></elem>");
        Element root = (Element) doc.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(doc, root.getFirstChild()), new QName("", "*"));
    }
}