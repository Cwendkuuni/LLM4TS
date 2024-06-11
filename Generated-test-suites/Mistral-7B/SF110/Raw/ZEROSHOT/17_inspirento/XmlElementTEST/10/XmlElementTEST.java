import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import static org.junit.Assert.*;

public class XmlElementTest {

    private XmlElement root;

    @Before
    public void setUp() {
        root = new XmlElement("root");;
    }

    @Test
    public void testConstructorWithName() {
        XmlElement xmlElement = new XmlElement("element");
        assertEquals("element", xmlElement.getName());
        assertEquals(new Hashtable<>(), xmlElement.getAttributes());
        assertEquals(new Vector<>(), xmlElement.getElements());
        assertNull(xmlElement.getParent());
        assertNull(xmlElement.getData());
    }

    @Test
    public void testConstructorWithNameAndAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");

        XmlElement xmlElement = new XmlElement("element", attributes);
        assertEquals("element", xmlElement.getName());
        assertEquals(attributes, xmlElement.getAttributes());
        assertEquals(new Vector<>(), xmlElement.getElements());
        assertNull(xmlElement.getParent());
        assertNull(xmlElement.getData());
    }

    @Test
    public void testConstructorWithNameAndData() {
        XmlElement xmlElement = new XmlElement("element", "data");
        assertEquals("element", xmlElement.getName());
        assertEquals(new Hashtable<>(), xmlElement.getAttributes());
        assertEquals(new Vector<>(), xmlElement.getElements());
        assertNull(xmlElement.getParent());
        assertEquals("data", xmlElement.getData());
    }

    @Test
    public void testAddAttribute() {
        root.addAttribute("attr1", "value1");
        Hashtable<String, String> attributes = root.getAttributes();
        assertEquals("value1", attributes.get("attr1"));
    }

    @Test
    public void testGetAttribute() {
        root.addAttribute("attr1", "value1");
        assertEquals("value1", root.getAttribute("attr1"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        root.addAttribute("attr1", "value1");
        assertEquals("value1", root.getAttribute("attr1"));
        assertEquals("defaultValue", root.getAttribute("attr2", "defaultValue"));
        assertNull(root.getAttribute("attr3"));
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

        Hashtable<String, String> attributes2 = new Hashtable<>();
        attributes2.put("attr3", "value3");

        root.setAttributes(attributes1);
        assertEquals(attributes1, root.getAttributes());
        root.setAttributes(attributes2);
        assertEquals(attributes2, root.getAttributes());
    }

    @Test
    public void testgetAttributeNames() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");

        root.setAttributes(attributes);

        Enumeration<String> enumeration = root.getAttributeNames();
        String name;
        while (enumeration.hasMoreElements()) {
            name = enumeration.nextElement();
            assertEquals(name, root.getAttribute(name));
        }
    }

    @Test
    public void testAddElement() {
        XmlElement child = new XmlElement("child1");
        root.addElement(child);
        assertEquals(1, root.count());
        assertEquals(child, root.getElement(0));
    }

    @Test
    public void testRemoveElement() {
        XmlElement child = new XmlElement("child1");
        root.addElement(child);
        root.removeElement(child);
        assertEquals(0, root.count());
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        root.addElement(child1);
        root.addElement(child2);

        root.removeElementByIndex(0);
        assertEquals(1, root.count());
        assertEquals(child2, root.getElement(0));
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        root.addElement(child1);
        root.addElement(child2);

        root.removeAllElements();
        assertEquals(0, root.count());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement child = new XmlElement("child");
        root.addElement(child);
        child.removeFromParent();
        assertEquals(0, root.count());
    }

    @Test
    public void testAppend() {
        XmlElement child = new XmlElement("child");
        root.append(child);
        assertEquals(1, root.count());
        assertEquals(child, root.getElement(0));
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
    }

    @Test
    public void testGetElements() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        root.addElement(child1);
        root.addElement(child2);

        List<XmlElement> elements = root.getElements();
        assertEquals(2, elements.size());
        assertEquals(child1, elements.get(0));
        assertEquals(child2, elements.get(1));
    }

    @Test
    public void testCount() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        root.addElement(child1);
        root.addElement(child2);

        assertEquals(2, root.count());
    }

    @Test
    public void testGetElement() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        XmlElement child3 = new XmlElement("child3");

        root.addElement(child1);
        root.addElement(child2);
        root.addElement(child3);

        assertEquals(child1, root.getElement("child1"));
        assertEquals(child2, root.getElement("child2"));
        assertEquals(child3, root.getElement("child3"));
        assertNull(root.getElement("child4"));
    }

    @Test
    public void testGetElementByIndex() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        XmlElement child3 = new XmlElement("child3");

        root.addElement(child1);
        root.addElement(child2);
        root.addElement(child3);

        assertEquals(child1, root.getElement(0));
        assertEquals(child2, root.getElement(1));
        assertEquals(child3, root.getElement(2));
    }

    @Test
    public void testAddSubElement() {
        XmlElement child = new XmlElement("child");
        root.addSubElement(child);
        assertEquals(1, root.count());
        assertEquals(child, root.getElement(0));
    }

    @Test
    public void testAddSubElementWithPath() {
        XmlElement subChild1 = new XmlElement("subChild1");
        XmlElement subChild2 = new XmlElement("subChild2");
        root.addSubElement("child1", subChild1);
        root.addSubElement("child1/child2", subChild2);

        List<XmlElement> elements = root.getElements();
        assertEquals(3, elements.size());
        XmlElement child1 = elements.get(0);
        assertEquals(2, child1.count());
        assertEquals(subChild1, child1.getElement("subChild1"));
        assertEquals(subChild2, child1.getElement("child2/subChild2"));
    }

    @Test
    public void testAddSubElementWithExistingElement() {
        XmlElement child1 = new XmlElement("child1");
        root.addSubElement("child1", child1);

        XmlElement child2 = new XmlElement("child2");
        root.addSubElement("child1/child2", child2);

        XmlElement child3 = new XmlElement("child3");
        root.addSubElement("child1/child2/child3", child3);

        assertEquals(3, root.count());
        assertEquals(child1, root.getElement("child1"));
        assertEquals(child2, root.getElement("child1/child2"));
        assertEquals(child3, root.getElement("child1/child2/child3"));
    }

    @Test
    public void testClone() {
        XmlElement original = new XmlElement("original");
        original.addAttribute("attr1", "value1");
        original.addAttribute("attr2", "value2");
        original.addElement(new XmlElement("child1"));
        original.addElement(new XmlElement("child2"));

        XmlElement cloned = (XmlElement) original.clone();

        assertEquals(original, cloned);
        assertNotEquals(original.hashCode(), cloned.hashCode());

        Hashtable<String, String> attributes1 = original.getAttributes();
        Hashtable<String, String> attributes2 = cloned.getAttributes();
        assertNotEquals(attributes1, attributes2);
        assertEquals(attributes1, attributes2);

        List<XmlElement> elementsOriginal = original.getElements();
        List<XmlElement> elementsCloned = cloned.getElements();
        assertEquals(elementsOriginal, elementsCloned);
        assertNotEquals(elementsOriginal, elementsCloned);
    }

    @Test
    public void testEquals() {
        XmlElement xmlElement1 = new XmlElement("element");
        XmlElement xmlElement2 = new XmlElement("element");

        XmlElement xmlElement3 = new XmlElement("element");
        xmlElement3.setAttributes(new Hashtable<>());
        xmlElement3.setElements(new Vector<>());

        XmlElement xmlElement4 = new XmlElement("element");
        xmlElement4.setAttributes(new Hashtable<>());
        xmlElement4.setElements(new Vector<>());
        xmlElement4.setData("data");

        XmlElement xmlElement5 = new XmlElement("element");
        xmlElement5.setAttributes(new Hashtable<>());
        xmlElement5.setElements(new Vector<>());
        xmlElement5.setData("otherData");

        XmlElement xmlElement6 = new XmlElement("element");
        xmlElement6.setAttributes(new Hashtable<>());
        xmlElement6.setElements(new Vector<>());
        xmlElement6.setName("otherName");

        XmlElement xmlElement7 = new XmlElement("element");
        xmlElement7.setAttributes(new Hashtable<>());
        xmlElement7.setElements(new Vector<>());
        xmlElement7.setName("element");
        xmlElement7.setData("data");

        assertTrue(xmlElement1.equals(xmlElement1));
        assertTrue(xmlElement1.equals(xmlElement2));
        assertFalse(xmlElement1.equals(xmlElement3));
        assertFalse(xmlElement1.equals(xmlElement4));
        assertFalse(xmlElement1.equals(xmlElement5));
        assertFalse(xmlElement1.equals(xmlElement6));
        assertFalse(xmlElement1.equals(xmlElement7));

        Hashtable<String, String> attributes1 = new Hashtable<>();
        attributes1.put("attr1", "value1");
        attributes1.put("attr2", "value2");

        XmlElement xmlElement8 = new XmlElement("element");
        xmlElement8.setAttributes(attributes1);
        xmlElement8.setElements(new Vector<>());

        assertTrue(xmlElement1.equals(xmlElement8));
    }

    @After
    public void tearDown() {
        root = null;
    }
}