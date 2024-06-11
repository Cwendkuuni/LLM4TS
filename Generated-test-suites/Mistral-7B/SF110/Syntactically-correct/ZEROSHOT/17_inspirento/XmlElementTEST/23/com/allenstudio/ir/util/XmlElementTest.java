import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Hashtable;
import java.util.Vector;

public class XmlElementTest {

    private XmlElement root;

    @Before
    public void setUp() {
        root = new XmlElement("root");;
    }

    @Test
    public void testConstructor() {
        XmlElement element = new XmlElement();
        assertNull(element.getName());
        assertNotNull(element.getSubElements());
        assertNotNull(element.getAttributes());

        element = new XmlElement("test");;
        assertEquals("test", element.getName());
        assertNotNull(element.getSubElements());
        assertNotNull(element.getAttributes());

        element = new XmlElement("test", new Hashtable<>());
        assertEquals("test", element.getName());
        assertNotNull(element.getSubElements());
        assertNotNull(element.getAttributes());
        assertEmpty(element.getSubElements());
        assertEmpty(element.getAttributes());

        element = new XmlElement("test", "testData");
        assertEquals("test", element.getName());
        assertEquals("testData", element.getData());
        assertNotNull(element.getSubElements());
        assertNotNull(element.getAttributes());
        assertEmpty(element.getSubElements());
        assertEmpty(element.getAttributes());
    }

    @Test
    public void testAddAttribute() {
        root.addAttribute("attribute1", "value1");
        assertNotNull(root.getAttribute("attribute1"));
        assertEquals("value1", root.getAttribute("attribute1"));

        root.addAttribute("attribute2", null);
        assertNull(root.getAttribute("attribute2"));

        root.addAttribute("attribute3", "value3");
        assertEquals("value1", root.getAttribute("attribute1"));
        assertEquals("value3", root.getAttribute("attribute3"));
    }

    @Test
    public void testGetAttribute() {
        root.addAttribute("attribute1", "value1");
        assertEquals("value1", root.getAttribute("attribute1"));

        root.addAttribute("attribute2", null);
        assertNull(root.getAttribute("attribute2"));

        root.addAttribute("attribute3", "value3");
        assertEquals("value1", root.getAttribute("attribute1"));
        assertEquals("value3", root.getAttribute("attribute3"));

        root.setAttribute(new Hashtable<>());
        assertEmpty(root.getAttributes());

        root.setAttributes(new Hashtable<>());
        assertEmpty(root.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        root.addAttribute("attribute1", "value1");
        root.addAttribute("attribute2", "value2");
        root.addAttribute("attribute3", "value3");

        assertFalse(root.getAttributeNames().hasMoreElements());

        root.setAttributes(new Hashtable<>());
        assertFalse(root.getAttributeNames().hasMoreElements());
    }

    @Test
    public void testAddElement() {
        XmlElement child1 = new XmlElement("child1");;
        XmlElement child2 = new XmlElement("child2");;

        root.addElement(child1);
        root.addElement(child2);

        assertEquals(2, root.getSubElements().size());
        assertTrue(root.getSubElements().contains(child1));
        assertTrue(root.getSubElements().contains(child2));

        child1.setParent(null);
        child2.setParent(null);
    }

    @Test
    public void testRemoveElement() {
        XmlElement child1 = new XmlElement("child1");;
        XmlElement child2 = new XmlElement("child2");;

        root.addElement(child1);
        root.addElement(child2);

        root.removeElement(child1);
        assertEquals(1, root.getSubElements().size());
        assertFalse(root.getSubElements().contains(child1));
        assertTrue(root.getSubElements().contains(child2));

        root.removeElement(child2);
        assertEquals(0, root.getSubElements().size());
        assertFalse(root.getSubElements().contains(child1));
        assertFalse(root.getSubElements().contains(child2));

        child1.setParent(null);
        child2.setParent(null);
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement child1 = new XmlElement("child1");;
        XmlElement child2 = new XmlElement("child2");;

        root.addElement(child1);
        root.addElement(child2);

        root.removeAllElements();
        assertEquals(0, root.getSubElements().size());
        assertFalse(root.getSubElements().contains(child1));
        assertFalse(root.getSubElements().contains(child2));

        child1.setParent(null);
        child2.setParent(null);
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement("parent");;
        XmlElement child = new XmlElement("child");;

        parent.addElement(child);

        child.removeFromParent();
        assertEquals(0, parent.getSubElements().size());
        assertFalse(parent.getSubElements().contains(child));

        child.setParent(null);
    }

    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement("parent");;
        XmlElement child1 = new XmlElement("child1");;
        XmlElement child2 = new XmlElement("child2");;

        parent.append(child1);
        parent.append(child2);

        assertEquals(2, parent.getSubElements().size());
        assertTrue(parent.getSubElements().contains(child1));
        assertTrue(parent.getSubElements().contains(child2));

        child1.setParent(null);
        child2.setParent(null);
    }

