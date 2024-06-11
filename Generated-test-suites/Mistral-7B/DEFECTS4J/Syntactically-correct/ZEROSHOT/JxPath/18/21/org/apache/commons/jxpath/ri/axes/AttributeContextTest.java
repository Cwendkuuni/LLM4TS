import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.axes.AttributeContextTest;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.XPathNode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AttributeContextTest {

    @org.junit.Before
    public void setUp() {
        // Set up test data here, if needed
    }

    @org.junit.After
    public void tearDown() {
        // Clean up test data here, if needed
    }

    /**
     * Test the constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest(new QName("testName"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertEquals(parentContext, attributeContext.getParentContext());
        assertEquals(nodeTest, attributeContext.getNodeTest());
        assertNull(attributeContext.getCurrentNodePointer());
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getIterator());
    }

    /**
     * Test getCurrentNodePointer().
     */
    @org.junit.Test
    public void testGetCurrentNodePointer() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest(new QName("testName"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        XPathNode rootNode = new XPathNode(new NodePointer(new XPathNode("root"), 0), null);
        parentContext.setCurrentNodePointer(rootNode.createNodePointer());

        XPathNode attributeNode = new XPathNode(new NodePointer(new XPathNode("root"), 1), new QName("testName"));
        attributeNode.setValue("testValue");
        NodeIterator attributeIterator = new NodeIterator(attributeNode.createNodeIterator());

        attributeContext.setIterator(attributeIterator);

        XPathNode currentNode = attributeContext.nextNode();
        AttributeContext currentAttributeContext = (AttributeContext) currentNode.getNodePointer().getOwnerNode();
        NodePointer expectedCurrentNodePointer = attributeNode.createNodePointer();
        NodePointer actualCurrentNodePointer = currentAttributeContext.getCurrentNodePointer();

        assertEquals(expectedCurrentNodePointer, actualCurrentNodePointer);
    }

    /**
     * Test reset().
     */
    @org.junit.Test
    public void testReset() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest(new QName("testName"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        XPathNode rootNode = new XPathNode(new NodePointer(new XPathNode("root"), 0), null);
        parentContext.setCurrentNodePointer(rootNode.createNodePointer());

        XPathNode attributeNode = new XPathNode(new NodePointer(new XPathNode("root"), 1), new QName("testName"));
        attributeNode.setValue("testValue");
        NodeIterator attributeIterator = new NodeIterator(attributeNode.createNodeIterator());

        attributeContext.setIterator(attributeIterator);

        attributeContext.nextNode();

        attributeContext.reset();

        assertNull(attributeContext.getCurrentNodePointer());
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getIterator());
    }

    /**
     * Test setPosition().
     */
    @org.junit.Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest(new QName("testName"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        XPathNode rootNode = new XPathNode(new NodePointer(new XPathNode("root"), 0), null);
        parentContext.setCurrentNodePointer(rootNode.createNodePointer());

        XPathNode attributeNode1 = new XPathNode(new NodePointer(new XPathNode("root"), 1), new QName("testName1"));
        attributeNode1.setValue("testValue1");
        NodeIterator attributeIterator1 = new NodeIterator(attributeNode1.createNodeIterator());

        XPathNode attributeNode2 = new XPathNode(new NodePointer(new XPathNode("root"), 1), new QName("testName2"));
        attributeNode2.setValue("testValue2");
        NodeIterator attributeIterator2 = new NodeIterator(attributeNode2.createNodeIterator());

        attributeContext.setIterator1(attributeIterator1);
        attributeContext.setIterator2(attributeIterator2);

        attributeContext.nextNode();

        assertTrue(attributeContext.nextNode());
        assertTrue(attributeContext.nextNode());

        attributeContext.setPosition(1);

        NodePointer actualCurrentNodePointer = attributeContext.getCurrentNodePointer();
        NodePointer expectedCurrentNodePointer = attributeNode1.createNodePointer();

        assertEquals(expectedCurrentNodePointer, actualCurrentNodePointer);
    }

    /**
     * Test nextNode().
     */
    @org.junit.Test
    public void testNextNode() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest(new QName("testName"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        XPathNode rootNode = new XPathNode(new NodePointer(new XPathNode("root"), 0), null);
        parentContext.setCurrentNodePointer(rootNode.createNodePointer());

        XPathNode attributeNode1 = new XPathNode(new NodePointer(new XPathNode("root"), 1), new QName("testName1"));
        attributeNode1.setValue("testValue1");
        NodeIterator attributeIterator1 = new NodeIterator(attributeNode1.createNodeIterator());

        XPathNode attributeNode2 = new XPathNode(new NodePointer(new XPathNode("root"), 1), new QName("testName2"));
        attributeNode2.setValue("testValue2");
        NodeIterator attributeIterator2 = new NodeIterator(attributeNode2.createNodeIterator());

        attributeContext.setIterator1(attributeIterator1);
        attributeContext.setIterator2(attributeIterator2);

        attributeContext.nextNode();

        assertTrue(attributeContext.nextNode());

        NodePointer actualCurrentNodePointer = attributeContext.getCurrentNodePointer();
        NodePointer expectedCurrentNodePointer = attributeNode2.createNodePointer();

        assertEquals(expectedCurrentNodePointer, actualCurrentNodePointer);
    }
}