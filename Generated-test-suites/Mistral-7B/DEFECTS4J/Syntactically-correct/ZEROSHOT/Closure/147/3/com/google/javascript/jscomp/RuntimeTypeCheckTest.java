package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.FunctionType;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.UnionType;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class RuntimeTypeCheckTest {

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
  private Node root;
  private Node externs;
  private String logFunction;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions(null);
    compiler = new Compiler(options);
    externs = Node.objectLiteral();
    root = Node.script("function test() {}");
  }

  @Test
  public void testRuntimeTypeCheck() {
    RuntimeTypeCheck pass = new RuntimeTypeCheck(compiler, logFunction);
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(pass);
    passManager.runPasses(externs, root);

    FunctionType testFunctionType = (FunctionType) root.getJSType();
    Node testFunction = root.getFirstChild();

    assertEquals("test", testFunctionType.getName());
    assertEquals(1, testFunctionType.getParameters().size());

    Node testFunctionParameter = testFunctionType.getParameters().get(0);
    assertEquals("test", testFunctionParameter.getName());
    assertEquals(JSType.UNDEFINED, testFunctionParameter.getType());
    assertNull(testFunctionType.getReturnType());

    Node testFunctionBody = testFunction.getLastChild();
    assertEquals(0, testFunctionBody.getChildCount());

    pass = new RuntimeTypeCheck(compiler, "function(warning, expr) {}", "");
    passManager = new CompilerPassManager(compiler);
    passManager.addPass(pass);
    passManager.runPasses(externs, root);

    testFunctionType = (FunctionType) root.getJSType();
    testFunction = root.getFirstChild();

    assertEquals("test", testFunctionType.getName());
    assertEquals(1, testFunctionType.getParameters().size());

    testFunctionParameter = testFunctionType.getParameters().get(0);
    assertEquals("test", testFunctionParameter.getName());
    assertEquals(JSType.ANY, testFunctionParameter.getType());
    assertNull(testFunctionType.getReturnType());

    testFunctionBody = testFunction.getLastChild();
    assertEquals(0, testFunctionBody.getChildCount());

    pass = new RuntimeTypeCheck(compiler, null);
    passManager = new CompilerPassManager(compiler);
    passManager.addPass(pass);
    passManager.runPasses(externs, root);

    testFunctionType = (FunctionType) root.getJSType();
    testFunction = root.getFirstChild();

    assertEquals("test", testFunctionType.getName());
    assertEquals(1, testFunctionType.getParameters().size());

    testFunctionParameter = testFunctionType.getParameters().get(0);
    assertEquals("test", testFunctionParameter.getName());
    assertEquals(JSType.UNDEFINED, testFunctionParameter.getType());
    assertNull(testFunctionType.getReturnType());

    Node testFunctionBody1 = testFunction.getLastChild();
    assertEquals(0, testFunctionBody1.getChildCount());

    FunctionType testFunctionType1 = (FunctionType) testFunctionType;
    ObjectType objectType = new ObjectType("Foo");
    testFunctionType1.addInterface(objectType);

    Node nodeToInsertAfter = findNodeToInsertAfter(testFunctionBody1);

    nodeToInsertAfter = addMarker(testFunctionType1, nodeToInsertAfter, objectType);

    testFunctionBody1 = testFunction.getLastChild();
    assertEquals(1, testFunctionBody1.getChildCount());

    pass = new RuntimeTypeCheck(compiler, "function(warning, expr) {}", "");
    passManager = new CompilerPassManager(compiler);
    passManager.addPass(pass);
    passManager.runPasses(externs, root);

    testFunctionType = (FunctionType) root.getJSType();
    testFunction = root.getFirstChild();

    assertEquals("test", testFunctionType.getName());
    assertEquals(1, testFunctionType.getParameters().size());

    testFunctionParameter = testFunctionType.getParameters().get(0);
    assertEquals("test", testFunctionParameter.getName());
    assertEquals(JSType.UNDEFINED, testFunctionParameter.getType());
    assertNull(testFunctionType.getReturnType());

    testFunctionBody1 = testFunction.getLastChild();
    assertEquals(2, testFunctionBody1.getChildCount());

    TreeSet<ObjectType> stuff = Sets.newTreeSet(ALPHA);
    Iterables.addAll(stuff, testFunctionType.getAllImplementedInterfaces());
    assertEquals(1, stuff.size());
    assertEquals("Foo", stuff.iterator().next().getReferenceName());

    Node marker = testFunctionBody1.getChild(1);
    assertEquals(Token.GETPROP, marker.getType());
    assertEquals("prototype", marker.getFirstChild().getString());
    assertEquals("implements__Foo", marker.getLastChild().getString());

    testFunctionBody1 = testFunction.getLastChild();
    assertEquals(2, testFunctionBody1.getChildCount());

    Node checkTypeCallNode = testFunctionBody1.getChild(1);
    assertEquals(Token.CALL, checkTypeCallNode.getType());
    assertEquals("checkType", checkTypeCallNode.getFirstChild().getName());

    Node checkTypeArguments = checkTypeCallNode.getLastChild();
    assertEquals(2, checkTypeArguments.getChildCount());

    Node checkerNode1 = checkTypeArguments.getChild(0);
    assertEquals(Token.CALL, checkerNode1.getType());
    assertEquals("valueChecker", checkerNode1.getFirstChild().getName());
    assertEquals("any", checkerNode1.getLastChild().getString());

    Node checkerNode2 = checkTypeArguments.getChild(1);
    assertEquals(Token.CALL, checkerNode2.getType());
    assertEquals("interfaceChecker", checkerNode2.getFirstChild().getName());
    assertEquals("Foo", checkerNode2.getLastChild().getString());

    Node checkTypeCheckNode = checkTypeCallNode.getLastChild();
    assertEquals(Token.EXPR_RESULT, checkTypeCheckNode.getType());
    assertEquals(testFunctionBody1.getChild(0), checkTypeCheckNode.getFirstChild());
  }

  private Node findNodeToInsertAfter(Node node) {
    while (node.getParent().getType() != Token.SCRIPT &&
        node.getParent().getType() != Token.BLOCK) {
      node = node.getParent();
    }
    return node;
  }

  private Node addMarker(FunctionType funType, Node nodeToInsertAfter,
      @Nullable ObjectType interfaceType) {

    if (funType.getSource() == null) {
      return nodeToInsertAfter;
    }

    String className = NodeUtil.getFunctionName(funType.getSource());

    Node classNode = NodeUtil.newQualifiedNameNode(className, -1, -1);

    Node marker = Node.newString(
            interfaceType == null ?
            "instance_of__" + className :
            "implements__" + interfaceType.getReferenceName());

    Node assign = new Node(Token.EXPR_RESULT, new Node(Token.ASSIGN,
        new Node(Token.GETELEM,
            new Node(Token.GETPROP,
                classNode,
                Node.newString("prototype")), marker),
        new Node(Token.TRUE)));

    nodeToInsertAfter.getParent().addChildAfter(assign, nodeToInsertAfter);
    compiler.reportCodeChange();
    nodeToInsertAfter = assign;
    return nodeToInsertAfter;
  }
}