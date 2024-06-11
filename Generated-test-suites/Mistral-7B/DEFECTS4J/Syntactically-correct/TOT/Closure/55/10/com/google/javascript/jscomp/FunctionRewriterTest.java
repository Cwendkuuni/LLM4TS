package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.FunctionRewriter.SAVINGS_THRESHOLD;
import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Reducer;
import com.google.javascript.jscomp.Reduction;
import com.google.javascript.rhino.NodeTraversal;
import org.junit.Before;
import org.junit.Test;

public class FunctionRewriterTest {
  private Compiler compiler;
  private Node rootNode;
  private FunctionRewriter functionRewriter;

  @Before
  public void setup() {
    compiler = new Compiler();
    rootNode = NodeUtil.parseCode(
        "function C() {this.getA = function() { return this.a_ }; this.setA = function(newValue) { this.a_ = newValue };}", null);
    functionRewriter = new FunctionRewriter(compiler);
  }

  @Test
  public void testGetSetFunctionsRewritten() {
    functionRewriter.process(null, rootNode);

    Node getA = NodeUtil.findChildByProperty(rootNode, "C.prototype", "getA");
    Node setA = NodeUtil.findChildByProperty(rootNode, "C.prototype", "setA");

    // getA and setA should be replaced with JSCompiler_* functions
    assertThat(getA.getFirstChild().getString()).isEqualTo("getA_");
    assertThat(setA.getFirstChild().getString()).isEqualTo("setA_");

    // getA and setA should not be present in the AST after rewriting
    Node newRoot = compiler.getRootNode();
    Node getAInNewRoot = NodeUtil.findChildByProperty(newRoot, "C.prototype", "getA");
    Node setAInNewRoot = NodeUtil.findChildByProperty(newRoot, "C.prototype", "setA");

    assertThat(getAInNewRoot).isNull();
    assertThat(setAInNewRoot).isNull();
  }

  @Test
  public void testEmptyFunctionRewritten() {
    Node emptyFunction = NodeUtil.parseCode("function(){}", null);
    functionRewriter.process(null, emptyFunction);

    Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.EmptyFunctionReducer.class);
    // helperCode should be present in the AST after rewriting
    Node newRoot = compiler.getRootNode();
    Node helperCodeInNewRoot = NodeUtil.findChildByCode(newRoot, helperCode);
    assertThat(helperCodeInNewRoot).isNotNull();
  }

  @Test
  public void testIdentityFunctionRewritten() {
    Node identityFunction = NodeUtil.parseCode("function(x){return x}", null);
    functionRewriter.process(null, identityFunction);

    Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.IdentityReducer.class);
    // helperCode should be present in the AST after rewriting
    Node newRoot = compiler.getRootNode();
    Node helperCodeInNewRoot = NodeUtil.findChildByCode(newRoot, helperCode);
    assertThat(helperCodeInNewRoot).isNotNull();
  }

  @Test
  public void testFunctionWithMultipleStatementsRewritten() {
    Node multipleStatementsFunction = NodeUtil.parseCode(
        "function(x) {return x; this.x = 10}", null);
    functionRewriter.process(null, multipleStatementsFunction);

    Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.class);
    // helperCode should not be present in the AST after rewriting
    Node newRoot = compiler.getRootNode();
    Node helperCodeInNewRoot = NodeUtil.findChildByCode(newRoot, helperCode);
    assertThat(helperCodeInNewRoot).isNull();
  }

  @Test(expected = IllegalStateException.class)
  public void testFunctionWithNonConstantReturnValueRewritten() {
    Node nonConstantReturnFunction = NodeUtil.parseCode("function() {return function(){}}", null);
    functionRewriter.process(null, nonConstantReturnFunction);
    // Test should fail as non-constant return statements are not supported
  }
}