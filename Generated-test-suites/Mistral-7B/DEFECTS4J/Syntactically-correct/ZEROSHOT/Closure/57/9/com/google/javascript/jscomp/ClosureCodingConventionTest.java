import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ClosureCodingConvention.*;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;

import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = createFunctionType("Parent");
    FunctionType childCtor = createFunctionType("Child");
    SubclassType relationshipType = SubclassType.INHERITS;

    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    codingConvention.applySubclassRelationship(parentCtor, childCtor, relationshipType);

    assertEquals(parentCtor, childCtor.getPrototype());
    assertEquals(childCtor, childCtor.getPrototype().getConstructor());
  }

  @Test
  public void testApplySubclassRelationship_mixin() {
    FunctionType parentPrototype = createFunctionType("ParentPrototype");
    FunctionType childPrototype = createFunctionType("ChildPrototype");
    FunctionType childCtor = createFunctionType("Child");

    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    SubclassType relationshipType = SubclassType.MIXIN;

    codingConvention.applySubclassRelationship(parentPrototype, childCtor, relationshipType);
    assertEquals(parentPrototype, childPrototype);

    codingConvention.applySubclassRelationship(childPrototype, childCtor, relationshipType);
    assertEquals(childPrototype, childCtor.getPrototype());
    assertEquals(childCtor, childCtor.getPrototype().getConstructor());
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node node = createCallNode("goog.inherits(MySubClass, SuperClass)");

    SubclassRelationship relationship = new ClosureCodingConvention()
        .getclasssDefinedByCall(node);

    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("MySubClass", relationship.getSubclass());
    assertEquals("SuperClass", relationship.getSuperclass());
  }

  @Test
  public void testGetclasssDefinedByCall_deprecated() {
    Node node = createCallNode("MySubClass.inherits(SuperClass)");

    SubclassRelationship relationship = new ClosureCodingConvention()
        .getclasssDefinedByCall(node);

    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("MySubClass", relationship.getSubclass());
    assertEquals("SuperClass", relationship.getSuperclass());
  }

  @Test
  public void testGetclasssDefinedByCall_mixin() {
    Node node = createCallNode("MySubClass.prototype.mixin(SuperClass.prototype)");

    SubclassRelationship relationship = new ClosureCodingConvention()
        .getclasssDefinedByCall(node);

    assertEquals(SubclassType.MIXIN, relationship.getType());
    assertEquals("MySubClass.prototype", relationship.getSubclass());
    assertEquals("SuperClass.prototype", relationship.getSuperclass());
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = createCallNode("goog.provide('MyClass')");

    String className = new ClosureCodingConvention()
        .extractClassNameIfProvide(node, node.getParent());

    assertEquals("MyClass", className);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = createCallNode("goog.require('MyClass')");

    String className = new ClosureCodingConvention()
        .extractClassNameIfRequire(node, node.getParent());

    assertEquals("MyClass", className);
  }

  @Test
  public void testIsSuperClassReference() {
    assertEquals(true, new ClosureCodingConvention().isSuperClassReference("superClass_"));
  }

  @Test
  public void testEndsWithPrototype() {
    Node node = createQualifiedNameNode("a.b.c.prototype");

    assertTrue(new ClosureCodingConvention().endsWithPrototype(node));

    node = createQualifiedNameNode("a.b.c");
    assertFalse(new ClosureCodingConvention().endsWithPrototype(node));
  }

  @Test
  public void testExtractClassNameIfGoog_provide() {
    Node node = createCallNode("goog.provide('MyClass')");

    String className = new ClosureCodingConvention()
        .extractClassNameIfGoog(node, node.getParent(), "goog.provide");

    assertEquals("MyClass", className);
  }

  @Test
  public void testExtractClassNameIfGoog_require() {
    Node node = createCallNode("goog.require('MyClass')");

    String className = new ClosureCodingConvention()
        .extractClassNameIfGoog(node, node.getParent(), "goog.require");

    assertEquals("MyClass", className);
  }

  @Test
  public void testGetExportPropertyFunction() {
    String exportPropertyFunction = new ClosureCodingConvention()
        .getExportPropertyFunction();

    assertEquals("goog.exportProperty", exportPropertyFunction);
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node node = createCallNode("goog.addSingletonGetter(MyClass, 'getName')");

    String getterClassName = new ClosureCodingConvention()
        .getSingletonGetterClassName(node);

    assertEquals("getName", getterClassName);
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = createFunctionType("MyClass");
    FunctionType getterType = createFunctionType("getName");
    ObjectType objectType = createObjectType("MyClass");

    new ClosureCodingConvention()
        .applySingletonGetter(functionType, getterType, objectType);

    FunctionType instance = functionType.getMethod("getInstance");
    ObjectType instanceObject = instance.getReturnType();
    FunctionType getter = instance.getMethod("getName");

    assertEquals(objectType, instanceObject);
    assertEquals(getterType, getter.getReturnType());
    assertEquals(objectType, getter.getThisType());
  }

  @Test
  public void testGetGlobalObject() {
    String globalObject = new ClosureCodingConvention()
        .getGlobalObject();

    assertEquals("goog.global", globalObject);
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node node = createCallNode("goog.isDef(x)");

    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(node));
  }

  @Test
  public void testObjectLiteralCast() {
    Node node = createCallNode("goog.reflect.object.cast(myObject, MyType)");
    ObjectLiteralCast expected =
        new ObjectLiteralCast("MyType", node.getNext().getNext());

    ObjectLiteralCast actual = new ClosureCodingConvention()
        .getObjectLiteralCast(NodeTraversal.emptyTraversal(), node);

    assertEquals(expected, actual);
  }

  @Test
  public void testIsOptionalParameter() {
    fail("not implemented");
  }

  @Test
  public void testIsVarArgsParameter() {
    fail("not implemented");
  }

  @Test
  public void testIsPrivate() {
    fail("not implemented");
  }

  @Test
  public void testAssertionFunctions() {
    List<AssertionFunctionSpec> assertionFunctions = new ClosureCodingConvention()
        .getAssertionFunctions();

    assertEquals(7, assertionFunctions.size());
    assertTrue(assertionFunctions.contains(new AssertionFunctionSpec(
        "goog.asserts.assert", JSTypeNative.OBJECT_TYPE)));
  }

  @Test
  public void testBind() {
    Node node = createCallNode("goog.bind(myFunction, thisValue, arg1, arg2)");
    Bind expected = new Bind("myFunction", node.getNext(), node.getNext().getNext(), node.getNext().getNext());

    Bind actual = new ClosureCodingConvention()
        .describeFunctionBind(node);

    assertEquals(expected, actual);
  }

  private FunctionType createFunctionType(String name) {
    ObjectType objectType = createObjectType(name);
    return new FunctionType(objectType, ImmutableList.<FunctionType>of(),
        ImmutableList.<String>of());
  }

  private ObjectType createObjectType(String name) {
    return new ObjectType(name, ImmutableSet.<String>of());
  }

  private Node createCallNode(String callName) {
    return new NodeBuilder()
        .token(Token.CALL, null, null, null)
        .addChild(createNode(Token.GETPROP, null, null, callName))
        .build();
  }

  private Node createQualifiedNameNode(String name) {
    return new NodeBuilder()
        .token(Token.GETPROP, null, null, null)
        .addChild(createNode(Token.STRING, null, null, name))
        .build();
  }

  private Node createNode(int type, Object value, Object location, String name) {
    Node node = new NodeBuilder().token(type, value, location, name).build();
    return node;
  }

  private Node createNode(int type, Object value, Object location) {
    return new NodeBuilder().token(type, value, location, null).build();
  }
}