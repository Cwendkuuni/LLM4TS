import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

@RunWith(Parameterized.class)
public class XmlElementTest {

    private XmlElement xmlElement;
    private String name;
    private String data;
    private List<String> attributes;
    private List<XmlElement> subElements;
    private XmlElement parent;

    @Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"", "", null, null, null, null},
                {"element1", null, null, null, null, null},
                {"element1", "data1", null, null, null, null},
                {"element1", null, null, new Hashtable<>(), null, null},
                {"element1", null, null, new Hashtable<>(), new ArrayList<>(), null},
                {"element1", "data1", null, new Hashtable<>(), new ArrayList<>(), null},
                {"element1", "data1", null, new Hashtable<>(), new ArrayList<>(), new XmlElement("element2")},
                {"element1", "data1", null, new Hashtable<>(), new ArrayList<>(), new XmlElement("element2", "data2")},
                {"element1", "data1", null, new Hashtable<>(), new ArrayList<>(), new XmlElement("element2", "data2", new Hashtable<>(), null, new ArrayList<>())},
                {"element1", "data1", null, new Hashtable<>(), new ArrayList<>(), new XmlElement("element2", "data2", new Hashtable<>(), new XmlElement("element3"), null)},
                {"element1", "data1", null, new Hashtable<>(), new ArrayList<>(), new XmlElement("element2", "data2", new Hashtable<>(), new XmlElement("element3", "data3"), null)}
        });
    }

    public XmlElementTest(String name, String data, List<String> attributes, List<XmlElement> subElements, XmlElement parent) {
        this.name = name;
        this.data = data;
        this.attributes = attributes == null ? new ArrayList<>() : Collections.unmodifiableList(attributes);
        this.subElements = subElements == null ? new ArrayList<>() : Collections.unmodifiableList(subElements);
        this.parent = parent;
        this.xmlElement = new XmlElement(name, data);
        if (attributes != null) {
            for (String attribute : attributes) {
                xmlElement.addAttribute(attribute, attribute);
            }
        }
        if (subElements != null) {
            for (XmlElement subElement : subElements) {
                xmlElement.addElement(subElement);
            }
        }
        if (parent != null) {
            xmlElement.setParent(parent);
        }
    }

    @Test
    public void testConstructor() {
        Assert.assertEquals(name, xmlElement.getName());
        Assert.assertEquals(data, xmlElement.getData());
        Assert.assertTrue(attributes.isEmpty() || attributes.equals(xmlElement.getAttributes()));
        Assert.assertTrue(subElements.isEmpty() || subElements.equals(xmlElement.getElements()));
        Assert.assertNull(xmlElement.getParent());
    }

    @Test
    public void testAddAttribute() {
        String attributeName = "attribute1";
        String attributeValue = "value1";
        xmlElement.addAttribute(attributeName, attributeValue);
        Assert.assertEquals(attributeValue, xmlElement.getAttribute(attributeName));
        Assert.assertEquals(attributeValue, xmlElement.getAttribute(attributeName, null));
        Assert.assertEquals(attributes.size() + 1, xmlElement.getAttributes().size());
        Assert.assertTrue(attributes.contains(attributeName));
        Assert.assertTrue(xmlElement.getAttributes().containsKey(attributeName));
    }

    @Test(expected = NullPointerException.class)
    public void testAddAttributeNullName() {
        xmlElement.addAttribute(null, "value1");
    }

    @Test(expected = NullPointerException.class)
    public void testAddAttributeNullValue() {
        xmlElement.addAttribute("name1", null);
    }

    @Test
    public void testGetAttribute() {
        Assert.assertNull(xmlElement.getAttribute(null));
        Assert.assertNull(xmlElement.getAttribute("attribute1", null));
        Assert.assertEquals(attributeValue, xmlElement.getAttribute(attributeName));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        Assert.assertEquals("defaultValue", xmlElement.getAttribute("attribute1", "defaultValue"));
        Assert.assertFalse(xmlElement.hasAttribute("attribute1"));
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attribute1", "value1");
        xmlElement.setAttributes(attributes);
        Assert.assertEquals(1, xmlElement.getAttributes().size());
        Assert.assertTrue(xmlElement.hasAttribute("attribute1"));
        Assert.assertEquals("value1", xmlElement.getAttribute("attribute1"));
    }

    @Test
    public void testGetAttributeNames() {
        if (attributes.isEmpty()) {
            Assert.assertTrue(xmlElement.getAttributeNames().hasMoreElements());
        } else {
            Assert.assertTrue(xmlElement.getAttributeNames().nextElement().toString().equals(attributeName));
            Assert.assertEquals(attributes.size(), xmlElement.getAttributeNames().size());
        }
    }

    @Test
    public void testAddElement() {
        XmlElement elementToAdd = new XmlElement("elementToAdd");
        xmlElement.addElement(elementToAdd);
        Assert.assertEquals(xmlElement.getElements().size(), 1 + xmlElement.getElements().size());
        Assert.assertEquals(elementToAdd, xmlElement.getElements().get(xmlElement.getElements().size() - 1));
    }

    @Test(expected = NullPointerException.class)
    public void testAddElementNullElement() {
        xmlElement.addElement(null);
    }

    @Test
    public void testRemoveElement() {
        XmlElement elementToRemove = new XmlElement("elementToRemove");
        xmlElement.addElement(elementToRemove);
        xmlElement.removeElement(elementToRemove);
        Assert.assertEquals(xmlElement.getElements().size(), xmlElement.getElements().size() - 1);
        Assert.assertFalse(xmlElement.getElements().contains(elementToRemove));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementIndexOutOfBounds() {
        xmlElement.removeElement(xmlElement.getElements().size());
    }

    @Test
    public void testRemoveAllElements() {
        xmlElement.removeAllElements();
        Assert.assertEquals(0, xmlElement.getElements().size());
    }

    @Test
    public void testRemoveFromParent() {
        if (parent != null) {
            parent.removeElement(xmlElement);
            Assert.assertNull(xmlElement.getParent());
        }
    }

    @Test
    public void testAppend() {
        XmlElement elementToAppend = new XmlElement("elementToAppend");
        xmlElement.append(elementToAppend);
        Assert.assertEquals(xmlElement.getElements().size(), 1 + xmlElement.getElements().size());
        Assert.assertEquals(elementToAppend, xmlElement.getElements().get(xmlElement.getElements().size() - 1));
    }

    @Test(expected = NullPointerException.class)
    public void testAppendNullElement() {
        xmlElement.append(null);
    }

    @Test
    public void testInsertElement() {
        XmlElement elementToInsert = new XmlElement("elementToInsert");
        xmlElement.insertElement(elementToInsert, 0);
        Assert.assertEquals(xmlElement.getElements().size(), 1 + xmlElement.getElements().size());
        Assert.assertEquals(elementToInsert, xmlElement.getElements().get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInsertElementIndexOutOfBounds() {
        xmlElement.insertElement(new XmlElement("elementToInsert"), xmlElement.getElements().size());
    }

    @Test
    public void testGetElement() {
        XmlElement elementToFind = new XmlElement("elementToFind");
        xmlElement.addElement(elementToFind);
        Assert.assertEquals(elementToFind, xmlElement.getElement("elementToFind"));
        Assert.assertEquals(elementToFind, xmlElement.getElement("/element1/elementToFind"));
        Assert.assertNull(xmlElement.getElement("nonExistentElement"));
        Assert.assertNull(xmlElement.getElement("/element1/nonExistentElement"));
    }

    @Test
    public void testGetElementIndex() {
        XmlElement elementToFind = new XmlElement("elementToFind");
        xmlElement.addElement(elementToFind);
        Assert.assertEquals(elementToFind, xmlElement.getElement(0));
        Assert.assertEquals(elementToFind, xmlElement.getElement("element1/elementToFind"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSubElementNullName() {
        xmlElement.addSubElement(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSubElementEmptyPath() {
        xmlElement.addSubElement("");
    }

    @Test
    public void testAddSubElementPath() {
        XmlElement elementToAdd = new XmlElement("elementToAdd");
        xmlElement.addSubElement("element1/elementToAdd");
        Assert.assertEquals(xmlElement.getElements().size(), 1 + xmlElement.getElements().size());
        Assert.assertEquals(elementToAdd, xmlElement.getElements().get(xmlElement.getElements().size() - 1));
    }

    @Test
    public void testAddSubElementExistingName() {
        XmlElement elementToAdd = new XmlElement("elementToAdd");
        xmlElement.addElement(new XmlElement("element1"));
        xmlElement.addSubElement("element1/elementToAdd");
        Assert.assertEquals(xmlElement.getElements().size(), 2 + xmlElement.getElements().size());
        Assert.assertEquals(elementToAdd, xmlElement.getElements().get(xmlElement.getElements().size() - 1));
    }

    @Test
    public void testSetParent() {
        XmlElement parentElement = new XmlElement("parentElement");
        xmlElement.setParent(parentElement);
        Assert.assertEquals(parentElement, xmlElement.getParent());
    }

    @Test
    public void testClone() {
        XmlElement clonedElement = (XmlElement) xmlElement.clone();
        Assert.assertEquals(name, clonedElement.getName());
        Assert.assertEquals(data, clonedElement.getData());
        Assert.assertEquals(attributes.size(), clonedElement.getAttributes().size());
        Assert.assertEquals(subElements.size(), clonedElement.getElements().size());
        Assert.assertFalse(attributes.isEqual(clonedElement.getAttributes()));
        Assert.assertFalse(subElements.equals(clonedElement.getElements()));
        Assert.assertNotSame(xmlElement, clonedElement);
    }

    @Test
    public void testEquals() {
        XmlElement otherXmlElement = new XmlElement(name, data, attributes, subElements, parent);
        Assert.assertEquals(xmlElement, otherXmlElement);
        otherXmlElement.setName("otherName");
        Assert.assertNotEquals(xmlElement, otherXmlElement);
        otherXmlElement.setName(name);
        otherXmlElement.setData("otherData");
        Assert.assertNotEquals(xmlElement, otherXmlElement);
        otherXmlElement.setData(data);
        otherXmlElement.setAttributes(attributes);
        otherXmlElement.setSubElements(subElements);
        otherXmlElement.setParent(parent);
        Assert.assertEquals(xmlElement, otherXmlElement);
    }

    @Test
    public void testHashCode() {
        XmlElement otherXmlElement = new XmlElement(name, data, attributes, subElements, parent);
        Assert.assertEquals(xmlElement.hashCode(), otherXmlElement.hashCode());
        otherXmlElement.setName("otherName");
        Assert.assertNotEquals(xmlElement.hashCode(), otherXmlElement.hashCode());
        otherXmlElement.setName(name);
        otherXmlElement.setData("otherData");
        Assert.assertNotEquals(xmlElement.hashCode(), otherXmlElement.hashCode());
        otherXmlElement.setData(data);
        otherXmlElement.setAttributes(new Hashtable<>());
        Assert.assertNotEquals(xmlElement.hashCode(), otherXmlElement.hashCode());
        otherXmlElement.setAttributes(attributes);
        otherXmlElement.setSubElements(new ArrayList<>());
        Assert.assertNotEquals(xmlElement.hashCode(), otherXmlElement.hashCode());
        otherXmlElement.setSubElements(subElements);
        otherXmlElement.setParent(new XmlElement("otherParent"));
        Assert.assertNotEquals(xmlElement.hashCode(), otherXmlElement.hashCode());
    }

    @Test
    public void testPrintNode() {
        XmlElement xmlElementToPrint = new XmlElement("elementToPrint");
        xmlElementToPrint.addAttribute("attribute1", "value1");
        xmlElementToPrint.addElement(new XmlElement("element1"));
        xmlElementToPrint.addElement(new XmlElement("element2"));
        xmlElementToPrint.addElement(xmlElementToPrint);
        XmlElement.printNode(xmlElementToPrint, "");
    }
}