import org.jdom2.Namespace;
import org.jdom2.internal.ArrayCopy;
import org.jdom2.JDOMException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NamespaceStackTest {

 @Test
 public void testNamespaceStackEmpty() {
 NamespaceStack stack = new NamespaceStack();
 assertTrue(stack.isEmpty());
 assertNull(stack.getScope());
 assertNull(stack.addedForward().iterator().next());
 assertNull(stack.addedReverse().iterator().next());
 }

 @Test
 public void testNamespaceStackPushPop() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 Namespace[] expectedScope = new Namespace[] { Namespace.XML_NAMESPACE };
 Namespace[] expectedAdded = new Namespace[] { Namespace.XML_NAMESPACE };
 assertArrayEquals(expectedScope, stack.getScope());
 assertArrayEquals(expectedAdded, stack.addedForward().toArray(new Namespace[0]));
 assertEquals(1, stack.getSize());
 stack.pop();
 assertTrue(stack.isEmpty());
 assertArrayEquals(new Namespace[0][], stack.getScope());
 assertArrayEquals(new Namespace[0][], stack.addedForward().toArray(new Namespace[0]));
 }

 @Test
 public void testNamespaceStackPushMultiple() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.push(new Element("element", Namespace.NS_A));
 stack.push(new Element("element2", Namespace.NS_B));
 Namespace[] expectedScope = new Namespace[] { Namespace.NS_B, Namespace.NS_A, Namespace.XML_NAMESPACE };
 Namespace[] expectedAdded = new Namespace[] { Namespace.NS_B, Namespace.NS_A, Namespace.XML_NAMESPACE };
 assertArrayEquals(expectedScope, stack.getScope());
 assertArrayEquals(expectedAdded, stack.addedForward().toArray(new Namespace[0]));
 assertEquals(3, stack.getSize());
 }

 @Test
 public void testNamespaceStackPushSameNamespace() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 Namespace[] expectedScope = new Namespace[] { Namespace.XML_NAMESPACE };
 Namespace[] expectedAdded = new Namespace[] { Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE };
 assertArrayEquals(expectedScope, stack.getScope());
 assertArrayEquals(expectedAdded, stack.addedForward().toArray(new Namespace[0]));
 assertEquals(2, stack.getSize());
 }

 @Test
 public void testNamespaceStackPushMultipleNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.push(new Element("elementA", Namespace.NS_A));
 stack.push(new Element("elementB", Namespace.NS_B));
 stack.push(new Element("elementC", Namespace.NS_C));
 Namespace[] expectedScope = new Namespace[] { Namespace.NS_C, Namespace.NS_B, Namespace.NS_A, Namespace.XML_NAMESPACE };
 Namespace[] expectedAdded = new Namespace[] { Namespace.NS_C, Namespace.NS_B, Namespace.NS_A, Namespace.XML_NAMESPACE };
 assertArrayEquals(expectedScope, stack.getScope());
 assertArrayEquals(expectedAdded, stack.addedForward().toArray(new Namespace[0]));
 assertEquals(4, stack.getSize());
 }

 @Test
 public void testNamespaceStackPushElementWithAdditionalNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.push(new Element("element", Namespace.NS_A));
 stack.push(new Element("element2", Namespace.NS_B));
 stack.push(new Element("element3", Namespace.NS_C, new Namespace[] { Namespace.NS_D, Namespace.NS_E }));
 Namespace[] expectedScope = new Namespace[] { Namespace.NS_E, Namespace.NS_D, Namespace.NS_C, Namespace.NS_B, Namespace.NS_A, Namespace.XML_NAMESPACE };
 Namespace[] expectedAdded = new Namespace[] { Namespace.NS_E, Namespace.NS_D, Namespace.NS_C, Namespace.NS_B, Namespace.NS_A, Namespace.XML_NAMESPACE };
 assertArrayEquals(expectedScope, stack.getScope());
 assertArrayEquals(expectedAdded, stack.addedForward().toArray(new Namespace[0]));
 assertEquals(5, stack.getSize());
 }

 @Test
 public void testNamespaceStackPushAttribute() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.push(new Attribute("attr", "value", Namespace.NS_A));
 Namespace[] expectedScope = new Namespace[] { Namespace.NS_A, Namespace.XML_NAMESPACE };
 Namespace[] expectedAdded = new Namespace[] { Namespace.NS_A, Namespace.XML_NAMESPACE };
 assertArrayEquals(expectedScope, stack.getScope());
 assertArrayEquals(expectedAdded, stack.addedForward().toArray(new Namespace[0]));
 assertEquals(2, stack.getSize());
 }

 @Test
 public void testNamespaceStackPushAttributeSameNamespace() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.push(new Attribute("attr1", "value1", Namespace.XML_NAMESPACE));
 stack.push(new Attribute("attr2", "value2", Namespace.XML_NAMESPACE));
 Namespace[] expectedScope = new Namespace[] { Namespace.XML_NAMESPACE };
 Namespace[] expectedAdded = new Namespace[] { Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE };
 assertArrayEquals(expectedScope, stack.getScope());
 assertArrayEquals(expectedAdded, stack.addedForward().toArray(new Namespace[0]));
 assertEquals(3, stack.getSize());
 }

 @Test
 public void testNamespaceStackPushAttributeDifferentNamespace() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.push(new Attribute("attr1", "value1", Namespace.NS_A));
 stack.push(new Attribute("attr2", "value2", Namespace.NS_B));
 Namespace[] expectedScope = new Namespace[] { Namespace.NS_B, Namespace.NS_A, Namespace.XML_NAMESPACE };
 Namespace[] expectedAdded = new Namespace[] { Namespace.NS_B, Namespace.NS_A, Namespace.XML_NAMESPACE };
 assertArrayEquals(expectedScope, stack.getScope());
 assertArrayEquals(expectedAdded, stack.addedForward().toArray(new Namespace[0]));
 assertEquals(3, stack.getSize());
 }

 @Test
 public void testNamespaceStackPushAttributeSamePrefixDifferentUris() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.push(new Attribute("attr1", "value1", Namespace.getNamespace("http://test.com/ns1")));
 stack.push(new Attribute("attr2", "value2", Namespace.getNamespace("http://test.com/ns2")));
 Namespace[] expectedScope = new Namespace[] { Namespace.getNamespace("http://test.com/ns2"), Namespace.getNamespace("http://test.com/ns1"), Namespace.XML_NAMESPACE };
 Namespace[] expectedAdded = new Namespace[] { Namespace.getNamespace("http://test.com/ns2"), Namespace.getNamespace("http://test.com/ns1"), Namespace.XML_NAMESPACE };
 assertArrayEquals(expectedScope, stack.getScope());
 assertArrayEquals(expectedAdded, stack.addedForward().toArray(new Namespace[0]));
 assertEquals(3, stack.getSize());
 }

 @Test (expected = JDOMException.class)
 public void testNamespaceStackPushNullElement() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(null);
 fail("Should throw JDOMException for null Element");
 }

 @Test (expected = JDOMException.class)
 public void testNamespaceStackPushNullAttribute() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.push(null);
 fail("Should throw JDOMException for null Attribute");
 }
}