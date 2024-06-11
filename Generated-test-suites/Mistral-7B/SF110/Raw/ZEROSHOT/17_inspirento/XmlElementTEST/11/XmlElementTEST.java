import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Hashtable;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlElementTest {

    private XmlElement rootElement;

    @Before
    public void setUp() {
        rootElement = new XmlElement("root");;
    }

    @After
    public void tearDown() {
        rootElement.removeAllElements();
    }

    @Test
    public void testXmlElementConstructor() {
        XmlElement element = new XmlElement();
        assertNotNull(element);
        assertEquals("", element.getData());
        assertEquals(null, element.getName());
        assertEquals(0, element.count());
        assertEquals(null, element.getParent());

        element = new XmlElement("elementName");
        assertNotNull(element);
        assertEquals("elementName", element.getName());
        assertEquals(0, element.count());
        assertEquals(null, element.getParent());
    }

    @Test
    public void testAddAttribute() {
        rootElement.addAttribute("testAttribute", "testValue");
        Hashtable<String, String> attributes = rootElement.getAttributes();
        assertEquals("testValue", attributes.get("testAttribute"));
    }

    @Test
    public void testGetAttribute() {
        rootElement.addAttribute("testAttribute", "testValue");
        assertEquals("testValue", rootElement.getAttribute("testAttribute"));
        assertEquals(null, rootElement.getAttribute("nonExistentAttribute"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        rootElement.addAttribute("testAttribute", "testValue");
        assertEquals("testValue", rootElement.getAttribute("testAttribute", "defaultValue"));
        assertEquals("defaultValue", rootElement.getAttribute("nonExistentAttribute", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        rootElement.addAttribute("testAttribute1", "testValue1");
        rootElement.addAttribute("testAttribute2", "testValue2");

        Hashtable<String, String> attributes = rootElement.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("testValue1", attributes.get("testAttribute1"));
        assertEquals("testValue2", attributes.get("testAttribute2"));
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("testAttribute1", "testValue1");
        attributes.put("testAttribute2", "testValue2");

        rootElement.setAttributes(attributes);
        assertEquals(2, rootElement.getAttributes().size());
        assertEquals("testValue1", rootElement.getAttribute("testAttribute1"));
        assertEquals("testValue2", rootElement.getAttribute("testAttribute2"));
    }

    @Test
    public void testGetAttributeNames() {
        rootElement.addAttribute("testAttribute1", "testValue1");
        rootElement.addAttribute("testAttribute2", "testValue2");

        Enumeration<String> attributeNames = rootElement.getAttributeNames();
        assertEquals(2, Iterators.size(attributeNames));
        assertEquals("testAttribute1", attributeNames.nextElement());
        assertEquals("testAttribute2", attributeNames.nextElement());
    }

    @Test
    public void testAddElement() {
        XmlElement childElement = new XmlElement("childElement");
        rootElement.addElement(childElement);
        assertEquals(1, rootElement.count());
        assertEquals(childElement, rootElement.getElement(0));
    }

    @Test
    public void testRemoveElement() {
        XmlElement childElement = new XmlElement("childElement");
        rootElement.addElement(childElement);
        rootElement.removeElement(childElement);
        assertEquals(0, rootElement.count());
        assertNull(rootElement.getElement(0));
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement childElement = new XmlElement("childElement");
        rootElement.addElement(childElement);
        childElement.removeFromParent();
        assertEquals(0, rootElement.count());
        assertNull(rootElement.getElement(0));
    }

    @Test
    public void testAddSubElement() {
        XmlElement childElement = new XmlElement("childElement");
        rootElement.addSubElement(childElement);
        assertEquals(1, rootElement.count());
        assertEquals(childElement, rootElement.getElement(0));
    }

    @Test
    public void testAddSubElementWithPath() {
        XmlElement parentElement = new XmlElement("parentElement");
        rootElement.addSubElement(parentElement);
        XmlElement childElement = rootElement.addSubElement("childElement", "childData");
        assertEquals(2, rootElement.count());
        assertEquals(parentElement, rootElement.getElement(0));
        assertEquals(childElement, rootElement.getElement(1));
    }

    @Test
    public void testGetElement() {
        XmlElement parentElement = new XmlElement("parentElement");
        rootElement.addElement(parentElement);
        XmlElement childElement = new XmlElement("childElement");
        parentElement.addElement(childElement);

        assertEquals(childElement, rootElement.getElement("parentElement/childElement"));
        assertEquals(null, rootElement.getElement("nonExistentElement"));
    }

    @Test
    public void testClone() {
        XmlElement originalElement = new XmlElement("originalElement");
        originalElement.addAttribute("testAttribute", "testValue");
        originalElement.addElement(new XmlElement("childElement"));

        XmlElement clonedElement = (XmlElement) originalElement.clone();

        assertEquals(originalElement.getName(), clonedElement.getName());
        assertEquals(originalElement.getData(), clonedElement.getData());
        assertEquals(originalElement.getAttributes().size(), clonedElement.getAttributes().size());
        assertEquals(originalElement.count(), clonedElement.count());

        for (int i = 0; i < originalElement.count(); i++) {
            XmlElement originalChild = originalElement.getElement(i);
            XmlElement clonedChild = clonedElement.getElement(i);

            assertEquals(originalChild.getName(), clonedChild.getName());
            assertEquals(originalChild.getData(), clonedChild.getData());
            assertEquals(originalChild.getAttributes().size(), clonedChild.getAttributes().size());
            assertEquals(originalChild.count(), clonedChild.count());
        }
    }

}