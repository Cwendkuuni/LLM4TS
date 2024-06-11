import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.apache.commons.jxpath.JXPathContext.newContext;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AttributeContextTest {

    @Test
    public void testAttributeContextSuccess() {
        Document doc = TestHelper.createSampleDocument("AttributeContextTest.xml");
        JXPathContext jxpathContext = newContext(doc);
        EvalContext parentContext = jxpathContext.newContext(new NodeTest(new QName("*")));
        EvalContext attributeContext = new AttributeContext(parentContext, new NodeNameTest("testattr"));

        // Test basic functionality
        attributeContext.nextNode();
        NodePointer currentNode = attributeContext.getCurrentNodePointer();
        assertTrue(currentNode != null);
        assertTrue(currentNode.getNodeType() == Node.ATTRIBUTE_NODE);
        assertTrue(currentNode.getNodeName().getLocalPart().equals("testattr"));
        assertTrue(currentNode.getNodeValue().equals("value1"));

        // Test next Node
        attributeContext.nextNode();
        currentNode = attributeContext.getCurrentNodePointer();
        assertTrue(currentNode != null);
        assertTrue(currentNode.getNodeType() == Node.ATTRIBUTE_NODE);
        assertTrue(currentNode.getNodeName().getLocalPart().equals("testattr"));
        assertTrue(currentNode.getNodeValue().equals("value2"));

        // Test reset
        attributeContext.reset();
        currentNode = attributeContext.getCurrentNodePointer();
        assertFalse(currentNode != null);

        // Test position
        attributeContext.setPosition(2);
        currentNode = attributeContext.getCurrentNodePointer();
        assertTrue(currentNode != null);
        assertTrue(currentNode.getNodeType() == Node.ATTRIBUTE_NODE);
        assertTrue(currentNode.getNodeName().getLocalPart().equals("testattr"));
        assertTrue(currentNode.getNodeValue().equals("value2"));

        // Test setPosition out of bounds
        attributeContext.setPosition(5);
        currentNode = attributeContext.getCurrentNodePointer();
        assertFalse(currentNode != null);
    }

    @Test
    public void testAttributeContextNoMatch() {
        Document doc = TestHelper.createSampleDocument("AttributeContextTest.xml");
        JXPathContext jxpathContext = newContext(doc);
        EvalContext parentContext = jxpathContext.newContext(new NodeTest(new QName("*")));
        EvalContext attributeContext = new AttributeContext(parentContext, new NodeNameTest("nonexistentattr"));
        attributeContext.nextNode();
        NodePointer currentNode = attributeContext.getCurrentNodePointer();
        assertFalse(currentNode != null);
    }

    @Test
    public void testAttributeContextNullParentContext() {
        Document doc = TestHelper.createSampleDocument("AttributeContextTest.xml");
        JXPathContext jxpathContext = newContext(doc);
        EvalContext parentContext = null;
        EvalContext attributeContext = new AttributeContext(parentContext, new NodeNameTest("testattr"));
        assertFalse(attributeContext.getCurrentNodePointer() != null);
    }

    @Test
    public void testAttributeContextNullNodeTest() {
        Document doc = TestHelper.createSampleDocument("AttributeContextTest.xml");
        JXPathContext jxpathContext = newContext(doc);
        EvalContext parentContext = jxpathContext.newContext(new NodeTest(new QName("*")));
        EvalContext attributeContext = new AttributeContext(parentContext, null);
        assertFalse(attributeContext.getCurrentNodePointer() != null);
    }
}