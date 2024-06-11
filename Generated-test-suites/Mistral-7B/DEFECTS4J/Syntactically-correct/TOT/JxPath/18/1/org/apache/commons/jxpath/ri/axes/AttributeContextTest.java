import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = new EvalContext(null, null);
        nodeTest = new NodeNameTest(new QName("testAttribute"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testNextNode_AttributeExist_ReturnTrue() {
        Element element = newElement("root", "test", "attributeValue");
        parentContext.setCurrentNodePointer(new NodePointer(element));

        assertTrue(attributeContext.nextNode());
        NodePointer expectedNodePointer = new NodePointer(element.getAttributeNode("testAttribute"));
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        assertEquals(expectedNodePointer, actualNodePointer);

        assertTrue(attributeContext.nextNode()); // next node should be the next sibling of the attribute node
    }

    @Test
    public void testNextNode_AttributeDoesntExist_ReturnFalse() {
        Element element = newElement("root", "test");
        parentContext.setCurrentNodePointer(new NodePointer(element));

        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode_ParentNodeDoesntHaveAttribute_ReturnFalse() {
        Element parentElement = newElement("root", "test");
        Element childElement = newElement("child", "testChild");
        parentElement.appendChild(childElement);
        parentContext.setCurrentNodePointer(new NodePointer(parentElement));

        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextNode_NullParentContext_ThrowIllegalArgumentException() {
        nodeTest = new NodeNameTest(new QName("testAttribute"));
        attributeContext = new AttributeContext(null, nodeTest);
        attributeContext.nextNode();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextNode_NullNodeTest_ThrowIllegalArgumentException() {
        AttributeContext attributeContext = new AttributeContext(parentContext, null);
        attributeContext.nextNode();
    }

    @Test
    public void testReset_ResetToInitialState() {
        Element element = newElement("root", "test", "attributeValue");
        parentContext.setCurrentNodePointer(new NodePointer(element));
        attributeContext.nextNode();
        attributeContext.reset();

        assertNull(attributeContext.getCurrentNodePointer());
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testSetPosition_PositionOutOfRange_ReturnFalse() {
        Element element = newElement("root", "test", "attributeValue");
        parentContext.setCurrentNodePointer(new NodePointer(element));
        attributeContext.nextNode();

        assertFalse(attributeContext.setPosition(-1));
        assertFalse(attributeContext.nextNode());

        assertFalse(attributeContext.setPosition(Integer.MAX_VALUE));
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testSetPosition_PositionWithinRange_ReturnTrue() {
        Element element = newElement("root", "test1", "attributeValue1");
        Element element2 = newElement("root", "test2", "attributeValue2");
        parentContext.setCurrentNodePointer(new NodePointer(element));
        attributeContext.nextNode();

        assertTrue(attributeContext.setPosition(0));
        assertTrue(attributeContext.nextNode()); // should return the first attribute of the first element

        assertTrue(attributeContext.setPosition(1));
        assertTrue(attributeContext.nextNode()); // should return the second attribute of the first element

        assertTrue(attributeContext.setPosition(1));
        assertTrue(attributeContext.nextNode()); // should return the first attribute of the second element

        assertTrue(attributeContext.setPosition(0));
        assertTrue(attributeContext.nextNode()); // should return the first attribute of the second element
    }

    private Element newElement(String tagName, String... attributeNamesAndValues) {
        Element element = document.createElement(tagName);
        for (String nameValue : attributeNamesAndValues) {
            String[] nameValuePair = nameValue.split("=");
            if (nameValuePair.length != 2) {
                throw new IllegalArgumentException("Invalid attribute format: " + nameValue);
            }
            String name = nameValuePair[0];
            String value = nameValuePair[1].substring(1, nameValuePair[1].length());
            Element attributeElement = document.createAttribute(name);
            attributeElement.setValue(value);
            element.setAttributeNode(attributeElement);
        }
        return element;
    }
}