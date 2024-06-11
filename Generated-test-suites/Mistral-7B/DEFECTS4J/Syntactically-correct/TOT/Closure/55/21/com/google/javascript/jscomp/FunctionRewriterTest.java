package com.google.javascript.jscomp.test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static com.google.common.truth.Truth.*;

public class FunctionRewriterTest {

  @Test
  public void testEmptyFunctionReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = functionRewriter.parseSyntheticCode("EmptyFunctionTest",
        "function test() {}");
    Node expectedRoot = functionRewriter.parseSyntheticCode(
        "EmptyFunctionTest", "emptyFn();");

    functionRewriter.process(null, root);

    Node actualRoot = functionRewriter.getNodeForCode(
        "EmptyFunctionTest", expectedRoot.getString());
    assertThat(actualRoot).isNotNull();
    assertThat(actualRoot).isEqualTo(expectedRoot);
  }

  @Test
  public void testIdentityFunctionReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    String inputCode = "function test(a) {return a}";
    Node inputRoot = functionRewriter.parseSyntheticCode("IdentityFunctionTest", inputCode);
    Node expectedRoot = functionRewriter.parseSyntheticCode(
        "IdentityFunctionTest", "JSCompiler_identityFn()");

    functionRewriter.process(null, inputRoot);

    Node actualRoot = functionRewriter.getNodeForCode(
        "IdentityFunctionTest", expectedRoot.getString());
    assertThat(actualRoot).isNotNull();
    assertThat(actualRoot).isEqualTo(expectedRoot);
  }

  @Test
  public void testReturnConstantReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    String inputCode = "function test() {return 10}";
    Node inputRoot = functionRewriter.parseSyntheticCode("ReturnConstantTest", inputCode);
    Node expectedRoot = functionRewriter.parseSyntheticCode(
        "ReturnConstantTest", "returnconst(10)");

    functionRewriter.process(null, inputRoot);

    Node actualRoot = functionRewriter.getNodeForCode(
        "ReturnConstantTest", expectedRoot.getString());
    assertThat(actualRoot).isNotNull();
    assertThat(actualRoot).isEqualTo(expectedRoot);
  }

  @Test
  public void testGetterReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    String inputCode = "function test() {return this.a}";
    Node inputRoot = functionRewriter.parseSyntheticCode("GetterTest", inputCode);
    Node expectedRoot = functionRewriter.parseSyntheticCode(
        "GetterTest", "JSCompiler_get('a')");

    functionRewriter.process(null, inputRoot);

    Node actualRoot = functionRewriter.getNodeForCode(
        "GetterTest", expectedRoot.getString());
    assertThat(actualRoot).isNotNull();
    assertThat(actualRoot).isEqualTo(expectedRoot);
  }

  @Test
  public void testSetterReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    String inputCode = "function test(value) {this.a = value}";
    Node inputRoot = functionRewriter.parseSyntheticCode("SetterTest", inputCode);
    Node expectedRoot = functionRewriter.parseSyntheticCode(
        "SetterTest", "JSCompiler_set('a', JSCompiler_get('value'))");

    functionRewriter.process(null, inputRoot);

    Node actualRoot = functionRewriter.getNodeForCode(
        "SetterTest", expectedRoot.getString());
    assertThat(actualRoot).isNotNull();
    assertThat(actualRoot).isEqualTo(expectedRoot);
  }

  @Test
  public void testReductionWithComplexCode() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    String inputCode = "function test() {" +
        "  this.a = 10;" +
        "  return this.a;" +
        "}";
    Node inputRoot = functionRewriter.parseSyntheticCode("ComplexFunctionTest", inputCode);
    Node expectedRoot = functionRewriter.parseSyntheticCode(
        "ComplexFunctionTest", "JSCompiler_set('a', 10)" +
        "; return JSCompiler_get('a')");

    functionRewriter.process(null, inputRoot);

    Node actualRoot = functionRewriter.getNodeForCode(
        "ComplexFunctionTest", expectedRoot.getString());
    assertThat(actualRoot).isNotNull();
    assertThat(actualRoot).isEqualTo(expectedRoot);
  }

  @Test
  public void testReductionWithMissingReturnStatement() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    String inputCode = "function test() {}";
    Node inputRoot = functionRewriter.parseSyntheticCode("MissingReturnTest", inputCode);

    functionRewriter.process(null, inputRoot);

    Node actualRoot = functionRewriter.getNodeForCode(
        "MissingReturnTest", "function() {}");
    Node expectedRoot = functionRewriter.parseSyntheticCode(
        "MissingReturnTest", "function() {}");

    assertThat(actualRoot).isNotNull();
    assertThat(actualRoot).isEqualTo(expectedRoot);
  }
}