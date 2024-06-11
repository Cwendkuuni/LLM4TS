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

    @After
    public void tearDown() {
        root.removeAllElements();
    }

    @Test
    public void testXmlElementConstruction() {
        XmlElement element1 = new XmlElement();
        XmlElement element2 = new XmlElement("element");
        XmlElement element3 = new XmlElement("element", new Hashtable<String, String>());
        XmlElement element4 = new XmlElement("element", "data");

        assertNotNull(element1);
        assertNotNull(element2);
        assertNotNull(element3);
        assertNotNull(element4);

        assertEquals("", element1.getData());
        assertEquals("element", element2.getName());
        assertEquals("element", element3.getName());
        assertEquals("data", element4.getData());

        assertTrue(element1.getAttributes().isEmpty());
        assertTrue(element2.getAttributes().isEmpty());
        assertTrue(element3.getAttributes().isEmpty());
        assertTrue(element4.getAttributes().isEmpty());

        assertNull(element1.getParent());
        assertNull(element2.getParent());
        assertNull(element3.getParent());
        assertNull(element4.getParent());

        assertTrue(element1.subElements.isEmpty());
        assertTrue(element2.subElements.isEmpty());
        assertTrue(element3.subElements.isEmpty());
        assertTrue(element4.subElements.isEmpty());
    }

    @Test
    public void testAddAttribute() {
        root.addAttribute("test1", "value1");
        root.addAttribute("test2", "value2");

        Hashtable<String, String> attributes = root.getAttributes();

        assertEquals("value1", root.getAttribute("test1"));
        assertEquals("value2", root.getAttribute("test2"));
        assertEquals(2, attributes.size());

        Object oldValue1 = root.addAttribute("test1", "value3");
        Object oldValue2 = root.addAttribute("test2", "value4");

        assertEquals("value3", root.getAttribute("test1"));
        assertEquals("value4", root.getAttribute("test2"));
        assertNull(oldValue1);
        assertNull(oldValue2);
    }

    @Test
    public void testGetAttribute() {
        root.addAttribute("test1", "value1");
        root.addAttribute("test2", "value2");

        assertEquals("value1", root.getAttribute("test1"));
        assertEquals("value2", root.getAttribute("test2"));
        assertEquals("", root.getAttribute("nonexistent"));
        assertEquals("value1", root.getAttribute("test1", "defaultValue"));
        assertEquals("defaultValue", root.getAttribute("nonexistent", "defaultValue"));

        Hashtable<String, String> attributes = root.getAttributes();
        assertEquals("value1", attributes.get("test1"));
        assertEquals("value2", attributes.get("test2"));
        assertNull(attributes.get("nonexistent"));
    }

    @Test
    public void testGetAttributes() {
        root.addAttribute("test1", "value1");
        root.addAttribute("test2", "value2");

        Hashtable<String, String> expectedAttributes = new Hashtable<String, String>() {{
            put("test1", "value1");
            put("test2", "value2");
        }};

        Hashtable<String, String> attributes = root.getAttributes();

        assertEquals(expectedAttributes, attributes);
        assertSame(attributes, root.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<String, String>() {{
            put("test1", "value1");
            put("test2", "value2");
        }};

        root.setAttributes(attributes);

        Hashtable<String, String> expectedAttributes = attributes;
        Hashtable<String, String> actualAttributes = root.getAttributes();

        assertEquals(expectedAttributes, actualAttributes);
        assertSame(expectedAttributes, actualAttributes);
    }

    @Test
    public void testgetAttributeNames() {
        root.addAttribute("test1", "value1");
        root.addAttribute("test2", "value2");

        Enumeration enumeration = root.getAttributeNames();

        ArrayList<String> expectedNames = new ArrayList<String>() {{
            add("test1");
            add("test2");
        }};

        while (enumeration.hasMoreElements()) {
            assertEquals(expectedNames.remove(0), enumeration.nextElement());
        }

        assertTrue(expectedNames.isEmpty());
    }

    @Test
    public void testAddElement() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");

        root.addElement(child1);
        root.addElement(child2);

        List<XmlElement> elements = root.getElements();

        assertEquals(2, root.count());
        assertEquals(2, elements.size());

        XmlElement actualChild1 = root.getElement("child1");
        XmlElement actualChild2 = root.getElement("child2");
        XmlElement expectedChild1 = child1;
        XmlElement expectedChild2 = child2;

        assertEquals(expectedChild1, actualChild1);
        assertEquals(expectedChild2, actualChild2);

        assertEquals(root, expectedChild1.getParent());
        assertEquals(root, expectedChild2.getParent());
    }

    @Test
    public void testRemoveElement() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");

        root.addElement(child1);
        root.addElement(child2);

        root.removeElement(child1);

        List<XmlElement> elements = root.getElements();

        assertEquals(1, root.count());
        assertEquals(1, elements.size());

        XmlElement actualChild2 = root.getElement("child2");
        XmlElement expectedChild2 = child2;

        assertEquals(expectedChild2, actualChild2);

        assertNull(expectedChild1.getParent());
        assertEquals(root, expectedChild2.getParent());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");

        root.addElement(child1);
        root.addElement(child2);

        child1.removeFromParent();

        List<XmlElement> elements = root.getElements();

        assertEquals(1, root.count());
        assertEquals(1, elements.size());

        XmlElement actualChild2 = root.getElement("child2");
        XmlElement expectedChild2 = child2;

        assertEquals(expectedChild2, actualChild2);

        assertNull(expectedChild1.getParent());
        assertEquals(null, expectedChild2.getParent());
    }

    @Test
    public void testClone() {
        XmlElement original = new XmlElement("original");
        XmlElement clone = (XmlElement)original.clone();

        Hashtable<String, String> originalAttributes = original.getAttributes();
        Hashtable<String, String> cloneAttributes = clone.getAttributes();

        List<XmlElement> originalSubElements = original.getElements();
        List<XmlElement> cloneSubElements = clone.getElements();

        assertEquals(originalAttributes, cloneAttributes);
        assertEquals(originalSubElements, cloneSubElements);

        assertNotSame(original, clone);
    }

    @Test
    public void testPrintNode() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        XmlElement element3 = new XmlElement("element3") {{
            setAttribute("test", "value");
            addElement(element1);
            addElement(element2);
        }};

        root.addElement(element3);

        XmlElement.printNode(element3, "");

        String expectedOutput = "element3:\n" +
                " test:value\n" +
                "element1\n" +
                "element2\n";

        String actualOutput = System.out.getOutputStream().toString();

        assertEquals(expectedOutput, actualOutput);
    }
}