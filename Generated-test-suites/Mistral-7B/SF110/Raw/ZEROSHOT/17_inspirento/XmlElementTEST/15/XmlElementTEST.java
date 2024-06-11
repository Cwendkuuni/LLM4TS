import org.junit.Test;
import org.junit.Assert.*;
import com.allenstudio.ir.util.XmlElement;

public class XmlElementTest {

    @Test
    public void testXmlElementConstruction() {
        XmlElement xmlElement = new XmlElement();
        Assert.assertNotNull(xmlElement);

        XmlElement xmlElementWithName = new XmlElement("elementName");
        Assert.assertEquals("elementName", xmlElementWithName.getName());

        XmlElement xmlElementWithNameAndAttributes = new XmlElement("elementName", new Hashtable<String, String>() {{
            put("attr1", "value1");
            put("attr2", "value2");
        }});
        Assert.assertEquals("elementName", xmlElementWithNameAndAttributes.getName());
        Assert.assertEquals("value1", xmlElementWithNameAndAttributes.getAttribute("attr1"));
        Assert.assertEquals("value2", xmlElementWithNameAndAttributes.getAttribute("attr2"));

        XmlElement xmlElementWithNameAndData = new XmlElement("elementName", "elementData");
        Assert.assertEquals("elementName", xmlElementWithNameAndData.getName());
        Assert.assertEquals("elementData", xmlElementWithNameAndData.getData());
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attr1", "value1");
        Assert.assertEquals("value1", xmlElement.getAttribute("attr1"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attr1", "value1");
        Assert.assertEquals("value1", xmlElement.getAttribute("attr1"));

        XmlElement xmlElementWithDefaultValue = new XmlElement();
        xmlElementWithDefaultValue.addAttribute("attr1", "defaultValue");
        Assert.assertEquals("defaultValue", xmlElementWithDefaultValue.getAttribute("attr1", "defaultValue2"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attr1", "value1");
        xmlElement.addAttribute("attr2", "value2");
        Assert.assertEquals(xmlElement.getAttributes(), xmlElement.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.setAttributes(new Hashtable<String, String>() {{
            put("attr1", "value1");
            put("attr2", "value2");
        }});
        Assert.assertEquals("value1", xmlElement.getAttribute("attr1"));
        Assert.assertEquals("value2", xmlElement.getAttribute("attr2"));
    }

    @Test
    public void testAttributeNames() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attr1", "value1");
        xmlElement.addAttribute("attr2", "value2");
        Enumeration enumeration = xmlElement.getAttributeNames();
        Assert.assertTrue(enumeration.hasMoreElements());
        Assert.assertEquals("attr1", enumeration.nextElement());
        Assert.assertTrue(enumeration.hasMoreElements());
        Assert.assertEquals("attr2", enumeration.nextElement());
    }

    @Test
    public void testAddElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("childElement");
        parentElement.addElement(childElement);
        Assert.assertEquals(childElement, parentElement.getElements().get(0));
    }

    @Test
    public void testRemoveElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("childElement");
        parentElement.addElement(childElement);
        parentElement.removeElement(childElement);
        Assert.assertFalse(parentElement.getElements().contains(childElement));
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        parentElement.removeAllElements();
        Assert.assertTrue(parentElement.getElements().isEmpty());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("childElement");
        parentElement.addElement(childElement);
        childElement.removeFromParent();
        Assert.assertFalse(parentElement.getElements().contains(childElement));
        Assert.assertNull(childElement.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");
        parentElement.append(childElement1);
        parentElement.append(childElement2);
        Assert.assertEquals(childElement1, parentElement.getElements().get(0));
        Assert.assertEquals(childElement2, parentElement.getElements().get(1));
    }

    @Test
    public void testInsertElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");
        parentElement.insertElement(childElement2, 0);
        Assert.assertEquals(childElement2, parentElement.getElements().get(0));
        Assert.assertEquals(childElement1, parentElement.getElements().get(1));
    }

    @Test
    public void testGetElements() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        List<XmlElement> elements = parentElement.getElements();
        Assert.assertEquals(childElement1, elements.get(0));
        Assert.assertEquals(childElement2, elements.get(1));
    }

    @Test
    public void testCount() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        Assert.assertEquals(2, parentElement.count());
    }

    @Test
    public void testElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = parentElement.getElement("childElement1/childElement2");
        Assert.assertNull(childElement);

        XmlElement parentElementWithChild = new XmlElement("parentElement");
        XmlElement childElement1 = new XmlElement("childElement1");
        XmlElement childElement2 = new XmlElement("childElement2");
        parentElementWithChild.addElement(childElement1);
        childElement1.addElement(childElement2);
        childElement = parentElementWithChild.getElement("childElement1/childElement2");
        Assert.assertEquals(childElement2, childElement);

