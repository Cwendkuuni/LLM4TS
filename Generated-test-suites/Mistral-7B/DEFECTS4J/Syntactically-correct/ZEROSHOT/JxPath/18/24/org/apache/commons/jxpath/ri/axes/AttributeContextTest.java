package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.Axis;

public class AttributeContextTest {

	@org.junit.Before
	public void setUp() {
		// Create mock EvalContext for parentContext
		EvalContext parentContext = mock(EvalContext.class);
	}

	@org.junit.Test
	public void testAttributeContextConstruction() {
		NodeTest nodeTest = mock(NodeTest.class);
		AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
		assertNotNull(attributeContext);
		assertEquals(Axis.ATTRIBUTE, attributeContext.getAxis());
	}

	@org.junit.Test
	public void testGetCurrentNodePointerWhenNotInitialized() {
		NodeTest nodeTest = mock(NodeTest.class);
		AttributeContext attributeContext = new AttributeContext(mock(EvalContext.class), nodeTest);
		assertNull(attributeContext.getCurrentNodePointer());
	}

	@org.junit.Test
	public void testSetPositionWhenNotInitialized() {
		NodeTest nodeTest = mock(NodeTest.class);
		AttributeContext attributeContext = new AttributeContext(mock(EvalContext.class), nodeTest);
		assertFalse(attributeContext.setPosition(0));
	}

	@org.junit.Test
	public void testNextNodeWhenNotInitialized() {
		NodeTest nodeTest = mock(NodeTest.class);
		AttributeContext attributeContext = new AttributeContext(mock(EvalContext.class), nodeTest);
		assertFalse(attributeContext.nextNode());
	}

	@org.junit.Test
	public void testNextNodeWhenAttributeIteratorRetunsNull() {
		NodeTest nodeTest = mock(NodeTest.class);
		AttributeContext attributeContext = new AttributeContext(mock(EvalContext.class), nodeTest);
		when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class));
		when(parentContext.getCurrentNodePointer().attributeIterator(nodeTest.getNodeName())).thenReturn(null);
		assertFalse(attributeContext.nextNode());
	}

	@org.junit.Test
	public void testNextNodeWhenAttributeIteratorRetunsNode() {
		NodeTest nodeTest = mock(NodeTest.class);
		AttributeContext attributeContext = new AttributeContext(mock(EvalContext.class), nodeTest);
		when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class));
		when(parentContext.getCurrentNodePointer().attributeIterator(nodeTest.getNodeName())).thenReturn(mock(NodeIterator.class));
		NodePointer nodePointer = mock(NodePointer.class);
		when(nodeIterator.getNodePointer()).thenReturn(nodePointer);
		when(nodePointer.getNodeType()).thenReturn(1); // ELEMENT_NODE
		assertTrue(attributeContext.nextNode());
		assertEquals(1, attributeContext.getCurrentPosition());
	}

	@org.junit.Test
	public void testNextNodeWhenAttributeIteratorRetunsLastNode() {
		NodeTest nodeTest = mock(NodeTest.class);
		AttributeContext attributeContext = new AttributeContext(mock(EvalContext.class), nodeTest);
		when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class));
		when(parentContext.getCurrentNodePointer().attributeIterator(nodeTest.getNodeName())).thenReturn(mock(NodeIterator.class));
		NodePointer nodePointer = mock(NodePointer.class);
		when(nodeIterator.getNodePointer()).thenReturn(nodePointer);
		when(nodePointer.getNodeType()).thenReturn(1); // ELEMENT_NODE
		when(nodeIterator.hasNext()).thenReturn(false);
		assertFalse(attributeContext.nextNode());
		assertEquals(1, attributeContext.getCurrentPosition());
	}

	@org.junit.Test
	public void testNextNodeWhenAttributeIteratorRetunsAttributeNode() {
		NodeTest nodeTest = mock(NodeTest.class);
		AttributeContext attributeContext = new AttributeContext(mock(EvalContext.class), nodeTest);
		when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class));
		when(parentContext.getCurrentNodePointer().attributeIterator(nodeTest.getNodeName())).thenReturn(mock(NodeIterator.class));
		NodePointer nodePointer = mock(NodePointer.class);
		when(nodeIterator.getNodePointer()).thenReturn(nodePointer);
		when(nodePointer.getNodeType()).thenReturn(2); // ATTRIBUTE_NODE
		assertTrue(attributeContext.nextNode());
		assertEquals(2, attributeContext.getCurrentPosition());
	}

	@org.junit.Test
	public void testGetCurrentNodePointerAfterNextNode() {
		NodeTest nodeTest = mock(NodeTest.class);
		AttributeContext attributeContext = new AttributeContext(mock(EvalContext.class), nodeTest);
		when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class));
		when(parentContext.getCurrentNodePointer().attributeIterator(nodeTest.getNodeName())).thenReturn(mock(NodeIterator.class));
		NodePointer nodePointer = mock(NodePointer.class);
		when(nodeIterator.getNodePointer()).thenReturn(nodePointer);
		when(nodePointer.getNodeType()).thenReturn(2); // ATTRIBUTE_NODE
		assertTrue(attributeContext.nextNode());
		assertEquals(2, attributeContext.getCurrentPosition());
		assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
	}

	@org.junit.Test
	public void testReset() {
		NodeTest nodeTest = mock(NodeTest.class);
		AttributeContext attributeContext = new AttributeContext(mock(EvalContext.class), nodeTest);
		attributeContext.nextNode();
		attributeContext.reset();
		assertNull(attributeContext.getCurrentNodePointer());
		assertEquals(0, attributeContext.getCurrentPosition());
	}
}