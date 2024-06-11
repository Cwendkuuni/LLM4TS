package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.jscomp.SubclassRelationship;

import java.util.Arrays;
import java.util.List;

public class ClosureCodingConventionTest {

  @org.junit.Test
  public void testApplySubclassRelationship() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    FunctionType parentCtor = new FunctionType("ParentCtor", ImmutableList.of(),
        new ObjectType("Parent"), null);
    FunctionType childCtor = new FunctionType("ChildCtor", ImmutableList.of(),
        new ObjectType("Child"), null);

    convention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    assertEquals("Parent", childCtor.getPrototype().getClass().getSimpleName());
    assertEquals(parentCtor, childCtor.getPrototype().getConstructor());

    FunctionType childCtor2 = new FunctionType("ChildCtor2", ImmutableList.of(),
        new ObjectType("Child2"), null);

    convention.applySubclassRelationship(parentCtor, childCtor2, SubclassType.MIXIN);

    assertEquals("Parent", childCtor2.getPrototype().getClass().getSimpleName());
    assertNull(childCtor2.getPrototype().getConstructor());
  }

  @org.junit.Test
  public void testGetclasssDefinedByCall() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node callNode = Node.createCall("goog.inherits",
        Node.createName("SubClass"), Node.createName("SuperClass"));

    SubclassRelationship relationship = convention.getclasssDefinedByCall(callNode);

    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("SubClass", relationship.getSubclass().getString());
    assertEquals("SuperClass", relationship.getSuperclass().getString());

    callNode = Node.createCall("goog.mixin",
        Node.createName("SubClass"), Node.createName("SuperClass").getLastChild());

    relationship = convention.getclasssDefinedByCall(callNode);

    assertEquals(SubclassType.MIXIN, relationship.getType());
    assertEquals("SubClass", relationship.getSubclass().getString());
    assertEquals("SuperClass", relationship.getSuperclass().getString());
  }

  @org.junit.Test
  public void testExtractClassNameIfProvide() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node node = Node.createCall("goog.provide", Node.createString("X"));

    String className = convention.extractClassNameIfProvide(node, null);

    assertEquals("X", className);
  }

  @org.junit.Test
  public void testExtractClassNameIfRequire() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node node = Node.createCall("goog.require", Node.createString("X"));

    String className = convention.extractClassNameIfRequire(node, null);

    assertEquals("X", className);
  }

  @org.junit.Test
  public void testGetExportPropertyFunction() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertEquals("goog.exportProperty",
        convention.getExportPropertyFunction());
  }

  @org.junit.Test
  public void testGetSingletonGetterClassName() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node callNode = Node.createCall("goog.addSingletonGetter",
        Node.createName("X"), Node.createName("getX"));

    String className = convention.getSingletonGetterClassName(callNode);

    assertEquals("X", className);
  }

  @org.junit.Test
  public void testApplySingletonGetter() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    FunctionType functionType = new FunctionType("X", ImmutableList.of(),
        new ObjectType("X"), null);
    FunctionType getterType = new FunctionType("getX", ImmutableList.of(),
        new ObjectType("X"), null);
    ObjectType objectType = new ObjectType("X");

    convention.applySingletonGetter(functionType, getterType, objectType);

    assertEquals("getInstance", functionType.getProperties().get("getInstance")
        .getName());
    assertEquals("instance_", functionType.getProperties().get("instance_")
        .getName());
  }

  @org.junit.Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertTrue(convention.isSuperClassReference("superClass_"));
    assertFalse(convention.isSuperClassReference("otherProperty"));
  }

  @org.junit.Test
  public void testIdentifyTypeDeclarationCall() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node node = Node.createCall("goog.addDependency", Node.createString("X"),
        Node.createArrayLiteral(Node.createString("Y"), Node.createString("Z")));

    List<String> typeNames = convention.identifyTypeDeclarationCall(node);

    assertEquals(Arrays.asList("Y", "Z"), typeNames);
  }

  @org.junit.Test
  public void testIsPropertyTestFunction() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node node = Node.createCall("goog.isDef", Node.createName("x"));

    assertTrue(convention.isPropertyTestFunction(node));
  }

  @org.junit.Test
  public void testGetObjectLiteralCast() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node node = Node.createCall(Node.createName("goog.reflect.object"),
        Node.createName("X"), Node.createObjectLiteral(
            Node.createProperty(Node.createName("prop"), Node.createString("val"))));

    ObjectLiteralCast cast = convention.getObjectLiteralCast(null, node);

    assertEquals("X", cast.getTypeName());
    assertEquals("prop", cast.getProperties().get(0).getName());
    assertEquals("val", cast.getProperties().get(0).getValue().getString());
  }
}