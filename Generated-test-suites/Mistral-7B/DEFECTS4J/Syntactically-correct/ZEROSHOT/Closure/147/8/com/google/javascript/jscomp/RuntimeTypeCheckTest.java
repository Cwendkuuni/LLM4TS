package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.NodeUtil;

public class RuntimeTypeCheckTest {

  @Test
  public void testRuntimeTypeCheck() {
    String sourceCode = "function Foo() {}";
    Node externs = NodeUtil.parseJs(sourceCode);
    Node root = NodeUtil.parseJs("function Bar(x) { return new Foo(); }");

    AbstractCompiler compiler = new Compiler(null);
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(externs, root);

    Node expected = NodeUtil.parseJs("function Bar(x) {\n"
        + "  checkType(x, [function(x) { return x instanceof Foo; }]);\n"
        + "  return new Foo();\n"
        + "}");

    Node actual = NodeTraversal.deepFirst(root);
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testRuntimeTypeCheckWithUnionType() {
    String sourceCode = "function Foo() {} function Bar(x) { return x; }";
    Node externs = NodeUtil.parseJs(sourceCode);
    Node root = NodeUtil.parseJs("function Baz(x) { return x; }");

    AbstractCompiler compiler = new Compiler(null);
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(externs, root);

    Node expected = NodeUtil.parseJs("function Baz(x) {\n"
        + "  checkType(x, [function(x) { return x instanceof Foo; }),\n"
        + "            function(x) { return x instanceof Bar; }]);\n"
        + "  return x;\n"
        + "}");

    Node actual = NodeTraversal.deepFirst(root);
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testRuntimeTypeCheckWithInterface() {
    String sourceCode = "interface MyInterface {\n"
        + "  method1(): void;\n"
        + "  method2(): number;\n"
        + "} function Foo() implements MyInterface {\n"
        + "  method1() {} function method2() { return 42; }\n"
        + "} function Bar(x) { return new Foo(); }";
    Node externs = NodeUtil.parseJs(sourceCode);
    Node root = NodeUtil.parseJs("function Bar(x) { return x; }");

    AbstractCompiler compiler = new Compiler(null);
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(externs, root);

    Node expected = NodeUtil.parseJs("function Bar(x) {\n"
        + "  checkType(x, [function(x) { return x instanceof Foo; }),\n"
        + "            function(x) {\n"
        + "              var result = jscomp.typecheck.checkInterface(x, MyInterface);\n"
        + "              if (!result) {\n"
        + "                throw new Error('Type check failed for Bar: expected an instance of MyInterface');\n"
        + "              }\n"
        + "            }]);\n"
        + "  return x;\n"
        + "}");

    Node actual = NodeTraversal.deepFirst(root);
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testRuntimeTypeCheckWithExternClass() {
    String sourceCode = "function Foo() {}";
    Node externs = NodeUtil.parseJs(sourceCode);
    Node root = NodeUtil.parseJs("function Bar(x) { return new external.Foo(); }");

    AbstractCompiler compiler = new Compiler(null);
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(externs, root);

    Node expected = NodeUtil.parseJs("function Bar(x) {\n"
        + "  checkType(x, [function(x) { return x instanceof external.Foo; }]);\n"
        + "  return new external.Foo();\n"
        + "}");

    Node actual = NodeTraversal.deepFirst(root);
    assertEquals(expected.toString(), actual.toString());
  }
}