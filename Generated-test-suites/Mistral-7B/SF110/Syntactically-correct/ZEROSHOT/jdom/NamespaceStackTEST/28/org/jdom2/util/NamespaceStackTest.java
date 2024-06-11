import org.jdom2.Namespace;
import org.jdom2.util.NamespaceStack;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(org.junit.runner.JUnit4.class)
@MethodSorts(MethodSorters.NAME_ASCENDING)
public class NamespaceStackTest {

 private NamespaceStack stack;
 private Element element;
 private Attribute attribute;

 @Before
 public void setUp() {
 Namespace namespace1 = new Namespace("http://www.w3.org/2001/XMLSchema", "xsd");
 Namespace namespace2 = new Namespace("http://www.w3.org/XML/1998/namespace", "xml");
 Namespace namespace3 = new Namespace("http://example.com/example", "ex");

 element = Mockito.spy(new Element("root"));
 element.setNamespace("http://www.w3.org/XML/1998/namespace");
 element.setName("root");
 element.addNamespaceDeclaration("xsd");
 element.addNamespaceDeclaration("ex");
 element.addAttribute("attr1", "value1", "xsd");
 element.addAttribute("attr2", "value2", "ex");

 attribute = Mockito.spy(new Attribute("attr", "value", "xsd"));

 stack = new NamespaceStack();
 }

 @Test
 public void testConstructor() {
 Namespace[] seed = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
 NamespaceStack stack = new NamespaceStack(seed);
 Assert.assertArrayEquals(seed, stack.getScope());
 }

 @Test
 public void testPushElement() {
 stack.push(element);
 List<Namespace> expected = new ArrayList<Namespace>();
 expected.add(namespace1);
 expected.add(namespace2);
 expected.add(namespace3);
 expected.addAll(Arrays.asList(element.getAdditionalNamespaces()));
 expected.addAll(Arrays.asList(element.getAttributes()).stream().map(a -> a.getNamespace()).collect(Collectors.toList()));
 Assert.assertEquals(expected.toArray(new Namespace[expected.size()]), stack.getAdded()[stack.getDepth()]);
 }

 @Test
 public void testPushAttribute() {
 stack.push(attribute);
 List<Namespace> expected = new ArrayList<Namespace>();
 expected.add(namespace3);
 Assert.assertEquals(expected.toArray(new Namespace[expected.size()]), stack.getAdded()[stack.getDepth()]);
 }

 @Test
 public void testPop() {
 stack.push(element);
 stack.pop();
 Assert.assertEquals(stack.getDepth(), -1);
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenEmptyStack() {
 stack.pop();
 }

 @Test
 public void testAddedForward() {
 stack.push(element);
 StackTraceElement[] trace = Thread.currentThread().getStackTrace();
 NamespaceIterable iterable = stack.addedForward();
 List<Namespace> expected = new ArrayList<Namespace>();
 expected.addAll(Arrays.asList(stack.getAdded()[stack.getDepth()]));
 for (int i = 1; i < trace.length; i++) {
 Namespace ns = iterable.next();
 expected.add(ns);
 }
 Assert.assertEquals(expected.toArray(new Namespace[expected.size()]), stack.getAdded()[stack.getDepth()]);
 }

 @Test
 public void testAddedReverse() {
 stack.push(element);
 StackTraceElement[] trace = Thread.currentThread().getStackTrace();
 NamespaceIterable iterable = stack.addedReverse();
 List<Namespace> expected = new ArrayList<Namespace>();
 expected.addAll(Arrays.asList(stack.getAdded()[stack.getDepth()]));
 for (int i = trace.length - 1; i >= 0; i--) {
 Namespace ns = iterable.next();
 expected.add(ns);
 }
 Assert.assertEquals(expected.toArray(new Namespace[expected.size()]), stack.getAdded()[stack.getDepth()]);
 }

 @Test
 public void testIsInScope() {
 stack.push(element);
 Assert.assertTrue(stack.isInScope(namespace1));
 Assert.assertTrue(stack.isInScope(namespace2));
 Assert.assertTrue(stack.isInScope(namespace3));
 Assert.assertTrue(stack.isInScope(element.getNamespace()));
 Assert.assertTrue(stack.isInScope(element.getAdditionalNamespaces().get(0)));
 Assert.assertTrue(stack.isInScope(element.getAttribute("attr1").getNamespace()));
 Assert.assertTrue(stack.isInScope(element.getAttribute("attr2").getNamespace()));
 }

 @Test
 public void testIsNotInScope() {
 stack.push(element);
 Assert.assertFalse(stack.isInScope(new Namespace("http://example.com/other", "other")));
 }

 @Test
 public void testIterator() {
 stack.push(element);
 Iterator<Namespace> iterator = stack.iterator();
 List<Namespace> expected = new ArrayList<Namespace>();
 expected.addAll(Arrays.asList(stack.getAdded()[stack.getDepth()]));
 while (iterator.hasNext()) {
 Namespace ns = iterator.next();
 expected.add(ns);
 }
 Assert.assertEquals(expected.toArray(new Namespace[expected.size()]), stack.getAdded()[stack.getDepth()]);
 }

 @Test
 public void testGetScope() {
 stack.push(element);
 Namespace[] expected = new Namespace[]{namespace1, namespace2, namespace3};
 Arrays.sort(expected);
 Assert.assertArrayEquals(expected, stack.getScope());
 }
}