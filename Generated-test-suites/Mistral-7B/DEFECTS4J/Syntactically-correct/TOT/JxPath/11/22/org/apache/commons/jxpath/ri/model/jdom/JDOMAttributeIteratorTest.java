import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() {
        Element root = new Element("root");
        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        assertFalse("Empty iterator should not have any attributes", iterator.hasNext());
    }

    @Test
    public void testSingleAttribute() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        Element root = new Element("root", "root", new Namespace(xmlns, "xml"));
        root.addAttribute("id", "123");
        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "id"));
        NodePointer attribute = iterator.next();
        assertTrue("Iterator should have next element", attribute != null);
        assertEquals("Iterator should return expected attribute", "123", attribute.getTextValue());
        assertFalse("Iterator should not have next element after returning the first one", iterator.hasNext());
    }

    @Test
    public void testMultipleAttributes() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "prefix";
        Element root = new Element("root", "root", new Namespace(xmlns, "xml"));
        root.addAttribute("id1", "123");
        root.addAttribute("id2", "456", new Namespace(xmlns, prefix));
        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "id"));
        NodePointer attribute1 = iterator.next();
        NodePointer attribute2 = iterator.next();
        assertTrue("Iterator should have next element", attribute2 != null);
        assertEquals("Iterator should return expected attribute", "456", attribute2.getTextValue());
        assertFalse("Iterator should not have next element after returning the second one", iterator.hasNext());

        iterator = new JDOMAttributeIterator(parent, new QName("", "id", new Namespace(xmlns, prefix)));
        NodePointer attribute3 = iterator.next();
        assertTrue("Iterator should have next element", attribute3 != null);
        assertEquals("Iterator should return expected attribute", "456", attribute3.getTextValue());
        assertFalse("Iterator should not have next element after returning the third one", iterator.hasNext());
    }

    @Test
    public void testEmptyAttributeName() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        Element root = new Element("root", "root", new Namespace(xmlns, "xml"));
        root.addAttribute("id1", "123");
        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        assertFalse("Empty iterator should not have any attributes", iterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullParent() {
        NodePointer parent = null;
        new JDOMAttributeIterator(parent, new QName("", "*"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        new JDOMAttributeIterator(parent, null);
    }
}