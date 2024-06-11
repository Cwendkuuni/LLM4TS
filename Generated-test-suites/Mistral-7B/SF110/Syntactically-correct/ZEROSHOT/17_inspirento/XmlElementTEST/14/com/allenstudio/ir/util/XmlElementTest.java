import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlElementTest {

    private XmlElement rootElement;

    @Before
    public void setUp() {
        rootElement = new XmlElement("root");;
    }

    @Test
    public void testXmlElementConstruction() {
        XmlElement element = new XmlElement();
        assertEquals("", element.getData());
        assertEquals(null, element.getName());
        assertEquals(null, element.getParent());
        assertTrue(element.getSubElements().isEmpty());
        assertTrue(element.getAttributes().isEmpty());

        element = new XmlElement("elementName");
        assertEquals("elementName", element.getName());
        assertEquals(null, element.getData());
        assertEquals(null, element.getParent());
        assertTrue(element.getSubElements().isEmpty());
        assertTrue(element.getAttributes().isEmpty());

        Map<String, String> attributes = new HashMap<>();
        attributes.put("attribute1", "value1");
        attributes.put("attribute2", "value2");
        element = new XmlElement("elementName", attributes);
        assertEquals("elementName", element.getName());
        assertEquals(null, element.getData());
        assertEquals(null, element.getParent());
        assertEquals(attributes, element.getAttributes());
        assertEquals(0, element.getSubElements().size());

        assertEquals("value1", element.getAttribute("attribute1"));
        assertEquals("value2", element.getAttribute("attribute2"));
        assertNull(element.getAttribute("unknownAttribute"));

        element.setData("some data");
        assertEquals("some data", element.getData());

        element.setName("newName");
        assertEquals("newName", element.getName());
    }

    @Test
    public void testHashtableMethods() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("attribute1", "value1");
        attributes.put("attribute2", "value2");

        XmlElement element = new XmlElement("elementName", attributes);

        assertEquals(2, element.getAttributes().size());
        assertEquals("value1", element.getAttribute("attribute1"));
        assertEquals("value2", element.getAttribute("attribute2"));
        assertEquals(null, element.getAttribute("unknownAttribute"));

        element.addAttribute("attribute3", "value3");
        assertEquals(3, element.getAttributes().size());
        assertEquals("value3", element.getAttribute("attribute3"));

        assertEquals(null, element.getAttributes().get("unknownAttribute"));
        assertNull(element.getAttributes().remove("unknownAttribute"));

        assertEquals("value1", element.getAttribute("attribute1", "defaultValue"));
        assertEquals("value1", element.getAttribute("attribute1"));

        assertTrue(element.getAttributes().containsKey("attribute1"));
        assertTrue(element.getAttributes().containsValue("value1"));

        assertFalse(element.getAttributes().containsValue("defaultValue"));
        assertFalse(element.getAttributes().containsKey("unknownAttribute"));

        List<Map.Entry<String, String>> entries = new ArrayList<>(element.getAttributes().entrySet());
        assertEquals("value1", entries.get(0).getValue());
        assertEquals("attribute1", entries.get(0).getKey());
        assertEquals("value2", entries.get(1).getValue());
        assertEquals("attribute2", entries.get(1).getKey());
        assertEquals("value3", entries.get(2).getValue());
        assertEquals("attribute3", entries.get(2).getKey());
    }

    @Test
    public void testListMethods() {
        XmlElement parentElement = rootElement;

        XmlElement childElement1 = new XmlElement("childElement1");
        parentElement.addElement(childElement1);
        assertEquals(1, parentElement.getSubElements().size());
        assertSame(childElement1, parentElement.getSubElements().get(0));

        XmlElement childElement2 = new XmlElement("childElement2");
        parentElement.addElement(childElement2);
        assertEquals(2, parentElement.getSubElements().size());
        assertSame(childElement1, parentElement.getSubElements().get(0));
        assertSame(childElement2, parentElement.getSubElements().get(1));

        XmlElement childElement3 = new XmlElement("childElement3");
        parentElement.insertElement(childElement3, 0);
        assertEquals(3, parentElement.getSubElements().size());
        assertSame(childElement3, parentElement.getSubElements().get(0));
        assertSame(childElement1, parentElement.getSubElements().get(1));
        assertSame(childElement2, parentElement.getSubElements().get(2));

        XmlElement childElement4 = new XmlElement("childElement4");
        parentElement.append(childElement4);
        assertEquals(4, parentElement.getSubElements().size());
        assertSame(childElement1, parentElement.getSubElements().get(0));
        assertSame(childElement2, parentElement.getSubElements().get(1));
        assertSame(childElement3, parentElement.getSubElements().get(2));
        assertSame(childElement4, parentElement.getSubElements().get(3));

        XmlElement childElementToRemove = childElement1;
        parentElement.removeElement(childElementToRemove);
        assertEquals(3, parentElement.getSubElements().size());
        assertFalse(parentElement.getSubElements().contains(childElementToRemove));

        parentElement.removeAllElements();
        assertTrue(parentElement.getSubElements().isEmpty());

        XmlElement grandChildElement = new XmlElement("grandChildElement");
        childElement1.addElement(grandChildElement);
        assertEquals(1, childElement1.getSubElements().size());
        assertSame(grandChildElement, childElement1.getSubElements().get(0));
    }

    @Test
    public void testGetElementMethod() {
        XmlElement parentElement = rootElement;

        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);

        XmlElement elementToFind = new XmlElement("childElement1/grandChildElement");
        XmlElement foundElement = parentElement.getElement(elementToFind.getPath());
        assertSame(childElement1, foundElement);

        elementToFind = new XmlElement("childElement2/grandChildElement");
        foundElement = parentElement.getElement(elementToFind.getPath());
        assertNull(foundElement);

        childElement1.addElement(new XmlElement("grandChildElement"));
        foundElement = parentElement.getElement(elementToFind.getPath());
        assertSame(childElement1.getSubElements().get(0), foundElement);

        elementToFind = new XmlElement("nonexistentElement");
        foundElement = parentElement.getElement(elementToFind.getPath());
        assertNull(foundElement);
    }

    @Test
    public void testAddSubElementMethod() {
        XmlElement parentElement = rootElement;

        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);

        XmlElement newElement1 = new XmlElement("newElement1");
        XmlElement newElement2 = new XmlElement("newElement2");
        XmlElement newElement3 = new XmlElement("newElement3");

        parentElement.addSubElement("childElement1/newElement1");
        assertEquals(1, childElement1.getSubElements().size());
        assertSame(newElement1, childElement1.getSubElements().get(0));

        parentElement.addSubElement("childElement2/newElement2");
        assertEquals(1, childElement2.getSubElements().size());
        assertSame(newElement2, childElement2.getSubElements().get(0));

        parentElement.addSubElement("childElement1/newElement3/grandChildElement");
        assertEquals(1, childElement1.getSubElements().size());
        assertEquals(1, ((XmlElement)childElement1.getSubElements().get(0)).getSubElements().size());
        assertSame(newElement3.getSubElements().get(0), ((XmlElement)childElement1.getSubElements().get(0)).getSubElements().get(0));

        parentElement.addSubElement(newElement3);
        assertEquals(1, parentElement.getSubElements().size());
        assertSame(newElement3, parentElement.getSubElements().get(0));

        parentElement.addSubElement("childElement1/newElement4");
        assertEquals(1, childElement1.getSubElements().size());
        assertTrue(childElement1.getSubElements().contains(newElement4));
        assertNull(newElement4); // newElement4 is not instantiated
    }

    @Test
    public void testCloneMethod() {
        XmlElement originalElement = new XmlElement("originalElement");
        XmlElement clonedElement = (XmlElement)originalElement.clone();

        assertEquals(originalElement.getData(), clonedElement.getData());
        assertEquals(originalElement.getName(), clonedElement.getName());
        assertEquals(originalElement.getAttributes(), clonedElement.getAttributes());
        assertEquals(originalElement.getSubElements(), clonedElement.getSubElements());

        originalElement.addAttribute("newAttribute", "newValue");
        assertFalse(clonedElement.hasAttribute("newAttribute"));

        originalElement.removeAttribute("attribute1");
        assertFalse(clonedElement.hasAttribute("attribute1"));

        originalElement.removeFromParent();
        assertNull(originalElement.getParent());
        assertNull(clonedElement.getParent());

        List<XmlElement> originalSubElements = originalElement.getSubElements();
        List<XmlElement> clonedSubElements = clonedElement.getSubElements();

        originalElement.setParent(new XmlElement("parentElement"));
        clonedElement.setParent(new XmlElement("parentElement"));

        assertEquals(originalSubElements.size(), clonedSubElements.size());
        for (int i = 0; i < originalSubElements.size(); i++) {
            XmlElement originalSubElement = originalSubElements.get(i);
            XmlElement clonedSubElement = clonedSubElements.get(i);
            assertEquals(originalSubElement, clonedSubElement);
        }
    }

    @After
    public void tearDown() {
        rootElement = null;
    }
}