package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.JSTypeNative;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.rhino.NodeTraversal;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ClosureCodingConventionTest {

  private static final ClosureCodingConvention convention = new ClosureCodingConvention();

  @org.junit.Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentCtor", ImmutableList.of(),
        ObjectType.OBJECT_TYPE);
    FunctionType childCtor = new FunctionType("ChildCtor", ImmutableList.of(),
        ObjectType.OBJECT_TYPE);
    SubclassType type = SubclassType.INHERITS;
    convention.applySubclassRelationship(parentCtor, childCtor, type);

    assertNotNull(childCtor.getProperty("superClass_"));
    assertNotNull(childCtor.getProperty("constructor"));

    ObjectType parentPrototype = parentCtor.getPrototype();
    assertEquals(childCtor, parentPrototype.getProperty("constructor"));
  }

  @org.junit.Test
  public void testApplySubclassRelationship_MIXIN() {
    FunctionType parentCtor = new FunctionType("ParentCtor", ImmutableList.of(),
        ObjectType.OBJECT_TYPE);
    FunctionType childCtor = new FunctionType("ChildCtor", ImmutableList.of(),
        ObjectType.OBJECT_TYPE);
    SubclassType type = SubclassType.MIXIN;
    convention.applySubclassRelationship(parentCtor, childCtor, type);

    assertNotNull(childCtor.getProperty("constructor"));
    assertFalse(childCtor.hasProperty("superClass_"));

    ObjectType parentPrototype = parentCtor.getPrototype();
    assertFalse(parentPrototype.hasProperty("constructor"));
    assertTrue(parentPrototype.hasProperty("superClass_"));
  }

  @org.junit.Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newExpression("goog.provide('MyClass')");
    String className = convention.extractClassNameIfProvide(node, null);
    assertEquals("MyClass", className);
  }

  @org.junit.Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newExpression("goog.require('MyClass')");
    String className = convention.extractClassNameIfRequire(node, null);
    assertEquals("MyClass", className);
  }

  @org.junit.Test
  public void testExtractClassNameIfGoog_invalid() {
    Node node = Node.newExpression("goog.foo()");
    String className = convention.extractClassNameIfGoog(node, null, "goog.foo");
    assertNull(className);
  }

  @org.junit.Test
  public void testExtractClassNameIfGoog() {
    Node node = Node.newExpression("goog.inherits(MySubClass, MySuperClass)");
    String className = convention.extractClassNameIfGoog(node, null, "goog.inherits");
    assertEquals("MySubClass", className);
  }

  @org.junit.Test
  public void testGetExportPropertyFunction() {
    String exportPropertyFunction = convention.getExportPropertyFunction();
    assertEquals("goog.exportProperty", exportPropertyFunction);
  }

  @org.junit.Test
  public void testGetSingletonGetterClassName() {
    Node node = Node.newExpression("goog.addSingletonGetter(MyClass, 'myProperty')");
    String className = convention.getSingletonGetterClassName(node);
    assertEquals("MyClass", className);
  }

  @org.junit.Test
  public void testApplySingletonGetter() {
    FunctionType functionType = new FunctionType("MyClass", ImmutableList.of(),
        ObjectType.OBJECT_TYPE);
    ObjectType objectType = new ObjectType("MyClass");
    convention.applySingletonGetter(functionType, ObjectType.getPrototypeType(),
        objectType);

    FunctionType getterType = new FunctionType("getMyProperty",
        ImmutableList.of(), JSTypeNative.getUndefinedType());
    FunctionType instanceType = new FunctionType("MyClass",
        ImmutableList.of(), JSTypeNative.OBJECT_TYPE);

    assertEquals(getterType, functionType.getProperty("getInstance"));
    assertEquals(instanceType, functionType.getProperty("instance_"));
  }

  @org.junit.Test
  public void testIsSuperClassReference() {
    assertTrue(convention.isSuperClassReference("superClass_"));
    assertFalse(convention.isSuperClassReference("foo"));
  }

  @org.junit.Test
  public void testEndsWithPrototype() {
    Node node = Node.newExpression("a.b.prototype");
    assertTrue(convention.endsWithPrototype(node));

    node = Node.newExpression("a.b");
    assertFalse(convention.endsWithPrototype(node));
  }

  @org.junit.Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = Node.newExpression("goog.addDependency('MyClass', ['MyDependency'])");
    List<String> typeNames = convention.identifyTypeDeclarationCall(node);
    assertEquals(ImmutableList.of("MyDependency"), typeNames);
  }

  @org.junit.Test
  public void testGetAbstractMethodName() {
    String abstractMethodName = convention.getAbstractMethodName();
    assertEquals("goog.abstractMethod", abstractMethodName);
  }

  @org.junit.Test
  public void testSubclassRelationship_fromCallNode() {
    Node node = Node.newExpression("goog.inherits(MySubClass, MySuperClass)");
    SubclassRelationship relationship = convention.getclasssDefinedByCall(node);

    assertNotNull(relationship);
    assertEquals(SubclassType.INHERITS, relationship.type);
    assertEquals("MySubClass", relationship.subclass);
    assertEquals("MySuperClass", relationship.superclass);
  }

  @org.junit.Test
  public void testSubclassRelationship_fromCallNode_MIXIN() {
    Node node = Node.newExpression("goog.mixin(MySubClass.prototype, MySuperClass.prototype)");
    SubclassRelationship relationship = convention.getclasssDefinedByCall(node);

    assertNotNull(relationship);
    assertEquals(SubclassType.MIXIN, relationship.type);
    assertEquals("MySubClass", relationship.subclass);
    assertEquals("MySuperClass", relationship.superclass);
  }

  @org.junit.Test
  public void testSubclassRelationship_fromCallNode_invalid() {
    Node node = Node.newExpression("goog.foo()");
    SubclassRelationship relationship = convention.getclasssDefinedByCall(node);
    assertNull(relationship);
  }

  @org.junit.Test
  public void testIsPropertyTestFunction() {
    Node node = Node.newExpression("goog.isDef(x)");
    assertTrue(convention.isPropertyTestFunction(node));
  }

  @org.junit.Test
  public void testPropertyTestFunction_invalid() {
    Node node = Node.newExpression("foo()");
    assertFalse(convention.isPropertyTestFunction(node));
  }

  @org.junit.Test
  public void testObjectLiteralCast() {
    Node node = Node.newExpression(
        "goog.reflect.object.getObjectType(goog.bind(function() {}, this))");
    ObjectLiteralCast cast = convention.getObjectLiteralCast(new NodeTraversal(), node);

    assertNotNull(cast);
    assertEquals("goog.bind", cast.functionName);
    assertEquals(JSTypeNative.FUNCTION_TYPE, cast.functionType);
  }

  @org.junit.Test
  public void testObjectLiteralCast_invalid() {
    Node node = Node.newExpression("goog.foo()");
    ObjectLiteralCast cast = convention.getObjectLiteralCast(new NodeTraversal(), node);
    assertNull(cast);
  }

  @org.junit.Test
  public void testOptionalParameter() {
    Node parameter = Node.newExpression("function(x = 42) {}");
    assertFalse(convention.isOptionalParameter(parameter));
  }

  @org.junit.Test
  public void testVarArgsParameter() {
    Node parameter = Node.newExpression("function(args) {}");
    assertFalse(convention.isVarArgsParameter(parameter));
  }

  @org.junit.Test
  public void testIsPrivate() {
    assertFalse(convention.isPrivate("foo"));
  }

  @org.junit.Test
  public void testAssertionFunctions() {
    Collection<AssertionFunctionSpec> functions = convention.getAssertionFunctions();
    assertEquals(8, functions.size());

    for (AssertionFunctionSpec functionSpec : functions) {
      String name = functionSpec.functionName;
      JSType type = functionSpec.type;
      assertNotNull(name);
      assertNotNull(type);
    }
  }

  @org.junit.Test
  public void testBind() {
    Node node = Node.newExpression("goog.bind(function() {}, this, 'arg1')");
    Bind bind = convention.describeFunctionBind(node);

    assertNotNull(bind);
    assertEquals("function() {}", bind.function);
    assertEquals("this", bind.thisValue);
    assertEquals(ImmutableList.of("arg1"), bind.parameters);
  }

  @org.junit.Test
  public void testBind_invalid() {
    Node node = Node.newExpression("goog.bind(function() {})");
    Bind bind = convention.describeFunctionBind(node);
    assertNull(bind);
  }

  @org.junit.Test
  public void testBind_partial() {
    Node node = Node.newExpression("goog.partial(function(a, b), 1)");
    Bind bind = convention.describeFunctionBind(node);

    assertNotNull(bind);
    assertEquals("function(a, b)", bind.function);
    assertNull(bind.thisValue);
    assertEquals(ImmutableList.of(Node.newNumber(1)), bind.parameters);
  }

  @org.junit.Test
  public void testBind_partial_invalid() {
    Node node = Node.newExpression("goog.partial(function(), 1)");
    Bind bind = convention.describeFunctionBind(node);
    assertNull(bind);
  }
}