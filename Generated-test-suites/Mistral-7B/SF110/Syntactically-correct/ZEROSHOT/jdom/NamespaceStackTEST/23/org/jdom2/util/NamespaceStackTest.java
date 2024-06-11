import org.jdom2.Namespace;
import org.jdom2.internal.ArrayCopy;
import org.jdom2.util.NamespaceIterable;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {

 private NamespaceStack stack;
 private Element element;
 private Attribute attribute;

 @Before
 public void setUp() {
 Namespace mns = new Namespace("testPrefix", "testURI");
 element = new Element("testElement", mns);
 attribute = new Attribute("testAttribute", "testValue", mns);
 stack = new NamespaceStack();
 }

 @Test
 public void testEmptyStack() {
 assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
 assertFalse(stack.isInScope(mns));
 }

 @Test
 public void testPushElement() {
 stack.push(element);
 Namespace[] scope = stack.getScope();
 assertEquals(mns, scope[0]);
 assertTrue(stack.isInScope(mns));
 }

 @Test
 public void testPushElementWithAdditionalNamespaces() {
 Namespace additionalNamespace = new Namespace("additionalPrefix", "additionalURI");
 element.setAdditionalNamespaces(Collections.singletonList(additionalNamespace));
 stack.push(element);
 scope = stack.getScope();
 assertEquals(mns, scope[0]);
 assertEquals(additionalNamespace, scope[1]);
 assertTrue(stack.isInScope(mns));
 assertTrue(stack.isInScope(additionalNamespace));
 }

 @Test
 public void testPushElementWithAttributes() {
 stack.push(element);
 stack.push(attribute);
 scope = stack.getScope();
 assertEquals(mns, scope[0]);
 assertEquals(mns, scope[1]);
 assertTrue(stack.isInScope(mns));
 }

 @Test
 public void testPushAttribute() {
 stack.push(attribute);
 scope = stack.getScope();
 assertEquals(mns, scope[0]);
 assertTrue(stack.isInScope(mns));
 }

 @Test
 public void testPushMultipleElements() {
 Element element2 = new Element("testElement2", mns);
 stack.push(element);
 stack.push(element2);
 scope = stack.getScope();
 assertEquals(mns, scope[0]);
 assertEquals(mns, scope[1]);
 assertTrue(stack.isInScope(mns));
 }

 @Test
 public void testPop() {
 stack.push(element);
 stack.pop();
 scope = stack.getScope();
 assertFalse(stack.isInScope(mns));
 assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
 }

 @Test
 public void testIteratorForward() {
 stack.push(attribute);
 stack.push(element);
 Iterator<Namespace> iterator = stack.iterator();
 Namespace namespace;
 while (iterator.hasNext()) {
 namespace = iterator.next();
 System.out.println(namespace);
 }
 assertEquals(mns, namespace);
 }

 @Test
 public void testIteratorReverse() {
 stack.push(element);
 stack.push(attribute);
 Iterator<Namespace> iterator = stack.iterator();
 stack.reverseIteration();
 namespace = iterator.next();
 System.out.println(namespace);
 assertEquals(mns, namespace);
 }

 @Test
 public void testAddedForward() {
 stack.push(element);
 Iterable<Namespace> addedForward = stack.addedForward();
 Namespace namespace;
 for (Iterator<Namespace> iterator = addedForward.iterator(); iterator.hasNext(); ) {
 namespace = iterator.next();
 System.out.println(namespace);
 }
 assertEquals(mns, namespace);
 }

 @Test
 public void testAddedReverse() {
 stack.push(element);
 Iterable<Namespace> addedReverse = stack.addedReverse();
 Namespace namespace;
 for (Iterator<Namespace> iterator = addedReverse.iterator(); iterator.hasNext(); ) {
 namespace = iterator.next();
 System.out.println(namespace);
 }
 assertEquals(mns, namespace);
 }
}