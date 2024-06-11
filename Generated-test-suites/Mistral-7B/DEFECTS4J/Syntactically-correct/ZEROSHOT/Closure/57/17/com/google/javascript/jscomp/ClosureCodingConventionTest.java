import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.jscomp.test_utils.JSTestCase.assertType;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.jscomp.Type;
import com.google.javascript.jscomp.check.BaseChecker;
import com.google.javascript.jscomp.check.CheckerFactory;
import com.google.javascript.jscomp.check.DefaultCodingConvention;
import com.google.javascript.jscomp.check.SourceFile;
import com.google.javascript.jscomp.testing.CheckType;
import com.google.javascript.rhino.JSType;

import java.io.IOException;
import java.util.List;

public class ClosureCodingConventionTest {

  public void testApplySubclassRelationship() throws IOException {
    SourceFile sourceFile = new TestSourceFile(
        "ClosureCodingConventionTest.java",
        "testApplySubclassRelationship");

    Node parentCtorNode = createConstructorNode("ParentClass");
    Node childCtorNode = createConstructorNode("ChildClass");

    CheckerFactory checkerFactory = new CheckerFactory();
    BaseChecker checker = checkerFactory.getChecker();
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(
        (FunctionType) parentCtorNode,
        (FunctionType) childCtorNode,
        SubclassType.INHERITS);

    FunctionType childCtor = (FunctionType) checkType(checker, childCtorNode);
    assertEquals("superClass_", childCtor.getPropertyNames()[0]);
    assertEquals("constructor", childCtor.getPropertyNames()[1]);

    FunctionType parentCtor = (FunctionType) checkType(checker, parentCtorNode);
    FunctionType prototype = parentCtor.getPrototype();

    assertEquals(childCtor, prototype.getProperty("constructor"));
    assertEquals(parentCtor, childCtor.getProperty("superClass_"));
  }

  public void testGetclasssDefinedByCall() throws IOException {
    SourceFile sourceFile = new TestSourceFile(
        "ClosureCodingConventionTest.java",
        "testGetclasssDefinedByCall");

    Node inheritsNode = createNode("goog.inherits(ChildClass, ParentClass)");
    Node mixinNode1 = createNode("goog.mixin(ChildClass.prototype, ParentClass.prototype)");
    Node mixinNode2 = createNode("ChildClass.prototype.mixin(ParentClass.prototype)");

    CheckerFactory checkerFactory = new CheckerFactory();
    BaseChecker checker = checkerFactory.getChecker();
    ClosureCodingConvention convention = new ClosureCodingConvention();

    SubclassRelationship result1 = convention.getclasssDefinedByCall(inheritsNode);
    assertNull(result1);

    SubclassRelationship result2 = convention.getclasssDefinedByCall(mixinNode1);
    SubclassType type = SubclassType.MIXIN;
    Node subclass = createConstructorNode("ChildClass");
    Node superclass = createConstructorNode("ParentClass");
    assertEquals(type, result2.getType());
    assertEquals(subclass, result2.getSubclass());
    assertEquals(superclass, result2.getSuperclass());

    SubclassRelationship result3 = convention.getclasssDefinedByCall(mixinNode2);
    assertEquals(type, result3.getType());
    assertEquals(subclass, result3.getSubclass());
    assertEquals(superclass, result3.getSuperclass());
  }

  public void testExtractClassNameIfProvide() throws IOException {
    SourceFile sourceFile = new TestSourceFile(
        "ClosureCodingConventionTest.java",
        "testExtractClassNameIfProvide");

    Node provideNode = createNode("goog.provide('ChildClass')");
    Node parentProvideNode = createNode("goog.provide('ParentClass')");

    CheckerFactory checkerFactory = new CheckerFactory();
    BaseChecker checker = checkerFactory.getChecker();
    ClosureCodingConvention convention = new ClosureCodingConvention();

    String result = convention.extractClassNameIfProvide(provideNode, null);
    assertEquals("ChildClass", result);

    result = convention.extractClassNameIfProvide(parentProvideNode, null);
    assertEquals("ParentClass", result);
  }

