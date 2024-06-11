package org.junit.jupiter.api.extension;

import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.exceptions.JXPathRuntimeException;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.Assert.*;

@ExtendWith(org.junit.jupiter.api.Assertions.Assertions.class)
public class AttributeContextTest {

    private AttributeContext attributeContext;
    private NodeTest nodeTest;
    private NodeIterator nodeIterator;

    @BeforeEach
    public void setup() {
        nodeTest = new NodeNameTest(new QName("test:attribute"));
        attributeContext = new AttributeContext(null, nodeTest);
        nodeIterator = new NodeIterator(new Object());
        attributeContext.setIterator(nodeIterator);
    }

    @Test
    public void testConstructor() {
        AttributeContext attributeContext = new AttributeContext(null, nodeTest);
        Assert.assertNotNull(attributeContext);
    }

    @Test
    public void testGetCurrentNodePointer() {
        NodePointer nodePointer = new NodePointer(new Object());
        attributeContext = new AttributeContext(null, nodeTest);
        attributeContext.setCurrentNodePointer(nodePointer);
        NodePointer actual = attributeContext.getCurrentNodePointer();
        Assert.assertEquals(nodePointer, actual);
    }

    @Test
    public void testReset() {
        attributeContext = new AttributeContext(null, nodeTest);
        attributeContext.reset();
        Assert.assertFalse(attributeContext.isSetStarted());
        Assert.assertNull(attributeContext.getIterator());
    }

    @Test
    public void testSetPosition() {
        attributeContext = new AttributeContext(null, nodeTest);
        attributeContext.setCurrentNodePointer(new NodePointer(new Object()));
        Assert.assertTrue(attributeContext.setPosition(0));
        Assert.assertTrue(attributeContext.getCurrentPosition() == 0);
        Assert.assertEquals(new NodePointer(new Object()), attributeContext.getCurrentNodePointer());
        Assert.assertFalse(attributeContext.setPosition(-1));
        Assert.assertFalse(attributeContext.setPosition(1));
    }

    @Test
    public void testNextNode() {
        attributeContext = new AttributeContext(null, nodeTest);
        attributeContext.setCurrentNodePointer(new NodePointer(new Object()));
        Assert.assertTrue(attributeContext.nextNode());
        Assert.assertTrue(attributeContext.getCurrentPosition() == 1);
        Assert.assertFalse(attributeContext.nextNode());
        Assert.assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testGetIterator() {
        attributeContext = new AttributeContext(null, nodeTest);
        Assert.assertNull(attributeContext.getIterator());
    }

    @Test
    public void testIsSetStarted() {
        attributeContext = new AttributeContext(null, nodeTest);
        Assert.assertFalse(attributeContext.isSetStarted());
    }

    @Test
    public void testSetIterator() {
        attributeContext = new AttributeContext(null, nodeTest);
        NodeIterator iterator = new NodeIterator(new Object());
        attributeContext.setIterator(iterator);
        NodeIterator actual = attributeContext.getIterator();
        Assert.assertEquals(iterator, actual);
    }

    @Test
    public void testGetCurrentPosition() {
        attributeContext = new AttributeContext(null, nodeTest);
        Assert.assertEquals(-1, attributeContext.getCurrentPosition());

        attributeContext.setIterator(new NodeIterator(new Object()));
        attributeContext.nextNode();
        Assert.assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test(expected = JXPathRuntimeException.class)
    public void testGetCurrentNodePointerWithNullIterator() {
        AttributeContext attributeContext = new AttributeContext(null, nodeTest);
        attributeContext.setIterator(null);
        attributeContext.getCurrentNodePointer();
    }

    @Test(expected = JXPathRuntimeException.class)
    public void testNextNodeWithNullIterator() {
        AttributeContext attributeContext = new AttributeContext(null, nodeTest);
        attributeContext.setIterator(null);
        attributeContext.nextNode();
    }

    @Test
    public void testConstructorWithNullParentContext() {
        AttributeContext attributeContext = new AttributeContext(null, nodeTest);
    }

    @Test
    public void testConstructorWithNullNodeTest() {
        AttributeContext attributeContext = new AttributeContext(null, null);
    }

    @Test(expected = JXPathRuntimeException.class)
    public void testGetCurrentNodePointerWithNullCurrentNodePointer() {
        AttributeContext attributeContext = new AttributeContext(null, nodeTest);
        attributeContext.setCurrentNodePointer(null);
        attributeContext.getCurrentNodePointer();
    }

    @Test(expected = JXPathRuntimeException.class)
    public void testSetPositionWithNegativePosition() {
        AttributeContext attributeContext = new AttributeContext(null, nodeTest);
        attributeContext.setPosition(-1);
    }

    @Test(expected = JXPathRuntimeException.class)
    public void testSetPositionWithPositionGreaterThanCurrentPosition() {
        AttributeContext attributeContext = new AttributeContext(null, nodeTest);
        attributeContext.nextNode();
        attributeContext.setPosition(2);
    }

    static Stream<Arguments> provideInvalidNodeTestCases() {
        return Stream.of(
                Arguments.arguments(null),
                Arguments.arguments(new Object())
        );
    }

    @ParameterizedTest(name = "{index}: testSetPositionWithInvalidNodeTestCases[{0}]")
    @MethodSource("provideInvalidNodeTestCases")
    public void testSetPositionWithInvalidNodeTestCases(Object invalidNodeTest) {
        AttributeContext attributeContext = new AttributeContext(null, null);
        AttributeContext attributeContextWithInvalidNodeTest = new AttributeContext(null, invalidNodeTest);
        attributeContextWithInvalidNodeTest.setPosition(0);
    }
}