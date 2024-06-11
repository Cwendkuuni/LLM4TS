import static org.junit.Assert.*;

import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.JSTypeNative;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;

import java.util.List;
import java.util.Set;

public class ClosureCodingConventionTest {

  @org.junit.Test
  public void testApplySubclassRelationship() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    FunctionType parentCtor = new FunctionType("ParentCtor", null, null, null);
    FunctionType childCtor = new FunctionType("ChildCtor", null, null, null);

    codingConvention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    assertEquals(parentCtor, childCtor.getPrototype());
    assertEquals(childCtor, childCtor.getPrototype().getConstructor());
  }

  @org.junit.Test
  public void testGetclasssDefinedByCall_Inherits() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node inheritNode = createNode("goog.inherits(ChildClass, ParentClass)");

    SubclassRelationship subclassRelationship = codingConvention.getclasssDefinedByCall(inheritNode);
    assertEquals(SubclassType.INHERITS, subclassRelationship.getType());
    assertEquals("ChildClass", subclassRelationship.getSubclass().getString());
    assertEquals("ParentClass", subclassRelationship.getSuperclass().getString());
  }

  @org.junit.Test
  public void testGetclasssDefinedByCall_Mixin() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node mixinNode = createNode("goog.mixin(ChildClass.prototype, ParentClass.prototype)");

    SubclassRelationship subclassRelationship = codingConvention.getclasssDefinedByCall(mixinNode);
    assertEquals(SubclassType.MIXIN, subclassRelationship.getType());
    assertEquals("ChildClass", subclassRelationship.getSubclass().getString());
    assertEquals("ParentClass", subclassRelationship.getSuperclass().getString());
  }

  @org.junit.Test
  public void testGetclasssDefinedByCall_Invalid() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node invalidNode = createNode("goog.inherits(ChildClass, ParentClass, OtherClass)");

    SubclassRelationship subclassRelationship = codingConvention.getclasssDefinedByCall(invalidNode);
    assertNull(subclassRelationship);
  }

  @org.junit.Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    assertTrue(codingConvention.isSuperClassReference("superClass_"));
    assertFalse(codingConvention.isSuperClassReference("otherProperty"));
  }

  @org.junit.Test
  public void testExtractClassNameIfProvide() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node provideNode = createNode("goog.provide('MyClass')");

    String className = codingConvention.extractClassNameIfProvide(provideNode, null);
    assertEquals("MyClass", className);
  }

  @org.junit.Test
  public void testExtractClassNameIfRequire() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node requireNode = createNode("goog.require('MyClass')");

    String className = codingConvention.extractClassNameIfRequire(requireNode, null);
    assertEquals("MyClass", className);
  }

  @org.junit.Test
  public void testGetExportPropertyFunction() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    String exportPropertyFunction = codingConvention.getExportPropertyFunction();
    assertEquals("goog.exportProperty", exportPropertyFunction);
  }

  @org.junit.Test
  public void testGetExportSymbolFunction() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    String exportSymbolFunction = codingConvention.getExportSymbolFunction();
    assertEquals("goog.exportSymbol", exportSymbolFunction);
  }

  @org.junit.Test
  public void testIdentifyTypeDeclarationCall() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node node = createNode("goog.addDependency('MyClass', ['Dependency1', 'Dependency2'])");
    List<String> typeNames = codingConvention.identifyTypeDeclarationCall(node);
    assertEquals(2, typeNames.size());
    assertEquals("Dependency1", typeNames.get(0));
    assertEquals("Dependency2", typeNames.get(1));
  }

  @org.junit.Test
  public void testGetAbstractMethodName() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    String abstractMethodName = codingConvention.getAbstractMethodName();
    assertEquals("goog.abstractMethod", abstractMethodName);
  }

  @org.junit.Test
  public void testGetSingletonGetterClassName() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node node = createNode("goog.addSingletonGetter(MySingletonGetter, 'MySingletonGetter')");

    String singletonGetterClassName = codingConvention.getSingletonGetterClassName(node);
    assertEquals("MySingletonGetter", singletonGetterClassName);
  }

  @org.junit.Test
  public void testApplySingletonGetter() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    FunctionType functionType = new FunctionType("SingletonGetter", null, null, null);
    FunctionType getterType = new FunctionType("SingletonGetterGetter", null, null, null);
    ObjectType objectType = new ObjectType("MySingletonGetter");

    codingConvention.applySingletonGetter(functionType, getterType, objectType);

    FunctionType prototype = functionType.getPrototype();
    assertEquals(getterType, prototype.getConstructor());
    assertEquals(objectType, prototype);
  }

  @org.junit.Test
  public void testGetGlobalObject() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    String globalObject = codingConvention.getGlobalObject();
    assertEquals("goog.global", globalObject);
  }

  @org.junit.Test
  public void testIsPropertyTestFunction() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node node = createNode("goog.isDef(value)");

    assertTrue(codingConvention.isPropertyTestFunction(node));
  }

  @org.junit.Test
  public void testGetObjectLiteralCast() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node node = createNode("goog.reflect.object(value)");

    ObjectLiteralCast objectLiteralCast = codingConvention.getObjectLiteralCast(null, node);
    assertEquals("goog.reflect.object", objectLiteralCast.getTypeName());
    assertEquals("value", objectLiteralCast.getExpression().getString());
  }

  @org.junit.Test
  public void testIsOptionalParameter() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node node = createNode("function(optionalParam) { }");

    assertFalse(codingConvention.isOptionalParameter(node));
  }

  @org.junit.Test
  public void testIsVarArgsParameter() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node node = createNode("function(varArgs) { var args = Array.prototype.slice.call(arguments); }");

    assertFalse(codingConvention.isVarArgsParameter(node));
  }

  @org.junit.Test
  public void testIsPrivate() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    assertFalse(codingConvention.isPrivate("myProperty"));
  }

  @org.junit.Test
  public void testGetAssertionFunctions() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    List<AssertionFunctionSpec> assertionFunctions = codingConvention.getAssertionFunctions();

    assertEquals(7, assertionFunctions.size());

    AssertionFunctionSpec assertFunction = assertionFunctions.get(0);
    assertEquals("goog.asserts.assert", assertFunction.getName());
    assertEquals(null, assertFunction.getType());

    AssertionFunctionSpec assertNumber = assertionFunctions.get(1);
    assertEquals("goog.asserts.assertNumber", assertNumber.getName());
    assertEquals(JSTypeNative.NUMBER_TYPE, assertNumber.getType());

    AssertionFunctionSpec assertString = assertionFunctions.get(2);
    assertEquals("goog.asserts.assertString", assertString.getName());
    assertEquals(JSTypeNative.STRING_TYPE, assertString.getType());

    AssertionFunctionSpec assertFunctionInstanceType = assertionFunctions.get(6);
    assertEquals("goog.asserts.assertInstanceof", assertFunctionInstanceType.getName());
    assertEquals(JSTypeNative.OBJECT_TYPE, assertFunctionInstanceType.getType());
  }

  @org.junit.Test
  public void testDescribeFunctionBind() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node bindNode = createNode("goog.bind(fn, thisValue, 'arg1', 'arg2')");

    Bind bind = codingConvention.describeFunctionBind(bindNode);

    assertEquals("fn", bind.getFunction().getString());
    assertEquals("thisValue", bind.getThisValue().getString());
    assertEquals(Lists.newArrayList("arg1", "arg2"), bind.getArguments());
  }

  private Node createNode(String expression) {
    return new Node(Token.DUMMY, null, null, new Node(Token.NAME, expression));
  }
}