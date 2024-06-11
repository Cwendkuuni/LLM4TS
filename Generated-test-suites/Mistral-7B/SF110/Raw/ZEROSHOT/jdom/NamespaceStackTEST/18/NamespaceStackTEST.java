package org.jdom2.util;

import org.jdom2.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class NamespaceStackTest {

 private NamespaceStack stack;
 private Element element;
 private Attribute attribute;

 @Before
 public void setUp() {
 Namespace xmlNamespace = new Namespace("xml", "http://www.w3.org/XML/1998/namespace");
 Namespace myNamespace = new Namespace("my", "http://www.example.com/my");
 Namespace ns1 = new Namespace("ns1", "http://www.example.com/ns1");
 Namespace ns2 = new Namespace("ns2", "http://www.example.com/ns2");

 stack = new NamespaceStack();
 element = new Element("root", null, null);
 attribute = new Attribute("attr", null, null, "http://www.example.com/attr");

 element.setNamespace(xmlNamespace);
 element.addNamespaceDeclaration("my", myNamespace);
 attribute.setNamespace(ns1);
 }

 @Test
 public void testEmptyStack() {
 Namespace[] expected = new Namespace[0];
 Namespace[] actual = stack.getScope();

 assertThat(actual, is(notNullValue()));
 assertThat(actual, is(Arrays.equal(expected)));
 }

 @Test
 public void testPushElement() {
 stack.push(element);
 Namespace[] expected = {xmlNamespace, myNamespace};
 Namespace[] actual = stack.getScope();

 assertThat(actual, is(Arrays.equal(expected)));
 }

 @Test
 public void testPushElementWithAdditionalNamespaces() {
 stack.push(element);
 element.setAdditionalNamespaces(Arrays.asList(ns1, ns2));
 stack.push(element);

 Namespace[] expected = {xmlNamespace, myNamespace, ns1, ns2};
 Namespace[] actual = stack.getScope();

 assertThat(actual, is(Arrays.equal(expected)));
 }

 @Test
 public void testPushAttribute() {
 stack.push(attribute);
 Namespace[] expected = {ns1};
 Namespace[] actual = stack.getScope();

 assertThat(actual, is(Arrays.equal(expected)));
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenEmpty() {
 stack.pop();
 }

 @Test
 public void testPop() {
 stack.push(element);
 stack.push(attribute);
 stack.pop();

 Namespace[] expected = {xmlNamespace, myNamespace};
 Namespace[] actual = stack.getScope();

 assertThat(actual, is(Arrays.equal(expected)));
 }

 @Test
 public void testAddedForward() {
 stack.push(element);
 stack.push(attribute);

 List<Namespace> expected = new ArrayList<>(Arrays.asList(ns1, myNamespace, xmlNamespace));
 Iterable<Namespace> actual = stack.addedForward();

 List<Namespace> actualList = new ArrayList<>();
 for (Namespace ns : actual) {
 actualList.add(ns);
 }

 assertThat(actualList, is(expected));
 }

 @Test
 public void testAddedReverse() {
 stack.push(element);
 stack.push(attribute);

 List<Namespace> expected = new ArrayList<>(Arrays.asList(xmlNamespace, myNamespace, ns1));
 Iterable<Namespace> actual = stack.addedReverse();

 List<Namespace> actualList = new ArrayList<>();
 for (Namespace ns : actual) {
 actualList.add(ns);
 }

 assertThat(actualList, is(expected));
 }

}