    @Test
    public void testInsertElement() {
        XmlElement parent = new XmlElement("parent");;
        XmlElement child1 = new XmlElement("child1");;
        XmlElement child2 = new XmlElement("child2");;

        parent.insertElement(child1, 1);
        parent.insertElement(child2, 1);

        assertEquals(2, parent.getSubElements().size());
        assertEquals(child1, parent.getSubElements().get(0));
        assertEquals(child2, parent.getSubElements().get(1));

        child1.setParent(null);
        child2.setParent(null);
    }

    @Test
    public void testGetElements() {
        XmlElement child1 = new XmlElement("child1");;
        XmlElement child2 = new XmlElement("child2");;

        root.addElement(child1);
        root.addElement(child2);

        List<XmlElement> elements = root.getElements();
        assertEquals(2, elements.size());
        assertTrue(elements.contains(child1));
        assertTrue(elements.contains(child2));
    }

    @Test
    public void testCount() {
        XmlElement child1 = new XmlElement("child1");;
        XmlElement child2 = new XmlElement("child2");;

        root.addElement(child1);
        root.addElement(child2);

        assertEquals(2, root.count());

        child1.setParent(null);
        child2.setParent(null);
    }

    @Test
    public void testGetElement() {
        XmlElement child1 = new XmlElement("child1");;
        XmlElement child2 = new XmlElement("child2");;
        XmlElement child3 = new XmlElement("child3");;

        root.addElement(child1);
        root.addElement(child2);
        root.addElement(child3);

        XmlElement element = root.getElement("child1/child2");
        assertNull(element);

        element = root.getElement("child1/child1");
        assertEquals(child1, element);

        element = root.getElement("child1");
        assertEquals(child1, element);

        element = root.getElement("child2/child3");
        assertNull(element);

        element = root.getElement("child2");
        assertEquals(child2, element);

        element = root.getElement("child3");
        assertEquals(child3, element);

        child1.setParent(null);
        child2.setParent(null);
        child3.setParent(null);
    }

    @Test
    public void testGetElementByPath() {
        XmlElement child1 = new XmlElement("child1");;
        XmlElement child2 = new XmlElement("child2");;
        XmlElement child3 = new XmlElement("child3");;
        XmlElement child4 = new XmlElement("child4");;

        root.addElement(child1);
        root.addElement(child2);
        root.addElement(child3);
        root.addElement(child4);

        XmlElement element = root.getElement("child1/child3");
        assertNull(element);

        element = root.getElement("child1/child1");
        assertEquals(child1, element);

        element = root.getElement("child1");
        assertEquals(child1, element);

        element = root.getElement("child2/child3");
        assertNull(element);

        element = root.getElement("child2");
        assertEquals(child2, element);

        element = root.getElement("child3");
        assertEquals(child3, element);

        element = root.getElement("child4");
        assertEquals(child4, element);

        child1.setParent(null);
        child2.setParent(null);
        child3.setParent(null);
        child4.setParent(null);
    }

    @Test
    public void testAddSubElement() {
        XmlElement parent = new XmlElement("parent");;

        XmlElement child1 = parent.addSubElement("child1");
        XmlElement child2 = parent.addSubElement("child2");
        XmlElement child3 = parent.addSubElement("child2/child3");

        assertEquals(3, parent.getSubElements().size());
        assertTrue(parent.getSubElements().contains(child1));
        assertTrue(parent.getSubElements().contains(child2));
        assertTrue(parent.getSubElements().contains(child3));

        assertEquals("child1", child1.getName());
        assertEquals("child2", child2.getName());
        assertEquals("child3", child3.getName());

        child1.setParent(null);
        child2.setParent(null);
        child3.setParent(null);
    }

