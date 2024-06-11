import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ClosureCodingConvention.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParseTree;
import com.google.javascript.rhino.NodeFactory;

import java.util.Collection;
import java.util.List;

public class ClosureCodingConventionTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new Compiler();
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testSubclassRelationship() {
    FunctionType parentCtor = createFunctionType("ParentCtor");
    FunctionType childCtor = createFunctionType("ChildCtor");

    SubclassType subclassType = SubclassType.INHERITS;
    applySubclassRelationship(parentCtor, childCtor, subclassType);

    assertEquals(2, childCtor.getProperties().size());
    assertEquals("superClass_", childCtor.getPropertyNames().get(0));
    assertEquals("constructor", childCtor.getPropertyNames().get(1));

    assertNotNull(childCtor.getProperty("superClass_"));
    assertEquals(parentCtor, childCtor.getProperty("superClass_").getValue());

    assertNotNull(childCtor.getProperty("constructor"));
    assertEquals(childCtor, childCtor.getProperty("constructor").getValue());
  }

  @Test
  public void testSubclassRelationship_MIXIN() {
    FunctionType parentPrototype = createFunctionType("ParentPrototype");
    FunctionType childPrototype = createFunctionType("ChildPrototype");

    SubclassType subclassType = SubclassType.MIXIN;
    applySubclassRelationship(parentPrototype, childPrototype, subclassType);

    assertEquals(0, childPrototype.getProperties().size());

    assertNotNull(childPrototype.getPrototype());
    assertEquals(parentPrototype, childPrototype.getPrototype());
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = nodeFactory.createCall(nodeFactory.createName("goog.provide"),
        ImmutableList.<Node>of(nodeFactory.createString("X")));

    String className = extractClassNameIfProvide(node, nodeFactory.createName("goog"));
    assertNotNull(className);
    assertEquals("X", className);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = nodeFactory.createCall(nodeFactory.createName("goog.require"),
        ImmutableList.<Node>of(nodeFactory.createString("X")));

    String className = extractClassNameIfRequire(node, nodeFactory.createName("goog"));
    assertNotNull(className);
    assertEquals("X", className);
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = createFunctionType("Singleton");
    FunctionType getterType = createFunctionType("getter");
    ObjectType objectType = createObjectType("Object");

    applySingletonGetter(functionType, getterType, objectType);

    assertEquals(2, functionType.getProperties().size());
    assertEquals("getInstance", functionType.getPropertyNames().get(0));
    assertEquals("instance_", functionType.getPropertyNames().get(1));

    assertNotNull(functionType.getProperty("getInstance"));
    assertTrue(functionType.getProperty("getInstance").isFunction());

    assertNotNull(functionType.getProperty("instance_"));
    assertTrue(functionType.getProperty("instance_").isObject());
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node call = nodeFactory.createCall(nodeFactory.createName("goog.isDef"),
        ImmutableList.<Node>of(nodeFactory.createNumber(1)));

    assertTrue(isPropertyTestFunction(call));
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
  public void testGetObjectLiteralCast() {
    ParseTree parseTree = compiler.parseJavaScript("(function() {" +
        "  function Foo() {};" +
        "  return goog.reflect.object.getPrototypeOf(new Foo());" +
        "})()");
    Node rootNode = parseTree.getRootNode();

    Node callNode = findCallNode("goog.reflect.object.getPrototypeOf", rootNode);
    ObjectLiteralCast expectedCast = new ObjectLiteralCast(
        "goog.reflect.object", null);
    ObjectLiteralCast actualCast = getObjectLiteralCast(
        new NodeTraversal(compiler, rootNode), callNode);

    assertEquals(expectedCast, actualCast);
  }

  @Test
  public void testGetObjectLiteralCast_MissingObjectLiteral() {
    ParseTree parseTree = compiler.parseJavaScript("(function() {" +
        "  function Foo() {};" +
        "  return goog.reflect.object.getPrototypeOf(new Foo());" +
        "})()");
    Node rootNode = parseTree.getRootNode();

    Node callNode = findCallNode("goog.reflect.object.getPrototypeOf", rootNode);
    assertTrue(getObjectLiteralCast(
        new NodeTraversal(compiler, rootNode), callNode) == null);
  }

  private FunctionType createFunctionType(String name) {
    return new FunctionType(name, ImmutableList.<String>of(),
        new ObjectType("Object"), null);
  }

  private ObjectType createObjectType(String name) {
    return new ObjectType(name);
  }

  private Node findCallNode(String name, Node node) {
    if (node.getType() == Token.CALL) {
      Node callName = node.getFirstChild();
      String qualifiedName = callName.getQualifiedName();
      if (qualifiedName != null && qualifiedName.equals(name)) {
        return node;
      }
    }

    if (node.getChildCount() > 0) {
      for (Node child = node.getFirstChild(); child != null;
           child = child.getNext()) {
        Node found = findCallNode(name, child);
        if (found != null) {
          return found;
        }
      }
    }

    return null;
  }

}