  public void testExtractClassNameIfRequire() throws IOException {
    SourceFile sourceFile = new TestSourceFile(
        "ClosureCodingConventionTest.java",
        "testExtractClassNameIfRequire");

    Node requireNode = createNode("goog.require('ChildClass')");
    Node parentRequireNode = createNode("goog.require('ParentClass')");

    CheckerFactory checkerFactory = new CheckerFactory();
    BaseChecker checker = checkerFactory.getChecker();
    ClosureCodingConvention convention = new ClosureCodingConvention();

    String result = convention.extractClassNameIfRequire(requireNode, null);
    assertEquals("ChildClass", result);

    result = convention.extractClassNameIfRequire(parentRequireNode, null);
    assertEquals("ParentClass", result);
  }

  public void testGetExportPropertyFunction() throws IOException {
    SourceFile sourceFile = new TestSourceFile(
        "ClosureCodingConventionTest.java",
        "testGetExportPropertyFunction");

    CheckerFactory checkerFactory = new CheckerFactory();
    BaseChecker checker = checkerFactory.getChecker();
    ClosureCodingConvention convention = new ClosureCodingConvention();

    String result = convention.getExportPropertyFunction();
    assertEquals("goog.exportProperty", result);
  }

  public void testGetSingletonGetterClassName() throws IOException {
    SourceFile sourceFile = new TestSourceFile(
        "ClosureCodingConventionTest.java",
        "testGetSingletonGetterClassName");

    Node addSingletonGetterNode = createNode("goog.addSingletonGetter(ChildClass, 'propertyName')");

    CheckerFactory checkerFactory = new CheckerFactory();
    BaseChecker checker = checkerFactory.getChecker();
    ClosureCodingConvention convention = new ClosureCodingConvention();

    String result = convention.getSingletonGetterClassName(addSingletonGetterNode);
    assertEquals("ChildClass", result);
  }

  public void testApplySingletonGetter() throws IOException {
    SourceFile sourceFile = new TestSourceFile(
        "ClosureCodingConventionTest.java",
        "testApplySingletonGetter");

    Type childType = new ObjectType("ChildClass");
    Type getterType = new FunctionType(JSType.VOID, new Type[] {childType});
    Type objectType = new ObjectType("Object");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySingletonGetter((FunctionType) childType, getterType, objectType);

    assertEquals("getInstance", childType.getPropertyNames()[0]);
    assertEquals("instance_", childType.getPropertyNames()[1]);
  }

  private Node createConstructorNode(String name) {
    Node constructorNode = NodeUtil.createNode(Token.CALL, null);
    constructorNode.addChild(NodeUtil.createNode(Token.GETPROP, null, name));
    return constructorNode;
  }

  private Node createNode(String nodeString) {
    return NodeUtil.createNode(Token.STRING, null, nodeString);
  }

  private <T> Type checkType(BaseChecker checker, Node node) {
    SourceFile sourceFile = new TestSourceFile("test.js", "test");
    traversalResult(traverse(sourceFile, checker, null), node);
    return checker.getType(node);
  }

  private static void traversalResult(Traversal.Result result, Node node) {
    if (result != null) {
      result.getPathToNode().stream()
          .filter(n -> n.getParentNode() == node)
          .findFirst()
          .ifPresent(n -> assertTrue(n.isTraversed()));
    }
  }

  private enum SubclassType {
    INHERITS,
    MIXIN
  }

  private static class SubclassRelationship {
    final SubclassType type;
    final Node subclass;
    final Node superclass;

    SubclassRelationship(SubclassType type, Node subclass, Node superclass) {
      this.type = type;
      this.subclass = subclass;
      this.superclass = superclass;
    }
  }

  private static class AssertionFunctionSpec {
    final String name;
    final JSType expectedType;

    AssertionFunctionSpec(String name, JSType expectedType) {
      this.name = name;
      this.expectedType = expectedType;
    }
  }

  @CheckType(types = {CHECK_TYPE_ERROR, JSError.class})
  private static class JSError {}
}