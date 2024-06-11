import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Hashtable;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class XmlElementTest {

    private XmlElement root;

    @Before
    public void setUp() {
        root = new XmlElement("root");;
    }

    @After
    public void tearDown() {
        root = null;
    }

    @Test
    public void testXmlElementConstructor() {
        XmlElement element = new XmlElement();
        assertEquals(null, element.getName());
        assertEquals(null, element.getData());
        assertEquals(0, element.count());
        assertEquals(null, element.getParent());

        element = new XmlElement("elementName");
        assertEquals("elementName", element.getName());
        assertEquals(null, element.getData());
        assertEquals(0, element.count());
        assertEquals(null, element.getParent());

        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        element = new XmlElement("elementName", attributes);
        assertEquals("elementName", element.getName());
        assertEquals(null, element.getData());
        assertEquals(0, element.count());
        assertEquals(null, element.getParent());
        assertEquals("value1", element.getAttribute("attr1"));
        assertEquals("value2", element.getAttribute("attr2"));

        String data = "testData";
        element = new XmlElement("elementName", data);
        assertEquals("elementName", element.getName());
        assertEquals(data, element.getData());
        assertEquals(0, element.count());
        assertEquals(null, element.getParent());
    }

    @Test
    public void testAddAttribute() {
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");
        assertEquals("value1", root.getAttribute("attr1"));
        assertEquals("value2", root.getAttribute("attr2"));
        assertEquals("value1", root.getAttribute("attr1", "defaultValue"));
        assertEquals("value2", root.getAttribute("attr2", "defaultValue"));
        assertEquals(2, root.getAttributes().size());
        assertNull(root.getAttribute("nonexistentAttribute"));
    }

    @Test
    public void testGetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        root.setAttributes(attributes);
        assertEquals(attributes, root.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes1 = new Hashtable<>();
        attributes1.put("attr1", "value1");
        attributes1.put("attr2", "value2");
        root.setAttributes(attributes1);
        assertEquals(attributes1, root.getAttributes());

        Hashtable<String, String> attributes2 = new Hashtable<>();
        attributes2.put("attr1", "newValue1");
        attributes2.put("attr3", "newValue3");
        root.setAttributes(attributes2);
        assertEquals(attributes2, root.getAttributes());
        assertEquals(2, root.getAttributes().size());
        assertEquals("newValue1", root.getAttribute("attr1"));
        assertEquals("newValue3", root.getAttribute("attr3"));
    }

    @Test
    public void testGetAttributeNames() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        root.setAttributes(attributes);
        assertEquals("attr1", root.getAttributeNames().nextElement());
        assertEquals("attr2", root.getAttributeNames().nextElement());
    }

    @Test
    public void testAddElement() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        root.addElement(child1);
        root.addElement(child2);
        assertEquals(2, root.count());
        assertEquals(child1, root.getElement(0));
        assertEquals(child2, root.getElement(1));
        assertEquals(child1, child1.getParent());
        assertEquals(child2, child2.getParent());
    }

    @Test
    public void testRemoveElement() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        root.addElement(child1);
        root.addElement(child2);
        assertEquals(2, root.count());
        root.removeElement(child1);
        assertEquals(1, root.count());
        assertNull(root.getElement(0));
        assertEquals(child1, child1.getParent());
        assertEquals(null, child1.getParent());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        root.addElement(child1);
        root.addElement(child2);
        assertEquals(2, root.count());
        child1.removeFromParent();
        assertEquals(1, root.count());
        assertNull(root.getElement(0));
        assertEquals(null, child1.getParent());
    }

    @Test
    public void testInsertElement() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        root.addElement(child1);
        root.insertElement(child2, 0);
        assertEquals(2, root.count());
        assertEquals(child2, root.getElement(0));
        assertEquals(child1, root.getElement(1));
        assertEquals(child2, child2.getParent());
        assertEquals(child1, child1.getParent());
    }

    @Test
    public void testAddSubElement() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        XmlElement child3 = new XmlElement("child3");
        root.addSubElement("child1");
        assertEquals(1, root.count());
        assertEquals(child1, root.getElement(0));
        assertEquals(null, child1.getParent());
        assertEquals(child1, child1.getParent());
        root.addSubElement("child1/child2");
        assertEquals(2, root.count());
        assertEquals(child1, root.getElement(0));
        assertEquals(child2, root.getElement(1));
        assertEquals(child1, child2.getParent());
        assertEquals(child2, child2.getParent());
        root.addSubElement("child1/child3");
        assertEquals(3, root.count());
        assertEquals(child1, root.getElement(0));
        assertEquals(child2, root.getElement(1));
        assertEquals(child3, root.getElement(2));
        assertEquals(child1, child3.getParent());
        assertEquals(child3, child3.getParent());
    }

    @Test
    public void testSetParent() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        root.addElement(child1);
        root.addElement(child2);
        child1.setParent(null);
        assertEquals(1, root.count());
        assertNull(root.getElement(0));
        child1.setParent(child2);
        assertEquals(1, child2.count());
        assertEquals(child1, child2.getElement(0));
        assertEquals(child2, child1.getParent());
    }

    @Test
    public void testGetParent() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        root.addElement(child1);
        root.addElement(child2);
        assertEquals(null, child1.getParent());
        assertEquals(child1, child2.getParent().getElement(0));
        assertEquals(child2, child1.getParent());
    }

    @Test
    public void testSetData() {
        root.setData("testData");
        assertEquals("testData", root.getData());
    }

    @Test
    public void testGetData() {
        root.setData("testData");
        assertEquals("testData", root.getData());
    }

    @Test
    public void testClone() {
        XmlElement original = new XmlElement("original");
        XmlElement cloned = (XmlElement) original.clone();
        assertEquals(original.getName(), cloned.getName());
        assertEquals(original.getData(), cloned.getData());
        assertEquals(original.count(), cloned.count());
        assertEquals(original.getAttributes().toString(), cloned.getAttributes().toString());

        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        original.setAttributes(attributes);
        assertEquals(attributes, original.getAttributes());
        assertEquals(attributes, cloned.getAttributes());

        List<XmlElement> subElements = new Vector<>();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        subElements.add(child1);
        subElements.add(child2);
        original.setElements(subElements);
        assertEquals(subElements, original.getElements());
        assertEquals(subElements, cloned.getElements());
        assertEquals(child1, cloned.getElement("child1"));
        assertEquals(child2, cloned.getElement("child2"));
    }
}