    @Test
    public void testAddSubElementWithData() {
        XmlElement parent = new XmlElement("parent");;

        XmlElement child1 = parent.addSubElement("child1", "data1");
        XmlElement child2 = parent.addSubElement("child2", "data2");
        XmlElement child3 = parent.addSubElement("child2/child3", "data3");

        assertEquals(3, parent.getSubElements().size());
        assertTrue(parent.getSubElements().contains(child1));
        assertTrue(parent.getSubElements().contains(child2));
        assertTrue(parent.getSubElements().contains(child3));

        assertEquals("child1", child1.getName());
        assertEquals("data1", child1.getData());
        assertEquals("child2", child2.getName());
        assertEquals("data2", child2.getData());
        assertEquals("child3", child3.getName());
        assertEquals("data3", child3.getData());

        child1.setParent(null);
        child2.setParent(null);
        child3.setParent(null);
    }

    @Test
    public void testPrintNode() {
        XmlElement root = new XmlElement("root");;

        XmlElement child1 = root.addSubElement("child1");
        child1.addSubElement("grandchild1");
        child1.addAttribute("attribute1", "value1");
        child1.setData("data1");

        XmlElement child2 = root.addSubElement("child2");
        child2.addSubElement("grandchild2");
        child2.addAttribute("attribute2", "value2");
        child2.setData("data2");

        XmlElement child3 = root.addSubElement("child3", "data3");
        child3.addSubElement("grandchild3");
        child3.addAttribute("attribute3", "value3");

        root.printNode(0, System.out);

        assertEquals("root\n", root.getData() != null ? root.getData() : "");

        assertEquals("child1\nchild1 = 'data1'\nattribute1: value1\ngrandchild1\n", child1.getData() != null ? child1.getData() : "");

        assertEquals("child2\nchild2 = 'data2'\nattribute2: value2\ngrandchild2\n", child2.getData() != null ? child2.getData() : "");

        assertEquals("child3\nchild3 = 'data3'\nattribute3: value3\ngrandchild3\n", child3.getData() != null ? child3.getData() : "");

        root.removeAllElements();
    }

    @Test
    public void testClone() {
        XmlElement root = new XmlElement("root");;

        XmlElement child1 = root.addSubElement("child1");
        child1.addSubElement("grandchild1");
        child1.addAttribute("attribute1", "value1");
        child1.setData("data1");

        XmlElement child2 = root.addSubElement("child2");
        child2.addSubElement("grandchild2");
        child2.addAttribute("attribute2", "value2");
        child2.setData("data2");

        XmlElement clonedRoot = (XmlElement) root.clone();

        assertEquals(2, clonedRoot.getSubElements().size());

        XmlElement clonedChild1 = clonedRoot.getSubElements().get(0);
        assertEquals("child1", clonedChild1.getName());
        assertEquals("grandchild1", clonedChild1.getSubElements().get(0).getName());
        assertEquals("data1", clonedChild1.getData());
        assertEquals("value1", clonedChild1.getAttributes().get("attribute1"));

        XmlElement clonedChild2 = clonedRoot.getSubElements().get(1);
        assertEquals("child2", clonedChild2.getName());
        assertEquals("grandchild2", clonedChild2.getSubElements().get(0).getName());
        assertEquals("data2", clonedChild2.getData());
        assertEquals("value2", clonedChild2.getAttributes().get("attribute2"));

        root.removeAllElements();
    }

    @Test
    public void testEquals() {
        XmlElement xmlElement1 = new XmlElement();
        XmlElement xmlElement2 = new XmlElement();

        assertFalse(xmlElement1.equals(xmlElement2));

        xmlElement1.setName("xmlElement1");
        xmlElement2.setName("xmlElement1");

        xmlElement1.addAttribute("attribute1", "value1");
        xmlElement2.addAttribute("attribute1", "value1");

        xmlElement1.setData("data1");
        xmlElement2.setData("data1");

        xmlElement1.addSubElement("child1");
        xmlElement2.addSubElement("child1");

        assertTrue(xmlElement1.equals(xmlElement2));

        xmlElement1.getSubElements().get(0).setName("child2");
        assertFalse(xmlElement1.equals(xmlElement2));

        xmlElement1.getSubElements().get(0).setName("child1");

        xmlElement1.getAttributes().put("attribute2", "value2");
        assertFalse(xmlElement1.equals(xmlElement2));

        xmlElement1.getAttributes().remove("attribute2");

        assertTrue(xmlElement1.equals(xmlElement2));
    }
}