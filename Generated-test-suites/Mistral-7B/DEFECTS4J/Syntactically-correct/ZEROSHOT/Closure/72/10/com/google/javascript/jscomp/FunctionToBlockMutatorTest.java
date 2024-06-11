package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.common.collect.LinkedHashMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.FunctionToBlockMutator.LabelNameSupplier;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.Map;
import java.util.Set;

/**
 * Tests for the FunctionToBlockMutator class.
 */
public class FunctionToBlockMutatorTest {

  private AbstractCompiler compiler;

  @org.junit.Before
  public void setUp() {
    compiler = new AbstractCompiler();
  }

  @org.junit.Test
  public void testMutateFunction() {
    FunctionToBlockMutator functionToBlockMutator =
        new FunctionToBlockMutator(compiler, new LabelNameSupplier(compiler.getUniqueNameIdSupplier()));
    Node fnNode = NodeUtil.parseString("function foo(x) { return x * 2; }");
    Node callNode = NodeUtil.parseString("foo(3);");
    Node resultNode = NodeUtil.newString(Token.NAME, "result");
    Node resultBlock = functionToBlockMutator.mutate(
        "foo", fnNode, callNode, resultNode.getString(), false, false);
    assertNotNull(resultBlock);
    Node expectedBlock = NodeUtil.parseString(
        "JSCompiler_inline_label_foo_" + compiler.getUniqueNameIdSupplier().get() + ": {\n" +
        "  var x = 3;\n" +
        "  x = x * 2;\n" +
        "  result = x;\n" +
        "  break JSCompiler_inline_label_foo_" + compiler.getUniqueNameIdSupplier().get() + ";\n" +
        "}");
    assertEquals(expectedBlock.toString(), resultBlock.toString());
  }

  @org.junit.Test
  public void testMutateFunctionWithArguments() {
    FunctionToBlockMutator functionToBlockMutator =
        new FunctionToBlockMutator(compiler, new LabelNameSupplier(compiler.getUniqueNameIdSupplier()));
    Node fnNode = NodeUtil.parseString("function foo(x, y) { return x + y; }");
    Node callNode = NodeUtil.parseString("foo(3, 4);");
    Map<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("x", NodeUtil.newNumber(3));
    argMap.put("y", NodeUtil.newNumber(4));
    Node resultNode = NodeUtil.newString(Token.NAME, "result");
    Node resultBlock = functionToBlockMutator.mutate(
        "foo", fnNode, callNode, resultNode.getString(), false, false);
    assertNotNull(resultBlock);
    Node expectedBlock = NodeUtil.parseString(
        "JSCompiler_inline_label_foo_" + compiler.getUniqueNameIdSupplier().get() + ": {\n" +
        "  var x = 3;\n" +
        "  var x_1 = x;\n" +
        "  x = x_1 + y;\n" +
        "  result = x;\n" +
        "  break JSCompiler_inline_label_foo_" + compiler.getUniqueNameIdSupplier().get() + ";\n" +
        "}");
    assertEquals(expectedBlock.toString(), resultBlock.toString());
  }

  @org.junit.Test
  public void testMutateFunctionWithArgumentsAndAlias() {
    FunctionToBlockMutator functionToBlockMutator =
        new FunctionToBlockMutator(compiler, new LabelNameSupplier(compiler.getUniqueNameIdSupplier()));
    Node fnNode = NodeUtil.parseString("function foo(x, y) { return x + y; }");
    Node callNode = NodeUtil.parseString("foo(3, 4);");
    Map<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("x", NodeUtil.newNumber(3).rename("x_alias"));
    argMap.put("y", NodeUtil.newNumber(4).rename("y_alias"));
    Node resultNode = NodeUtil.newString(Token.NAME, "result");
    Node resultBlock = functionToBlockMutator.mutate(
        "foo", fnNode, callNode, resultNode.getString(), false, false);
    assertNotNull(resultBlock);
    Node expectedBlock = NodeUtil.parseString(
        "JSCompiler_inline_label_foo_" + compiler.getUniqueNameIdSupplier().get() + ": {\n" +
        "  var x_alias = 3;\n" +
        "  var x = x_alias;\n" +
        "  var y_alias = 4;\n" +
        "  var y = y_alias;\n" +
        "  x = x + y;\n" +
        "  result = x;\n" +
        "  break JSCompiler_inline_label_foo_" + compiler.getUniqueNameIdSupplier().get() + ";\n" +
        "}");
    assertEquals(expectedBlock.toString(), resultBlock.toString());
  }

