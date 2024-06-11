import org.apache.commons.jxpath.ri.ModelTest;
import org.apache.commons.jxpath.ri.ModelTestHelper;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

/**
 * Tests for {@link AttributeContext}.
 */
public class AttributeContextTest extends ModelTest {

    private ModelTestHelper helper;
    private AttributeContext attributeContext;
    private NodeIterator iterator;
    private NodePointer currentNodePointer;

    @Before
    public void setUp() {
        helper = new ModelTestHelper(this);
        Document document = helper.getDocument();
        Element root = document.createElement("root");
        root.setAttribute("data", "value");
        root.setAttribute("test", "testvalue");
        root.setAttribute("empty", "");
        document.appendChild(root);
        attributeContext = new AttributeContext(helper.createContext(document), new NodeNameTest(new QName("data")));
    }

    @Test
    public void testNextNodeNormalCase() {
        // When next node is called
        attributeContext.nextNode();

        // Then the current node pointer should be set to the data attribute node
        assertSame(root.getAttributeNode("data"), attributeContext.getCurrentNodePointer().getNode());
    }

    @Test
    public void testNextNodeAfterLastAttribute() {
        // Given that nextNode() was called for all the attributes
        for (int i = 0; i < root.getAttributes().getLength(); i++) {
            attributeContext.nextNode();
        }

        // When nextNode() is called again
        attributeContext.nextNode();

        // Then the current node pointer should be null
        assertNull(attributeContext.getCurrentNodePointer().getNode());
    }

    @Test
    public void testSetPositionNormalCase() {
        // Given that the current position is less than the number of attributes
        attributeContext.setPosition(1);
        Node attribute = attributeContext.nextNode().getNodePointer().getNode();

        // When setPosition() is called with the same position
        attributeContext.setPosition(1);

        // Then the current node pointer should be the same
        assertSame(attribute, attributeContext.getCurrentNodePointer().getNode());
    }

    @Test
    public void testSetPositionAfterLastAttribute() {
        // Given that the current position is equal to the number of attributes
        for (int i = 0; i < root.getAttributes().getLength(); i++) {
            attributeContext.nextNode();
        }

        // When setPosition() is called with the same position
        attributeContext.setPosition(root.getAttributes().getLength());

        // Then the current node pointer should be null
        assertNull(attributeContext.getCurrentNodePointer().getNode());
    }

    @Test
    public void testSetPositionBeforeFirstAttribute() {
        // Given that the current position is negative
        attributeContext.setPosition(-1);

        // When nextNode() is called
        Node node = attributeContext.nextNode().getNodePointer().getNode();

        // Then the current node pointer should be the first attribute node
        assertSame(root.getAttributeNode("data"), node);
    }

    @Test
    public void testNextNodeWithEmptyAttribute() {
        // Given an empty attribute
        attributeContext = new AttributeContext(helper.createContext(document), new NodeNameTest(new QName("empty")));

        // When nextNode() is called
        attributeContext.nextNode();

        // Then the current node pointer should be set to the empty attribute node
        assertSame(root.getAttributeNode("empty"), attributeContext.getCurrentNodePointer().getNode());
    }

    @Test
    public void testNextNodeWithNonExistentAttribute() {
        // Given a non-existent attribute
        attributeContext = new AttributeContext(helper.createContext(document), new NodeNameTest(new QName("nonExistentAttribute")));

        // When nextNode() is called
        attributeContext.nextNode();

        // Then the current node pointer should be null
        assertNull(attributeContext.getCurrentNodePointer().getNode());
    }

    @Test
    public void testNextNodeWithAttributeTestThatDoesNotMatchAnyAttribute() {
        // Given an attribute test that does not match any attribute
        attributeContext = new AttributeContext(helper.createContext(document), new NodeNameTest(new QName("nonExistentAttributeNameWithDifferentLength")));

        // When nextNode() is called
        attributeContext.nextNode();

        // Then the current node pointer should be null
        assertNull(attributeContext.getCurrentNodePointer().getNode());
    }
}