import org.jdom2.Namespace;
import org.jdom2.util. NamespaceStack;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

 private static Namespace createNamespace(String prefix) {
 return new Namespace(prefix, null);
 }

 @Test
 public void testEmptyStack() {
 NamespaceStack stack = new NamespaceStack();
 Assert.assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
 Assert.assertFalse(stack.isInScope(createNamespace("test")));
 }

 @Test
 public void testPushAndPop() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(createElementWithNamespace("test"));
 stack.pop();
 Assert.assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
 }

 @Test
 public void testPushMultipleNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(createElementWithNamespace("test1"));
 stack.push(createElementWithNamespace("test2"));
 stack.push(createElementWithNamespace("test3"));

 Namespace[] expected = { createNamespace("test3"), createNamespace("test2"), createNamespace("test1") };
 Assert.assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testPushSameNamespaceMultipleTimes() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(createElementWithNamespace("test"));
 stack.push(createElementWithNamespace("test"));

 Namespace[] expected = { createNamespace("test"), createNamespace("test") };
 Assert.assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testPushMultipleNamespacesWithSamePrefixButDifferentUri() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(createElementWithNamespace("test1"));
 stack.push(createElementWithNamespace("test2"));

 Namespace test1 = createNamespace("test", "uri1");
 Namespace test2 = createNamespace("test", "uri2");
 Assert.assertArrayEquals(new Namespace[]{test1, test2}, stack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testPushWithNullElement() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(null);
 }

 @Test
 public void testPushWithElementWithAdditionalNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(createElementWithNamespaceAndAdditionalNamespaces("test1", createNamespace("test2")));

 Namespace[] expected = { createNamespace("test2"), createNamespace("test1") };
 Assert.assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testPushWithElementWithAttributes() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(createElementWithAttributes("test1", createAttributeWithNamespace("test2")));

 Namespace[] expected = { createNamespace("test2"), createNamespace("test1") };
 Assert.assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testPushWithElementWithMultipleAttributes() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(createElementWithAttributes("test1", createAttributeWithNamespace("test2"), createAttributeWithNamespace("test3")));

 Namespace[] expected = { createNamespace("test3"), createNamespace("test2"), createNamespace("test1") };
 Assert.assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testIsInScope() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(createElementWithNamespace("test"));

 Assert.assertTrue(stack.isInScope(createNamespace("test")));
 Assert.assertFalse(stack.isInScope(createNamespace("test2")));
 }

 @Test
 public void testIterator() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(createElementWithNamespace("test1"));
 stack.push(createElementWithNamespace("test2"));

 List<Namespace> expected = new ArrayList<Namespace>() {{
 add(createNamespace("test2"));
 add(createNamespace("test1"));
 }};

 List<Namespace> actual = new ArrayList<Namespace>(stack.iterator());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testAddedIterators() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(createElementWithNamespace("test1"));
 stack.push(createElementWithNamespace("test2"));

 List<Namespace> addedForward = new ArrayList<Namespace>(stack.addedForward());
 List<Namespace> addedReverse = new ArrayList<Namespace>(stack.addedReverse());

 List<Namespace> expected = new ArrayList<Namespace>() {{
 add(createNamespace("test1"));
 add(createNamespace("test2"));
 }};

 Assert.assertEquals(expected, addedForward);
 Assert.assertEquals(expected, addedReverse);
 }

 private Element createElementWithNamespace(String prefix) {
 return new Element("test", createNamespace("test"));
 }

 private Element createElementWithNamespaceAndAdditionalNamespaces(String prefix, Namespace additionalNamespace) {
 return new Element("test", createNamespace("test"), new Namespace[]{ additionalNamespace });
 }

 private Attribute createAttributeWithNamespace(String prefix) {
 return new Attribute("test", createNamespace("test"));
 }

}