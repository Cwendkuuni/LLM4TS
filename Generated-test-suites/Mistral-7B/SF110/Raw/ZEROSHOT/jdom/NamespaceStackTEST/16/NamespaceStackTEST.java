package org.jdom2.util;

import org.jdom2.internal.Namespaces;
import org.jdom2.Namespace;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.arrayContaining;

public class NamespaceStackTest {

 private NamespaceStack namespaceStack;
 private Element element;
 private Attribute attribute;

 @Before
 public void setUp() {
 namespaceStack = new NamespaceStack();
 element = new Element("root");
 attribute = new Attribute("namespace", "test", "urn:test");
 }

 @Test
 public void testEmptyConstructor() {
 NamespaceStack emptyStack = new NamespaceStack();
 Assert.assertThat(emptyStack.getScope(), is(equalTo(NamespaceStack.DEFAULTSEED)));
 }

 @Test
 public void testConstructorWithSeed() {
 Namespace[] seed = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
 NamespaceStack stackWithSeed = new NamespaceStack(seed);
 Assert.assertThat(stackWithSeed.getScope(), is(equalTo(seed)));
 }

 @Test
 public void testPushElement() {
 namespaceStack.push(element);
 List<Namespace> added = Arrays.asList(namespaceStack.addedForward());
 Assert.assertThat(added, is(arrayContaining(element.getNamespace())));
 }

 @Test
 public void testPushAttribute() {
 namespaceStack.push(attribute);
 List<Namespace> added = Arrays.asList(namespaceStack.addedForward());
 Assert.assertThat(added, is(arrayContaining(attribute.getNamespace())));
 }

 @Test
 public void testPushMultipleNamespaces() {
 Element elementWithNamespaces = new Element("root");
 elementWithNamespaces.setName("{urn:test}root");
 namespaceStack.push(elementWithNamespaces);
 Attribute attributeWithNamespaces = new Attribute("namespace", "test2", "urn:test2");
 namespaceStack.push(attributeWithNamespaces);
 List<Namespace> added = Arrays.asList(namespaceStack.addedForward());
 Assert.assertThat(added, is(arrayContaining(elementWithNamespaces.getNamespace())));
 Assert.assertThat(added, is(arrayContaining(attributeWithNamespaces.getNamespace())));
 }

 @Test
 public void testPop() {
 namespaceStack.push(element);
 List<Namespace> added = Arrays.asList(namespaceStack.addedForward());
 Assert.assertThat(added, is(arrayContaining(element.getNamespace())));
 namespaceStack.pop();
 Assert.assertThat(namespaceStack.addedForward(), is(emptyIterable()));
 }

 @Test
 public void testGetScope() {
 namespaceStack.push(element);
 List<Namespace> added = Arrays.asList(namespaceStack.addedForward());
 Namespace[] scope = namespaceStack.getScope();
 Assert.assertThat(scope, is(equalTo(added.toArray(new Namespace[added.size()]))));
 }

 @Test
 public void testIsInScope() {
 namespaceStack.push(element);
 Assert.assertTrue(namespaceStack.isInScope(element.getNamespace()));
 }

 @Test
 public void testIterator() {
 namespaceStack.push(element);
 Iterator<Namespace> iterator = namespaceStack.iterator();
 Assert.assertTrue(iterator.hasNext());
 Namespace namespace = iterator.next();
 Assert.assertThat(namespace, is(equalTo(element.getNamespace())));
 }

 @Test
 public void testAddedForward() {
 namespaceStack.push(element);
 List<Namespace> addedForward = Arrays.asList(namespaceStack.addedForward().toArray(new Namespace[namespaceStack.addedForward().size()]));
 List<Namespace> added = Arrays.asList(namespaceStack.addedForward());
 Assert.assertThat(addedForward, is(equalTo(added)));
 }

 @Test
 public void testAddedReverse() {
 namespaceStack.push(element);
 List<Namespace> addedReverse = Arrays.asList(namespaceStack.addedReverse().toArray(new Namespace[namespaceStack.addedReverse().size()]));
 List<Namespace> added = Arrays.asList(namespaceStack.addedForward());
 Collections.reverse(added);
 Assert.assertThat(addedReverse, is(equalTo(added)));
 }
}