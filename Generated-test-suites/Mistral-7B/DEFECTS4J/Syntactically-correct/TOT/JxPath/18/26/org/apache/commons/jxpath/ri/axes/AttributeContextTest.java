import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;
    private Document document;
    private Element rootElement;

    @Before
    public void setUp() {
        nodeTest = new NodeNameTest("testAttr");
        parentContext = new EvalContext(null, null);
        document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        rootElement = document.createElement("root");
        document.appendChild(rootElement);
        rootElement.setAttribute("testAttr1", "value1");
        rootElement.setAttribute("testAttr2", "value2");
        parentContext.setCurrentNodePointer(document);
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @After
    public void tearDown() {
        attributeContext.reset();
    }

    @Test
    public void testGetCurrentNodePointer_initialState() {
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testGetCurrentNodePointer_nextNode_returnsSameNode() {
        attributeContext.nextNode();
        NodePointer expectedNodePointer = rootElement.getAttributeNodeNamed("testAttr1");
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        assertSame(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testGetCurrentNodePointer_nextNode_returnsNull() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_initialState() {
        assertFalse(attributeContext.setPosition(0));
    }

    @Test
    public void testSetPosition_nextNode_returnsTrue() {
        attributeContext.nextNode();
        assertTrue(attributeContext.setPosition(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPosition_outOfBounds() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.setPosition(3);
    }

    @Test
    public void testNextNode_initialState() {
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNode_returnsTrue() {
        attributeContext.nextNode();
        assertTrue(attributeContext.nextNode());
    }

    @Test
    public void testNextNode_returnsFalse() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        assertFalse(attributeContext.nextNode());
    }
}