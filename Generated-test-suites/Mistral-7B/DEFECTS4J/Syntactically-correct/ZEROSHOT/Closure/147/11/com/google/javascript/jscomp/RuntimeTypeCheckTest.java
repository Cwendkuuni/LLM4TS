package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.NodeUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class RuntimeTypeCheckTest {

  private AbstractCompiler compiler;
  private RuntimeTypeCheck runtimeTypeCheck;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
  }

  @Test
  public void testAddMarkers() {
    String source = "function Foo() {}";
    CompilerInput input = new CompilerInput("test.js", source, null, null);
    Node root = NodeUtil.parseJs(source);
    runtimeTypeCheck.process(input, root);

    Node classNode = NodeUtil.newQualifiedNameNode("Foo");
    Node marker = NodeUtil.getFirstChildOfType(classNode, Token.GETPROP);
    assertNotNull(marker);
    assertEquals("prototype", marker.getFirstChild().toString());
    assertEquals("instance_of__Foo", marker.getLastChild().toString());
  }

  @Test
  public void testAddChecksForFunction() {
    String source = "function Foo(x: number) { return x; }";
    CompilerInput input = new CompilerInput("test.js", source, null, null);
    Node root = NodeUtil.parseJs(source);
    runtimeTypeCheck.process(input, root);

    Node functionNode = NodeUtil.getFunctionNode(root, "Foo");
    Node checkNode = NodeUtil.getFirstChildOfType(functionNode, Token.CALL);
    assertNotNull(checkNode);
    assertEquals("checkType", checkNode.getFirstChild().toString());

    Node paramNode = NodeUtil.getFunctionParameter(functionNode, 0);
    checkNode = NodeUtil.getFirstChildOfType(paramNode, Token.GET);
    assertNotNull(checkNode);
    assertEquals("valueChecker", checkNode.getFirstChild().toString());
    assertEquals("number", checkNode.getLastChild().toString());
  }

  @Test
  public void testAddChecksForClassAndInterface() throws IOException {
    String source = "interface IFoo {" +
            "  bar: number;" +
            "}" +
            "" +
            "class Foo implements IFoo {" +
            "  foo: string;" +
            "  bar: number;" +
            "}" +
            "" +
            "function FooFactory() {" +
            "  return new Foo();" +
            "}" +
            "function test() {" +
            "  var foo = FooFactory();" +
            "  checkType(foo, [" +
            "      valueChecker('string')," +
            "      classChecker('Foo')," +
            "      interfaceChecker('IFoo')" +
            "  ]);" +
            "}" +
            "";

    CompilerInput input = new CompilerInput("test.js", source, null, null);
    Node root = NodeUtil.parseJs(source);
    runtimeTypeCheck.process(input, root);

    Node checkNode = NodeUtil.getFirstChildOfType(NodeUtil.getFunctionNode(root, "test"), Token.CALL);
    assertNotNull(checkNode);
    assertEquals("checkType", checkNode.getFirstChild().toString());

    Node fooNode = NodeUtil.getFirstChildOfType(NodeUtil.getFunctionNode(root, "FooFactory"), Token.GETNEW);
    assertNotNull(fooNode);

    checkNode = NodeUtil.getFirstChildOfType(fooNode, Token.CALL);
    assertNotNull(checkNode);
    assertEquals("classChecker", checkNode.get(1).getFirstChild().toString());
    assertEquals("Foo", checkNode.get(1).getLastChild().toString());

    checkNode = NodeUtil.getChildAfter(checkNode, 0);
    assertEquals("interfaceChecker", checkNode.getFirstChild().toString());
    assertEquals("IFoo", checkNode.getLastChild().toString());
  }

  @Test
  public void testAddChecksForFunctionWithMultipleParameters() {
    String source = "function Foo(x: number, y: boolean) { return x + y; }";
    CompilerInput input = new CompilerInput("test.js", source, null, null);
    Node root = NodeUtil.parseJs(source);
    runtimeTypeCheck.process(input, root);

    List<Node> parameters = NodeUtil.getFunctionParameters(NodeUtil.getFunctionNode(root, "Foo"));
    assertEquals(2, parameters.size());

    Node checkNode = NodeUtil.getFirstChildOfType(parameters.get(0), Token.GET);
    assertNotNull(checkNode);
    assertEquals("valueChecker", checkNode.getFirstChild().toString());
    assertEquals("number", checkNode.getLastChild().toString());

    checkNode = NodeUtil.getFirstChildOfType(parameters.get(1), Token.GET);
    assertNotNull(checkNode);
    assertEquals("valueChecker", checkNode.getFirstChild().toString());
    assertEquals("boolean", checkNode.getLastChild().toString());
  }

  @Test
  public void testAddChecksForFunctionWithReturnValue() {
    String source = "function Foo() { return 42; }";
    CompilerInput input = new CompilerInput("test.js", source, null, null);
    Node root = NodeUtil.parseJs(source);
    runtimeTypeCheck.process(input, root);

    Node functionNode = NodeUtil.getFunctionNode(root, "Foo");
    Node checkNode = NodeUtil.getFirstChildOfType(functionNode.getLastChild(), Token.CALL);
    assertNotNull(checkNode);
    assertEquals("checkType", checkNode.getFirstChild().toString());

    Node returnNode = NodeUtil.getFirstChildOfType(functionNode.getLastChild(), Token.RETURN);
    checkNode = NodeUtil.getFirstChildOfType(returnNode, Token.GET);
    assertNotNull(checkNode);
    assertEquals("valueChecker", checkNode.getFirstChild().toString());
    assertEquals("number", checkNode.getLastChild().toString());
  }

  @Test
  public void testAddChecksForFunctionWithVoidReturnType() {
    String source = "function Foo() {}";
    CompilerInput input = new CompilerInput("test.js", source, null, null);
    Node root = NodeUtil.parseJs(source);
    runtimeTypeCheck.process(input, root);

    Node functionNode = NodeUtil.getFunctionNode(root, "Foo");
    Node checkNode = NodeUtil.getFirstChildOfType(functionNode.getLastChild(), Token.CALL);
    assertNotNull(checkNode);
    assertEquals("checkType", checkNode.getFirstChild().toString());

    Node returnNode = NodeUtil.getFirstChildOfType(functionNode.getLastChild(), Token.RETURN);
    assertNull(returnNode);
  }

  @Test
  public void testAddChecksForUnionType() {
    String source = "function Foo(x: number | string) { return x; }";
    CompilerInput input = new CompilerInput("test.js", source, null, null);
    Node root = NodeUtil.parseJs(source);
    runtimeTypeCheck.process(input, root);

    List<Node> parameters = NodeUtil.getFunctionParameters(NodeUtil.getFunctionNode(root, "Foo"));
    assertEquals(1, parameters.size());

    Node checkNode = NodeUtil.getFirstChildOfType(parameters.get(0), Token.GET);
    assertNotNull(checkNode);
    assertEquals("unionChecker", checkNode.getFirstChild().toString());
    Node unionTypeNode = checkNode.getLastChild();

    Set<JSType> alternatives = new HashSet<>();
    Node child = unionTypeNode.getFirstChild();
    while (child != null) {
      alternatives.add(child.getJSType());
      child = child.getNext();
    }

    assertEquals(2, alternatives.size());
    assertTrue(alternatives.contains(JSType.getNumberType()));
    assertTrue(alternatives.contains(JSType.getStringType()));
  }

  @Test
  public void testAddChecksForExternedClass() {
    String source = "interface IFoo {" +
            "  bar: number;" +
            "}" +
            "" +
            "class Foo {" +
            "  foo: string;" +
            "  bar: number;" +
            "}" +
            "" +
            "extern class ExternFoo {" +
            "  static function doSomething() {" +
            "    var foo = new Foo();" +
            "    checkType(foo, [" +
            "        valueChecker('string')," +
            "        classChecker('Foo')," +
            "        interfaceChecker('IFoo')" +
            "    ]);" +
            "  }" +
            "}" +
            "";

    CompilerInput input = new CompilerInput("test.js", source, null, null);
    Node root = NodeUtil.parseJs(source);
    runtimeTypeCheck.process(input, root);

    Node checkNode = NodeUtil.getFirstChildOfType(NodeUtil.getFunctionNode(root, "ExternFoo.doSomething"), Token.CALL);
    assertNotNull(checkNode);
    assertEquals("classChecker", checkNode.get(1).getFirstChild().toString());
    assertEquals("Foo", checkNode.get(1).getLastChild().toString());
  }

  @Test
  public void testLogFunction() {
    String source = "function Foo() {}";
    CompilerInput input = new CompilerInput("test.js", source, null, null);
    Node root = NodeUtil.parseJs(source);
    runtimeTypeCheck.process(input, root);

    Node logFunctionNode = NodeUtil.getFunctionNode(root, "checkType");
    Node logFunctionCallNode = NodeUtil.getFirstChildOfType(logFunctionNode, Token.CALL);
    Node logFunction = logFunctionCallNode.get(1);

    Node logFunctionCheckerNode = NodeUtil.getFirstChildOfType(logFunction, Token.GET);
    assertNotNull(logFunctionCheckerNode);
    assertEquals("function", logFunctionCheckerNode.getFirstChild().toString());
    assertEquals("(warning: any, expr: any) => void", logFunctionCheckerNode.getLastChild().toString());
  }
}