  @org.junit.Test
  public void testMutateFunctionWithLockedArguments() {
    FunctionToBlockMutator functionToBlockMutator =
        new FunctionToBlockMutator(compiler, new LabelNameSupplier(compiler.getUniqueNameIdSupplier()));
    Node fnNode = NodeUtil.parseString("function foo(x, y) { return x + y; }");
    Node callNode = NodeUtil.parseString("foo(3, 4);");
    Map<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("x", NodeUtil.newNumber(3));
    argMap.put("y", NodeUtil.newNumber(4));
    Set<String> namesToAlias = Sets.newHashSet("x");
    Node resultNode = NodeUtil.newString(Token.NAME, "result");
    Node resultBlock = functionToBlockMutator.mutate(
        "foo", fnNode, callNode, resultNode.getString(), false, false);
    assertNotNull(resultBlock);
    Node expectedBlock = NodeUtil.parseString(
        "JSCompiler_inline_label_foo_" + compiler.getUniqueNameIdSupplier().get() + ": {\n" +
        "  var x__1 = x;\n" +
        "  var x = x__1;\n" +
        "  var y = 4;\n" +
        "  x = x + y;\n" +
        "  result = x;\n" +
        "  break JSCompiler_inline_label_foo_" + compiler.getUniqueNameIdSupplier().get() + ";\n" +
        "}");
    assertEquals(expectedBlock.toString(), resultBlock.toString());
  }

  @org.junit.Test
  public void testMutateFunctionWithLoopCall() {
    FunctionToBlockMutator functionToBlockMutator =
        new FunctionToBlockMutator(compiler, new LabelNameSupplier(compiler.getUniqueNameIdSupplier()));
    Node fnNode = NodeUtil.parseString("function foo(x) { return x * 2; }");
    Node callNode = NodeUtil.parseString("for (var i = 0; i < 5; i++) { foo(i); }");
    Node resultNode = NodeUtil.newString(Token.NAME, "result");
    Node resultBlock = functionToBlockMutator.mutate(
        "foo", fnNode, callNode, resultNode.getString(), false, true);
    assertNotNull(resultBlock);
    Node expectedBlock = NodeUtil.parseString(
        "for (var i = 0; i < 5; i++) {\n" +
        "  JSCompiler_inline_label_foo_" + compiler.getUniqueNameIdSupplier().get() + ":" + "\n" +
        "    var x = i;\n" +
        "    x = x * 2;\n" +
        "    result = x;\n" +
        "    break JSCompiler_inline_label_foo_" + compiler.getUniqueNameIdSupplier().get() + ";\n" +
        "}\n");
    assertEquals(expectedBlock.toString(), resultBlock.toString());
  }

  @org.junit.Test
  public void testMutateFunctionWithResult() {
    FunctionToBlockMutator functionToBlockMutator =
        new FunctionToBlockMutator(compiler, new LabelNameSupplier(compiler.getUniqueNameIdSupplier()));
    Node fnNode = NodeUtil.parseString("function foo(x) { return x * 2; }");
    Node callNode = NodeUtil.parseString("foo(3);");
    Node resultNode = NodeUtil.newString(Token.NAME, "result");
    Node resultBlock = functionToBlockMutator.mutate(
        "foo", fnNode, callNode, resultNode.getString(), true, false);
    assertNotNull(resultBlock);
    Node expectedBlock = NodeUtil.parseString(
        "JSCompiler_inline_label_foo_" + compiler.getUniqueNameIdSupplier().get() + ": {\n" +
        "  var x = 3;\n" +
        "  x = x * 2;\n" +
        "  result = x;\n" +
        "  break JSCompiler_inline_label_foo_" + compiler.getUniqueNameIdSupplier().get() + ";\n" +
        "}");
    assertEquals(expectedBlock.toString(), resultBlock.toString());
  }
}