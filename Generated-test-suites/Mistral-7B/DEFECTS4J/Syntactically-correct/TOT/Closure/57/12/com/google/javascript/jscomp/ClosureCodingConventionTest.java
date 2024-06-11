import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ClosureCodingConvention.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = createFunctionType("ParentCtor");
    FunctionType childCtor = createFunctionType("ChildCtor");
    SubclassType type = SubclassType.INHERITS;
    applySubclassRelationship(parentCtor, childCtor, type);

    assertEquals(parentCtor.getPrototype(), childCtor.getPrototype().get("superClass_"));
    assertEquals(childCtor, childCtor.getPrototype().get("constructor"));
  }

  @Test
  public void testApplySubclassRelationship_mixin() {
    FunctionType parentPrototype = createFunctionType("ParentPrototype");
    FunctionType childPrototype = createFunctionType("ChildPrototype");
    applySubclassRelationship(parentPrototype, childPrototype, SubclassType.MIXIN);

    assertEquals(parentPrototype, childPrototype.getPrototype());
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node callNode = createNode("goog.inherits(SubClass, SuperClass)");
    SubclassRelationship relationship = getclasssDefinedByCall(callNode);

    assertNotNull(relationship);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("SubClass", relationship.getSubclassName());
    assertEquals("SuperClass", relationship.getSuperclassName());
  }

  @Test
  public void testGetclasssDefinedByCall_mixin() {
    Node callNode = createNode("goog.mixin(SubClass.prototype, SuperClass.prototype)");
    SubclassRelationship relationship = getclasssDefinedByCall(callNode);

    assertNotNull(relationship);
    assertEquals(SubclassType.MIXIN, relationship.getType());
    assertEquals("SubClass", relationship.getSubclassName());
    assertEquals("SuperClass", relationship.getSuperclassName());
  }

  @Test(expected = AssertionError.class)
  public void testGetclasssDefinedByCall_invalid() {
    Node callNode = createNode("goog.inherits(SubClass, SuperClass1, SuperClass2)");
    getclasssDefinedByCall(callNode);
  }

  @Test
  public void testIsSuperClassReference() {
    assertTrue(isSuperClassReference("superClass_"));
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = createNode("goog.provide('MyClass')");
    assertEquals("MyClass", extractClassNameIfProvide(node, null));
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = createNode("goog.require('MyClass')");
    assertEquals("MyClass", extractClassNameIfRequire(node, null));
  }

  @Test
  public void testGetExportPropertyFunction() {
    assertEquals("goog.exportProperty", getExportPropertyFunction());
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node callNode = createNode("goog.addSingletonGetter(MyClass, 'myProperty')");
    assertEquals("myProperty", getSingletonGetterClassName(callNode));
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = createFunctionType("MyClass");
    FunctionType getterType = createFunctionType("getMyProperty");
    ObjectType objectType = createObjectType("MyClass");

    applySingletonGetter(functionType, getterType, objectType);

    assertEquals(getterType, functionType.getPrototype().get("getInstance"));
    // instance_ is not directly accessible from the test.
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node callNode = createNode("goog.isNumber(42)");
    assertTrue(isPropertyTestFunction(callNode));
  }

  @Test
  public void testObjectLiteralCast() {
    Node callNode = createNode("goog.reflect.object.cast(new Object(), Number)");
    ObjectLiteralCast cast = getObjectLiteralCast(callNode);

    assertNotNull(cast);
    assertEquals(JSTypeNative.NUMBER_TYPE, cast.getType());
  }

  @Test
  public void testIsOptionalParameter() {
    Node parameter = createNode("function(optParam) { }");
    assertFalse(isOptionalParameter(parameter));
  }

  @Test
  public void testIsVarArgsParameter() {
    Node parameter = createNode("function(varArgs) { }");
    assertFalse(isVarArgsParameter(parameter));
  }

  @Test
  public void testIsPrivate() {
    assertFalse(isPrivate("privateProperty"));
  }

  private FunctionType createFunctionType(String name) {
    return new FunctionType(name, ImmutableList.of());
  }

  private ObjectType createObjectType(String name) {
    return new ObjectType(name);
  }

  private Node createNode(String nodeString) {
    return NodeFactory.createNodeFromString(nodeString);
  }
}