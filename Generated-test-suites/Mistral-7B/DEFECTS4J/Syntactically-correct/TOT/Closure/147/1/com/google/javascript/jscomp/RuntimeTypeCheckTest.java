package com.google.javascript.jscomp;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.NodeUtil;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * Tests for {@code RuntimeTypeCheck} class.
 */
public class RuntimeTypeCheckTest extends TestCase {

  private static final Comparator<JSType> ALPHA = new Comparator<JSType>() {
    public int compare(JSType t1, JSType t2) {
      return getName(t1).compareTo(getName(t2));
    }

    private String getName(JSType type) {
      if (type.isInstanceType()) {
        return ((ObjectType) type).getReferenceName();
      } else if (type.isNullType()
          || type.isBooleanValueType()
          || type.isNumberValueType()
          || type.isStringValueType()
          || type.isVoidType()) {
        return type.toString();
      } else {
        // Type unchecked at runtime, so we don't care about the sorting order.
        return "";
      }
    }
  };

  private Compiler compiler;
  private RuntimeTypeCheck runtimeTypeCheck;
  private String logFunction;

  @Override
  protected void setUp() throws Exception {
    compiler = new Compiler();
    runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
  }

  @Override
  protected void tearDown() throws Exception {
    runtimeTypeCheck = null;
    compiler = null;
  }

  /**
   * Test for inserting markers for user-defined interfaces and classes.
   */
  public void testInsertMarkers() {
    String testCode = "function Foo() {}" +
        "function FooInterface() {}" +
        "FooInterface.prototype.myMethod = function() {}";

    Node root = Parse.parseCompilationUnit(testCode);
    runtimeTypeCheck.process(null, root);

    Node classNode = NodeUtil.newQualifiedNameNode("Foo");
    assertEquals(3, NodeUtil.getChildrenCount(classNode.getFirstChild()));
    Node marker = NodeUtil.getFirstChildOfType(
        NodeUtil.getChildren(classNode.getFirstChild()),
        Token.GETPROP, "prototype");
    assertEquals("instance_of__Foo", marker.getFirstChild().getString());

    marker = NodeUtil.getFirstChildOfType(
        NodeUtil.getChildren(classNode.getFirstChild()),
        Token.GETPROP, "prototype");
    marker = NodeUtil.getFirstChildOfType(marker.getChildren(), Token.EXPR_LIST);
    assertEquals("implements__FooInterface",
        marker.getLastChild().getFirstChild().getString());
  }

  /**
   * Test for inserting type checks for function parameters and return value.
   */
  public void testInsertChecks() {
    String testCode = "function Foo(x) {" +
        "if (x instanceof Number) {" +
        "  return 2 * x;" +
        "} else {" +
        "  throw new TypeError('Expected number');" +
        "}" +
        "}";

    Node root = Parse.parseCompilationUnit(testCode);
    runtimeTypeCheck.process(null, root);

    Node callNode = NodeUtil.getFirstChildOfType(
        root.getChildren(), Token.EXPR_LIST);
    Node functionNode = NodeUtil.getFirstChildOfType(
        callNode.getChildren(), Token.CALL);
    assertEquals(2, functionNode.getChildren().size());

    Node functionTypeNode = functionNode.getFirstChild();
    assertEquals(1, functionTypeNode.getChildren().size());

    Node parameterNode = functionTypeNode.getChildren().get(0);
    assertEquals("Number", parameterNode.getString());

    Node checkNode = NodeUtil.getFirstChildOfType(
        functionTypeNode.getChildren(), Token.EXPR_LIST);
    assertEquals(1, checkNode.getChildren().size());
    assertEquals("valueChecker", checkNode.getChildren().get(0).getString());

    Node bodyNode = NodeUtil.getFirstChildOfType(
        root.getChildren(), Token.BLOCK);
    Node multiplyNode = NodeUtil.getFirstChildOfType(
        bodyNode.getChildren(), Token.EXPR_LIST);
    Node timesNode = NodeUtil.getFirstChildOfType(
        multiplyNode.getChildren(), Token.TIMES);
    Node parameterNodeInBody = timesNode.getChildren().get(0);

    Node checkReturnNode = NodeUtil.getFirstChildOfType(
        root.getChildren(), Token.RETURN);
    assertEquals(1, checkReturnNode.getChildren().size());

    Node checkNodeForReturn = checkReturnNode.getChildren().get(0);
    assertEquals("valueChecker", checkNodeForReturn.getString());
  }

