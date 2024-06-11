package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.NodeTraversal;

import java.util.List;

import org.junit.Test;

/**
 * Tests for the {@link ClosureCodingConvention} class.
 */
public class ClosureCodingConventionTest {

  @Test
  public void testSubclassRelationship() {
    Node node = createNode(
        "goog.inherits(MySubClass, MySuperClass)",
        "MySuperClass", "MySubClass");
    FunctionType superCtor = createFunctionType("MySuperClass");
    FunctionType subCtor = createFunctionType("MySubClass");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(superCtor, subCtor, SubclassType.INHERITS);
    assertEquals(superCtor, subCtor.getPrototype());
    assertEquals(subCtor, superCtor.getPrototype().getConstructor());

    node = createNode(
        "goog.mixin(MySubClass.prototype, MySuperClass.prototype)",
        "MySuperClass.prototype", "MySubClass.prototype");
    SubclassType type = SubclassType.MIXIN;
    convention.applySubclassRelationship(null, null, type);
    assertEquals(type, SubclassType.MIXIN);
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = createNode("goog.provide('MyClass')");
    assertEquals("MyClass", convention.extractClassNameIfProvide(node, null));
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = createNode("goog.require('MyClass')");
    assertEquals("MyClass", convention.extractClassNameIfRequire(node, null));
  }

  @Test
  public void testGetExportPropertyFunction() {
    assertEquals("goog.exportProperty",
        convention.getExportPropertyFunction());
  }

  @Test
  public void testGetExportSymbolFunction() {
    assertEquals("goog.exportSymbol",
        convention.getExportSymbolFunction());
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = createNode("goog.addDependency('MyType', 'MyModule')");
    assertEquals(ImmutableList.of("MyType"),
        convention.identifyTypeDeclarationCall(node));
  }

  @Test
  public void testGetAbstractMethodName() {
    assertEquals("goog.abstractMethod",
        convention.getAbstractMethodName());
  }

  @Test
  public void testSingletonGetterClassName() {
    Node node = createNode(
        "goog.addSingletonGetter(MyClass.prototype, 'myProperty')",
        "MyClass.prototype");
    assertEquals("myProperty", convention.getSingletonGetterClassName(node));
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = createFunctionType("MyClass");
    Node getterType = createFunctionType("getMyProperty");
    ObjectType objectType = createObjectType("MyClass");
    Convention convention = new ClosureCodingConvention();
    convention.applySingletonGetter(functionType, getterType, objectType);
    FunctionType prototype = functionType.getPrototype();
    assertEquals(getterType, prototype.getDeclaredProperty("getInstance"));
    assertEquals(objectType, prototype.getDeclaredProperty("instance_"));
  }

  @Test
  public void testGetGlobalObject() {
    assertEquals("goog.global", convention.getGlobalObject());
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node node = createNode("goog.isDef(myValue)");
    assertTrue(convention.isPropertyTestFunction(node));
  }

  @Test
  public void testObjectLiteralCast() {
    NodeTraversal traversal = new NodeTraversal(null);
    Node node = createNode("goog.reflect.object.getTypes(myValue)",
        "myValue");
    ObjectLiteralCast cast = convention.getObjectLiteralCast(traversal, node);
    assertEquals("goog.reflect.object", cast.getFunctionName());
    assertEquals("myValue", cast.getArgument());
  }

  @Test
  public void testIsOptionalParameter() {
    Node node = createNode("function myFunction(optionalParam) {}");
    assertFalse(convention.isOptionalParameter(node.getFirstChild()));
  }

  @Test
  public void testIsVarArgsParameter() {
    Node node = createNode("function myFunction(params...) {}");
    assertFalse(convention.isVarArgsParameter(node.getFirstChild()));
  }

  @Test
  public void testIsPrivate() {
    Node node = createNode("function MyClass() {}");
    assertFalse(convention.isPrivate("MyClass"));
  }

  @Test
  public void testAssertionFunctions() {
    List<AssertionFunctionSpec> functions = convention.getAssertionFunctions();
    assertEquals(7, functions.size());
  }

  @Test
  public void testBind() {
    Node node = createNode("goog.bind(MyFunction, this, argument)",
        "MyFunction", "this", "argument");
    Bind bind = convention.describeFunctionBind(node);
    assertEquals("MyFunction", bind.getFunctionName());
    assertEquals("this", bind.getThisValue());
    assertEquals(ImmutableList.of("argument"), bind.getArguments());
  }

  private Node createNode(String expression, String... arguments) {
    return NodeFactory.createNodeFromString(expression, null);
  }

  private FunctionType createFunctionType(String name) {
    return new FunctionType(name, ImmutableList.<ObjectType>of(),
        new ObjectType("Object"));
  }

  private ObjectType createObjectType(String name) {
    return new ObjectType(name);
  }
}