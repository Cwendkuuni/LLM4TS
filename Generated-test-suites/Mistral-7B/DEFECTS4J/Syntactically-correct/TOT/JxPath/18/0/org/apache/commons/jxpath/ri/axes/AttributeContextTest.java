import org.apache.commons.jxpath.ri.AXIS;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.model.AbstractIteratingNodesTest;
import org.apache.commons.jxpath.ri.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AttributeContextTest extends AbstractIteratingNodesTest {

    private AttributeContext attributeContext;
    private EvalContext parentContext;
    private Document doc;

    @Before
    public void setUp() {
        parentContext = new EvalContext(null, null);
        doc = TestUtil.newDocument();

        Element root = doc.createElement("root");
        root.setAttribute("attr1", "value1");
        root.setAttribute("attr2", "value2");
        root.setAttribute("attr3", "value3");
        root.setAttribute("attr4", null);
        root.setAttribute("attr5", "");
        doc.appendChild(root);

        attributeContext = new AttributeContext(parentContext, new NodeNameTest("attr1"));
    }

    @Test
    public void testNextNode_FirstAttribute() {
        NodePointer expectedNodePointer = new NodePointer(doc.getDocumentElement(), "attr1");
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();

        assertTrue(attributeContext.nextNode());
        assertEquals(expectedNodePointer, actualNodePointer);
        assertEquals("value1", attributeContext.evaluateStringExpression("..", "."));
    }

    @Test
    public void testNextNode_SecondAttribute() {
        attributeContext.nextNode();
        NodePointer expectedNodePointer = new NodePointer(doc.getDocumentElement(), "attr2");
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();

        assertTrue(attributeContext.nextNode());
        assertEquals(expectedNodePointer, actualNodePointer);
        assertEquals("value2", attributeContext.evaluateStringExpression("..", "."));
    }

    @Test
    public void testNextNode_ThirdAttribute() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        NodePointer expectedNodePointer = new NodePointer(doc.getDocumentElement(), "attr3");
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();

        assertTrue(attributeContext.nextNode());
        assertEquals(expectedNodePointer, actualNodePointer);
        assertEquals("value3", attributeContext.evaluateStringExpression("..", "."));
    }

    @Test
    public void testNextNode_FourthAttribute() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        NodePointer expectedNodePointer = new NodePointer(doc.getDocumentElement(), "attr4");
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();

        assertTrue(attributeContext.nextNode());
        assertEquals(expectedNodePointer, actualNodePointer);
        assertNull(attributeContext.evaluateStringExpression("..", "."));
    }

    @Test
    public void testNextNode_FifthAttribute() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        NodePointer expectedNodePointer = new NodePointer(doc.getDocumentElement(), "attr5");
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();

        assertTrue(attributeContext.nextNode());
        assertEquals(expectedNodePointer, actualNodePointer);
        assertNull(attributeContext.evaluateStringExpression("..", "."));
    }

    @Test
    public void testNextNode_EndOfAttributes() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();

        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testSetPosition_FirstAttribute() {
        attributeContext.nextNode();

        assertTrue(attributeContext.setPosition(0));
        NodePointer expectedNodePointer = new NodePointer(doc.getDocumentElement(), "attr1");
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();

        assertEquals(expectedNodePointer, actualNodePointer);
        assertEquals("value1", attributeContext.evaluateStringExpression("..", "."));
    }

    @Test
    public void testSetPosition_SecondAttribute() {
        attributeContext.nextNode();

        assertTrue(attributeContext.setPosition(1));
        NodePointer expectedNodePointer = new NodePointer(doc.getDocumentElement(), "attr2");
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();

        assertEquals(expectedNodePointer, actualNodePointer);
        assertEquals("value2", attributeContext.evaluateStringExpression("..", "."));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPosition_AttributeOutOfBounds() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();

        attributeContext.setPosition(4);
    }

    @Test
    public void testSetPosition_ParentNode() {
        attributeContext.setPosition(0);

        NodeTest nodeTest = new NodeNameTest("root");
        EvalContext rootContext = new AttributeContext(parentContext, nodeTest);
        assertNull(rootContext.getCurrentNodePointer());
    }
}