  /**
   * Test for checking return types in case of multiple inheritance.
   */
  public void testCheckReturnTypesMultipleInheritance() {
    String testCode = "function Foo() {}" +
        "function Bar() {}" +
        "Foo.prototype = Object.create(Bar.prototype);" +
        "function Baz() {" +
        "  return new Foo();" +
        "}" +
        "Baz.prototype.myMethod = function() {}";

    Node root = Parse.parseCompilationUnit(testCode);
    runtimeTypeCheck.process(null, root);

    Node callNode = new Node(Token.CALL, NodeUtil.newQualifiedNameNode("Baz"));
    Node functionNode = NodeUtil.getFirstChildOfType(
        callNode.getChildren(), Token.CALL);
    assertEquals(2, functionNode.getChildren().size());

    Node functionTypeNode = functionNode.getFirstChild();
    assertEquals(1, functionTypeNode.getChildren().size());

    Node parameterNode = functionTypeNode.getChildren().get(0);
    assertEquals("", parameterNode.getString());

    Node checkNode = NodeUtil.getFirstChildOfType(
        functionTypeNode.getChildren(), Token.EXPR_LIST);
    assertEquals(2, checkNode.getChildren().size());

    Node classCheckerNode = checkNode.getChildren().get(0);
    assertEquals("classChecker", classCheckerNode.getString());
    assertEquals("Foo", classCheckerNode.getLastChild().getString());

    Node interfaceCheckerNode = checkNode.getChildren().get(1);
    assertEquals("interfaceChecker", interfaceCheckerNode.getString());

    TreeSet<JSType> typeSet = Sets.newTreeSet(ALPHA);
    Iterables.addAll(typeSet,
        new ObjectType(new ObjectType.Constructor(null, "Foo"), null),
        new ObjectType(new ObjectType.Constructor(null, "Bar"), null));
    assertEquals(typeSet,
        TypeChecker.checkTypes(
            compiler, NodeUtil.getFunctionType(functionNode)));
  }

  /**
   * Test for checking return types in case of intersection types.
   */
  public void testCheckReturnTypesIntersectionTypes() {
    String testCode = "function Foo() {}" +
        "function Bar() {}" +
        "function Baz(x) {" +
        "  if (x instanceof Foo && x instanceof Bar) {" +
        "    return 2 * x;" +
        "  } else {" +
        "    throw new TypeError('Expected Foo & Bar');" +
        "  }" +
        "}" +
        "Baz.prototype.myMethod = function() {}";

    Node root = Parse.parseCompilationUnit(testCode);
    runtimeTypeCheck.process(null, root);

    Node callNode = new Node(Token.CALL, NodeUtil.newQualifiedNameNode("Baz"));
    Node functionNode = NodeUtil.getFirstChildOfType(
        callNode.getChildren(), Token.CALL);
    assertEquals(2, functionNode.getChildren().size());

    Node functionTypeNode = functionNode.getFirstChild();
    assertEquals(1, functionTypeNode.getChildren().size());

    Node parameterNode = functionTypeNode.getChildren().get(0);
    assertEquals("", parameterNode.getString());

    Node checkNode = NodeUtil.getFirstChildOfType(
        functionTypeNode.getChildren(), Token.EXPR_LIST);
    assertEquals(2, checkNode.getChildren().size());

    Node classCheckerNode1 = checkNode.getChildren().get(0);
    assertEquals("classChecker", classCheckerNode1.getString());
    assertEquals("Foo", classCheckerNode1.getLastChild().getString());

    Node classCheckerNode2 = checkNode.getChildren().get(1);
    assertEquals("classChecker", classCheckerNode2.getString());
    assertEquals("Bar", classCheckerNode2.getLastChild().getString());

    TreeSet<JSType> typeSet = Sets.newTreeSet(ALPHA);
    Iterables.addAll(typeSet,
        new ObjectType(new ObjectType.Constructor(null, "Foo"), null),
        new ObjectType(new ObjectType.Constructor(null, "Bar"), null));
    assertEquals(typeSet,
        TypeChecker.checkTypes(
            compiler, NodeUtil.getFunctionType(functionNode)));
  }

  /**
   * Test for handling cases when the compiler doesn't have enough information
   * to check a type.
   */
  public void testCheckReturnTypesUnknownTypes() {
    String testCode = "function Foo() {" +
        "  return foo();" +
        "}" +
        "function foo() {}";

    Node root = Parse.parseCompilationUnit(testCode);
    runtimeTypeCheck.process(null, root);

    Node callNode = new Node(Token.CALL, NodeUtil.newQualifiedNameNode("Foo"));
    Node functionNode = NodeUtil.getFirstChildOfType(
        callNode.getChildren(), Token.CALL);
    assertEquals(2, functionNode.getChildren().size());

    Node functionTypeNode = functionNode.getFirstChild();
    assertEquals(1, functionTypeNode.getChildren().size());

    Node parameterNode = functionTypeNode.getChildren().get(0);
    assertEquals("", parameterNode.getString());

    Node checkNode = NodeUtil.getFirstChildOfType(
        functionTypeNode.getChildren(), Token.EXPR_LIST);
    assertEquals(0, checkNode.getChildren().size());
  }

