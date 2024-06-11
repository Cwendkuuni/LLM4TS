import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ClosureCodingConvention.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

import java.util.List;

public class ClosureCodingConventionTest {

  private static FunctionType createTestFunctionType() {
    ObjectType prototype = new ObjectType("prototype");
    prototype.defineProperty("constructor", new FunctionType(null, null), null);

    return new FunctionType(null, ImmutableList.of(prototype), prototype);
  }

  @org.junit.Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = createTestFunctionType();
    FunctionType childCtor = createTestFunctionType();

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    // Check that the properties are defined on the child class.
    Node childPrototype = childCtor.getPrototype();
    Node superClassProperty = childPrototype.getChildByName("superClass_");
    Node constructorProperty = childPrototype.getChildByName("constructor");

    // Check that the superClass_ property points back to the parent class.
    Node parentPrototype = parentCtor.getPrototype();
    Node childSuperClassProperty = superClassProperty.getChild();
    Node childConstructorProperty = constructorProperty.getChild();

    assertEquals(parentPrototype, childSuperClassProperty);
    assertEquals(childCtor, childConstructorProperty);
  }

  @org.junit.Test
  public void testGetclasssDefinedByCall() {
    Node callNode = Node.createCall(Token.NAME, "goog.inherits",
        Node.createName("SubClass"), Node.createName("SuperClass"));

    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(callNode);

    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("SubClass", relationship.getSubclassName());
    assertEquals("SuperClass", relationship.getSuperclassName());
  }

  @org.junit.Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.createCall(Token.NAME, "goog.provide",
        Node.createArrayLiteral(Node.createName("X")));

    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.extractClassNameIfProvide(node, null);

    assertEquals("X", className);
  }

  @org.junit.Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.createCall(Token.NAME, "goog.require",
        Node.createArrayLiteral(Node.createName("X")));

    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.extractClassNameIfRequire(node, null);

    assertEquals("X", className);
  }

  @org.junit.Test
  public void testApplySingletonGetter() {
    FunctionType functionType = createTestFunctionType();
    FunctionType getterType = createTestFunctionType();
    ObjectType objectType = new ObjectType("X");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySingletonGetter(functionType, getterType, objectType);

    // Check that the getInstance property is defined on the function type.
    Node getInstance = functionType.getChildByName("getInstance");
    assertEquals(getterType, getInstance.getType());

    // Check that the instance_ property is defined on the function type.
    Node instance = functionType.getChildByName("instance_");
    assertEquals(objectType, instance.getType());
  }

  @org.junit.Test
  public void testIsPropertyTestFunction() {
    Node callNode = Node.createCall(Token.NAME, "goog.isDef",
        Node.createName("x"));

    ClosureCodingConvention convention = new ClosureCodingConvention();
    boolean isTestFunction = convention.isPropertyTestFunction(callNode);

    assertTrue(isTestFunction);
  }

  @org.junit.Test
  public void testObjectLiteralCast() {
    Node callNode = Node.createCall(Token.NAME, "goog.reflect.object",
        Token.NUMBER_LITERAL, Node.createObjectLiteral(
            Node.createProperty(Token.STRING_LITERAL, Token.NUMBER_LITERAL)));

    ClosureCodingConvention convention = new ClosureCodingConvention();
    ObjectLiteralCast cast = convention.getObjectLiteralCast(null, callNode);

    assertNotNull(cast);
    assertEquals("goog.reflect.object", cast.getFunctionName());
    assertEquals(Node.createObjectLiteral(
        Node.createProperty(Token.STRING_LITERAL, Node.createNumberLiteral(1))),
        cast.getObjectLiteral());
  }
}