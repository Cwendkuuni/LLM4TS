import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
    public void testXmlElementCreation() {
        XmlElement element1 = new XmlElement();
        XmlElement element2 = new XmlElement("elementName");
        XmlElement element3 = new XmlElement("elementName", new Hashtable<String, String>());
        XmlElement element4 = new XmlElement("elementName", "data");

        assertNull(element1.getName());
        assertNull(element1.getData());
        assertNotNull(element1.getAttributes());
        assertTrue(element1.getAttributes().isEmpty());
        assertNotNull(element1.getSubElements());
        assertTrue(element1.getSubElements().isEmpty());

        assertEquals("elementName", element2.getName());
        assertNull(element2.getData());
        assertNotNull(element2.getAttributes());
        assertTrue(element2.getAttributes().isEmpty());
        assertNotNull(element2.getSubElements());
        assertTrue(element2.getSubElements().isEmpty());

        assertEquals("elementName", element3.getName());
        assertNull(element3.getData());
        assertNotNull(element3.getAttributes());
        assertTrue(element3.getAttributes().isEmpty());
        assertNotNull(element3.getSubElements());
        assertTrue(element3.getSubElements().isEmpty());

        assertEquals("elementName", element4.getName());
        assertEquals("data", element4.getData());
        assertNotNull(element4.getAttributes());
        assertTrue(element4.getAttributes().isEmpty());
        assertNotNull(element4.getSubElements());
        assertTrue(element4.getSubElements().isEmpty());
    }

    @Test
    public void testAddAttribute() {
        root.addAttribute("attributeName1", "attributeValue1");
        root.addAttribute("attributeName2", "attributeValue2");

        assertEquals("attributeValue1", root.getAttribute("attributeName1"));
        assertEquals("attributeValue2", root.getAttribute("attributeName2"));
        assertEquals("attributeValue1 attributeName1:attributeValue1 attributeName2:attributeValue2", root.toString());

        Hashtable<String, String> attributes = root.getAttributes();
        assertEquals("attributeValue1", attributes.get("attributeName1"));
        assertEquals("attributeValue2", attributes.get("attributeName2"));
    }

    @Test
    public void testGetAttribute() {
        root.addAttribute("attributeName1", "attributeValue1");

        assertEquals("attributeValue1", root.getAttribute("attributeName1"));
        assertEquals(null, root.getAttribute("unknownAttributeName"));
    }

    @Test
    public void testGetAttributes() {
        root.addAttribute("attributeName1", "attributeValue1");
        root.addAttribute("attributeName2", "attributeValue2");

        Hashtable<String, String> attributes = root.getAttributes();
        assertEquals("attributeValue1", attributes.get("attributeName1"));
        assertEquals("attributeValue2", attributes.get("attributeName2"));
        assertEquals(2, attributes.size());
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("attributeName1", "attributeValue1");
        attributes.put("attributeName2", "attributeValue2");

        root.setAttributes(attributes);

        assertEquals("attributeName1:attributeValue1 attributeName2:attributeValue2", root.toString());

        Hashtable<String, String> rootAttributes = root.getAttributes();
        assertEquals("attributeValue1", rootAttributes.get("attributeName1"));
        assertEquals("attributeValue2", rootAttributes.get("attributeName2"));
        assertEquals(2, rootAttributes.size());
    }

    @Test
    public void testAttributeNames() {
        root.addAttribute("attributeName1", "attributeValue1");
        root.addAttribute("attributeName2", "attributeValue2");

        Enumeration<String> attributeNames = root.getAttributeNames();

        String attributeName;
        while (attributeNames.hasMoreElements()) {
            attributeName = attributeNames.nextElement();
            assertEquals("attributeName1", attributeName);
            assertEquals("attributeName2", attributeName);
        }
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
    }

    @Test
    public void testRemoveElement() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");

        root.addElement(child1);
        root.addElement(child2);

        root.removeElement(child1);

        assertEquals(1, root.count());
        assertNull(root.getElement(0));
        assertEquals(child1, child1.getParent());
        assertNull(child1.getParent());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");

        parent.addElement(child);

        child.removeFromParent();

        assertEquals(0, parent.count());
        assertEquals(null, child.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");

        parent.append(child1);
        parent.append(child2);

        assertEquals(2, parent.count());
        assertEquals(child1, parent.getElement(0));
        assertEquals(child2, parent.getElement(1));
    }

    @Test
    public void testInsertElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");

        parent.addElement(child1);
        parent.insertElement(child2, 0);

        assertEquals(2, parent.count());
        assertEquals(child2, parent.getElement(0));
        assertEquals(child1, parent.getElement(1));
    }

    @Test
    public void testGetElements() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");

        parent.addElement(child1);
        parent.addElement(child2);

        List<XmlElement> elements = parent.getElements();

        assertEquals(2, elements.size());
        assertEquals(child1, elements.get(0));
        assertEquals(child2, elements.get(1));
    }

    @Test
    public void testClone() {
        XmlElement original = new XmlElement("original");
        XmlElement cloned = (XmlElement) original.clone();

        original.addAttribute("originalAttribute", "originalAttributeValue");
        original.addSubElement("originalSubElement");

        assertEquals("original", original.getName());
        assertEquals("original", cloned.getName());

        assertNotSame(original.getData(), cloned.getData());
        assertNull(original.getData());
        assertNull(cloned.getData());

        Hashtable<String, String> originalAttributes = original.getAttributes();
        Hashtable<String, String> clonedAttributes = cloned.getAttributes();

        assertEquals("originalAttribute", originalAttributes.keySet().nextElement().toString());
        assertEquals("originalAttributeValue", originalAttributes.get("originalAttribute"));
        assertEquals(1, originalAttributes.size());

        assertTrue(clonedAttributes.isEmpty());

        XmlElement originalSubElement = original.getSubElement("originalSubElement");
        XmlElement clonedSubElement = cloned.getSubElement("originalSubElement");

        assertEquals("originalSubElement", originalSubElement.getName());
        assertEquals("originalSubElement", clonedSubElement.getName());

        assertNotSame(originalSubElement, clonedSubElement);

        assertEquals(1, original.count());
        assertEquals(0, cloned.count());
    }

    @Test
    public void testToString() {
        XmlElement root = new XmlElement("root");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");

        root.addAttribute("attribute1", "value1");
        root.addAttribute("attribute2", "value2");
        root.addElement(child1);
        root.addElement(child2);
        child1.addAttribute("child1Attribute", "child1AttributeValue");
        child1.addElement(new XmlElement("grandchild1"));
        child2.addAttribute("child2Attribute", "child2AttributeValue");

        assertEquals("<root attribute1=\"value1\" attribute2=\"value2\">" +
                "<child1 attributeName=\"child1Attribute\">child1AttributeValue</child1>" +
                "<grandchild1/>" +
                "<child2 attributeName=\"child2Attribute\">child2AttributeValue</child2>" +
                "</root>", root.toString());
    }
}