  public void testRuntimeTypeCheckFunction() {
    String testCode = "function Foo() {}" +
        "function checkType(expr, checkers) {" +
        "  for (var i = 0; i < checkers.length; i++) {" +
        "    if (expr instanceof checkers[i]) {" +
        "      return expr;" +
        "    }" +
        "  }" +
        "  throw new TypeError('Expected one of the given types');" +
        "}" +
        "function foo() {}" +
        "function Baz() {" +
        "  var x = new Foo();" +
        "  checkType(x, [Function.prototype, ObjectType.getType(Foo)]);" +
        "}" +
        "Baz.prototype.myMethod = function() {}";

    Node root = Parse.parseCompilationUnit(testCode);
    runtimeTypeCheck.process(null, root);

    Node callNode = new Node(Token.CALL, NodeUtil.newQualifiedNameNode("Baz"));
    Node functionNode = NodeUtil.getFirstChildOfType(
        callNode.getChildren(), Token.CALL);
    assertEquals(2, functionNode.getChildren().size());

    Node functionTypeNode = functionNode.getFirstChild();
    assertEquals(1, functionTypeNode.getChildren().size());

    Node parameterNode = functionTypeNode.getChildren().get(0);
    assertEquals("", parameterNode.getString());

    Node checkNode = NodeUtil.getFirstChildOfType(
        functionTypeNode.getChildren(), Token.EXPR_LIST);
    assertEquals(2, checkNode.getChildren().size());

    Node arrayNode = checkNode.getChildren().get(1);
    assertEquals("Array", arrayNode.getTypeName());

    TreeSet<JSType> types = new TreeSet<>();
    types.add(FunctionType.FUNCTION_TYPE);
    types.add(FunctionType.FUNCTION_TYPE);
    types.add(NodeUtil.getFunctionType(NodeUtil.getFunction("Foo")));

    assertEquals(types,
        TypeChecker.checkTypes(compiler, NodeUtil.getFunctionType(functionNode)));
  }

  public void testRuntimeTypeCheckFunctionWithInterface() {
    String testCode = "function Foo() {}" +
        "interface FooInterface {myMethod()}" +
        "function Baz() {" +
        "  var x = new Foo();" +
        "  checkType(x, [Function.prototype, ObjectType.getType(FooInterface)]);" +
        "}" +
        "Baz.prototype.myMethod = function() {}";

    Node root = Parse.parseCompilationUnit(testCode);
    runtimeTypeCheck.process(null, root);

    Node callNode = new Node(Token.CALL, NodeUtil.newQualifiedNameNode("Baz"));
    Node functionNode = NodeUtil.getFirstChildOfType(
        callNode.getChildren(), Token.CALL);
    assertEquals(2, functionNode.getChildren().size());

    Node functionTypeNode = functionNode.getFirstChild();
    assertEquals(1, functionTypeNode.getChildren().size());

    Node parameterNode = functionTypeNode.getChildren().get(0);
    assertEquals("", parameterNode.getString());

    Node checkNode = NodeUtil.getFirstChildOfType(
        functionTypeNode.getChildren(), Token.EXPR_LIST);
    assertEquals(2, checkNode.getChildren().size());

    Node arrayNode = checkNode.getChildren().get(1);
    assertEquals("Array", arrayNode.getTypeName());

    TreeSet<JSType> types = new TreeSet<>();
    types.add(FunctionType.FUNCTION_TYPE);
    types.add(new ObjectType(
        new ObjectType.Constructor(null, "FooInterface"), null));

    assertEquals(types,
        TypeChecker.checkTypes(compiler, NodeUtil.getFunctionType(functionNode)));
  }

  public void testRuntimeTypeCheckFunctionWithUnionType() {
    String testCode = "function Foo() {}" +
        "function Bar() {}" +
        "function Baz() {" +
        "  var x = 10;" +
        "  checkType(x, [Number.TYPE, String.TYPE, Function.prototype, ObjectType.getType(Foo), ObjectType.getType(Bar)]);" +
        "}" +
        "Baz.prototype.myMethod = function() {}";

    Node root = Parse.parseCompilationUnit(testCode);
    runtimeTypeCheck.process(null, root);

    Node callNode = new Node(Token.CALL, NodeUtil.newQualifiedNameNode("Baz"));
    Node functionNode = NodeUtil.getFirstChildOfType(
        callNode.getChildren(), Token.CALL);
    assertEquals(2, functionNode.getChildren().size());

    Node functionTypeNode = functionNode.getFirstChild();
    assertEquals(1, functionTypeNode.getChildren().size());

    Node parameterNode = functionTypeNode.getChildren().get(0);
    assertEquals("", parameterNode.getString());

    Node checkNode = NodeUtil.getFirstChildOfType(
        functionTypeNode.getChildren(), Token.EXPR_LIST);
    assertEquals(6, checkNode.getChildren().size());

    TreeSet<JSType> types = new TreeSet<>();
    types.add(Number.TYPE);
    types.add(String.TYPE);
    types.add(FunctionType.FUNCTION_TYPE);
    types.add(NodeUtil.getFunctionType(NodeUtil.getFunction("Foo")));
    types.add(NodeUtil.getFunctionType(NodeUtil.getFunction("Bar")));

    assertEquals(types,
        TypeChecker.checkTypes(compiler, NodeUtil.getFunctionType(functionNode)));
  }
}