        childElement = parentElementWithChild.getElement("childElement3/childElement4");
        Assert.assertNull(childElement);
    }

    @Test
    public void testAddSubElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = parentElement.addSubElement("childElement");
        Assert.assertEquals("childElement", childElement.getName());
        Assert.assertNull(childElement.getData());
        Assert.assertTrue(parentElement.getElements().contains(childElement));
        Assert.assertNull(childElement.getParent());

        childElement = parentElement.addSubElement("childElement", "childData");
        Assert.assertEquals("childElement", childElement.getName());
        Assert.assertEquals("childData", childElement.getData());
        Assert.assertTrue(parentElement.getElements().contains(childElement));
        Assert.assertNull(childElement.getParent());
    }

    @Test
    public void testSetParent() {
        XmlElement childElement = new XmlElement("childElement");
        XmlElement parentElement = new XmlElement();
        childElement.setParent(parentElement);
        Assert.assertEquals(parentElement, childElement.getParent());
        Assert.assertFalse(parentElement.getElements().contains(childElement));

        parentElement.addElement(childElement);
        Assert.assertEquals(childElement, parentElement.getElements().get(0));
        Assert.assertEquals(parentElement, childElement.getParent());
    }

    @Test
    public void testClone() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attr1", "value1");
        xmlElement.addAttribute("attr2", "value2");
        xmlElement.addElement(new XmlElement("childElement1"));
        xmlElement.addElement(new XmlElement("childElement2"));

        XmlElement clonedXmlElement = (XmlElement) xmlElement.clone();
        Assert.assertNotSame(xmlElement, clonedXmlElement);
        Assert.assertEquals(xmlElement.getData(), clonedXmlElement.getData());
        Assert.assertEquals(xmlElement.getName(), clonedXmlElement.getName());
        Assert.assertEquals(xmlElement.getAttributes(), clonedXmlElement.getAttributes());
        Assert.assertEquals(xmlElement.getElements(), clonedXmlElement.getElements());

        XmlElement childElement1 = xmlElement.getElements().get(0);
        XmlElement clonedChildElement1 = clonedXmlElement.getElements().get(0);
        Assert.assertEquals(childElement1, clonedChildElement1);

        XmlElement childElement2 = xmlElement.getElements().get(1);
        XmlElement clonedChildElement2 = clonedXmlElement.getElements().get(1);
        Assert.assertEquals(childElement2, clonedChildElement2);
    }

    @Test
    public void testPrintNode() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attr1", "value1");
        xmlElement.addElement(new XmlElement("childElement1"));
        xmlElement.addElement(new XmlElement("childElement2"));
        xmlElement.addAttribute("attr2", "value2");

        XmlElement.printNode(xmlElement, "");
    }

    @Test
    public void testEquals() {
        XmlElement xmlElement1 = new XmlElement();
        XmlElement xmlElement2 = new XmlElement();
        XmlElement xmlElement3 = new XmlElement();
        XmlElement xmlElement4 = xmlElement1;

        Assert.assertFalse(xmlElement1.equals(xmlElement2));
        Assert.assertFalse(xmlElement1.equals(xmlElement3));
        Assert.assertTrue(xmlElement1.equals(xmlElement4));

        xmlElement1.addAttribute("attr1", "value1");
        xmlElement2.addAttribute("attr1", "value1");
        Assert.assertTrue(xmlElement1.equals(xmlElement2));

        xmlElement1.addAttribute("attr1", "value2");
        Assert.assertFalse(xmlElement1.equals(xmlElement2));

        xmlElement1.setData("data1");
        xmlElement2.setData("data1");
        Assert.assertTrue(xmlElement1.equals(xmlElement2));

        xmlElement1.setData("data2");
        Assert.assertFalse(xmlElement1.equals(xmlElement2));

        xmlElement1.setName("name1");
        xmlElement2.setName("name2");
        Assert.assertFalse(xmlElement1.equals(xmlElement2));

        xmlElement1.addElement(new XmlElement("childElement1"));
        xmlElement2.addElement(new XmlElement("childElement1"));
        Assert.assertTrue(xmlElement1.equals(xmlElement2));

        xmlElement1.getElements().clear();
        xmlElement2.getElements().clear();
        Assert.assertFalse(xmlElement1.equals(xmlElement2));

        xmlElement1.getElements().add(new XmlElement("childElement1"));
        xmlElement2.getElements().add(new XmlElement("childElement2"));
        Assert.assertFalse(xmlElement1.equals(xmlElement2));
    }

    @Test
    public void testHashCode() {
        XmlElement xmlElement = new XmlElement();
        int hashCode = xmlElement.hashCode();

        xmlElement.addAttribute("attr1", "value1");
        hashCode = xmlElement.hashCode();

        xmlElement.addAttribute("attr2", "value2");
        hashCode = xmlElement.hashCode();

        xmlElement.addElement(new XmlElement("childElement1"));
        hashCode = xmlElement.hashCode();

        Assert.assertNotEquals(hashCode, xmlElement.hashCode());
    }
}