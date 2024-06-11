package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = createFunctionType("ParentClass");
    FunctionType childCtor = createFunctionType("ChildClass");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    assertEquals("ParentClass", childCtor.getPrototype().getClass().getName());
    assertEquals("ParentClass", childCtor.getPrototype().get("constructor").getName());

    ConventionTestUtil.assertHasProperty(childCtor, "superClass_", parentCtor);
  }

  @Test
  public void testApplySubclassRelationshipMixin() {
    FunctionType parentCtor = createFunctionType("ParentClass");
    FunctionType childCtor = createFunctionType("ChildClass");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, SubclassType.MIXIN);

    assertEquals("ParentClass", childCtor.getPrototype().getClass().getName());
    assertEquals("ParentClass", childCtor.getPrototype().get("constructor").getName());

    ConventionTestUtil.assertHasProperty(childCtor, "superClass_", parentCtor);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplySubclassRelationshipInvalidCall() {
    FunctionType parentCtor = createFunctionType("ParentClass");
    FunctionType childCtor = createFunctionType("ChildClass");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, null);
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node node = createTestCallNode("goog.inherits(ChildClass, ParentClass)");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(node);

    assertNotNull(relationship);
    assertEquals(SubclassType.INHERITS, relationship.getRelationType());
    assertEquals("ChildClass", relationship.getChildName());
    assertEquals("ParentClass", relationship.getParentName());
  }

  @Test
  public void testGetclasssDefinedByCallWithPrototype() {
    Node node = createTestCallNode("goog.inherits(ChildClass.prototype, ParentClass.prototype)");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(node);

    assertNotNull(relationship);
    assertEquals(SubclassType.MIXIN, relationship.getRelationType());
    assertEquals("ChildClass.prototype", relationship.getChildName());
    assertEquals("ParentClass.prototype", relationship.getParentName());
  }

  @Test
  public void testGetclasssDefinedByCallWithDeprecatedCall() {
    Node node = createTestCallNode("ChildClass.inherits(ParentClass)");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(node);

    assertNotNull(relationship);
    assertEquals(SubclassType.INHERITS, relationship.getRelationType());
    assertEquals("ChildClass", relationship.getChildName());
    assertEquals("ParentClass", relationship.getParentName());
  }

  @Test
  public void testGetclasssDefinedByCallWithMixinCall() {
    Node node = createTestCallNode("goog.mixin(ChildClass.prototype, ParentClass.prototype)");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(node);

    assertNotNull(relationship);
    assertEquals(SubclassType.MIXIN, relationship.getRelationType());
    assertEquals("ChildClass.prototype", relationship.getChildName());
    assertEquals("ParentClass.prototype", relationship.getParentName());
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertFalse(convention.isSuperClassReference("foo"));
    assertTrue(convention.isSuperClassReference("superClass_"));
  }

  @Test
  public void testExtracClassNameIfProvide() {
    Node node = createTestCallNode("goog.provide('MyClass')");

    ClosureCodingConvention convention = new ClosureCodingConvention();

    String className = convention.extractClassNameIfProvide(node, null);

    assertEquals("MyClass", className);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = createTestCallNode("goog.require('MyClass')");

    ClosureCodingConvention convention = new ClosureCodingConvention();

    String className = convention.extractClassNameIfRequire(node, null);

    assertEquals("MyClass", className);
  }

  @Test
  public void testExtractClassNameIfGoog() {
    Node node = createTestCallNode("goog.provide('MyClass')");
    node = node.getNext();

    ClosureCodingConvention convention = new ClosureCodingConvention();

    String className = convention.extractClassNameIfGoog(node, null, "goog.addDependency");

    assertEquals("MyClass", className);
  }

  @Test
  public void testGetExportPropertyFunction() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    String functionName = convention.getExportPropertyFunction();

    assertEquals("goog.exportProperty", functionName);
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node node = createTestCallNode("goog.addSingletonGetter(MyClass, 'myProperty')");

    ClosureCodingConvention convention = new ClosureCodingConvention();

    String className = convention.getSingletonGetterClassName(node);

    assertEquals("MyClass", className);
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = createFunctionType("MyClass");
    FunctionType getterType = createFunctionType("MySingletonGetter");
    ObjectType objectType = createObjectType("MyObject");

    ClosureCodingConvention convention = new ClosureCodingConvention();

    convention.applySingletonGetter(functionType, getterType, objectType);

    assertEquals(getterType, functionType.getPrototype().get("getInstance"));
    assertEquals(objectType, functionType.getPrototype().get("instance_"));
  }

  @Test
  public void testIsPropertyTestFunction() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node callNode = createTestCallNode("goog.isNumber(42)");

    assertTrue(convention.isPropertyTestFunction(callNode));
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node callNode = createTestCallNode("goog.reflect.object(myObject)");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    ObjectLiteralCast cast = convention.getObjectLiteralCast(null, callNode);

    assertNotNull(cast);
    assertEquals("goog.reflect.object", cast.getTypeName());
    assertEquals("myObject", cast.getCastedName());
  }

  @Test(expected = RuntimeException.class)
  public void testAssertThrowsWithIllegalArgumentException() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node callNode = createTestCallNode("goog.asserts.assert(null, 'test')");

    assertThrows(IllegalArgumentException.class, () -> {
      convention.assertThrows(callNode, IllegalArgumentException.class);
    });
  }

  @Test
  public void testAssertThrowsWithAssertionError() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node callNode = createTestCallNode("goog.asserts.assert(42, 'test')");

    assertThrows(AssertionError.class, () -> {
      convention.assertThrows(callNode, AssertionError.class);
    });
  }

  private FunctionType createFunctionType(String name) {
    FunctionType type = new FunctionType(name, null);
    type.setSourceName("TestSourceFile.js");
    return type;
  }

  private ObjectType createObjectType(String name) {
    ObjectType type = new ObjectType(name, null, null);
    type.setSourceName("TestSourceFile.js");
    return type;
  }

  private Node createTestCallNode(String callString) {
    Node n = new Node();
    n.setType(Token.CALL);
    Node callNode = new Node();
    callNode.setType(Token.GETPROP);
    callNode.setString(callString);
    n.addChild(callNode);
    Node firstChild = new Node();
    firstChild.setType(Token.STRING);
    firstChild.setString("goog");
    n.addChild(firstChild);
    Node nextChild = new Node();
    nextChild.setType(Token.GETPROP);
    nextChild.setString(callString.substring(0, callString.lastIndexOf('.')));
    n.addChild(nextChild);
    Node lastChild = new Node();
    lastChild.setType(Token.STRING);
    lastChild.setString(callString.substring(callString.lastIndexOf('.') + 1));
    n.addChild(lastChild);
    return n;
  }

  private static class ConventionTestUtil {
    static void assertHasProperty(FunctionType functionType, String propertyName,
        Node expected) {
      Node propertyNode = functionType.getPrototype().get(propertyName);
      assertNotNull(propertyNode);
      assertEquals(expected.getQualifiedName(), propertyNode.getQualifiedName());
    }
  }

  private enum SubclassType {
    INHERITS,
    MIXIN
  }
}