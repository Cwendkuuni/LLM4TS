import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class XmlElementTest {

    private XmlElement root;

    @Before
    public void setUp() {
        root = new XmlElement("root");;
    }

    @Test
    public void testEmptyConstructor() {
        XmlElement element = new XmlElement();
        assertEquals(Collections.emptyMap(), element.getAttributes());
        assertTrue(element.getElements().isEmpty());
        assertNull(element.getParent());
    }

    @Test
    public void testNameConstructor() {
        XmlElement element = new XmlElement("elementName");
        assertEquals("elementName", element.getName());
        assertTrue(element.getElements().isEmpty());
        assertNull(element.getParent());
    }

    @Test
    public void testNameConstrucorWithAttributes() {
        XmlElement element = new XmlElement("elementName", new HashMap<String, String>() {{
            put("attributeName1", "attributeValue1");
            put("attributeName2", "attributeValue2");
        }});
        assertEquals("elementName", element.getName());
        assertEquals(2, element.getAttributes().size());
        assertEquals("attributeValue1", element.getAttribute("attributeName1"));
        assertEquals("attributeValue2", element.getAttribute("attributeName2"));
        assertTrue(element.getElements().isEmpty());
        assertNull(element.getParent());
    }

    @Test
    public void testNameConstructorWithData() {
        XmlElement element = new XmlElement("elementName", "someData");
        assertEquals("elementName", element.getName());
        assertEquals("someData", element.getData());
        assertTrue(element.getElements().isEmpty());
        assertNull(element.getParent());
    }

    @Test
    public void testAddAttribute() {
        root.addAttribute("attributeName1", "attributeValue1");
        root.addAttribute("attributeName2", "attributeValue2");
        assertEquals(2, root.getAttributes().size());
        assertEquals("attributeValue1", root.getAttribute("attributeName1"));
        assertEquals("attributeValue2", root.getAttribute("attributeName2"));
    }

    @Test
    public void testGetAttribute() {
        root.addAttribute("attributeName1", "attributeValue1");
        assertEquals("attributeValue1", root.getAttribute("attributeName1"));
        assertEquals(null, root.getAttribute("attributeName2"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        root.addAttribute("attributeName1", "attributeValue1");
        assertEquals("attributeValue1", root.getAttribute("attributeName1"));
        assertEquals("defaultValue", root.getAttribute("attributeName2", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        root.addAttribute("attributeName1", "attributeValue1");
        root.addAttribute("attributeName2", "attributeValue2");
        assertEquals(new HashMap<String, String>(Arrays.asList(new String[]{"attributeName1", "attributeValue1"}, new String[]{"attributeName2", "attributeValue2"})), root.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        HashMap<String, String> attributes = new HashMap<String, String>() {{
            put("attributeName1", "attributeValue1");
            put("attributeName2", "attributeValue2");
        }};
        root.setAttributes(attributes);
        assertEquals(new HashMap<String, String>(Arrays.asList(new String[]{"attributeName1", "attributeValue1"}, new String[]{"attributeName2", "attributeValue2"})), root.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        root.addAttribute("attributeName1", "attributeValue1");
        root.addAttribute("attributeName2", "attributeValue2");
        List<String> attributeNames = Arrays.asList("attributeName1", "attributeName2");
        assertEquals(attributeNames, Arrays.asList(root.getAttributeNames().toArray(new String[0])));
    }

    @Test
    public void testAddElement() {
        XmlElement child = new XmlElement("child");
        root.addElement(child);
        assertEquals(1, root.getElements().size());
        assertSame(child, root.getElements().get(0));
        assertEquals(root, child.getParent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddElementNull() {
        root.addElement(null);
    }

    @Test
    public void testRemoveElement() {
        XmlElement child = new XmlElement("child");
        root.addElement(child);
        root.removeElement(child);
        assertEquals(0, root.getElements().size());
        assertNull(child.getParent());
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        root.addElement(child1);
        root.addElement(child2);
        root.removeElement(1);
        assertEquals(1, root.getElements().size());
        assertSame(child1, root.getElements().get(0));
        assertNull(child1.getParent());
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        root.addElement(child1);
        root.addElement(child2);
        root.removeAllElements();
        assertEquals(0, root.getElements().size());
        assertNull(child1.getParent());
        assertNull(child2.getParent());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement child = new XmlElement("child");
        root.addElement(child);
        child.removeFromParent();
        assertEquals(0, root.getElements().size());
        assertNull(child.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement child = new XmlElement("child");
        root.removeFromParent();
        root.append(child);
        assertEquals(1, root.getElements().size());
        assertEquals(child, root.getElements().get(0));
        assertSame(root, child.getParent());
    }

    @Test
    public void testInsertElement() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        root.addElement(child1);
        root.insertElement(child2, 0);
        assertEquals(2, root.getElements().size());
        assertSame(child2, root.getElements().get(0));
        assertSame(root, child2.getParent());
        assertSame(child1, root.getElements().get(1));
        assertSame(root, child1.getParent());
    }

    @Test
    public void testGetElements() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        root.addElement(child1);
        root.addElement(child2);
        assertEquals(new Vector<XmlElement>(Arrays.asList(child1, child2)), root.getElements());
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
    public void testGetElementByPath() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2") {{
            addElement(new XmlElement("element3"));
            addElement(new XmlElement("element4"));
        }};
        XmlElement element3 = new XmlElement("element3");
        XmlElement element4 = new XmlElement("element4");
        root.addElement(element1);
        root.addElement(element2);
        assertEquals(element1, root.getElement("element1"));
        assertEquals(element2, root.getElement("element2"));
        assertEquals(element3, root.getElement("element2/element3"));
        assertEquals(element4, root.getElement("element2/element4"));
        assertNull(root.getElement("element5"));
    }

    @Test
    public void testAddSubElement() {
        XmlElement child = new XmlElement("child");
        root.addSubElement("child");
        assertEquals(1, root.getElements().size());
        assertEquals(child, root.getElements().get(0));
        assertSame(root, child.getParent());
    }

    @Test
    public void testAddSubElementWithPath() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        root.addSubElement("child1");
        root.addSubElement("child1/child2");
        assertEquals(2, root.getElements().size());
        assertEquals(child1, root.getElements().get(0));
        assertSame(root, child1.getParent());
        assertEquals(child2, root.getElements().get(1));
        assertSame(child1, child2.getParent());
    }

    @Test
    public void testAddSubElementWithElement() {
        XmlElement child = new XmlElement("child");
        root.addSubElement(child);
        assertEquals(1, root.getElements().size());
        assertEquals(child, root.getElements().get(0));
        assertSame(root, child.getParent());
    }

    @Test
    public void testAddSubElementWithNameAndData() {
        XmlElement child = new XmlElement("child");
        root.addSubElement("child", "someData");
        assertEquals(1, root.getElements().size());
        assertEquals(child, root.getElements().get(0));
        assertSame(root, child.getParent());
        assertEquals("someData", child.getData());
    }

    @Test
    public void testClone() {
        XmlElement original = new XmlElement("root"); {{
            addAttribute("attributeName1", "attributeValue1");
            addAttribute("attributeName2", "attributeValue2");
            addElement(new XmlElement("child1"));
            addElement(new XmlElement("child2"));
        }};
        XmlElement cloned = (XmlElement) original.clone();
        assertEquals(new HashMap<String, String>(Arrays.asList(new String[]{"attributeName1", "attributeValue1"}, new String[]{"attributeName2", "attributeValue2"})), cloned.getAttributes());
        assertEquals(2, cloned.count());
        assertEquals(original.getElements().get(0), cloned.getElements().get(0));
        assertEquals(original.getElements().get(1), cloned.getElements().get(1));
    }

    @Test
    public void testNotifyObservers() {
        XmlElement observer = new XmlElement("observer"); {{
            root.addObserver(observer);
        }};
        root.setName("newName");
        assertTrue(observer.hasChanged());
    }

    @Test
    public void testEquals() {
        XmlElement other = new XmlElement("root"); {{
            addAttribute("attributeName1", "attributeValue1");
            addAttribute("attributeName2", "attributeValue2");
            addElement(new XmlElement("child1"));
            addElement(new XmlElement("child2"));
        }};
        assertTrue(root.equals(other));
        XmlElement otherWithDifferentName = new XmlElement("otherRoot"); {{
            addAttribute("attributeName1", "attributeValue1");
            addAttribute("attributeName2", "attributeValue2");
            addElement(new XmlElement("child1"));
            addElement(new XmlElement("child2"));
        }};
        assertFalse(root.equals(otherWithDifferentName));
        XmlElement otherWithDifferentAttributes = new XmlElement("root") {{
            addAttribute("attributeName1", "differentAttributeValue1");
            addAttribute("attributeName2", "attributeValue2");
            addElement(new XmlElement("child1"));
            addElement(new XmlElement("child2"));
        }};
        assertFalse(root.equals(otherWithDifferentAttributes));
        XmlElement otherWithDifferentChildren = new XmlElement("root") {{
            addAttribute("attributeName1", "attributeValue1");
            addAttribute("attributeName2", "attributeValue2");
            addElement(new XmlElement("child1WithDifferentName"));
            addElement(new XmlElement("child2"));
        }};
        assertFalse(root.equals(otherWithDifferentChildren));
        XmlElement otherWithDifferentData = new XmlElement("root") {{
            addAttribute("attributeName1", "attributeValue1");
            addAttribute("attributeName2", "attributeValue2");
            setData("differentData");
            addElement(new XmlElement("child1"));
            addElement(new XmlElement("child2"));
        }};
        assertFalse(root.equals(otherWithDifferentData));
    }

    @Test
    public void testHashCode() {
        XmlElement other = new XmlElement("root") {{
            addAttribute("attributeName1", "attributeValue1");
            addAttribute("attributeName2", "attributeValue2");
            addElement(new XmlElement("child1"));
            addElement(new XmlElement("child2"));
        }};
        int expectedHashCode = 23;
        int rootHashCode = root.hashCode();
        int otherHashCode = other.hashCode();
        assertEquals(expectedHashCode, rootHashCode);
        assertEquals(expectedHashCode, otherHashCode);
    }
}