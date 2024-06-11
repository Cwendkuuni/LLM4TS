package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.rhino.NodeTraversal;

import java.util.List;

/**
 * Tests for the ClosureCodingConvention class.
 */
public class ClosureCodingConventionTest {

  /**
   * Test that the getclasssDefinedByCall method identifies the correct
   * inheritance relationship between classes.
   */
  public void testGetclasssDefinedByCall() {
    Node node = Node.newNode("goog.inherits(MySubClass, SuperClass)");
    NodeTraversal traversal = new NodeTraversal(node);
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    SubclassRelationship relationship = codingConvention.getclasssDefinedByCall(node, null);
    assertNotNull(relationship);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("MySubClass", relationship.getSubclassName());
    assertEquals("SuperClass", relationship.getSuperclassName());
  }

  /**
   * Test that the getclasssDefinedByCall method does not identify an
   * inheritance relationship when the method does not define one.
   */
  public void testGetclasssDefinedByCall_NoRelationship() {
    Node node = Node.newNode("MySubClass.inherits(SuperClass)");
    NodeTraversal traversal = new NodeTraversal(node);
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    SubclassRelationship relationship = codingConvention.getclasssDefinedByCall(node, null);
    assertNull(relationship);
  }

  /**
   * Test that the getclasssDefinedByCall method identifies the correct
   * inheritance relationship between a mixin and its base class.
   */
  public void testGetclasssDefinedByCall_Mixin() {
    Node node = Node.newNode("MySubClass.mixin(SuperClass.prototype)");
    NodeTraversal traversal = new NodeTraversal(node);
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    SubclassRelationship relationship = codingConvention.getclasssDefinedByCall(node, null);
    assertNotNull(relationship);
    assertEquals(SubclassType.MIXIN, relationship.getType());
    assertEquals("MySubClass", relationship.getSubclassName());
    assertEquals("SuperClass", relationship.getSuperclassName());
  }

  /**
   * Test that the getclasssDefinedByCall method does not identify an
   * inheritance relationship when the method does not define a mixin.
   */
  public void testGetclasssDefinedByCall_NoMixin() {
    Node node = Node.newNode("MySubClass.mixin(MyOtherClass.prototype)");
    NodeTraversal traversal = new NodeTraversal(node);
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    SubclassRelationship relationship = codingConvention.getclasssDefinedByCall(node, null);
    assertNull(relationship);
  }

  /**
   * Test that the applySubclassRelationship method adds the expected
   * properties to the child class when using the INHERITS relationship.
   */
  public void testApplySubclassRelationship_Inherits() {
    FunctionType parentCtor = new FunctionType("SuperClass", null, null);
    FunctionType childCtor = new FunctionType("MySubClass", null, null);
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    codingConvention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    ObjectType prototype = childCtor.getPrototype();
    assertNotNull(prototype);
    assertEquals("superClass_", prototype.getDeclaredPropertyNames().get(0));
    assertEquals(parentCtor, prototype.getDeclaredProperty("superClass_").getValue());
    assertEquals("constructor", prototype.getDeclaredPropertyNames().get(1));
    assertEquals(childCtor, prototype.getDeclaredProperty("constructor").getValue());
  }

  /**
   * Test that the applySubclassRelationship method does not add any
   * properties to the child class when using the MIXIN relationship.
   */
  public void testApplySubclassRelationship_Mixin() {
    FunctionType parentCtor = new FunctionType("SuperClass", null, null);
    FunctionType childCtor = new FunctionType("MySubClass", null, null);
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    codingConvention.applySubclassRelationship(parentCtor, childCtor, SubclassType.MIXIN);

    ObjectType prototype = childCtor.getPrototype();
    assertNotNull(prototype);
    assertEquals(0, prototype.getDeclaredPropertyNames().size());
  }

  /**
   * Test that the extractClassNameIfProvide method extracts the correct
   * class name from a goog.provide statement.
   */
  public void testExtractClassNameIfProvide() {
    Node node = Node.newNode("goog.provide('MyModule')");
    String className = new ClosureCodingConvention().extractClassNameIfProvide(node, null);
    assertEquals("MyModule", className);
  }

  /**
   * Test that the extractClassNameIfRequire method extracts the correct
   * class name from a goog.require statement.
   */
  public void testExtractClassNameIfRequire() {
    Node node = Node.newNode("goog.require('MyModule')");
    String className = new ClosureCodingConvention().extractClassNameIfRequire(node, null);
    assertEquals("MyModule", className);
  }

  /**
   * Test that the extractClassNameIfGoog method extracts the correct
   * class name from a goog.addSingletonGetter statement.
   */
  public void testExtractClassNameIfGoog_SingletonGetter() {
    Node node = Node.newNode("goog.addSingletonGetter(MyClass, 'myProperty')");
    String className = new ClosureCodingConvention().extractClassNameIfGoog(node, null, "goog.addSingletonGetter");
    assertEquals("MyClass", className);
  }

  /**
   * Test that the extractClassNameIfGoog method extracts the correct
   * class name from a goog.exportSymbol statement.
   */
  public void testExtractClassNameIfGoog_ExportSymbol() {
    Node node = Node.newNode("goog.exportSymbol('MyModule.mySymbol', MyClass.prototype.myProperty)");
    String className = new ClosureCodingConvention().extractClassNameIfGoog(node, null, "goog.exportSymbol");
    assertEquals("MyClass", className);
  }

  /**
   * Test that the extractClassNameIfGoog method returns null when given
   * a non-goog statement.
   */
  public void testExtractClassNameIfGoog_NoneGoog() {
    Node node = Node.newNode("MyClass.myProperty = 42");
    String className = new ClosureCodingConvention().extractClassNameIfGoog(node, null, "goog.exportSymbol");
    assertNull(className);
  }

  /**
   * Test that the applySingletonGetter method sets the expected properties
   * on the class.
   */
  public void testApplySingletonGetter() {
    FunctionType functionType = new FunctionType("MyClass", null, null);
    ObjectType objectType = new ObjectType("MyClass");
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    codingConvention.applySingletonGetter(functionType, objectType, null);

    assertEquals("getInstance", functionType.getDeclaredPropertyNames().get(0));
    assertEquals("instance_", functionType.getDeclaredPropertyNames().get(1));
  }

  /**
   * Test that the getSingletonGetterClassName method extracts the correct
   * class name from a goog.addSingletonGetter statement.
   */
  public void testGetSingletonGetterClassName() {
    Node node = Node.newNode("goog.addSingletonGetter('MyClass.myProperty', MyClass.prototype.myProperty)");
    String className = new ClosureCodingConvention().getSingletonGetterClassName(node, null);
    assertEquals("MyClass", className);
  }

  /**
   * Test that the isPropertyTestFunction method identifies the correct
   * property test functions.
   */
  public void testIsPropertyTestFunction() {
    Node node = Node.newNode("goog.isDef(x)");
    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(node));
  }

  /**
   * Test that the getObjectLiteralCast method extracts the correct cast
   * information from a goog.reflect.object call.
   */
  public void testGetObjectLiteralCast() {
    Node node = Node.newNode("goog.reflect.object.cast(Object, x)");
    ObjectLiteralCast cast = new ClosureCodingConvention().getObjectLiteralCast(new NodeTraversal(node.getParentNode()), node);
    assertEquals("Object", cast.getTypeName());
    assertEquals("x", cast.getExpression().toString());
  }
}