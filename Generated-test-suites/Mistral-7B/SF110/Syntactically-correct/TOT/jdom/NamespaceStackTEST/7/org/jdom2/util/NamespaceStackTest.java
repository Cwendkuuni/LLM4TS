package org.jdom2.util;

import junit.framework.TestCase;
import org.jdom2.*;

import java.util.ArrayList;
import java.util.List;

public class NamespaceStackTest extends TestCase {

 private NamespaceStack stack;

 @Before
 public void setUp() {
 Element element = new Element("root");
 stack = new NamespaceStack();
 stack.push(element);
 }

 @Test
 public void testPushElement() {
 Element element = new Element("test");
 Namespace mns = new Namespace("testPrefix", "");
 Namespace[] newscope = new Namespace[2];
 newscope[0] = mns;
 stack.push(element);
 Namespace[] expectedScope = new Namespace[2];
 expectedScope[0] = mns;
 expectedScope[1] = Namespace.XML_NAMESPACE;
 assertEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushMultipleNamespacesElement() {
 Element element = new Element("root");
 element.setNamespacePrefix("testPrefix");
 stack.push(element);
 Namespace mns = new Namespace("testPrefix2", "");
 Namespace[] newscope = new Namespace[3];
 newscope[0] = mns;
 stack.push(element);
 Namespace[] expectedScope = new Namespace[3];
 expectedScope[0] = mns;
 expectedScope[1] = Namespace.XML_NAMESPACE;
 expectedScope[2] = Namespace.NO_NAMESPACE;
 assertEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushMultipleNamespacesAttribute() {
 Attribute att = new Attribute("test", "testValue");
 Namespace mns = new Namespace("testPrefix", "");
 Namespace[] newscope = new Namespace[2];
 newscope[0] = mns;
 stack.push(att);
 Namespace[] expectedScope = new Namespace[2];
 expectedScope[0] = mns;
 expectedScope[1] = Namespace.NO_NAMESPACE;
 assertEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushMultipleNamespacesAttributeMultiplePrefixes() {
 Attribute att = new Attribute("test", "testValue");
 Namespace mns1 = new Namespace("testPrefix1", "");
 Namespace mns2 = new Namespace("testPrefix2", "");
 Namespace[] newscope = new Namespace[3];
 newscope[0] = mns1;
 stack.push(att);
 Namespace[] expectedScope = new Namespace[3];
 expectedScope[0] = mns1;
 expectedScope[1] = mns2;
 expectedScope[2] = Namespace.NO_NAMESPACE;
 stack.push(att);
 assertEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushAndPop() {
 Element element = new Element("root");
 stack.push(element);
 Namespace mns = new Namespace("testPrefix", "");
 stack.push(element);
 stack.push(mns);
 stack.pop();
 Namespace[] expectedScope = new Namespace[2];
 expectedScope[0] = Namespace.XML_NAMESPACE;
 expectedScope[1] = Namespace.NO_NAMESPACE;
 assertEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushAndPopMultipleNamespaces() {
 Element element = new Element("root");
 stack.push(element);
 Namespace mns1 = new Namespace("testPrefix1", "");
 stack.push(element);
 stack.push(mns1);
 Namespace mns2 = new Namespace("testPrefix2", "");
 stack.push(mns1);
 stack.pop();
 Namespace[] expectedScope = new Namespace[2];
 expectedScope[0] = Namespace.XML_NAMESPACE;
 expectedScope[1] = Namespace.NO_NAMESPACE;
 assertEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushEmptyList() {
 Element element = new Element("root");
 stack.push(element);
 stack.push(new ArrayList<Namespace>());
 Namespace[] expectedScope = new Namespace[2];
 expectedScope[0] = Namespace.XML_NAMESPACE;
 expectedScope[1] = Namespace.NO_NAMESPACE;
 assertEquals(expectedScope, stack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testPopEmptyStack() {
 stack.pop();
 }

 @After
 public void tearDown() {
 stack = null;
 }

}