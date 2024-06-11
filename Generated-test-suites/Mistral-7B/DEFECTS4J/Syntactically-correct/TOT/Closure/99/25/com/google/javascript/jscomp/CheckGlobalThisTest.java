package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;
import static com.google.javascript. rhino.JSDocInfo.*;

import org.junit.Test;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.ErrorReporter;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.TestRunner;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    String jsCode = "function evil() { this.useful = undefined; }";
    ErrorReporter reporter = TestRunner.getCheckReporter();
    Node rootNode = RhinoLoader.loadJS(jsCode);

    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = new CompilerPasses()
        .setOptions(new CompilerOptions())
        .setErrorReporter(reporter)
        .addPass(new CheckGlobalThis(compiler, level))
        .compile(rootNode);

    Node functionNode = rootNode.getFirstChild();
    assertTrue("Function node is null", functionNode != null);

    JSDocInfo jsDocInfo = new CheckGlobalThis().getFunctionJsDocInfo(functionNode);
    assertNull("Function has JSDoc annotation", jsDocInfo);

    NodeTraversal traversal = new NodeTraversal(rootNode);
    NodeTraversal.Callback callback = new NodeTraversal.Callback() {
      @Override
      public void visit(Node node) {
        if (node.getType() == Token.THIS) {
          fail("Found unexpected 'this' keyword at " + NodeUtil.toString(node));
        }
      }
    };
    traversal.traverse(rootNode, callback);

    int diagnostics = reporter.getDiagnostics().size();
    assertTrue("No diagnostics expected", diagnostics > 0);
  }

  @Test
  public void testCheckGlobalThisWithJSDocAnnotation() {
    String jsCode = "/** @this {Number} */ function safe() { this.safeNumber = 5; }";
    ErrorReporter reporter = TestRunner.getCheckReporter();
    Node rootNode = RhinoLoader.loadJS(jsCode);

    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = new CompilerPasses()
        .setOptions(new CompilerOptions())
        .setErrorReporter(reporter)
        .addPass(new CheckGlobalThis(compiler, level))
        .compile(rootNode);

    Node functionNode = rootNode.getFirstChild();
    assertTrue("Function node is null", functionNode != null);

    JSDocInfo jsDocInfo = new CheckGlobalThis().getFunctionJsDocInfo(functionNode);
    assertNotNull("Function should have JSDoc annotation", jsDocInfo);
    assertEquals("Annotation should be @this", "@this {Number}", jsDocInfo.getAnnotationText());

    NodeTraversal traversal = new NodeTraversal(rootNode);
    NodeTraversal.Callback callback = new NodeTraversal.Callback() {
      @Override
      public void visit(Node node) {
        if (node.getType() == Token.THIS) {
          fail("Found unexpected 'this' keyword at " + NodeUtil.toString(node));
        }
      }
    };
    traversal.traverse(rootNode, callback);

    int diagnostics = reporter.getDiagnostics().size();
    assertTrue("No diagnostics expected", diagnostics == 0);
  }

  @Test
  public void testCheckGlobalThisWithConstructorAnnotation() {
    String jsCode = "function SafeConstructor() {}";
    ErrorReporter reporter = TestRunner.getCheckReporter();
    Node rootNode = RhinoLoader.loadJS(jsCode);

    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = new CompilerPasses()
        .setOptions(new CompilerOptions())
        .setErrorReporter(reporter)
        .addPass(new CheckGlobalThis(compiler, level))
        .compile(rootNode);

    Node constructorNode = null;
    for (Node node = rootNode.getFirstChild(); node != null; node = node.getNext()) {
      if (node.getType() == Token.FUNCTION && isConstructor(node)) {
        constructorNode = node;
      }
    }
    assertTrue("Constructor node is null", constructorNode != null);

    NodeTraversal traversal = new NodeTraversal(rootNode);
    NodeTraversal.Callback callback = new NodeTraversal.Callback() {
      @Override
      public void visit(Node node) {
        if (node.getType() == Token.THIS) {
          fail("Found unexpected 'this' keyword at " + NodeUtil.toString(node));
        }
      }
    };
    traversal.traverse(rootNode, callback);

    int diagnostics = reporter.getDiagnostics().size();
    assertTrue("No diagnostics expected", diagnostics == 0);
  }

  @Test
  public void testCheckGlobalThisWithPrototypeProperty() {
    String jsCode = "function ObjectWithPrototype() { this.prototype.useful = 5; }";
    ErrorReporter reporter = TestRunner.getCheckReporter();
    Node rootNode = RhinoLoader.loadJS(jsCode);

    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = new CompilerPasses()
        .setOptions(new CompilerOptions())
        .setErrorReporter(reporter)
        .addPass(new CheckGlobalThis(compiler, level))
        .compile(rootNode);

    Node functionNode = rootNode.getFirstChild();
    assertTrue("Function node is null", functionNode != null);

    NodeTraversal traversal = new NodeTraversal(rootNode);
    NodeTraversal.Callback callback = new NodeTraversal.Callback() {
      @Override
      public void visit(Node node) {
        if (node.getType() == Token.THIS) {
          fail("Found unexpected 'this' keyword at " + NodeUtil.toString(node));
        }
      }
    };
    traversal.traverse(rootNode, callback);

    int diagnostics = reporter.getDiagnostics().size();
    assertTrue("No diagnostics expected", diagnostics == 0);
  }

  @Test
  public void testCheckGlobalThisWithPrototypeSubProperty() {
    String jsCode = "function ObjectWithPrototype() { this.prototype.sub.useful = 5; }";
    ErrorReporter reporter = TestRunner.getCheckReporter();
    Node rootNode = RhinoLoader.loadJS(jsCode);

    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = new CompilerPasses()
        .setOptions(new CompilerOptions())
        .setErrorReporter(reporter)
        .addPass(new CheckGlobalThis(compiler, level))
        .compile(rootNode);

    Node functionNode = rootNode.getFirstChild();
    assertTrue("Function node is null", functionNode != null);

    NodeTraversal traversal = new NodeTraversal(rootNode);
    NodeTraversal.Callback callback = new NodeTraversal.Callback() {
      @Override
      public void visit(Node node) {
        if (node.getType() == Token.THIS) {
          fail("Found unexpected 'this' keyword at " + NodeUtil.toString(node));
        }
      }
    };
    traversal.traverse(rootNode, callback);

    int diagnostics = reporter.getDiagnostics().size();
    assertTrue("No diagnostics expected", diagnostics == 0);
  }

  private static boolean isConstructor(Node node) {
    JSDocInfo jsDocInfo = node.getJSDocInfo();
    return jsDocInfo != null && jsDocInfo.isConstructor();
  }
}