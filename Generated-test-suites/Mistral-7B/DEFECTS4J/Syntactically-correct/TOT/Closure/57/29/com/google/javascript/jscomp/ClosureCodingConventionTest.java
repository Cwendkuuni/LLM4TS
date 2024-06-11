package com.google.javascript.jscomp.test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.Token;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType(
        "ParentConstructor", ImmutableList.of(), ObjectType.OBJECT);
    FunctionType childCtor = new FunctionType(
        "ChildConstructor", ImmutableList.of(), ObjectType.OBJECT);
    SubclassType relationship = new ClosureCodingConvention().applySubclassRelationship(
        parentCtor, childCtor, SubclassType.INHERITS);
    assertEquals(SubclassType.INHERITS, relationship);

    // Test with 'mixin' relationship
    relationship = new ClosureCodingConvention().applySubclassRelationship(
        parentCtor, childCtor, SubclassType.MIXIN);
    assertEquals(SubclassType.MIXIN, relationship);
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node callNode = Node.newCall("goog.inherits",
        Node.newQualifiedName("ChildClass"),
        Node.newQualifiedName("ParentClass"));
    SubclassRelationship relationship = new ClosureCodingConvention()
        .getclasssDefinedByCall(callNode);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("ChildClass", relationship.getSubclassName());
    assertEquals("ParentClass", relationship.getSuperclassName());

    callNode = Node.newCall("goog.mixin",
        Node.newQualifiedName("ChildClass.prototype"),
        Node.newQualifiedName("ParentClass.prototype"));
    relationship = new ClosureCodingConvention()
        .getclasssDefinedByCall(callNode);
    assertEquals(SubclassType.MIXIN, relationship.getType());
    assertEquals("ChildClass", relationship.getSubclassName());
    assertEquals("ParentClass", relationship.getSuperclassName());
  }

  @Test
  public void testEndsWithPrototype() {
    Node qualifiedName = Node.newQualifiedName("a.b.c");
    assertFalse(new ClosureCodingConvention().endsWithPrototype(qualifiedName));

    qualifiedName = Node.newQualifiedName("a.b.c.prototype");
    assertTrue(new ClosureCodingConvention().endsWithPrototype(qualifiedName));
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node callNode = Node.newCall(
        Node.newQualifiedName("goog.provide"),
        Node.newString("X"));
    assertEquals("X", new ClosureCodingConvention()
        .extractClassNameIfProvide(callNode, null));
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node callNode = Node.newCall(
        Node.newQualifiedName("goog.require"),
        Node.newString("X"));
    assertEquals("X", new ClosureCodingConvention()
        .extractClassNameIfRequire(callNode, null));
  }

  @Test
  public void testGetExportPropertyFunction() {
    assertEquals("goog.exportProperty",
        new ClosureCodingConvention().getExportPropertyFunction());
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node callNode = Node.newCall(
        Node.newQualifiedName("goog.addSingletonGetter"),
        Node.newQualifiedName("X"),
        Node.newQualifiedName("Y"));
    assertEquals("Y", new ClosureCodingConvention()
        .getSingletonGetterClassName(callNode));
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType getterType = new FunctionType(
        "GetterFunction", ImmutableList.of(), ObjectType.OBJECT);
    ObjectType objectType = new ObjectType("ObjectType");

    new ClosureCodingConvention()
        .applySingletonGetter(new FunctionType("FunctionType",
            ImmutableList.of(), ObjectType.OBJECT),
            getterType, objectType);

    assertEquals(ObjectType.OBJECT, getterType.getReturnType());
    assertEquals("instance_", getterType.getPrototype().getDeclaredPropertyNames()
        .get(0));
  }

  @Test
  public void testPropertyTestFunction() {
    Node callNode = Node.newCall(
        Node.newQualifiedName("goog.isFunction"),
        Node.newCall(Node.newQualifiedName("goog.getBase"),
            Node.newQualifiedName("MyClass")));

    assertTrue(new ClosureCodingConvention()
        .isPropertyTestFunction(callNode));
  }

  @Test
  public void testObjectLiteralCast() {
    Node callNode = Node.newCall(
        Node.newQualifiedName("goog.reflect.object", "cast"),
        Node.newString("function"),
        Node.newObjectLiteral(
            Node.newProperty("name", Node.newString("test"))));

    ObjectLiteralCast objectLiteralCast = new ClosureCodingConvention()
        .getObjectLiteralCast(NodeTraversal.EMPTY, callNode);

    assertEquals("function", objectLiteralCast.getExpectedType());
    assertEquals("test", objectLiteralCast.getObjectLiteral().getProperties()
        .get("name").getValue().getString());
  }

  @Test
  public void testIsOptionalParameter() {
    Node parameter = Node.newIdent("optionalParameter");
    assertFalse(new ClosureCodingConvention().isOptionalParameter(parameter));
  }

  @Test
  public void testIsVarArgsParameter() {
    Node parameter = Node.newIdent("varArgsParameter");
    assertFalse(new ClosureCodingConvention().isVarArgsParameter(parameter));
  }

  @Test
  public void testIsPrivate() {
    assertFalse(new ClosureCodingConvention().isPrivate("privateVariable"));
  }

  @Test
  public void testAssertFunction() {
    FunctionType functionType = new FunctionType(
        "AssertFunction", ImmutableList.of(), ObjectType.OBJECT);
    Node callNode = Node.newCall(
        Node.newQualifiedName("goog.asserts.assertFunction"),
        functionType);

    assertTrue(new ClosureCodingConvention()
        .isPropertyTestFunction(callNode));

    Node function = Node.newCall(
        Node.newQualifiedName("goog.asserts.assert"),
        Node.newBoolean(true),
        Node.newString("assertionMessage"));

    assertTrue(functionType.call(function, ImmutableList.of()).isBoolean());
  }
}