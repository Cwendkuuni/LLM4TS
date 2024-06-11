import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.QName;
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
 nodeTest = new NodeNameTest(new QName("test:attr"));
 attributeContext = new AttributeContext(parentContext, nodeTest);
 }

 @Test
 public void testNextNodeAttributeContext() {
 NodeIterator attributeIterator = new NodeIterator() {
 @Override
 public NodePointer nodePointerAt(int position) {
 switch (position) {
 case 0:
 return new NodePointer(new String[]{"root", "element1", "element1", "attr1"});
 case 1:
 return new NodePointer(new String[]{"root", "element1", "element1", "attr2"});
 case 2:
 return new NodePointer(new String[]{"root", "element1", "element2", "attr1"});
 default:
 return null;
 }
 }
 };
 parentContext.setNodeIterator(attributeIterator);

 attributeContext.nextNode();
 assertEquals("element1", attributeContext.getCurrentNodePointer().getLocalName());
 assertEquals("attr1", attributeContext.getCurrentNodePointer().getNodeName().getLocalPart());
 assertEquals("attr1Value1", attributeContext.getCurrentNodePointer().getNodeValue());

 attributeContext.nextNode();
 assertEquals("element1", attributeContext.getCurrentNodePointer().getParent().getLocalName());
 assertEquals("attr2", attributeContext.getCurrentNodePointer().getNodeName().getLocalPart());
 assertEquals("attr2Value1", attributeContext.getCurrentNodePointer().getNodeValue());

 attributeContext.nextNode();
 assertEquals("element2", attributeContext.getCurrentNodePointer().getParent().getLocalName());
 assertEquals("attr1", attributeContext.getCurrentNodePointer().getNodeName().getLocalPart());
 assertEquals("attr1Value2", attributeContext.getCurrentNodePointer().getNodeValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testNextNodeAttributeContextWithNoAttributes() {
 NodeIterator attributeIterator = new NodeIterator() {
 @Override
 public NodePointer nodePointerAt(int position) {
 switch (position) {
 case 0:
 return new NodePointer(new String[]{"root", "element1"});
 default:
 return null;
 }
 }
 };
 parentContext.setNodeIterator(attributeIterator);
 attributeContext.nextNode();
 attributeContext.nextNode();
 attributeContext.nextNode(); // this should throw an exception
 }

 @Test(expected = IllegalStateException.class)
 public void testNextNodeAttributeContextAfterEndOfIterator() {
 NodeIterator attributeIterator = new NodeIterator() {
 @Override
 public NodePointer nodePointerAt(int position) {
 switch (position) {
 case 0:
 return new NodePointer(new String[]{"root", "element1", "element1", "attr1"});
 default:
 return null;
 }
 }
 };
 parentContext.setNodeIterator(attributeIterator);
 attributeContext.nextNode();
 attributeContext.nextNode();
 parentContext.setNodeIterator(null);
 attributeContext.nextNode(); // this should throw an exception
 }

 @Test
 public void testSetPositionAttributeContext() {
 NodeIterator attributeIterator = new NodeIterator() {
 @Override
 public NodePointer nodePointerAt(int position) {
 switch (position) {
 case 0:
 return new NodePointer(new String[]{"root", "element1", "element1", "attr1"});
 case 1:
 return new NodePointer(new String[]{"root", "element1", "element1", "attr2"});
 default:
 return null;
 }
 }
 };
 parentContext.setNodeIterator(attributeIterator);

 attributeContext.setPosition(2);
 assertEquals("element1", attributeContext.getCurrentNodePointer().getParent().getLocalName());
 assertEquals("element2", attributeContext.getCurrentNodePointer().getLocalName());
 assertEquals("attr1", attributeContext.getCurrentNodePointer().getPreviousNodePointer().getLocalName());
 assertEquals("attr1Value1", attributeContext.getCurrentNodePointer().getPreviousNodePointer().getNodeValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionAttributeContextBeforeStart() {
 NodeIterator attributeIterator = new NodeIterator() {
 @Override
 public NodePointer nodePointerAt(int position) {
 switch (position) {
 case 0:
 return new NodePointer(new String[]{"root", "element1", "element1", "attr1"});
 default:
 return null;
 }
 }
 };
 parentContext.setNodeIterator(attributeIterator);
 attributeContext.setPosition(-1); // this should throw an exception
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionAttributeContextAfterEnd() {
 NodeIterator attributeIterator = new NodeIterator() {
 @Override
 public NodePointer nodePointerAt(int position) {
 switch (position) {
 case 0:
 return new NodePointer(new String[]{"root", "element1", "element1", "attr1"});
 case 1:
 return new NodePointer(new String[]{"root", "element1", "element1", "attr2"});
 default:
 return null;
 }
 }
 };
 parentContext.setNodeIterator(attributeIterator);
 attributeContext.setPosition(3); // this should throw an exception
 }
}