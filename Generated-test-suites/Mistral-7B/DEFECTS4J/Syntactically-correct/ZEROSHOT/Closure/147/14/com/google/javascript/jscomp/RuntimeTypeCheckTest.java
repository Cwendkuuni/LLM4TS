package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerTestCase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.jstype.JSType;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link RuntimeTypeCheck}.
 */
public class RuntimeTypeCheckTest extends CompilerTestCase {

  public RuntimeTypeCheckTest() {
    super(new CompilerOptions()
        .addInclude("test/RuntimeTypeCheckTest.js")
        .addInclude("test/externs/runtime-type-check.js")
        .setPrintErrorMessages(false)
        .setPrintWarningMessages(true)
        .setOutputFile("output/RuntimeTypeCheckTest-out.js")
        .setGenerator(new SimpleGenerator()));
  }

  @Override
  protected CompilerPass getProcessor(Compiler compiler) {
    return new RuntimeTypeCheck(compiler, null);
  }

  /**
   * Tests that function parameters are checked for the correct types.
   */
  public void testFunctionParameters() {
    String input =
        "function foo(x, y) {"
        + "  x = 5;"
        + "  y = 'hello';"
        + "  return x + y;"
        + "}";

    Node ast = parseTestCode(input);
    processTestCode(ast);

    Node function = NodeUtil.getFirstChild(ast);
    Node block = NodeUtil.getLastChild(function);

    List<Node> checks = Arrays.asList(
        NodeUtil.getChildAtIndex(block, 0),
        NodeUtil.getChildAtIndex(block, 1)
    );

    assertEquals(2, checks.size());

    for (Node check : checks) {
      assertEquals("jscomp.typecheck.checkType", check.getFirstChild().getNodeType());
      assertEquals("valueChecker", check.getLastChild().getNodeType());
    }

    // Check the type of the first parameter.
    assertEquals("number", checkType(function, 0, 0));

    // Check the type of the second parameter.
    assertEquals("string", checkType(function, 1, 1));
  }

  /**
   * Tests that a return value is checked for the correct type.
   */
  public void testFunctionReturnValue() {
    String input = "function foo() { return 5; }";

    Node ast = parseTestCode(input);
    processTestCode(ast);

    Node function = NodeUtil.getFirstChild(ast);
    Node block = NodeUtil.getLastChild(function);

    List<Node> checks = Arrays.asList(
        NodeUtil.getLastChild(block)
    );

    assertEquals(1, checks.size());

    assertEquals("jscomp.typecheck.checkType", checks.get(0).getFirstChild().getNodeType());
    assertEquals("valueChecker", checks.get(0).getLastChild().getNodeType());

    // Check the type of the function return value.
    assertEquals("number", checkType(function, 0, 0));
  }

  /**
   * Tests that constructor calls are checked.
   */
  public void testConstructorCalls() {
    String input = "function Foo(x) { this.x = x; }";
    String code = "new Foo(5)";

    Node ast = parseTestCode(input + "\n" + code);
    processTestCode(ast);

    Node constructorCall = NodeUtil.getFirstChild(ast);

    List<Node> checks = Arrays.asList(
        NodeUtil.getChildAtIndex(constructorCall, 1)
    );

    assertEquals(1, checks.size());

    assertEquals("jscomp.typecheck.checkType", checks.get(0).getFirstChild().getNodeType());
    assertEquals("classChecker", checks.get(0).getLastChild().getNodeType());
    assertEquals("Foo", getClassName(checks.get(0).getLastChild().getLastChild()));

    // Check the type of the constructor argument.
    assertEquals("number", checkType(constructorCall, 0, 0));
  }

  /**
   * Tests that constructor calls with no arguments are checked.
   */
  public void testConstructorCallsWithNoArguments() {
    String input = "function Foo() {}";
    String code = "new Foo()";

    Node ast = parseTestCode(input + "\n" + code);
    processTestCode(ast);

    Node constructorCall = NodeUtil.getFirstChild(ast);

    List<Node> checks = Arrays.asList(
        NodeUtil.getChildAtIndex(constructorCall, 1)
    );

    assertEquals(1, checks.size());

    assertEquals("jscomp.typecheck.checkType", checks.get(0).getFirstChild().getNodeType());
    assertEquals("classChecker", checks.get(0).getLastChild().getNodeType());
    assertEquals("Foo", getClassName(checks.get(0).getLastChild().getLastChild()));

    // Check that there are no type checks for the constructor call arguments.
    assertEquals(0, NodeUtil.getChildCount(constructorCall));
  }

  /**
   * Tests that anonymous functions are checked.
   */
  public void testAnonymousFunction() {
    String input = "var x = 5;\n" +
        "function foo() {\n" +
        "  return function(y) {\n" +
        "    x += y;\n" +
        "  };\n" +
        "}";

    Node ast = parseTestCode(input);
    processTestCode(ast);

    Node anonymousFunction = NodeUtil.getLastChild(ast);
    Node block = NodeUtil.getLastChild(anonymousFunction);

    List<Node> checks = Arrays.asList(
        NodeUtil.getLastChild(block)
    );

    assertEquals(1, checks.size());

    assertEquals("jscomp.typecheck.checkType", checks.get(0).getFirstChild().getNodeType());
    assertEquals("valueChecker", checks.get(0).getLastChild().getNodeType());

    // Check the type of the anonymous function argument.
    assertEquals("number", checkType(anonymousFunction, 0, 0));
  }

  private String getClassName(Node node) {
    if (node.getNodeType() != Token.GETPROP) {
      throw new IllegalArgumentException("Expected a getprop node.");
    }
    return NodeUtil.getQualifiedName(node.getFirstChild());
  }

  private String checkType(Node node, int index, int depth) {
    Node checkTypeCall = NodeUtil.getChildAtIndex(node, depth);
    JSType type = checkTypeCall.getLastChild().getLastChild().getJSType();
    return type.isInstanceType() ? type.getReferenceName() : type.toString();
  }
}