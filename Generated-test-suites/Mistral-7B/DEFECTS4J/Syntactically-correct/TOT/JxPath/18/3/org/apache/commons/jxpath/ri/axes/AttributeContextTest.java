package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test for {@link AttributeContext} class.
 *
 * @author Dmitri Plotnikov
 */
public class AttributeContextTest {

    private static final String XML_STRING =
        "<root attr1=\"value1\" attr2=\"value2\">" +
        " <child1 attr3=\"value3\" attr4=\"value4\"/>" +
        "</root>";

    private Document document;
    private EvalContext parentContext;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        document = TestUtil.parseStringToDocument(XML_STRING);
        parentContext = new EvalContext(new EvalContext(new EvalContext(document)));
    }

    @Test
    public void testFindAttributeByName() {
        NodeTest nodeTest = new NodeNameTest("@attr1");
        attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertEquals("value1", attributeContext.getCurrentNodePointer().getNodeValue());
    }

    @Test
    public void testFindAttributeByNonExistentName() {
        NodeTest nodeTest = new NodeNameTest("@attr5");
        attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNull(attributeContext.nextNode());
    }

    @Test
    public void testFindAttributeByEmptyName() {
        NodeTest nodeTest = new NodeNameTest("@");
        attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNull(attributeContext.nextNode());
    }

    @Test
    public void testFindAttributeByNullName() {
        NodeTest nodeTest = null;
        attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNull(attributeContext.nextNode());
    }

    @Test
    public void testFindAttributeByNameWithMultipleAttributes() {
        NodeTest nodeTest = new NodeNameTest("@attr2");
        attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertEquals("value2", attributeContext.getCurrentNodePointer().getNodeValue());
    }

    @Test
    public void testFindAttributeByNameWithDifferentNamespaces() {
        NodeTest nodeTest = new NodeNameTest("@ns1:attr1");
        attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNull(attributeContext.nextNode());

        NodeTest nodeTest2 = new NodeNameTest("@xmlns:ns1");
        parentContext = new EvalContext(parentContext, nodeTest2);
        attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertEquals("value1", attributeContext.getCurrentNodePointer().getNodeValue());
    }

    @Test
    public void testFindAttributeByNameWithSameNamespaceButDifferentLocalName() {
        NodeTest nodeTest = new NodeNameTest("@attr3");
        attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNull(attributeContext.nextNode());

        Element child1 = (Element) document.getElementsByTagName("child1").item(0);
        NodeTest nodeTest2 = new NodeNameTest("@attr4");
        parentContext = new EvalContext(parentContext, new EvalContext(child1));
        attributeContext = new AttributeContext(parentContext, nodeTest2);
        attributeContext.nextNode();
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertEquals("value4", attributeContext.getCurrentNodePointer().getNodeValue());
    }

    @Test
    public void testFindAttributeByNameWithEmptyValue() {
        NodeTest nodeTest = new NodeNameTest("@attr1");
        attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertEquals("", attributeContext.getCurrentNodePointer().getNodeValue());
    }
}