import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

 private EvalContext parentContext;
 private AttributeContext attributeContext;
 private NodeTest nodeTest;

 @Before
 public void setUp() {
 parentContext = new EvaluationContext();
 nodeTest = new NodeNameTest(new QName("test"));
 attributeContext = new AttributeContext(parentContext, nodeTest);
 }

 @Test
 public void testNextNodeWithExistingAttribute() {
 // Given: A node with an existing attribute
 NodePointer nodeWithAttribute = new NodePointer(new Object(), new int[] {0});
 nodeWithAttribute.setLineNumber(1);
 nodeWithAttribute.setColumnNumber(1);
 NodeIterator attributeIterator = new NodeIterator() {
 @Override
 public NodePointer getNodePointer() {
 return new NodePointer(new Object(), new int[] {1});
 }

 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public NodePointer next() {
 return new NodePointer(new Object(), new int[] {1});
 }

 @Override
 public NodeIterator attributeIterator(QName name) {
 return this;
 }
 };
 parentContext.setCurrentNodePointer(nodeWithAttribute);
 attributeContext.setStarted = true;
 attributeContext.iterator = attributeIterator;

 // When: Next node is called
 NodePointer nextNode = attributeContext.nextNode();

 // Then: The next node should be the attribute node
 assertEquals(nextNode.getLineNumber(), 1);
 assertEquals(nextNode.getColumnNumber(), 1);
 assertEquals(nextNode.getNodeName().getLocalPart(), "test");
 }

 @Test
 public void testNextNodeWithNonExistentAttribute() {
 // Given: A node without the attribute
 NodePointer nodeWithoutAttribute = new NodePointer(new Object(), new int[] {0});
 nodeWithoutAttribute.setLineNumber(1);
 nodeWithoutAttribute.setColumnNumber(1);
 parentContext.setCurrentNodePointer(nodeWithoutAttribute);
 attributeContext.setStarted = true;

 // When: Next node is called
 NodePointer nextNode = attributeContext.nextNode();

 // Then: The next node should be null
 assertNull(nextNode);
 }

 @Test
 public void testNextNodeWithDifferentAttributeName() {
 // Given: A node with an existing attribute, but with a different name
 NodePointer nodeWithAttribute = new NodePointer(new Object(), new int[] {0});
 nodeWithAttribute.setLineNumber(1);
 nodeWithAttribute.setColumnNumber(1);
 NodeIterator attributeIterator = new NodeIterator() {
 @Override
 public NodePointer getNodePointer() {
 return new NodePointer(new Object(), new int[] {0});
 }

 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public NodePointer next() {
 return new NodePointer(new Object(), new int[] {1});
 }

 @Override
 public NodeIterator attributeIterator(QName name) {
 return this;
 }
 };
 parentContext.setCurrentNodePointer(nodeWithAttribute);
 NodeTest differentAttributeName = new NodeNameTest(new QName("different"));
 AttributeContext differentAttributeContext = new AttributeContext(parentContext, differentAttributeName);
 differentAttributeContext.setStarted = true;
 differentAttributeContext.iterator = attributeIterator;

 // When: Next node is called
 NodePointer nextNode = differentAttributeContext.nextNode();

 // Then: The next node should be null
 assertNull(nextNode);
 }

 @Test
 public void testReset() {
 // Given: A context with an iterator
 NodePointer nodeWithAttribute = new NodePointer(new Object(), new int[] {0});
 nodeWithAttribute.setLineNumber(1);
 nodeWithAttribute.setColumnNumber(1);
 NodeIterator attributeIterator = new NodeIterator() {
 @Override
 public NodePointer getNodePointer() {
 return new NodePointer(new Object(), new int[] {1});
 }

 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public NodePointer next() {
 return new NodePointer(new Object(), new int[] {1});
 }

 @Override
 public NodeIterator attributeIterator(QName name) {
 return this;
 }
 };
 parentContext.setCurrentNodePointer(nodeWithAttribute);
 attributeContext.setStarted = true;
 attributeContext.iterator = attributeIterator;
 // When: Reset is called
 attributeContext.reset();
 // Then: The iterator should be null
 assertNull(attributeContext.iterator);
 }
}