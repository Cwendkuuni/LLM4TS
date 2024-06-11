package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserTestingHelper;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class FunctionRewriterTest {
  private NodeFactory nodeFactory;
  private CompilerOptions options;

  @Before
  public void setup() {
    nodeFactory = ParserTestingHelper.newNodeFactory();
    options = new CompilerOptions();
  }

  @Test
  public void testEmptyFunction() {
    String jsCode = "function F() {}";
    Node root = ParserTestingHelper.parseAndTraverse(
        nodeFactory,
        jsCode,
        options,
        new FunctionRewriter(new Compiler(options)));

    Node functionNode = root.lookupVariable("F");
    FunctionRewriter rewriter = new FunctionRewriter(options);
    rewriter.process(null, root);

    Node expectedHelperCode = nodeFactory.parseTemplate(
        "function helper() {return function() {}}", null);
    Node helperCode = root.getChildAt(0);
    assertThat(helperCode).isEqualTo(expectedHelperCode);

    Node expectedFunctionNode = nodeFactory.parseTemplate(
        "function F() {return helper();}", null);
    Node actualFunctionNode = functionNode.getFirstChild();
    assertThat(actualFunctionNode).isEqualTo(expectedFunctionNode);
  }

  @Test
  public void testIdentityFunction() {
    String jsCode = "function F(x) {return x;}";
    Node root = ParserTestingHelp.parseAndTraverse(
        nodeFactory,
        jsCode,
        options,
        new FunctionRewriter(new Compiler(options)));

    FunctionRewriter rewriter = new FunctionRewriter(options);
    rewriter.process(null, root);

    Node expectedHelperCode = nodeFactory.parseTemplate(
        "function helper(helperValue) {return function(x) {return helperValue;}}",
        null);
    Node helperCode = root.getChildAt(0);
    assertThat(helperCode).isEqualTo(expectedHelperCode);

    Node expectedFunctionNode = nodeFactory.parseTemplate(
        "function F(x) {return helper(function(x) {return x;});}", null);
    Node actualFunctionNode = functionNode.getFirstChild();
    assertThat(actualFunctionNode).isEqualTo(expectedFunctionNode);
  }

  @Test
  public void testGetter() {
    String jsCode = "function C() {" +
        "this.a_ = 1;" +
        "this.getA = function() {return this.a_;}";
    Node root = ParserTestingHelp.parseAndTraverse(
        nodeFactory,
        jsCode,
        options,
        new FunctionRewriter(new Compiler(options)));

    FunctionRewriter rewriter = new FunctionRewriter(options);
    rewriter.process(null, root);

    Node expectedHelperCode = nodeFactory.parseTemplate(
        "function helper(helperName) {return function() {return this[helperName];}}",
        null);
    Node helperCode = root.getChildAt(0);
    assertThat(helperCode).isEqualTo(expectedHelperCode);

    Node expectedFunctionNode = nodeFactory.parseTemplate(
        "function C() {" +
        "this.a_ = 1;" +
        "this.getA = helper('a_');}", null);
    Node actualFunctionNode = root.getFirstChild().getLastChild();
    Node functionNode = actualFunctionNode.getFirstChild();
    Node getterNode = functionNode.getLastChild();
    assertThat(getterNode).isEqualTo(expectedFunctionNode.getLastChild());
  }

  @Test
  public void testSetter() {
    String jsCode = "function C() {" +
        "this.a_ = 1;" +
        "this.setA = function(value) {this.a_ = value;}";
    Node root = ParserTestingHelp.parseAndTraverse(
        nodeFactory,
        jsCode,
        options,
        new FunctionRewriter(new Compiler(options)));

    FunctionRewriter rewriter = new FunctionRewriter(options);
    rewriter.process(null, root);

    Node expectedHelperCode = nodeFactory.parseTemplate(
        "function helper(helperName) {return function(value) {this[helperName] = value;}}",
        null);
    Node helperCode = root.getChildAt(0);
    assertThat(helperCode).isEqualTo(expectedHelperCode);

    Node expectedFunctionNode = nodeFactory.parseTemplate(
        "function C() {" +
        "this.a_ = 1;" +
        "this.setA = helper('a_');}", null);
    Node actualFunctionNode = root.getFirstChild().getLastChild();
    Node functionNode = actualFunctionNode.getFirstChild();
    Node setterNode = functionNode.getLastChild();
    assertThat(setterNode).isEqualTo(expectedFunctionNode.getLastChild());
  }

  @Test
  public void testNonReducibleFunction() {
    String jsCode = "function F() {" +
        "function G() {}" +
        "return G();}";
    Node root = ParserTestingHelp.parseAndTraverse(
        nodeFactory,
        jsCode,
        options,
        new FunctionRewriter(new Compiler(options)));

    FunctionRewriter rewriter = new FunctionRewriter(options);
    assertThrows(UnsupportedOperationException.class, () -> {
      rewriter.process(null, root);
    });
  }

  @Test
  public void testReturnConstant() {
    String jsCode = "function F() {return 1;}" +
        "function G() {return 'hello';}";
    Node root = ParserTestingHelp.parseAndTraverse(
        nodeFactory,
        jsCode,
        options,
        new FunctionRewriter(new Compiler(options)));

    FunctionRewriter rewriter = new FunctionRewriter(options);
    rewriter.process(null, root);

    Node expectedHelperCode = nodeFactory.parseTemplate(
        "function helper(helperValue) {" +
        " return function() {" +
        " return helperValue;" +
        "}", null);
    Node helperCode = root.getChildAt(0);
    assertThat(helperCode).isEqualTo(expectedHelperCode);

    Node expectedFunctionNodeF = nodeFactory.parseTemplate(
        "function F() {return helper(1);}", null);
    Node actualFunctionNodeF = root.getFirstChild().getLastChild();
    Node functionNodeF = actualFunctionNodeF.getFirstChild();
    assertThat(functionNodeF).isEqualTo(expectedFunctionNodeF);

    Node expectedFunctionNodeG = nodeFactory.parseTemplate(
        "function G() {return helper('hello');}", null);
    Node actualFunctionNodeG = root.getFirstChild().getPrevious().getLastChild();
    Node functionNodeG = actualFunctionNodeG.getFirstChild();
    assertThat(functionNodeG).isEqualTo(expectedFunctionNodeG);
  }
}