import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class XmlElementTest {

    private XmlElement xmlElement;
    private String name;
    private String data;
    private Map<String, String> attributes;
    private List<XmlElement> subElements;
    private XmlElement parent;

    public XmlElementTest(String name, String data, Map<String, String> attributes, List<XmlElement> subElements) {
        this.name = name;
        this.data = data;
        this.attributes = attributes;
        this.subElements = subElements;
    }

    @Before
    public void setUp() {
        xmlElement = new XmlElement();
    }

    @Test
    public void testEmptyConstructor() {
        assertEquals(new Vector<>(), xmlElement.getSubElements());
        assertEquals(new Hashtable<>(10), xmlElement.getAttributes());
    }

    @Test
    public void testXmlElementConstructor() {
        xmlElement = new XmlElement(name);
        assertEquals(name, xmlElement.getName());
        assertEquals(new Hashtable<>(10), xmlElement.getAttributes());
        assertEquals(new Vector<>(), xmlElement.getSubElements());
    }

    @Test
    public void testXmlElementConstructorWithAttributes() {
        xmlElement = new XmlElement(name, attributes);
        assertEquals(name, xmlElement.getName());
        assertEquals(attributes, xmlElement.getAttributes());
        assertEquals(new Vector<>(), xmlElement.getSubElements());
    }

    @Test
    public void testXmlElementConstructorWithData() {
        xmlElement = new XmlElement(name, data);
        assertEquals(name, xmlElement.getName());
        assertEquals(new Hashtable<>(10), xmlElement.getAttributes());
        assertEquals(new Vector<>(), xmlElement.getSubElements());
        assertEquals(data, xmlElement.getData());
    }

    @Test
    public void testAddAttribute() {
        xmlElement.addAttribute("test", "testData");
        assertEquals("testData", xmlElement.getAttribute("test"));
    }

    @Test(expected = NullPointerException.class)
    public void testAddAttributeNullName() {
        xmlElement.addAttribute(null, "testData");
    }

    @Test(expected = NullPointerException.class)
    public void testAddAttributeNullValue() {
        xmlElement.addAttribute("test", null);
    }

    @Test
    public void testGetAttribute() {
        xmlElement.addAttribute("test", "testData");
        assertEquals("testData", xmlElement.getAttribute("test"));
        assertEquals(null, xmlElement.getAttribute("nonexistent"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        xmlElement.addAttribute("test", null);
        assertEquals("defaultValue", xmlElement.getAttribute("test", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        xmlElement.addAttribute("test1", "testData1");
        xmlElement.addAttribute("test2", "testData2");
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        Map<String, String> newAttributes = new HashMap<>(attributes);
        newAttributes.put("test", "testData");
        xmlElement.setAttributes(newAttributes);
        assertEquals(newAttributes, xmlElement.getAttributes());
    }

    @Test
    public void testgetAttributeNames() {
        xmlElement.addAttribute("test1", "testData1");
        xmlElement.addAttribute("test2", "testData2");
        Set<String> keys = xmlElement.getAttributeNames();
        assertTrue(keys.containsAll(attributes.keySet()));
    }

    @Test
    public void testAddElement() {
        XmlElement subElement = new XmlElement("subElement");
        xmlElement.addElement(subElement);
        assertTrue(xmlElement.getSubElements().contains(subElement));
    }

    @Test(expected = NullPointerException.class)
    public void testAddElementNullElement() {
        xmlElement.addElement(null);
    }

    @Test
    public void testRemoveElement() {
        XmlElement subElement = new XmlElement("subElement");
        xmlElement.addElement(subElement);
        xmlElement.removeElement(subElement);
        assertFalse(xmlElement.getSubElements().contains(subElement));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementInvalidIndex() {
        xmlElement.removeElement(100);
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement subElement1 = new XmlElement("subElement1");
        XmlElement subElement2 = new XmlElement("subElement2");
        xmlElement.addElement(subElement1);
        xmlElement.addElement(subElement2);
        xmlElement.removeAllElements();
        assertTrue(xmlElement.getSubElements().isEmpty());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parentMock = Mockito.mock(XmlElement.class);
        XmlElement child = new XmlElement("child");
        child.setParent(parentMock);
        xmlElement.addElement(child);
        xmlElement.removeFromParent();
        assertEquals(null, child.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement subElement = new XmlElement("subElement");
        xmlElement.append(subElement);
        assertTrue(xmlElement.getSubElements().contains(subElement));
    }

    @Test
    public void testInsertElement() {
        XmlElement subElement = new XmlElement("subElement");
        xmlElement.insertElement(subElement, 0);
        assertTrue(xmlElement.getSubElements().get(0) == subElement);
    }

    @Test
    public void testGetElements() {
        XmlElement subElement1 = new XmlElement("subElement1");
        XmlElement subElement2 = new XmlElement("subElement2");
        xmlElement.addElement(subElement1);
        xmlElement.addElement(subElement2);
        List<XmlElement> elements = xmlElement.getElements();
        assertTrue(elements.containsAll(subElements));
    }

    @Test
    public void testCount() {
        XmlElement subElement1 = new XmlElement("subElement1");
        XmlElement subElement2 = new XmlElement("subElement2");
        xmlElement.addElement(subElement1);
        xmlElement.addElement(subElement2);
        assertEquals(subElements.size(), xmlElement.count());
    }

    @Test
    public void testgetElement() {
        XmlElement subElement = new XmlElement("subElement");
        xmlElement.addElement(subElement);
        XmlElement result = xmlElement.getElement("subElement");
        assertEquals(subElement, result);
    }

    @Test
    public void testgetElementNullPath() {
        XmlElement result = xmlElement.getElement(null);
        assertNull(result);
    }

    @Test
    public void testgetElementEmptyPath() {
        XmlElement result = xmlElement.getElement("");
        assertNull(result);
    }

    @Test
    public void testgetElementInvalidPath() {
        XmlElement result = xmlElement.getElement("invalidPath");
        assertNull(result);
    }

    @Test
    public void testgetElementWithSubElements() {
        XmlElement subElement1 = new XmlElement("subElement1");
        xmlElement.addElement(subElement1);
        XmlElement subElement2 = new XmlElement("subElement2");
        subElement1.addElement(subElement2);
        XmlElement result = xmlElement.getElement("subElement1/subElement2");
        assertEquals(subElement2, result);
    }

    @Test
    public void testGetElementInvalidPathWithSlashAtBeginning() {
        XmlElement result = xmlElement.getElement("/subElement");
        assertNull(result);
    }

    @Test
    public void testGetElementInvalidPathWithSlashAtEnd() {
        XmlElement result = xmlElement.getElement("subElement/");
        assertNull(result);
    }

    @Test
    public void testAddSubElement() {
        XmlElement subElement = new XmlElement("subElement");
        xmlElement.addSubElement("subElement");
        assertTrue(xmlElement.getSubElements().contains(subElement));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSubElementWithInvalidPath() {
        xmlElement.addSubElement("invalidPath");
    }

    @Test
    public void testAddSubElementWithSubElements() {
        XmlElement subElement1 = new XmlElement("subElement1");
        xmlElement.addSubElement("subElement1");
        XmlElement subElement2 = new XmlElement("subElement2");
        subElement1.addElement(subElement2);
        XmlElement result = xmlElement.getElement("subElement1/subElement2");
        assertEquals(subElement2, result);
    }

    @Test
    public void testClone() {
        xmlElement.addAttribute("test1", "testData1");
        xmlElement.addAttribute("test2", "testData2");
        xmlElement.addElement(new XmlElement("subElement"));
        XmlElement clonedXmlElement = (XmlElement) xmlElement.clone();
        assertEquals(xmlElement.getName(), clonedXmlElement.getName());
        assertEquals(xmlElement.getData(), clonedXmlElement.getData());
        assertEquals(xmlElement.getAttributes(), clonedXmlElement.getAttributes());
        assertEquals(xmlElement.getSubElements(), clonedXmlElement.getSubElements());
    }

    @Test
    public void testEquals() {
        XmlElement otherXmlElement = new XmlElement();
        assertFalse(xmlElement.equals(otherXmlElement));

        XmlElement otherXmlElementWithSameAttributesAndData = new XmlElement();
        otherXmlElementWithSameAttributesAndData.setName(name);
        otherXmlElementWithSameAttributesAndData.setData(data);
        otherXmlElementWithSameAttributesAndData.setAttributes(attributes);
        otherXmlElementWithSameAttributesAndData.setSubElements(subElements);
        assertTrue(xmlElement.equals(otherXmlElementWithSameAttributesAndData));

        XmlElement otherXmlElementWithSameNameAndAttributesButDifferentData = new XmlElement(name);
        otherXmlElementWithSameNameAndAttributesButDifferentData.setData("differentData");
        otherXmlElementWithSameNameAndAttributesButDifferentData.setAttributes(attributes);
        assertFalse(xmlElement.equals(otherXmlElementWithSameNameAndAttributesButDifferentData));

        XmlElement otherXmlElementWithSameNameAndDataButDifferentAttributes = new XmlElement(name, data);
        otherXmlElementWithSameNameAndDataButDifferentAttributes.setAttributes(new HashMap<>(attributes));
        otherXmlElementWithSameNameAndDataButDifferentAttributes.getAttributes().put("differentAttribute", "differentValue");
        assertFalse(xmlElement.equals(otherXmlElementWithSameNameAndDataButDifferentAttributes));

        XmlElement otherXmlElementWithSameNameAndAttributesAndSubElementsButDifferentData = new XmlElement(name, data);
        otherXmlElementWithSameNameAndAttributesAndSubElementsButDifferentData.setAttributes(attributes);
        otherXmlElementWithSameNameAndAttributesAndSubElementsButDifferentData.setSubElements(subElements);
        otherXmlElementWithSameNameAndAttributesAndSubElementsButDifferentData.setData("differentData");
        assertFalse(xmlElement.equals(otherXmlElementWithSameNameAndAttributesAndSubElementsButDifferentData));

        XmlElement otherXmlElementWithSameNameAndAttributesAndSubElementsButDifferentSubElements = new XmlElement(name, data);
        otherXmlElementWithSameNameAndAttributesAndSubElementsButDifferentSubElements.setAttributes(attributes);
        otherXmlElementWithSameNameAndAttributesAndSubElementsButDifferentSubElements.setSubElements(new ArrayList<>());
        otherXmlElementWithSameNameAndAttributesAndSubElementsButDifferentSubElements.getSubElements().add(new XmlElement("differentSubElement"));
        assertFalse(xmlElement.equals(otherXmlElementWithSameNameAndAttributesAndSubElementsButDifferentSubElements));
    }

    @Test
    public void testHashCode() {
        XmlElement otherXmlElement = new XmlElement();
        int xmlElementHashCode = xmlElement.hashCode();
        int otherXmlElementHashCode = otherXmlElement.hashCode();
        assertNotEquals(xmlElementHashCode, otherXmlElementHashCode);

        XmlElement otherXmlElementWithSameAttributesAndData = new XmlElement();
        otherXmlElementWithSameAttributesAndData.setName(name);
        otherXmlElementWithSameAttributesAndData.setData(data);
        otherXmlElementWithSameAttributesAndData.setAttributes(attributes);
        otherXmlElementWithSameAttributesAndData.setSubElements(subElements);
        int otherXmlElementWithSameAttributesAndDataHashCode = otherXmlElementWithSameAttributesAndData.hashCode();
        assertEquals(xmlElementHashCode, otherXmlElementWithSameAttributesAndDataHashCode);
    }

    @After
    public void tearDown() {
        xmlElement = null;
    }
}