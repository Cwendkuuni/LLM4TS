package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;

import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link AttributeContext}.
 */
public class AttributeContextTest {

    private EvalContext parentContext;
    private AttributeContext attributeContext;
    private Node testNode;

    @Before
    public void setUp() {
        Document doc = new DocumentImpl();
        Element root = doc.createElement("root");
        doc.appendChild(root);

        Element a = doc.createElement("a");
        a.setAttribute("name", "value");
        root.appendChild(a);

        testNode = doc.createElement("test");
        a.appendChild(testNode);
        testNode.setAttribute("test", "attribute");

        parentContext = new EvaluationContext(doc);
    }

    @Test
    public void testAttributeContext_MatchingAttribute() {
        NodeTest nodeTest = new NodeNameTest(new QName("test:test"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertNotNull(attributeContext.getCurrentNodePointer().getNode());
        assertNotNull(attributeContext.getCurrentNodePointer().getNode().getNodeValue());
    }

    @Test
    public void testAttributeContext_NoMatchingAttribute() {
        NodeTest nodeTest = new NodeNameTest(new QName("test:wrong"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testAttributeContext_ParentNodeIsNull() {
        NodeTest nodeTest = new NodeNameTest(new QName("test:test"));
        AttributeContext attributeContext = new AttributeContext(null, nodeTest);
        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testAttributeContext_IterateThroughAllAttributes() {
        NodeTest nodeTest = new NodeNameTest(new QName("*:*"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        int count = 0;
        while (attributeContext.nextNode()) {
            assertNotNull(attributeContext.getCurrentNodePointer());
            assertNotNull(attributeContext.getCurrentNodePointer().getNode());
            assertNotNull(attributeContext.getCurrentNodePointer().getNode().getNodeValue());
            count++;
        }
        assertEquals(3, count); // root, a, test
    }

    @Test
    public void testAttributeContext_Reset() {
        NodeTest nodeTest = new NodeNameTest(new QName("test:test"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        attributeContext.reset();
        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }
}