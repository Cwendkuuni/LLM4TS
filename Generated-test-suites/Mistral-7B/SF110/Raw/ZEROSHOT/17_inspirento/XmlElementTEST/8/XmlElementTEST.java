import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Hashtable;
import java.util.Vector;

import static org.junit.Assert.*;

public class XmlElementTest {

    private XmlElement xmlElement;

    @Before
    public void setUp() {
        xmlElement = new XmlElement("root");
    }

    @Test
    public void testXmlElementConstructor() {
        XmlElement xmlElement = new XmlElement();
        assertNotNull(xmlElement);
        xmlElement = new XmlElement("elementName");
        assertNotNull(xmlElement);
        xmlElement = new XmlElement("elementName", new Hashtable<String, String>());
        assertNotNull(xmlElement);
        xmlElement = new XmlElement("elementName", "data");
        assertNotNull(xmlElement);
    }

    @Test
    public void testAddAttribute() {
        xmlElement.addAttribute("attributeName", "attributeValue");
        assertEquals("attributeValue", xmlElement.getAttribute("attributeName"));
    }

    @Test
    public void testGetAttribute() {
        xmlElement.addAttribute("attributeName", "attributeValue");
        assertEquals("attributeValue", xmlElement.getAttribute("attributeName"));
        assertEquals(null, xmlElement.getAttribute("nonExistentAttribute"));
    }

    @Test
    public void testGetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("attributeName1", "attributeValue1");
        attributes.put("attributeName2", "attributeValue2");
        xmlElement.setAttributes(attributes);

        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("attributeName1", "attributeValue1");
        attributes.put("attributeName2", "attributeValue2");
        xmlElement.setAttributes(attributes);

        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testAttributeNames() {
        xmlElement.addAttribute("attributeName1", "attributeValue1");
        xmlElement.addAttribute("attributeName2", "attributeValue2");

        assertNotNull(xmlElement.getAttributeNames());
    }

    @Test
    public void testAddElement() {
        XmlElement childElement = new XmlElement("childElement");
        xmlElement.addElement(childElement);
        assertEquals(childElement, xmlElement.getElements().get(0));
    }

    @Test
    public void testRemoveElement() {
        XmlElement childElement = new XmlElement("childElement");
        xmlElement.addElement(childElement);
        xmlElement.removeElement(childElement);
        assertFalse(xmlElement.getElements().contains(childElement));
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");
        xmlElement.addElement(childElement1);
        xmlElement.addElement(childElement2);
        xmlElement.removeAllElements();
        assertTrue(xmlElement.getElements().isEmpty());
    }

    @Test
    public void testGetElement() {
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");
        xmlElement.addElement(childElement1);
        xmlElement.addElement(childElement2);

        assertEquals(childElement1, xmlElement.getElement("childElement1"));
        assertEquals(childElement2, xmlElement.getElement("childElement2"));
        assertEquals(null, xmlElement.getElement("nonExistentElement"));
    }

    @Test
    public void testGetElement_String_int() {
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");
        xmlElement.addElement(childElement1);
        xmlElement.addElement(childElement2);

        assertEquals(childElement1, xmlElement.getElement("childElement1"));
        assertEquals(childElement2, xmlElement.getElement("childElement2"));
        assertEquals(null, xmlElement.getElement("", 1));
    }

    @Test
    public void testAddSubElement() {
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");
        xmlElement.addSubElement("childElement3");
        xmlElement.addSubElement("childElement4/childElement5", childElement1);
        xmlElement.addSubElement("childElement4/childElement5", childElement2);

        assertEquals(childElement1, xmlElement.getElements().get(3));
        assertEquals(childElement2, xmlElement.getElements().get(4));
    }

    @Test
    public void testClone() {
        XmlElement xmlElement1 = new XmlElement("root");
        xmlElement1.addAttribute("attributeName1", "attributeValue1");
        xmlElement1.addAttribute("attributeName2", "attributeValue2");
        xmlElement1.addElement(new XmlElement("childElement1"));
        xmlElement1.addElement(new XmlElement("childElement2"));

        XmlElement xmlElement2 = (XmlElement) xmlElement1.clone();

        assertEquals(xmlElement1, xmlElement2);
        assertNotSame(xmlElement1, xmlElement2);

        xmlElement1.addAttribute("newAttributeName", "newAttributeValue");
        xmlElement1.getElements().get(0).addAttribute("newChildAttributeName", "newChildAttributeValue");

        assertNotEquals(xmlElement1, xmlElement2);
        assertEquals(xmlElement1.getAttributes(), xmlElement2.getAttributes());
        assertEquals(xmlElement1.getElements(), xmlElement2.getElements());
    }

    @After
    public void tearDown() {
        xmlElement = null;
    }
}