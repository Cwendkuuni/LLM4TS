package com.google.javascript.jscomp.tests;

import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FunctionRewriterTest {

  @Test
  public void testEmptyFunctionReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = functionRewriter.parseSyntheticCode("test",
        "function() {}");
    FunctionRewriter functionRewriterInstance = new FunctionRewriter(null);
    Node rewrittenRoot = functionRewriterInstance.process(null, root).getFirstChild();
    Node expectedHelperCode = functionRewriterInstance.parseHelperCode(EmptyFunctionReducer.class);

    assertEquals("Expected empty function to be reduced correctly",
        expectedHelperCode, rewrittenRoot);
  }

  @Test
  public void testIdentityFunctionReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = functionRewriter.parseSyntheticCode("test",
        "function(x) { return x; }");
    FunctionRewriter functionRewriterInstance = new FunctionRewriter(null);
    Node rewrittenRoot = functionRewriterInstance.process(null, root).getFirstChild();
    Node expectedHelperCode = functionRewriterInstance.parseHelperCode(IdentityReducer.class);

    assertEquals("Expected identity function to be reduced correctly",
        expectedHelperCode, rewrittenRoot);
  }

  @Test
  public void testReturnConstantReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = functionRewriter.parseSyntheticCode("test",
        "function() { return 42; }");
    FunctionRewriter functionRewriterInstance = new FunctionRewriter(null);
    Node rewrittenRoot = functionRewriterInstance.process(null, root).getFirstChild();
    Node expectedHelperCode = functionRewriterInstance.parseHelperCode(ReturnConstantReducer.class);

    assertEquals("Expected constant return function to be reduced correctly",
        expectedHelperCode, rewrittenRoot);
  }

  @Test
  public void testGetterReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = functionRewriter.parseSyntheticCode("test",
        "function getA() {return this.a_;}");
    FunctionRewriter functionRewriterInstance = new FunctionRewriter(null);
    Node rewrittenRoot = functionRewriterInstance.process(null, root).getFirstChild();
    Node expectedHelperCode = functionRewriterInstance.parseHelperCode(GetterReducer.class);

    assertEquals("Expected getter function to be reduced correctly",
        expectedHelperCode, rewrittenRoot);
  }

  @Test
  public void testSetterReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = functionRewriter.parseSyntheticCode("test",
        "function setA(newValue) { this.a_ = newValue; }");
    FunctionRewriter functionRewriterInstance = new FunctionRewriter(null);
    Node rewrittenRoot = functionRewriterInstance.process(null, root).getFirstChild();
    Node expectedHelperCode = functionRewriterInstance.parseHelperCode(SetterReducer.class);

    assertEquals("Expected setter function to be reduced correctly",
        expectedHelperCode, rewrittenRoot);
  }

  @Test
  public void testMultipleReductions() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = functionRewriter.parseSyntheticCode("test",
        "function getA() {return this.a_;}" +
        "function setA(newValue) { this.a_ = newValue; }");
    FunctionRewriter functionRewriterInstance = new FunctionRewriter(null);
    Node rewrittenRoot = functionRewriterInstance.process(null, root).getFirstChild();

    Node expectedHelperCodeGetA = functionRewriterInstance.parseHelperCode(GetterReducer.class);
    Node expectedHelperCodeSetA = functionRewriterInstance.parseHelperCode(SetterReducer.class);

    Node helperCode = rewrittenRoot;
    // Helper code should be composed of getter and setter helpers
    while (helperCode != null && !helperCode.isRoot()) {
      Node name = helperCode.getLastChild().getString();
      if (name.equals(GetterReducer.class.getName())) {
        Node expectedGetHelper = expectedHelperCodeGetA;
        assertEquals("Expected getter helper to be present",
            expectedGetHelper, helperCode);
        helperCode = helperCode.getPreviousSibling();
      } else if (name.equals(SetterReducer.class.getName())) {
        Node expectedSetHelper = expectedHelperCodeSetA;
        assertEquals("Expected setter helper to be present",
            expectedSetHelper, helperCode);
        helperCode = null;
      } else {
        helperCode = helperCode.getPreviousSibling();
      }
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = functionRewriter.parseSyntheticCode("test",
        "function(x,y) { return x + y; }");
    FunctionRewriter functionRewriterInstance = new FunctionRewriter(null);
    FunctionRewriter.process(null, root, functionRewriterInstance);
  }

  @Test(expected = IllegalStateException.class)
  public void testNullArgumentReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = functionRewriter.parseSyntheticCode("test",
        "function(x) { return null; }");
    FunctionRewriter functionRewriterInstance = new FunctionRewriter(null);
    FunctionRewriter.process(null, root, functionRewriterInstance);
  }

  @Test(expected = IllegalStateException.class)
  public void testManyArgumentsReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = functionRewriter.parseSyntheticCode("test",
        "function(x,y,z) { return x + y + z; }");
    FunctionRewriter functionRewriterInstance = new FunctionRewriter(null);
    FunctionRewriter.process(null, root, functionRewriterInstance);
  }
}